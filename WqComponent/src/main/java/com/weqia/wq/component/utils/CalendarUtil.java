package com.weqia.wq.component.utils;

import java.util.Calendar;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.weqia.utils.TimeUtils;
import com.weqia.wq.R;
import com.weqia.wq.component.view.calendarview.CalendarAdapter;
import com.weqia.wq.component.view.calendarview.CalendarAdapter.CalendarItem;

public abstract class CalendarUtil {

    private Activity ctx;
    private View parent;
    protected Calendar calendar;
    private ViewSwitcher calendarSwitcher;
    private TextView currentMonth;
    private CalendarAdapter calendarAdapter;
    //    private final Locale locale;
    private int dayDay;
//    public int curMonth;


    public Map<String, String> dayTag;

    public CalendarUtil(Activity ctx, View parent) {
        this.ctx = ctx;
        this.parent = parent;
        calendar = Calendar.getInstance();
        initView();
    }


    public CalendarUtil(Activity ctx, View parent, Map<String, String> dayTag) {
        this.ctx = ctx;
        this.parent = parent;
        this.dayTag = dayTag;
        calendar = Calendar.getInstance();
        initView();
    }


    private void initView() {
        final GridView calendarDayGrid = (GridView) parent.findViewById(R.id.calendar_days_grid);
        final GestureDetector swipeDetector = new GestureDetector(ctx, new SwipeGesture(ctx));
        final GridView calendarGrid = (GridView) parent.findViewById(R.id.calendar_grid);
        calendarSwitcher = (ViewSwitcher) parent.findViewById(R.id.calendar_switcher);
        currentMonth = (TextView) parent.findViewById(R.id.current_month);
        calendarAdapter = new CalendarAdapter(ctx, calendar);

        updateCurrentMonth();

        final ImageView nextMonth = (ImageView) parent.findViewById(R.id.next_month);
        nextMonth.setOnClickListener(new NextMonthClickListener());
        final ImageView prevMonth = (ImageView) parent.findViewById(R.id.previous_month);
        prevMonth.setOnClickListener(new PreviousMonthClickListener());
        calendarGrid.setOnItemClickListener(new DayItemClickListener());

        calendarGrid.setAdapter(calendarAdapter);
        calendarGrid.setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return swipeDetector.onTouchEvent(event);
            }
        });
        calendarDayGrid.setAdapter(new ArrayAdapter<String>(ctx, R.layout.day_item, ctx
                .getResources().getStringArray(R.array.days_array)));
    }

    protected void updateCurrentMonth() {
        calendarAdapter.refreshDays(dayTag);
        currentMonth.setText(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月");

//        XUtil.debug("monthmonth====>"+curMonth);
    }

    public void updateCurrentMonth(Map<String, String> dayTag) {
        this.dayTag = dayTag;
        updateCurrentMonth();
    }

    private final class DayItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            dateChange(view);
            OnDayClickListener();
        }
    }

    private void dateChange(View view) {
        final TextView dayView = (TextView) view.findViewById(R.id.date);
        final CharSequence text = dayView.getText();

        if (text != null && !"".equals(text)) {
            int tmpDay = Integer.valueOf(String.valueOf(text));
            calendarAdapter.setSelected(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    tmpDay);
            dayDay = tmpDay;
        }
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public String getCurrentDate() {
        CalendarItem selItem = calendarAdapter.getSelectedDate();
        dayDay = selItem.day;
        String currentDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                + dayDay;
        return currentDate;
    }

    public String getCurrentDateYMD() {
        CalendarItem selItem = calendarAdapter.getSelectedDate();
        dayDay = selItem.day;

        Calendar tmp = Calendar.getInstance();
        tmp.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), dayDay);
//yyyy-MM-dd
        String ymd = TimeUtils.getTimeYMD(tmp.getTime().getTime() + "");
        return ymd;
    }

    public Long getCurrentDateLong() {
        CalendarItem selItem = calendarAdapter.getSelectedDate();
        dayDay = selItem.day;

        Calendar tmp = Calendar.getInstance();
        tmp.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), dayDay);
        return tmp.getTimeInMillis();
    }

    public String getCurrentDayStart() {
        CalendarItem selItem = calendarAdapter.getSelectedDate();
        dayDay = selItem.day;
        String currentDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                + dayDay;
        return currentDate;
    }


    protected final void onNextMonth() {

        calendarSwitcher.setInAnimation(ctx, R.anim.in_from_right);
        calendarSwitcher.setOutAnimation(ctx, R.anim.out_to_left);
        calendarSwitcher.showNext();
        if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
            calendar.set((calendar.get(Calendar.YEAR) + 1), Calendar.JANUARY, 1);
        } else {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        }
        int curMonth = calendar.get(Calendar.MONTH) + 1;
        OnMonthClickListener(false, curMonth);
        updateCurrentMonth();
    }

    protected final void onPreviousMonth() {

        calendarSwitcher.setInAnimation(ctx, R.anim.in_from_left);
        calendarSwitcher.setOutAnimation(ctx, R.anim.out_to_right);
        calendarSwitcher.showPrevious();
        if (calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
            calendar.set((calendar.get(Calendar.YEAR) - 1), Calendar.DECEMBER, 1);
        } else {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        }
        int curMonth = calendar.get(Calendar.MONTH) + 1;
        OnMonthClickListener(true, curMonth);
        updateCurrentMonth();
    }

    private final class NextMonthClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            onNextMonth();
        }
    }

    private final class PreviousMonthClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            onPreviousMonth();
        }
    }

    private final class SwipeGesture extends SimpleOnGestureListener {
        private final int swipeMinDistance;
        private final int swipeThresholdVelocity;

        public SwipeGesture(Context context) {
            final ViewConfiguration viewConfig = ViewConfiguration.get(context);
            swipeMinDistance = viewConfig.getScaledTouchSlop();
            swipeThresholdVelocity = viewConfig.getScaledMinimumFlingVelocity();
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > swipeMinDistance
                    && Math.abs(velocityX) > swipeThresholdVelocity) {
                onNextMonth();
            } else if (e2.getX() - e1.getX() > swipeMinDistance
                    && Math.abs(velocityX) > swipeThresholdVelocity) {
                onPreviousMonth();
            }
            return false;
        }
    }

    public Calendar getCalendar() {
        return calendar;
    }


    public abstract void OnDayClickListener();

    public abstract void OnMonthClickListener(boolean isPre, int month);
}
