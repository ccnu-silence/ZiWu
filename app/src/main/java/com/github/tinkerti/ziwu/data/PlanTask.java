package com.github.tinkerti.ziwu.data;

import android.database.Cursor;

import com.github.tinkerti.ziwu.data.model.AddTaskDetailInfo;
import com.github.tinkerti.ziwu.data.model.TaskDetailInfo;
import com.github.tinkerti.ziwu.data.model.TaskRecordInfo;

import java.util.ArrayList;
import java.util.List;

public class PlanTask extends ITask {

    private static class SingleTonHolder {
        private static PlanTask sIns = new PlanTask();
    }

    @Override
    protected void onInit(TaskManager taskManager) {
        super.onInit(taskManager);
    }

    @Override
    public void onLoginSuccess() {

    }

    public static PlanTask getInstance() {
        return SingleTonHolder.sIns;
    }

    public void getPlanDetailInfoByType(final int type, final SimpleResultCallback<List<TaskRecordInfo>> resultCallback) {
        TaskManager.getInstance().getWorkHandler().post(new Runnable() {
            @Override
            public void run() {
                List<TaskRecordInfo> taskRecordInfoList = getPlanDetailInfoByTypeFromDb(type);
                if (resultCallback != null) {
                    resultCallback.onSuccess(taskRecordInfoList);
                }
            }
        });
    }

    public List<TaskRecordInfo> getPlanDetailInfoByTypeFromDb(int type) {
        List<TaskRecordInfo> taskRecordInfoList = new ArrayList<>();
        long beginTime = 0;
        switch (type) {
            case Consts.TYPE_IS_VALID:
                beginTime = 0;
                break;
        }
        String sql = "select PlanDetail.planId," +
                "PlanDetail.planName," +
                "PlanDetail.planType," +
                "PlanDetail.createTime," +
                "PlanDetail.planPriority," +
                "PlanDetail.planTime," +
                "PlanDetail.planJoinParentId," +
                "PlanDetail.planTag," +
                "PlanDetail.planNote," +
                "RecordDetail.recordId," +
                "max(RecordDetail.beginTime) as beginTime," +
                "RecordDetail.endTime," +
                "RecordDetail.timeDuration," +
                "RecordDetail.recordState," +
                "PlanDetail.isExpand from " +
                Consts.TABLE_NAME_PLAN_DETAIL +
                " left join " + Consts.TABLE_NAME_RECORD_DETAIL + " on RecordDetail.planId = PlanDetail.planId "
                + " where " + Consts.PLAN_DETAIL_TABLE_COLUMN_PLAN_TYPE
                + " = " + type + " and createTime>" + beginTime + " GROUP BY PlanDetail.planId ORDER BY PlanDetail.createTime desc";
        Cursor cursor = TaskManager.getInstance().getDb().rawQuery(sql, null);

        while (cursor.moveToNext()) {
            TaskRecordInfo taskRecordInfo = new TaskRecordInfo();
            taskRecordInfo.setPlanId(cursor.getString(cursor.getColumnIndex(Consts.PLAN_DETAIL_TABLE_COLUMN_PLAN_ID)));
            taskRecordInfo.setPlanName(cursor.getString(cursor.getColumnIndex(Consts.PLAN_DETAIL_TABLE_COLUMN_PLAN_NAME)));
            taskRecordInfo.setPlanType(cursor.getInt(cursor.getColumnIndex(Consts.PLAN_DETAIL_TABLE_COLUMN_PLAN_TYPE)));
            taskRecordInfo.setCreateTime(cursor.getLong(cursor.getColumnIndex(Consts.PLAN_DETAIL_TABLE_COLUMN_PLAN_CREATE_TIME)));
            taskRecordInfo.setPlanPriority(cursor.getInt(cursor.getColumnIndex(Consts.PLAN_DETAIL_TABLE_COLUMN_PLAN_PRIORITY)));
            taskRecordInfo.setPlanTime(cursor.getLong(cursor.getColumnIndex(Consts.PLAN_DETAIL_TABLE_COLUMN_PLAN_TIME)));
            taskRecordInfo.setPlanJoinParentId(cursor.getString(cursor.getColumnIndex(Consts.PLAN_DETAIL_TABLE_COLUMN_PLAN_JOIN_PARENT_ID)));
            taskRecordInfo.setPlanTag(cursor.getString(cursor.getColumnIndex(Consts.PLAN_DETAIL_TABLE_COLUMN_PLAN_TAG)));
            taskRecordInfo.setPlanNote(cursor.getString(cursor.getColumnIndex(Consts.PLAN_DETAIL_TABLE_COLUMN_PLAN_NOTE)));

            taskRecordInfo.setRecordId(cursor.getString(cursor.getColumnIndex(Consts.RECORD_DETAIL_TABLE_COLUMN_RECORD_ID)));
            taskRecordInfo.setBeginTime(cursor.getLong(cursor.getColumnIndex(Consts.RECORD_DETAIL_TABLE_COLUMN_BEGIN_TIME)));
            taskRecordInfo.setEndTime(cursor.getLong(cursor.getColumnIndex(Consts.RECORD_DETAIL_TABLE_COLUMN_END_TIME)));
            taskRecordInfo.setTimeDuration(cursor.getLong(cursor.getColumnIndex(Consts.RECORD_DETAIL_TABLE_COLUMN_TIME_DURATION)));
            taskRecordInfo.setRecordState(cursor.getInt(cursor.getColumnIndex(Consts.RECORD_DETAIL_TABLE_COLUMN_RECORD_STATE)) == 0
                    ? Consts.RECORD_STATE_STOP : cursor.getInt(cursor.getColumnIndex(Consts.RECORD_DETAIL_TABLE_COLUMN_RECORD_STATE)));
            taskRecordInfo.setExpand(cursor.getInt(cursor.getColumnIndex(Consts.RECORD_DETAIL_TABLE_COLUMN_IS_EXPAND)) != 0);

            long timeDuration = RecordTask.getInstance().getRecordTimeFromDb(taskRecordInfo.getPlanId());
            taskRecordInfo.setTimeDuration(timeDuration);
            taskRecordInfoList.add(taskRecordInfo);

        }
        cursor.close();
        return taskRecordInfoList;
    }

    public void addPlanToDb(AddTaskDetailInfo info) {
        //在插入的时候书写sql语句出现了问题，text类型的数据需要额外添加''引号来表示；
        String sql = "insert or replace into " + Consts.TABLE_NAME_PLAN_DETAIL +
                " values ( '" +
                info.getPlanId() + "', '" +
                info.getPlanName() + "', " +
                info.getPlanType() + " , " +
                info.getCreateTime() + ", " +
                info.getPlanPriority() + ", " +
                info.getPlanTime() + ", '" +
                info.getPlanJoinParentId() + "', '" +
                info.getPlanTag() + "', '" +
                info.getPlanNote() +
                "' )";
        TaskManager.getInstance().getDb().execSQL(sql);
    }

    public void deletePlanDetailInfoById(String planId) {
        String sql = "delete from " + Consts.TABLE_NAME_PLAN_DETAIL + " where " + Consts.PLAN_DETAIL_TABLE_COLUMN_PLAN_ID + " = '" + planId + "'";
        TaskManager.getInstance().getDb().execSQL(sql);
    }


    public void renamePlan(TaskDetailInfo info) {
        String sql = "update " + Consts.TABLE_NAME_PLAN_DETAIL +
                " set planName= '" + info.getPlanName() +
                "' where planId='" + info.getPlanId() +
                "'";
        TaskManager.getInstance().getDb().execSQL(sql);
    }


}
