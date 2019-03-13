package com.weqia.wq.data.global;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.MemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.weqia.BitmapInit;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.bitmap.BitmapUtil;
import com.weqia.utils.datastorage.file.PathUtil;
import com.weqia.utils.exception.CheckedExceptionHandler;
import com.weqia.utils.http.okgo.OkGo;
import com.weqia.utils.http.okgo.cache.CacheMode;
import com.weqia.wq.R;
import com.weqia.wq.RouterUtil;
import com.weqia.wq.UtilApplication;
import com.weqia.wq.component.db.WeqiaDbUtil;
import com.weqia.wq.component.notification.NotificationHelper;
import com.weqia.wq.component.utils.GlobalUtil;
import com.weqia.wq.component.utils.ShareUtil;
import com.weqia.wq.component.utils.autoupdate.UpdateUtil;
import com.weqia.wq.component.utils.bitmap.WqImageDownloader;
import com.weqia.wq.component.utils.request.ServiceRequester;
import com.weqia.wq.component.utils.request.UserService;
import com.weqia.wq.data.EnumData;
import com.weqia.wq.data.LoginUserData;
import com.weqia.wq.data.WPf;
import com.weqia.wq.data.WPfCommon;
import com.weqia.wq.data.WPfMid;
import com.weqia.wq.data.newdemand.MemberProjectPower;
import com.weqia.wq.data.paker.MyPackerNg;
import com.weqia.wq.global.ComponentUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import com.umeng.analytics.MobclickAgent.UMAnalyticsConfig;

/**
 * 全局对象
 *
 * @author Dminter
 */
public abstract class WeqiaApplication extends UtilApplication {
    private static WeqiaApplication instance;
    private LoginUserData loginUser;
    private Map<String, Integer> mFaceMap = new LinkedHashMap<String, Integer>();
    private List<Integer> gSendingIds;
    private static String gMCoId;
    private static float proSize;  //字体比例大小
    public WeqiaDbUtil dbUtil;

    private String lastState;
    private String lastNetInfo;
    private static Set<Integer> wantRfSet;
    private List<Activity> activities = new ArrayList<>();
    private ActivityManager myActivityManager;
    private static int avamem;

    public static boolean isLev = false;
    public static boolean isLogout = false;

    private UpdateUtil updateUtil;

    public static WeqiaApplication getInstance() {
        return instance;
    }

    public List<Activity> getActivities() {
        return activities;
    }
    private MemberProjectPower memPower;     //人员权限
    private boolean tourist;

    /**
     * 初始化程序数据
     */
    public void resetAppData() {
        if (dbUtil != null) {
            NotificationHelper.clearNotificationById();
        }
        isLogout = false;
        if (WPf.getInstance().getCachedObjs() != null) {
            WPf.getInstance().getCachedObjs().evictAll();
        }
        WPf.setInstance(null);
        WPfMid.setInstance(null);
        if (gSendingIds != null) {
            gSendingIds.clear();
            gSendingIds = null;
        }

        WeqiaDbUtil util = getDbUtil();
        if (util != null) {
            util.removeDbUtil(util);
        }

        gMCoId = null;
        proSize = 1.0f;

        if (wantRfSet != null) {
            wantRfSet.clear();
            wantRfSet = null;
        }
        UserService.resetHttp();
        getBitmapUtil().clearMemoryCache();
        BitmapInit.getInstance().getErrList().clear();
        ServiceRequester.getReqCache().evictAll();
        RouterUtil.routerActionSync(WeqiaApplication.ctx, "pvremotemsg", "acmsgstop");
        setLoginUser(null);

    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);

        SDKInitializer.initialize(this);
        // 为了防止被初始化2次，加此判断会保证被初始化1次
        if (processAppName == null || !processAppName.equalsIgnoreCase(instance.getPackageName())) {
            if (L.D) L.e("----service调用初始化");
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        ShareUtil.initShare();
        WPfCommon.getInstance();
        upDateMemInfo();
        configDb();
        initHttp();
        UserService.getHttpUtil();
        initImageLoader(instance);
        setBitmapUtil(null);
        initFaceMap();
        RouterUtil.routerActionSync(WeqiaApplication.ctx, "pvremotemsg", "acmsginit");
        initUpdate();
        // 如果没有使用PackerNg打包添加渠道，默认返回的是""
        // com.mcxiaoke.packer.helper.MyPackerNg
        final String market = MyPackerNg.getMarket(this, "cn_pinming");
        // 或者使用 MyPackerNg.getMarket(Context,defaultValue)
        // 之后就可以使用了，比如友盟可以这样设置

/*        if (StrUtil.notEmptyOrNull(market) && !market.endsWith("-dev")) {
            MobclickAgent.startWithConfigure(new UMAnalyticsConfig(ctx, ctx
                    .getString(R.string.umeng_key), market));
        }*/
        UMConfigure.init(ctx, ctx.getString(R.string.umeng_key), market,UMConfigure.DEVICE_TYPE_PHONE, null);
        MobclickAgent.setCatchUncaughtExceptions(true);
        proSize = WPfMid.getInstance().get(Hks.font_protion, Float.class, 1.0f);
//        if (L.D) L.e("proSize：" + proSize);
    }


//    //华为push需要在EMUI2.0以上使用所以添加此判断！
//    public static boolean bHuaweiPush(String s) {
//        if (StrUtil.isEmptyOrNull(s)) {
//            return false;
//        }
//        Pattern pattern = Pattern.compile("[1-9]{1,2}([.][0-9]{1,3})?");
//        Matcher matcher = pattern.matcher(s);
//        matcher.find();
//        String str = matcher.group();
//        if (StrUtil.isEmptyOrNull(str)) {
//            return false;
//        }
//        try {
//            Double b = Double.valueOf(str);
//            if (b >= 2.0) {
//                return true;
//            }
//        } catch (Exception e) {
//            return false;
//        }
//        return false;
//    }

    private void initUpdate() {
        updateUtil = UpdateUtil.create();
    }

    public UpdateUtil getUpdateUtil() {
        return updateUtil;
    }


    private void initHttp() {
        OkGo.init(this);
        OkGo.getInstance()//打开该调试开关,控制台会使用 红色error 级别打印log,并不是错误,是为了显眼,不需要就不要加入该行
                //如果使用默认的 60秒,以下三行也不需要传
//                .debug("weqia")
                .setConnectTimeout(20 * 1000)  //全局的连接超时时间
                .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS * 1)     //全局的读取超时时间
                .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)    //全局的写入超时时间
                //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 https://github.com/jeasonlzy0216/
                .setCacheMode(CacheMode.NO_CACHE)
//                .setCacheTime(TimeUtils.ONE_DAY)
                //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
//                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                //可以全局统一设置超时重连次数,默认为三次,那么最差的情况会请求4次(一次原始请求,三次重连请求),不需要可以设置为0
                .setRetryCount(0)
        //如果不想让框架管理cookie,以下不需要
//                .setCookieStore(new MemoryCookieStore())                //cookie使用内存缓存（app退出后，cookie消失）
//                .setCookieStore(new PersistentCookieStore())          //cookie持久化存储，如果cookie不过期，则一直有效
        ;
        //可以设置https的证书,以下几种方案根据需要自己设置
//                    .setCertificates()                                  //方法一：信任所有证书（选一种即可）
//                    .setCertificates(getAssets().open("srca.cer"))      //方法二：也可以自己设置https证书（选一种即可）
//                    .setCertificates(getAssets().open("aaaa.bks"), "123456", getAssets().open("srca.cer"))//方法三：传入bks证书,密码,和cer证书,支持双向加密
        //可以添加全局拦截器,不会用的千万不要传,错误写法直接导致任何回调不执行
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        return chain.proceed(chain.request());
//                    }
//                })
    }

    ;


    @SuppressWarnings("rawtypes")
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) instance.getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info =
                    (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return processName;
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
                        .imageDownloader(new WqImageDownloader(instance, 15 * 1000, 30 * 1000));
        if (cachFile != null) {
            build.diskCache(new UnlimitedDiskCache(cachFile));
        }
        ImageLoaderConfiguration config = build.build();
        BitmapUtil.getInstance().init(config);
    }

    @Override
    public void setBitmapUtil(BitmapUtil bitmapUtil) {
        super.setBitmapUtil(getBitmapUtilLoadContact());
    }

    public BitmapUtil getBitmapUtilLoadContact() {
        return BitmapUtil.getInstance();
    }

    public WeqiaDbUtil getDbUtil() {
        if (dbUtil == null) {
            L.i("initDbAndUser: dbUtil == null");
            ComponentUtil.initDbAndUser();
        }
        return dbUtil;
    }

    public void setDbUtil(WeqiaDbUtil dbUtil) {
        super.setDbUtil(dbUtil);
        this.dbUtil = dbUtil;
    }

    public MemberProjectPower getMemPower() {
        if (memPower == null) {
            memPower = WPfCommon.getInstance().get(Hks.project_member_power, MemberProjectPower.class);
        }
        return memPower;
    }

    public void setMemPower(MemberProjectPower memPower) {
        this.memPower = memPower;
        if (this.memPower == null) {
            WPfCommon.getInstance().remove(Hks.project_member_power);
        } else {
            WPfCommon.getInstance().put(Hks.project_member_power, memPower, true);
        }
    }

/*    *//**
     * 判断项目成员是否有权限，负责人和管理员权限全开，其他成员看具体权限
     *//*
    public boolean isPower(boolean isPower) {
        if (getMemPower().isProjectSuperAdmin() || getMemPower().isProjectManager()) {
            return true;
        } else {
            return isPower;
        }
    }*/
    /**
     * 配置数据库缓存
     *
     * @Description
     */
    private void configDb() {
        String databasesPath = "data/data/" + GlobalUtil.getPackage(ctx) + "/databases";
        File file = new File(databasesPath);
        if (!file.exists()) {
            file.mkdir();
        }
        // 2015年9月25日 覆盖数据库 【修改新名称实现】
        if (!(new File(GlobalUtil.getDbFileName(ctx)).exists())) {
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(GlobalUtil.getDbFileName(ctx));
                byte[] buf = new byte[1024];
                int count = 0;
                InputStream inputStream = ctx.getResources().openRawResource(R.raw.weiqiadb2);
                while ((count = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, count);
                }
                outputStream.close();
                inputStream.close();
            } catch (Exception e) {
                CheckedExceptionHandler.handleException(e);
            }
        }
    }

    public LoginUserData getLoginUser() {
        if (loginUser == null) {
            loginUser = WPfCommon.getInstance().get(Hks.user_info, LoginUserData.class);
        }
        return loginUser;
    }

    public static String getgMCoId() {
        return gMCoId;
    }

    public static float getProSize() {
        return proSize;
    }

    public static void setProSize(float proSize) {
        WeqiaApplication.proSize = proSize;
    }

    //
    public static void setgMCoId(String gMCoId) {
        WeqiaApplication.gMCoId = gMCoId;
    }

    public void setLoginUser(LoginUserData loginUser) {
        this.loginUser = loginUser;
        if (this.loginUser == null) {
            WPfCommon.getInstance().remove(Hks.user_info);
        } else {
            WPfCommon.getInstance().put(Hks.user_info, loginUser, true);
        }
    }

    private void initFaceMap() {
        mFaceMap.put("[害羞]", R.drawable.f000);
        mFaceMap.put("[微笑]", R.drawable.f001);
        mFaceMap.put("[撇嘴]", R.drawable.f002);
        mFaceMap.put("[色]", R.drawable.f003);
        mFaceMap.put("[发呆]", R.drawable.f004);
        mFaceMap.put("[得意]", R.drawable.f005);
        mFaceMap.put("[流泪]", R.drawable.f006);
        mFaceMap.put("[闭嘴]", R.drawable.f007);
        mFaceMap.put("[睡]", R.drawable.f008);
        mFaceMap.put("[大哭]", R.drawable.f009);
        mFaceMap.put("[尴尬]", R.drawable.f010);
        mFaceMap.put("[发怒]", R.drawable.f011);
        mFaceMap.put("[调皮]", R.drawable.f012);
        mFaceMap.put("[呲牙]", R.drawable.f013);
        mFaceMap.put("[惊讶]", R.drawable.f014);
        mFaceMap.put("[难过]", R.drawable.f015);
        mFaceMap.put("[酷]", R.drawable.f016);
        mFaceMap.put("[冷汗]", R.drawable.f017);
        mFaceMap.put("[抓狂]", R.drawable.f018);
        mFaceMap.put("[吐]", R.drawable.f019);
        mFaceMap.put("[偷笑]", R.drawable.f020);
        mFaceMap.put("[可爱]", R.drawable.f021);
        mFaceMap.put("[白眼]", R.drawable.f022);
        mFaceMap.put("[傲慢]", R.drawable.f023);
        mFaceMap.put("[饥饿]", R.drawable.f024);
        mFaceMap.put("[困]", R.drawable.f025);
        mFaceMap.put("[惊恐]", R.drawable.f026);
        mFaceMap.put("[流汗]", R.drawable.f027);
        mFaceMap.put("[憨笑]", R.drawable.f028);
        mFaceMap.put("[大兵]", R.drawable.f029);
        mFaceMap.put("[奋斗]", R.drawable.f030);
        mFaceMap.put("[咒骂]", R.drawable.f031);
        mFaceMap.put("[疑问]", R.drawable.f032);
        mFaceMap.put("[嘘]", R.drawable.f033);
        mFaceMap.put("[晕]", R.drawable.f034);
        mFaceMap.put("[折磨]", R.drawable.f035);
        mFaceMap.put("[衰]", R.drawable.f036);
        mFaceMap.put("[骷髅]", R.drawable.f037);
        mFaceMap.put("[敲打]", R.drawable.f038);
        mFaceMap.put("[再见]", R.drawable.f039);
        mFaceMap.put("[擦汗]", R.drawable.f040);
        mFaceMap.put("[抠鼻]", R.drawable.f041);
        mFaceMap.put("[鼓掌]", R.drawable.f042);
        mFaceMap.put("[糗大了]", R.drawable.f043);
        mFaceMap.put("[坏笑]", R.drawable.f044);
        mFaceMap.put("[左哼哼]", R.drawable.f045);
        mFaceMap.put("[右哼哼]", R.drawable.f046);
        mFaceMap.put("[哈欠]", R.drawable.f047);
        mFaceMap.put("[鄙视]", R.drawable.f048);
        mFaceMap.put("[委屈]", R.drawable.f049);
        mFaceMap.put("[快哭了]", R.drawable.f050);
        mFaceMap.put("[阴险]", R.drawable.f051);
        mFaceMap.put("[亲亲]", R.drawable.f052);
        mFaceMap.put("[吓]", R.drawable.f053);
        mFaceMap.put("[可怜]", R.drawable.f054);
        mFaceMap.put("[菜刀]", R.drawable.f055);
        mFaceMap.put("[西瓜]", R.drawable.f056);
        mFaceMap.put("[啤酒]", R.drawable.f057);
        mFaceMap.put("[篮球]", R.drawable.f058);
        mFaceMap.put("[乒乓球]", R.drawable.f059);
        mFaceMap.put("[咖啡]", R.drawable.f060);
        mFaceMap.put("[饭]", R.drawable.f061);
        mFaceMap.put("[猪头]", R.drawable.f062);
        mFaceMap.put("[玫瑰]", R.drawable.f063);
        mFaceMap.put("[凋谢]", R.drawable.f064);
        mFaceMap.put("[示爱]", R.drawable.f065);
        mFaceMap.put("[爱心]", R.drawable.f066);
        mFaceMap.put("[心碎]", R.drawable.f067);
        mFaceMap.put("[蛋糕]", R.drawable.f068);
        mFaceMap.put("[闪电]", R.drawable.f069);
        mFaceMap.put("[炸弹]", R.drawable.f070);
        mFaceMap.put("[刀]", R.drawable.f071);
        mFaceMap.put("[足球]", R.drawable.f072);
        mFaceMap.put("[飘虫]", R.drawable.f073);
        mFaceMap.put("[便便]", R.drawable.f074);
        mFaceMap.put("[月亮]", R.drawable.f075);
        mFaceMap.put("[太阳]", R.drawable.f076);
        mFaceMap.put("[礼物]", R.drawable.f077);
        mFaceMap.put("[拥抱]", R.drawable.f078);
        mFaceMap.put("[强]", R.drawable.f079);
        mFaceMap.put("[弱]", R.drawable.f080);
        mFaceMap.put("[握手]", R.drawable.f081);
        mFaceMap.put("[胜利]", R.drawable.f082);
        mFaceMap.put("[抱拳]", R.drawable.f083);
        mFaceMap.put("[勾引]", R.drawable.f084);
        mFaceMap.put("[拳头]", R.drawable.f085);
        mFaceMap.put("[差劲]", R.drawable.f086);
        mFaceMap.put("[爱你]", R.drawable.f087);
        mFaceMap.put("[NO]", R.drawable.f088);
        mFaceMap.put("[OK]", R.drawable.f089);
        mFaceMap.put("[爱情]", R.drawable.f090);
        mFaceMap.put("[飞吻]", R.drawable.f091);
        mFaceMap.put("[跳跳]", R.drawable.f092);
        mFaceMap.put("[发抖]", R.drawable.f093);
        mFaceMap.put("[怄火]", R.drawable.f094);
        mFaceMap.put("[转圈]", R.drawable.f095);
        mFaceMap.put("[磕头]", R.drawable.f096);
        mFaceMap.put("[回头]", R.drawable.f097);
        mFaceMap.put("[跳绳]", R.drawable.f098);
        mFaceMap.put("[挥手]", R.drawable.f099);
        mFaceMap.put("[哈哈]", R.drawable.f100);
        mFaceMap.put("[口罩]", R.drawable.f101);
        mFaceMap.put("[大笑]", R.drawable.f102);
        mFaceMap.put("[闭眼]", R.drawable.f103);
        mFaceMap.put("[吃惊]", R.drawable.f104);
        mFaceMap.put("[脸红]", R.drawable.f105);
        mFaceMap.put("[恐怖]", R.drawable.f106);
        mFaceMap.put("[思考]", R.drawable.f107);
        mFaceMap.put("[眨眼]", R.drawable.f108);
        mFaceMap.put("[满足]", R.drawable.f109);
        mFaceMap.put("[不高兴]", R.drawable.f110);
        mFaceMap.put("[恶魔]", R.drawable.f111);
        mFaceMap.put("[幽灵]", R.drawable.f112);
        mFaceMap.put("[心形礼盒]", R.drawable.f113);
        mFaceMap.put("[合掌]", R.drawable.f114);
        mFaceMap.put("[肌肉]", R.drawable.f115);
        mFaceMap.put("[钱]", R.drawable.f116);
        mFaceMap.put("[生日]", R.drawable.f117);
        mFaceMap.put("[派对]", R.drawable.f118);
        mFaceMap.put("[皇冠]", R.drawable.f119);
    }

    public Map<String, Integer> getFaceMap() {
        if (!mFaceMap.isEmpty()) return mFaceMap;
        return null;
    }

    @Override
    public void onLowMemory() {
        if (L.D) L.e("没有内存了，释放一下图片缓存");
        if (getBitmapUtil() != null) {
            getBitmapUtil().clearMemoryCache();
        }
        super.onLowMemory();
    }


    public List<Integer> getgSendingIds() {
        if (gSendingIds == null) {
            gSendingIds = new ArrayList<Integer>();
        }
        return gSendingIds;
    }

    public void setgSendingIds(List<Integer> gSendingIds) {
        this.gSendingIds = gSendingIds;
    }

    /**
     * 是否需要刷新
     *
     * @param refeshKey
     * @return
     */
    public static boolean wantRf(Integer refeshKey) {
        return wantRf(refeshKey, false);
    }

    /**
     * 是否移除
     *
     * @param refeshKey
     * @param remove
     * @return
     */
    public static boolean wantRf(Integer refeshKey, boolean remove) {
        if (refeshKey == null) {
            return false;
        }

        boolean bWant = getWantRfSet().contains(refeshKey);
        if (bWant) {
            if (L.D) L.e("刷新    " + refeshKey);
        }
        if (remove) {
            removeRf(refeshKey);
        }
        return bWant;
    }

    public static boolean wantRf(EnumData.RefeshKey refeshKey, boolean remove) {
        if (refeshKey == null) {
            return false;
        }

        boolean bWant = getWantRfSet().contains(refeshKey.value());
        if (bWant) {
            if (L.D) L.e("刷新    " + refeshKey.description());
        }
        if (remove) {
            removeRf(refeshKey);
        }
        return bWant;
    }

    public static boolean wantRf(EnumData.RefeshKey refeshKey) {
        if (refeshKey == null) {
            return false;
        }

        boolean bWant = getWantRfSet().contains(refeshKey.value());
        if (bWant) {
            if (L.D) L.e("刷新    " + refeshKey.description());
        }

        return bWant;
    }

    public static void removeRf(EnumData.RefeshKey refeshKey) {
        if (refeshKey == null) {
            return;
        }
        if (getWantRfSet().remove(refeshKey.value())) {
            if (L.D) L.e("移除标志    " + refeshKey.description());
        }
    }

    public static void addRf(EnumData.RefeshKey refeshKey) {
        if (refeshKey == null) {
            return;
        }
        if (getWantRfSet().add(refeshKey.value())) {
            if (L.D) L.e("添加标志    " + refeshKey.description());
        }
    }

    public static void removeRf(Integer refeshKey) {
        if (refeshKey == null) {
            return;
        }
        if (getWantRfSet().remove(refeshKey)) {
            EnumData.RefeshKey enumKey = EnumData.RefeshKey.valueOf(refeshKey);
            if (enumKey != null) {
                if (L.D) L.e("移除标志    " + enumKey.description());
            } else {
                if (L.D) L.e("移除标志    " + refeshKey);
            }
        }
    }

    public static void addRf(Integer refeshKey) {
        if (refeshKey == null) {
            return;
        }
        if (getWantRfSet().add(refeshKey)) {
            EnumData.RefeshKey enumKey = EnumData.RefeshKey.valueOf(refeshKey);
            if (enumKey != null) {
                if (L.D) L.e("添加标志    " + enumKey.description());
            } else  {
                if (L.D) L.e("添加标志    " + refeshKey);

            }
        }
    }

    private static Set<Integer> getWantRfSet() {
        if (wantRfSet == null) {
            wantRfSet = new HashSet<Integer>();
        }
        return wantRfSet;
    }

    public String getLastState() {
        return lastState;
    }

    public void setLastState(String lastState) {
        this.lastState = lastState;
    }

    public String getLastNetInfo() {
        return lastNetInfo;
    }

    public void setLastNetInfo(String lastNetInfo) {
        this.lastNetInfo = lastNetInfo;
    }

    public void setTourist(boolean tourist) {
        this.tourist = tourist;
    }

    public boolean isTourist() {
        if (WeqiaApplication.getInstance().getLoginUser() == null) {
            return false;
        } else {
            return WeqiaApplication.getInstance().getLoginUser().isTourist();
        }

    }
}
