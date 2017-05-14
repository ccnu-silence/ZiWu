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
    private List<PlanRecordInfo> planRecordInfoList;
    private int recordType;
    private float totalRecordTime = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recordType = Constants.WEEK_TYPE;
        planRecordInfoList = RecordTask.getInstance().getPlanHistoryTime(recordType);
        totalRecordTime = RecordTask.getInstance().getPlanTotalRecordedTimeByType(recordType);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        planTypeTextView = (TextView) view.findViewById(R.id.fr_tv_plan_type_view);
        pieChart = (PieChart) view.findViewById(R.id.fr_pc_plan_record_summary);
        selectPlanType(recordType);
        drawRecordPieChart(recordType);
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
                planTypeTextView.setText(getString(R.string.plan_today));
                drawRecordPieChart(recordType);
                break;
            case Constants.WEEK_TYPE:
                planTypeTextView.setText(getString(R.string.plan_this_week));
                drawRecordPieChart(recordType);
                break;
            case Constants.LONG_TERM_TYPE:
                planTypeTextView.setText(getString(R.string.plan_long_time));
                drawRecordPieChart(recordType);
                break;
        }
    }

    private void drawRecordPieChart(int recordType) {
        //test
        int count = 0;
        List<PieEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();
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
