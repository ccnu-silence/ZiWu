package com.github.tinkerti.ziwu.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.tinkerti.ziwu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiankui on 7/2/17.
 */

public class OptionsPopupDialog extends AlertDialog {
    Context context;
    OptionPopupWindowClickListener listener;

    public OptionsPopupDialog(Context context) {
        super(context);
        this.context = context;
    }


    @Override
    protected void onStart() {
        super.onStart();
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog_option, null);
        ListView listView = (ListView) view.findViewById(R.id.dialog_option_list_view);
        List<String> listData = new ArrayList<>();
        listData.add(context.getResources().getString(R.string.dialog_rename_plan));
        listData.add(context.getResources().getString(R.string.dialog_delete_plan));
        listData.add(context.getString(R.string.dialog_transfer_plan));
        listData.add(context.getString(R.string.dialog_check_plan_detail));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.dialog_option_content_item, R.id.dialog_text_view_item, listData);
        listView.setAdapter(arrayAdapter);
        setContentView(view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onClick(position);
                }
                dismiss();
            }
        });
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = getWidth();
        getWindow().setAttributes(layoutParams);
    }

    private int getWidth() {
        return (int) (((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth() - 2 * context.getResources().getDimension(R.dimen.dimen_dp_40));
    }


    public OptionPopupWindowClickListener getListener() {
        return listener;
    }

    public void setListener(OptionPopupWindowClickListener listener) {
        this.listener = listener;
    }

    public interface OptionPopupWindowClickListener {
        void onClick(int position);
    }
}
