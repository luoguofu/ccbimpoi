package com.weqia.wq.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.wq.component.utils.request.UserService;
import com.weqia.wq.data.global.WeqiaApplication;

/**
 * Android 利用广播BroadCast监听网络的变化
 */
public class NetChangeBroadCast extends BroadcastReceiver {
    State wifiState = null;
    State mobileState = null;
    public static final String ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION.equals(intent.getAction())) {
            // L.e("网络变化了");
            // 获取手机的连接服务管理器，这里是连接管理器类
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiInfo != null) {
                wifiState = wifiInfo.getState();
            }
            NetworkInfo mobileInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mobileInfo != null) {
                mobileState = mobileInfo.getState();
            }

            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();

            String lastsState = WeqiaApplication.getInstance().getLastState();
            if (wifiState != null && mobileState != null) {
                if (StrUtil.notEmptyOrNull(lastsState)) {
                    String[] states = lastsState.split(":");
                    if (states.length == 3) {
                        State tmpWifi = State.valueOf(states[0]);
                        State tmpMoile = State.valueOf(states[1]);
                        String ipAddress = states[2];
                        int ip = 0;
                        if (StrUtil.isEmptyOrNull(ipAddress)) {
                            ipAddress = "";
                        } else {
                            try {
                                ip = Integer.parseInt(ipAddress);
                            } catch (NumberFormatException e) {
                                ip = 0;
                            }
                        }
                        if (wifiState.compareTo(tmpWifi) == 0
                                && mobileState.compareTo(tmpMoile) == 0) {
                            if (wifiState.compareTo(tmpWifi) == 0 && info.getIpAddress() != ip) {
                                if (L.D) L.e("两次wifi不一样，还是要重启网络");
                            } else {
                                if (L.D) L.e("网络没有变化");
                                return;
                            }
                        }
                    }
                }

                if (State.CONNECTED != wifiState && State.CONNECTED == mobileState) {
                    if (L.D) L.e("已连接移动网络" + cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getExtraInfo());
                } else if (State.CONNECTED == wifiState && State.CONNECTED != mobileState) {
                    if (L.D) L.e("已连接Wifi网络");
                } else if (State.CONNECTED != wifiState && State.CONNECTED != mobileState) {
                    if (L.D) L.e("无网络连接");
                    L.toastLong("无网络连接,请检查网络！");
                }
                netChangDo();

            } else {
                netChangDo();
                if (L.D) L.e("网络有情况为空,需要处理");
            }
            WeqiaApplication.getInstance().setLastState(
                    wifiState + ":" + mobileState + ":" + info.getIpAddress());
        }
    }

    private void netChangDo() {
        UserService.resetHttp();
//        XUtil.sendMsgRead();
//        PunchUtil.setAutoAlarm(System.currentTimeMillis() + 1000);
    }
}
