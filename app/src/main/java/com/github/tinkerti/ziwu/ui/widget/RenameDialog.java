package com.github.tinkerti.ziwu.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.github.tinkerti.ziwu.R;
import com.github.tinkerti.ziwu.ui.utils.CommonUtils;

/**
 * Created by tiankui on 7/2/17.
 */

public class RenameDialog extends AlertDialog {
    Context context;
    private String planName;
    private EditText planNameEditText;

    public void setListener(DialogClickListener listener) {
        this.listener = listener;
    }

    private DialogClickListener listener;

    public RenameDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_rename_plan, null);
        setContentView(view);
        TextView cancelTextView = (TextView) view.findViewById(R.id.tv_cancel);
        TextView okTextView = (TextView) view.findViewById(R.id.tv_ok);
        planNameEditText = (EditText) view.findViewById(R.id.dialog_name_edit_text);

        //设置监听
        planNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                planName = s.toString();
            }
        });
        //设置editText获取和失去焦点的逻辑
        planNameEditText.setFocusable(true);
        planNameEditText.setFocusableInTouchMode(true);
        planNameEditText.requestFocus();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        CommonUtils.setEditTextFocus(planNameEditText);
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onOKClick(planName);
                }
                dismiss();
            }
        });

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = ((WindowManager) (context.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getWidth()
                - 2 * (int) context.getResources().getDimension(R.dimen.dimen_dp_40);
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(layoutParams);



    }

    public void setTextContent(String content){
        planNameEditText.setText(content);
    }

    public interface DialogClickListener {
        void onOKClick(String planName);
    }
}
