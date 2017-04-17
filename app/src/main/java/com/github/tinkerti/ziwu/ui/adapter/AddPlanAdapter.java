package com.github.tinkerti.ziwu.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.tinkerti.ziwu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiankui on 4/12/17.
 */

public class AddPlanAdapter extends RecyclerView.Adapter{

    private static final int ADD_EDIT_TYPE=1;
    private static final int ADD_SUMMARY_TYPE=2;
    private static final int ADD_DETAIL_TYPE=3;

    private List<ItemModel> modelList;
    public AddPlanAdapter(){
        modelList=new ArrayList<>();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder itemViewHolder=null;
        LayoutInflater inflater=(LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (viewType){
            case ADD_EDIT_TYPE:
                View view=inflater.inflate(R.layout.adapter_item_add_plan_edit,parent,false);
                itemViewHolder=new AddEditViewHolder(view);
                break;
            case ADD_SUMMARY_TYPE:
                break;
            case ADD_DETAIL_TYPE:
                break;
        }
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder)holder).update(position);
    }

    @Override
    public int getItemCount() {
        if(modelList!=null){
            return modelList.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return modelList.get(position).getType();
    }


    /**
     * UI展示
     */
    public abstract class ItemViewHolder extends RecyclerView.ViewHolder{

        public ItemViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void update(int position);
    }

    public class AddEditViewHolder extends ItemViewHolder{


        public AddEditViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void update(int position) {

        }
    }

    public class AddSummaryViewHolder extends ItemViewHolder{
        public AddSummaryViewHolder(View itemView){
            super(itemView);
        }

        @Override
        public void update(int position) {

        }
    }

    public class AddDetailViewHolder extends ItemViewHolder{

        public AddDetailViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void update(int position) {

        }
    }
    /**
     * 数据模型
     */

    public static abstract class ItemModel{
        private int type;

        public abstract int getType();
    }

    public static class AddEditModel extends ItemModel{

        @Override
        public int getType() {
            return ADD_EDIT_TYPE;
        }
    }

    public static class AddSummaryModel extends ItemModel{
        @Override
        public int getType() {
            return ADD_SUMMARY_TYPE;
        }
    }

    public static class AddDetailModel extends ItemModel{
        @Override
        public int getType() {
            return ADD_DETAIL_TYPE;
        }
    }

    /**
     * 数据list
     * @return
     */

    public List<ItemModel> getModelList() {
        return modelList;
    }

    public void setModelList(List<ItemModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

}
