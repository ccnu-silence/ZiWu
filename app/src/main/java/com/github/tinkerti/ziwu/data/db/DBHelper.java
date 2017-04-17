package com.github.tinkerti.ziwu.data.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
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


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(new AppContext(context,DBPath), name, factory, version, errorHandler);
    }

    public void setDBPath(Context context,String userId){
        DBPath=context.getFilesDir().getAbsolutePath();
        DBPath=DBPath+ File.separator+userId;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.CREATE_PLAN_DETAIL_TABLE);
        db.execSQL(Constants.CREATE_RECORD_DETAIL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
