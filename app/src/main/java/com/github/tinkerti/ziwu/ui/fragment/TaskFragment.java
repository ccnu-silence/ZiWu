package com.github.tinkerti.ziwu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.github.tinkerti.ziwu.data.SimpleResultCallback;
import com.github.tinkerti.ziwu.data.model.TaskRecordInfo;
import com.github.tinkerti.ziwu.ui.activity.SearchActivity;
import com.github.tinkerti.ziwu.ui.adapter.TaskListAdapter;
import com.github.tinkerti.ziwu.ui.utils.ZLog;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "TaskFragment";
    TaskListAdapter taskListAdapter;
    int[] types;
    private RecyclerView recyclerView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZLog.e(TAG, "onCreate");
        taskListAdapter = new TaskListAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ZLog.e(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_plan_list, container, false);
        recyclerView = view.findViewById(R.id.fr_rv_plan_summary_list);
        types = new int[]{Consts.TYPE_IS_VALID};
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(taskListAdapter);
        view.findViewById(R.id.fl_search_container).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_search_container:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }

    private void getPlanListByType(int[] types) {
        final List<TaskListAdapter.ItemModel> itemModelList = new ArrayList<>();
        for (final int type : types) {
            PlanTask.getInstance().getPlanDetailInfoByType(type, new SimpleResultCallback<List<TaskRecordInfo>>() {
                @Override
                protected void onSuccessOnUIThread(List<TaskRecordInfo> taskRecordInfoList) {
                    for (TaskRecordInfo taskRecordInfo : taskRecordInfoList) {
                        TaskListAdapter.TaskSummaryModel taskSummaryModel = new TaskListAdapter.TaskSummaryModel(taskRecordInfo);
                        itemModelList.add(taskSummaryModel);
                    }
                    taskListAdapter.setModelList(itemModelList);
                    //加上这一句代码，来刷新ui，否则的话，第一次进入app，添加计划，planList界面不刷新；
                    recyclerView.setAdapter(taskListAdapter);
                }
            });

        }
        //TODO :如果没有task时，空白页面；
    }

    @Override
    public void onResume() {
        super.onResume();
        ZLog.e(TAG, "onResume");
        //在这调用这个方法的目的是当添加新的plan之后，会回到主界面，
        // 当前fragment的生命周期不会走onCreate()和onCreateView（）,实时更新planList就需要在这添加这个方法
        getPlanListByType(types);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Consts.ADD_PLAN_REQUEST) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ZLog.e(TAG, "onDestroy");
        for (TaskListAdapter.ItemModel itemModel : taskListAdapter.getModelList()) {
            if (itemModel instanceof TaskListAdapter.TaskSummaryModel) {
                TaskListAdapter.TaskSummaryModel taskSummaryModel = (TaskListAdapter.TaskSummaryModel) itemModel;
                taskListAdapter.getUiHandler().removeCallbacks(taskSummaryModel.recordInfo.getRefreshUiRunnable());
            }
        }
    }
}
