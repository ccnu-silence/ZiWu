package com.github.tinkerti.ziwu.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.github.tinkerti.ziwu.data.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiankui on 4/17/17.
 */

public class TaskManager {

    private static String DB_NAME = "zw_db";
    private static int DB_VERSION = 1;
    private static TaskManager sInstance;
    private static DBHelper dbHelper;
    private static SQLiteDatabase db;
    private List<ITask> taskList;
    private Handler workHandler;
    private Handler uiHandler;

    private TaskManager() {
        HandlerThread handlerThread = new HandlerThread("handlerThread");
        handlerThread.start();
        workHandler = new Handler(handlerThread.getLooper());
        uiHandler = new Handler(Looper.getMainLooper());
        taskList = new ArrayList<>();
        taskList.add(new RecordTask());
        taskList.add(new PlanTask());
        for (ITask task : taskList) {
            task.onInit(this);
        }
    }

    public static void init(Context context) {
        sInstance = new TaskManager();
    }

    public static TaskManager getInstance() {
        return sInstance;
    }


    public static DBHelper getDbHelper() {
        return dbHelper;
    }

    public void loginSuccess(Context context, String userId) {
        openDB(context, userId);
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void openDB(Context context, String userId) {
        DBHelper.setDBPath(context, userId);
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
        db = dbHelper.getReadableDatabase();
    }

    public Handler getWorkHandler() {
        return workHandler;
    }

    public Handler getUiHandler() {
        return uiHandler;
    }
}
