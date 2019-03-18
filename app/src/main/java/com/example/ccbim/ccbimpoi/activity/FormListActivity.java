package com.example.ccbim.ccbimpoi.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.ccbim.ccbimpoi.R;
import com.example.ccbim.ccbimpoi.data.CellData;
import com.example.ccbim.ccbimpoi.data.CheckDetailData;
import com.example.ccbim.ccbimpoi.data.ChildItemBean;
import com.example.ccbim.ccbimpoi.data.FormItemDataBean;
import com.example.ccbim.ccbimpoi.data.ProjectCheckData;
import com.example.ccbim.ccbimpoi.util.ConstantUtil;
import com.example.ccbim.ccbimpoi.widget.FormListItemAdapter;
import com.weqia.utils.datastorage.db.DbUtil;
import com.weqia.wq.data.eventbus.RefreshEvent;
import com.weqia.wq.data.global.WeqiaApplication;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class FormListActivity extends AppCompatActivity {

    private ListView mListItem;
    private FormListItemAdapter formListItemAdapter;
    private ProjectCheckData projectCheckData;
    private boolean completeStatus = true; //完成状态，false为未完成，true为已完成

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_list_activity_layout);
        EventBus.getDefault().register(this);
        setTitle(R.string.form_list);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        projectCheckData = (ProjectCheckData) getIntent().getSerializableExtra(ConstantUtil.PROJECTEXTRA);
        if (projectCheckData != null) {
            ArrayList<CellData> tabBody = (ArrayList<CellData>) JSON.parseArray(projectCheckData.getTabBodyStr(), CellData.class);
            projectCheckData.setTabBody(tabBody);
            ArrayList<CellData> tabHead = (ArrayList<CellData>) JSON.parseArray(projectCheckData.getTabHeadStr(), CellData.class);
            projectCheckData.setTabHead(tabHead);
            ArrayList<CellData> tabFoot = (ArrayList<CellData>) JSON.parseArray(projectCheckData.getTabFootStr(), CellData.class);
            projectCheckData.setTabFoot(tabFoot);
        }
        initView();
        initData();
    }

    private void initView() {
        mListItem = (ListView) findViewById(R.id.list_item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            back();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void back() {
        completeStatus = true;
        for (CellData cellData : projectCheckData.getTabBody()) {
            for (CheckDetailData checkDetailData : cellData.getSubCellList()) {
                if (checkDetailData.getStatus() != 1) {
                    completeStatus = false;
                }
            }
        }
        if (completeStatus) {
            projectCheckData.setCompleteStatus(1);
        } else {
            projectCheckData.setCompleteStatus(0);
        }
        projectCheckData.setTabHeadStr(projectCheckData.getTabHead().toString());
        projectCheckData.setTabBodyStr(projectCheckData.getTabBody().toString());
        projectCheckData.setTabFootStr(projectCheckData.getTabFoot().toString());
        DbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        dbUtil.save(projectCheckData,true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }

    public void initData() {
/*        List<FormItemDataBean> shops = new ArrayList<FormItemDataBean>();
        for(int i = 0; i < 4; i++) {
            List<ChildItemBean> ods = new ArrayList<ChildItemBean>();
            for(int j = 0 ;j < 5; j++) {
                ChildItemBean od = new ChildItemBean(j+"", 3);
                ods.add(od);
            }
            FormItemDataBean sp = new FormItemDataBean(i+"东方饺子", ods);
            shops.add(sp);
        }*/
//        m=new OrderFormModel("wsf", "望京西街融科橄榄城西区", 12.3f, 2, 3, "2013-11-7 12:33", shops, 4f);
        ArrayList<CellData> list = projectCheckData.getTabBody();
        formListItemAdapter = new FormListItemAdapter(this,list);
        mListItem.setAdapter(formListItemAdapter);


    }
    // 添加成功 刷新界面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final RefreshEvent event) {
        String key = event.key;
        if ("projectdata".equals(key)) {
            projectCheckData = (ProjectCheckData) event.obj;
            initData();
        }
    }

    public ProjectCheckData getProjectCheckData() {
        return projectCheckData;
    }

    public void setProjectCheckData(ProjectCheckData projectCheckData) {
        this.projectCheckData = projectCheckData;
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }
}
