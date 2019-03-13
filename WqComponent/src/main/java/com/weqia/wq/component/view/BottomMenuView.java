package com.weqia.wq.component.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weqia.utils.view.CommonImageView;
import com.weqia.wq.R;

/**
 * Created by berwin on 15/10/16.
 */
public class BottomMenuView extends LinearLayout {

    private Context ctx;
    private TextView smallCount;
    private TextView bigCount;
    private CommonImageView ivIcon;
    private TextView tvContent;

    public BottomMenuView(Context context) {
        this(context, null);
    }

    public BottomMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        initView();
    }

    private void initView() {
        if (isInEditMode()) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(ctx);
        LinearLayout layout = new LinearLayout(ctx);
        View view = inflater.inflate(R.layout.view_bottom_tab_small, layout);
        if (view != null) {
            ivIcon =
                    (CommonImageView) view.findViewById(R.id.bottom_tab_view_rb_icon);
            tvContent = (TextView) view.findViewById(R.id.bottom_tab_view_tv_show);
            smallCount = (TextView) view.findViewById(R.id.bottom_tab_view_tv_count);
            bigCount = (TextView) view.findViewById(R.id.bottom_tab_view_tv_big_count);
        }
        this.addView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    public void setData(String text, int imgRes) {
        ivIcon.setImageResource(imgRes);
        tvContent.setText(text);
    }

    public TextView getTvContent() {
        return tvContent;
    }

    public TextView getSmallCount() {
        return smallCount;
    }

    public TextView getBigCount() {
        return bigCount;
    }

//    public void setText(String text) {
//        this.tvContent.setText(text);
//    }
//
//    public void setText(int id) {
//        this.tvContent.setText(ctx.getText(id));
//    }
//
//    public void setImage(int res) {
//        this.ivIcon.setImageResource(res);
//    }

//    public CommonImageView getImage() {
//        return ivIcon;
//    }
}