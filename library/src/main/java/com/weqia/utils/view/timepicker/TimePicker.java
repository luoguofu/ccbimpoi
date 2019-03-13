package com.weqia.utils.view.timepicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;

import com.weqia.BaseInit;
import com.weqia.utils.L;
import com.weqia.utils.MResource;
import com.weqia.utils.StrUtil;
import com.weqia.utils.TimeUtils;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.view.timepicker.NumberPicker.OnValueChangeListener;

import java.util.Calendar;

public class TimePicker extends FrameLayout implements OnClickListener {

    private Context mContext;
    private NumberPicker dayPicker;
    private NumberPicker hourPicker;
    private NumberPicker minPicker;
    private TextSwitcher timeSwitcher;

    private Calendar mCalendar;
    boolean is24Hour;
    boolean isAm = true;
    private Boolean currentDay = null;// null位隐藏，true为当日，false为次日

    public TimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, null, null);
    }

    public TimePicker(Context context) {
        this(context, null);
    }

    public TimePicker(Context context, AttributeSet attrs, String time, Boolean currentDay) {
        super(context, attrs);
        mContext = context;
        mCalendar = Calendar.getInstance(TimeUtils.getDefaultTimeZone(), TimeUtils.getDefaultLocale());
        ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                MResource.getIdByName(BaseInit.ctx.getPackageName(), "layout", "util_time_picker"),
                this, true);
        dayPicker =
                (NumberPicker) findViewById(MResource.getIdByName(BaseInit.ctx.getPackageName(),
                        "id", "time_day"));
        hourPicker =
                (NumberPicker) findViewById(MResource.getIdByName(BaseInit.ctx.getPackageName(),
                        "id", "time_hours"));
        minPicker =
                (NumberPicker) findViewById(MResource.getIdByName(BaseInit.ctx.getPackageName(),
                        "id", "time_minutes"));
        timeSwitcher =
                (TextSwitcher) findViewById(MResource.getIdByName(BaseInit.ctx.getPackageName(),
                        "id", "time_switcher"));

        minPicker.setMinValue(0);
        minPicker.setMaxValue(59);
        dayPicker.setMinValue(0);
        dayPicker.setMaxValue(9);
        minPicker.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
        hourPicker.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
        dayPicker.setFormatter(NumberPicker.DAY_FORMATTER);

        this.currentDay = currentDay;
        if (currentDay == null) {
            ViewUtils.hideView(dayPicker);
        } else {
            ViewUtils.showView(dayPicker);
        }


        is24Hour = true;
        // is24Hour = android.text.format.DateFormat.is24HourFormat(context);

        timeSwitcher.setOnClickListener(this);
        minPicker.setOnValueChangedListener(new OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mCalendar.set(Calendar.MINUTE, newVal);
            }
        });

        hourPicker.setOnValueChangedListener(new OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mCalendar.set(Calendar.HOUR, newVal);
            }
        });

        if (StrUtil.notEmptyOrNull(time)) {
            setTimer(time);
        } else {
            updateTime();
        }
    }

    private void updateTime() {
        if (L.D) L.i(mCalendar.getTime() + "--");
        if (is24Hour) {
            hourPicker.setMinValue(0);
            hourPicker.setMaxValue(23);
            hourPicker.setValue(mCalendar.get(Calendar.HOUR_OF_DAY));
            timeSwitcher.setVisibility(View.GONE);
        } else {
            hourPicker.setMinValue(1);
            hourPicker.setMaxValue(12);
            hourPicker.setValue(mCalendar.get(Calendar.HOUR));
            if (mCalendar.get(Calendar.AM_PM) == Calendar.PM) {
                isAm = false;
                timeSwitcher.setText("pm");
            } else {
                isAm = true;
                timeSwitcher.setText("am");
            }
            timeSwitcher.setVisibility(View.VISIBLE);
        }
        minPicker.setValue(mCalendar.get(Calendar.MINUTE));
    }

    public boolean isIs24Hour() {
        return is24Hour;
    }

    public void setIs24Hour(boolean is24Hour) {
        this.is24Hour = is24Hour;
    }

    public String getTime() {
        String time = "";
        String minValue = minPicker.getValue() + "";
        if (minValue.equalsIgnoreCase("0")) {
            minValue = "00";
        } else {
            if (minPicker.getValue() < 10) {
                minValue = "0" + minPicker.getValue();
            }
        }

        int hour = hourPicker.getValue();

        if (getCurrentDay() != null && !getCurrentDay()) {
            if (L.D) L.e("次日时间----");
            hour += 24;
        }
        String hourStr = "";
        if (hour < 10) {
            hourStr = "0" + hour;
        } else {
            hourStr = hour + "";
        }
        if (is24Hour) {
            time = hourStr + ":" + minValue;
        } else {
            time = hourStr + ":" + minValue + " " + (isAm ? "am" : "pm");
        }
        return time;
    }

    /**
     * 是否是当日
     * 
     * @return
     */
    public Boolean getCurrentDay() {
        if (currentDay == null) {
            return null;
        }
        int dayValue = dayPicker.getValue();
        if (dayValue % 2 == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void setCurrentDay(Boolean currentDay) {
        this.currentDay = currentDay;
    }


    public int getHourOfDay() {
        return is24Hour || isAm ? hourPicker.getValue() : (hourPicker.getValue() + 12) % 24;
    }

    public int getHour() {
        return hourPicker.getValue();
    }

    public int getMinute() {
        return mCalendar.get(Calendar.MINUTE);
    }

    public void setCalendar(Calendar calendar) {
        this.mCalendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        this.mCalendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
        updateTime();
    }

    public void setTimer(String time) {
        if (StrUtil.notEmptyOrNull(time) && time.contains(":")) {
            String[] times = time.split(":");
            if (times != null && times.length == 2) {
                String timeStr = times[0];
                String tomoPre = "次日";
                int hour = 0;
                if (timeStr.startsWith(tomoPre)) {
                    timeStr = timeStr.replace(tomoPre, "");
                    dayPicker.setValue(1);
                    hour = Integer.parseInt(timeStr);
                } else {
                    hour = Integer.parseInt(timeStr);
                    if (hour >= 24) {
                        dayPicker.setValue(1);
                        hour = hour - 24;
                    }
                }
                this.mCalendar.set(Calendar.HOUR_OF_DAY, hour);
                this.mCalendar.set(Calendar.MINUTE, Integer.parseInt(times[1]));
                updateTime();
            }
        }
    }

    @Override
    public void onClick(View v) {
        isAm = !isAm;
        if (isAm) {
            mCalendar.roll(Calendar.HOUR, -12);
            timeSwitcher.setText("am");
        } else {
            mCalendar.roll(Calendar.HOUR, 12);
            timeSwitcher.setText("pm");
        }
    }

    public static String getShowTimeStr(String str) {
        String dayPre = "次日";
        if (str.startsWith(dayPre)) {
            return str;
        }
        String[] times = str.trim().split(":");
        if (times != null && times.length == 2) {
            int hour = 0;
            try {
                hour = Integer.parseInt(times[0]);
            } catch (NumberFormatException e) {
                return str;
            }
            if (hour >= 24) {
                return dayPre + (hour - 24) + ":" + times[1];
            } else {
                return str;
            }
        } else {
            return str;
        }
    }

    public static String getRealTimeStr(String str) {
        String dayPre = "次日";
        if (!str.startsWith(dayPre)) {
            return str;
        }

        String realStr = str.replace(dayPre, "");
        String[] times = realStr.trim().split(":");
        if (times != null && times.length == 2) {
            int hour = 0;
            try {
                hour = Integer.parseInt(times[0]);
            } catch (NumberFormatException e) {
                return str;
            }
            hour = hour + 24;
            return hour + ":" + times[1];
        }
        return str;
    }
}
