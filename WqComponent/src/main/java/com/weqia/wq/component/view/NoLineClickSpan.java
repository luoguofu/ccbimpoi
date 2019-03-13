package com.weqia.wq.component.view;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.weqia.utils.L;

//无下划线超链接，使用textColorLink、textColorHighlight分别修改超链接前景色和按下时的颜色
public class NoLineClickSpan extends ClickableSpan { 
    String text;

    public NoLineClickSpan(String text) {
        super();
        this.text = text;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false); //<span style="color: red;">//去掉下划线</span>
    }

    @Override
    public void onClick(View widget) { 
    	L.e("afdsfadsfadsfadf1111111111111``````````````````");
//        processHyperLinkClick(text); //<span style="color: red;">//点击超链接时调用</span>
    }
}