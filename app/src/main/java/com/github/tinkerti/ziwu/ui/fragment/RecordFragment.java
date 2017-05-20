package com.github.tinkerti.ziwu.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.tinkerti.ziwu.R;
import com.github.tinkerti.ziwu.data.Constants;
import com.github.tinkerti.ziwu.data.RecordTask;
import com.github.tinkerti.ziwu.data.model.PlanRecordInfo;
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
        selectPlanType(type);
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
                type=Constants.DAY_TYPE;
                planTypeTextView.setText(getString(R.string.plan_today));
                drawRecordPieChart(type);
                break;
            case Constants.WEEK_TYPE:
                type=Constants.WEEK_TYPE;
                planTypeTextView.setText(getString(R.string.plan_this_week));
                drawRecordPieChart(type);
                break;
            case Constants.LONG_TERM_TYPE:
                type=Constants.LONG_TERM_TYPE;
                planTypeTextView.setText(getString(R.string.plan_long_time));
                drawRecordPieChart(type);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        selectPlanType(type);
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
        int count = 0;
        List<PieEntry> entries = new ArrayList<>();
        for (PlanRecordInfo recordInfo : planRecordInfoList) {
            float percent = recordInfo.getTimeDuration() / totalRecordTime;
            if (percent - 0 > 0.01) {
                entries.add(new PieEntry(percent * 100, recordInfo.getPlanName(), count));
            }
            count++;
        }

        PieDataSet set = new PieDataSet(entries, "Election Results");
        PieData data = new PieData(set);

        set.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    private float getTimePercent(PlanRecordInfo recordInfo, int type) {
        float timePercent = 0f;
        switch (type) {
            case Constants.DAY_TYPE:
                timePercent = recordInfo.getTimeDuration() / Constants.ONE_DAY_TOTAL_MILLI_SECS;
                break;
            case Constants.WEEK_TYPE:
                timePercent = recordInfo.getTimeDuration() / Constants.SEVEN_DAY_TOTAL_MILLIS_SECS;
                break;
            case Constants.LONG_TERM_TYPE:
                break;
        }
        return timePercent;
    }
}
