package com.weqia.wq.component.modules;

import android.os.Bundle;

import com.weqia.utils.StrUtil;
import com.weqia.wq.R;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;
import com.weqia.wq.component.modules.assist.SimpleInfoFragement;

//单页展示详情
public class SimpleInfoActivity extends SharedDetailTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ft);
        String title = getIntent().getExtras().getString("title");
        if (StrUtil.notEmptyOrNull(title)) {
            sharedTitleView.initTopBanner(title);
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new SimpleInfoFragement())
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

}
