package com.weqia;

import com.weqia.data.UtilsConstants;
import com.weqia.utils.L;
import com.weqia.utils.datastorage.file.PathUtil;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class BaseInit {

    public static Application ctx;
    private static BaseInit instance;

    public static BaseInit getInstance() {
        if (instance == null) {
            instance = new BaseInit();
        }
        return instance;
    }

    private BaseInit() {}

    /**
     * 用Storage之前一定要init下,不然会各种报错
     * 
     * @param ctx
     */
    public void init(Application ctx, String rootPath, boolean debug) {
        init(ctx, rootPath, null, debug);
    }
    
    /**
     * 用Storage之前一定要init下,不然会各种报错
     * 
     * @param ctx
     */
    public void init(Application ctx, String rootPath, String wqPath, boolean debug) {
        BaseInit.ctx = ctx;
        L.D = debug;
        PathUtil.initUtil(rootPath, wqPath);
    }
    
    public void showSql() {
        UtilsConstants.DEBUG_DB = true;
    }

    /**
     * @param fileString
     * @return SharedPreferences
     * @throws
     * @Title: getPreferences
     */
    public static SharedPreferences getPreferences(String fileString) {
        return ctx.getSharedPreferences(fileString, Context.MODE_PRIVATE);
    }

    /**
     * Context.MODE_PRIVATE：指定该SharedPreferences数据只能被本应用程序读、写<br>
     * Context.MODE_WORLD_READABLE：指定该SharedPreferences数据能被其他应用程序读，但不能写<br>
     * Context.MODE_WORLD_WRITEABLE：指定该SharedPreferences数据能被其他应用程序读写。<br>
     * 
     * @param fileString
     * @param mod
     * @return SharedPreferences
     * @throws
     * @Title: getPreferences
     */
    public static SharedPreferences getPreferences(String fileString, int mod) {
        return ctx.getSharedPreferences(fileString, mod);
    }
}
