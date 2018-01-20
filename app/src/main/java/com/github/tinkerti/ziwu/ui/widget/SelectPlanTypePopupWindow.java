package com.github.tinkerti.ziwu.ui.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.tinkerti.ziwu.R;
import com.github.tinkerti.ziwu.data.Constants;
import com.github.tinkerti.ziwu.ui.fragment.RecordFragment;

/**
 * Created by tiankui on 5/14/17.
 */

public class SelectPlanTypePopupWindow extends PopupWindow {

    private RecordFragment recordFragment;

    public SelectPlanTypePopupWindow(Context context, final android.support.v4.app.Fragment fragment) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_window_select_plan_type, null);
        TextView todayPlanView = (TextView) view.findViewById(R.id.tv_show_today);
        TextView thisWeekPlanView = (TextView) view.findViewById(R.id.tv_show_seven_days);
        TextView longTermPlanView = (TextView) view.findViewById(R.id.tv_show_long_term);

        recordFragment = null;
        if (fragment instanceof RecordFragment) {
            recordFragment = (RecordFragment) fragment;
        }
        todayPlanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recordFragment != null) {
                    recordFragment.selectPlanType(Constants.DAY_TYPE);
                }
                dismiss();
            }
        });
        thisWeekPlanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recordFragment != null) {
                    recordFragment.selectPlanType(Constants.WEEK_TYPE);
                }
                dismiss();
            }
        });
        longTermPlanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recordFragment!=null){
                    recordFragment.selectPlanType(Constants.TYPE_IS_VALID);
                }
                dismiss();
            }
        });
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(24420863);
        setBackgroundDrawable(dw);
        update();
    }

    public void  showPopupWindow(View anchor){
        if (!isShowing()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                showAsDropDown(anchor, -60, 10);
            } else {
                showAsDropDown(anchor, 0, 10, Gravity.TOP | Gravity.LEFT);
            }
        } else {
            dismiss();
        }
    }
}
