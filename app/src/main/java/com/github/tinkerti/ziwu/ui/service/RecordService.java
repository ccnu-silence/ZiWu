package com.github.tinkerti.ziwu.ui.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.github.tinkerti.ziwu.R;
import com.github.tinkerti.ziwu.data.Constants;
import com.github.tinkerti.ziwu.data.model.PlanRecordInfo;
import com.github.tinkerti.ziwu.ui.activity.MainActivity;

/**
 * Created by tiankui on 4/30/17.
 */

public class RecordService extends Service {

    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread handlerThread = new HandlerThread("ServiceWorkThread");
        //不要忘了调用start();
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new RecordServiceBinder();
    }

    public class RecordServiceBinder extends Binder {

        public void startRecord(final PlanRecordInfo recordInfo) {
            showNotification();
            final Runnable startRecordRunnable = new Runnable() {
                @Override
                public void run() {
                    recordInfo.setTimeDuration(recordInfo.getTimeDuration() + 1000);
                    recordInfo.setRecordState(Constants.RECORD_STATE_RECORDING);
                    handler.postDelayed(this,1000);
                }
            };
            handler.postDelayed(startRecordRunnable, 1000);
        }
    }

    private void showNotification() {
        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.notification_content_view);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), Constants.NOTIFICATION_START_ACTIVITY, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(getApplicationContext())
                .setContent(notificationView)
                .setContentIntent(pendingIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(101, notification);
        startForeground(1, notification);
    }
}
