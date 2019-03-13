package com.weqia.utils;

import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.View;

public class CURLSpanUtils extends URLSpan {

    public CURLSpanUtils(String url) {
        super(url);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(0xFF576B95);// UtilApplication.ctx.getResources().getColor(R.color.underline_color));
        ds.setUnderlineText(false);
    }


    @Override
    public void onClick(View widget) {
        super.onClick(widget);
    }
}
