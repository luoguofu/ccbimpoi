package com.example.ccbim.ccbimpoi.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.ccbim.ccbimpoi.R;
import com.example.ccbim.ccbimpoi.data.ChildItemBean;
import com.example.ccbim.ccbimpoi.data.FormItemDataBean;
import com.example.ccbim.ccbimpoi.widget.FormListItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class FormListActivity extends AppCompatActivity {

    private ListView mListItem;
    private FormListItemAdapter formListItemAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_list_activity_layout);
        setTitle(R.string.form_list);
        initView();
        initData();
    }

    private void initView() {
        mListItem = (ListView) findViewById(R.id.list_item);
    }

    public void initData() {
        List<FormItemDataBean> shops = new ArrayList<FormItemDataBean>();
        for(int i = 0; i < 4; i++) {
            List<ChildItemBean> ods = new ArrayList<ChildItemBean>();
            for(int j = 0 ;j < 5; j++) {
                ChildItemBean od = new ChildItemBean(j+"", 3);
                ods.add(od);
            }
            FormItemDataBean sp = new FormItemDataBean(i+"东方饺子", ods);
            shops.add(sp);
        }
//        m=new OrderFormModel("wsf", "望京西街融科橄榄城西区", 12.3f, 2, 3, "2013-11-7 12:33", shops, 4f);

        formListItemAdapter = new FormListItemAdapter(this,shops);

//        adapter_2=new order_cplt_adapter(order_form_act.this, m.getShop_ordered().get(0).getList_ordered());
        mListItem.setAdapter(formListItemAdapter);


    }

}
