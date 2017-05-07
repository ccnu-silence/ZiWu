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
import com.github.tinkerti.ziwu.data.Constants;
import com.github.tinkerti.ziwu.data.PlanTask;
import com.github.tinkerti.ziwu.data.model.PlanDetailInfo;
import com.github.tinkerti.ziwu.data.model.PlanRecordInfo;
import com.github.tinkerti.ziwu.ui.adapter.PlanAdapter;
import com.github.tinkerti.ziwu.ui.service.RecordService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by tiankui on 4/9/17.
 */

public class PlanFragment extends Fragment {
    //Todo:有个问题，有一定概率你添加计划清单之后没有及时的在计划列表中进行展示；可能跟数据库的异步操作有关？
    PlanAdapter planAdapter;
    int[] types;
    private RecordServiceConnection serviceConnection;
    private Map<String, PlanRecordInfo> planRecordInfoMap;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        planRecordInfoMap = new HashMap<>();
        planAdapter = new PlanAdapter();
        planAdapter.setHandler(new Handler(Looper.getMainLooper()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initBindService();
        View view = inflater.inflate(R.layout.fragment_plan_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.fr_rv_plan_summary_list);
        types = new int[]{Constants.DAY_TYPE, Constants.WEEK_TYPE, Constants.LONG_TERM_TYPE};
        //从数据库中查询今天的计划清单
        //周计划列表
        //从数据库中查询本周的计划清单
        //获取长期计划
        getPlanListByType(types);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(planAdapter);
        return view;
    }

    private void getPlanListByType(int[] types) {
        List<PlanAdapter.ItemModel> itemModelList = new ArrayList<>();
        for (int type : types) {
            PlanAdapter.PlanCategoryModel planCategoryModel = new PlanAdapter.PlanCategoryModel();
            planCategoryModel.setPlanType(type);  //根据类型来显示；
            itemModelList.add(planCategoryModel);
            List<PlanDetailInfo> planList = PlanTask.getInstance().getPlanDetailInfoByType(type);
            if (planList.size() > 0) {
                for (PlanDetailInfo planDetailInfo : planList) {
                    PlanAdapter.PlanSummaryModel planSummaryModel = new PlanAdapter.PlanSummaryModel();
                    PlanRecordInfo planRecordInfo = planRecordInfoMap.get(planDetailInfo.getPlanId());
                    if (planRecordInfo == null) {
                        planRecordInfo = new PlanRecordInfo();
                        planRecordInfoMap.put(planDetailInfo.getPlanId(), planRecordInfo);
                    }
                    //展示今日计划清单
                    planSummaryModel.setPlanName(planDetailInfo.getPlanName());
                    planSummaryModel.setPlanId(planDetailInfo.getPlanId());
                    planSummaryModel.setRecordInfo(planRecordInfo);
                    planSummaryModel.setPlanType(type);
                    itemModelList.add(planSummaryModel);
                }
            } else {
                //展示暂无计划
                PlanAdapter.NoPlanModel noPlanModel = new PlanAdapter.NoPlanModel();
                noPlanModel.setNoPlanType(type);
                itemModelList.add(noPlanModel);

            }
        }
        //setModelList()中没有进行notifyDateSetChanged的原因，是因为着这样做的话计时会有问题
        planAdapter.setModelList(itemModelList);
        //加上这一句代码，来刷新ui，否则的话，第一次进入app，添加计划，planList界面不刷新；
        recyclerView.setAdapter(planAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        //在这调用这个方法的目的是当添加新的plan之后，会回到主界面，
        // 当前fragment的生命周期不会走onCreate()和onCreateView（）,实时更新planList就需要在这添加这个方法
        getPlanListByType(types);
    }

    private void initBindService() {
        Intent intent = new Intent(getContext(), RecordService.class);
        serviceConnection = new RecordServiceConnection();
        getContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public class RecordServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            RecordService.RecordServiceBinder binder = (RecordService.RecordServiceBinder) service;
            planAdapter.setBinder(binder);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.ADD_PLAN_REQUEST) {

        }
    }
}
