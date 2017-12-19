package com.github.tinkerti.ziwu.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.tinkerti.ziwu.R;
import com.github.tinkerti.ziwu.data.Constants;
import com.github.tinkerti.ziwu.data.RecordTask;
import com.github.tinkerti.ziwu.data.model.PlanRecordInfo;
import com.github.tinkerti.ziwu.ui.activity.ModifyRecordDetailActivity;
import com.github.tinkerti.ziwu.ui.adapter.RecordListAdapter;
import com.github.tinkerti.ziwu.ui.utils.DateUtils;
import com.github.tinkerti.ziwu.ui.widget.SelectPlanTypePopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiankui on 4/9/17.
 */

public class RecordFragment extends Fragment {

    private PieChart pieChart;
    private TextView planTypeTextView;
    private TextView planNoRecord;
    public int type;
    private RecyclerView recordList;
    private RecordListAdapter recordListAdapter;
    private boolean isFirstTime = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = Constants.DAY_TYPE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        planTypeTextView = (TextView) view.findViewById(R.id.fr_tv_plan_type_view);
        pieChart = (PieChart) view.findViewById(R.id.fr_pc_plan_record_summary);
        planNoRecord = (TextView) view.findViewById(R.id.fr_tv_plan_no_record);
        recordList = (RecyclerView) view.findViewById(R.id.rv_recycler_view);
        recordList.setVisibility(View.VISIBLE);
        recordListAdapter = new RecordListAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recordList.setLayoutManager(layoutManager);
        recordList.setAdapter(recordListAdapter);
        selectPlanType(type);
        recordListAdapter.setListItemClickListener(new RecordListAdapter.RecordListItemClickListener() {
            @Override
            public void onClick(RecordListAdapter.RecordListItemModel itemModel) {
                Intent intent = new Intent(getActivity(), ModifyRecordDetailActivity.class);
                intent.putExtra("planName", itemModel.getPlanName());
                intent.putExtra("planBeginTime", itemModel.getBeginTime());
                intent.putExtra("planEndTime", itemModel.getEndTime());
                startActivity(intent);
            }
        });
        planTypeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectPlanTypePopupWindow popupWindow = new SelectPlanTypePopupWindow(getActivity(), RecordFragment.this);
                popupWindow.showPopupWindow(planTypeTextView);
            }
        });
        return view;
    }

    public void selectPlanType(int recordType) {
        switch (recordType) {
            case Constants.DAY_TYPE:
                type = Constants.DAY_TYPE;
                planTypeTextView.setText(getString(R.string.plan_today));
                drawRecordPieChart(type);
                break;
            case Constants.WEEK_TYPE:
                type = Constants.WEEK_TYPE;
                planTypeTextView.setText(getString(R.string.plan_this_week));
                drawRecordPieChart(type);
                break;
            case Constants.LONG_TERM_TYPE:
                type = Constants.LONG_TERM_TYPE;
                planTypeTextView.setText(getString(R.string.plan_long_time));
                drawRecordPieChart(type);
                break;
        }
        getPlanRecordDetailList(type);
    }

    @Override
    public void onResume() {
        super.onResume();
        selectPlanType(type);
    }


    private void getPlanRecordDetailList(int recordType) {
        List<RecordListAdapter.ItemModel> itemModelList = new ArrayList<>();
        long recordFirstTime = RecordTask.getInstance().getPlanRecordStartTimeByType(recordType);
        long currentDateTime = DateUtils.getTodayMorning();
        boolean once = false;
        boolean onceBeforeToday = false;
        while (true) {
            List<PlanRecordInfo> planRecordInfoList = RecordTask.getInstance().getPlanRecordListByType(
                    currentDateTime,
                    currentDateTime + (long) Constants.ONE_DAY_TOTAL_MILLI_SECS);
            for (PlanRecordInfo planRecordInfo : planRecordInfoList) {
                if (planRecordInfo.getStartTime() > DateUtils.getTodayMorning() && !once) {
                    RecordListAdapter.RecordDateItemModel recordDateItemModel = new RecordListAdapter.RecordDateItemModel();
                    recordDateItemModel.setDateName(getString(R.string.plan_today));
                    once = true;
                    itemModelList.add(recordDateItemModel);
                }
                if (planRecordInfo.getStartTime() < DateUtils.getTodayMorning() && !onceBeforeToday) {
                    RecordListAdapter.RecordDateItemModel recordDateItemModel = new RecordListAdapter.RecordDateItemModel();
                    recordDateItemModel.setDateName(DateUtils.getDateFormat(planRecordInfo.getStartTime()));
                    onceBeforeToday = true;
                    itemModelList.add(recordDateItemModel);
                }
                RecordListAdapter.RecordListItemModel itemModel = new RecordListAdapter.RecordListItemModel();
                itemModel.setPlanName(planRecordInfo.getPlanName());
                itemModel.setBeginTime(planRecordInfo.getStartTime());
                itemModel.setEndTime(planRecordInfo.getEndTime());
                itemModelList.add(itemModel);
            }
            if (recordFirstTime > currentDateTime) {
                break;
            }
            currentDateTime = currentDateTime - (long) Constants.ONE_DAY_TOTAL_MILLI_SECS;
        }
        recordListAdapter.setModelList(itemModelList);
    }

    private void drawRecordPieChart(int recordType) {
        List<PlanRecordInfo> planRecordInfoList = RecordTask.getInstance().getPlanHistoryTime(recordType);
        float totalRecordTime = RecordTask.getInstance().getPlanTotalRecordedTimeByType(recordType);
        if (totalRecordTime - 0.0 < 0.0000000001) {
            planNoRecord.setVisibility(View.VISIBLE);
            pieChart.setVisibility(View.GONE);
            return;
        }
        planNoRecord.setVisibility(View.GONE);
        pieChart.setVisibility(View.VISIBLE);

        pieChart.setUsePercentValues(false);
        pieChart.setRotationEnabled(false);//控制是否旋转
        pieChart.getDescription().setEnabled(false);//不显示description；
//        pieChart.setExtraOffsets(35, 35, 35, 35);//控制pieChart与屏幕边框的举例？；

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);              //holeRadius 表示中间空心的半径
        pieChart.setTransparentCircleRadius(61f);  //transparentCircleRadius 表示透明圆圈的半径

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);  //是否显示legend；

        int count = 0;

        //添加数据
        List<PieEntry> entries = new ArrayList<>();
        for (PlanRecordInfo recordInfo : planRecordInfoList) {
            float percent = recordInfo.getTimeDuration() / totalRecordTime;
            if (percent - 0 > 0.01) {
//                entries.add(new PieEntry(percent * 100, recordInfo.getPlanName(), count));  //自己计算好百分比
//                entries.add(new PieEntry(recordInfo.getTimeDuration() / (3600 * 1000f), recordInfo.getPlanName(), count));//传入原始数据，可以通过 setUsePercentValues(true)来进行百分比显示；
                entries.add(new PieEntry(recordInfo.getTimeDuration() / (3600 * 1000f), recordInfo.getPlanName(), count));
            }
            count++;
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);


        pieChart.setEntryLabelColor(Color.BLACK);
//        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
//        data.setValueTextColor(Color.BLUE); //设置slice中的y值的字体颜色


        pieChart.setData(data);

        pieChart.invalidate();
    }


}
