package com.github.tinkerti.ziwu.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.tinkerti.ziwu.AppContext;
import com.github.tinkerti.ziwu.data.Consts;

import java.io.File;

/**
 * Created by tiankui on 4/12/17.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static String DBPath;
    private static final int DB_VERSION = 2;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(new AppContext(context, DBPath), name, factory, DB_VERSION);
    }

    public static void setDBPath(Context context, String userId) {
        DBPath = context.getFilesDir().getAbsolutePath();
        DBPath = DBPath + File.separator + userId;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //两个planTable,一个记录添加创建时候的表
        //一个记录创建完成的表；
        db.execSQL(Consts.CREATE_PLAN_DETAIL_TABLE);
        db.execSQL(Consts.CREATE_RECORD_DETAIL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                updateDb1TO2(db);
                break;
        }
    }

    private void updateDb1TO2(SQLiteDatabase database) {
        String sql = "ALTER TABLE " + Consts.PLAN_DETAIL_TABLE_NAME + " ADD COLUMN planNote text";
        database.execSQL(sql);
    }
}
