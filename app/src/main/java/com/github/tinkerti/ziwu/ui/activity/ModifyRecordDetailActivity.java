package com.github.tinkerti.ziwu.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.github.tinkerti.ziwu.R;

public class ModifyRecordDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_plan_record_time);
        String recordId = getIntent().getStringExtra("recordId");
    }


    @Override
    public void onCreateTitleBar(TitleBar titleBar) {
        View view = titleBar.onCreateTitle(R.layout.action_bar_modify_plan_record_time);
        TextView cancelView = view.findViewById(R.id.tv_cancel_modify);
        TextView saveView = view.findViewById(R.id.tv_save_modify);
        TextView nameView = view.findViewById(R.id.tv_plan_name);
    }
}
