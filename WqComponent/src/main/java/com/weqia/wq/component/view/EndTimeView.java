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
public abstract class EndTimeView implements OnClickListener {

	private String[] datas;
	private LinearLayout rootView;
	private LayoutInflater inflater;
	public static final Integer ID_ENDTIME_BEGIN = 12345;
	private static final long DAY_MILLS = 24 * 60 * 60 * 1000l;

	public EndTimeView(Activity ctx, int index) {
		datas = ctx.getResources().getStringArray(R.array.end_time_array);
		inflater = LayoutInflater.from(ctx);
		rootView = (LinearLayout) ctx.findViewById(R.id.ll_reused_endtime);
		if (rootView != null) {
			rootView.removeAllViews();
			for (int i = 0; i < datas.length; i++) {
				addCellView(datas[i], i);
			}
		}
		if (rootView != null) {
			TextView tv = (TextView) rootView.getChildAt(index);
			tv.setSelected(true);
			setTag(tv, index);
		}

	}

	public EndTimeView(Activity ctx) {
		datas = ctx.getResources().getStringArray(R.array.end_time_array);   //结束时间的时间字符串数组
		inflater = LayoutInflater.from(ctx);   //获取inflater对象
		rootView = (LinearLayout) ctx.findViewById(R.id.ll_reused_endtime);   //获取横向滚动条的对象
		if (rootView != null) {
			rootView.removeAllViews();
			for (int i = 0; i < datas.length; i++) {
				addCellView(datas[i], i);  //通过循环将字符串数组中的元素添加到textView控件中去，并将textView再一次添加到横向滚动条中去
			}
		}

	}

	private void addCellView(String name, Integer index) {
		TextView tv = (TextView) inflater.inflate(
				R.layout.view_reused_endtime_button, null);
		if (tv != null) {
			tv.setText(name);
			tv.setId(ID_ENDTIME_BEGIN + index);
			tv.setOnClickListener(this);
			setTag(tv, index);
			rootView.addView(tv, index);
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
			tv.setTag(DAY_MILLS * 7);
			break;
		case 6:
			tv.setTag(DAY_MILLS * 14);
			break;
		case 7:
			tv.setTag(DAY_MILLS * 30);
			break;
		case 8:
			tv.setTag(DAY_MILLS * 60);
			break;
		default:
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
