package com.example.ccbim.ccbimpoi.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.alibaba.fastjson.JSON;
import com.example.ccbim.ccbimpoi.R;
import com.example.ccbim.ccbimpoi.data.CellData;
import com.example.ccbim.ccbimpoi.data.ExcelEnum;
import com.example.ccbim.ccbimpoi.data.ProjectCheckData;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.wq.component.db.WeqiaDbUtil;
import com.weqia.wq.data.eventbus.RefreshEvent;
import com.weqia.wq.data.global.WeqiaApplication;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lgf on 2019/4/9.
 */

public class BatchAddActivity extends BaseActivity {
    private EditText etBuildingNum,etFloorNum, etExcelNum;
    private Spinner spExcelSelecr;
    private ArrayList<String> excelList = new ArrayList<>();
    private String selectExcelName;
    public static String SPLITEXCEL = "-";
    private String companyName;
    private String projectName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_add);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        EventBus.getDefault().register(this);
        etBuildingNum = (EditText) findViewById(R.id.et_building_num);
        etFloorNum = (EditText) findViewById(R.id.et_floor_num);
        etExcelNum = (EditText) findViewById(R.id.et_excel_num);
        spExcelSelecr = (Spinner) findViewById(R.id.sp_excel_select);
        initSpinner();
        SharedPreferences sharedPreferences= getSharedPreferences("setting", Context.MODE_PRIVATE);
        companyName = sharedPreferences.getString("companyName", "");
        projectName = sharedPreferences.getString("projectName", "");
    }
    private void initSpinner() {
        excelList.clear();
        for (ExcelEnum excelEnum : ExcelEnum.values()) {
            excelList.add(excelEnum.getStrName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, excelList);
        spExcelSelecr.setAdapter(adapter);

        spExcelSelecr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectExcelName = excelList.get(i);
//                L.toastShort("您点击了" + excelList.get(i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        } );
    }
    // 添加成功 刷新界面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final RefreshEvent event) {
        String key = event.key;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_batch, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sure:
                if (StrUtil.isEmptyOrNull(etBuildingNum.getText().toString())) {
                    L.toastShort("请先填写楼号");
                }
                if (StrUtil.isEmptyOrNull(etFloorNum.getText().toString())) {
                    L.toastShort("请先填写层数");
                }
                if (StrUtil.isEmptyOrNull(etExcelNum.getText().toString())) {
                    L.toastShort("请先填写单层数量");
                }
                WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
                //楼号
                ProjectCheckData buildingData = new ProjectCheckData();
                buildingData.setExcelFullName(etBuildingNum.getText().toString());
                buildingData.setLevel(1);
                buildingData.setParentId(-1);
                buildingData.setFileType(2);
                dbUtil.save(buildingData);

                ProjectCheckData floorParentData = null;
                List<ProjectCheckData> list = dbUtil.findAllByWhere(ProjectCheckData.class, "excelFullName = '" + buildingData.getExcelFullName() + "' and parentId = -1");
                if (StrUtil.listNotNull(list)) {
                    floorParentData = list.get(0);
                }
                if (StrUtil.notEmptyOrNull(selectExcelName) && floorParentData != null) {

                    //表单名
                    ProjectCheckData floorData = new ProjectCheckData();
                    floorData.setExcelFullName(selectExcelName);
                    floorData.setLevel(2);
                    floorData.setParentId(floorParentData.getId());
                    floorData.setFileType(2);
                    dbUtil.save(floorData);

                    List<ProjectCheckData> floorist = dbUtil.findAllByWhere(ProjectCheckData.class, "excelFullName = '" + selectExcelName + "' and parentId = " + floorParentData.getId());
                    ProjectCheckData excelParentData = null;
                    if (StrUtil.listNotNull(floorist)) {
                        excelParentData = floorist.get(0);
                    }
                    if (isNumeric(etFloorNum.getText().toString())) {
                        if (isNumeric(etExcelNum.getText().toString())) {
                            if (excelParentData != null) {
                                for (int j = 1; j <= Integer.parseInt(etFloorNum.getText().toString()); j++) {
                                    for (int i = 1; i <= Integer.parseInt(etExcelNum.getText().toString()); i++) {
                                        ProjectCheckData data = JSON.parseObject(ExcelEnum.nameOf(selectExcelName).getValue(), ProjectCheckData.class);
                                        data.setCheckPartName(etBuildingNum.getText().toString() + SPLITEXCEL + j + "F" + SPLITEXCEL + i);
                                        data.setExcelFullName(data.getCheckPartName() + data.getExcelName());
                                        if (StrUtil.notEmptyOrNull(companyName)) {
                                            data.getTabHead().get(4).setCellName(companyName);
                                        }
                                        if (StrUtil.notEmptyOrNull(projectName)) {
                                            data.getTabHead().get(10).setCellName(projectName);
                                        }
                                        data.getTabHead().add(new CellData(data.getCheckPartName(), "6", "6", "6", "7"));
                                        data.setTabHeadStr(data.getTabHead().toString());
                                        data.setTabBodyStr(data.getTabBody().toString());
                                        data.setTabFootStr(data.getTabFoot().toString());
                                        data.setLevel(3);
                                        data.setParentId(excelParentData.getId());
                                        dbUtil.save(data);
                                    }
                                }

                            }
                        } else {
                            L.toastShort("单层数量请输入数字！");
                        }
                    } else {
                        L.toastShort("层数请输入数字！");
                    }
                }
                EventBus.getDefault().post(new RefreshEvent("projectCheckDataRefresh"));
                finish();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * 利用正则表达式判断字符串是否是数字
     * @param str
     * @return
     */
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
