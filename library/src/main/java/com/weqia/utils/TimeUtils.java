package com.weqia.utils;

import android.annotation.SuppressLint;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat")
public class TimeUtils {

    public static final long ONE_MINUTE = 60 * 1000;
    public static final long ONE_HOUR = 60 * ONE_MINUTE;
    public static final long ONE_DAY = 24 * ONE_HOUR;

    public static Locale getDefaultLocale() {
        return Locale.CHINA;
    }

    public static TimeZone getDefaultTimeZone(){
        return TimeZone.getTimeZone("GMT+8");
    }

    public static String getToDay() {
        return getFormatter("yyyy-MM-dd").format(new Date().getTime());
    }


    public static SimpleDateFormat getFormatter(String mat) {
        SimpleDateFormat formatter = new SimpleDateFormat(mat);
        formatter.setTimeZone(getDefaultTimeZone());
        return formatter;
    }

    public static Long parseDateLong(String str) {
        Date addTime = null;
        try {
            addTime = getFormatter("yyyy-MM-dd HH:mm").parse(str);
            return addTime.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // public static Long getTodayLong() {
    // SimpleDateFormat formatter = getFormatter("yyyy-MM-dd");
    // return stringToDate(formatter.format(new Date().getTime())).getTime();
    // }


    public static String getDateString(Long date) {
        Date nowDate = new Date(date); // 获取系统时间
        return getFormatter("yyyy/MM/dd").format(nowDate);
    }

    private static final String COMMON_FORMAR = "yyyy-MM-dd HH:mm";

    private static final String DAY = "天";
    private static final String HOUR = "小时";
    private static final String MINUTE = "分钟";
    private static final String UNKNOWN = "刚刚";
    private static final String SUFFIX = "前";

    public static String getDisplayDate(Date inputDate) {
        Date localDate = new Date();
        long l1 = (localDate.getTime() - inputDate.getTime()) / 1000L;
        while (true) {
            if (l1 <= 60L) {
                return UNKNOWN;
            }
            long l2 = l1 / 60L;
            if (l2 < 60L) {
                return l2 + MINUTE + SUFFIX;
            }

            long l3 = l2 / 60L;
            if (l3 < 24L) {
                return l3 + HOUR + SUFFIX;
            }
            long l4 = l3 / 24L;

            if (l4 > 7 && l4 < 365) {
                return getTimeMDHM(inputDate);
            } else if (l4 >= 365) {
                return getTimeYMDHM(inputDate);
            } else {
                return l4 + DAY + SUFFIX;
            }

        }
    }
    
    public static String formatFriendlyForSS(Date date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > ONE_DAY) {
            return TimeUtils.getDateMDChineseFromLong(date.getTime());
        }
        if (diff > ONE_HOUR) {
            r = (diff / ONE_HOUR);
            return r + "个小时前";
        }
        if (diff > ONE_MINUTE) {
            r = (diff / ONE_MINUTE);
            return r + "分钟前";
        }
        return "刚刚";
    }

    public static String getDisplayDateShow(String longTime) {

        Date inputDate = new Date();
        try {
            inputDate = new Date(Long.parseLong(longTime));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        Date localDate = new Date();
        long l1 = (localDate.getTime() - inputDate.getTime()) / 1000L;
        while (true) {
            if (l1 <= 60L) {
                return UNKNOWN;
            }
            long l2 = l1 / 60L;
            if (l2 < 60L) {
                return l2 + MINUTE + SUFFIX;
            }

            long l3 = l2 / 60L;
            if (l3 < 24L) {
                return l3 + HOUR + SUFFIX;
            }
            long l4 = l3 / 24L;

            if (l4 > 7 && l4 < 365) {
                return getTimeMDHM(inputDate);
            } else if (l4 >= 365) {
                return getTimeYMDHM(inputDate);
            } else {
                return l4 + DAY + SUFFIX;
            }

        }
    }

    public static boolean isAddTime(Date inputDate) {

        boolean is_add_time = false;

        if (is_add_time) {

        }
        return is_add_time;
    }

    // public static Date stringToDate(String strDate) {
    // Date date = null;
    // try {
    // date = DateFormat.getDateInstance().parse(strDate);
    // } catch (ParseException e) {
    // CheckedExceptionHandler.handleException(e);
    // }
    // return date;
    // }

    /**
     * 通过string获取时间，若获取不到，则返回当前时间
     * 
     * @param string
     * @return
     * @Description
     */
    public static Date dateFromString(String string) {
        if (StrUtil.isEmptyOrNull(string)) {
            return new Date(System.currentTimeMillis());
        }
        DateFormat df = getFormatter(COMMON_FORMAR);
        Date date = null;
        try {
            date = df.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date(System.currentTimeMillis());
        }
        return date;
    }

    public static String getDateFromLong(long time) {
        SimpleDateFormat sdf = getFormatter(COMMON_FORMAR);
        String dt = sdf.format(time);
        return dt;
    }

    public static String getDateYMDFromLong(long time) {
        SimpleDateFormat sdf = getFormatter("yyyy-MM-dd");
        String dt = sdf.format(time);
        return dt;
    }
    public static String getDateYMDHMFromLong(long time) {
        SimpleDateFormat sdf = getFormatter("yyyy-MM-dd HH:mm");
        String dt = sdf.format(time);
        return dt;
    }

    public static String getDateYMDChineseFromLong(long time) {
        SimpleDateFormat sdf = getFormatter("yyyy年MM月dd日");
        String dt = sdf.format(time);
        return dt;
    }

    public static String getDateMDChineseFromLong(long time) {
        SimpleDateFormat sdf = getFormatter("MM月dd日");
        String dt = sdf.format(time);
        return dt;
    }

    public static String getDateWeekChineseFromLong(long time) {
        SimpleDateFormat sdf = getFormatter("E");
        String dt = sdf.format(time);
        return dt;
    }

    // 年首日起毫秒
    public static long getYearStart() {
        Calendar cal = Calendar.getInstance(getDefaultTimeZone(), getDefaultLocale());
        cal.set(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static int getCurYear() {
        Calendar cal = Calendar.getInstance(getDefaultTimeZone(), getDefaultLocale());
        return cal.get(Calendar.YEAR);
    }

    public static int getCurMonth() {
        Calendar cal = Calendar.getInstance(getDefaultTimeZone(), getDefaultLocale());
        return (cal.get(Calendar.MONTH) + 1);
    }

    public static String getDateMDHM(String time) {
        String dt = "";
        try {
            Long timeLong = Long.parseLong(time);
            String formatStr = "MM-dd HH:mm";
            if (timeLong < getYearStart()) {
                formatStr = "yyyy-MM-dd HH:mm";
            }
            SimpleDateFormat sdf = getFormatter(formatStr);
            dt = sdf.format(timeLong);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    public static String getDateMDHMChinese(String time) {
        String dt = "";
        try {
            Long timeLong = Long.parseLong(time);
            String formatStr = "MM月dd日 HH:mm";
            if (timeLong < getYearStart()) {
                formatStr = "yyyy年MM月dd日 HH:mm";
            }
            SimpleDateFormat sdf = getFormatter(formatStr);
            dt = sdf.format(timeLong);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    public static String getDateHMFromLong(long time) {
        SimpleDateFormat sdf = getFormatter("HH:mm");
        String dt = sdf.format(time);
        return dt;
    }

    // public static String getDateMDHMFromLong(long time) {
    // SimpleDateFormat sdf = getFormatter("MM月dd日 HH:mm", UtilApplication.getInstance()
    // .getDefaultLocale());
    // String dt = sdf.format(time);
    // return dt;
    // }

    public static String getDateMDHMFromLong(Long time) {
        String dt = null;
        if (time == null) {
            return dt;
        }
        SimpleDateFormat sdf = null;
        if (time >= getYearStart()) {
            sdf = getFormatter("MM-dd HH:mm");
        } else {
            sdf = getFormatter("yyyy-MM-dd HH:mm");
        }
        dt = sdf.format(time);
        return dt;
    }

    public static String getDateMDEFromLong(long time) {
        SimpleDateFormat sdf = getFormatter("MM月dd日 E");
        return sdf.format(time);
    }

    public static String getDateFromFormat(String format, long time) {
        SimpleDateFormat sdf = getFormatter(format);
        return sdf.format(time);
    }

    // year
    public static String getDateYear(long time) {
        SimpleDateFormat sdf = getFormatter("yyyy");
        return sdf.format(time);
    }

    // month
    public static String getDateMonth(long time) {
        SimpleDateFormat sdf = getFormatter("M");
        return sdf.format(time);
    }

    // day
    public static String getDateDay(long time) {
        SimpleDateFormat sdf = getFormatter("dd");
        return sdf.format(time);
    }

    // week
    public static String getDateWeek(long time) {
        SimpleDateFormat sdf = getFormatter("E");
        return sdf.format(time);
    }

    // hour
    public static String getDateHM(long time) {
        SimpleDateFormat sdf = getFormatter("HH:mm");
        return sdf.format(time);
    }

    public static String getTimeFromLong(long time) {
        SimpleDateFormat sdf = getFormatter("HH:mm");
        return sdf.format(time);
    }

    public static String dateToEDate(Date date) {
        String eDate = "";
        SimpleDateFormat dateFm = getFormatter("EEEE");
        eDate = dateFm.format(date);
        return eDate;
    }

    public static String getYesterdayDate() {
        return getFormatter("yyyy-MM-dd").format(new Date().getTime() - 24 * 60 * 60 * 1000);
    }

    public static String getDate() {
        return getFormatter("yyyy-MM-dd").format(new Date());
    }

    public static String getelDate() {
        return getFormatter("MM月dd日 EEEE").format(new Date());
    }

    public static String getelDate(Date date) {
        SimpleDateFormat sdf = getFormatter("MM月dd日 EEEE");
        return sdf.format(date);
    }

    public static String getDateYDM() {
        return getFormatter("yyyy-MM-dd").format(new Date());
    }

    public static String getTime() {
        return getFormatter("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String getTimess() {
        return getFormatter("yyyy-MM-dd HH:mm:ss SSS").format(new Date());
    }

    public static String getFileTime() {
        return getFormatter("yyyy-MM-dd_HH_mm_ss").format(new Date());
    }

    public static String getTimeHS() {
        return getFormatter("HH:mm").format(new Date());
    }

    public static String getTimeHS(long time) {
        return getFormatter("HH:mm").format(time);
    }

    public static String getTimeHms() {
        return getFormatter("HH:mm:ss").format(new Date());
    }

    public static String getTimeHms(Date date) {
        return getFormatter("HH:mm:ss").format(date);
    }

    public static String getTimeYMDHM(Date inputDate) {
        return getFormatter("yyyy-MM-dd HH:mm").format(inputDate);
    }

    public static String getTimeYMDHM(String inputStr) {
        Date inputDate = new Date();
        if (StrUtil.notEmptyOrNull(inputStr)) {
            inputDate = new Date(Long.parseLong(inputStr));
        }
        return getFormatter("yyyy-MM-dd HH:mm").format(inputDate);
    }

    public static String getDateShort(String inputStr) {
        Date inputDate = new Date();
        if (StrUtil.notEmptyOrNull(inputStr)) {
            inputDate = new Date(Long.parseLong(inputStr));
        }
        return getFormatter("yy/MM/dd").format(inputDate);
    }

    public static String getDateTimeShort(String inputStr) {
        Date inputDate = new Date();
        if (StrUtil.notEmptyOrNull(inputStr)) {
            inputDate = new Date(Long.parseLong(inputStr));
        }
        return getFormatter("yy/MM/dd HH:mm").format(inputDate);
    }

    public static String getTimeYMD(String inputStr) {
        Date inputDate = new Date();
        if (StrUtil.notEmptyOrNull(inputStr)) {
            inputDate = new Date(Long.parseLong(inputStr));
        }
        return getFormatter("yyyy-MM-dd").format(inputDate);
    }

    public static String getTimeMD(String inputStr) {
        Date inputDate = new Date();
        if (StrUtil.notEmptyOrNull(inputStr)) {
            inputDate = new Date(Long.parseLong(inputStr));
        }
        return getFormatter("MM月dd日").format(inputDate);
    }

    public static String getTimeMDHM(Date inputDate) {
        return getFormatter("MM-dd HH:mm").format(inputDate);
    }

    public static String getBirthdayYMD(String inputStr) {
        Date inputDate = null;
        if (StrUtil.notEmptyOrNull(inputStr)) {
            inputDate = new Date(Long.parseLong(inputStr));
        }
        return inputDate != null ? getFormatter("yyyy-MM-dd").format(inputDate) : null;
    }

    public static String getLongTime() {
        return (new Date().getTime()) + "";
    }

    public static Long getTimeLong() {
        return new Date().getTime();
    }

    public static Long getYMDTimeLong() {
        Date date = new Date();
        SimpleDateFormat sf = getFormatter("yyyy-MM-dd");
        try {
            date = sf.parse(getDateYDM());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date.getTime();
    }

    public static Long getYMDTimeLong(String dateStr) {
        Date date = new Date();
        SimpleDateFormat sf = getFormatter("yyyy-MM-dd");
        try {
            date = sf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static Long getYMDTimeLong(Long dateLong) {
        // Date dateFrom = new Date(dateLong);
        String dateFrom = TimeUtils.getDateYMDFromLong(dateLong);
        Date dateTo = new Date();
        SimpleDateFormat sf = getFormatter("yyyy-MM-dd");
        try {
            dateTo = sf.parse(dateFrom);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTo.getTime();
    }


    public static String getFileSaveTime() {
        return getFormatter("yyyyMMdd_HHmmss").format(new Date());
    }

    public static String getTime(Date inputDate) {
        return getFormatter("yyyy-MM-dd HH:mm:ss").format(inputDate);
    }

    // public static String getChineseDate(Date inputDate) {
    // String show = "";
    // Date nowDate = new Date();
    // if (getDateYDM(inputDate).equals(getDateYDM(nowDate))) {
    // // 今天
    // Calendar cal = Calendar.getInstance(TimeUtils.getDefaultTimeZone(), TimeUtils.getDefaultLocale());
    // cal.setTime(inputDate);
    // int hour = cal.get(Calendar.HOUR_OF_DAY);
    // if (hour >= 6 && hour < 12) {
    // // 0:00:00 - 11:59:59 算上午
    // show = "上午 " + getTimeHM(inputDate);
    // } else if (hour >= 12 && hour < 18) {
    // // 12:00:00 - 17:59:59 算下午
    // show = "下午 " + getTimeHM(inputDate);
    // } else if (hour >= 0 && hour < 6) {
    // show = "凌晨 " + getTimeHM(inputDate);
    // } else {
    // // 18:00:00 - 23:59:59 算晚上
    // show = "晚上 " + getTimeHM(inputDate);
    // }
    // } else if (getLastWeekDay().getTime() < inputDate.getTime()) {
    // show = dateToEDate(inputDate).replace("星期", "周") + " " + getTimeHM(inputDate);
    // } else {
    // show = getMDDate(inputDate);
    // }
    //
    // return show;
    //
    // }
    //
    public static String getChineseShow(String longTime) {
        Date inputDate = new Date();
        if (StrUtil.isEmptyOrNull(longTime)) {
            return "";
        }
        try {
            inputDate = new Date(Long.parseLong(longTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String show = "";
        Date nowDate = new Date();
        if (getDateYDM(inputDate).equals(getDateYDM(nowDate))) {
            // 今天
            Calendar cal = Calendar.getInstance(getDefaultTimeZone(), getDefaultLocale());
            cal.setTime(inputDate);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            if (hour >= 6 && hour < 12) {
                // 0:00:00 - 11:59:59 算上午
                show = "上午 " + getTimeHM(inputDate);
            } else if (hour >= 12 && hour < 18) {
                // 12:00:00 - 17:59:59 算下午
                show = "下午 " + getTimeHM(inputDate);
            } else if (hour >= 0 && hour < 6) {
                show = "凌晨 " + getTimeHM(inputDate);
            } else {
                // 18:00:00 - 23:59:59 算晚上
                show = "晚上 " + getTimeHM(inputDate);
            }
        } else if (getLastWeekDay().getTime() < inputDate.getTime()) {
            show = dateToEDate(inputDate).replace("星期", "周") + " " + getTimeHM(inputDate);
        } else {
            show = getMDDate(inputDate);
        }
        return show;
    }


    public static long getDayStart() {
        Calendar cal = Calendar.getInstance(getDefaultTimeZone(), getDefaultLocale());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static String getChineseShow(String timeStr, boolean bHM) {
        Long longTime = null;
        try {
            longTime = Long.parseLong(timeStr);
        } catch (NumberFormatException e) {
            longTime = System.currentTimeMillis();
            // CheckedExceptionHandler.handleException(e);
        }
        Date tmp = new Date(longTime);
        StringBuffer sbTime = new StringBuffer();
        String today;
        if (longTime < getDayStart()) {
            sbTime.append(getMDDate(tmp));
        }
        Calendar cal = Calendar.getInstance(getDefaultTimeZone(), getDefaultLocale());
        cal.setTime(tmp);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour < 12) {
            // 0:00:00 - 11:59:59 算上午
            today = "上午 ";
        } else if (hour >= 12 && hour < 18) {
            // 12:00:00 - 17:59:59 算下午
            today = "下午 ";
        } else if (hour >= 0 && hour < 6) {
            today = "凌晨 ";
        } else {
            // 18:00:00 - 23:59:59 算晚上
            today = "晚上 ";
        }
        if (bHM) {
            sbTime.append(" " + today + getTimeHM(tmp));
        } else {
            if (longTime >= getDayStart()) {
                sbTime.append(today + getTimeHM(tmp));
            }
        }
        return sbTime.toString().trim();
    }

    public static String getMDDate(Date inputDate) {
        if (inputDate == null) {
            return "";
        }
        if (inputDate.getTime() < getYearStart()) {
            return getFormatter("yyyy年M月dd日").format(inputDate);
        } else {
            return getFormatter("M月dd日").format(inputDate);
        }
    }

    public static String getDateYDM(Date inputDate) {
        return getFormatter("yyyyMMdd").format(inputDate);
    }

    public static String getTimeHM(Date inputDate) {
        return getFormatter("HH:mm").format(inputDate);
    }


    public static String getTimeHM(Long inputDate) {
        return getFormatter("HH:mm").format(new Date(inputDate));
    }

    public static Date getLastWeekDay() {
        Date lastWeekDay;
        Calendar cal = Calendar.getInstance(getDefaultTimeZone(), getDefaultLocale());
        cal.set(Calendar.DAY_OF_WEEK, 1);
        lastWeekDay = cal.getTime();
        return lastWeekDay;
    }

    // 获得几天后的时间
    public static String getDayLater(int day) {
        // SimpleDateFormat dateFormat = getFormatter("yyyy-MM-dd hh-mm-ss");
        Calendar calendar = new GregorianCalendar(getDefaultTimeZone(), getDefaultLocale());
        Date date = new Date();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        date = calendar.getTime();
        Timestamp ts = new Timestamp(date.getTime());
        // System.err.println(ts.getTime() + "");
        // System.err.println(dateFormat.format(date));
        // System.err.println(System.currentTimeMillis());
        return ts.getTime() + "";
    }

    /**
     * 获取今天的截止时间
     * 
     * @param day
     * @return
     */
    public static long getDayOver(int day) {
        Calendar calendar = new GregorianCalendar(getDefaultTimeZone(), getDefaultLocale());
        Date date = new Date();
        calendar.add(Calendar.DATE, day);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        date = calendar.getTime();
        Timestamp ts = new Timestamp(date.getTime());
        return ts.getTime();
    }

    public static long getTimesMorning() {
        Calendar cal = Calendar.getInstance(getDefaultTimeZone(), getDefaultLocale());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
    
    public static long getTimesMorning(Long time) {
        if (time == null) {
            return getTimesMorning();
        }
        Calendar timeCalendar = Calendar.getInstance(getDefaultTimeZone(), getDefaultLocale());
        timeCalendar.setTimeInMillis(time);
        timeCalendar.set(Calendar.HOUR_OF_DAY, 0);
        timeCalendar.set(Calendar.SECOND, 0);
        timeCalendar.set(Calendar.MINUTE, 0);
        timeCalendar.set(Calendar.MILLISECOND, 0);
        return timeCalendar.getTimeInMillis();
    }

    public static long getTimeMonthBegin() {
        Calendar cal = Calendar.getInstance(getDefaultTimeZone(), getDefaultLocale());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static String getCalendarStr(Calendar calendar) {
        return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DATE) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
    }
}
