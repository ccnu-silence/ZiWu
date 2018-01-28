package com.github.tinkerti.ziwu.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tiankui on 4/23/17.
 */

public class TaskBaseInfo implements Parcelable{
    private String planId;
    private String planName;
    private int planType;
    private long createTime;
    private int planPriority;
    private long planTime;
    private String planJoinParentId;
    private String planTag;
    private String planDuration;

    public String getPlanNote() {
        return planNote;
    }

    public void setPlanNote(String planNote) {
        this.planNote = planNote;
    }

    private String planNote;

    public TaskBaseInfo(){}

    protected TaskBaseInfo(Parcel in) {
        planId=in.readString();
        planName = in.readString();
        planType=in.readInt();
        createTime = in.readLong();
        planPriority = in.readInt();
        planTime = in.readLong();
        planJoinParentId = in.readString();
        planTag = in.readString();
        planDuration = in.readString();
    }

    public static final Creator<TaskBaseInfo> CREATOR = new Creator<TaskBaseInfo>() {
        @Override
        public TaskBaseInfo createFromParcel(Parcel in) {
            return new TaskBaseInfo(in);
        }

        @Override
        public TaskBaseInfo[] newArray(int size) {
            return new TaskBaseInfo[size];
        }
    };

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getPlanPriority() {
        return planPriority;
    }

    public void setPlanPriority(int planPriority) {
        this.planPriority = planPriority;
    }

    public long getPlanTime() {
        return planTime;
    }

    public void setPlanTime(long planTime) {
        this.planTime = planTime;
    }

    public String getPlanJoinParentId() {
        return planJoinParentId;
    }

    public void setPlanJoinParentId(String planJoinParentId) {
        this.planJoinParentId = planJoinParentId;
    }

    public String getPlanTag() {
        return planTag;
    }

    public void setPlanTag(String planTag) {
        this.planTag = planTag;
    }

    public String getPlanDuration() {
        return planDuration;
    }

    public void setPlanDuration(String planDuration) {
        this.planDuration = planDuration;
    }

    public int getPlanType() {
        return planType;
    }

    public void setPlanType(int planType) {
        this.planType = planType;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(planId);
        dest.writeString(planName);
        dest.writeInt(planType);
        dest.writeLong(createTime);
        dest.writeInt(planPriority);
        dest.writeLong(planTime);
        dest.writeString(planJoinParentId);
        dest.writeString(planTag);
        dest.writeString(planDuration);
    }
}
