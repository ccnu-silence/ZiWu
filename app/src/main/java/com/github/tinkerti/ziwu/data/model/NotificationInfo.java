package com.github.tinkerti.ziwu.data.model;

import android.app.Notification;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.RemoteViews;

/**
 * Created by tiankui on 7/1/17.
 */

public class NotificationInfo implements Parcelable {
    private int id;

    public RemoteViews getRemoteViews() {
        return remoteViews;
    }

    public void setRemoteViews(RemoteViews remoteViews) {
        this.remoteViews = remoteViews;
    }

    private RemoteViews remoteViews;

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    private Notification notification;

    public NotificationInfo(){}
    protected NotificationInfo(Parcel in) {
        id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationInfo> CREATOR = new Creator<NotificationInfo>() {
        @Override
        public NotificationInfo createFromParcel(Parcel in) {
            return new NotificationInfo(in);
        }

        @Override
        public NotificationInfo[] newArray(int size) {
            return new NotificationInfo[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
