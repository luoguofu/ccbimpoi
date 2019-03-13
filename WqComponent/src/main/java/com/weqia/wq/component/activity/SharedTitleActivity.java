package com.weqia.wq.component.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.weqia.utils.DeviceUtil;
import com.weqia.utils.StrUtil;
import com.weqia.utils.bitmap.BitmapUtil;
import com.weqia.wq.R;
import com.weqia.wq.component.LogoutHandler;
import com.weqia.wq.component.db.WeqiaDbUtil;
import com.weqia.wq.component.receiver.PushNotificationReceiver;
import com.weqia.wq.component.statusbar.StatusBarCompat;
import com.weqia.wq.component.view.TitleView;
import com.weqia.wq.data.BaseData;
import com.weqia.wq.data.LoginUserData;
import com.weqia.wq.data.eventbus.RefreshEvent;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.data.global.WeqiaApplication;
import com.weqia.wq.data.newdemand.AppManager;
import com.weqia.wq.data.push.PushData;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Set;

import static com.weqia.wq.component.utils.request.ServiceRequester.LEAVE_COMPANY;
import static com.weqia.wq.component.utils.request.ServiceRequester.LOGIN_OTH_DEVICE;

@SuppressLint("Registered")
public class SharedTitleActivity extends FragmentActivity implements OnClickListener {
    public TitleView sharedTitleView;
    private boolean bReceivePushNotification = false;
    private PushNotificationReceiver pushNotification;
    protected SharedTitleActivity pctx;
    private int guideResourceId = 0;// 引导页图片资源id
    private LogoutHandler logoutHandler;

    @Override
    protected void onStart() {
        super.onStart();
//        addGuideImage();// 添加引导页
    }

//    /**
//     * 添加引导图片
//     */
//    public void addGuideImage() {
//        View view = getWindow().getDecorView().findViewById(R.id.guid_view);// 查找通过setContentView上的根布局
//        if (view == null)
//            return;
//        if (GlobalUtil.activityIsGuided(this, this.getClass().getName())) {
//            // 引导过了
//            return;
//        }
//        ViewParent viewParent = view.getParent();
//        if (viewParent instanceof FrameLayout) {
//            final FrameLayout frameLayout = (FrameLayout) viewParent;
//            if (guideResourceId != 0) {// 设置了引导图片
//                final com.weqia.utils.view.CommonImageView guideImage =
//                        new com.weqia.utils.view.CommonImageView(this);
//                FrameLayout.LayoutParams params =
//                        new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                                ViewGroup.LayoutParams.MATCH_PARENT);
//                guideImage.setLayoutParams(params);
//                guideImage.setScaleType(com.weqia.utils.view.CommonImageView.ScaleType.FIT_XY);
//                guideImage.setImageResource(guideResourceId);
//                guideImage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        frameLayout.removeView(guideImage);
//                        GlobalUtil.setIsGuided(getApplicationContext(), SharedTitleActivity.this
//                                .getClass().getName());// 设为已引导
//                    }
//                });
//                frameLayout.addView(guideImage);// 添加引导图片
//            }
//        }
//    }

    /**
     * 子类在onCreate中调用，设置引导图片的资源id 并在布局xml的根元素上设置android:id="@id/guid_view"
     */
    protected void setGuideResId(int resId) {
        this.guideResourceId = resId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        //        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        loadView();
    }


    public View getContentView() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        FrameLayout content = (FrameLayout) view.getChildAt(0);
        return content.getChildAt(0);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        loadView();
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        loadView();
        pctx = this;

    }

    private void smoothSwitchScreen() {
        // 5.0以上修复了此bug
        //        L.e(Build.VERSION.SDK_INT + "----------------------");
        //        L.e(this.getClass().getName());
        //        if (isFullScreen(this)) {
        //            L.e("全屏");
        //        } else {
        //            L.e("不全屏----");
        //            // ViewGroup rootView = ((ViewGroup) this.findViewById(android.R.id.content));
        //            // int resourceId = getResources().getIdentifier("status_bar_height", "dimen",
        //            // "android");
        //            // int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        //            // rootView.setPadding(0, statusBarHeight, 0, 0);
        //            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        //            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //        }
    }

    /**
     * @param activity
     * @return 判断当前手机是否是全屏
     */
    public static boolean isFullScreen(Activity activity) {
        int flag = activity.getWindow().getAttributes().flags;
        if ((flag & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            return true;
        } else {
            return false;
        }
    }

    public void loadView() {
        sharedTitleView = new TitleView(this);
        sharedTitleView.loadView();
        sharedTitleView.setListener(this);
        smoothSwitchScreen();
        //设置状态栏的颜色
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.black));
    }

    public WeqiaDbUtil getDbUtil() {
        WeqiaDbUtil tmpDbUtil = WeqiaApplication.getInstance().getDbUtil();
        return tmpDbUtil;
    }

    public LoginUserData getLoginUser() {
        return WeqiaApplication.getInstance().getLoginUser();
    }

    public void setLoginUser(LoginUserData loginUser) {
        WeqiaApplication.getInstance().setLoginUser(loginUser);
    }

    public String getMid() {
        if (getLoginUser() != null) {
            return getLoginUser().getMid();
        } else {
            return null;
        }
    }

    public BitmapUtil getBitmapUtil() {
        return WeqiaApplication.getInstance().getBitmapUtil();
    }

    public boolean isbReceivePushNotification() {
        return bReceivePushNotification;
    }

    public void setbReceivePushNotification(boolean bReceivePushNotification) {
        this.bReceivePushNotification = bReceivePushNotification;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (bReceivePushNotification && pushNotification == null) {
            pushNotification = new PushNotificationReceiver() { // 实例化广播接收器

                @Override
                public void receivePushNotification(PushData datas) { // 实现抽象方法
                    if (datas == null || datas.getMsgType() == null) {
                        return;
                    }
                    receivePushNotifi(datas);
                }
            };
            IntentFilter intentFilter =
                    new IntentFilter(GlobalConstants.PUSH_NOTIFICATION_SERVICE_NAME);
            registerReceiver(pushNotification, intentFilter); // 注册广播
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        if (pushNotification != null) {
            unregisterReceiver(pushNotification);
            pushNotification = null;
        }
    }

    public void receivePushNotifi(PushData data) {
    }

    /**
     * @param cls
     * @param maps
     * @Description
     */
    public void startToActivity(Class<?> cls, Map<String, String> maps) {
        Intent newIntent = new Intent(this, cls);
        if (maps != null) {
            Set<String> keys = maps.keySet();
            for (String str : keys) {
                newIntent.putExtra(str, maps.get(str));
            }
        }
        startActivity(newIntent);
    }

    /**
     * @param cls
     * @param maps
     * @param requestCode
     * @Description
     */
    public void startToActivityForResult(Class<?> cls, Map<String, String> maps, int requestCode) {
        Intent newIntent = new Intent(this, cls);
        if (maps != null) {
            Set<String> keys = maps.keySet();
            for (String str : keys) {
                newIntent.putExtra(str, maps.get(str));
            }
        }
        startActivityForResult(newIntent, requestCode);
    }

    /**
     * @param cls
     * @Description
     */
    public void startToActivity(Class<?> cls) {
        Intent newIntent = new Intent(this, cls);
        startActivity(newIntent);
    }


    public void startToActivity(Class<?> cls, BaseData data) {
        startToActivity(cls, null, null, data, null);
    }

    /**
     * 跳转到带title的页面
     *
     * @param cls
     * @param title
     * @Description
     */
    public void startToActivity(Class<?> cls, String title) {
        startToActivity(cls, title, null, null, null);
    }


    /**
     * 跳转到带title的页面
     *
     * @param cls
     * @param title
     * @Description
     */
    public void startToActivity(Class<?> cls, String title, String coId) {
        startToActivity(cls, title, coId, null, null);
    }

    /**
     * 带数据，带title
     *
     * @param cls
     * @param title
     * @param data
     * @Description
     */
    public void startToActivity(Class<?> cls, String title, BaseData data) {
        startToActivity(cls, title, null, data, null);
    }

    /**
     * 带数据，带title
     *
     * @param cls
     * @param title
     * @param data
     * @Description
     */
    public void startToActivity(Class<?> cls, String title, String coId, BaseData data) {
        startToActivity(cls, title, coId, data, null);
    }

    public void startToActivity(Class<?> cls, String title, BaseData data, int bottomIndex) {
        startToActivity(cls, title, null, data, bottomIndex);
    }

    public void startToActivity(Class<?> cls, String title, String coId, BaseData data, Integer bottomIndex) {
        Intent newIntent = new Intent(this, cls);
        if (bottomIndex != null) {
//            newIntent.setFlags(flag);
            newIntent.putExtra(GlobalConstants.KEY_TOP_BANNER_INT, bottomIndex);
        }


        if (StrUtil.notEmptyOrNull(title)) {
            newIntent.putExtra(GlobalConstants.KEY_TOP_BANNER_TITLE, title);

        }
        if (data != null) {
            newIntent.putExtra(GlobalConstants.KEY_PARAM_DATA, data);
        }
        if (StrUtil.notEmptyOrNull(coId)) {
            newIntent.putExtra(GlobalConstants.KEY_COID, coId);
        }
        startActivity(newIntent);
    }

    /**
     * @param cls
     * @param requestCode
     * @Description
     */
    public void startToActivityForResult(Class<?> cls, int requestCode) {
        Intent newIntent = new Intent(this, cls);
        startActivityForResult(newIntent, requestCode);
    }


    public void startToActivityForResult(Class<?> cls, BaseData data, int requestCode) {
        startToActivityForResult(cls, null, null, data, null, requestCode);
    }

    /**
     * 跳转到带title的页面
     *
     * @param cls
     * @param title
     * @Description
     */
    public void startToActivityForResult(Class<?> cls, String title, int requestCode) {
        startToActivityForResult(cls, title, null, null, null, requestCode);
    }


    /**
     * 跳转到带title的页面
     *
     * @param cls
     * @param title
     * @Description
     */
    public void startToActivityForResult(Class<?> cls, String title, String coId, int requestCode) {
        startToActivityForResult(cls, title, coId, null, null, requestCode);
    }

    /**
     * 带数据，带title
     *
     * @param cls
     * @param title
     * @param data
     * @Description
     */
    public void startToActivityForResult(Class<?> cls, String title, BaseData data, int requestCode) {
        startToActivityForResult(cls, title, null, data, null, requestCode);
    }

    /**
     * 带数据，带title
     *
     * @param cls
     * @param title
     * @param data
     * @Description
     */
    public void startToActivityForResult(Class<?> cls, String title, String coId, BaseData data,
                                         int requestCode) {
        startToActivityForResult(cls, title, coId, data, null, requestCode);
    }

    public void startToActivityForResult(Class<?> cls, String title, BaseData data, int flag,
                                         int requestCode) {
        startToActivityForResult(cls, title, null, data, flag, requestCode);
    }

    public void startToActivityForResult(Class<?> cls, String title, String coId, BaseData data,
                                         Integer flag, int requestCode) {
        Intent newIntent = new Intent(this, cls);
        if (flag != null) {
            newIntent.setFlags(flag);
        }
        if (StrUtil.notEmptyOrNull(title)) {
            newIntent.putExtra(GlobalConstants.KEY_TOP_BANNER_TITLE, title); // 传递标题的值
        }
        if (data != null) {
            newIntent.putExtra(GlobalConstants.KEY_PARAM_DATA, data);
        }
        if (StrUtil.notEmptyOrNull(coId)) {
            newIntent.putExtra(GlobalConstants.KEY_COID, coId); // 传递企业的ID
        }
        startActivityForResult(newIntent, requestCode); // 请求码
    }

    public void startToActivitySelectDataForResult(Class<?> cls, String requestKey, int requestCode) {
        startToActivitySelectDataForResult(cls, requestKey, requestCode, null, null);
    }

    public void startToActivitySelectDataForResult(Class<?> cls, int requestCode, Bundle bundle) {
        startToActivitySelectDataForResult(cls, null, requestCode, null, bundle);
    }

    public void startToActivitySelectDataForResult(Class<?> cls, String isSelect, int requestCode, BaseData data, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        if (StrUtil.notEmptyOrNull(isSelect)) {
            intent.putExtra(isSelect, true);
        }
        if (data != null) {
            intent.putExtra(GlobalConstants.KEY_PARAM_DATA, data);
        }
        if (bundle != null) {
            intent.putExtra(GlobalConstants.KEY_BUNDLE_DATA, bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    public static abstract class UIHandler extends Handler {
        WeakReference<SharedTitleActivity> viewController;

        public UIHandler(SharedTitleActivity activity) {
            viewController = new WeakReference<SharedTitleActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SharedTitleActivity baseViewController = viewController.get();
            handleMessage(msg, baseViewController);
        }

        public abstract void handleMessage(Message msg, SharedTitleActivity viewController);
    }

    /**
     * @param point
     * @param ivImg
     */
    public void resetCellImgSize(Point point, ImageView ivImg) {
        if (point == null) {
            return;
        }
        if (ivImg != null) {
            if (ivImg.getParent() instanceof RelativeLayout) {
                RelativeLayout.LayoutParams params =
                        (RelativeLayout.LayoutParams) ivImg.getLayoutParams();
                params.width = point.x;
                params.height = point.y;
                ivImg.setLayoutParams(params);
            } else if ((ivImg.getParent() instanceof FrameLayout)) {
                FrameLayout.LayoutParams params =
                        (FrameLayout.LayoutParams) ivImg.getLayoutParams();
                params.width = point.x;
                params.height = point.y;
                ivImg.setLayoutParams(params);
            } else if (ivImg.getParent() instanceof LinearLayout) {
                LinearLayout.LayoutParams params =
                        (LinearLayout.LayoutParams) ivImg.getLayoutParams();
                params.width = point.x;
                params.height = point.y;
                ivImg.setLayoutParams(params);
            }
            int defaultWidth =
                    (int) (GlobalConstants.DEFAULT_SINGLE_PIC_WIDTH * DeviceUtil.getDeviceDensity());
            float scale = (float) point.x / (float) point.y;
            if (point.x == point.y && point.x == defaultWidth) {
                ivImg.setScaleType(ScaleType.CENTER_CROP);
            } else {
                if (scale == 0.5 || scale == 2) {
                    ivImg.setScaleType(ScaleType.CENTER_CROP);
                } else {
                    ivImg.setScaleType(ScaleType.FIT_XY);
                }
            }
        }
    }


    public SharedTitleActivity getCtx() {
        return pctx;
    }

    public void setCtx(SharedTitleActivity ctx) {
        this.pctx = ctx;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            UMShareAPI mShareAPI = UMShareAPI.get(this);
            mShareAPI.onActivityResult(requestCode, resultCode, data);
        }
    }

    //    @Override
    //    public void onConfigurationChanged(Configuration newConfig) {
    //        // super.onConfigurationChanged(newConfig);
    //
    //    }

    @Override
    public void onClick(View v) {
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_HOME
                && event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
            this.moveTaskToBack(true);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public LogoutHandler getLogoutHandler() {
        if (logoutHandler == null) {
            logoutHandler = new LogoutHandler(this);
        }
        return logoutHandler;
    }

    // 添加成功 刷新界面
    public void logoutEvent(RefreshEvent event) {
        int type = event.type;
        if (type == LOGIN_OTH_DEVICE) {
            getLogoutHandler().showLogoutDialog();
        } else if (type == LEAVE_COMPANY) {
            getLogoutHandler().showLeaveDialog();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
