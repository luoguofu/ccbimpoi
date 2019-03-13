package com.weqia.utils.datastorage.db;

import android.content.Context;

public class DaoConfig {
    //2015年5月26日 >144 4.0版本 升级保留消息列表
    public static final int dbVersion40 = 144;// 数据库版本
    public static final int dbVersion39 = 111;//3.9版本

    private Context context = null;// android上下文
    private String dbName = "weiqiadb.db";// 数据库名字

    private DbUpdateListener dbUpdateListener;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public DbUpdateListener getDbUpdateListener() {
        return dbUpdateListener;
    }

    public void setDbUpdateListener(DbUpdateListener dbUpdateListener) {
        this.dbUpdateListener = dbUpdateListener;
    }
}