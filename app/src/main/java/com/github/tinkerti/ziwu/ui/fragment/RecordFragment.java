package com.github.tinkerti.ziwu.ui.fragment;

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
import com.github.tinkerti.ziwu.R;
import com.github.tinkerti.ziwu.data.Consts;
import com.github.tinkerti.ziwu.data.RecordTask;
import com.github.tinkerti.ziwu.data.SimpleResultCallback;
import com.github.tinkerti.ziwu.data.model.TaskRecordInfo;
import com.github.tinkerti.ziwu.ui.adapter.RecordListAdapter;
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
        type = Consts.TYPE_ALL;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        planTypeTextView = view.findViewById(R.id.fr_tv_plan_type_view);
        pieChart = view.findViewById(R.id.fr_pc_plan_record_summary);
        planNoRecord = view.findViewById(R.id.fr_tv_plan_no_record);
        recordList = view.findViewById(R.id.rv_recycler_view);
        recordList.setVisibility(View.VISIBLE);
        recordListAdapter = new RecordListAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recordList.setLayoutManager(layoutManager);
        recordList.setAdapter(recordListAdapter);

        getPlanRecordDetailList(type, 0);
        recordListAdapter.setListItemClickListener(new RecordListAdapter.RecordListItemClickListener() {
            @Override
            public void onClick(RecordListAdapter.RecordListItemModel itemModel) {
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

    @Override
    public void onResume() {
        super.onResume();
        getPlanRecordDetailList(type, 0);
    }

    public void getPlanRecordDetailList(final int recordType, int offset) {
        final List<RecordListAdapter.ItemModel> itemModelList = new ArrayList<>();
        RecordTask.getInstance().getRecordList(recordType, offset, new SimpleResultCallback<List<List<TaskRecordInfo>>>() {
            @Override
            protected void onSuccessOnUIThread(List<List<TaskRecordInfo>> monthRecordList) {
                for (List<TaskRecordInfo> list : monthRecordList) {
                    RecordListAdapter.RecordDateItemModel monthTitleModel = new RecordListAdapter.RecordDateItemModel();
                    monthTitleModel.setDateName("本月");
                    itemModelList.add(monthTitleModel);
                    for (TaskRecordInfo recordInfo : list) {
                        RecordListAdapter.RecordListItemModel recordListItemModel = new RecordListAdapter.RecordListItemModel(recordInfo);
                        itemModelList.add(recordListItemModel);
                    }
                }
                recordListAdapter.setModelList(itemModelList);
            }
        });
    }

}
