package com.example.ccbim.ccbimpoi.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.ccbim.ccbimpoi.R;
import com.example.ccbim.ccbimpoi.data.CellData;
import com.example.ccbim.ccbimpoi.data.ExcelEnum;
import com.example.ccbim.ccbimpoi.data.ProjectCheckData;
import com.example.ccbim.ccbimpoi.widget.HomeListAdapter;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.datastorage.db.DbUtil;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;
import com.weqia.wq.data.base.NotifyData;
import com.weqia.wq.data.global.WeqiaApplication;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout mFrameLayout;
    private TextView mTextBefore;
    private TextView mTextLast;
    private RecyclerView mCommonRecyclerview;
    private HomeListAdapter homeListAdapter;
    private Button mTextWork;
    private Button mTextWorkDone;
    private ImageButton mImgAddForm;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<ProjectCheckData> listData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_layout);
        initView();
    }

    private void initView() {
        mFrameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mTextBefore = (TextView) findViewById(R.id.text_before);
        mTextBefore.setOnClickListener(this);
        mTextLast = (TextView) findViewById(R.id.text_last);
        mTextLast.setOnClickListener(this);
        mCommonRecyclerview = (RecyclerView) findViewById(R.id.common_recyclerview);
        mTextWork = (Button) findViewById(R.id.text_work);
        mTextWork.setOnClickListener(this);
        mTextWorkDone = (Button) findViewById(R.id.text_work_done);
        mTextWorkDone.setOnClickListener(this);
        mFrameLayout.setOnClickListener(this);
        mCommonRecyclerview.setOnClickListener(this);
        mImgAddForm = (ImageButton) findViewById(R.id.img_add_form);
        mImgAddForm.setOnClickListener(this);
//        DbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
//        listData = (ArrayList<ProjectCheckData>) dbUtil.findAll(ProjectCheckData.class);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        mCommonRecyclerview.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        homeListAdapter = new HomeListAdapter(this, listData);
        mCommonRecyclerview.setAdapter(homeListAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // 启动刷新的控件
                initData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        initData();
    }

    private void initData() {
        DbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        ArrayList<ProjectCheckData> list = (ArrayList<ProjectCheckData>) dbUtil.findAll(ProjectCheckData.class);
        listData.clear();
        listData.addAll(list);
        homeListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.text_before:
                break;
            case R.id.text_last:
                break;
            case R.id.text_work:
                break;
            case R.id.text_work_done:
                break;
            case R.id.frame_layout:
                break;
            case R.id.common_recyclerview:
                break;
            case R.id.img_add_form:
//                startActivity(new Intent(this, FormListActivity.class));
                showAddFormDialog();
                break;
        }
    }

    private void showAddFormDialog() {
//        final String[] items = {"防水检查1", "防水检查2", "防水检查3", "防水检查4"};
        ArrayList<String> excelList = new ArrayList<>();
        for (ExcelEnum excelEnum : ExcelEnum.values()) {
            excelList.add(excelEnum.getStrName());
        }
        final String[] items = excelList.toArray(new String[excelList.size()]);
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(this);
        listDialog.setTitle("");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(HomeActivity.this,
                        "你点击了" + items[which],
                        Toast.LENGTH_SHORT).show();
                showAddAddressDialog(items[which]);
            }
        });
        listDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showAddAddressDialog(items[which]);
                dialog.dismiss();
            }
        });
        listDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
/*        listDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });*/
        listDialog.show();
    }

    private void showAddAddressDialog(String name) {
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.add_form_dialog_layout, null);
        customizeDialog.setTitle("");
        customizeDialog.setView(dialogView);
        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText edit_text =
                                (EditText) dialogView.findViewById(R.id.edit_text);
/*                        DbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
                        NotifyData data = new NotifyData();
                        data.setTitle("好好范德萨");
                        dbUtil.save(data);
                        ArrayList<NotifyData> list= (ArrayList<NotifyData>) dbUtil.findAll(NotifyData.class);*/
                        if (StrUtil.notEmptyOrNull(edit_text.getText().toString())) {
                            ProjectCheckData data = JSON.parseObject(ExcelEnum.TOILETCHECKEXCEL.getValue(), ProjectCheckData.class);
                            data.setCheckPartName(edit_text.getText().toString());
                            data.getTabHead().add(new CellData(edit_text.getText().toString(), "6", "6", "6", "7"));
                            DbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
                            dbUtil.save(data);
                            listData.add(data);
                            homeListAdapter.notifyDataSetChanged();
                        } else {
                            L.toastShort("部位不能为空");
                        }
                        Toast.makeText(HomeActivity.this,
                                edit_text.getText().toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
        customizeDialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });
        customizeDialog.show();
    }

    public ArrayList<ProjectCheckData> getListData() {
        return listData;
    }

    public void setListData(ArrayList<ProjectCheckData> listData) {
        this.listData = listData;
    }
}
