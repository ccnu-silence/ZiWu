package com.github.tinkerti.ziwu.data;

import android.database.Cursor;
import android.text.TextUtils;

import com.github.tinkerti.ziwu.data.model.TaskRecordInfo;
import com.github.tinkerti.ziwu.ui.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiankui on 5/7/17.
 */

public class RecordTask extends ITask {

    public RecordTask() {
    }

    private static class SingletonHolder {
        private static RecordTask sIns = new RecordTask();
    }


    @Override
    void onInit(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void onLoginSuccess() {

    }

    public static RecordTask getInstance() {
        return SingletonHolder.sIns;
    }

    public void addTaskRecord(TaskRecordInfo recordInfo) {
        String sql = "insert into " +
                Consts.TABLE_NAME_RECORD_DETAIL +
                " values('" +
                recordInfo.getRecordId() + "','" +
                recordInfo.getPlanId() + "'," +
                recordInfo.getBeginTime() + "," +
                recordInfo.getEndTime() + "," +
                recordInfo.getRealRecordTime() + "," +
                recordInfo.getRecordState() + ")";
        TaskManager.getDbHelper().getWritableDatabase().execSQL(sql);
    }

    public void updateTaskRecord(TaskRecordInfo recordInfo) {
        String sql = "update " + Consts.TABLE_NAME_RECORD_DETAIL +
                " set endTime= " + recordInfo.getEndTime() +
                ",timeDuration=" + recordInfo.getRealRecordTime() +
                ",recordState=" + recordInfo.getRecordState() +
                " where recordId='" + recordInfo.getRecordId() +
                "'";
        TaskManager.getDbHelper().getWritableDatabase().execSQL(sql);
    }


    public long getPlanRecordStartTimeByType(int type) {
        long beginTime = 0;
        switch (type) {
            case Consts.DAY_TYPE:
                beginTime = DateUtils.getTodayMorning();
                break;
            case Consts.WEEK_TYPE:
                beginTime = DateUtils.getCurrentWeekMorning();
                break;
            case Consts.TYPE_IS_VALID:
                beginTime = 0;
                break;
        }
        return beginTime;
    }

    public long getPlanRecordEndTimeByType(int type) {
        long endTime = System.currentTimeMillis();
        switch (type) {
            case Consts.DAY_TYPE:
                endTime = DateUtils.getTodayNight();
                break;
            case Consts.WEEK_TYPE:
                endTime = DateUtils.getCurrentWeekNight();
                break;
            case Consts.TYPE_IS_VALID:
                endTime = System.currentTimeMillis();
                break;
        }
        return endTime;
    }

    public List<TaskRecordInfo> getTaskRecordListByType(long beginTime, long endTime) {
        List<TaskRecordInfo> taskRecordInfoList = new ArrayList<>();
        String sql = "select " +
                "RecordDetail.planId," +
                "planName," +
                "planType," +
                "createTime," +
                "planPriority," +
                "planTime," +
                "planJoinParentId," +
                "planTag," +
                "timeDuration," +
                "beginTime," +
                "endTime, " +
                "recordState" +
                " from " + Consts.TABLE_NAME_RECORD_DETAIL +
                " inner join " + Consts.TABLE_NAME_PLAN_DETAIL +
                " on RecordDetail.planId=PlanDetail.planId where beginTime> " + beginTime +
                " and endTime< " + endTime +
                " order by beginTime desc";
        Cursor cursor = TaskManager.getDbHelper().getWritableDatabase().rawQuery(sql, null);
        try {
            while (cursor.moveToNext()) {
                TaskRecordInfo recordInfo = new TaskRecordInfo();
                recordInfo.setPlanId(cursor.getString(cursor.getColumnIndex("planId")));
                recordInfo.setPlanName(cursor.getString(cursor.getColumnIndex("planName")));
                recordInfo.setPlanType(cursor.getInt(cursor.getColumnIndex("planType")));
                recordInfo.setCreateTime(cursor.getLong(cursor.getColumnIndex("createTime")));
                recordInfo.setPlanPriority(cursor.getInt(cursor.getColumnIndex("planPriority")));
                recordInfo.setPlanTime(cursor.getLong(cursor.getColumnIndex("planTime")));
                recordInfo.setPlanJoinParentId(cursor.getString(cursor.getColumnIndex("planJoinParentId")));
                recordInfo.setPlanTag(cursor.getString(cursor.getColumnIndex("planTag")));
                recordInfo.setTimeDuration(cursor.getLong(cursor.getColumnIndex("timeDuration")));
                recordInfo.setBeginTime(cursor.getLong(cursor.getColumnIndex("beginTime")));
                recordInfo.setEndTime(cursor.getLong(cursor.getColumnIndex("endTime")));
                recordInfo.setRecordState(cursor.getInt(cursor.getColumnIndex("recordState")));

                taskRecordInfoList.add(recordInfo);
            }
        } finally {
            cursor.close();
        }

        return taskRecordInfoList;
    }

    public List<TaskRecordInfo> getPlanHistoryTime(int type) {
        List<TaskRecordInfo> taskRecordInfoList = new ArrayList<>();
        long beginTime = 0;
        long endTime = System.currentTimeMillis();
        switch (type) {
            case Consts.DAY_TYPE:
                beginTime = DateUtils.getTodayMorning();
                endTime = DateUtils.getTodayNight();
                break;
            case Consts.MONTH_TYPE:
                beginTime = DateUtils.getCurrentWeekMorning();
                endTime = DateUtils.getCurrentWeekNight();
                break;
            case Consts.TYPE_IS_VALID:
                beginTime = 0;
                endTime = System.currentTimeMillis();
                break;
        }
        String sql = "select RecordDetail.planId,planName,planType,createTime,planPriority,planTime,planJoinParentId,planTag,sum(timeDuration) as sumTime " +
                "from " + Consts.TABLE_NAME_RECORD_DETAIL +
                " inner join " + Consts.TABLE_NAME_PLAN_DETAIL +
                " on RecordDetail.planId=PlanDetail.planId where beginTime> " + beginTime +
                " and endTime< " + endTime +
                " group by RecordDetail.planId";
        Cursor cursor = TaskManager.getDbHelper().getWritableDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            TaskRecordInfo recordInfo = new TaskRecordInfo();
            recordInfo.setPlanId(cursor.getString(cursor.getColumnIndex("planId")));
            recordInfo.setPlanName(cursor.getString(cursor.getColumnIndex("planName")));
            recordInfo.setPlanType(cursor.getInt(cursor.getColumnIndex("planType")));
            recordInfo.setCreateTime(cursor.getLong(cursor.getColumnIndex("createTime")));
            recordInfo.setPlanPriority(cursor.getInt(cursor.getColumnIndex("planPriority")));
            recordInfo.setPlanTime(cursor.getLong(cursor.getColumnIndex("planTime")));
            recordInfo.setPlanJoinParentId(cursor.getString(cursor.getColumnIndex("planJoinParentId")));
            recordInfo.setPlanTag(cursor.getString(cursor.getColumnIndex("planTag")));
            recordInfo.setTimeDuration(cursor.getLong(cursor.getColumnIndex("sumTime")));

            taskRecordInfoList.add(recordInfo);
        }
        return taskRecordInfoList;
    }

    public long getPlanTotalRecordedTimeByType(int type) {
        long beginTime = 0;
        long endTime = System.currentTimeMillis();
        switch (type) {
            case Consts.DAY_TYPE:
                beginTime = DateUtils.getTodayMorning();
                endTime = DateUtils.getTodayNight();
                break;
            case Consts.MONTH_TYPE:
                beginTime = DateUtils.getCurrentWeekMorning();
                endTime = DateUtils.getCurrentWeekNight();
                break;
            case Consts.TYPE_IS_VALID:
                beginTime = 0;
                endTime = System.currentTimeMillis();
                break;
        }
        String sql = "select sum(timeDuration) from " + Consts.TABLE_NAME_RECORD_DETAIL +
                " where beginTime>" + beginTime +
                " and endTime<" + endTime;
        Cursor cursor = TaskManager.getDbHelper().getWritableDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            return cursor.getLong(0);
        }
        return 0l;
    }

    public void deleteRecordInfoByPlanId(String planId) {
        String deleteSql = "delete from " + Consts.TABLE_NAME_RECORD_DETAIL + " where " + Consts.PLAN_DETAIL_TABLE_COLUMN_PLAN_ID + "= '" + planId + "'";
        TaskManager.getDbHelper().getWritableDatabase().execSQL(deleteSql);
    }
    
    public void getLatestOneRecordInfo(final String planId, final SimpleResultCallback<List<TaskRecordInfo>> callback) {
        if (TextUtils.isEmpty(planId)) {
            return;
        }
        taskManager.getWorkHandler().post(new Runnable() {
            @Override
            public void run() {
                List<TaskRecordInfo> recordInfoList = getLatestOneRecordInfoFromDb(planId);
                callback.onSuccess(recordInfoList);
            }
        });
    }

    public List<TaskRecordInfo> getLatestOneRecordInfoFromDb(String planId) {
        List<TaskRecordInfo> recordInfoList = new ArrayList<>();
        String sql = "select recordId, max(beginTime) from "
                + Consts.TABLE_NAME_RECORD_DETAIL + " where planId = '" + planId + "'";
        Cursor cursor = taskManager.getDb().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            TaskRecordInfo recordInfo = new TaskRecordInfo();
            recordInfo.setRecordId(cursor.getString(0));
            recordInfo.setBeginTime(cursor.getLong(1));
        }
        return recordInfoList;
    }
}
