package com.github.tinkerti.ziwu.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.tinkerti.ziwu.R;
import com.github.tinkerti.ziwu.data.Constants;

import java.util.List;

/**
 * Created by tiankui on 9/16/17.
 */

public class RecordListAdapter extends RecyclerView.Adapter {

    List<ItemModel> modelList;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        switch (viewType) {
            case Constants.RECORD_DATE_TITLE:
                view = inflater.inflate(R.layout.adapter_record_list_item_date_title, parent, false);
                viewHolder = new RecordDateItemViewHolder(view);
                break;
            case Constants.RECORD_LIST_ITEM:
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
            return Constants.RECORD_DATE_TITLE;
        }
        if (itemModel instanceof RecordListItemModel) {
            return Constants.RECORD_LIST_ITEM;
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

        public RecordDateItemViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void update(int position) {

        }
    }

    public class RecordListItemViewHolder extends ItemViewHolder {
        TextView nameView;
        TextView timeView;
        View mainView;

        public RecordListItemViewHolder(View itemView) {
            super(itemView);
            mainView = itemView;
            nameView = (TextView) itemView.findViewById(R.id.tv_record_list_item_plan_name);
            timeView = (TextView) itemView.findViewById(R.id.tv_record_list_item_plan_time);
        }

        @Override
        public void update(int position) {
            RecordListItemModel recordListItemModel = (RecordListItemModel) modelList.get(position);
            nameView.setText(recordListItemModel.getPlanName());
            timeView.setText(String.valueOf(recordListItemModel.getBeginTime()));
        }
    }

    public static abstract class ItemModel {

        public abstract int getType();
    }

    public static class RecordDateItemModel extends ItemModel {

        @Override
        public int getType() {
            return Constants.RECORD_DATE_TITLE;
        }
    }

    public static class RecordListItemModel extends ItemModel {

        private String planId;
        private String planName;
        private long beginTime;
        private long endTime;

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

        @Override
        public int getType() {
            return Constants.RECORD_LIST_ITEM;
        }
    }

    public List<ItemModel> getModelList() {
        return modelList;
    }

    public void setModelList(List<ItemModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }
}
