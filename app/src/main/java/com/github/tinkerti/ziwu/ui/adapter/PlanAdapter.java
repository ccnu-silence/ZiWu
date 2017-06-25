package com.github.tinkerti.ziwu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import com.github.tinkerti.ziwu.ui.utils.ZLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter {
    //TODO:需要把planType和recordState都写成枚举类型，日志内容跟易读些；
    private static final String TAG = "PlanAdapter";
    private static final int PLAN_CATEGORY_TYPE = 1;
    private static final int PLAN_SUMMARY_TYPE = 2;
    private static final int NO_PLAN_TYPE = 4;
    List<ItemModel> modelList;
    private RecordService.RecordServiceBinder binder;
    private Handler handler;//更新界面用；
    private HashMap<String, PlanRecordInfo> recordInfoHashMap;//记录正在进行的计时任务；这里的 id是recordId；

    public PlanAdapter() {
        ZLog.d(TAG, "new PlanAdapter");
        modelList = new ArrayList<>();
        recordInfoHashMap = new HashMap<>();
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
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
        if (itemModel instanceof PlanSummaryModel) {
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
        if (itemModel instanceof PlanSummaryModel) {
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
            ZLog.d(TAG, "PlanSummaryItemView update");
            final PlanSummaryModel planSummaryModel = (PlanSummaryModel) modelList.get(position);
            planNameTextView.setText(planSummaryModel.getPlanName());
            ZLog.d(TAG, "planSummary name:" + planSummaryModel.getPlanName());
            String recordingTime = FormatTime.calculateTimeString(planSummaryModel.getRecordInfo().getTimeDuration());
            recordingTimeTextView.setText(recordingTime);
            ZLog.d(TAG, "recording time:" + recordingTime);

            //planRecordInfo对象，用来保存计划进行时间
            final PlanRecordInfo recordInfo = planSummaryModel.getRecordInfo();
            if (recordInfo.getRecordState() == Constants.RECORD_STATE_RECORDING) {
                recordingTimeTextView.setVisibility(View.VISIBLE);
            }
            recordInfo.setPlanId(planSummaryModel.getPlanId());//这里为什么要设置planId?
            //设置recordView是否显示；
            recordContainer.setVisibility(recordInfo.isExpand() ? View.VISIBLE : View.GONE);
            long totalTime = RecordTask.getInstance().getPlanTotalRecordedTime(recordInfo, planSummaryModel.getPlanType());
            recordInfo.setTotalRecordTime(totalTime);
            ZLog.d(TAG, "totalTime:" + totalTime);
            //之所以要加上timeDuration是因为，退出界面在现实的时候会出现时间跳动，因为部分正在计时着的时间实际上没有计入到数据库中；
            detailRecordedTimeView.setText(getColoredString(context, planSummaryModel.getPlanName(), recordInfo.getTotalRecordTime() + recordInfo.getTimeDuration()));
            ZLog.d(TAG, "detail record time:" + (recordInfo.getTotalRecordTime() + recordInfo.getTimeDuration()) + "| time duration this time:" + recordInfo.getTimeDuration());
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

            //计划item长按点击事件，可以对计划进行修改、删除操作和查看详情操作；
            planSummaryView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });

            //这个地方有点问题，需要优化下，这样做没有多大必要；
            Runnable recordRunnable = new Runnable() {
                @Override
                public void run() {
                    if (recordInfo != null) {
                        recordingTimeTextView.setText(FormatTime.calculateTimeString(recordInfo.getTimeDuration()));
                        ZLog.d(TAG, "(runnable)" + this + " recording time:" + FormatTime.calculateTimeString(recordInfo.getTimeDuration()));
                        detailRecordedTimeView.setText(getColoredString(context, planSummaryModel.getPlanName(), recordInfo.getTotalRecordTime() + recordInfo.getTimeDuration()));
                        ZLog.d(TAG, "(runnable)" + this + " detail record time:" + (recordInfo.getTotalRecordTime() + recordInfo.getTimeDuration()));
                    }
                    handler.postDelayed(this, 1000);
                }
            };
            handler.removeCallbacks(recordInfo.getRefreshUiRunnable());
            ZLog.d(TAG, "remove runnable " + recordInfo.getRefreshUiRunnable());
            recordInfo.setRefreshUiRunnable(recordRunnable);
            ZLog.d(TAG, "set runnable " + recordInfo.getRefreshUiRunnable());
            //点击开始计时，如果处于计时进行中的状态，点击暂停计时
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (binder != null) {
                        if (recordInfo.getRecordState() == Constants.RECORD_STATE_IDLE
                                || recordInfo.getRecordState() == Constants.RECORD_STATE_STOP) {
                            recordInfo.setTimeDuration(0);//为了解决，锁屏之后，结束计时，然后再开始计时，记录详情时间记录问题；
                            binder.startRecord(recordInfo);
                            handler.postDelayed(recordInfo.getRefreshUiRunnable(), 1000);//点击开始更新计时view；
                            recordInfoHashMap.put(recordInfo.getRecordId(), recordInfo);
                        }
                        recordingTimeTextView.setVisibility(View.VISIBLE);
                        recordingTimeTextView.setText(FormatTime.calculateTimeString(recordInfo.getTimeDuration()));

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
                            detailRecordedTimeView.setText(getColoredString(context, planSummaryModel.getPlanName(), realRecordTime));
                            handler.removeCallbacks(recordInfo.getRefreshUiRunnable());
                            recordInfo.setTotalRecordTime(RecordTask.getInstance().getPlanTotalRecordedTime(recordInfo, planSummaryModel.getPlanType()));
                        }
                        recordingTimeTextView.setVisibility(View.GONE);
                        recordInfo.setTimeDuration(0);
                    }
                }
            });
            //点击完成该计划
            finishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


            //如果是正在记录则需要去更新界面，而处于idle或者stop的状态，则不去更新
            if (recordInfo.getRecordState() == Constants.RECORD_STATE_RECORDING) {
                handler.postDelayed(recordInfo.getRefreshUiRunnable(), 1000);
            } else if (recordInfo.getRecordState() == Constants.RECORD_STATE_IDLE
                    || recordInfo.getRecordState() == Constants.RECORD_STATE_STOP) {
                handler.removeCallbacks(recordInfo.getRefreshUiRunnable());
            }

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

    public static class PlanSummaryModel extends ItemModel {

        private String planId;
        private String planName;
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

    public void setModelList(List<ItemModel> modelList) {
        this.modelList = modelList;
    }

    public void setBinder(RecordService.RecordServiceBinder binder) {
        this.binder = binder;
    }

    private SpannableStringBuilder getColoredString(Context context, String name, long totalRecordTime) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(context.getString(R.string.ad_item_plan_recorded_time,
                name,
                FormatTime.calculateTimeString(totalRecordTime)));
        spannableStringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.baseColor)),
                0,
                name.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.greenColor)),
                spannableStringBuilder.length() - FormatTime.calculateTimeString(totalRecordTime).length(),
                spannableStringBuilder.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

}
