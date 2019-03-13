package com.weqia.wq.component.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class ScrollerGridView extends GridView {

    public ScrollerGridView(Context context) {
        super(context);
    }

    public ScrollerGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollerGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
