package com.github.tinkerti.ziwu.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tiankui on 4/30/17.
 */

public class PlanRecordInfo extends PlanBaseInfo implements Parcelable{

    private long startTime;
    private long endTime;
    private long timeDuration=0;
    private int recordState;

    public PlanRecordInfo(){}
    protected PlanRecordInfo(Parcel in) {
        startTime = in.readLong();
        endTime = in.readLong();
        timeDuration = in.readLong();
        recordState = in.readInt();
    }

    public static final Creator<PlanRecordInfo> CREATOR = new Creator<PlanRecordInfo>() {
        @Override
        public PlanRecordInfo createFromParcel(Parcel in) {
            return new PlanRecordInfo(in);
        }

        @Override
        public PlanRecordInfo[] newArray(int size) {
            return new PlanRecordInfo[size];
        }
    };

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(long timeDuration) {
        this.timeDuration = timeDuration;
    }

    public int getRecordState() {
        return recordState;
    }

    public void setRecordState(int recordState) {
        this.recordState = recordState;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(startTime);
        dest.writeLong(endTime);
        dest.writeLong(timeDuration);
        dest.writeInt(recordState);
    }
}
