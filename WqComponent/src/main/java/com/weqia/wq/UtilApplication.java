package com.weqia.wq;

import android.content.Context;
import android.content.res.Configuration;

import com.spinytech.macore.MaApplication;
import com.weqia.BaseInit;
import com.weqia.BitmapInit;
import com.weqia.HttpInit;
import com.weqia.utils.L;
import com.weqia.utils.bitmap.BitmapUtil;
import com.weqia.utils.datastorage.db.DbUtil;
import com.weqia.utils.exception.UnCheckedExceptiondHandler;

public abstract class UtilApplication extends MaApplication {

    //    public static String MID = "402880825e08c464015e121f478d0002";
//    public static String MID = "402880835d9695be015dcb0ad4310014";
    private static UtilApplication instance;
    public static Context ctx;

    private DbUtil udbUtil;
    private BitmapUtil bitmapUtil;

    public static UtilApplication getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        ctx = this.getApplicationContext();
        if (L.D) L.i("Start Application");
        UnCheckedExceptiondHandler handler = UnCheckedExceptiondHandler.getInstance();
        handler.init();

        BaseInit.getInstance().init(this, getString(R.string.co_root_path), getString(R.string.bim_down_path), true);
//        BaseInit.getInstance().init(this, getString(R.string.co_root_path), BuildConfig.DEBUG);
//        BaseInit.getInstance().showSql();
        BitmapInit.getInstance().init(this, false, false);
        HttpInit.getInstance().init(this);
    }

    public DbUtil getDbUtil() {
        return udbUtil;
    }

    public void setDbUtil(DbUtil dbUtil) {
        this.udbUtil = dbUtil;
        BitmapInit.getInstance().setDbUtil(dbUtil);
        HttpInit.getInstance().setDbUtil(dbUtil);
    }

    public BitmapUtil getBitmapUtil() {
        if (bitmapUtil == null) {
            bitmapUtil = BitmapUtil.getInstance();
        }
        return bitmapUtil;
    }

    public void setBitmapUtil(BitmapUtil bitmapUtil) {
        this.bitmapUtil = bitmapUtil;
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        L.e("程序退出了，哎");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        L.e("程序没有内存了");
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }
}
