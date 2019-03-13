package com.weqia.wq.component.sys;

import android.text.Layout;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import com.weqia.wq.global.ComponentUtil;
import com.weqia.wq.data.global.WeqiaApplication;



public abstract class EasyTouchListener implements OnTouchListener {

    private EasyGestureDetector mGestureDetector;

    public EasyTouchListener() {
        mGestureDetector = new EasyGestureDetector(WeqiaApplication.ctx, new MyOnGestureListener());
    }

    class MyOnGestureListener extends SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (mGestureDetector.getGesView() == null) {
                return false;
            }
            if (mGestureDetector.getGesView() instanceof TextView) {
                TextView widget = (TextView) mGestureDetector.getGesView();
                CharSequence text = widget.getText();
                Spannable stext = Spannable.Factory.getInstance().newSpannable(text);
                int x = (int) e.getX();
                int y = (int) e.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);
                int bet = layout.getOffsetForHorizontal(line, x - ComponentUtil.dip2px(20));
                if (x - ComponentUtil.dip2px(20) > 0 && bet == off) {
                    clickPress();
                } else {
                    ClickableSpan[] link = stext.getSpans(off, off, ClickableSpan.class);
                    if (link.length != 0) {
                        link[0].onClick(widget);
                    } else {
                        clickPress();
                    }
                }
            } else {
                clickPress();
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            longPress();
        }
    }

    @Override
    public boolean onTouch(View v, final MotionEvent event) {
        mGestureDetector.setGesView(v);
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    public abstract void clickPress();

    public abstract void longPress();

}
