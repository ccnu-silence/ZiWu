package com.github.tinkerti.ziwu.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.tinkerti.ziwu.R;
import com.github.tinkerti.ziwu.data.Consts;
import com.github.tinkerti.ziwu.ui.utils.DateUtils;
import com.github.tinkerti.ziwu.ui.utils.FormatTime;

import java.util.List;


public class RecordListAdapter extends RecyclerView.Adapter {

    private List<ItemModel> modelList;
    private RecordListItemClickListener listItemClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        switch (viewType) {
            case Consts.RECORD_DATE_TITLE:
                view = inflater.inflate(R.layout.adapter_record_list_item_date_title, parent, false);
                viewHolder = new RecordDateItemViewHolder(view);
                break;
            case Consts.RECORD_LIST_ITEM:
                view = inflater.inflate(R.layout.adapter_record_list_item_detail, parent, false);
                viewHolder = new RecordListItemViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemModel itemModel = modelList.get(position);
        if (itemModel instanceof RecordDateItemModel) {
            ((RecordDateItemViewHolder) holder).update(position);
        }
        if (itemModel instanceof RecordListItemModel) {
            ((RecordListItemViewHolder) holder).update(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        ItemModel itemModel = modelList.get(position);
        if (itemModel instanceof RecordDateItemModel) {
            return Consts.RECORD_DATE_TITLE;
        }
        if (itemModel instanceof RecordListItemModel) {
            return Consts.RECORD_LIST_ITEM;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (modelList == null) {
            return 0;
        }
        return modelList.size();
    }

    public abstract class ItemViewHolder extends RecyclerView.ViewHolder {


        public ItemViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void update(int position);
    }

    public class RecordDateItemViewHolder extends ItemViewHolder {
        private TextView dateNameView;

        public RecordDateItemViewHolder(View itemView) {
            super(itemView);
            dateNameView = (TextView) itemView.findViewById(R.id.tv_record_list_item_title);
        }

        @Override
        public void update(int position) {
            RecordDateItemModel recordDateItemModel = (RecordDateItemModel) modelList.get(position);
            dateNameView.setText(recordDateItemModel.getDateName());
        }
    }

    public class RecordListItemViewHolder extends ItemViewHolder {
        TextView nameView;
        TextView timeView;
        View mainView;
        TextView timeDurationView;

        public RecordListItemViewHolder(View itemView) {
            super(itemView);
            mainView = itemView;
            nameView =  itemView.findViewById(R.id.tv_record_list_item_plan_name);
            timeView = itemView.findViewById(R.id.tv_record_list_item_plan_time);
            timeDurationView =itemView.findViewById(R.id.tv_time_duration);
        }

        @Override
        public void update(int position) {
            final RecordListItemModel recordListItemModel = (RecordListItemModel) modelList.get(position);
            nameView.setText(recordListItemModel.getPlanName());
            timeView.setText(DateUtils.getFormatTime(recordListItemModel.getBeginTime(), recordListItemModel.getEndTime()));
            timeDurationView.setText(FormatTime.formatTimeToString(recordListItemModel.getTimeDuration()));
            mainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listItemClickListener != null) {
                        listItemClickListener.onClick(recordListItemModel);
                    }
                }
            });
        }
    }

    public static abstract class ItemModel {

        public abstract int getType();
    }

    public static class RecordDateItemModel extends ItemModel {
        public String getDateName() {
            return dateName;
        }

        public void setDateName(String dateName) {
            this.dateName = dateName;
        }

        private String dateName;

        @Override
        public int getType() {
            return Consts.RECORD_DATE_TITLE;
        }
    }

    public static class RecordListItemModel extends ItemModel {

        private String planId;
        private String planName;
        private long beginTime;
        private long endTime;
        private String recordId;
        private long timeDuration;

        public String getPlanId() {
            return planId;
        }

        public void setPlanId(String planId) {
            this.planId = planId;
        }

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public long getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(long beginTime) {
            this.beginTime = beginTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }


        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }


        public long getTimeDuration() {
            return timeDuration;
        }

        public void setTimeDuration(long timeDuration) {
            this.timeDuration = timeDuration;
        }

        @Override
        public int getType() {
            return Consts.RECORD_LIST_ITEM;
        }
    }

    public List<ItemModel> getModelList() {
        return modelList;
    }

    public void setModelList(List<ItemModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    public RecordListItemClickListener getListItemClickListener() {
        return listItemClickListener;
    }

    public void setListItemClickListener(RecordListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    public interface RecordListItemClickListener {
        void onClick(RecordListItemModel recordListItemModel);
    }
}
