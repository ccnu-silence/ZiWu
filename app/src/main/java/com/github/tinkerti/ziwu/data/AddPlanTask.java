package com.github.tinkerti.ziwu.data;

import android.database.Cursor;

import com.github.tinkerti.ziwu.data.model.AddPlanDetailInfo;

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

    public void addPlanToDb(AddPlanDetailInfo info) {
        //在插入的时候书写sql语句出现了问题，text类型的数据需要额外添加''引号来表示；
        String sql = "insert or replace into " + Constants.ADD_PLAN_DETAIL_TABLE_NAME +
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

    public AddPlanDetailInfo getPlanDetailInfoById(String planId) {
        String sql = "select * from " +
                Constants.ADD_PLAN_DETAIL_TABLE_NAME +
                " where planId = '" +
                planId + "'";
        Cursor cursor = TaskManager.getInstance().getDb().rawQuery(sql, null);
        AddPlanDetailInfo addPlanDetailInfo = null;
        while (cursor.moveToNext()) {
            addPlanDetailInfo = new AddPlanDetailInfo();
            addPlanDetailInfo.setPlanId(cursor.getString(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_ID)));
            addPlanDetailInfo.setPlanName(cursor.getString(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_NAME)));
            addPlanDetailInfo.setCreateTime(cursor.getLong(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_CREATE_TIME)));
            addPlanDetailInfo.setPlanPriority(cursor.getInt(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_PRIORITY)));
            addPlanDetailInfo.setPlanTime(cursor.getLong(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_TIME)));
            addPlanDetailInfo.setPlanJoinParentId(cursor.getString(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_JOIN_PARENT_ID)));
            addPlanDetailInfo.setPlanTag(cursor.getString(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_TAG)));
        }
        cursor.close();
        return addPlanDetailInfo;
    }


    public List<AddPlanDetailInfo> getPlanDetailInfoByType(int type) {
        List<AddPlanDetailInfo> addPlanDetailInfoList = new ArrayList<>();
        String sql = "select * from " +
                Constants.ADD_PLAN_DETAIL_TABLE_NAME
                + " where " + Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_TYPE
                + " = " + type;
        Cursor cursor = TaskManager.getInstance().getDb().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            AddPlanDetailInfo addPlanDetailInfo = new AddPlanDetailInfo();
            addPlanDetailInfo.setPlanId(cursor.getString(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_ID)));
            addPlanDetailInfo.setPlanName(cursor.getString(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_NAME)));
            addPlanDetailInfo.setPlanType(cursor.getInt(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_TYPE)));
            addPlanDetailInfo.setCreateTime(cursor.getLong(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_CREATE_TIME)));
            addPlanDetailInfo.setPlanPriority(cursor.getInt(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_PRIORITY)));
            addPlanDetailInfo.setPlanTime(cursor.getLong(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_TIME)));
            addPlanDetailInfo.setPlanJoinParentId(cursor.getString(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_JOIN_PARENT_ID)));
            addPlanDetailInfo.setPlanTag(cursor.getString(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_TAG)));
            addPlanDetailInfoList.add(addPlanDetailInfo);
        }
        cursor.close();
        return addPlanDetailInfoList;
    }

    public List<AddPlanDetailInfo> getPlanDetailInfo() {
        List<AddPlanDetailInfo> addPlanDetailInfoList = new ArrayList<>();
        String sql = "select * from " +
                Constants.ADD_PLAN_DETAIL_TABLE_NAME;
        Cursor cursor = TaskManager.getInstance().getDb().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            AddPlanDetailInfo addPlanDetailInfo = new AddPlanDetailInfo();
            addPlanDetailInfo.setPlanId(cursor.getString(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_ID)));
            addPlanDetailInfo.setPlanName(cursor.getString(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_NAME)));
            addPlanDetailInfo.setPlanType(cursor.getInt(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_TYPE)));
            addPlanDetailInfo.setCreateTime(cursor.getLong(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_CREATE_TIME)));
            addPlanDetailInfo.setPlanPriority(cursor.getInt(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_PRIORITY)));
            addPlanDetailInfo.setPlanTime(cursor.getLong(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_TIME)));
            addPlanDetailInfo.setPlanJoinParentId(cursor.getString(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_JOIN_PARENT_ID)));
            addPlanDetailInfo.setPlanTag(cursor.getString(cursor.getColumnIndex(Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_TAG)));
            addPlanDetailInfoList.add(addPlanDetailInfo);
        }
        cursor.close();
        return addPlanDetailInfoList;
    }

    public void deletePlanDetailInfoById(String planId) {
        String sql = "delete from " + Constants.ADD_PLAN_DETAIL_TABLE_NAME + " where " + Constants.PLAN_DETAIL_TABLE_COLUMN_PLAN_ID + " = '" + planId + "'";
        TaskManager.getInstance().getDb().execSQL(sql);
    }

    public void deletePlanDetailInfo() {
        String sql = "delete from " + Constants.ADD_PLAN_DETAIL_TABLE_NAME;
        TaskManager.getInstance().getDb().execSQL(sql);
    }
    //没有用了
//    public void savePlanDetailInfo() {
//        String savePlanSql = "insert or replace into " + Constants.PLAN_DETAIL_TABLE_NAME +
//                " select * from " + Constants.ADD_PLAN_DETAIL_TABLE_NAME;
//        TaskManager.getInstance().getDb().execSQL(savePlanSql);
//        deletePlanDetailInfo();
//    }
}
