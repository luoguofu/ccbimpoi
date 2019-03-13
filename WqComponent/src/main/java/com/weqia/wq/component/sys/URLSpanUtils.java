package com.weqia.wq.component.sys;

import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.View;

import com.jrmf360.walletlib.ui.WebViewActivity;
import com.weqia.utils.StrUtil;
import com.weqia.wq.R;
import com.weqia.wq.UtilApplication;
import com.weqia.wq.data.base.WebViewData;
import com.weqia.wq.data.global.WeqiaApplication;

public class URLSpanUtils extends URLSpan {

    int color = 0;

    public URLSpanUtils(String url) {
        super(url);
    }

    public URLSpanUtils(String url, int color) {
        super(url);
        this.color = color;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        if (color == 0) {
            color = UtilApplication.ctx.getResources().getColor(R.color.underline_color);
        }
        ds.setColor(color);
        if (color==UtilApplication.ctx.getResources().getColor(R.color.talk_me_link_color)){
            ds.setUnderlineText(true);
        }else {
            ds.setUnderlineText(false);
        }

    }

    @Override
    public void onClick(View widget) {
        // super.onClick(widget);
        if (widget.getTag() != null) {
            widget.setTag(null);
            return;
        }
        try {
            Context context = widget.getContext();
            String url = getURL();
            if (StrUtil.notEmptyOrNull(url)) {
                if (url.startsWith("mailto:") || url.startsWith("tel:")) {
                    super.onClick(widget);
                } else {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("WebViewData", new WebViewData(WeqiaApplication.getInstance()
                            .getString(R.string.weqia_str), getURL()));
                    context.startActivity(intent);
                }
            } else {
                super.onClick(widget);
            }
        } catch (Exception e) {
            super.onClick(widget);
            e.printStackTrace();
        }
    }
}
