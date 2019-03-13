package com.weqia.utils.datastorage.db;

import android.database.sqlite.SQLiteDatabase;

public interface DbUpdateListener {

    /**
     * 数据库升级
     * @Description
     * @param db
     * @param oldVersion
     * @param newVersion  
     *
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion);
}
