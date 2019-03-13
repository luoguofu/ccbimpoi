package com.weqia.wq.component.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weqia.wq.R;

/**
 * 结束时间选框
 * 
 * @author Dminter
 * 
 */
public abstract class EndDateView implements OnClickListener {

    private String[] datas;
    private LinearLayout rootView;
    private LayoutInflater inflater;
    public static final Integer ID_ENDTIME_BEGIN = 12345;
    private static final long DAY_MILLS = 24 * 60 * 60 * 1000l;
    private boolean showNull = true;

    public EndDateView(Activity ctx, Integer index) {
        this(ctx, index, true);

    }

    public EndDateView(Activity ctx) {
        this(ctx, null);
    }

    public EndDateView(Activity ctx, Integer index, boolean shown) {
        this.showNull = shown;
        datas = ctx.getResources().getStringArray(R.array.end_time_array);
        inflater = LayoutInflater.from(ctx);
        rootView = (LinearLayout) ctx.findViewById(R.id.ll_reused_endtime);
        if (rootView != null) {
            rootView.removeAllViews();
            int i = 0;
            if (!showNull) {
                i = 1;
            }
            for (; i < datas.length; i++) {
                if (!showNull) {
                    addCellView(datas[i], i, showNull);
                }
            }
        }
        if (index != null) {
            if (rootView != null) {
                TextView tv = (TextView) rootView.getChildAt(index);
                tv.setSelected(true);
                // setTag(tv, index);
            }
        }
    }

    private void addCellView(String name, Integer index, boolean showNull) {
        TextView tv = (TextView) inflater.inflate(R.layout.view_reused_endtime_button, null);
        if (tv != null) {
            tv.setText(name);
            tv.setId(ID_ENDTIME_BEGIN + index);
            tv.setOnClickListener(this);
            setTag(tv, index);
            if (!showNull) {
                rootView.addView(tv, index - 1);
            } else {
                rootView.addView(tv, index);
            }
        }
    }

    private void setTag(TextView tv, Integer index) {
        switch (index) {
            case 0:
                tv.setTag(0l);
                break;
            case 1:
                tv.setTag(1l);
                break;
            case 2:
                tv.setTag(DAY_MILLS);
                break;
            case 3:
                tv.setTag(DAY_MILLS * 2);
                break;
            case 4:
                tv.setTag(DAY_MILLS * 3);
                break;
            case 5:
                tv.setTag(DAY_MILLS * 6);
                break;
            case 6:
                tv.setTag(DAY_MILLS * 13);
                break;
            case 7:
                tv.setTag(DAY_MILLS * 29);
                break;
            case 8:
                tv.setTag(DAY_MILLS * 59);
                break;

        }
    }

    @Override
    public void onClick(View v) {
        onChangeTimeByScroller((Long) v.getTag());
        if (rootView == null) {
            return;
        }
        for (int i = 0; i < rootView.getChildCount(); i++) {
            TextView tv = (TextView) rootView.getChildAt(i);
            if (tv == v) {
                tv.setSelected(true);
            } else {
                tv.setSelected(false);
            }
        }
    }

    public abstract void onChangeTimeByScroller(long timeInteval);
}
