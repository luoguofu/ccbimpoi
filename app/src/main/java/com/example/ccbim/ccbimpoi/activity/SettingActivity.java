package com.example.ccbim.ccbimpoi.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.ccbim.ccbimpoi.R;
import com.weqia.utils.StrUtil;
import com.weqia.utils.ViewUtils;


/**
 * Created by lgf on 2019/4/9.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener{
    private EditText etCompanyName, etProjectName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("设置");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        etCompanyName = (EditText) findViewById(R.id.et_companyname);
        etProjectName = (EditText) findViewById(R.id.et_projectname);
        ViewUtils.bindClickListenerOnViews(this, this, R.id.tv_add_batch, R.id.tv_exported_excel, R.id.tv_excel_manage, R.id.tv_recycle, R.id.tv_about);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_batch:
                back();
                startActivity(new Intent(this, BatchAddActivity.class));
                break;
            case R.id.tv_exported_excel:
                startActivity(new Intent(this, ExportedActivity.class));
                break;
            case R.id.tv_excel_manage:
                break;
            case R.id.tv_recycle:
                break;
            case R.id.tv_about:
                break;

        }
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

    @Override
    public void onBackPressed() {
        back();
        super.onBackPressed();
    }

    private void back() {
        if (StrUtil.isEmptyOrNull(etCompanyName.getText().toString()) && StrUtil.isEmptyOrNull(etProjectName.getText().toString())) {
            return;
        }
        SharedPreferences sharedPreferences= getSharedPreferences("setting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("companyName", etCompanyName.getText().toString());
        editor.putString("projectName", etProjectName.getText().toString());
        editor.apply();
    }
}
