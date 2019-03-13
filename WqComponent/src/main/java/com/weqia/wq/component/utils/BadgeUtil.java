package com.weqia.wq.component.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

public class BadgeUtil {

    public static void setBadgeCount(Context context, int count) {
        if (count <= 0) {
            count = 0;
        } else {
            count = Math.max(0, Math.min(count, 99));
        }
        if (Build.MANUFACTURER.toLowerCase().contains("samsung")) {
            sendToSamsumg(context, count);
        } else if (Build.MANUFACTURER.toLowerCase().contains("huawei")) {
            setHuaweiBadge(context, count);
        }
    }

    /**
     * 在华为手机上显示桌面徽标
     *
     * @param context
     * @param num
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void setHuaweiBadge(Context context, int num) {
        try {
            String launcherClassName = getLauncherClassName(context);
            if (launcherClassName == null) {
                return;
            }
            Bundle localBundle = new Bundle();
            localBundle.putString("package", context.getPackageName());
            localBundle.putString("class", launcherClassName);
            localBundle.putInt("badgenumber", num);
            context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, localBundle);
        } catch (Exception e) {

        }

    }

//    private static void sendToXiaoMi(Context context, int count) {
//        try {
//            Class miuiNotificationClass = Class.forName("android.app.MiuiNotification");
//            Object miuiNotification = miuiNotificationClass.newInstance();
//            Field field = miuiNotification.getClass().getDeclaredField("messageCount");
//            field.setAccessible(true);
//            field.set(miuiNotification, String.valueOf(count == 0 ? "" : count));  // 设置信息数-->这种发送必须是miui 6才行
//        } catch (Exception e) {
//            e.printStackTrace();
//            // miui 6之前的版本
//            Intent localIntent = new Intent(
//                    "android.intent.action.APPLICATION_MESSAGE_UPDATE");
//            localIntent.putExtra(
//                    "android.intent.extra.update_application_component_name",
//                    context.getPackageName() + "/" + getLauncherClassName(context));
//            localIntent.putExtra(
//                    "android.intent.extra.update_application_message_text", String.valueOf(count == 0 ? "" : count));
//            context.sendBroadcast(localIntent);
//        }
//    }
//
//
//    private static void sendToSony(Context context, int count) {
//        String launcherClassName = getLauncherClassName(context);
//        if (launcherClassName == null) {
//            return;
//        }
//        boolean isShow = true;
//        if (count == 0) {
//            isShow = false;
//        }
//        Intent localIntent = new Intent();
//        localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
//        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", isShow);//是否显示
//        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", launcherClassName);//启动页
//        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", String.valueOf(count));//数字
//        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context.getPackageName());//包名
//        context.sendBroadcast(localIntent);
//    }


    private static void sendToSamsumg(Context context, int count) {
        try {
            String launcherClassName = getLauncherClassName(context);
            if (launcherClassName == null) {
                return;
            }
            Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count", count);
            intent.putExtra("badge_count_package_name", context.getPackageName());
            intent.putExtra("badge_count_class_name", launcherClassName);
            context.sendBroadcast(intent);
        } catch (Exception e) {

        }
    }


    public static void resetBadgeCount(Context context) {
        setBadgeCount(context, 0);
    }


    private static String getLauncherClassName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ResolveInfo info = packageManager
                .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (info == null) {
            info = packageManager.resolveActivity(intent, 0);
        }
        return info.activityInfo.name;
    }

}