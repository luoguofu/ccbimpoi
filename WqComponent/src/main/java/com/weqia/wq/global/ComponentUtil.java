package com.weqia.wq.global;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.Spannable;
import android.text.style.URLSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.weqia.utils.CustomLinkMovementMethod;
import com.weqia.utils.DeviceUtil;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.TimeUtils;
import com.weqia.utils.datastorage.db.DaoConfig;
import com.weqia.utils.datastorage.db.sqlite.DbModel;
import com.weqia.utils.datastorage.file.NativeFileUtil;
import com.weqia.utils.datastorage.file.PathUtil;
import com.weqia.utils.exception.CheckedExceptionHandler;
import com.weqia.wq.R;
import com.weqia.wq.RouterUtil;
import com.weqia.wq.UtilApplication;
import com.weqia.wq.component.activity.SharedTitleActivity;
import com.weqia.wq.component.db.DBHelper;
import com.weqia.wq.component.db.WeqiaDbUtil;
import com.weqia.wq.component.modules.ShowInfoActivity;
import com.weqia.wq.component.qr.QRScanActivity;
import com.weqia.wq.component.sys.URLSpanUtils;
import com.weqia.wq.component.utils.AttachUtils;
import com.weqia.wq.component.utils.DialogUtil;
import com.weqia.wq.component.utils.DoubleClickImp;
import com.weqia.wq.component.utils.FileMiniUtil;
import com.weqia.wq.component.utils.GlobalUtil;
import com.weqia.wq.component.utils.request.ResultEx;
import com.weqia.wq.component.utils.request.ServiceParams;
import com.weqia.wq.component.utils.request.ServiceRequester;
import com.weqia.wq.component.utils.request.UserService;
import com.weqia.wq.component.view.flowlayout.FlowLayout;
import com.weqia.wq.component.view.title_pop.TitleItem;
import com.weqia.wq.component.view.title_pop.TitlePopup;
import com.weqia.wq.data.AttachmentData;
import com.weqia.wq.data.BucketFileData;
import com.weqia.wq.data.ComponentReqEnum;
import com.weqia.wq.data.EnumData;
import com.weqia.wq.data.LoginUserData;
import com.weqia.wq.data.PortData;
import com.weqia.wq.data.WPfCommon;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.data.global.Hks;
import com.weqia.wq.data.global.WeqiaApplication;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by berwin on 2017/8/15.
 */

public class ComponentUtil {

    public static int SINGLE_WIDTH = (int) (84 * DeviceUtil.getDeviceDensity());

    public static void putExtra(HashMap<String, String> dataMap, Intent intent) {
        if (dataMap == null || intent == null) {
            L.e("没有需要赋值action的对象，---");
            return;
        }
        if (dataMap != null) {
            for (String key : dataMap.keySet()) {
                intent.putExtra(key, dataMap.get(key));
            }
        }
    }

    // 获取十六进制的颜色代码.例如 "#6E36B4"
    public static String getRandColorCode() {
        String r, g, b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();
        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        return "#" + r + g + b;
    }

    public static void stripUnderlines(TextView textView) {
        textView.setMovementMethod(CustomLinkMovementMethod.getInstance());
    }

    public static void stripUnderlinesEditText(EditText editText) {
        if (null != editText && editText.getText() instanceof Spannable) {
            Spannable s = (Spannable) editText.getText();
            URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
            if (spans != null && spans.length > 0) {
                for (URLSpan span : spans) {
                    int start = s.getSpanStart(span);
                    int end = s.getSpanEnd(span);
                    s.removeSpan(span);
                    span = new URLSpanUtils(span.getURL());
                    s.setSpan(span, start, end, 0);
                }
            }
        }
    }

    // 刷新 图库，（及时显示图片）
    public static void refreshGallery(Context ctx, String filePath) {
        // ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("filePath://" +
        // filePath)));
        L.i("filePath: " + filePath);
        MediaScannerConnection.scanFile(ctx, new String[]{filePath}, null, null);
    }

    public static void setStatusBarDarkMode(boolean darkmode, Activity activity) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean setStatusBarDarkIcon(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }

    @TargetApi(19)
    public static void setTranslucentStatus(boolean on, Activity activity) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public static int dip2px(float dipValue) {
        final float scale = UtilApplication.ctx.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(float pxValue) {
        final float scale = UtilApplication.ctx.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据图片比例获取大小
     */
    public static Point getImagePoint(float picScale) {
        int defaultWidth =
                (int) (GlobalConstants.DEFAULT_SINGLE_PIC_WIDTH * DeviceUtil.getDeviceDensity());
        if (picScale != 0) {
            if (picScale <= 1) {
                return new Point((int) (defaultWidth * picScale), defaultWidth);
            } else if (picScale > 1) {
                return new Point(defaultWidth, (int) ((float) defaultWidth / (float) picScale));
            } else {
                return new Point(defaultWidth, defaultWidth);
            }
        }
        return new Point(defaultWidth, defaultWidth);
    }

    // 多张图展示gridview高度
    public static int gvHeight(int count, float apart) {
        int height = SINGLE_WIDTH;
        // (int) (DeviceUtil.getDeviceWidth() / part);
        switch (count) {
            case 1:
                height =
                        (int) ((GlobalConstants.DEFAULT_SINGLE_PIC_WIDTH * DeviceUtil
                                .getDeviceDensity()));
                break;
            case 2:
            case 3:
                height = height * 1;
                break;
            case 4:
            case 5:
            case 6:
                height = (int) (height * 2 + 3 * DeviceUtil.getDeviceDensity());
                break;
            case 7:
            case 8:
            case 9:
                height = (int) (height * 3 + 6 * DeviceUtil.getDeviceDensity());
                break;

            default:
                height = height * 1;
                break;
        }

        return height;
    }

    // 多张图展示gridview宽度
    public static int gvWidth(int count, float part) {
        int defaultWidth =
                (int) (GlobalConstants.DEFAULT_SINGLE_PIC_WIDTH * DeviceUtil.getDeviceDensity());
        // (int) (DeviceUtil.getDeviceWidth() / part)
        int width = SINGLE_WIDTH;
        switch (count) {
            case 1:  //一张图片时的图片宽度
                width = defaultWidth;
                break;
            case 2:  //二张图片时的图片宽度
                width = (int) (width * 2 + 3 * DeviceUtil.getDeviceDensity());
                break;
            case 3:  //三张图片时的图片宽度
                width = (int) (width * 3 + 6 * DeviceUtil.getDeviceDensity());
                break;
            case 4:  //四张图片时的图片宽度
                width = (int) (width * 2 + 3 * DeviceUtil.getDeviceDensity());
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:  //多张图片时的图片宽度
                width = (int) (width * 3 + 6 * DeviceUtil.getDeviceDensity());
                break;

            default:
                width = width * 1;
                break;
        }
        return width;
    }

    // 多张图展示gridview列数
    public static int gvNumColumns(int count) {

        int numColumns = 1;
        switch (count) {
            case 1:
                numColumns = 1;
                break;
            case 2:
                numColumns = 2;
                break;
            case 3:
                numColumns = 3;
                break;
            case 4:
                numColumns = 2;
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                numColumns = 3;
                break;
            default:
                numColumns = 1;
                break;
        }
        return numColumns;
    }

    // 双击大段文本预览
    public static void doubleTextPre(final Context ctx, TextView tvText, final String texts) {
        if (tvText == null || StrUtil.isEmptyOrNull(texts)) {
            return;
        }
        DoubleClickImp.registerDoubleClickListener(tvText,
                new DoubleClickImp.OnDoubleClickListener() {
                    @Override
                    public void OnSingleClick(View v) {

                    }

                    @Override
                    public void OnDoubleClick(View v) {
                        if (StrUtil.notEmptyOrNull(texts)) {
                            Intent newIntent = new Intent(ctx, ShowInfoActivity.class);
                            newIntent.putExtra("show", texts);
                            ctx.startActivity(newIntent);
                        }
                    }
                });
    }

    /**
     * 获取位置信息
     */
    public static String getAddressInfo(String adName, String addrStr) {
        if (StrUtil.notEmptyOrNull(adName) && !adName.equalsIgnoreCase("[位置]")) {
            return adName;
        } else if (StrUtil.notEmptyOrNull(addrStr)) {
            return addrStr;
        } else {
            return "位置信息";
        }
    }

    public static boolean isWiFiActive(Context inContext) {
        Context context = inContext.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // 是否含有文件
    public static boolean checkbFile(int sendId) {
        Integer count = 0;
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (dbUtil != null) {
            DbModel dbModel =
                    dbUtil.findDbModelBySQL("SELECT count(*) as count from wait_upfile WHERE sendId  = "
                            + sendId);
            if (dbModel != null) {
                try {
                    count = dbModel.getInt("count");
                } catch (Exception e) {
                    CheckedExceptionHandler.handleException(e);
                    return false;
                }
            }
        }

        return count > 0;
    }

    // 重新导入省市DB
    public static void configWqDb(Context ctx) {
        File file = new File(GlobalUtil.getDbFile(ctx));
        if (!file.exists()) {
            file.mkdir();
        }
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

    // 检查文件是否全部发送
    public static boolean checkFileSendStutus(int sendId) {
        Integer count = 0;
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (dbUtil != null) {
            DbModel dbModel =
                    dbUtil.findDbModelBySQL("SELECT count(*) as count from wait_upfile WHERE sendId = "
                            + sendId
                            + " AND sendStatus <> "
                            + EnumData.DataStatusEnum.SEND_SUCCESS.value()
                    );
            //            DbModel dbModel =
            //                    dbUtil.findDbModelBySQL("SELECT count(*) as count from wait_upfile WHERE sendId = "
            //                            + sendId
            //                            + " AND sendStatus <> "
            //                            + DataStatusEnum.SEND_SUCCESS.value()
            //                            + " AND gCoId  = '" + WeqiaApplication.getgMCoId() + "'");
            if (dbModel != null) {
                try {
                    count = dbModel.getInt("count");
                } catch (Exception e) {
                    CheckedExceptionHandler.handleException(e);
                }
            }
        }
        return count == 0;
    }

    // 实例化数据库
    @SuppressLint("SdCardPath")
    public static void initDbAndUser() {
        if (WeqiaApplication.getInstance().dbUtil != null) {
            if (L.D)
                L.e("数据库已经初始化，不需要再初始化");
            return;
        } else {
            if (L.D)
                L.e("数据库初始化");
        }
        LoginUserData loginInfo = WeqiaApplication.getInstance().getLoginUser();
        DaoConfig config = new DaoConfig();
        WeqiaApplication weqiaApplication = WeqiaApplication.getInstance();
        if (weqiaApplication == null) {
            return;
        }
        config.setContext(UtilApplication.ctx);
        if (loginInfo != null) {
            config.setDbName(loginInfo.getMid());
            int dbVersion = WPfCommon.getInstance().get(Hks.db_version, Integer.class, 0);
            if (dbVersion != WeqiaDbUtil.getDbVersion()) {
                if (dbVersion > WeqiaDbUtil.getDbVersion()) {
                    NativeFileUtil.delFolder(new File(GlobalUtil.getDbFile(UtilApplication.ctx)));
                }
                DBHelper.createAllTable();
                WPfCommon.getInstance().put(Hks.db_version, WeqiaDbUtil.getDbVersion());
            }
            final WeqiaDbUtil util = WeqiaDbUtil.create(config);
            WeqiaApplication.getInstance().setDbUtil(util);
        }
    }

    /**
     * 获取日期
     */
    public static String getTimeM_D(String inputStr) {
        Date inputDate = new Date();
        if (StrUtil.notEmptyOrNull(inputStr)) {
            inputDate = new Date(Long.parseLong(inputStr));
        }

        return (new SimpleDateFormat("MM-dd")).format(inputDate);
    }

    public static void writeLog(String log) {
        String CRASH_LOG = PathUtil.getCrashPath() + "/" + TimeUtils.getToDay() + ".txt";
        File f = new File(CRASH_LOG);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (Exception e) {
                return;
            }
        }
        StringBuffer sb = new StringBuffer();
        sb.append(log + "\n");
        writeToTxtByOutputStream(f, sb.toString());
    }

    public static void writeToTxtByOutputStream(File file, String content) {
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file, true));
            bufferedOutputStream.write(content.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // webview-...
    public static void initWebViewPopData(final Context ctx, TitlePopup titlePopup,
                                          TitlePopup.OnItemOnClickListener onItemClick) {

//        PlugConfig config = WeqiaApplication.getInstance().getPlugConfig();

//        titlePopup.addAction(new TitleItem(ctx, 0, "发送给", R.drawable.icon_fasonggei));
//        if (config.isWeibo()) {
//            titlePopup.addAction(new TitleItem(ctx, 1, "分享到同事圈", R.drawable.icon_circle_of_friends));
//        }
//        titlePopup.addAction(new TitleItem(ctx, 2, "分享到工作圈", R.drawable.icon_gongzuoquan));
//        titlePopup.addAction(new TitleItem(ctx, 3, "分享到其他应用", R.drawable.icon_fenxiang_yingyong));
        titlePopup.addAction(new TitleItem(ctx, 4, "在浏览器中打开", R.drawable.icon_liulanqi));
        titlePopup.addAction(new TitleItem(ctx, 5, "复制链接", R.drawable.icon_fuzhi));
        titlePopup.setItemOnClickListener(onItemClick);
    }

    static Dialog dlg = null;

    public static void copyTextView(final Activity ctx, TextView textView, final String text) {

        if (textView == null || StrUtil.isEmptyOrNull(text)) {
            return;
        }

        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dlg =
                        DialogUtil.initLongClickDialog(ctx, null, new String[]{"复制"},
                                new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        dlg.dismiss();
                                        StrUtil.copyText(text);
                                    }
                                });
                dlg.show();
                return true;
            }
        });
    }

    public static void autoKeyBoardShow(final EditText editText) {
        // new Handler().postDelayed(new Runnable() {
        //
        // @Override
        // public void run() {
        InputMethodManager inputManager =
                (InputMethodManager) editText.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
        // }
        // }, 100);
    }

    public static void hideKeyBoard(final EditText editText) {
        // new Timer().schedule(new TimerTask() {
        // public void run() {
        InputMethodManager inputManager =
                (InputMethodManager) editText.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        // }
        // }, 100);
    }

    /**
     * 日志
     */
    public static void log(Object string) {
        try {
            Log.i("[WQ-LOG]", String.valueOf(string));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void callPhoneNumber(Activity activity, String phoneNumber) {
        if (StrUtil.notEmptyOrNull(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
    }

    public static TextView getEmptyViewTextView(Context context, String text) {
        if (StrUtil.isEmptyOrNull(text)) {
            text = " ";
        }
        TextView textView = new TextView(context);
        textView.setLayoutParams(new FlowLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(context.getResources().getColor(R.color.black));
        textView.setText(text);
        return textView;
    }

    public static ImageView getEmptyViewImageView(Context context, int resId) {
        if (resId == 0) {
            resId = R.drawable.icon;
        }
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new FlowLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(resId);
        return imageView;
    }

    /**
     * 日志
     */
    public static void debug(Object string) {
        try {
            Log.i("[Bim360]", String.valueOf(string));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFirstUrl(String str) {
        List<String> ls = new ArrayList<String>();
        Pattern pattern = Patterns.WEB_URL;
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            ls.add(matcher.group());
            break;
        }
        if (ls.size() == 0) {
            return null;
        } else {
            return ls.get(0);
        }
    }

    /**
     * 统一的下载地址
     */
    public static String getDownApkUrl() {
        String serverIp = "";
        if (WPfCommon.getInstance().get(Hks.is_private_ip, Boolean.class, false)) {
            serverIp = WPfCommon.getInstance().get(Hks.private_ip, String.class);
        }
        return "http://" + "ccbim.pinming.cn" + "/apk.htm";
//        return "https://fir.im/vbs4";
    }


    public static void portClick(final SharedTitleActivity ctx, final PortData portData, String fromClass, final boolean bCanAction) {
        if (StrUtil.notEmptyOrNull(fromClass) && fromClass.equals("MobileSurfaceActivity")) {
            Intent back_intent = new Intent();
            back_intent.putExtra("viewInfo", portData.getViewInfo());
            ctx.setResult(Activity.RESULT_OK, back_intent);
            ctx.finish();
        } else {
            if (portData != null) {
                final AttachmentData attachmentData = new AttachmentData();
                String name = "";
                String fileName = "";
                String fileEnd = "";
                if (StrUtil.notEmptyOrNull(portData.getModelName())) {
                    fileName = portData.getModelName();
                    fileEnd = FileMiniUtil.getFileEnd(fileName);
                }
                /**
                 *5D工程，优先显示工程名称
                 */
                if (StrUtil.notEmptyOrNull(portData.getFileName())) {
                    name = portData.getFileName();
                    /**
                     *没有后缀无法下载，有了后缀不要重复加后缀
                     */
                    if (StrUtil.notEmptyOrNull(fileEnd) && !name.contains(".")) {
                        name = name + "." + fileEnd;
                    }
                } else if (StrUtil.notEmptyOrNull(fileName)) {
                    name = fileName;
                }
                L.e("下载文件的名称: " + name);
                attachmentData.setsType(1); //模型文件
                attachmentData.setName(name);
//                        attachmentData.setFileSize((portData.getFileSize() / 1024) + "");
                attachmentData.setNodeId(portData.getNodeId());
                attachmentData.setVideoPrew(portData.toString());
                if (StrUtil.notEmptyOrNull(portData.getPjId())) {
                    attachmentData.setPjId(portData.getPjId());
                }
                attachmentData.setbCanAction(bCanAction);

                if (StrUtil.isEmptyOrNull(portData.getVersionId())) {
                    L.toastShort("没有文件版本ID，直接返回");
                    return;
                }
                ServiceParams convParam = new ServiceParams(ComponentReqEnum.CONVERT_URL.order());
                convParam.put("versionId", portData.getVersionId());
                convParam.put("convertVersion", 1 + "");


                List<BucketFileData> files = ctx.getDbUtil().findAllByKeyWhere(BucketFileData.class, "versionId = '" + portData.getVersionId() + "'");
                BucketFileData bucketFileData = null;
                if (StrUtil.listNotNull(files)) {
                    bucketFileData = files.get(0);
                }

/*
                HashMap<String, String> dataMap = new HashMap<>();
                dataMap.put("filePath", "");
                dataMap.put("fileName", portData.getName());
//            dataMap.put("nodeId", projectModeData.getNodeId());
                String nodeId = portData.getNodeId();
                boolean selectMode = false;
                if (StrUtil.notEmptyOrNull(nodeId)) {
                    if (nodeId.startsWith(GlobalConstants.SELECT_TASK)) {
                        selectMode = true;
                        nodeId = nodeId.substring(GlobalConstants.SELECT_TASK.length());
                        dataMap.put("selectType", GlobalConstants.SELECT_TASK);
                    }
                    if (nodeId.startsWith(GlobalConstants.SELECT_DISCUSS)) {
                        selectMode = true;
                        nodeId = nodeId.substring(GlobalConstants.SELECT_DISCUSS.length());
                        dataMap.put("selectType", GlobalConstants.SELECT_DISCUSS);
                    }
                }
                L.e("nodeId = " + nodeId);
                dataMap.put("nodeId", nodeId);
                if (selectMode)
                    dataMap.put("selectMode", "1");

                dataMap.put("portInfo", portData.toString());
                dataMap.put("nodeType", 1 + "");
                dataMap.put("key_can_action", bCanAction ? "1" : "2");
//                dataMap.put("pjId", portData.getPjId());
                dataMap.put("versionId", portData.getVersionId());
//                dataMap.put("convertTime", portData.getConvertTime());
                RouterUtil.routerActionSync(ctx, "pvshowdraw", "acloadmode", dataMap);
*/

                if (bucketFileData != null && StrUtil.notEmptyOrNull(bucketFileData.getFileKey())) {
                    fileOpen(bucketFileData, attachmentData, ctx);
                    return;
                } else if (bucketFileData != null && StrUtil.listNotNull(bucketFileData.getFileConvertResultsSenior())) {
                    modeOpen(ctx, portData, bCanAction);
                    return;
                }

                UserService.getDataFromServer(true, convParam, new ServiceRequester() {
                    @Override
                    public void onResult(ResultEx resultEx) {
                        BucketFileData bucketFileData = resultEx.getDataObject(BucketFileData.class);
                        if (bucketFileData != null && StrUtil.notEmptyOrNull(bucketFileData.getFileKey())) {
                            bucketFileData.setVersionId(portData.getVersionId());
                            ctx.getDbUtil().save(bucketFileData);
                            fileOpen(bucketFileData, attachmentData, ctx);
                        } else if (bucketFileData != null&&StrUtil.listNotNull(bucketFileData.getFileConvertResultsSenior())) {
                            modeOpen(ctx, portData, bCanAction);
                        } else {
                            L.toastShort("模型还没有转换完成，请稍候查看！");
                            cleanQR();
                        }
                    }

                    @Override
                    public void onError(Integer errCode) {
                        cleanQR();
                        L.toastShort("模型还没有转换完成，请稍候查看！");
                    }
                });
            } else {
                cleanQR();
                L.e("文件出错，portData为空");
            }
        }
    }

    private static void cleanQR() {
        if (StrUtil.notEmptyOrNull(CoConfig.getQrPjId())) {
            CoConfig.setQrPjId(null);
            if (QRScanActivity.getInstance() != null) {
                QRScanActivity.getInstance().finish();
            }
        }
    }

    private static void modeOpen(final SharedTitleActivity ctx, final PortData portData, boolean bCanAction) {
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("filePath", "");
        dataMap.put("fileName", portData.getFileName());
//            dataMap.put("nodeId", projectModeData.getNodeId());
        String nodeId = portData.getNodeId();
        boolean selectMode = false;
        if (StrUtil.notEmptyOrNull(nodeId)) {
            if (nodeId.startsWith(GlobalConstants.SELECT_TASK)) {
                selectMode = true;
                nodeId = nodeId.substring(GlobalConstants.SELECT_TASK.length());
                dataMap.put("selectType", GlobalConstants.SELECT_TASK);
            }
            if (nodeId.startsWith(GlobalConstants.SELECT_DISCUSS)) {
                selectMode = true;
                nodeId = nodeId.substring(GlobalConstants.SELECT_DISCUSS.length());
                dataMap.put("selectType", GlobalConstants.SELECT_DISCUSS);
            }
        }
        L.e("nodeId = " + nodeId);
        dataMap.put("nodeId", nodeId);
        if (selectMode)
            dataMap.put("selectMode", "1");

        dataMap.put("portInfo", portData.toString());
        dataMap.put("nodeType", 1 + "");
        dataMap.put("key_can_action", bCanAction ? "1" : "2");
        dataMap.put("pjId", portData.getPjId());
        dataMap.put("versionId", portData.getVersionId());
//                dataMap.put("convertTime", portData.getConvertTime());
        RouterUtil.routerActionSync(ctx, "pvshowdraw", "acloadmode", dataMap);
    }

    private static void fileOpen(BucketFileData bucketFileData, AttachmentData attachmentData, SharedTitleActivity ctx) {
        String path = GlobalUtil.wrapBucketUrl(null,
                bucketFileData.getAccountType(),
                bucketFileData.getFileBucket(),
                bucketFileData.getFileKey());
        attachmentData.setUrl(path);
//                            String downName = "";
//                            String fileName = bucketFileData.getFileKey();
//                            String end = fileName.substring(fileName.lastIndexOf(".") + 1,
//                                    fileName.length()).toLowerCase();
//                            if (StrUtil.notEmptyOrNull(attachmentData.getName())) {
//                                String nameEnd = attachmentData.getName().substring(attachmentData.getName().lastIndexOf(".") + 1,
//                                        attachmentData.getName().length()).toLowerCase();
//                                if (StrUtil.isEmptyOrNull(nameEnd) || !nameEnd.equals(end)) {
//                                    downName = attachmentData.getName().subSequence(0, attachmentData.getName().lastIndexOf(".") + 1) + end;
//                                }
//                            } else {
//                                downName = fileName;
//                            }
//                            attachmentData.setName(downName);
        AttachUtils.attachClick(ctx, attachmentData, null);
    }
    public static List<String> getFileList(String fileStr) {
        List<String> ret = new ArrayList<>();
        if (StrUtil.isEmptyOrNull(fileStr)) {
            return null;
        }
        if (fileStr.contains(",")) {
            String tmp[] = fileStr.split(",");
            for (String str : tmp) {
                if (StrUtil.notEmptyOrNull(str)) {
                    ret.add(str);
                }
            }
        } else {
            ret.add(fileStr);
        }
        return ret;
    }
}
