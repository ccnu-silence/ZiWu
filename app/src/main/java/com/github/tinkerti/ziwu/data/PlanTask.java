package com.github.tinkerti.ziwu.data;

import android.database.Cursor;

import com.github.tinkerti.ziwu.data.model.AddPlanDetailInfo;
import com.github.tinkerti.ziwu.data.model.PlanDetailInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiankui on 4/23/17.
 */

public class PlanTask implements ITask {

    private static class SingleTonHolder {
        private static PlanTask sIns = new PlanTask();
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onLoginSuccess() {

    }

    public static PlanTask getInstance() {
        return SingleTonHolder.sIns;
    }

    public List<PlanDetailInfo> getPlanDetailInfoByType(int type) {
        List<PlanDetailInfo> planDetailInfoList = new ArrayList<>();
        String sql = "select * from " +
                Constants.PLAN_DETAIL_TABLE_NAME
                + " where " + Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_TYPE
                + " = " + type;
        Cursor cursor = TaskManager.getInstance().getDb().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            PlanDetailInfo planDetailInfo = new PlanDetailInfo();
            planDetailInfo.setPlanId(cursor.getString(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_ID)));
            planDetailInfo.setPlanName(cursor.getString(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_NAME)));
            planDetailInfo.setPlanType(cursor.getInt(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_TYPE)));
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

    public void addPlanToDb(AddPlanDetailInfo info) {
        //在插入的时候书写sql语句出现了问题，text类型的数据需要额外添加''引号来表示；
        String sql = "insert or replace into " + Constants.PLAN_DETAIL_TABLE_NAME +
                " values ( '" +
                info.getPlanId() + "', '" +
                info.getPlanName() + "', " +
                info.getPlanType() + " , " +
                info.getCreateTime() + ", " +
                info.getPlanPriority() + ", " +
                info.getPlanTime() + ", '" +
                info.getPlanJoinParentId() + "', '" +
                info.getPlanTag() +
                "' )";
        TaskManager.getInstance().getDb().execSQL(sql);
    }

    public void deletePlanDetailInfoById(String planId) {
        String sql = "delete from " + Constants.ADD_PLAN_DETAIL_TABLE_NAME + " where " + Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_ID + " = '" + planId + "'";
        TaskManager.getInstance().getDb().execSQL(sql);
    }


}
