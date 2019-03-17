package com.example.ccbim.ccbimpoi;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.spinytech.macore.router.WideRouter;
import com.weqia.BaseInit;
import com.weqia.BitmapInit;
import com.weqia.HttpInit;
import com.weqia.utils.datastorage.db.DaoConfig;
import com.weqia.utils.datastorage.file.NativeFileUtil;
import com.weqia.wq.component.db.DBHelper;
import com.weqia.wq.component.db.WeqiaDbUtil;
import com.weqia.wq.component.utils.GlobalUtil;
import com.weqia.wq.data.WPfCommon;
import com.weqia.wq.data.global.Hks;
import com.weqia.wq.data.global.WeqiaApplication;

import java.io.File;

/**
 * Created by lgf on 2019/3/14.
 */

public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initDb(this);

    }
    public static void initDb(Application ctx) {
        WeqiaApplication.getInstance(ctx);
        BaseInit.getInstance().init(ctx, "CCBIM", "download", true);
        BitmapInit.getInstance().init(ctx, false, false);
        HttpInit.getInstance().init(ctx);
        DaoConfig config = new DaoConfig();
        WeqiaApplication weqiaApplication = WeqiaApplication.getInstance();
        if (weqiaApplication == null) {
            return;
        }
        config.setContext(ctx);
        config.setDbName("ccbimDb");
        int dbVersion = WPfCommon.getInstance().get(Hks.db_version, Integer.class, 0);
        if (dbVersion != WeqiaDbUtil.getDbVersion()) {
            if (dbVersion > WeqiaDbUtil.getDbVersion()) {
                NativeFileUtil.delFolder(new File(GlobalUtil.getDbFile(ctx)));
            }
            DBHelper.createAllTable();
            WPfCommon.getInstance().put(Hks.db_version, WeqiaDbUtil.getDbVersion());
        }
        final WeqiaDbUtil util = WeqiaDbUtil.create(config);
        WeqiaApplication.getInstance().setDbUtil(util);
    }

/*    @Override
    public void initializeAllProcessRouter() {
        WideRouter.registerLocalRouter("com.example.ccbim.ccbimpoi", MainRouterConnectService.class);

    }

    @Override
    protected void initializeLogic() {
        registerApplicationLogic("com.example.ccbim.ccbimpoi", 999, MainApplicationLogic.class);

    }

    @Override
    public boolean needMultipleProcess() {
        return true;
    }*/
}
