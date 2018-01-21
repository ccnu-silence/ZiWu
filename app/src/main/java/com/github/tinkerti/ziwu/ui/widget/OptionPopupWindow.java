package com.github.tinkerti.ziwu.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.github.tinkerti.ziwu.R;
import com.github.tinkerti.ziwu.ui.activity.AddPlanDetailActivity;

/**
 * Created by tiankui on 1/20/18.
 */

public class OptionPopupWindow extends PopupWindow {

    public OptionPopupWindow(final Context context) {
        super(context);  //第一次写，忘记加这句话就会导致UI不展示;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View content = layoutInflater.inflate(R.layout.widget_option_popup_window, null);
        setContentView(content);
        content.findViewById(R.id.omi_add_new_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddPlanDetailActivity.class);
                context.startActivity(intent);
                dismiss();
            }
        });
        setBackgroundDrawable(context.getResources().getDrawable(R.color.color_transparent));//设置背景颜色为透明；
        setOutsideTouchable(true); //这个设置：点击popupWindow外，可隐藏popupWindow；

    }
}
