package com.weqia.utils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class FocusTextView extends TextView {

    public FocusTextView(Context context) {
        super(context);
    }

    public FocusTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setPressed(boolean bPresed) {
        if (((View) getParent()).isPressed()) {
            return;
        } else {
            super.setPressed(bPresed);
        }
    }
    
    public void setPre(boolean bPresed) {
        super.setPressed(bPresed);
    }
}
