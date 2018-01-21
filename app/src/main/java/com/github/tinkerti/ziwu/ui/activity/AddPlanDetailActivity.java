package com.github.tinkerti.ziwu.ui.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.tinkerti.ziwu.R;
import com.github.tinkerti.ziwu.ui.adapter.AddPlanAdapter;

import java.util.ArrayList;

/**
 * Created by tiankui on 4/9/17.
 */

public class AddPlanDetailActivity extends BaseActivity {

    RecyclerView addPlanRecyclerView;
    private int type;
    private AddPlanAdapter addPlanAdapter;

    public void setSaveTaskInfoListener(SaveTaskInfoListener saveTaskInfoListener) {
        this.saveTaskInfoListener = saveTaskInfoListener;
    }

    private SaveTaskInfoListener saveTaskInfoListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        type = getIntent().getIntExtra("type", -1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan_detail);
        addPlanRecyclerView = findViewById(R.id.ac_rv_add_plan_detail);
        addPlanRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        addPlanAdapter = new AddPlanAdapter(this);
        ArrayList<AddPlanAdapter.ItemModel> modelList = new ArrayList<>();
        AddPlanAdapter.AddTaskItemModel addTaskItemModel = new AddPlanAdapter.AddTaskItemModel();
        modelList.add(addTaskItemModel);
        addPlanAdapter.setModelList(modelList);
        addPlanRecyclerView.setAdapter(addPlanAdapter);
    }

    @Override
    public void onCreateTitleBar(TitleBar titleBar) {
        View view = titleBar.onCreateTitle(R.layout.title_bar_add_plan_detail);
        TextView titleView = view.findViewById(R.id.tv_add_plan_detail_title);
        titleView.setText(getString(R.string.add_new_task));

        view.findViewById(R.id.tv_cancel_add_plan_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        view.findViewById(R.id.tv_ok_add_plan_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveTaskInfoListener != null) {
                    saveTaskInfoListener.saveTaskInfo();
                }
                finish();
            }
        });
    }

    public interface SaveTaskInfoListener {
        void saveTaskInfo();
    }

    // 点击editText外，失去焦点；
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获取当前焦点所在的控件；
            View view = getCurrentFocus();
            if (view != null && view instanceof EditText) {
                Rect r = new Rect();
                view.getGlobalVisibleRect(r);
                int rawX = (int) ev.getRawX();
                int rawY = (int) ev.getRawY();

                // 判断点击的点是否落在当前焦点所在的 view 上；
                if (!r.contains(rawX, rawY)) {
                    view.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

}
