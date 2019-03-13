package com.weqia.wq.component.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weqia.wq.R;


/**
 * 头像+消息数目
 * 
 * @author Dminter
 * 
 */
public class PushSmallCountView extends PushCountView {

    public PushSmallCountView(Context ctx) {
        super(ctx);
    }

    public PushSmallCountView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
    }


    protected void initView() {
        if (isInEditMode()) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(getCtx());
        RelativeLayout layout = new RelativeLayout(getCtx());
        View view = inflater.inflate(R.layout.view_reused_small_count_icon, layout);
        if (view != null) {
            setIvIcon((com.weqia.utils.view.CommonImageView) view.findViewById(R.id.iv_reused_icon));
            setTvCount((TextView) view.findViewById(R.id.tv_reused_new_count));
            setmLayout((RelativeLayout) view.findViewById(R.id.rl_small_push));
        }
        this.addView(layout);

    }

}
