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
 * Created by tiankui on 4/9/17.
 */

public class TabIndicatorItemView extends LinearLayout {

    TextView indicatorTextView;
    private ImageView indicatorImageView;

    public TabIndicatorItemView(Context context) {
        super(context);
        initView(null);
    }

    public TabIndicatorItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        setOrientation(VERTICAL);
        inflate(getContext(), R.layout.wi_bottom_indicator_item, this);
        indicatorTextView = (TextView) findViewById(R.id.tab_tv_indicator_text);
        indicatorImageView = (ImageView) findViewById(R.id.tab_iv_indicator_image);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TabIndicatorItemView, 0, 0);
        Drawable imageDrawable = typedArray.getDrawable(R.styleable.TabIndicatorItemView_IndicatorButton);
        String text = typedArray.getString(R.styleable.TabIndicatorItemView_IndicatorText);
        indicatorTextView.setText(text);
        if (imageDrawable != null) {
            indicatorImageView.setImageDrawable(imageDrawable);
        }
    }

    @Override
    public void setSelected(boolean selected) {
        indicatorImageView.setSelected(selected);
        if (selected) {
            indicatorTextView.setTextColor(getResources().getColor(R.color.baseColor));
        } else {
            indicatorTextView.setTextColor(getResources().getColor(R.color.blackColor));
        }
    }
}
