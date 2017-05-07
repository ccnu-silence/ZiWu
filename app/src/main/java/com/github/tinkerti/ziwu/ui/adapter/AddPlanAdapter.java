package com.github.tinkerti.ziwu.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tinkerti.ziwu.R;
import com.github.tinkerti.ziwu.data.AddPlanTask;
import com.github.tinkerti.ziwu.data.PlanTask;
import com.github.tinkerti.ziwu.data.model.AddPlanDetailInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by tiankui on 4/12/17.
 */

public class AddPlanAdapter extends RecyclerView.Adapter {

    private static final int MODEL_ADD_EDIT_TYPE = 1;
    private static final int MODEL_ADD_SUMMARY_TYPE = 2;
    private static final int MODEL_ADD_DETAIL_TYPE = 3;

    private ArrayList<ItemModel> modelList;

    public AddPlanAdapter() {
        modelList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder itemViewHolder = null;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        switch (viewType) {
            case MODEL_ADD_EDIT_TYPE:
                view = inflater.inflate(R.layout.adapter_item_add_plan_edit, parent, false);
                itemViewHolder = new AddEditViewHolder(view);
                break;
            case MODEL_ADD_SUMMARY_TYPE:
                view = inflater.inflate(R.layout.adapter_item_add_plan_summary, parent, false);
                itemViewHolder = new AddSummaryViewHolder(view);
                break;
            case MODEL_ADD_DETAIL_TYPE:
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

        private View itemView;
        List<EditText> editTextList;

        public AddEditViewHolder(View itemView) {
            super(itemView);
            editTextList = new ArrayList<>();
            nameEditText = (EditText) itemView.findViewById(R.id.ad_et_add_plan_edit_name);
            addButton = (TextView) itemView.findViewById(R.id.ad_tv_add_plan_button);
            moreButton = (TextView) itemView.findViewById(R.id.ad_tv_add_plan_more_item);
            moreItemContainer = (LinearLayout) itemView.findViewById(R.id.ad_ll_add_plan_more_item_container);
            tagEditText = (EditText) itemView.findViewById(R.id.ad_et_add_plan_edit_plan_tag);
            priorityEditText = (EditText) itemView.findViewById(R.id.ad_et_add_plan_edit_priority);
            planJoinParentEditText = (EditText) itemView.findViewById(R.id.ad_et_add_plan_edit_join_parent_id);
            planTimeEditText = (EditText) itemView.findViewById(R.id.ad_et_add_plan_edit_plan_time);
            this.itemView = itemView;
            editTextList.add(nameEditText);
            editTextList.add(tagEditText);
            editTextList.add(priorityEditText);
            editTextList.add(planJoinParentEditText);
            editTextList.add(planTimeEditText);
        }

        @Override
        public void update(int position) {
            final AddEditModel addEditModel = (AddEditModel) modelList.get(position);
            final AddPlanDetailInfo addPlanDetailInfo = new AddPlanDetailInfo();
            //editText 获取焦点问题
            //初始化加载的时候，itemView获取焦点
            itemView.setFocusable(true);
            itemView.setFocusableInTouchMode(true);
            itemView.requestFocus();

            //设置editText获取焦点和失去焦点；

            setEditTextFocus(editTextList);

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(nameEditText.getText())) {
                        Toast.makeText(itemView.getContext(), itemView.getContext().getString(R.string.add_plan_detail_edit_text_name_no_string), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    addPlanDetailInfo.setPlanId(UUID.randomUUID().toString());
                    addPlanDetailInfo.setPlanName(nameEditText.getText().toString());
                    addPlanDetailInfo.setPlanType(addEditModel.getPlanType());
                    addPlanDetailInfo.setPlanTag(TextUtils.isEmpty(tagEditText.getText().toString()) ? null : tagEditText.getText().toString());
                    addPlanDetailInfo.setPlanPriority(TextUtils.isEmpty(priorityEditText.getText().toString()) ? 0 : Integer.valueOf(priorityEditText.getText().toString()));
                    addPlanDetailInfo.setPlanJoinParentId(TextUtils.isEmpty(planJoinParentEditText.getText().toString()) ? null : planJoinParentEditText.getText().toString());
                    addPlanDetailInfo.setPlanTime(TextUtils.isEmpty(planTimeEditText.getText().toString()) ? 0 : Integer.valueOf(planTimeEditText.getText().toString()));
                    addPlanDetailInfo.setCreateTime(System.currentTimeMillis());
                    AddPlanTask.getInstance().addPlanToDb(addPlanDetailInfo);
                    PlanTask.getInstance().addPlanToDb(addPlanDetailInfo);
                    moreItemContainer.setVisibility(View.GONE);
                    moreButton.setVisibility(View.VISIBLE);
                    //重置所有的EditText；
                    clearText(editTextList);

                    AddSummaryModel addSummaryModel = new AddSummaryModel();
                    addSummaryModel.setId(addPlanDetailInfo.getPlanId());
                    addSummaryModel.setName(addPlanDetailInfo.getPlanName());
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
        public void update(final int position) {
            final AddSummaryModel addSummaryModel = (AddSummaryModel) (modelList.get(position));
            planNameTextView.setText(addSummaryModel.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!addSummaryModel.isShowDetail()) {
                        AddPlanDetailInfo addPlanDetailInfo = AddPlanTask.getInstance().getPlanDetailInfoById(addSummaryModel.getId());
                        AddDetailModel addDetailModel = new AddDetailModel();
                        if (addPlanDetailInfo != null) {
                            addDetailModel.setPlanName(addPlanDetailInfo.getPlanName());
                            addDetailModel.setPlanPriority(addPlanDetailInfo.getPlanPriority());
                            addDetailModel.setPlanTime(addPlanDetailInfo.getPlanTime());
                            addDetailModel.setPlanJoinParentId(addPlanDetailInfo.getPlanJoinParentId());
                            addDetailModel.setPlanTag(addPlanDetailInfo.getPlanTag());
                            modelList.add(getAdapterPosition() + 1, addDetailModel);
                            notifyDataSetChanged();
                        }
                    } else {
                        modelList.remove(position + 1);
                        notifyItemRangeRemoved(getAdapterPosition() + 1, 1);
                    }

                    addSummaryModel.setShowDetail(!addSummaryModel.isShowDetail());
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddPlanTask.getInstance().deletePlanDetailInfoById(addSummaryModel.getId());
                    modelList.remove(position);
                    if (addSummaryModel.isShowDetail()) {
                        modelList.remove(position);
                    }
                    notifyDataSetChanged();
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

        private View itemView;

        public AddDetailViewHolder(View itemView) {
            super(itemView);
            planName = (TextView) itemView.findViewById(R.id.ad_tv_add_plan_detail_name);
            planPriority = (TextView) itemView.findViewById(R.id.ad_tv_plan_detail_priority);
            planTime = (TextView) itemView.findViewById(R.id.ad_tv_plan_detail_plan_time);
            planJoinParentId = (TextView) itemView.findViewById(R.id.ad_tv_plan_detail_join_parent_id);
            planTag = (TextView) itemView.findViewById(R.id.ad_tv_plan_detail_tag);
            this.itemView = itemView;
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

        private int planType;

        @Override
        public int getType() {
            return MODEL_ADD_EDIT_TYPE;
        }

        public int getPlanType() {
            return planType;
        }

        public void setPlanType(int planType) {
            this.planType = planType;
        }
    }

    public static class AddSummaryModel extends ItemModel {

        private String name;

        public boolean isShowDetail() {
            return isShowDetail;
        }

        public void setShowDetail(boolean showDetail) {
            isShowDetail = showDetail;
        }

        private boolean isShowDetail = false;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        @Override
        public int getType() {
            return MODEL_ADD_SUMMARY_TYPE;
        }
    }

    public static class AddDetailModel extends ItemModel {
        private String planName;
        private long createTime;
        private int planPriority;
        private long planTime;
        private String planJoinParentId;
        private String planTag;
        private boolean isShow = false;

        @Override
        public int getType() {
            return MODEL_ADD_DETAIL_TYPE;
        }

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


        public boolean isShow() {
            return isShow;
        }

        public void setShow(boolean show) {
            isShow = show;
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

    public void setModelList(ArrayList<ItemModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    private void editTextGetFocus(final EditText editText) {
        editText.requestFocus();
        editText.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager manager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.showSoftInput(editText, 0);
            }
        });
        editText.setSelection(editText.getText().length());
    }

    private void editTextLoseFocus(final EditText editText) {
        editText.clearFocus();
        editText.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager manager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        });
    }

    private void setEditTextFocus(List<EditText> editTextList) {
        for (EditText editText : editTextList) {
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击editText的时候获取焦点
                    editTextGetFocus((EditText) v);
                }
            });

            //获取和失去焦点的监听
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        editTextGetFocus((EditText) v);
                    } else {
                        editTextLoseFocus((EditText) v);
                    }
                }
            });
        }

    }

    private void clearText(List<EditText> editTextList) {
        for (EditText editText : editTextList) {
            editText.setText("");
        }
    }
}
