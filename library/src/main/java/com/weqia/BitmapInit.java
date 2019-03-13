package com.weqia;

import android.app.Application;
import android.graphics.Point;

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.weqia.utils.bitmap.BitmapUtil;
import com.weqia.utils.datastorage.db.DbUtil;

import java.util.LinkedList;

public class BitmapInit {

	public static final int AVATAR_DOWNLOAD_SUCCESS = -13;
    public static Application ctx;
    private UsingFreqLimitedMemoryCache maskImages;
    private LruMemoryCache<String, Point> cellPoint;
    private static BitmapInit instance;
    private DbUtil dbUtil;
    public static boolean round;

    private LinkedList<String> errList;

    public static boolean DEBUG_BITMAP = false;

    public static BitmapInit getInstance() {
        if (instance == null) {
            instance = new BitmapInit();
        }
        return instance;
    }

    private BitmapInit() {
    }

    /**
     * 用Bitmap之前一定要init下,不然会各种报错
     *
     * @param ctx
     */
    public void init(Application ctx, boolean round) {
        init(ctx, round, false);
    }

    /**
     * 用Bitmap之前一定要init下,不然会各种报错
     *
     * @param ctx
     */
    public void init(Application ctx, boolean round, boolean debugBitmap) {
        BitmapInit.ctx = ctx;
        BitmapUtil.getInstance();
        BitmapInit.round = round;
        DEBUG_BITMAP = debugBitmap;
    }

    /**
     * 用Bitmap之前一定要init下,不然会各种报错
     *
     * @param ctx
     */
    public void initBitmap(Application ctx, DbUtil dbUtil) {
        BitmapInit.ctx = ctx;
        this.dbUtil = dbUtil;
        BitmapUtil.getInstance();
    }

    public UsingFreqLimitedMemoryCache getMaskImages() {
        if (maskImages == null) {
            maskImages = new UsingFreqLimitedMemoryCache(10 * 1024 * 1024);
        }
        return maskImages;
    }

    /**
     * 获取通用的加载选项
     *
     * @return
     */

    public LruMemoryCache<String, Point> getCellPoint() {
        if (cellPoint == null) {
            cellPoint = new LruMemoryCache<String, Point>(200);
        }
        return cellPoint;
    }

    public DbUtil getDbUtil() {
        return dbUtil;
    }

    public void setDbUtil(DbUtil dbUtil) {
        this.dbUtil = dbUtil;
    }

    public LinkedList<String> getErrList() {
        if (errList == null) {
            errList = new LinkedList<>();
        }
        return errList;
    }
}
