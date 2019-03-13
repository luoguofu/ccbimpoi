package com.weqia.utils.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.weqia.utils.exception.CheckedExceptionHandler;

public class CustomViewPager extends ViewPager {

	private boolean enabled;

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.enabled = true;
	}

	@SuppressLint("ClickableViewAccessibility")
    @Override
	public boolean onTouchEvent(MotionEvent event) {
		if (this.enabled) {
			return super.onTouchEvent(event);
		}

		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			if (this.enabled) {
				return super.onInterceptTouchEvent(ev);

			}
			return false;
		} catch (IllegalArgumentException e) {
			CheckedExceptionHandler.handleException(e);
			return false;
		}

	}

	public void setSlidable(boolean enabled) {
		this.enabled = enabled;
	}

}
