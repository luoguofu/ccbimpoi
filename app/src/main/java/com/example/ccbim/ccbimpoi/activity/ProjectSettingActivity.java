package com.example.ccbim.ccbimpoi.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.ccbim.ccbimpoi.R;

public class ProjectSettingActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_setting_activity_layout);
        setTitle(R.string.project_setting);
    }
}
