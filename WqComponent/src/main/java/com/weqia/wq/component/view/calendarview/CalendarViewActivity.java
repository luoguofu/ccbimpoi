package com.weqia.wq.component.view.calendarview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.weqia.utils.TimeUtils;
import com.weqia.utils.view.timepicker.TimePicker;
import com.weqia.wq.R;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;
import com.weqia.wq.component.utils.CalendarUtil;
import com.weqia.wq.data.global.GlobalConstants;

public class CalendarViewActivity extends SharedDetailTitleActivity {
    private Long timeChoose = 0l;
    // private boolean dateChange = false;
    private TimePicker timePicker;
    private CalendarUtil calendarUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        initView();
    }

    private void initView() {
        sharedTitleView.initTopBanner("提醒时间", "确定");
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        calendarUtil = new CalendarUtil(this, getWindow().getDecorView()) {
            @Override
            public void OnDayClickListener() {

            }

            @Override
            public void OnMonthClickListener(boolean isPre, int month) {

            }
        };
        String timeStr =
                TimeUtils.getToDay() + " " + timePicker.getHour() + ":" + timePicker.getMinute();
        timeChoose = TimeUtils.parseDateLong(timeStr);
    }

    @Override
    public void onClick(View v) {

        super.onClick(v);
        if (v.getId() == R.id.topbanner_button_string_right) {
            // if (!dateChange) {
            String timeStr =
                    calendarUtil.getCurrentDate() + " " + timePicker.getHour() + ":"
                            + timePicker.getMinute();
            timeChoose = TimeUtils.parseDateLong(timeStr);
            // }
            Intent back_intent = new Intent();
            back_intent.putExtra(GlobalConstants.TIME_CHOOSE_LONG, timeChoose);
            setResult(RESULT_OK, back_intent);
            finish();
        }
    }
}
