package com.github.tinkerti.ziwu.data;

import android.database.Cursor;

import com.github.tinkerti.ziwu.data.model.PlanRecordInfo;
import com.github.tinkerti.ziwu.ui.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiankui on 5/7/17.
 */

public class RecordTask implements ITask {

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
        while (cursor.moveToNext()) {
            return cursor.getLong(0);
        }
        return 0l;
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

    public long getPlanTotalRecordedTimeByType( int type) {
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


}
