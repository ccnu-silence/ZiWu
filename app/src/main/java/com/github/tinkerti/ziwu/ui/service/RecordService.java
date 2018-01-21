package com.github.tinkerti.ziwu.ui.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.github.tinkerti.ziwu.R;
import com.github.tinkerti.ziwu.data.Consts;
import com.github.tinkerti.ziwu.data.RecordTask;
import com.github.tinkerti.ziwu.data.model.NotificationInfo;
import com.github.tinkerti.ziwu.data.model.TaskRecordInfo;
import com.github.tinkerti.ziwu.ui.activity.MainActivity;
import com.github.tinkerti.ziwu.ui.utils.CommonUtils;
import com.github.tinkerti.ziwu.ui.utils.FormatTime;
import com.github.tinkerti.ziwu.ui.utils.ZLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.github.tinkerti.ziwu.data.Consts.SERVICE_RECORDING_PLAN_INFO_LIST;

/**
 * Created by tiankui on 4/30/17.
 */

public class RecordService extends Service {

    private static final String TAG = "RecordService";
    private Handler handler;
    private HashMap<String, TaskRecordInfo> recordInfoHashMap;
    private ArrayList<TaskRecordInfo> recordInfoArrayList;


    @Override
    public void onCreate() {
        super.onCreate();
        ZLog.d(TAG, "onCreate");
        HandlerThread handlerThread = new HandlerThread("ServiceWorkThread");
        //不要忘了调用start();
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        recordInfoHashMap = new HashMap<>();
        recordInfoArrayList = new ArrayList<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            List<TaskRecordInfo> recordInfoList = intent.getParcelableArrayListExtra(Consts.SERVICE_RECORDING_PLAN_INFO_LIST);
            if (recordInfoList != null) {
                for (TaskRecordInfo info : recordInfoList) {
                    recordInfoHashMap.put(info.getPlanId(), info);
                }
            }
        }
        return Service.START_STICKY; //返回这个
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new RecordServiceBinder();
    }

    public class RecordServiceBinder extends Binder {
        public HashMap<String, TaskRecordInfo> getRecordInfoHashMap() {
            return recordInfoHashMap;
        }

        public void startNewRecord(final TaskRecordInfo recordInfo) {
            ZLog.d(TAG, "start record:" + recordInfo.getPlanName());
            recordInfoHashMap.put(recordInfo.getPlanId(), recordInfo);
            recordInfoArrayList.add(recordInfo);
            showNotification(recordInfo);
            final Runnable startRecordRunnable = new Runnable() {
                @Override
                public void run() {
                    long timeDuration = System.currentTimeMillis() - recordInfo.getBeginTime();//矫正锁屏时计时不准确的问题
                    if (timeDuration > recordInfo.getTimeDuration()) {
                        recordInfo.setTimeDuration(timeDuration);
                    }
                    recordInfo.setTimeDuration(recordInfo.getTimeDuration() + 1000);

                    //用于更新通知栏的计时状态
                    if (recordInfo.getNotificationInfo() != null) {
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(recordInfo.getNotificationInfo().getId(), recordInfo.getNotificationInfo().getNotification());
                        if (recordInfo.getNotificationInfo().getRemoteViews() != null) {
                            recordInfo.getNotificationInfo().getRemoteViews().setTextViewText(R.id.tv_notification_record_time, FormatTime.calculateTimeString(recordInfo.getTimeDuration()));
                        }
                    }
                    ZLog.d(TAG, recordInfo.getPlanName() + "(runnable)" + this + " time duration:" + recordInfo.getTimeDuration());
                    recordInfo.setRecordState(Consts.RECORD_STATE_RECORDING);
                    handler.postDelayed(this, 1000);
                    recordInfo.setRecordTimeRunnable(this);
                }
            };
            handler.postDelayed(startRecordRunnable, 1000);
            recordInfo.setBeginTime(System.currentTimeMillis());
            recordInfo.setRecordId(UUID.randomUUID().toString());
            recordInfo.setRecordState(Consts.RECORD_STATE_RECORDING);
            recordInfo.setEndTime(System.currentTimeMillis());
            recordInfo.setRealRecordTime(0);
            RecordTask.getInstance().addTaskRecord(recordInfo);
        }

        public void stopRecord(TaskRecordInfo recordInfo, boolean isPause) {
            recordInfo.setEndTime(System.currentTimeMillis());
            recordInfo.setRealRecordTime(recordInfo.getEndTime() - recordInfo.getBeginTime());
            recordInfo.setRecordState(isPause ? Consts.RECORD_STATE_PAUSE : Consts.RECORD_STATE_STOP);
            RecordTask.getInstance().updateTaskRecord(recordInfo);

            ZLog.d(TAG, "stop record:" + recordInfo.getPlanName());
            handler.removeCallbacks(recordInfo.getRecordTimeRunnable());
            ZLog.d(TAG, "remove runnable " + recordInfo.getRecordTimeRunnable());
            if (!isPause) {
                recordInfo.setTimeDuration(0l);
            }
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (recordInfo.getNotificationInfo() != null) {
                notificationManager.cancel(recordInfo.getNotificationInfo().getId());
                stopForeground(true);
            }
            recordInfoArrayList.remove(recordInfo);
        }
    }

    private void showNotification(TaskRecordInfo recordInfo) {
        ZLog.d(TAG, "show notification");
        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.notification_content_view);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), Consts.NOTIFICATION_START_ACTIVITY, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.notification_icon)
                .setContent(notificationView)
                .setContentIntent(pendingIntent)
                .build();
        NotificationInfo notificationInfo = new NotificationInfo();
        notificationInfo.setId(CommonUtils.getID());
        notificationInfo.setNotification(notification);
        notificationInfo.setRemoteViews(notificationView);
        recordInfo.setNotificationInfo(notificationInfo);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notificationInfo.getId(), notification);
        startForeground(notificationInfo.getId(), notification);
    }

    @Override
    public void onDestroy() {
        ZLog.e(TAG, "onDestroy");
        Intent intent = new Intent(this, RecordService.class);
        intent.putParcelableArrayListExtra(SERVICE_RECORDING_PLAN_INFO_LIST, recordInfoArrayList);
        startService(intent);
    }
}
