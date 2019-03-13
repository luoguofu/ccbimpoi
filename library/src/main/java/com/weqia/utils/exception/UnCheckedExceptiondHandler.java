package com.weqia.utils.exception;

import android.os.Build;

import com.weqia.BaseInit;
import com.weqia.utils.DeviceUtil;
import com.weqia.utils.L;
import com.weqia.utils.TimeUtils;
import com.weqia.utils.datastorage.file.PathUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;

public class UnCheckedExceptiondHandler implements UncaughtExceptionHandler {
    private static String ANDROID = Build.VERSION.RELEASE;
    private static String MODEL = Build.MODEL;
    private static String MANUFACTURER = Build.MANUFACTURER;

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static UnCheckedExceptiondHandler instance;

    // private static long mainTheadID = Looper.getMainLooper().getThread().getId();

    public static UnCheckedExceptiondHandler getInstance() {
        if (instance == null) {
            instance = new UnCheckedExceptiondHandler();
        }
        return instance;
    }

    /**
     * 获取系统默认的UncaughtException处理器, 设置该UnCheckExceptiondHandler为程序的默认处理器
     * 
     * @return void
     * @throws
     * @Title: init
     */
    public void init() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     * 
     * @param thread
     * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread,
     *      java.lang.Throwable)
     */
    @Override
    public void uncaughtException(Thread thread, Throwable tr) {
        if (!handleException(thread, tr) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, tr);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    /**
     * @param thread
     * @param tr
     * @return boolean
     * @throws
     * @Title: handleException
     */
    private boolean handleException(Thread thread, Throwable tr) {

        writeCrashLog(tr);

        if (L.D) L.e("程序错误了，挂了");
        // if (thread.getId() == mainTheadID) {
        if (L.D) {
            L.e(tr.getClass().getSimpleName(), tr);
            return true;
        } else {
            // String exceptionFileName = "unCheckedException-" + TimeUtils.getFileTime() +
            // ".log";
            // String filePath = null;
            // try {
            // filePath = FileUtil.getFile(PathUtil.getExceptionPath(), exceptionFileName);
            // if (filePath == null) {
            // return true;
            // } else {
            ThrowableOperate.operateException(tr, false);
            // }
            // } catch (IOException e) {
            // filePath = null;
            // e.printStackTrace();
            // }
            return true;
        }
        // } else {
        // L.e(tr.getClass().getSimpleName(), tr);
        // loopLoop();
        // }

        // return true;
    }

    // public static void loopLoop() {
    // L.e("程序错误了，挂了");
    // // try {
    // // Looper.prepare();
    // // Looper.loop();
    // // } catch (Throwable tr) {
    // // L.e("exception", tr);
    // // } finally {
    // // L.d("finally");
    // // }
    // }


    private static void writeCrashLog(Throwable tr) {
        String CRASH_LOG = PathUtil.getCrashPath() + "crash.log";
        File f = new File(CRASH_LOG);
        if (f.exists()) {
            f.delete();
        } else {
            try {
                f.createNewFile();
            } catch (Exception e) {
                return;
            }
        }

        PrintWriter p;
        try {
            p = new PrintWriter(f);
        } catch (Exception e) {
            return;
        }
        p.write("Error Time: " + TimeUtils.getTime() + "\n");
        p.write("Android Version: " + ANDROID + "\n");
        p.write("Device Model: " + MODEL + "\n");
        p.write("Device Manufacturer: " + MANUFACTURER + "\n");
        p.write("App Version: " + DeviceUtil.getVersionName(BaseInit.ctx) + "\n");
        p.write("*********************\n");
        tr.printStackTrace(p);
        p.close();
    }

    public static String readLog() {
        String res = "no crash_log !";
        try {
            String CRASH_LOG = PathUtil.getCrashPath() + "crash.log";
            if (new File(CRASH_LOG).exists()) {
                FileInputStream fin = new FileInputStream(CRASH_LOG);
                int length = fin.available();
                byte[] buffer = new byte[length];
                fin.read(buffer);
                res = new String(buffer, "UTF-8");
                fin.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
