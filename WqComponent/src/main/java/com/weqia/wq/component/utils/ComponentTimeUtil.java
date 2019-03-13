package com.weqia.wq.component.utils;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by berwin on 2017/8/15.
 */

public class ComponentTimeUtil {
    private static SimpleDateFormat getFormatter(String mat) {
        SimpleDateFormat formatter = new SimpleDateFormat(mat);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return formatter;
    }

    public static String getDateM(long time) {
        SimpleDateFormat sdf = getFormatter("M");
        String dt = sdf.format(Long.valueOf(time));
        return dt;
    }

    public static String getDateD(long time) {
        SimpleDateFormat sdf = getFormatter("d");
        String dt = sdf.format(Long.valueOf(time));
        return dt;
    }

    public static String getDateYear(long time) {
        SimpleDateFormat sdf = getFormatter("yyyy");
        String dt = sdf.format(Long.valueOf(time));
        return dt;
    }
}
