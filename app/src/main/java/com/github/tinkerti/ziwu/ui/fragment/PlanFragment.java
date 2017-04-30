package com.github.tinkerti.ziwu.ui.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
import com.github.tinkerti.ziwu.ui.adapter.PlanAdapter;
import com.github.tinkerti.ziwu.ui.service.RecordService;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by tiankui on 4/9/17.
 */

public class PlanFragment extends Fragment {

    PlanAdapter planAdapter;
    int[] types;
    private RecordServiceConnection serviceConnection;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBindService();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fr_rv_plan_summary_list);
        planAdapter = new PlanAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        types = new int[]{Constants.DAY_TYPE, Constants.WEEK_TYPE, Constants.LONG_TERM_TYPE};
        //从数据库中查询今天的计划清单
        //周计划列表
        //从数据库中查询本周的计划清单
        //获取长期计划
        getPlanListByType(types);
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
                    //展示今日计划清单
                    planSummaryModel.setPlanName(planDetailInfo.getPlanName());
                    planSummaryModel.setPlanId(planDetailInfo.getPlanId());
                    planSummaryModel.setRecordingTime(planDetailInfo.getRecordingTime());
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
        planAdapter.setModelList(itemModelList);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPlanListByType(types);
    }

    private void initBindService(){
        Intent intent=new Intent(getContext(), RecordService.class);
        serviceConnection=new RecordServiceConnection();
        getContext().bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
    }
    public class RecordServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            RecordService.RecordServiceBinder binder=(RecordService.RecordServiceBinder)service;
            planAdapter.setBinder(binder);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
