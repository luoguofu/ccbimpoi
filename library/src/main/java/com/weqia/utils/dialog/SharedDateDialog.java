package com.weqia.utils.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.weqia.BaseInit;
import com.weqia.utils.MResource;
import com.weqia.utils.StrUtil;
import com.weqia.utils.TimeUtils;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.dialog.date.NumericWheelAdapter;
import com.weqia.utils.dialog.date.OnWheelChangedListener;
import com.weqia.utils.dialog.date.WheelView;

import java.util.Calendar;

@SuppressLint("ViewConstructor")
public class SharedDateDialog extends Dialog implements OnClickListener {
    private Activity mContext;
    private View mMenuView;
    private ViewFlipper viewfipper;
    private Button btSubmit, btCancel;
    private TextView tvTitle;
    private DateNumericAdapter yearAdapter, monthAdapter, dayAdapter;
    private DateNumericAdapter minsAdapter, houtAdapter;
    private WheelView year, month, day;
    private WheelView hour, mins;
    private int mCurYear, mCurMonth, mCurDay;
    private int mCurrentHour, mCurrentMins;
    private String[] dateType;
    private Long selectDate;
    private onDateChangedListener dateChangedListener;
    private boolean wantTime = true;

    // 是否要具体时间
    @SuppressLint("InflateParams")
    public SharedDateDialog(Activity context, boolean wantTime, Long currentDate, String title,
            onDateChangedListener dateChangedListener) {
        super(context, MResource.getIdByName(BaseInit.ctx.getPackageName(), "style",
                "dialog_fullscreen"));
        mContext = context;
        this.wantTime = wantTime;
        this.dateChangedListener = dateChangedListener;

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView =
                inflater.inflate(MResource.getIdByName(BaseInit.ctx.getPackageName(), "layout",
                        "util_custom_dialog_date"), null);
        viewfipper = new ViewFlipper(context);
        viewfipper.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));

        year =
                (WheelView) mMenuView.findViewById(MResource.getIdByName(
                        BaseInit.ctx.getPackageName(), "id", "year"));
        month =
                (WheelView) mMenuView.findViewById(MResource.getIdByName(
                        BaseInit.ctx.getPackageName(), "id", "month"));
        day =
                (WheelView) mMenuView.findViewById(MResource.getIdByName(
                        BaseInit.ctx.getPackageName(), "id", "day"));
        hour =
                (WheelView) mMenuView.findViewById(MResource.getIdByName(
                        BaseInit.ctx.getPackageName(), "id", "hour"));
        mins =
                (WheelView) mMenuView.findViewById(MResource.getIdByName(
                        BaseInit.ctx.getPackageName(), "id", "mins"));
        if (wantTime) {
            ViewUtils.showViews(hour, mins);
        } else {
            ViewUtils.hideViews(hour, mins);
        }

        tvTitle =
                (TextView) mMenuView.findViewById(MResource.getIdByName(
                        BaseInit.ctx.getPackageName(), "id", "tv_dlg_date_title"));
        if (StrUtil.notEmptyOrNull(title)) {
            tvTitle.setText(title);
        }
        btSubmit =
                (Button) mMenuView.findViewById(MResource.getIdByName(
                        BaseInit.ctx.getPackageName(), "id", "submit"));
        btCancel =
                (Button) mMenuView.findViewById(MResource.getIdByName(
                        BaseInit.ctx.getPackageName(), "id", "cancel"));
        btSubmit.setOnClickListener(this);
        btCancel.setOnClickListener(this);

        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(year, month, day, hour, mins);
            }
        };

        Calendar newCalendar = Calendar.getInstance(TimeUtils.getDefaultTimeZone(), TimeUtils.getDefaultLocale());
        Calendar currentCalendar = Calendar.getInstance(TimeUtils.getDefaultTimeZone(), TimeUtils.getDefaultLocale());
        if (currentDate != null) {
            newCalendar.setTimeInMillis(currentDate);
        }

        int curYear = currentCalendar.get(Calendar.YEAR);
        int curMonth = newCalendar.get(Calendar.MONTH);
        int curDay = newCalendar.get(Calendar.DAY_OF_MONTH);

        mCurYear = 100 - (curYear - newCalendar.get(Calendar.YEAR));
        mCurMonth = curMonth;
        mCurDay = curDay - 1;

        if (wantTime) {
            int curHour = newCalendar.get(Calendar.HOUR_OF_DAY);
            int curMin = newCalendar.get(Calendar.MINUTE);
            mCurrentHour = curHour;
            mCurrentMins = curMin;
        }

        // if (age != null && age.contains("-")) {
        // String str[] = age.split("-");
        // mCurYear = 100 - (curYear - Integer.parseInt(str[0]));
        // mCurMonth = Integer.parseInt(str[1]) - 1;
        // mCurDay = Integer.parseInt(str[2]) - 1;;
        // }
        dateType =
                mContext.getResources().getStringArray(
                        MResource.getIdByName(BaseInit.ctx.getPackageName(), "array", "date"));
        monthAdapter = new DateNumericAdapter(context, 1, 12, mCurMonth);
        monthAdapter.setTextType(dateType[1]);
        month.setViewAdapter(monthAdapter);
        month.setCurrentItem(mCurMonth);
        month.addChangingListener(listener);
        // year

        yearAdapter = new DateNumericAdapter(context, curYear - 100, curYear + 100, mCurYear);
        yearAdapter.setTextType(dateType[0]);
        year.setViewAdapter(yearAdapter);
        year.setCurrentItem(mCurYear);
        year.addChangingListener(listener);
        // day

        if (wantTime) {
            houtAdapter = new DateNumericAdapter(context, 0, 23, mCurrentHour);
            houtAdapter.setTextType(dateType[3]);
            hour.setViewAdapter(houtAdapter);
            hour.setCurrentItem(mCurrentHour);
            hour.addChangingListener(listener);

            minsAdapter = new DateNumericAdapter(context, 0, 59, mCurrentMins);
            minsAdapter.setTextType(dateType[4]);
            mins.setViewAdapter(minsAdapter);
            mins.setCurrentItem(mCurrentMins);
            mins.addChangingListener(listener);
        }

        updateDays(year, month, day, hour, mins);
        day.setCurrentItem(mCurDay);
        updateDays(year, month, day, hour, mins);
        day.addChangingListener(listener);

        viewfipper.addView(mMenuView);
        viewfipper.setFlipInterval(6000000);
        this.setContentView(viewfipper);


        mMenuView.findViewById(
                MResource.getIdByName(BaseInit.ctx.getPackageName(), "id", "view_empty"))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });

        // this.setWidth(LayoutParams.FILL_PARENT);
        // this.setHeight(LayoutParams.WRAP_CONTENT);
        // this.setFocusable(true);
        // ColorDrawable dw = new ColorDrawable(0x00000000);
        // this.setBackgroundDrawable(dw);
        // this.update();
    }

    // @Override
    // public void showAtLocation(View parent, int gravity, int x, int y) {
    // super.showAtLocation(parent, gravity, x, y);
    // viewfipper.startFlipping();
    // }


    private void updateDays(WheelView year, WheelView month, WheelView day, WheelView hour,
            WheelView mins) {

        Calendar calendar = Calendar.getInstance(TimeUtils.getDefaultTimeZone(), TimeUtils.getDefaultLocale());
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        dayAdapter =
                new DateNumericAdapter(mContext, 1, maxDays,
                        calendar.get(Calendar.DAY_OF_MONTH) - 1);
        dayAdapter.setTextType(dateType[2]);
        day.setViewAdapter(dayAdapter);
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
        int years = calendar.get(Calendar.YEAR) - 100;
        if (wantTime) {
            calendar.set(years, month.getCurrentItem(), day.getCurrentItem() + 1,
                    hour.getCurrentItem(), mins.getCurrentItem());
        } else {
            calendar.set(years, month.getCurrentItem(), day.getCurrentItem() + 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        }

        selectDate = calendar.getTimeInMillis();
        // Log.e("modify", date.toString());
        // Log.e("ddd", years + "-" + (month.getCurrentItem() + 1) + "-" + (day.getCurrentItem() +
        // 1)
        // + " " + hour.getCurrentItem() + ":" + mins.getCurrentItem());
    }

    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateNumericAdapter extends NumericWheelAdapter {

        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue);
            // setTextSize(16);
        }

        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            view.setTypeface(Typeface.SANS_SERIF);
        }

        public CharSequence getItemText(int index) {
            return super.getItemText(index);
        }
    }

    public void onClick(View v) {
        if (v == btSubmit) {
            if (dateChangedListener != null) {
                dateChangedListener.dateChanged(selectDate);
            }
        }
        this.dismiss();
    }

    /**
     * 日期接口
     * 
     * @Description :
     * @author Berwin
     * @version 1.0
     * @created 2013-5-10 下午4:32:51
     * @fileName com.weqia.utils.dialog.SharedDateDialog.java
     * 
     */
    public interface onDateChangedListener {
        /**
         * 选择了时间
         * 
         * @Description
         * @param date
         * 
         */
        public void dateChanged(Long date);
    }
}
