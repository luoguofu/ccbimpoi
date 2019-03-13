package com.weqia.wq.component.sys;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.weqia.wq.R;
import com.weqia.wq.UtilApplication;

public class HbSpan extends ClickableSpan {
    private String envId;
    private HbClickListen hbClickListen;

    public HbSpan(String envId, HbClickListen hbClickListen) {
        super();
        this.envId = envId;
        this.hbClickListen = hbClickListen;
    }


    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(UtilApplication.ctx.getResources().getColor(R.color.hb_click));
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        if (widget.getTag() != null) {
            widget.setTag(null);
            return;
        }
        if (hbClickListen != null) {
            hbClickListen.onhbClick(envId);
        }
        
    }

}
