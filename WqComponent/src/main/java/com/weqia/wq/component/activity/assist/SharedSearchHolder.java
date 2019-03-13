package com.weqia.wq.component.activity.assist;

import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weqia.utils.view.CommonImageView;
import com.weqia.wq.component.view.PushCountView;

public class SharedSearchHolder {

    public PushCountView pushView;
    public ImageView imgHead;
    public CommonImageView ivIcon;
    public TextView tvTitle;
    public RelativeLayout rlZan;
    public LinearLayout llZan;
    public TextView tvTime;
    public TextView tvContent;



    public ImageView ivZanIcon;
    public TextView tvZanCount;


    public ImageView ivTasklevel;
    public TextView tvTitleTime;  //搜索布局的标题时间显示，只用与同事圈搜索是使用
    public CommonImageView ivLine;  //线条

    //    public View llPicture;
    public LinearLayout llAttachAll;
    public GridView gvPicture;
    public TextView picNumber;
    public CommonImageView ivAttach;
    public TextView  tvAttachCount;
    public View rlAttach;
    public TextView tvIvTitle;
}
