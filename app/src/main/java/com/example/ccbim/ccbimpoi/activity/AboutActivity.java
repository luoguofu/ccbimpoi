package com.example.ccbim.ccbimpoi.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.ccbim.ccbimpoi.R;
import com.weqia.utils.DeviceUtil;

/**
 * Created by lgf on 2019/4/20.
 */

public class AboutActivity extends BaseActivity {
    private TextView appVersion, resVersion;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        appVersion = (TextView) findViewById(R.id.tv_app_vercode);
        resVersion = (TextView) findViewById(R.id.tv_resource_version);
        appVersion.setText(DeviceUtil.getVersionName(this));
        SharedPreferences sharedPreferences = getSharedPreferences("excelCode", Context.MODE_PRIVATE);
        resVersion.setText(sharedPreferences.getString("resourceCode", ""));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
