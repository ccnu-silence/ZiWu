package com.github.tinkerti.ziwu.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.tinkerti.ziwu.R;
import com.github.tinkerti.ziwu.ui.adapter.PlanAdapter;

/**
 * Created by tiankui on 4/9/17.
 */

public class PlanFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fr_rv_plan_summary_list);
        PlanAdapter planAdapter = new PlanAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(planAdapter);
        return view;
    }
}
