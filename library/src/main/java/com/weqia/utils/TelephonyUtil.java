package com.weqia.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;


/**
 * Description: 手机信息功能类<br>
 * DeviceUtility.java Create on 2012-12-30 下午6:27:09
 * 
 * @author Bewin berwinzheng@gmail.com
 * @version 1.0 Copyright (c) 2012 Company,Inc. All Rights Res
 */
public class TelephonyUtil {



    /**
     * 连续点击处理
     */
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


    public static TelephonyManager getTm(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        return tm;

    }

    /**
     * 
     * @Title: getIMEI
     * @return String
     * @throws
     */
    public static String getIMEI(Context ctx) {
        return getTm(ctx).getDeviceId();
    }

    /**
     * 
     * @Title: getIMSI
     * @return String
     * @throws
     */
    public static String getIMSI(Context ctx) {
        return getTm(ctx).getSubscriberId();
    }

    /**
     * 
     * @Title: getPhoneNumber
     * @return String
     * @throws
     */
    public static String getPhoneNumber(Context ctx) {
        return getTm(ctx).getLine1Number();
    }

    /**
     * 电话状态：<br>
     * CALL_STATE_IDLE=0 无活动<br>
     * CALL_STATE_RINGING=1 响铃<br>
     * CALL_STATE_OFFHOOK=2 摘机<br>
     * 
     */
    public static int getCalState(Context ctx) {
        return getTm(ctx).getCallState();
    }

    /**
     * 当前使用的网络类型：<br>
     * NETWORK_TYPE_UNKNOWN 网络类型未知 0 <br>
     * NETWORK_TYPE_GPRS GPRS网络 1<br>
     * NETWORK_TYPE_EDGE EDGE网络 2<br>
     * NETWORK_TYPE_UMTS UMTS网络 3<br>
     * NETWORK_TYPE_HSDPA HSDPA网络 8<br>
     * NETWORK_TYPE_HSUPA HSUPA网络 9<br>
     * NETWORK_TYPE_HSPA HSPA网络 10<br>
     * NETWORK_TYPE_CDMA CDMA网络,IS95A 或 IS95B. 4<br>
     * NETWORK_TYPE_EVDO_0 EVDO网络, revision 0. 5<br>
     * NETWORK_TYPE_EVDO_A EVDO网络, revision A. 6<br>
     * NETWORK_TYPE_1xRTT 1xRTT网络 7<br>
     **/

    public static int getNetworkType(Context ctx) {
        return getTm(ctx).getNetworkType();
    }

    /**
     * SIM的状态信息：<br>
     * SIM_STATE_UNKNOWN 未知状态 0<br>
     * SIM_STATE_ABSENT 没插卡 1<br>
     * SIM_STATE_PIN_REQUIRED 锁定状态，需要用户的PIN码解锁 2<br>
     * SIM_STATE_PUK_REQUIRED 锁定状态，需要用户的PUK码解锁 3<br>
     * SIM_STATE_NETWORK_LOCKED 锁定状态，需要网络的PIN码解锁 4<br>
     * SIM_STATE_READY 就绪状态 5<br>
     **/
    public static int getSimState(Context ctx) {
        return getTm(ctx).getSimState();
    }

    /**
     * 手机信号类型：<br>
     * PHONE_TYPE_NONE 无信号<br>
     * PHONE_TYPE_GSM GSM信号<br>
     * PHONE_TYPE_CDMA CDMA信号<br>
     * PHONE_TYPE_SIP VOIP信号<br>
     **/
    public static int getPhoneType(Context ctx) {
        return getTm(ctx).getPhoneType();
    }

    /**
     * 服务商名称： 例如：中国移动、联通 SIM卡的状态必须是 SIM_STATE_READY
     **/
    public static String getSimOpreateName(Context ctx) {
        if (getSimState(ctx) == TelephonyManager.SIM_STATE_READY) {
            return getTm(ctx).getSimOperatorName();
        } else {
            return null;
        }
    }

    /**
     * 是否漫游: (在GSM用途下)
     **/
    public static boolean isRoaming(Context ctx) {
        if (getPhoneType(ctx) == TelephonyManager.PHONE_TYPE_GSM) {
            return getTm(ctx).isNetworkRoaming();
        } else {
            return false;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }
}
