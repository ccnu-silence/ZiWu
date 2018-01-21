package com.github.tinkerti.ziwu.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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

    private TaskManager() {
        taskList = new ArrayList<>();
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

    public static void setDbHelper(DBHelper dbHelper) {
        TaskManager.dbHelper = dbHelper;
    }

    public void loginSuccess(Context context, String userId) {
        openDB(context, userId);
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void setDb(SQLiteDatabase db) {
        TaskManager.db = db;
    }

    public void openDB(Context context, String userId) {
        DBHelper.setDBPath(context, userId);
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
        db = dbHelper.getReadableDatabase();
    }


}
