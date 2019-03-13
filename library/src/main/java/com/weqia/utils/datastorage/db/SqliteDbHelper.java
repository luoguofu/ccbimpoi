package com.weqia.utils.datastorage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

class SqliteDbHelper extends SQLiteOpenHelper {

    private DbUpdateListener mDbUpdateListener;

    public SqliteDbHelper(Context context, String name, int version,
                          DbUpdateListener dbUpdateListener) {
        super(context, name, null, version);
        this.mDbUpdateListener = dbUpdateListener;
        getWritableDatabase().enableWriteAheadLogging();
    }

    public void onCreate(SQLiteDatabase db) {
    }

    private void runSqlNoException(SQLiteDatabase db, String sql) {
        try {
            db.execSQL(sql);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (mDbUpdateListener != null) {
            mDbUpdateListener.onUpgrade(db, oldVersion, newVersion);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (mDbUpdateListener != null) {
            mDbUpdateListener.onDowngrade(db, oldVersion, newVersion);
        }
    }
}
