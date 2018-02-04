package com.github.tinkerti.ziwu.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.tinkerti.ziwu.R;
import com.github.tinkerti.ziwu.data.Consts;
import com.github.tinkerti.ziwu.data.RecordTask;
import com.github.tinkerti.ziwu.data.model.TaskRecordInfo;
import com.github.tinkerti.ziwu.ui.utils.FormatTime;

import java.util.ArrayList;
import java.util.List;

public class RecordChartActivity extends BaseActivity {

    private PieChart pieChart;
    private TextView totalRecordTimeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNameText(getString(R.string.record_week_chart_title));
        setContentView(R.layout.activity_record_chart);

        pieChart = findViewById(R.id.pc_plan_record_pie);
        totalRecordTimeView = findViewById(R.id.tv_total_record_time);
        drawRecordPieChart(Consts.WEEK_TYPE);
    }

    private void drawRecordPieChart(int recordType) {
        List<TaskRecordInfo> taskRecordInfoList = RecordTask.getInstance().getPlanHistoryTime(recordType);
        long totalRecordTime = RecordTask.getInstance().getPlanTotalRecordedTimeByType(recordType);
        totalRecordTimeView.setText(FormatTime.formatTimeToUnitString(totalRecordTime));
        float floatTotalRecordTime = totalRecordTime;
        if (floatTotalRecordTime - 0.0 < 0.0000000001) {
            pieChart.setVisibility(View.GONE);
            return;
        }
        pieChart.setVisibility(View.VISIBLE);
        pieChart.setUsePercentValues(false);
        pieChart.setRotationEnabled(false);//控制是否旋转
        pieChart.getDescription().setEnabled(false);//不显示description；
//        pieChart.setExtraOffsets(35, 35, 35, 35);//控制pieChart与屏幕边框的举例？；

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);              //holeRadius 表示中间空心的半径
        pieChart.setTransparentCircleRadius(58f);  //transparentCircleRadius 表示透明圆圈的半径

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);  //是否显示legend；

        int count = 0;
        //添加数据
        List<PieEntry> entries = new ArrayList<>();
        for (TaskRecordInfo recordInfo : taskRecordInfoList) {
            float percent = recordInfo.getTimeDuration() / floatTotalRecordTime;
            if (percent - 0 > 0.01) {
//                entries.add(new PieEntry(percent * 100, recordInfo.getPlanName(), count));  //自己计算好百分比
//                entries.add(new PieEntry(recordInfo.getTimeDuration() / (3600 * 1000f), recordInfo.getPlanName(), count));//传入原始数据，可以通过 setUsePercentValues(true)来进行百分比显示；
//                entries.add(new PieEntry(recordInfo.getTimeDuration() / (3600 * 1000f), recordInfo.getPlanName(), count));
                //通过构造不同的PieEntry，可以来标注和说明pie的扇形区的含义；
                entries.add(new PieEntry(recordInfo.getTimeDuration() / (3600 * 1000f)));
            }
            count++;
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(0f);//设置不同扇形区域之间的间隔，0的话没有间隔；
        dataSet.setSelectionShift(5f);//设置点击扇形时，扇形向外变大的距离；
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        //设置扇形区域的标签
//        dataSet.setValueLinePart1OffsetPercentage(80.f);
//        dataSet.setValueLinePart1Length(0.2f);
//        dataSet.setValueLinePart2Length(0.4f);
//        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        dataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);


        pieChart.setEntryLabelColor(Color.BLACK);
//        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
//        data.setValueTextColor(Color.BLUE); //设置slice中的y值的字体颜色
        pieChart.setData(data);
        pieChart.invalidate();
    }
}
