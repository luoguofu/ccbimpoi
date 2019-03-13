package com.weqia.wq.component.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

public class LinksTextView extends AppCompatTextView {

    public LinksTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LinksTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinksTextView(Context context) {
        super(context);
    }

//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        super.onTouchEvent(event);
//        try {
//            ClickableSpan[] links =
//                    ((Spannable) this.getText()).getSpans(getSelectionStart(), getSelectionEnd(),
//                            ClickableSpan.class);
//            if (links.length != 0) {
//                links[0].onClick(this);
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            return false;
//        }
//
//    }
}
