package com.github.tinkerti.ziwu.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.tinkerti.ziwu.data.Consts;

public class TaskRecordInfo extends TaskBaseInfo implements Parcelable {

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    private String recordId;
    private long beginTime;
    private long endTime;
    private long timeDuration = 0;
    private int recordState = Consts.RECORD_STATE_STOP;
    private Runnable refreshUiRunnable;//用于定时刷新计时的ui
    private boolean isExpand = false;
    private NotificationInfo notificationInfo;
    private String taskNote;

    public ImageView startButton;
    public TextView recordingTimeTextView;
    public TextView expandedRecordingTimeView;

    public int select_state = Consts.READ_TO_SELECT;

    public long getTimeDurationPerRecord() {
        return timeDurationPerRecord;
    }

    public void setTimeDurationPerRecord(long timeDurationPerRecord) {
        this.timeDurationPerRecord = timeDurationPerRecord;
    }

    private long timeDurationPerRecord;

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public Runnable getRecordTimeRunnable() {
        return recordTimeRunnable;
    }

    public void setRecordTimeRunnable(Runnable recordTimeRunnable) {
        this.recordTimeRunnable = recordTimeRunnable;
    }

    private Runnable recordTimeRunnable;//用于计时的runnable；

    public Runnable getRefreshUiRunnable() {
        return refreshUiRunnable;
    }

    public void setRefreshUiRunnable(Runnable refreshUiRunnable) {
        this.refreshUiRunnable = refreshUiRunnable;
    }

    public NotificationInfo getNotificationInfo() {
        return notificationInfo;
    }

    public void setNotificationInfo(NotificationInfo notificationInfo) {
        this.notificationInfo = notificationInfo;
    }

    public TaskRecordInfo() {
    }


    public String getTaskNote() {
        return taskNote;
    }

    public void setTaskNote(String taskNote) {
        this.taskNote = taskNote;
    }

    protected TaskRecordInfo(Parcel in) {
        beginTime = in.readLong();
        endTime = in.readLong();
        timeDuration = in.readLong();
        recordState = in.readInt();
        recordId = in.readString();
        notificationInfo = in.readParcelable(Thread.currentThread().getContextClassLoader());
    }

    public static final Creator<TaskRecordInfo> CREATOR = new Creator<TaskRecordInfo>() {
        @Override
        public TaskRecordInfo createFromParcel(Parcel in) {
            return new TaskRecordInfo(in);
        }

        @Override
        public TaskRecordInfo[] newArray(int size) {
            return new TaskRecordInfo[size];
        }
    };

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
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
        dest.writeLong(beginTime);
        dest.writeLong(endTime);
        dest.writeLong(timeDuration);
        dest.writeInt(recordState);
        dest.writeString(recordId);
        dest.writeParcelable(notificationInfo, 0);
    }
}
