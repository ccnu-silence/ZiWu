package com.github.tinkerti.ziwu.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.tinkerti.ziwu.R;

/**
 * Created by tiankui on 7/2/17.
 */

public class DeleteConfirmDialog extends AlertDialog {
    //TODO：抽出一个baseDialog来共用。重复的代码太多了；
    Context context;
    private TextView titleView;
    private DialogClickListener listener;

    public void setListener(DialogClickListener listener) {
        this.listener = listener;
    }

    public DeleteConfirmDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_delete_confirm, null);
        TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView delete = (TextView) view.findViewById(R.id.tv_ok);
        titleView = (TextView) view.findViewById(R.id.tv_dialog_confirm_delete_plan_title);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setContentView(view);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDeleteClick();
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

    public void setTitleView(String text) {
        titleView.setText(text);
    }

    public interface DialogClickListener {
        void onDeleteClick();
    }

}
