package com.weqia.utils.exception;

import com.weqia.data.StatedPerference;
import com.weqia.data.UtilsConstants;
import com.weqia.utils.L;
import com.weqia.utils.TimeUtils;
import com.weqia.utils.datastorage.file.NativeFileUtil;
import com.weqia.utils.datastorage.file.PathUtil;

import java.io.File;

/**
 * Description:统一处理异常<br>
 * CheckedExceptionHandler.java Create on 2013-1-3 下午7:56:17
 * 
 * @author Bewin berwinzheng@gmail.com
 * @version 1.0 Copyright (c) 2013 Company,Inc. All Rights Res
 */
public class CheckedExceptionHandler {

    public static void handleException(Object param, Throwable tr) {
        if (param == null)
            handleException(tr);
        else {
            if (L.D)
                L.w("exception for instance " + param.toString(), tr);
            else {
                writeException(tr);
            }
        }
    }

    public static void handleException(Throwable tr) {
        if (L.D) {
            L.w(tr.getClass().getSimpleName(), tr);
        } else {
            writeException(tr);
        }
    }

    private static void writeException(Throwable tr) {
        long curTime = TimeUtils.getTimeLong();
        long oldTime =
                StatedPerference.getInstance().get(UtilsConstants.pr_log_clear_time, Long.class);// getLastLogClearTime();
        if ((curTime - oldTime) >= UtilsConstants.LOG_CLEAR_TIME) {
            NativeFileUtil.delFolder(new File(PathUtil.getExceptionPath()));
            StatedPerference.getInstance().put(UtilsConstants.pr_log_clear_time, curTime);// setLastLogClearTime(curTime);
        }
        if (L.D) {
            L.w("程序有被捕捉的异常，请注意");
            L.w(tr.getClass().getSimpleName(), tr);
        } else {
            ThrowableOperate.operateException(tr, true);
        }
    }
}
