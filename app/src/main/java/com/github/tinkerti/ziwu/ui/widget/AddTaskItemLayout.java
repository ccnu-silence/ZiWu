package com.github.tinkerti.ziwu.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.tinkerti.ziwu.R;

/**
 * Created by tiankui on 1/21/18.
 */

public class AddTaskItemLayout extends LinearLayout {

    private EditText contentView;

    public AddTaskItemLayout(Context context) {
        this(context, null);
    }

    public AddTaskItemLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.wi_add_task_item_layout, this);
        TextView nameTagView = findViewById(R.id.tv_item_name);
        contentView = findViewById(R.id.et_item_content);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AddTaskItemLayout);
            CharSequence nameTag = array.getText(R.styleable.AddTaskItemLayout_nameTag);
            nameTagView.setText(nameTag);
        }
    }

    public String getItemContent() {
        return contentView.getText().toString();
    }
}
