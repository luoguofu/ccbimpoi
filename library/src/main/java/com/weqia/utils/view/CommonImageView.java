package com.weqia.utils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.weqia.utils.L;

public class CommonImageView extends ImageView {

    public CommonImageView(Context context) {
        super(context);
    }

    public CommonImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        } catch (Exception e) {
           L.e("Commoncom.weqia.utils.view.CommonImageView  -> onDraw() Canvas: trying to use a recycled bitmap");
        }
    }
}
