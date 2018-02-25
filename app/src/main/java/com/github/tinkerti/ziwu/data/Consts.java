package com.github.tinkerti.ziwu.data;

/**
 * Created by tiankui on 4/9/17.
 */

public class Consts {

    public static final int TYPE_ALL = 1001;

    public static final int RENAME_PLAN = 0;
    public static final int DELETE_PLAN = 1;
    public static final int TRANSFER_PLAN = 2;
    public static final int CHECK_DETAIL = 3;

    public static final int DAY_TYPE = 0;
    public static final int WEEK_TYPE = 1;
    public static final int MONTH_TYPE = 2;
    public static final int YEAR_TYPE = 3;
    public static final int TYPE_IS_VALID = 4;
    public static final int NOTIFICATION_START_ACTIVITY = 11;
    public static final int RECORD_STATE_RECORDING = 101;
    public static final int RECORD_STATE_PAUSE = 102;
    public static final int RECORD_STATE_STOP = 103;

    public static final int ADD_PLAN_REQUEST = 1000;

    public static final int READ_TO_SELECT = 101;
    public static final int SELECTED = 102;


    public static final float ONE_DAY_TOTAL_MILLI_SECS = 1 * 24 * 60 * 60 * 1000.0f;
    public static final float SEVEN_DAY_TOTAL_MILLIS_SECS = 7 * ONE_DAY_TOTAL_MILLI_SECS;

    public static final int RECORD_DATE_TITLE = 0;
    public static final int RECORD_LIST_ITEM = 1;

    public static final String TABLE_NAME_PLAN_DETAIL = "PlanDetail";
    public static final String TABLE_NAME_RECORD_DETAIL = "RecordDetail";

    public static final String PLAN_DETAIL_TABLE_COLUMN_PLAN_ID = "planId";
    public static final String PLAN_DETAIL_TABLE_COLUMN_PLAN_TYPE = "planType";
    public static final String PLAN_DETAIL_TABLE_COLUMN_PLAN_NAME = "planName";
    public static final String PLAN_DETAIL_TABLE_COLUMN_PLAN_CREATE_TIME = "createTime";
    public static final String PLAN_DETAIL_TABLE_COLUMN_PLAN_PRIORITY = "planPriority";
    public static final String PLAN_DETAIL_TABLE_COLUMN_PLAN_TIME = "planTime";
    public static final String PLAN_DETAIL_TABLE_COLUMN_PLAN_JOIN_PARENT_ID = "planJoinParentId";
    public static final String PLAN_DETAIL_TABLE_COLUMN_PLAN_TAG = "planTag";
    public static final String PLAN_DETAIL_TABLE_COLUMN_PLAN_NOTE = "planNote";

    public static final String RECORD_DETAIL_TABLE_COLUMN_RECORD_ID = "recordId";
    public static final String RECORD_DETAIL_TABLE_COLUMN_PLAN_ID = "planId";
    public static final String RECORD_DETAIL_TABLE_COLUMN_BEGIN_TIME = "beginTime";
    public static final String RECORD_DETAIL_TABLE_COLUMN_END_TIME = "endTime";
    public static final String RECORD_DETAIL_TABLE_COLUMN_TIME_DURATION = "timeDuration";
    public static final String RECORD_DETAIL_TABLE_COLUMN_RECORD_STATE = "recordState";
    public static final String RECORD_DETAIL_TABLE_COLUMN_IS_EXPAND = "isExpand";


    public static final String SERVICE_RECORDING_PLAN_INFO_LIST = "planRecordingList";


    public static final String CREATE_PLAN_DETAIL_TABLE = "create table " + TABLE_NAME_PLAN_DETAIL +
            " ( planId text not null primary key, " +
            " planName text not null," +
            " planType integer not null, " +
            " createTime integer not null ," +
            " planPriority integer not null ," +
            " planTime integer ," +
            " planJoinParentId text ," +
            " planTag text, " +
            " planNote text," +
            " isExpand integer )";

    public static final String CREATE_RECORD_DETAIL_TABLE = "create table if not exists " + TABLE_NAME_RECORD_DETAIL +
            " ( recordId text not null primary key," +
            " planId text ," +
            " beginTime integer ," +
            " endTime integer ," +
            " timeDuration integer," +
            " recordState integer," +
            " isExpand integer )";
}
