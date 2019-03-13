package com.weqia.wq.component.sys;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.weqia.wq.R;
import com.weqia.wq.UtilApplication;
import com.weqia.wq.data.BaseData;
import com.weqia.wq.service.ZanCommonClickListen;

public class ZanSpan extends ClickableSpan {
    private String mid;
    private String coId;
    private int type;  //1查看联系人, 2位点内容
    private ZanCommonClickListen zlistener;
    private BaseData data;

    public ZanSpan(String mid, String coId, Integer type, ZanCommonClickListen zlistener) {
        super();
        this.mid = mid;
        this.coId = coId;
        this.type = type;
        this.zlistener = zlistener;
    }
    
    public ZanSpan(BaseData data, Integer type, ZanCommonClickListen zlistener) {
        super();
        this.data = data;
        this.type = type;
        this.zlistener = zlistener;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        if (type == 1) {
            ds.setColor(UtilApplication.ctx.getResources().getColor(R.color.underline_color));
        }
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {

        if (widget.getTag() != null) {
            widget.setTag(null);
            return;
        }
        
        if (zlistener != null) {
            zlistener.onCommonClick(mid, coId, data, type);
        }
        
////        super.onClick(widget);
//        if (type != null && type.intValue() == 1) {
//            XUtil.viewClickDo(ctx, mid, coId);
//        } else {
//            if (zlistener != null) {
//                zlistener.onCommonClick(data, type);
//            } else {
//                Intent newIntent = new Intent(ctx, WcActivity.class);
//                newIntent.putExtra(GlobalConstants.KEY_BASE_STRING, mid);
//                newIntent.putExtra(GlobalConstants.KEY_COID, coId);
//                ctx.startActivity(newIntent);
//            }
//        }
    }

}
