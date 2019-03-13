package com.weqia.wq.component.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weqia.utils.StrUtil;
import com.weqia.wq.R;

public class BottomNumView extends LinearLayout {  //自定义组件

    private Context ctx;  //上下文对象！
    private TextView tvBottomNum;
    private TextView tvBottomTitle;

    public BottomNumView(Context context) {
        this(context, null);
    }

    public BottomNumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        initView();
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    public Context getCtx() {
        return ctx;
    }

    private void initView() {
        if (isInEditMode()) {
            //if (isInEditMode()) { return; }，防止可视化编辑报错
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(ctx);  //找到LayoutInflater对象
        LinearLayout layout = new LinearLayout(ctx);
        View view = inflater.inflate(R.layout.view_bottom_num, layout);  //layout应该是父布局
        if (view != null) {
            setTvBottomNum((TextView) view.findViewById(R.id.tv_bottom_num));  //set（）方法。可以在外界存储值
            setTvBottomTitle((TextView) view.findViewById(R.id.tv_bottom_title));
        }
        this.addView(layout);  //在BottomNumView布局里添加布局
    }


    @Override
    public void setSelected(boolean selected) {   //选中之后字体颜色的变化！
        super.setSelected(selected);
        if (selected) {
            tvBottomNum.setTextColor(getResources().getColor(R.color.most_new_blue));
            tvBottomTitle.setTextColor(getResources().getColor(R.color.most_new_blue));
        } else {
            tvBottomNum.setTextColor(getResources().getColor(R.color.black_font));
            tvBottomTitle.setTextColor(getResources().getColor(R.color.black_font));
        }
    }

    public TextView getTvBottomNum() {
        return tvBottomNum;
    }

    public void setTvBottomNum(TextView tvBottomNum) {
        this.tvBottomNum = tvBottomNum;
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

    public void setNum(Integer num) {
        if (num == null) {
            this.tvBottomNum.setVisibility(View.GONE);
            this.tvBottomTitle.setPadding(0, 0, 0, 0);
        } else {
            this.tvBottomNum.setVisibility(View.VISIBLE);
            this.tvBottomTitle.setPadding(0, -2, 0, 0);
            this.tvBottomNum.setText(num + "");
        }
    }

    public Integer getNum() {
        if (this.tvBottomNum.getVisibility() == View.GONE) {
            return 0;
        } else {
            String tvNum = this.tvBottomNum.getText().toString();
            if (StrUtil.isEmptyOrNull(tvNum)) {
                return 0;
            } else {
                try {
                    int num = Integer.parseInt(tvNum);
                    return num;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return 0;
                }

            }
        }
    }
}
