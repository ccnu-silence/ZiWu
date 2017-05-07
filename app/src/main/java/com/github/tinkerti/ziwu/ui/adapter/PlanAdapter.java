package com.github.tinkerti.ziwu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.tinkerti.ziwu.R;
import com.github.tinkerti.ziwu.data.Constants;
import com.github.tinkerti.ziwu.data.RecordTask;
import com.github.tinkerti.ziwu.data.model.PlanRecordInfo;
import com.github.tinkerti.ziwu.ui.activity.AddPlanDetailActivity;
import com.github.tinkerti.ziwu.ui.service.RecordService;
import com.github.tinkerti.ziwu.ui.utils.FormatTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiankui on 4/23/17.
 */

public class PlanAdapter extends RecyclerView.Adapter {
    private static final int PLAN_CATEGORY_TYPE = 1;
    private static final int PLAN_SUMMARY_TYPE = 2;
    private static final int NO_PLAN_TYPE = 4;
    private static final int PLAN_RECORD_TYPE = 3;
    List<ItemModel> modelList;
    private RecordService.RecordServiceBinder binder;

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    private Handler handler;//更新界面用；

    public PlanAdapter() {
        modelList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
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
            case PLAN_RECORD_TYPE:
                view = inflater.inflate(R.layout.adapter_item_plan_record, parent, false);
                holder = new PlanRecordItemViewHolder(view);
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemModel itemModel = modelList.get(position);
        if (itemModel instanceof PlanSummaryModel) {
            ((PlanSummaryItemViewHolder) holder).update(position);
        }
        if (itemModel instanceof PlanCategoryModel) {
            ((PlanCategoryItemViewHolder) holder).update(position);
        }

        if (itemModel instanceof PlanRecordModel) {
            ((PlanRecordItemViewHolder) holder).update(position);
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
        if (itemModel instanceof PlanSummaryModel) {
            return PLAN_SUMMARY_TYPE;
        }
        if (itemModel instanceof PlanCategoryModel) {
            return PLAN_CATEGORY_TYPE;
        }
        if (itemModel instanceof PlanRecordModel) {
            return PLAN_RECORD_TYPE;
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

        private TextView planNameTextView;
        private TextView recordingTimeTextView;
        private View itemView;
        private RelativeLayout recordContainer;
        private RelativeLayout planSummaryView;

        private TextView detailRecordedTimeView;//点击显示记录详情时，展示已经进行的时间；
        private TextView startButton;
        private TextView stopButton;
        private TextView finishButton;
        private Context context;


        public PlanSummaryItemViewHolder(View itemView) {
            super(itemView);
            planNameTextView = (TextView) itemView.findViewById(R.id.ad_tv_plan_summary_name);
            recordingTimeTextView = (TextView) itemView.findViewById(R.id.ad_tv_plan_summary_recording_time);
            recordContainer = (RelativeLayout) itemView.findViewById(R.id.ad_rl_plan_record_container);
            planSummaryView = (RelativeLayout) itemView.findViewById(R.id.ad_rl_plan_summary_view);
            this.itemView = itemView;
            detailRecordedTimeView = (TextView) itemView.findViewById(R.id.ad_tv_plan_recorded_time);
            startButton = (TextView) itemView.findViewById(R.id.ad_tv_plan_record_begin);
            stopButton = (TextView) itemView.findViewById(R.id.ad_tv_plan_record_stop);
            finishButton = (TextView) itemView.findViewById(R.id.ad_tv_plan_record_finish);
            context = itemView.getContext();
        }

        @Override
        public void update(final int position) {
            final PlanSummaryModel planSummaryModel = (PlanSummaryModel) modelList.get(position);
            planNameTextView.setText(planSummaryModel.getPlanName());
            recordingTimeTextView.setText(FormatTime.calculateTimeString(planSummaryModel.getRecordInfo().getTimeDuration()));
            //planRecordInfo对象，用来保存计划进行时间
            final PlanRecordInfo recordInfo = planSummaryModel.getRecordInfo();
            recordInfo.setPlanId(planSummaryModel.getPlanId());
            //设置recordView是否显示；
            recordContainer.setVisibility(recordInfo.isExpand() ? View.VISIBLE : View.GONE);

            long totalRecordTime = RecordTask.getInstance().getPlanTotalRecordedTime(recordInfo, planSummaryModel.getPlanType());

            detailRecordedTimeView.setText(context.getString(R.string.ad_item_plan_recorded_time, planSummaryModel.getPlanName(), FormatTime.calculateTimeString(totalRecordTime)));
            planSummaryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!planSummaryModel.isShowRecordView()) {
                        recordContainer.setVisibility(View.VISIBLE);
                        recordInfo.setExpand(true);
                    } else {
                        recordContainer.setVisibility(View.GONE);
                        recordInfo.setExpand(false);
                    }
                    planSummaryModel.setShowRecordView(!planSummaryModel.isShowRecordView());
                }
            });

            //点击开始计时，如果处于计时进行中的状态，点击暂停计时
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (binder != null) {
                        binder.startRecord(recordInfo);
                    }
                }
            });
            planSummaryModel.setRecordInfo(recordInfo);
            //点击结束计时
            stopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (binder != null) {
                        //需要判断下记录状态，否则的话，点击stopButton会一致进行增加计时的操作；
                        if (recordInfo.getRecordState() == Constants.RECORD_STATE_RECORDING) {
                            binder.stopRecord(recordInfo);
                            recordInfo.setEndTime(System.currentTimeMillis());
                            recordInfo.setRealRecordTime(recordInfo.getEndTime() - recordInfo.getStartTime());
                            recordInfo.setRecordState(Constants.RECORD_STATE_STOP);
                            RecordTask.getInstance().updatePlanRecord(recordInfo);
                            long realRecordTime = RecordTask.getInstance().getPlanTotalRecordedTime(recordInfo, planSummaryModel.getPlanType());
                            detailRecordedTimeView.setText(context.getString(R.string.ad_item_plan_recorded_time,
                                    planSummaryModel.getPlanName(),
                                    FormatTime.calculateTimeString(realRecordTime)));
                        }
                    }
                }
            });
            //点击完成该计划
            finishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            //这个地方有点问题，需要优化下，这样做没有多大必要；
            Runnable recordRunnable = new Runnable() {
                @Override
                public void run() {
                    PlanRecordInfo recordInfo = planSummaryModel.getRecordInfo();
                    if (recordInfo != null) {
                        recordingTimeTextView.setText(FormatTime.calculateTimeString(recordInfo.getTimeDuration()));
                    }
                    handler.postDelayed(this, 1000);
                }
            };


            handler.removeCallbacks(recordInfo.getRefreshUiRunnable());
            recordInfo.setRefreshUiRunnable(recordRunnable);

            handler.postDelayed(recordInfo.getRefreshUiRunnable(), 1000);
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
                case Constants.DAY_TYPE:
                    categoryTextView.setText(categoryTextView.getContext().getString(R.string.plan_today));
                    break;
                case Constants.WEEK_TYPE:
                    categoryTextView.setText(categoryTextView.getContext().getString(R.string.plan_this_week));
                    break;
                case Constants.LONG_TERM_TYPE:
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
                    Intent intent = new Intent(itemView.getContext(), AddPlanDetailActivity.class);
                    intent.putExtra("type", noPlanModel.getNoPlanType());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    /**
     * 暂时不用这个类了，因为记录详情view添加到了summaryView中，通过隐藏和显示来控制；
     */

    public class PlanRecordItemViewHolder extends ItemViewHolder {
        private TextView recordedTimeView;
        private TextView startButton;
        private TextView stopButton;
        private TextView finishButton;
        private Context context;

        public PlanRecordItemViewHolder(View itemView) {
            super(itemView);
            recordedTimeView = (TextView) itemView.findViewById(R.id.ad_tv_plan_recorded_time);
            startButton = (TextView) itemView.findViewById(R.id.ad_tv_plan_record_begin);
            stopButton = (TextView) itemView.findViewById(R.id.ad_tv_plan_record_stop);
            finishButton = (TextView) itemView.findViewById(R.id.ad_tv_plan_record_finish);
            context = itemView.getContext();

        }

        @Override
        public void update(int position) {
            PlanRecordModel planRecordModel = (PlanRecordModel) modelList.get(position);
            String planTypeString = null;
            switch (planRecordModel.getPlanType()) {
                case Constants.DAY_TYPE:
                    planTypeString = context.getString(R.string.plan_today);
                    break;
                case Constants.WEEK_TYPE:
                    planTypeString = context.getString(R.string.plan_this_week);
                    break;
                case Constants.LONG_TERM_TYPE:
                    planTypeString = context.getString(R.string.plan_long_time);
                    break;
            }
            recordedTimeView.setText(context.getString(R.string.ad_item_plan_recorded_time, planRecordModel.getPlanName(), String.valueOf(0)));
            //planRecordInfo对象，用来保存计划进行时间
            final PlanRecordInfo recordInfo = new PlanRecordInfo();
            //点击开始计时，如果处于计时进行中的状态，点击暂停计时
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binder.startRecord(recordInfo);
                }
            });
            planRecordModel.setRecordInfo(recordInfo);
            //点击结束计时
            stopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            //点击完成该计划
            finishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    public static abstract class ItemModel {
        private int Type;

        private String planId;

        public abstract int getType();

        public String getPlanId() {
            return planId;
        }

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

    public static class PlanSummaryModel extends ItemModel {

        private String planId;
        private String planName;
        private long recordingTime;
        private int planType;
        private boolean isShowRecordView = false;
        private PlanRecordInfo recordInfo;

        @Override
        public int getType() {
            return PLAN_SUMMARY_TYPE;
        }

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public long getRecordingTime() {
            return recordingTime;
        }

        public void setRecordingTime(long recordingTime) {
            this.recordingTime = recordingTime;
        }

        public int getPlanType() {
            return planType;
        }

        public void setPlanType(int planType) {
            this.planType = planType;
        }

        public boolean isShowRecordView() {
            return isShowRecordView;
        }

        public void setShowRecordView(boolean showRecordView) {
            isShowRecordView = showRecordView;
        }

        @Override
        public String getPlanId() {
            return planId;
        }

        @Override
        public void setPlanId(String planId) {
            this.planId = planId;
        }


        public PlanRecordInfo getRecordInfo() {
            return recordInfo;
        }

        public void setRecordInfo(PlanRecordInfo recordInfo) {
            this.recordInfo = recordInfo;
        }
    }

    public static class PlanRecordModel extends ItemModel {
        private int planType;
        private String planName;
        private PlanRecordInfo recordInfo;

        @Override
        public int getType() {
            return PLAN_RECORD_TYPE;
        }

        public int getPlanType() {
            return planType;
        }

        public void setPlanType(int planType) {
            this.planType = planType;
        }

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }


        public PlanRecordInfo getRecordInfo() {
            return recordInfo;
        }

        public void setRecordInfo(PlanRecordInfo recordInfo) {
            this.recordInfo = recordInfo;
        }
    }

    public List<ItemModel> getModelList() {
        return modelList;
    }

    public void setModelList(List<ItemModel> modelList) {
        this.modelList = modelList;
    }

    public RecordService.RecordServiceBinder getBinder() {
        return binder;
    }

    public void setBinder(RecordService.RecordServiceBinder binder) {
        this.binder = binder;
    }

}
