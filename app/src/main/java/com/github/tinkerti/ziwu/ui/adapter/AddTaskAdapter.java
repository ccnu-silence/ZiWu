package com.github.tinkerti.ziwu.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.github.tinkerti.ziwu.R;
import com.github.tinkerti.ziwu.data.Consts;
import com.github.tinkerti.ziwu.data.PlanTask;
import com.github.tinkerti.ziwu.data.model.AddPlanDetailInfo;
import com.github.tinkerti.ziwu.ui.activity.AddTaskActivity;
import com.github.tinkerti.ziwu.ui.widget.AddTaskItemLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by tiankui on 4/12/17.
 */

public class AddTaskAdapter extends RecyclerView.Adapter {

    private static final int MODEL_ADD_TASK_ITEM = 4;
    private ArrayList<ItemModel> modelList;
    private Activity activity;

    public AddTaskAdapter(Activity activity) {
        modelList = new ArrayList<>();
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder itemViewHolder = null;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        switch (viewType) {
            case MODEL_ADD_TASK_ITEM:
                view = inflater.inflate(R.layout.ad_item_add_task, parent, false);
                itemViewHolder = new AddTaskItemViewHolder(view);
                break;
        }
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemModel itemModel = modelList.get(position);
        if (itemModel instanceof AddTaskItemModel) {
            ((AddTaskItemViewHolder) holder).update(position);
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

    public class AddTaskItemViewHolder extends ItemViewHolder {

        private AddTaskItemLayout nameItemView;
        private AddTaskItemLayout noteItemView;

        public AddTaskItemViewHolder(View itemView) {
            super(itemView);
            nameItemView = itemView.findViewById(R.id.atl_name_item);
            noteItemView = itemView.findViewById(R.id.atl_note_item);
        }

        @Override
        public void update(int position) {
            final AddTaskItemModel model = (AddTaskItemModel) modelList.get(position);
            if (activity instanceof AddTaskActivity) {
                AddTaskActivity addTaskActivity = (AddTaskActivity) activity;
                addTaskActivity.setSaveTaskInfoListener(new AddTaskActivity.SaveTaskInfoListener() {
                    @Override
                    public void saveTaskInfo() {
                        AddPlanDetailInfo addPlanDetailInfo = new AddPlanDetailInfo();
                        addPlanDetailInfo.setPlanId(UUID.randomUUID().toString());
                        addPlanDetailInfo.setPlanType(Consts.TYPE_IS_VALID);
                        addPlanDetailInfo.setPlanName(nameItemView.getItemContent());
                        addPlanDetailInfo.setCreateTime(System.currentTimeMillis());
                        addPlanDetailInfo.setPlanNote(noteItemView.getItemContent());
                        PlanTask.getInstance().addPlanToDb(addPlanDetailInfo);
                    }
                });
            }

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

    public static class AddTaskItemModel extends ItemModel {
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private String name;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        private String content;

        @Override
        public int getType() {
            return MODEL_ADD_TASK_ITEM;
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
