package com.github.tinkerti.ziwu.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.tinkerti.ziwu.R;
import com.github.tinkerti.ziwu.data.AddPlanTask;
import com.github.tinkerti.ziwu.data.model.PlanDetailInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by tiankui on 4/12/17.
 */

public class AddPlanAdapter extends RecyclerView.Adapter {

    private static final int ADD_EDIT_TYPE = 1;
    private static final int ADD_SUMMARY_TYPE = 2;
    private static final int ADD_DETAIL_TYPE = 3;

    private List<ItemModel> modelList;

    public AddPlanAdapter() {
        modelList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder itemViewHolder = null;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        switch (viewType) {
            case ADD_EDIT_TYPE:
                view = inflater.inflate(R.layout.adapter_item_add_plan_edit, parent, false);
                itemViewHolder = new AddEditViewHolder(view);
                break;
            case ADD_SUMMARY_TYPE:
                view = inflater.inflate(R.layout.adapter_item_add_plan_summary, parent, false);
                itemViewHolder = new AddSummaryViewHolder(view);
                break;
            case ADD_DETAIL_TYPE:
                view = inflater.inflate(R.layout.adapter_item_add_plan_detail, parent, false);
                itemViewHolder = new AddDetailViewHolder(view);
                break;
        }
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemModel itemModel = modelList.get(position);
        if (itemModel instanceof AddEditModel) {
            ((AddEditViewHolder) holder).update(position);
        }
        if (itemModel instanceof AddSummaryModel) {
            ((AddSummaryViewHolder) holder).update(position);
        }
        if (itemModel instanceof AddDetailModel) {
            ((AddDetailViewHolder) holder).update(position);
        }
    }

    @Override
    public int getItemCount() {
        if (modelList != null) {
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
    public abstract class ItemViewHolder extends RecyclerView.ViewHolder {

        public ItemViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void update(int position);
    }

    public class AddEditViewHolder extends ItemViewHolder {

        private EditText nameEditText;
        private TextView addButton;
        private TextView moreButton;
        private LinearLayout moreItemContainer;
        private EditText tagEditText;
        private EditText priorityEditText;
        private EditText planTimeEditText;
        private EditText planJoinParentEditText;

        public AddEditViewHolder(View itemView) {
            super(itemView);
            nameEditText = (EditText) itemView.findViewById(R.id.ad_et_add_plan_edit_name);
            addButton = (TextView) itemView.findViewById(R.id.ad_tv_add_plan_button);
            moreButton = (TextView) itemView.findViewById(R.id.ad_tv_add_plan_more_item);
            moreItemContainer = (LinearLayout) itemView.findViewById(R.id.ad_ll_add_plan_more_item_container);
            tagEditText = (EditText) itemView.findViewById(R.id.ad_et_add_plan_edit_plan_tag);
            priorityEditText = (EditText) itemView.findViewById(R.id.ad_et_add_plan_edit_priority);
            planJoinParentEditText = (EditText) itemView.findViewById(R.id.ad_et_add_plan_edit_join_parent_id);
            planTimeEditText = (EditText) itemView.findViewById(R.id.ad_et_add_plan_edit_plan_time);
        }

        @Override
        public void update(int position) {
            ItemModel itemModel = modelList.get(position);

            final PlanDetailInfo planDetailInfo = new PlanDetailInfo();
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    planDetailInfo.setPlanId(UUID.randomUUID().toString());
                    planDetailInfo.setPlanName(nameEditText.getText().toString());
                    planDetailInfo.setPlanTag(TextUtils.isEmpty(tagEditText.getText().toString()) ? null : tagEditText.getText().toString());
                    planDetailInfo.setPlanPriority(TextUtils.isEmpty(priorityEditText.getText().toString()) ? 0 : Integer.valueOf(priorityEditText.getText().toString()));
                    planDetailInfo.setPlanJoinParentId(TextUtils.isEmpty(planJoinParentEditText.getText().toString()) ? null : planJoinParentEditText.getText().toString());
                    planDetailInfo.setPlanTime(TextUtils.isEmpty(planTimeEditText.getText().toString()) ? 0 : Integer.valueOf(planTimeEditText.getText().toString()));
                    planDetailInfo.setCreateTime(System.currentTimeMillis());
                    AddPlanTask.getInstance().addPlanToDb(planDetailInfo);
                    moreItemContainer.setVisibility(View.GONE);
                    moreButton.setVisibility(View.VISIBLE);
                    nameEditText.setText("");

                    AddSummaryModel addSummaryModel = new AddSummaryModel();
                    addSummaryModel.setId(planDetailInfo.getPlanId());
                    addSummaryModel.setName(planDetailInfo.getPlanName());
                    modelList.add(0, addSummaryModel);
                    notifyDataSetChanged();

                }
            });
            moreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moreItemContainer.setVisibility(View.VISIBLE);
                    moreButton.setVisibility(View.GONE);
                }
            });


        }
    }

    public class AddSummaryViewHolder extends ItemViewHolder {
        private TextView planNameTextView;
        private TextView deleteButton;
        private View itemView;

        public AddSummaryViewHolder(View itemView) {
            super(itemView);
            deleteButton = (TextView) itemView.findViewById(R.id.ad_tv_add_plan_summary_delete);
            planNameTextView = (TextView) itemView.findViewById(R.id.ad_tv_add_plan_summary_name);
            this.itemView = itemView;
        }

        @Override
        public void update(int position) {
            final AddSummaryModel addSummaryModel = (AddSummaryModel) (modelList.get(position));
            planNameTextView.setText(addSummaryModel.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlanDetailInfo planDetailInfo = AddPlanTask.getInstance().getPlanDetailInfoById(addSummaryModel.getId());
                    AddDetailModel addDetailModel = new AddDetailModel();
                    if (planDetailInfo != null) {
                        addDetailModel.setPlanName(planDetailInfo.getPlanName());
                        addDetailModel.setPlanPriority(planDetailInfo.getPlanPriority());
                        addDetailModel.setPlanTime(planDetailInfo.getPlanTime());
                        addDetailModel.setPlanJoinParentId(planDetailInfo.getPlanJoinParentId());
                        addDetailModel.setPlanTag(planDetailInfo.getPlanTag());

                        int position = findPosition(addSummaryModel);
                        if (position >= 0) {
                            modelList.add(position + 1, addDetailModel);
                            notifyDataSetChanged();
                        }
                    }
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    public class AddDetailViewHolder extends ItemViewHolder {

        private TextView planName;
        private TextView planPriority;
        private TextView planTime;
        private TextView planJoinParentId;
        private TextView planTag;

        public AddDetailViewHolder(View itemView) {
            super(itemView);
            planName = (TextView) itemView.findViewById(R.id.ad_tv_plan_detail_name);
            planPriority = (TextView) itemView.findViewById(R.id.ad_tv_plan_detail_priority);
            planTime = (TextView) itemView.findViewById(R.id.ad_tv_plan_detail_plan_time);
            planJoinParentId = (TextView) itemView.findViewById(R.id.ad_tv_plan_detail_join_parent_id);
            planTag = (TextView) itemView.findViewById(R.id.ad_tv_plan_detail_tag);
        }

        @Override
        public void update(int position) {
            AddDetailModel addDetailModel = (AddDetailModel) modelList.get(position);
            planName.setText(addDetailModel.getPlanName());
            planPriority.setText(String.valueOf(addDetailModel.getPlanPriority()));
            planTag.setText(addDetailModel.getPlanTag());
            planTime.setText(String.valueOf(addDetailModel.getPlanTime()));
            planJoinParentId.setText(addDetailModel.getPlanJoinParentId());
        }
    }

    /**
     * 数据模型
     */

    public static abstract class ItemModel {
        private int type;
        private String id;

        public abstract int getType();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class AddEditModel extends ItemModel {

        @Override
        public int getType() {
            return ADD_EDIT_TYPE;
        }
    }

    public static class AddSummaryModel extends ItemModel {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        @Override
        public int getType() {
            return ADD_SUMMARY_TYPE;
        }
    }

    public static class AddDetailModel extends ItemModel {

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getPlanPriority() {
            return planPriority;
        }

        public void setPlanPriority(int planPriority) {
            this.planPriority = planPriority;
        }

        public long getPlanTime() {
            return planTime;
        }

        public void setPlanTime(long planTime) {
            this.planTime = planTime;
        }

        public String getPlanJoinParentId() {
            return planJoinParentId;
        }

        public void setPlanJoinParentId(String planJoinParentId) {
            this.planJoinParentId = planJoinParentId;
        }

        public String getPlanTag() {
            return planTag;
        }

        public void setPlanTag(String planTag) {
            this.planTag = planTag;
        }

        private String planName;
        private long createTime;
        private int planPriority;
        private long planTime;
        private String planJoinParentId;
        private String planTag;

        @Override
        public int getType() {
            return ADD_DETAIL_TYPE;
        }
    }

    /**
     * 数据list
     *
     * @return
     */

    public List<ItemModel> getModelList() {
        return modelList;
    }

    public void setModelList(List<ItemModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    private int findPosition(ItemModel model) {
        int position = 0;
        for (ItemModel itemModel : modelList) {
            if (itemModel.getId().equals(model.getId())) {
                return position;
            }
            position++;
        }
        return -1;
    }
}
