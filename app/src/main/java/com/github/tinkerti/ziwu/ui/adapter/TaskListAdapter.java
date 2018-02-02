package com.github.tinkerti.ziwu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.tinkerti.ziwu.R;
import com.github.tinkerti.ziwu.data.Consts;
import com.github.tinkerti.ziwu.data.PlanTask;
import com.github.tinkerti.ziwu.data.RecordTask;
import com.github.tinkerti.ziwu.data.model.TaskDetailInfo;
import com.github.tinkerti.ziwu.data.model.TaskRecordInfo;
import com.github.tinkerti.ziwu.ui.activity.AddTaskActivity;
import com.github.tinkerti.ziwu.ui.utils.FormatTime;
import com.github.tinkerti.ziwu.ui.utils.ZLog;
import com.github.tinkerti.ziwu.ui.widget.DeleteConfirmDialog;
import com.github.tinkerti.ziwu.ui.widget.OptionsPopupDialog;
import com.github.tinkerti.ziwu.ui.widget.RenameDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskListAdapter extends RecyclerView.Adapter {
    //TODO:需要把planType和recordState都写成枚举类型，日志内容跟易读些；
    private static final String TAG = "TaskListAdapter";
    private static final int PLAN_CATEGORY_TYPE = 1;
    private static final int PLAN_SUMMARY_TYPE = 2;
    private static final int NO_PLAN_TYPE = 4;

    public List<ItemModel> getModelList() {
        return modelList;
    }

    List<ItemModel> modelList;


    public Handler getUiHandler() {
        return uiHandler;
    }

    private Handler uiHandler;//更新界面用；

    public Handler getWorkHandler() {
        return workHandler;
    }

    private Handler workHandler;

    public TaskListAdapter() {
        ZLog.d(TAG, "new TaskListAdapter");
        modelList = new ArrayList<>();
        HandlerThread handlerThread = new HandlerThread("ServiceWorkThread");
        //不要忘了调用start();
        handlerThread.start();
        workHandler = new Handler(handlerThread.getLooper());
        uiHandler = new Handler(Looper.getMainLooper());
    }

    public void setUiHandler(Handler uiHandler) {
        this.uiHandler = uiHandler;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        switch (viewType) {
            case PLAN_SUMMARY_TYPE:
                view = inflater.inflate(R.layout.adapter_item_plan_summary, parent, false);
                holder = new PlanSummaryItemViewHolder(view);
                break;
            case PLAN_CATEGORY_TYPE:
                view = inflater.inflate(R.layout.adapter_item_plan_category, parent, false);
                holder = new PlanCategoryItemViewHolder(view);
                break;
            case NO_PLAN_TYPE:
                view = inflater.inflate(R.layout.adapter_item_plan_no_plan, parent, false);
                holder = new NoPlanItemViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemModel itemModel = modelList.get(position);
        if (itemModel instanceof TaskSummaryModel) {
            ((PlanSummaryItemViewHolder) holder).update(position);
        }
        if (itemModel instanceof PlanCategoryModel) {
            ((PlanCategoryItemViewHolder) holder).update(position);
        }

        if (itemModel instanceof NoPlanModel) {
            ((NoPlanItemViewHolder) holder).update(position);
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
        ItemModel itemModel = modelList.get(position);
        if (itemModel instanceof TaskSummaryModel) {
            return PLAN_SUMMARY_TYPE;
        }
        if (itemModel instanceof PlanCategoryModel) {
            return PLAN_CATEGORY_TYPE;
        }
        if (itemModel instanceof NoPlanModel) {
            return NO_PLAN_TYPE;
        }
        return super.getItemViewType(position);
    }


    public abstract class ItemViewHolder extends RecyclerView.ViewHolder {

        public ItemViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void update(int position);
    }

    public class PlanSummaryItemViewHolder extends ItemViewHolder {

        private TextView taskNameTextView;
        private TextView recordingTimeTextView;
        private RelativeLayout recordContainer;
        private RelativeLayout planSummaryView;

        private TextView expandedRecordingTimeView;//点击显示记录详情时，展示已经进行的时间；
        private ImageView startButton;
        private ImageView stopButton;
        private Context context;
        private ImageView arrowImageView;
        private FrameLayout startContainer;
        private FrameLayout stopContainer;


        public PlanSummaryItemViewHolder(View itemView) {
            super(itemView);
            taskNameTextView = itemView.findViewById(R.id.ad_tv_plan_summary_name);
            recordingTimeTextView = itemView.findViewById(R.id.ad_tv_plan_summary_recording_time);
            recordContainer = itemView.findViewById(R.id.ad_rl_plan_record_container);
            planSummaryView = itemView.findViewById(R.id.ad_rl_plan_summary_view);
            expandedRecordingTimeView = itemView.findViewById(R.id.ad_tv_plan_recorded_time);
            arrowImageView = itemView.findViewById(R.id.iv_expand_arrow);
            startContainer = itemView.findViewById(R.id.fl_start_button_container);
            stopContainer = itemView.findViewById(R.id.fl_stop_button_container);
            startButton = itemView.findViewById(R.id.iv_record_start_or_pause);
            stopButton = itemView.findViewById(R.id.iv_record_stop);
            context = itemView.getContext();
        }

        @Override
        public void update(final int position) {
            final TaskSummaryModel taskSummaryModel = (TaskSummaryModel) modelList.get(position);
            taskNameTextView.setText(taskSummaryModel.recordInfo.getPlanName());

            String recordingTime = FormatTime.calculateTimeString(taskSummaryModel.recordInfo.getTimeDuration());
            recordingTimeTextView.setText(recordingTime);
            expandedRecordingTimeView.setText(recordingTime);
            ZLog.d(TAG, taskSummaryModel.recordInfo.getPlanName() + " recording time:" + recordingTime);

            //planRecordInfo对象，用来保存计划进行时间
            final TaskRecordInfo recordInfo = taskSummaryModel.recordInfo;
            if (recordInfo.getRecordState() == Consts.RECORD_STATE_PAUSE
                    || recordInfo.getRecordState() == Consts.RECORD_STATE_RECORDING) {
                if (recordInfo.isExpand()) {
                    expandedRecordingTimeView.setVisibility(View.VISIBLE);
                    recordingTimeTextView.setVisibility(View.GONE);
                } else {
                    recordingTimeTextView.setVisibility(View.VISIBLE);
                    expandedRecordingTimeView.setVisibility(View.GONE);
                }
            }
            if (recordInfo.getRecordState() == Consts.RECORD_STATE_RECORDING) {
                startButton.setImageDrawable(planSummaryView.getContext().getResources().getDrawable(R.mipmap.pause_record_icon));
            }
            //设置recordView是否显示；
            recordContainer.setVisibility(recordInfo.isExpand() ? View.VISIBLE : View.GONE);
            if (recordInfo.isExpand()) {
                arrowImageView.animate().setDuration(0).rotation(90).start();
            }
            //这个地方有点问题，需要优化下，这样做没有多大必要；
            final Runnable recordRunnable = new Runnable() {
                @Override
                public void run() {
                    if (recordInfo != null) {
                        recordingTimeTextView.setText(FormatTime.calculateTimeString(recordInfo.getTimeDuration()));
                        expandedRecordingTimeView.setText(FormatTime.calculateTimeString(recordInfo.getTimeDuration()));
                        ZLog.d(TAG, taskSummaryModel.recordInfo.getPlanName() + " (runnable)" + this + " recording time:" + FormatTime.calculateTimeString(recordInfo.getTimeDuration()));
                    }
                    uiHandler.postDelayed(this, 1000);
                }
            };
            uiHandler.removeCallbacks(recordInfo.getRefreshUiRunnable());
            ZLog.d(TAG, "remove runnable " + recordInfo.getRefreshUiRunnable());
            recordInfo.setRefreshUiRunnable(recordRunnable);
            ZLog.d(TAG, "set runnable " + recordInfo.getRefreshUiRunnable());
            setListener(position);

            //如果是正在记录则需要去更新界面，而处于idle或者stop的状态，则不去更新
            if (recordInfo.getRecordState() == Consts.RECORD_STATE_RECORDING) {
                uiHandler.postDelayed(recordInfo.getRefreshUiRunnable(), 0);
                //同时需要调用service方法来开启计时；
                startRecord(recordInfo, false);
            } else if (recordInfo.getRecordState() == Consts.RECORD_STATE_STOP
                    || recordInfo.getRecordState() == Consts.RECORD_STATE_PAUSE) {
                uiHandler.removeCallbacks(recordInfo.getRefreshUiRunnable());
            }
        }

        private void setListener(final int pos) {
            final TaskSummaryModel taskSummaryModel = (TaskSummaryModel) modelList.get(pos);
            final TaskRecordInfo recordInfo = taskSummaryModel.recordInfo;
            planSummaryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!recordInfo.isExpand()) {
                        recordContainer.setVisibility(View.VISIBLE);
                        recordingTimeTextView.setVisibility(View.GONE);
                        if (recordInfo.getRecordState() == Consts.RECORD_STATE_RECORDING
                                || recordInfo.getRecordState() == Consts.RECORD_STATE_PAUSE) {
                            expandedRecordingTimeView.setVisibility(View.VISIBLE);
                        }
                        recordInfo.setExpand(true);
                        RecordTask.getInstance().updateExpandState(recordInfo);
                        arrowImageView.animate().setDuration(200).rotation(90).start();
                    } else {
                        recordContainer.setVisibility(View.GONE);
                        if (recordInfo.getRecordState() == Consts.RECORD_STATE_RECORDING
                                || recordInfo.getRecordState() == Consts.RECORD_STATE_PAUSE) {
                            recordingTimeTextView.setVisibility(View.VISIBLE);
                        }
                        recordInfo.setExpand(false);
                        RecordTask.getInstance().updateExpandState(recordInfo);
                        arrowImageView.animate().setDuration(200).rotation(0).start();
                    }
                }
            });

            //点击开始计时，如果处于计时进行中的状态，点击暂停计时
            startContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recordInfo.getRecordState() == Consts.RECORD_STATE_STOP) {
                        startRecord(recordInfo, true);
                        uiHandler.postDelayed(recordInfo.getRefreshUiRunnable(), 1000);//点击开始更新计时view；
                        startButton.setImageDrawable(planSummaryView.getContext().getResources().getDrawable(R.mipmap.pause_record_icon));
                    } else if (recordInfo.getRecordState() == Consts.RECORD_STATE_PAUSE) {
                        startRecord(recordInfo, true);
                        uiHandler.postDelayed(recordInfo.getRefreshUiRunnable(), 1000);//点击开始更新计时view；
                        startButton.setImageDrawable(planSummaryView.getContext().getResources().getDrawable(R.mipmap.pause_record_icon));
                    } else if (recordInfo.getRecordState() == Consts.RECORD_STATE_RECORDING) {
                        startButton.setImageDrawable(planSummaryView.getContext().getResources().getDrawable(R.mipmap.start_button));
                        stopRecord(recordInfo, true);
                        uiHandler.removeCallbacks(recordInfo.getRefreshUiRunnable());
                    }
                    expandedRecordingTimeView.setVisibility(View.VISIBLE);
                    expandedRecordingTimeView.setText(FormatTime.calculateTimeString(recordInfo.getTimeDuration()));
                }
            });

            //点击结束计时
            stopContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //需要判断下记录状态，否则的话，点击stopButton会一致进行增加计时的操作；
                    if (recordInfo.getRecordState() == Consts.RECORD_STATE_RECORDING
                            || recordInfo.getRecordState() == Consts.RECORD_STATE_PAUSE) {
                        stopRecord(recordInfo, false);
                        uiHandler.removeCallbacks(recordInfo.getRefreshUiRunnable());
                    }
                    startButton.setImageDrawable(planSummaryView.getContext().getResources().getDrawable(R.mipmap.start_button));
                    recordingTimeTextView.setVisibility(View.GONE);
                    expandedRecordingTimeView.setVisibility(View.GONE);
                    recordContainer.setVisibility(View.GONE);
                    recordInfo.setExpand(false);
                    RecordTask.getInstance().updateExpandState(recordInfo);
                    arrowImageView.animate().setDuration(200).rotation(0).start();
                }
            });

            //计划item长按点击事件，可以对计划进行修改、删除操作和查看详情操作；
            planSummaryView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    OptionsPopupDialog optionsPopupDialog = new OptionsPopupDialog(context);
                    optionsPopupDialog.setListener(new OptionsPopupDialog.OptionPopupWindowClickListener() {
                        @Override
                        public void onClick(final int position) {
                            switch (position) {
                                case Consts.RENAME_PLAN:
                                    RenameDialog renameDialog = new RenameDialog(context);
                                    renameDialog.setListener(new RenameDialog.DialogClickListener() {
                                        @Override
                                        public void onOKClick(String planName) {
                                            if (!TextUtils.isEmpty(planName)) {
                                                TaskDetailInfo info = new TaskDetailInfo();
                                                info.setPlanId(taskSummaryModel.recordInfo.getPlanId());
                                                info.setPlanName(planName);
                                                PlanTask.getInstance().renamePlan(info);
                                                ((TaskSummaryModel) modelList.get(pos)).recordInfo.setPlanName(planName);
                                                //注意需要更新adapter中的pos位置的数据，这样调用notifyItemChange才会刷新
                                                //之前不刷新是因为更改的不是modelList中的数据；
                                                notifyItemChanged(pos);
                                            }
                                        }
                                    });
                                    renameDialog.show();
                                    renameDialog.setTextContent(taskSummaryModel.recordInfo.getPlanName());
                                    break;
                                case Consts.DELETE_PLAN:
                                    DeleteConfirmDialog deleteConfirmDialog = new DeleteConfirmDialog(context);
                                    deleteConfirmDialog.setListener(new DeleteConfirmDialog.DialogClickListener() {
                                        @Override
                                        public void onDeleteClick() {
                                            PlanTask.getInstance().deletePlanDetailInfoById(taskSummaryModel.recordInfo.getPlanId());
                                            RecordTask.getInstance().deleteRecordInfoByPlanId(taskSummaryModel.recordInfo.getPlanId());
                                            modelList.remove(pos);
                                            notifyDataSetChanged();
                                        }
                                    });
                                    deleteConfirmDialog.show();
                                    deleteConfirmDialog.setTitleView(taskSummaryModel.recordInfo.getPlanName());
                                    break;
                                case Consts.TRANSFER_PLAN:
                                    break;
                                case Consts.CHECK_DETAIL:
                                    break;
                            }
                        }
                    });
                    optionsPopupDialog.show();
                    return true;
                }
            });
        }
    }


    private void startRecord(final TaskRecordInfo recordInfo, boolean isNew) {
        ZLog.d(TAG, "start record:" + recordInfo.getPlanName());
        final Runnable startRecordRunnable = new Runnable() {
            @Override
            public void run() {
                recordInfo.setTimeDuration(recordInfo.getTimeDuration() + 1000);
                ZLog.d(TAG, recordInfo.getPlanName() + "(runnable)" + this + " time duration:" + recordInfo.getTimeDuration());
                recordInfo.setRecordState(Consts.RECORD_STATE_RECORDING);
                workHandler.postDelayed(this, 1000);
                recordInfo.setRecordTimeRunnable(this);
            }
        };
        workHandler.postDelayed(startRecordRunnable, 1000);
        if (isNew) {
            recordInfo.setBeginTime(System.currentTimeMillis());
            recordInfo.setRecordId(UUID.randomUUID().toString());
            recordInfo.setRecordState(Consts.RECORD_STATE_RECORDING);
            recordInfo.setEndTime(System.currentTimeMillis());
            RecordTask.getInstance().addTaskRecord(recordInfo);
        }
    }

    public void stopRecord(TaskRecordInfo recordInfo, boolean isPause) {
        if (recordInfo.getRecordState() == Consts.RECORD_STATE_RECORDING) {
            recordInfo.setEndTime(System.currentTimeMillis());
            recordInfo.setTimeDurationPerRecord(recordInfo.getEndTime() - recordInfo.getBeginTime());
            recordInfo.setRecordState(isPause ? Consts.RECORD_STATE_PAUSE : Consts.RECORD_STATE_STOP);
            RecordTask.getInstance().updateTaskRecord(recordInfo);
        } else if (recordInfo.getRecordState() == Consts.RECORD_STATE_PAUSE) {
            //当处于暂停状态时，如果点击stop按钮，需要更新数据库中的记录状态；
            recordInfo.setRecordState(isPause ? Consts.RECORD_STATE_PAUSE : Consts.RECORD_STATE_STOP);
            RecordTask.getInstance().updateRecordState(recordInfo);
        }
        recordInfo.setRecordState(isPause ? Consts.RECORD_STATE_PAUSE : Consts.RECORD_STATE_STOP);
        ZLog.d(TAG, "stop record:" + recordInfo.getPlanName());
        uiHandler.removeCallbacks(recordInfo.getRecordTimeRunnable());
        ZLog.d(TAG, "remove runnable " + recordInfo.getRecordTimeRunnable());
        if (!isPause) {
            //不是暂停的话，需要
            recordInfo.setTimeDuration(0l);
        }
    }


    public class PlanCategoryItemViewHolder extends ItemViewHolder {
        private TextView categoryTextView;

        public PlanCategoryItemViewHolder(View itemView) {
            super(itemView);
            categoryTextView = (TextView) itemView.findViewById(R.id.ad_tv_plan_category);

        }

        @Override
        public void update(int position) {
            PlanCategoryModel planCategoryModel = (PlanCategoryModel) modelList.get(position);
            switch (planCategoryModel.getPlanType()) {
                case Consts.DAY_TYPE:
                    categoryTextView.setText(categoryTextView.getContext().getString(R.string.plan_today));
                    break;
                case Consts.WEEK_TYPE:
                    categoryTextView.setText(categoryTextView.getContext().getString(R.string.plan_this_week));
                    break;
                case Consts.TYPE_IS_VALID:
                    categoryTextView.setText(categoryTextView.getContext().getString(R.string.plan_long_time));
                    break;
            }
        }
    }

    public class NoPlanItemViewHolder extends ItemViewHolder {

        View itemView;

        public NoPlanItemViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        @Override
        public void update(int position) {
            final NoPlanModel noPlanModel = (NoPlanModel) modelList.get(position);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), AddTaskActivity.class);
                    intent.putExtra("type", noPlanModel.getNoPlanType());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }


    public static abstract class ItemModel {

        public String getPlanId() {
            return planId;
        }

        private String planId;

        public abstract int getType();

        public void setPlanId(String planId) {
            this.planId = planId;
        }

    }


    public static class NoPlanModel extends ItemModel {

        private int noPlanType;

        @Override
        public int getType() {
            return NO_PLAN_TYPE;
        }

        public int getNoPlanType() {
            return noPlanType;
        }

        public void setNoPlanType(int noPlanType) {
            this.noPlanType = noPlanType;
        }
    }

    public static class PlanCategoryModel extends ItemModel {
        private int planType;

        @Override
        public int getType() {
            return PLAN_CATEGORY_TYPE;
        }

        public int getPlanType() {
            return planType;
        }

        public void setPlanType(int planType) {
            this.planType = planType;
        }
    }

    public static class TaskSummaryModel extends ItemModel {

        public TaskRecordInfo recordInfo;
        public boolean isFirstTime = true;

        public TaskSummaryModel(TaskRecordInfo taskRecordInfo) {
            setPlanId(taskRecordInfo.getPlanId());
            this.recordInfo = taskRecordInfo;
        }

        @Override
        public int getType() {
            return PLAN_SUMMARY_TYPE;
        }
    }

    public void setModelList(List<ItemModel> modelList) {
        this.modelList = modelList;
    }

    public int findPosition(TaskRecordInfo recordInfo) {
        int position = -1;
        for (ItemModel itemModel : modelList) {
            position++;
            if (recordInfo.getPlanId().equals(itemModel.getPlanId())) {
                return position;
            }
        }
        return position;
    }
}
