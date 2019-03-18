package com.example.ccbim.ccbimpoi.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.ccbim.ccbimpoi.R;
import com.example.ccbim.ccbimpoi.data.FormItemDataBean;

public class FormItemView extends LinearLayout {

    private View formItemView;
    private FormItemDataBean formItemDataBeans;
    private Context mContext;
    private ChildListView childListView;
    private ChildListViewAdapter adapter;
    private TextView res_name;
    private TextView total;

    public FormItemView(Context context) {
        super(context);
    }

    public FormItemView(Context context, FormItemDataBean formItemDataBeanList) {
        super(context);
        mContext=context;
        formItemDataBeans=formItemDataBeanList;
//        adapter=new ChildListViewAdapter(mContext, formItemDataBeanList.getChildItemBeanList());
        initView();
    }
    private void initView() {
        formItemView=LayoutInflater.from(mContext).inflate(R.layout.form_item_view_layout, null);
        childListView=(ChildListView)formItemView.findViewById(R.id.list_item);
        childListView.setAdapter(adapter);
    }


}