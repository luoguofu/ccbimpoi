package com.weqia.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.weqia.BaseInit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class DeviceUtil {

    private static final String marshmallowMacAddress = "02:00:00:00:00:00";
    private static final String fileAddressMac = "/sys/class/net/wlan0/address";

    public static String getAdresseMAC(Context context) {
        WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();

        if (wifiInf != null && marshmallowMacAddress.equals(wifiInf.getMacAddress())) {
            String result = null;
            try {
                result = getAdressMacByInterface();
                if (result != null) {
                    return result;
                } else {
                    result = getAddressMacByFile(wifiMan);
                    return result;
                }
            } catch (IOException e) {
                Log.e("MobileAccess", "Erreur lecture propriete Adresse MAC");
            } catch (Exception e) {
                Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
            }
        } else {
            if (wifiInf != null && wifiInf.getMacAddress() != null) {
                return wifiInf.getMacAddress();
            } else {
                return "";
            }
        }
        return marshmallowMacAddress;
    }

    private static String getAdressMacByInterface() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:", b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            }

        } catch (Exception e) {
            Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
        }
        return null;
    }

    private static String getAddressMacByFile(WifiManager wifiMan) throws Exception {
        String ret;
        int wifiState = wifiMan.getWifiState();

        wifiMan.setWifiEnabled(true);
        File fl = new File(fileAddressMac);
        FileInputStream fin = new FileInputStream(fl);
        ret = crunchifyGetStringFromStream(fin);
        fin.close();

        boolean enabled = WifiManager.WIFI_STATE_ENABLED == wifiState;
        wifiMan.setWifiEnabled(enabled);
        return ret;
    }

    private static String crunchifyGetStringFromStream(InputStream crunchifyStream) throws IOException {
        if (crunchifyStream != null) {
            Writer crunchifyWriter = new StringWriter();

            char[] crunchifyBuffer = new char[2048];
            try {
                Reader crunchifyReader = new BufferedReader(new InputStreamReader(crunchifyStream, "UTF-8"));
                int counter;
                while ((counter = crunchifyReader.read(crunchifyBuffer)) != -1) {
                    crunchifyWriter.write(crunchifyBuffer, 0, counter);
                }
            } finally {
                crunchifyStream.close();
            }
            return crunchifyWriter.toString();
        } else {
            return "No Contents";
        }
    }


    public static String getMac() {
        WifiManager wifi = (WifiManager) BaseInit.ctx.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }


    public static String getIMEI() {
        String imei =
                ((TelephonyManager) BaseInit.ctx.getSystemService(Context.TELEPHONY_SERVICE))
                        .getDeviceId();
        return imei;
    }

    public static String getIMSI() {
        TelephonyManager tel =
                (TelephonyManager) BaseInit.ctx.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = tel.getSubscriberId();
        return imsi;
    }

    public static String getSimSerialNumber() {
        TelephonyManager tel =
                (TelephonyManager) BaseInit.ctx.getSystemService(Context.TELEPHONY_SERVICE);
        String simserialnumber = tel.getSimSerialNumber();
        return simserialnumber;
    }


    // /**
    // * @return 获取屏幕宽度
    // */
    // public int getScreenWidth(Activity context) {
    // WindowManager windowManager = context.getWindowManager();
    // // Display display = windowManager.getDefaultDisplay();
    //
    // DisplayMetrics dm = new DisplayMetrics();
    // windowManager.getDefaultDisplay().getMetrics(dm);
    // // int height = dm.heightPixels;
    // int width = dm.widthPixels;
    // // int ca = display.getWidth();
    //
    // return width;
    // }

    /**
     * 获取屏幕的宽高
     * 
     * @return
     */
    public static Point getDeviceSize() {
        DisplayMetrics dm = BaseInit.ctx.getResources().getDisplayMetrics();
        Point size = new Point();
        size.x = dm.widthPixels;
        size.y = dm.heightPixels;
        return size;
    }

    /**
     * 检查SD卡是否可用
     * 
     * @return
     * @Description
     * @author Dminter
     */
    public static boolean isSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     */

    // public static final

    /**
     * @return DisplayMetrics
     * @throws
     * @Title: getDeviceMetrics
     */
    public static DisplayMetrics getDeviceMetrics() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) BaseInit.ctx.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * @return float
     * @throws
     * @Title: getDeviceDensity
     */
    public static float getDeviceDensity() {
        return getDeviceMetrics().density;
    }

    /**
     * @return int
     * @throws
     * @Title: getDeviceDesityDpi
     */
    public static int getDeviceDesityDpi() {
        return getDeviceMetrics().densityDpi;
    }

    /**
     * @return int
     * @throws
     * @Title: getDeviceHeight
     */
    public static int getDeviceHeight() {
        return getDeviceMetrics().heightPixels;
    }

    /**
     * @return int
     * @throws
     * @Title: getDeviceWidth
     */
    public static int getDeviceWidth() {
        return getDeviceMetrics().widthPixels;
    }

    /**
     * @param @return
     * @return String
     * @throws
     * @Title: getDeviceModel
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * @param @return
     * @return int
     * @throws
     * @Title: getOSSdkVersion
     */
    public static int getOSSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * @param @return
     * @return String
     * @throws
     * @Title: getOSVersion
     */
    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 通过反射获取手机硬件信息
     * 
     * @return String
     * @throws
     * @Title: getDeviceInfoByReflection
     */
    public static String getDeviceInfoByReflection() {
        StringBuffer sb = new StringBuffer();
        try {
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                sb.append(name + "=" + value);
                sb.append("\n");
            }
        } catch (Exception e) {}
        return sb.toString();
    }

    public static void call(Context ctx, String phoneNumber) {
        // 调用Android系统API打电话
        Uri uri = Uri.parse("tel:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        ctx.startActivity(intent);
    }

    public static void goCallUI(Context ctx, String phoneNumber) {
        // 跳转到Android系统打电话界面
        Uri uri = Uri.parse("tel:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        ctx.startActivity(intent);
    }

    public static void sms(Context ctx, String phoneNumber) {
        // 调用Android系统API发送短信
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        ctx.startActivity(intent);
    }

    public static Integer getVersionCode(Activity ctx) {

        // 获取packagemanager的实例
        PackageManager packageManager = ctx.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        Integer versionCode = packInfo.versionCode;
        return versionCode;
    }

    public static String getVersionName(Context ctx) {

        // 获取packagemanager的实例
        PackageManager packageManager = ctx.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }

}
