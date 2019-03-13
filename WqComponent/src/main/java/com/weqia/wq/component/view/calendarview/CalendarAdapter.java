package com.weqia.wq.component.view.calendarview;

import java.util.Calendar;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.weqia.utils.StrUtil;
import com.weqia.utils.TimeUtils;
import com.weqia.wq.R;

public class CalendarAdapter extends BaseAdapter {
    private static final int FIRST_DAY_OF_WEEK = Calendar.MONDAY;
    public static final String CAL_XIU = "3";
    private final Calendar calendar;
    @SuppressWarnings("unused")
    private final CalendarItem today;
    private CalendarItem selected;
    private final LayoutInflater inflater;
    private CalendarItem[] days;
    private Context ctx;


    public CalendarAdapter(Context context, Calendar monthCalendar) {
        calendar = monthCalendar;
        today = new CalendarItem(monthCalendar, 0);
        selected = new CalendarItem(monthCalendar, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.ctx = context;
    }

    @Override
    public int getCount() {
        return days.length;
    }

    @Override
    public Object getItem(int position) {
        return days[position];
    }

    @Override
    public long getItemId(int position) {
        final CalendarItem item = days[position];
        if (item != null) {
            return days[position].id;
        }
        return -1;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.calendar_item, null);
        }
        final TextView dayView = (TextView) view.findViewById(R.id.date);
        final ImageView ivTag = (ImageView) view.findViewById(R.id.ivTag);
        final CalendarItem currentItem = days[position];

        if (currentItem == null) {
            dayView.setClickable(false);
            dayView.setFocusable(false);
            dayView.setBackgroundResource(android.R.color.transparent);
            dayView.setText(null);

            ivTag.setVisibility(View.GONE);
        } else {

            if (currentItem.equals(selected)) {
                dayView.setBackgroundResource(R.drawable.sel_background);
                dayView.setTextColor(ctx.getResources().getColor(R.color.white));
            } else {
                dayView.setBackgroundResource(android.R.color.transparent);
                dayView.setTextColor(ctx.getResources().getColor(R.color.black));
            }

            dayView.setText(currentItem.text);
            CalendarItem curDay = days[position];
            if (curDay.tag != 0) {
                ivTag.setVisibility(View.VISIBLE);
                if (curDay.tag == 1) {
                    ivTag.setImageResource(R.drawable.icon_ban);
                    dayView.setTextColor(ctx.getResources().getColor(R.color.cal_1));

                } else if (curDay.tag == 2) {
                    ivTag.setImageResource(R.drawable.icon_xiu);
                    dayView.setTextColor(ctx.getResources().getColor(R.color.cal_2));

                } else if (curDay.tag == 3) {
                    ivTag.setVisibility(View.GONE);
                    dayView.setTextColor(ctx.getResources().getColor(R.color.cal_2));
                } else if (curDay.tag == 4) {
                    ivTag.setVisibility(View.GONE);
//                    ivTag.setImageResource(R.drawable.icon);
                    dayView.setTextColor(ctx.getResources().getColor(R.color.cal_1));
                } else {
                    ivTag.setVisibility(View.GONE);
                }

            } else {
                ivTag.setVisibility(View.GONE);
            }

            if (currentItem.equals(selected)) {
                dayView.setBackgroundResource(R.drawable.sel_background);
                dayView.setTextColor(ctx.getResources().getColor(R.color.white));
            }

            int month = TimeUtils.getCurMonth();
            if (month == currentItem.month + 1) {
                Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                if (day == currentItem.day && !currentItem.equals(selected)) {
                    dayView.setTextColor(ctx.getResources().getColor(R.color.cal_3));
                }
            }

        }

        return view;
    }

    public final void setSelected(int year, int month, int day) {
        selected.year = year;
        selected.month = month;
        selected.day = day;
        notifyDataSetChanged();
    }

    public CalendarItem getSelectedDate() {
        return selected;
    }

    // 1-1 x月1日 休
    public final void refreshDays() {
        refreshDays(null);
    }

    public final void refreshDays(Map<String, String> dayTag) {
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        final int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        final int blankies;
        final CalendarItem[] days;

        if (firstDayOfMonth == FIRST_DAY_OF_WEEK) {
            blankies = 0;
        } else if (firstDayOfMonth < FIRST_DAY_OF_WEEK) {
            blankies = Calendar.SATURDAY - (FIRST_DAY_OF_WEEK - 1);
        } else {
            blankies = firstDayOfMonth - FIRST_DAY_OF_WEEK;
        }
        days = new CalendarItem[lastDayOfMonth + blankies];

        int tagInt = 0;
        for (int day = 1, position = blankies; position < days.length; position++) {


            if (dayTag != null && dayTag.size() > 0) {


                try {
                    Calendar tmpCal = Calendar.getInstance();
                    tmpCal.set(year, month, day);
                    tagInt = 0;
                    String tag = dayTag.get(TimeUtils.getDateYDM(tmpCal.getTime()));
                    if (StrUtil.notEmptyOrNull(tag)) {
                        tagInt = Integer.parseInt(tag);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            days[position] = new CalendarItem(year, month, day++, tagInt);
        }

        this.days = days;
        notifyDataSetChanged();
    }

    public static class CalendarItem {
        public int year;
        public int month;
        public int day;
        public int tag;//1休，2班
        public String text;
        public long id;

        public CalendarItem(Calendar calendar, int tag) {
            this(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), tag);
        }

        public CalendarItem(int year, int month, int day, int tag) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.tag = tag;
            this.text = String.valueOf(day);
            this.id = Long.valueOf(year + "" + month + "" + day);
        }

        @Override
        public boolean equals(Object o) {
            if (o != null && o instanceof CalendarItem) {
                final CalendarItem item = (CalendarItem) o;
                return item.year == year && item.month == month && item.day == day;
            }
            return false;
        }
    }
}