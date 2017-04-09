package com.github.tinkerti.ziwu.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.github.tinkerti.ziwu.R;

/**
 * Created by tiankui on 4/9/17.
 */

public class AddPlanButton extends ImageButton {

    public AddPlanButton(Context context) {
        super(context);
    }

    public AddPlanButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray=context.getTheme().obtainStyledAttributes(attrs, R.styleable.AddImageButton,0,0);
        Drawable backgroundDrawable=typedArray.getDrawable(R.styleable.AddImageButton_AddButtonBackground);
        Drawable imageDrawable=typedArray.getDrawable(R.styleable.AddImageButton_AddButtonImageSrc);
        setImageDrawable(imageDrawable);
        setBackground(backgroundDrawable);

        typedArray.recycle();
    }
}
