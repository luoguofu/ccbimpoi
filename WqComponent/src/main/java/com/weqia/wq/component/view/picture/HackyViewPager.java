package com.weqia.wq.component.view.picture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HackyViewPager extends ViewPager {
	public boolean isScrollable = true;

	public HackyViewPager(Context context) {
		super(context);
	}

	public HackyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			return isScrollable?super.onInterceptTouchEvent(ev):false;
		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
			return false;
		}
	}
	
	@SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
//            ex.printStackTrace();
            // ignore it
        }
        return false;
    }

}
