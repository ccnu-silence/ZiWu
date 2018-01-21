package com.github.tinkerti.ziwu.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.tinkerti.ziwu.R;

/**
 * Created by tiankui on 1/20/18.
 */

public class OptionMenuItemLayout extends LinearLayout {
    public OptionMenuItemLayout(Context context) {
        this(context, null);
    }


    public OptionMenuItemLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        inflate(getContext(), R.layout.wi_option_menu_item, this);
        ImageView preIcon = findViewById(R.id.iv_pre_icon);
        TextView content = findViewById(R.id.tv_text_content);
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.OptionMenuItemLayout);
            CharSequence text = array.getText(R.styleable.OptionMenuItemLayout_content);
            Drawable imageDrawable = array.getDrawable(R.styleable.OptionMenuItemLayout_preIcon);
            preIcon.setImageDrawable(imageDrawable);
            content.setText(text);
        }
    }
}
