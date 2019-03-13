package com.weqia.wq.component.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weqia.wq.R;

/**
 * Created by ML on 2016/10/13.
 */

public class BottomLinesNumView extends LinearLayout {

    private TextView tvLine;
    private Context ctx;  //上下文对象！
    private TextView tvBottomTitle;

    public BottomLinesNumView(Context context) {
        super(context);
    }

    public BottomLinesNumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        initView();
    }

    public void setTvLine(TextView tvLine) {
        this.tvLine = tvLine;
    }

    public TextView getTvLine() {
        return tvLine;
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    public Context getCtx() {
        return ctx;
    }

    public void initView(){
        if (isInEditMode()) {
            //if (isInEditMode()) { return; }，防止可视化编辑报错
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(getCtx());
        LinearLayout layout = new LinearLayout(getCtx());
        View view = inflater.inflate(R.layout.view_bottom_num_line,layout);
        if (view != null) {
            setTvBottomTitle((TextView) view.findViewById(R.id.tv_bottom_title_line));
            setTvLine((TextView)view.findViewById(R.id.tv_bottom_line));
        }
        LayoutParams layoutParams =new LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(layoutParams);  //配置View的大小！
        this.addView(layout);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            getTvBottomTitle().setTextColor(getResources().getColor(R.color.main_color));
            getTvLine().setBackgroundColor(getResources().getColor(R.color.main_color));
        } else {
            getTvBottomTitle().setTextColor(getResources().getColor(R.color.black_font));
            getTvLine().setBackgroundColor(getResources().getColor(R.color.white));
        }
    }


    public TextView getTvBottomTitle() {
        return tvBottomTitle;
    }

    public void setTvBottomTitle(TextView tvBottomTitle) {
        this.tvBottomTitle = tvBottomTitle;
    }

    public void setText(String text) {
        this.tvBottomTitle.setText(text);
    }

    public void setText(int id) {
        this.tvBottomTitle.setText(ctx.getText(id));
    }


}
