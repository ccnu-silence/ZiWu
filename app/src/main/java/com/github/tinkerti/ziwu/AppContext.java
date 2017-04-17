package com.github.tinkerti.ziwu;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by tiankui on 4/9/17.
 */

public class AppContext extends ContextWrapper {

    private String dbPath;

    public AppContext(Context base) {
        super(base);
    }

    public AppContext(Context base, String dbPath) {
        this(base);
        this.dbPath = dbPath;
    }

    @Override
    public File getDatabasePath(String name) {
        File file=new File(dbPath+File.separator+name);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        return file;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name),factory);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name).getAbsolutePath(),factory,errorHandler);
    }
}
