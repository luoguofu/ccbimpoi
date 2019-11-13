package com.example.ccbim.ccbimpoi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ccbim.ccbimpoi.R;
import com.example.ccbim.ccbimpoi.data.ExcelData;
import com.example.ccbim.ccbimpoi.util.BaseUtil;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.TimeUtils;
import com.weqia.utils.ViewUtils;
import com.weqia.wq.component.db.WeqiaDbUtil;
import com.weqia.wq.data.global.WeqiaApplication;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgf on 2019/4/8.
 */

public class SearchExcelActivity extends BaseActivity implements View.OnClickListener{
    private TextView tvSure;
    private TextView tvReset;
    private TextView tvBegTime;
    private TextView tvOverTime;
    private String beginTime;
    private String endTime;
    private Spinner spSelect;
    private ArrayList<String> excelList = new ArrayList<>();
    private String selectExcelName = "";
    private boolean isFirst = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_excel);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        tvSure = (TextView) findViewById(R.id.tvSure);
        tvReset = (TextView) findViewById(R.id.tvReset);
        tvBegTime = (TextView) findViewById(R.id.tv_begin_time);
        tvOverTime = (TextView) findViewById(R.id.tv_over_time);
        spSelect = (Spinner) findViewById(R.id.sp_select_excel);
        initrRecordCycleView();
        initSpinner();
        ViewUtils.bindClickListenerOnViews(this, this, R.id.tvSure, R.id.tvReset);
    }

    private void initSpinner() {
        excelList.clear();
//        for (ExcelEnum excelEnum : ExcelEnum.values()) {
//            excelList.add(excelEnum.getStrName());
//        }
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        List<ExcelData> list = dbUtil.findAll(ExcelData.class);
        if (StrUtil.listNotNull(list)) {
            for (ExcelData excelData : list) {
                excelList.add(excelData.getExcelName());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, excelList);
        spSelect.setAdapter(adapter);

        spSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (isFirst) {
                    view.setVisibility(View.INVISIBLE);
                } else {
                    view.setVisibility(View.VISIBLE);
                    selectExcelName = excelList.get(i);
//                    L.toastShort("您点击了" + excelList.get(i));
                }
                isFirst = false;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        } );
        spSelect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    Class<?> clazz = AdapterView.class;
                    Field field = clazz.getDeclaredField("mOldSelectedPosition");
                    field.setAccessible(true);
                    field.setInt(spSelect, AdapterView.INVALID_POSITION);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

    }

    /**
     * 初始化日志筛选-统计周期
     */
    private void initrRecordCycleView() {
        tvBegTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseUtil.selectDateTimeDialog(SearchExcelActivity.this, StrUtil.notEmptyOrNull(beginTime) ? Long.parseLong(beginTime) : System.currentTimeMillis(), tvBegTime, "开始时间", true, null, null);
                BaseUtil.setmClickListener(new BaseUtil.TimeSelectDialogClickListener() {
                    @Override
                    public void timeSelectedListener(View view) {
                        if (view == tvBegTime) {
                            if (tvBegTime.getTag() != null) {
                                L.e("选择的开始时间：" + TimeUtils.getDateMDEFromLong((Long) tvBegTime.getTag()));
                                beginTime = tvBegTime.getTag() + "";
                                configCycleBtnBg(tvBegTime, true, R.id.iv_begin_pic);
                            } else {
                                configCycleBtnBg(tvBegTime, false, R.id.iv_begin_pic);
                            }
                        }
                    }
                });
            }
        });

        tvOverTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseUtil.selectDateTimeDialog(SearchExcelActivity.this, StrUtil.notEmptyOrNull(endTime) ? Long.parseLong(endTime) : System.currentTimeMillis(), tvOverTime, "结束时间", true, StrUtil.notEmptyOrNull(beginTime) ? Long.parseLong(beginTime) : null, "结束时间不能小于开始时间");
                BaseUtil.setmClickListener(new BaseUtil.TimeSelectDialogClickListener() {
                    @Override
                    public void timeSelectedListener(View view) {
                        if (view == tvOverTime) {
                            if (tvOverTime.getTag() != null) {
                                L.e("选择的结束时间：" + TimeUtils.getDateMDEFromLong((Long) tvOverTime.getTag()));
                                endTime = tvOverTime.getTag() + "";
                                endTime = (Long.parseLong(endTime) + 1000 * 60 * 60 * 24) + "";         //endTime默认是零点，所以要加一天
                                configCycleBtnBg(tvOverTime, true, R.id.iv_over_pic);
                            } else {
                                configCycleBtnBg(tvOverTime, false, R.id.iv_over_pic);
                            }
                        }
                    }
                });
            }
        });
    }
    public void configCycleBtnBg(TextView tvSelect, boolean isSelect, int ids) {
        if (isSelect) {
            ViewUtils.showViews(this, ids);
            tvSelect.setSelected(true);
            tvSelect.setTextColor(this.getResources().getColor(R.color.main_color));
        } else {
            ViewUtils.hideViews(this, ids);
            tvSelect.setSelected(false);
            tvSelect.setTextColor(this.getResources().getColor(R.color.black));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvReset:
                break;
            case R.id.tvSure:
                Intent intent = new Intent();
                intent.putExtra("selectExcelName", selectExcelName);
                intent.putExtra("beginTime", beginTime);
                intent.putExtra("endTime", endTime);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
