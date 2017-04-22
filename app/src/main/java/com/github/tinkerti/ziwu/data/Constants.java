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

    public static final String CREATE_PLAN_DETAIL_TABLE="create table PlanDetail ( planName text not null, " +
            "createTime integer not null , planPriority integer not null , planTime integer , planJoinParentId text ," +
            "planTag text , planDuration integer )";

    public static final String CREATE_RECORD_DETAIL_TABLE="create table if not exists RecordDetail ( recordId text not null ," +
            "planId text , beginTime integer , endTime integer , timeDuration integer, recordState integer )";
}
