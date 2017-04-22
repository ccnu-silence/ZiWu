package com.github.tinkerti.ziwu.data;

import android.database.Cursor;

import com.github.tinkerti.ziwu.data.model.PlanDetailInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiankui on 4/22/17.
 */

public class AddPlanTask implements ITask {


    private static class SingleTonHolder {
        private static AddPlanTask sIns = new AddPlanTask();
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onLoginSuccess() {

    }

    public static AddPlanTask getInstance() {
        return SingleTonHolder.sIns;
    }

    public void addPlanToDb(PlanDetailInfo info) {
        //在插入的时候书写sql语句出现了问题，text类型的数据需要额外添加''引号来表示；
        String sql = "insert or replace into " + Constants.PLAN_DETAIL_TABLE_NAME +
                " values ( '" +
                info.getPlanId() + "', '" +
                info.getPlanName() + "', " +
                info.getCreateTime() + ", " +
                info.getPlanPriority() + ", " +
                info.getPlanTime() + ", '" +
                info.getPlanJoinParentId() + "', '" +
                info.getPlanTag() +
                "' )";
        TaskManager.getInstance().getDb().execSQL(sql);
    }

    public PlanDetailInfo getPlanDetailInfoById(String planId) {
        String sql = "select * from " +
                Constants.PLAN_DETAIL_TABLE_NAME +
                " where planId = '" +
                planId + "'";
        Cursor cursor = TaskManager.getInstance().getDb().rawQuery(sql, null);
        PlanDetailInfo planDetailInfo = null;
        while (cursor.moveToNext()) {
            planDetailInfo = new PlanDetailInfo();
            planDetailInfo.setPlanId(cursor.getString(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_ID)));
            planDetailInfo.setPlanName(cursor.getString(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_NAME)));
            planDetailInfo.setCreateTime(cursor.getLong(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_CREATE_TIME)));
            planDetailInfo.setPlanPriority(cursor.getInt(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_PRIORITY)));
            planDetailInfo.setPlanTime(cursor.getLong(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_TIME)));
            planDetailInfo.setPlanJoinParentId(cursor.getString(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_JOIN_PARENT_ID)));
            planDetailInfo.setPlanTag(cursor.getString(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_TAG)));
        }
        cursor.close();
        return planDetailInfo;
    }

    public List<PlanDetailInfo> getPlanDetailInfo() {
        List<PlanDetailInfo> planDetailInfoList = new ArrayList<>();
        String sql = "select * from " +
                Constants.PLAN_DETAIL_TABLE_NAME;
        Cursor cursor = TaskManager.getInstance().getDb().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            PlanDetailInfo planDetailInfo = new PlanDetailInfo();
            planDetailInfo.setPlanId(cursor.getString(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_ID)));
            planDetailInfo.setPlanName(cursor.getString(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_NAME)));
            planDetailInfo.setCreateTime(cursor.getLong(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_CREATE_TIME)));
            planDetailInfo.setPlanPriority(cursor.getInt(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_PRIORITY)));
            planDetailInfo.setPlanTime(cursor.getLong(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_TIME)));
            planDetailInfo.setPlanJoinParentId(cursor.getString(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_JOIN_PARENT_ID)));
            planDetailInfo.setPlanTag(cursor.getString(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_TAG)));
            planDetailInfoList.add(planDetailInfo);
        }
        cursor.close();
        return planDetailInfoList;
    }
}
