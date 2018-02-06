package com.github.tinkerti.ziwu.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.tinkerti.ziwu.R;
import com.github.tinkerti.ziwu.data.model.TaskRecordInfo;
import com.github.tinkerti.ziwu.ui.utils.FormatTime;

import java.util.List;


public class RecordSummaryView extends LinearLayout {

    private TextView titleView;
    private List<TaskRecordInfo> recordInfoList;
    private RecordListAdapter recordListAdapter;
    private ListView recordListView;

    public RecordSummaryView(Context context) {
        this(context, null);
    }

    public RecordSummaryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.widget_record_summary_view, this);
        titleView = findViewById(R.id.tv_record_summary_title);
        recordListView = findViewById(R.id.lv_record_item_list);
        recordListAdapter = new RecordListAdapter();
        recordListView.setAdapter(recordListAdapter);
    }

    private void setListViewHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        int height = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            height += listItem.getMeasuredHeight();
        }
        LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.height = height;
        listView.setLayoutParams(params);
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setList(List<TaskRecordInfo> recordInfoList) {
        this.recordInfoList = recordInfoList;
        recordListAdapter.notifyDataSetChanged();
        setListViewHeight(recordListView);
    }

    private class RecordListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (recordInfoList == null) {
                return 0;
            }
            return recordInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            if (recordInfoList == null || recordInfoList.size() == 0) {
                return null;
            }
            return recordInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TaskRecordInfo recordInfo = recordInfoList.get(position);
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_item_record_summary, null);
            ImageView orderIcon = convertView.findViewById(R.id.iv_order_icon);
            TextView nameView = convertView.findViewById(R.id.tv_record_name);
            TextView timeView = convertView.findViewById(R.id.tv_record_time_duration);
            nameView.setText(recordInfo.getPlanName());
            timeView.setText(FormatTime.formatTimeToUnitString(recordInfo.getTimeDuration()));
            return convertView;
        }
    }
}
