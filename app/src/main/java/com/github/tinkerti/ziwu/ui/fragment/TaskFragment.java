package com.github.tinkerti.ziwu.ui.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.tinkerti.ziwu.R;
import com.github.tinkerti.ziwu.data.Consts;
import com.github.tinkerti.ziwu.data.PlanTask;
import com.github.tinkerti.ziwu.data.model.PlanDetailInfo;
import com.github.tinkerti.ziwu.data.model.TaskRecordInfo;
import com.github.tinkerti.ziwu.ui.adapter.TaskListAdapter;
import com.github.tinkerti.ziwu.ui.service.RecordService;
import com.github.tinkerti.ziwu.ui.utils.ZLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskFragment extends Fragment {
    private static final String TAG = "TaskFragment";
    TaskListAdapter taskListAdapter;
    int[] types;
    private RecordServiceConnection serviceConnection;
    private Map<String, TaskRecordInfo> planRecordInfoMap;
    private RecyclerView recyclerView;
    private Handler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZLog.d(TAG, "onCreate");
        planRecordInfoMap = new HashMap<>();
        taskListAdapter = new TaskListAdapter();
        handler = new Handler(Looper.getMainLooper());
        taskListAdapter.setHandler(handler);
        initBindService();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ZLog.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_plan_list, container, false);
        recyclerView = view.findViewById(R.id.fr_rv_plan_summary_list);
        types = new int[]{Consts.TYPE_IS_VALID};
        getPlanListByType(types);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(taskListAdapter);
        return view;
    }

    private void getPlanListByType(int[] types) {
        List<TaskListAdapter.ItemModel> itemModelList = new ArrayList<>();
        for (int type : types) {
            //todo:需要查询recordInfo的状态，查询出record_state 为recording的计划，再次启动界面之后需要恢复计时状态
            List<PlanDetailInfo> planList = PlanTask.getInstance().getPlanDetailInfoByType(type);
            if (planList.size() > 0) {
                for (PlanDetailInfo planDetailInfo : planList) {
                    TaskListAdapter.PlanSummaryModel planSummaryModel = new TaskListAdapter.PlanSummaryModel();
                    //从数据库中查询该任务最后一条记录的状态，若是RECORD_STATE_PAUSE，那么需要加上前面连续都是RECORD_STATE_PAUSE的时间

                    TaskRecordInfo taskRecordInfo = planRecordInfoMap.get(planDetailInfo.getPlanId());
                    if (taskRecordInfo == null) {
                        taskRecordInfo = new TaskRecordInfo();
                        planRecordInfoMap.put(planDetailInfo.getPlanId(), taskRecordInfo);
                    }
                    planSummaryModel.setPlanName(planDetailInfo.getPlanName());
                    planSummaryModel.setPlanId(planDetailInfo.getPlanId());
                    planSummaryModel.setRecordInfo(taskRecordInfo);
                    planSummaryModel.setPlanType(type);
                    itemModelList.add(planSummaryModel);
                }
            } else {
                //展示暂无计划
                TaskListAdapter.NoPlanModel noPlanModel = new TaskListAdapter.NoPlanModel();
                noPlanModel.setNoPlanType(type);
                itemModelList.add(noPlanModel);
            }
        }
        //setModelList()中没有进行notifyDateSetChanged的原因，是因为着这样做的话计时会有问题
        taskListAdapter.setModelList(itemModelList);
        //加上这一句代码，来刷新ui，否则的话，第一次进入app，添加计划，planList界面不刷新；
        recyclerView.setAdapter(taskListAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ZLog.d(TAG, "onResume");
        //在这调用这个方法的目的是当添加新的plan之后，会回到主界面，
        // 当前fragment的生命周期不会走onCreate()和onCreateView（）,实时更新planList就需要在这添加这个方法
        getPlanListByType(types);
    }

    private void initBindService() {
        ZLog.d(TAG, "initBindService");
        Intent intent = new Intent(getContext(), RecordService.class);
        serviceConnection = new RecordServiceConnection();
        getContext().startService(intent);
        getContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public class RecordServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ZLog.d(TAG, "onServiceConnected: record service connected");
            RecordService.RecordServiceBinder binder = (RecordService.RecordServiceBinder) service;
            taskListAdapter.setBinder(binder);
            planRecordInfoMap = binder.getRecordInfoHashMap();
            getPlanListByType(types);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            ZLog.d(TAG, "onServiceDisconnected: record service disconnected");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Consts.ADD_PLAN_REQUEST) {

        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ZLog.e(TAG, "onDestroy");
        getContext().unbindService(serviceConnection);
    }
}
