package com.github.tinkerti.ziwu.data;

import android.database.Cursor;

import com.github.tinkerti.ziwu.data.model.PlanDetailInfo;
import com.github.tinkerti.ziwu.data.model.PlanRecordInfo;
import com.github.tinkerti.ziwu.ui.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiankui on 5/7/17.
 */

public class RecordTask implements ITask {
    List<BinderServiceObserver> observerList;

    public RecordTask() {
        observerList = new ArrayList<>();
    }

    private static class SingletonHolder {
        private static RecordTask sIns = new RecordTask();
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onLoginSuccess() {

    }

    public static RecordTask getInstance() {
        return SingletonHolder.sIns;
    }

    public void addPlanRecord(PlanRecordInfo recordInfo) {
        String sql = "insert into " +
                Constants.RECORD_DETAIL_TABLE_NAME +
                " values('" +
                recordInfo.getRecordId() + "','" +
                recordInfo.getPlanId() + "'," +
                recordInfo.getStartTime() + "," +
                recordInfo.getEndTime() + "," +
                recordInfo.getRealRecordTime() + "," +
                recordInfo.getRecordState() + ")";
        TaskManager.getDbHelper().getWritableDatabase().execSQL(sql);
    }

    public void updatePlanRecord(PlanRecordInfo recordInfo) {
        String sql = "update " + Constants.RECORD_DETAIL_TABLE_NAME +
                " set endTime= " + recordInfo.getEndTime() +
                ",timeDuration=" + recordInfo.getRealRecordTime() +
                ",recordState=" + recordInfo.getRecordState() +
                " where recordId='" + recordInfo.getRecordId() +
                "'";
        TaskManager.getDbHelper().getWritableDatabase().execSQL(sql);
    }

    public List<PlanRecordInfo> getPlanInRecordingState(PlanDetailInfo planDetailInfo) {
        List<PlanRecordInfo> recordInfoList = new ArrayList<>();
        String sql = "select * from " + Constants.RECORD_DETAIL_TABLE_NAME +
                " where planId= '" + planDetailInfo.getPlanId() +
                "' and recordState=" + Constants.RECORD_STATE_RECORDING +
                " order by beginTime desc";
        Cursor cursor = TaskManager.getDbHelper().getWritableDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            PlanRecordInfo recordInfo = new PlanRecordInfo();
            recordInfo.setRecordId(cursor.getString(cursor.getColumnIndex("recordId")));
            recordInfo.setPlanId(cursor.getString(cursor.getColumnIndex("planId")));
            recordInfo.setStartTime(cursor.getLong(cursor.getColumnIndex("beginTime")));
            recordInfo.setEndTime(cursor.getLong(cursor.getColumnIndex("endTime")));
            recordInfo.setTimeDuration(cursor.getLong(cursor.getColumnIndex("timeDuration")));
            recordInfo.setRecordState(cursor.getInt(cursor.getColumnIndex("recordState")));
            recordInfoList.add(recordInfo);
        }
        return recordInfoList;
    }

    /**
     * 根据计划的类型来获取计划已经进行的时间；
     *
     * @param recordInfo
     * @param type
     * @return
     */
    public long getPlanTotalRecordedTime(PlanRecordInfo recordInfo, int type) {
        long beginTime = 0;
        long endTime = System.currentTimeMillis();
        switch (type) {
            case Constants.DAY_TYPE:
                beginTime = DateUtils.getTodayMorning();
                endTime = DateUtils.getTodayNight();
                break;
            case Constants.MONTH_TYPE:
                beginTime = DateUtils.getCurrentWeekMorning();
                endTime = DateUtils.getCurrentWeekNight();
                break;
            case Constants.LONG_TERM_TYPE:
                beginTime = 0;
                endTime = System.currentTimeMillis();
                break;
        }
        String sql = "select sum(timeDuration) from " + Constants.RECORD_DETAIL_TABLE_NAME +
                " where planId='" + recordInfo.getPlanId() +
                "' and beginTime>" + beginTime +
                " and endTime<" + endTime;
        Cursor cursor = TaskManager.getDbHelper().getWritableDatabase().rawQuery(sql, null);
        try {
            while (cursor.moveToNext()) {
                return cursor.getLong(0);
            }
        } finally {
            cursor.close();
        }
        return 0l;
    }

    public long getPlanRecordStartTimeByType(int type) {
        long beginTime = 0;
        switch (type) {
            case Constants.DAY_TYPE:
                beginTime = DateUtils.getTodayMorning();
                break;
            case Constants.WEEK_TYPE:
                beginTime = DateUtils.getCurrentWeekMorning();
                break;
            case Constants.LONG_TERM_TYPE:
                beginTime = 0;
                break;
        }
        return beginTime;
    }

    public long getPlanRecordEndTimeByType(int type) {
        long endTime = System.currentTimeMillis();
        switch (type) {
            case Constants.DAY_TYPE:
                endTime = DateUtils.getTodayNight();
                break;
            case Constants.WEEK_TYPE:
                endTime = DateUtils.getCurrentWeekNight();
                break;
            case Constants.LONG_TERM_TYPE:
                endTime = System.currentTimeMillis();
                break;
        }
        return endTime;
    }

    public List<PlanRecordInfo> getPlanRecordListByType(long beginTime, long endTime) {
        List<PlanRecordInfo> planRecordInfoList = new ArrayList<>();
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
                " from " + Constants.RECORD_DETAIL_TABLE_NAME +
                " inner join " + Constants.PLAN_DETAIL_TABLE_NAME +
                " on RecordDetail.planId=PlanDetail.planId where beginTime> " + beginTime +
                " and endTime< " + endTime +
                " order by beginTime desc";
        Cursor cursor = TaskManager.getDbHelper().getWritableDatabase().rawQuery(sql, null);
        try {
            while (cursor.moveToNext()) {
                PlanRecordInfo recordInfo = new PlanRecordInfo();
                recordInfo.setPlanId(cursor.getString(cursor.getColumnIndex("planId")));
                recordInfo.setPlanName(cursor.getString(cursor.getColumnIndex("planName")));
                recordInfo.setPlanType(cursor.getInt(cursor.getColumnIndex("planType")));
                recordInfo.setCreateTime(cursor.getLong(cursor.getColumnIndex("createTime")));
                recordInfo.setPlanPriority(cursor.getInt(cursor.getColumnIndex("planPriority")));
                recordInfo.setPlanTime(cursor.getLong(cursor.getColumnIndex("planTime")));
                recordInfo.setPlanJoinParentId(cursor.getString(cursor.getColumnIndex("planJoinParentId")));
                recordInfo.setPlanTag(cursor.getString(cursor.getColumnIndex("planTag")));
                recordInfo.setTimeDuration(cursor.getLong(cursor.getColumnIndex("timeDuration")));
                recordInfo.setStartTime(cursor.getLong(cursor.getColumnIndex("beginTime")));
                recordInfo.setEndTime(cursor.getLong(cursor.getColumnIndex("endTime")));
                recordInfo.setRecordState(cursor.getInt(cursor.getColumnIndex("recordState")));

                planRecordInfoList.add(recordInfo);
            }
        } finally {
            cursor.close();
        }

        return planRecordInfoList;
    }

    public List<PlanRecordInfo> getPlanRecordListByType(int type) {
        List<PlanRecordInfo> planRecordInfoList = new ArrayList<>();
        long beginTime = 0;
        long endTime = System.currentTimeMillis();
        switch (type) {
            case Constants.DAY_TYPE:
                beginTime = DateUtils.getTodayMorning();
                endTime = DateUtils.getTodayNight();
                break;
            case Constants.MONTH_TYPE:
                beginTime = DateUtils.getCurrentWeekMorning();
                endTime = DateUtils.getCurrentWeekNight();
                break;
            case Constants.LONG_TERM_TYPE:
                beginTime = 0;
                endTime = System.currentTimeMillis();
                break;
        }
        String sql = "select RecordDetail.planId," +
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
                " from " + Constants.RECORD_DETAIL_TABLE_NAME +
                " inner join " + Constants.PLAN_DETAIL_TABLE_NAME +
                " on RecordDetail.planId=PlanDetail.planId where beginTime> " + beginTime +
                " and endTime< " + endTime +
                " order by beginTime desc";
        Cursor cursor = TaskManager.getDbHelper().getWritableDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            PlanRecordInfo recordInfo = new PlanRecordInfo();
            recordInfo.setPlanId(cursor.getString(cursor.getColumnIndex("planId")));
            recordInfo.setPlanName(cursor.getString(cursor.getColumnIndex("planName")));
            recordInfo.setPlanType(cursor.getInt(cursor.getColumnIndex("planType")));
            recordInfo.setCreateTime(cursor.getLong(cursor.getColumnIndex("createTime")));
            recordInfo.setPlanPriority(cursor.getInt(cursor.getColumnIndex("planPriority")));
            recordInfo.setPlanTime(cursor.getLong(cursor.getColumnIndex("planTime")));
            recordInfo.setPlanJoinParentId(cursor.getString(cursor.getColumnIndex("planJoinParentId")));
            recordInfo.setPlanTag(cursor.getString(cursor.getColumnIndex("planTag")));
            recordInfo.setTimeDuration(cursor.getLong(cursor.getColumnIndex("timeDuration")));
            recordInfo.setStartTime(cursor.getLong(cursor.getColumnIndex("beginTime")));
            recordInfo.setEndTime(cursor.getLong(cursor.getColumnIndex("endTime")));
            recordInfo.setRecordState(cursor.getInt(cursor.getColumnIndex("recordState")));

            planRecordInfoList.add(recordInfo);
        }
        return planRecordInfoList;
    }

    public List<PlanRecordInfo> getPlanHistoryTime(int type) {
        List<PlanRecordInfo> planRecordInfoList = new ArrayList<>();
        long beginTime = 0;
        long endTime = System.currentTimeMillis();
        switch (type) {
            case Constants.DAY_TYPE:
                beginTime = DateUtils.getTodayMorning();
                endTime = DateUtils.getTodayNight();
                break;
            case Constants.MONTH_TYPE:
                beginTime = DateUtils.getCurrentWeekMorning();
                endTime = DateUtils.getCurrentWeekNight();
                break;
            case Constants.LONG_TERM_TYPE:
                beginTime = 0;
                endTime = System.currentTimeMillis();
                break;
        }
        String sql = "select RecordDetail.planId,planName,planType,createTime,planPriority,planTime,planJoinParentId,planTag,sum(timeDuration) as sumTime " +
                "from " + Constants.RECORD_DETAIL_TABLE_NAME +
                " inner join " + Constants.PLAN_DETAIL_TABLE_NAME +
                " on RecordDetail.planId=PlanDetail.planId where beginTime> " + beginTime +
                " and endTime< " + endTime +
                " group by RecordDetail.planId";
        Cursor cursor = TaskManager.getDbHelper().getWritableDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            PlanRecordInfo recordInfo = new PlanRecordInfo();
            recordInfo.setPlanId(cursor.getString(cursor.getColumnIndex("planId")));
            recordInfo.setPlanName(cursor.getString(cursor.getColumnIndex("planName")));
            recordInfo.setPlanType(cursor.getInt(cursor.getColumnIndex("planType")));
            recordInfo.setCreateTime(cursor.getLong(cursor.getColumnIndex("createTime")));
            recordInfo.setPlanPriority(cursor.getInt(cursor.getColumnIndex("planPriority")));
            recordInfo.setPlanTime(cursor.getLong(cursor.getColumnIndex("planTime")));
            recordInfo.setPlanJoinParentId(cursor.getString(cursor.getColumnIndex("planJoinParentId")));
            recordInfo.setPlanTag(cursor.getString(cursor.getColumnIndex("planTag")));
            recordInfo.setTimeDuration(cursor.getLong(cursor.getColumnIndex("sumTime")));

            planRecordInfoList.add(recordInfo);
        }
        return planRecordInfoList;
    }

    public long getPlanTotalRecordedTimeByType(int type) {
        long beginTime = 0;
        long endTime = System.currentTimeMillis();
        switch (type) {
            case Constants.DAY_TYPE:
                beginTime = DateUtils.getTodayMorning();
                endTime = DateUtils.getTodayNight();
                break;
            case Constants.MONTH_TYPE:
                beginTime = DateUtils.getCurrentWeekMorning();
                endTime = DateUtils.getCurrentWeekNight();
                break;
            case Constants.LONG_TERM_TYPE:
                beginTime = 0;
                endTime = System.currentTimeMillis();
                break;
        }
        String sql = "select sum(timeDuration) from " + Constants.RECORD_DETAIL_TABLE_NAME +
                " where beginTime>" + beginTime +
                " and endTime<" + endTime;
        Cursor cursor = TaskManager.getDbHelper().getWritableDatabase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            return cursor.getLong(0);
        }
        return 0l;
    }

    public void deleteRecordInfoByPlanId(String planId) {
        String deleteSql = "delete from " + Constants.RECORD_DETAIL_TABLE_NAME + " where " + Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_ID + "= '" + planId + "'";
        TaskManager.getDbHelper().getWritableDatabase().execSQL(deleteSql);
    }

    public void addBinderServiceObserver(BinderServiceObserver observer) {
        observerList.add(observer);
    }

    public void removeBinderServiceObserver(BinderServiceObserver observer) {
        observerList.remove(observer);
    }

    public void notifyServiceConnected() {
        for (BinderServiceObserver observer : observerList) {
            observer.onServiceConnected();
        }
    }

    public interface BinderServiceObserver {
        public void onServiceConnected();
    }

}
