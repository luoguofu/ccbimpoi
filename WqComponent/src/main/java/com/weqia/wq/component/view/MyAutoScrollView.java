package com.weqia.wq.component.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;


public class MyAutoScrollView extends ScrollView {

    public MyAutoScrollView(Context context) {
        this(context, null);
    }

    public MyAutoScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyAutoScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private ScrollViewListener scrollViewListener = null;

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    public interface ScrollViewListener {
        void onScrollChanged(MyAutoScrollView scrollView, int x, int y, int oldx, int oldy);
    }
}
