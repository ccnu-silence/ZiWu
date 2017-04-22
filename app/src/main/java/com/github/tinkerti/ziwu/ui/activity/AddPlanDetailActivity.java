package com.github.tinkerti.ziwu.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.tinkerti.ziwu.R;
import com.github.tinkerti.ziwu.data.AddPlanTask;
import com.github.tinkerti.ziwu.data.Constants;
import com.github.tinkerti.ziwu.data.model.PlanDetailInfo;
import com.github.tinkerti.ziwu.ui.adapter.AddPlanAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiankui on 4/9/17.
 */

public class AddPlanDetailActivity extends BaseActivity{

    //Todo:点击查看和收起计划详情；2、根据计划制定类型来显示计划列表；3、需要在planDetail表中添加计划类型column；
    RecyclerView addPlanRecyclerView;
    private int type;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        type=getIntent().getIntExtra("type",-1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan_detail);
        addPlanRecyclerView=(RecyclerView)findViewById(R.id.ac_rv_add_plan_detail);
        addPlanRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        AddPlanAdapter addPlanAdapter=new AddPlanAdapter();
        List<AddPlanAdapter.ItemModel> modelList=new ArrayList<>();
        List<PlanDetailInfo> planDetailInfoList= AddPlanTask.getInstance().getPlanDetailInfo() ;
        for(PlanDetailInfo planDetailInfo:planDetailInfoList){
            AddPlanAdapter.AddSummaryModel addSummaryModel=new AddPlanAdapter.AddSummaryModel();
            addSummaryModel.setName(planDetailInfo.getPlanName());
            addSummaryModel.setId(planDetailInfo.getPlanId());
            modelList.add(addSummaryModel);
        }
        AddPlanAdapter.AddEditModel addEditModel=new AddPlanAdapter.AddEditModel();
        modelList.add(addEditModel);
        addPlanAdapter.setModelList(modelList);
        addPlanRecyclerView.setAdapter(addPlanAdapter);
    }

    @Override
    public void onCreateTitleBar(TitleBar titleBar) {
        View view=titleBar.onCreateTitle(R.layout.title_bar_add_plan_detail);
        TextView titleView=(TextView) view.findViewById(R.id.tv_add_plan_detail_title);
        switch (type){
            case Constants.DAY_TYPE:
                titleView.setText(getString(R.string.add_plan_today));
                break;
            case Constants.WEEK_TYPE:
                titleView.setText(getString(R.string.add_plan_this_week));
                break;
            case Constants.LONG_TERM_TYPE:
                titleView.setText(getString(R.string.add_plan_long_time));
                break;
        }

        view.findViewById(R.id.tv_cancel_add_plan_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        view.findViewById(R.id.tv_ok_add_plan_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
