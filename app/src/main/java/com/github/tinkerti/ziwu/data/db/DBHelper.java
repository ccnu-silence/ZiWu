package com.github.tinkerti.ziwu.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.tinkerti.ziwu.AppContext;
import com.github.tinkerti.ziwu.data.Constants;

import java.io.File;

/**
 * Created by tiankui on 4/12/17.
 */

public class DBHelper extends SQLiteOpenHelper {


    private static String DBPath;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(new AppContext(context,DBPath), name, factory, version);
    }

    public static void setDBPath(Context context,String userId) {
        DBPath = context.getFilesDir().getAbsolutePath();
        DBPath = DBPath + File.separator + userId;
    }

    private static boolean mainTmpDirSet = false;

    @Override
    public SQLiteDatabase getWritableDatabase() {
        if (!mainTmpDirSet) {
            boolean rs = new File(DBPath+"main").mkdir();
            super.getWritableDatabase().execSQL("PRAGMA temp_store_directory = '/data/user/0/com.github.tinkerti.ziwu/files/userId/zw_db/main'");
            mainTmpDirSet = true;
            return super.getWritableDatabase();
        }
        return super.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //两个planTable,一个记录添加创建时候的表
        //一个记录创建完成的表；
        db.execSQL(Constants.CREATE_ADD_PLAN_DETAIL_TABLE);
        db.execSQL(Constants.CREATE_PLAN_DETAIL_TABLE);
        db.execSQL(Constants.CREATE_RECORD_DETAIL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
