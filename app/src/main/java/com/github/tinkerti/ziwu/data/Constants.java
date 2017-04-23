package com.github.tinkerti.ziwu.data;

/**
 * Created by tiankui on 4/9/17.
 */

public class Constants {
    public static final int DAY_TYPE = 0;
    public static final int WEEK_TYPE = 1;
    public static final int MONTH_TYPE = 2;
    public static final int YEAR_TYPE = 3;
    public static final int LONG_TERM_TYPE = 4;

    public static final String PLAN_DETAIL_TABLE_NAME = "PlanDetail";
    public static final String ADD_PLAN_DETAIL_TABLE_NAME = "PlanDetail";
    public static final String RECORD_DETAIL_TABLE_NAME = "RecordDetail";

    public static final String PLAN_DETAIL_TABLE_COLUMN_PLAN_ID = "planId";
    public static final String PLAN_DETAIL_TABLE_COLUMN_PLAN_TYPE = "planType";
    public static final String PLAN_DETAIL_TABLE_COLUMN_PLAN_NAME = "planName";
    public static final String PLAN_DETAIL_TABLE_COLUMN_PLAN_CREATE_TIME = "createTime";
    public static final String PLAN_DETAIL_TABLE_COLUMN_PLAN_PRIORITY = "planPriority";

    public static final String PLAN_DETAIL_TABLE_COLUMN_PLAN_TIME = "planTime";

    public static final String PLAN_DETAIL_TABLE_COLUMN_PLAN_JOIN_PARENT_ID = "planJoinParentId";
    public static final String PLAN_DETAIL_TABLE_COLUMN_PLAN_TAG = "planTag";


    public static final String CREATE_ADD_PLAN_DETAIL_TABLE = "create table " + ADD_PLAN_DETAIL_TABLE_NAME + " ( planId text not null primary key, " +
            "planName text not null, planType integer not null, " +
            "createTime integer not null , planPriority integer not null , planTime integer , planJoinParentId text ," +
            "planTag text )";

    public static final String CREATE_PLAN_DETAIL_TABLE = "create table " + PLAN_DETAIL_TABLE_NAME + " ( planId text not null primary key, " +
            "planName text not null, planType integer not null, " +
            "createTime integer not null , planPriority integer not null , planTime integer , planJoinParentId text ," +
            "planTag text )";

    public static final String CREATE_RECORD_DETAIL_TABLE = "create table if not exists " + RECORD_DETAIL_TABLE_NAME + " ( recordId text not null primary key," +
            "planId text , beginTime integer , endTime integer , timeDuration integer, recordState integer )";
}
