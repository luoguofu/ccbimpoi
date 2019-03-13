package com.weqia.wq.component.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.weqia.wq.component.utils.request.UserService;

public class NetworkUtil {

    /**
     * 判断网络是否可用
     *
     * @param ctx
     * @return
     */
    public static boolean detect(Context ctx) {

        ConnectivityManager manager =
                (ConnectivityManager) ctx.getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            return false;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }

        return true;
    }


    public static boolean isConnServer() {

//        try {
//            InetAddress address = InetAddress.getByName(UserService.SERV_IP);
//            return address.isReachable(1000);
//        } catch (UnknownHostException e1) {
//            e1.printStackTrace();
//            return false;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }


        boolean isConn = false;
        URL url;
        HttpURLConnection conn = null;
        try {
            url = new URL("http://" + UserService.SERV_IP);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(1000 * 5);
            if (conn.getResponseCode() == 200) {
                isConn = true;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return isConn;
    }
}
