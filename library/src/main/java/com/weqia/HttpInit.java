package com.weqia;

import android.app.Application;

import com.weqia.utils.datastorage.db.DbUtil;

public class HttpInit {

    private static HttpInit instance;
    public static Application ctx;
    private DbUtil dbUtil;

    public static HttpInit getInstance() {
        if (instance == null) {
            instance = new HttpInit();
        }
        return instance;
    }

    public HttpInit() {}

    /**
     * 用Bitmap之前一定要init下,不然会各种报错
     * 
     * @param ctx
     */
    public void init(Application ctx) {
        HttpInit.ctx = ctx;
    }

    public DbUtil getDbUtil() {
        return this.dbUtil;
    }

    public void setDbUtil(DbUtil dbUtil) {
        this.dbUtil = dbUtil;
    }
}
