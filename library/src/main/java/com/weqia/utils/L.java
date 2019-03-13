package com.weqia.utils;

import android.widget.Toast;

import com.weqia.BaseInit;
import com.weqia.utils.LogContent.Type;

/**
 * Description:日志工程类<br>
 * L.java Create on 2012-12-30 下午7:38:10
 * 
 * @author Bewin berwinzheng@gmail.com
 * @version 1.0 Copyright (c) 2012 Company,Inc. All Rights Res
 */
public class L {

    public static boolean D = true;

    /**
     * @param msg
     * @return void
     * @throws
     * @Title: i
     */
    public static void i(String msg) {
        new LogContent(Type.Information, msg).flush();
    }

    /**
     * @param msg
     * @param tr
     * @return void
     * @throws
     * @Title: i
     */
    public static void i(String msg, Throwable tr) {
        new LogContent(Type.Information, msg, tr).flush();
    }

    /**
     * @param msg
     * @return void
     * @throws
     * @Title: e
     */
    public static void e(String msg) {
        new LogContent(Type.Error, msg).flush();
    }

    /**
     * @param msg
     * @param tr
     * @return void
     * @throws
     * @Title: e
     */
    public static void e(String msg, Throwable tr) {
        new LogContent(Type.Error, msg, tr).flush();
    }

    /**
     * @param msg
     * @return void
     * @throws
     * @Title: d
     */
    public static void d(String msg) {
        new LogContent(Type.Debug, msg).flush();
    }

    /**
     * @param msg
     * @param tr
     * @return void
     * @throws
     * @Title: d
     */
    public static void d(String msg, Throwable tr) {
        new LogContent(Type.Debug, msg, tr).flush();
    }

    /**
     * @param msg
     * @return void
     * @throws
     * @Title: v
     */
    public static void v(String msg) {
        new LogContent(Type.Verbose, msg).flush();
    }

    /**
     * @param msg
     * @param tr
     * @return void
     * @throws
     * @Title: v
     */
    public static void v(String msg, Throwable tr) {
        new LogContent(Type.Verbose, msg, tr).flush();
    }

    /**
     * @param msg
     * @return void
     * @throws
     * @Title: w
     */
    public static void w(String msg) {
        new LogContent(Type.Warning, msg).flush();
    }

    /**
     * @param msg
     * @param tr
     * @return void
     * @throws
     * @Title: w
     */
    public static void w(String msg, Throwable tr) {
        new LogContent(Type.Warning, msg, tr).flush();
    }

    // private static void toastString(String text, int duration) {
    // mHandler.removeCallbacks(r);
    // if (mToast != null)
    // mToast.setText(text);
    // else
    // mToast = Toast.makeText(ctx, text, duration);
    // mHandler.postDelayed(r, duration);
    // mToast.show();
    // }

    /**
     * @param msg
     * @return void
     * @throws
     * @Title: toastShart
     */
    public static void toastShort(String msg) {
        // toastString(msg, Toast.LENGTH_SHORT);
        TispToastFactory.getToast(BaseInit.ctx, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param msgRes
     * @return void
     * @throws
     * @Title: toastShart
     */
    public static void toastShort(int msgRes) {
        // toastString(ctx.getString(msgRes), Toast.LENGTH_SHORT);
        TispToastFactory.getToast(BaseInit.ctx, BaseInit.ctx.getString(msgRes), Toast.LENGTH_SHORT).show();
    }

    /**
     * @param msg
     * @return void
     * @throws
     * @Title: toastLong
     */
    public static void toastLong(String msg) {
        // toastString(msg, Toast.LENGTH_LONG);
        TispToastFactory.getToast(BaseInit.ctx, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * @param msgRes
     * @return void
     * @throws
     * @Title: toastLong
     */
    public static void toastLong(int msgRes) {
        // toastString(ctx.getString(msgRes), Toast.LENGTH_LONG);
        TispToastFactory.getToast(BaseInit.ctx, BaseInit.ctx.getString(msgRes), Toast.LENGTH_LONG).show();
    }

    public static void json(Object message) {
        new LHelper().json(String.valueOf(message));
    }
}
