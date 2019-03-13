package com.weqia.utils.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Looper;

import com.umeng.analytics.MobclickAgent;
import com.weqia.BaseInit;
import com.weqia.utils.DeviceUtil;
import com.weqia.utils.L;
import com.weqia.utils.data.ThrowableData;

/**
 * Description:获取错误信息以及手机相关信息 <br>
 * ExceptionMsg.java Create on 2013-1-3 下午6:38:44
 * 
 * @author Bewin berwinzheng@gmail.com
 * @version 1.0 Copyright (c) 2013 Company,Inc. All Rights Res
 * @updated 12-一月-2013 18:05:18
 */
public class ThrowableOperate {

    public static final String VERSION_UNKOWN_STRING = "version_unkown";


    /**
     * 
     * @Title: getMsg
     * @param tr
     * @return
     * @return String
     * @throws
     */
    public static String getMsg(Throwable tr) {
        ThrowableData dataThrowable =
                new ThrowableData(getErrorInfo(tr), DeviceUtil.getDeviceInfoByReflection(),
                        getVersionInfo());
        return dataThrowable.toString();
    }

    /**
     * 获取错误的信息
     * 
     * @param tr
     * @return
     */
    private static String getErrorInfo(Throwable tr) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        tr.printStackTrace(pw);
        pw.close();
        String error = writer.toString();
        return error;
    }

    /**
     * 
     * @Title: getVersionInfo
     * @return
     * @return String
     * @throws
     */
    private static String getVersionInfo() {
        try {
            PackageManager pm = BaseInit.ctx.getPackageManager();
            PackageInfo info = pm.getPackageInfo(BaseInit.ctx.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            CheckedExceptionHandler.handleException(e);
            return VERSION_UNKOWN_STRING;
        }
    }

    /**
     * 
     * @Title: handleException
     * @param tr
     * @param bShowToast
     * @param filePath
     * @return void
     * @throws
     */
    public static void operateException(final Throwable tr, final boolean checked) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                // String errorInfo = getMsg(tr);
                // if (errorInfo == null) {
                // return;
                // }
                if (L.D) L.e(getErrorInfo(tr));
                // if (bShow) {
                // L.toastShort(UtilsNotice.ERROR_APPLICATION_UI_EXCEPTION);
                // L.e(tr.getClass().getSimpleName(), tr);
                // }
                // FileUtil.writeStringToFile(filePath, errorInfo);

                MobclickAgent.reportError(BaseInit.ctx, checked
                        ? ("checked:" + getErrorInfo(tr))
                        : "error:" + getErrorInfo(tr));
                Looper.loop();
            }
        }).start();
    }
}
