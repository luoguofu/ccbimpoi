package com.weqia.utils;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by berwin on 15/10/14.
 */
public class CustomLinkMovementMethod extends LinkMovementMethod {




    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer,
                                MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_MOVE) {
            // Prevent scroll event
            return true;
        }

        if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

            if (link.length != 0) {
                ClickableSpan cs = link[0];
                int csStart = buffer.getSpanStart(cs);
                int csEnd = buffer.getSpanEnd(cs);
                // offset在Span的区间内才认为是点击到了Span上。默认的处理是，如果点击在行尾，即使点击在了
                // Span外也认为点击了Span，判断点击offset小于line end的原因是点击内容区外面系统也认为点
                // 击的位置是最后一个文字，影响就是如果链接在行尾，点最后一个字母没反应，不过影响不大
                if (off >= csStart && off < csEnd && off < layout.getLineEnd(line)) {
                    if (action == MotionEvent.ACTION_UP) {
                        link[0].onClick(widget);
                    } else {
                        Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]));
                    }
                }
                return true;
            } else {
                Selection.removeSelection(buffer);
            }
        }

        return super.onTouchEvent(widget, buffer, event);
    }
}
