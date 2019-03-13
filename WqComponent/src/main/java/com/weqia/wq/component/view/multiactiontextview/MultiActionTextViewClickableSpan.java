package com.weqia.wq.component.view.multiactiontextview;

import android.text.TextPaint;
import android.text.style.ClickableSpan;

public abstract class MultiActionTextViewClickableSpan extends ClickableSpan {

    private boolean isUnderLineRequired;

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(isUnderLineRequired);
    }

    public MultiActionTextViewClickableSpan(boolean isUnderLineRequired) {
        this.isUnderLineRequired = isUnderLineRequired;
    }

}