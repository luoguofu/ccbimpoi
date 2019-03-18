package com.example.ccbim.ccbimpoi;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDex;


import com.linked.annotion.Modules;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.spinytech.macore.router.WideRouter;
import com.weqia.BaseInit;
import com.weqia.BitmapInit;
import com.weqia.HttpInit;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.bitmap.BitmapUtil;
import com.weqia.utils.datastorage.db.DaoConfig;
import com.weqia.utils.datastorage.file.NativeFileUtil;
import com.weqia.utils.datastorage.file.PathUtil;
import com.weqia.wq.component.db.DBHelper;
import com.weqia.wq.component.db.WeqiaDbUtil;
import com.weqia.wq.component.utils.GlobalUtil;
import com.weqia.wq.component.utils.bitmap.WqImageDownloader;
import com.weqia.wq.data.WPfCommon;
import com.weqia.wq.data.global.Hks;
import com.weqia.wq.data.global.WeqiaApplication;

import java.io.File;

/**
 * Created by lgf on 2019/3/14.
 */
//@Modules(modules = {"mdLibra"})
public class MyApplication extends Application {
    private static MyApplication instance;
    private ActivityManager myActivityManager;
    private static int avamem;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        upDateMemInfo();
        initDb(this);

    }
    public static void initDb(Application ctx) {
        WeqiaApplication.getInstance(ctx);
        BaseInit.getInstance().init(ctx, "CCBIM", "download", true);
        BitmapInit.getInstance().init(ctx, false, false);
        initImageLoader(ctx);
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
    // 更新可用内存信息
    public void upDateMemInfo() {
        myActivityManager = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        myActivityManager.getMemoryInfo(memoryInfo);
        avamem = (int) (memoryInfo.availMem / (1024 * 1024));
        if (L.D) L.e("ava_mem = " + avamem + "M");
    }

    public static void initImageLoader(Context context) {
        String cachePath = PathUtil.getCachePath();
        File cachFile = null;
        if (StrUtil.notEmptyOrNull(cachePath)) {
            cachFile = new File(cachePath);
        }

        int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 3);
        MemoryCache memoryCache;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            memoryCache =
                    new com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache(
                            memoryCacheSize);
        } else {
            memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
        }

        int maxMemory = ((int) Runtime.getRuntime().maxMemory()) / 1024 / 1024;
        if (L.D) L.e("max_mem = " + maxMemory + "M");
        maxMemory = maxMemory < avamem ? maxMemory : avamem;
        if (L.D) L.e("use_mem = " + ((int) Runtime.getRuntime().totalMemory()) / 1024 / 1024 + "M");
        if (L.D) L.e("free_mem = " + ((int) Runtime.getRuntime().freeMemory()) / 1024 / 1024 + "M");
        ImageLoaderConfiguration.Builder build =
                new ImageLoaderConfiguration.Builder(context).memoryCacheExtraOptions(900, 1680)
                        .threadPoolSize(6).threadPriority(Thread.NORM_PRIORITY - 2)
                        .denyCacheImageMultipleSizesInMemory().memoryCache(memoryCache)
                        .memoryCacheSize((maxMemory / 3) * 1024 * 1024)
                        .diskCacheSize(100 * 1024 * 1024)
                        .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                        .tasksProcessingOrder(QueueProcessingType.LIFO).diskCacheFileCount(5000)
                        // .diskCache(new UnlimitedDiskCache(cachFile))
                        .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                        .imageDownloader(new WqImageDownloader(WeqiaApplication.getInstance(), 15 * 1000, 30 * 1000));
        if (cachFile != null) {
            build.diskCache(new UnlimitedDiskCache(cachFile));
        }
        ImageLoaderConfiguration config = build.build();
        BitmapUtil.getInstance().init(config);
    }


/*    @Override
    public void initializeAllProcessRouter() {
        WideRouter.registerLocalRouter("com.example.ccbim.ccbimpoi", MainRouterConnectService.class);

    }

    @Override
    protected void initializeLogic() {
        registerApplicationLogic("com.example.ccbim.ccbimpoi", 999, PoiApplicationLogic.class);
        registerApplicationLogic("com.example.ccbim.ccbimpoi", 998, LibraApplicationLogic.class);

    }

    @Override
    public boolean needMultipleProcess() {
        return true;
    }*/
}
