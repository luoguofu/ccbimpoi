package com.weqia.wq.component.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ListListView extends ListView {

	public ListListView(Context context) {
		super(context);
	}

	public ListListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);

		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
