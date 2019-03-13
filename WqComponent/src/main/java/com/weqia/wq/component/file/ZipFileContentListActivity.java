package com.weqia.wq.component.file;

import android.os.Bundle;
import android.view.View;

import com.weqia.wq.R;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;
import com.weqia.wq.component.file.assist.ZipContentFt;

/**
 * Created by 20161005 on 2017/6/30.
 */

public class ZipFileContentListActivity extends SharedDetailTitleActivity {

    private ZipContentFt zipFt;
    private String zipPath;
    private String zipName;
    private Integer nodeType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ft);
//        zipName = getCoIdParam();
        zipPath = getIntent().getStringExtra("filePath");
        zipName = getIntent().getStringExtra("title");
        nodeType = getIntent().getIntExtra("nodeType", 0);
//        String nodeId = ;
//        String portInfo = ;
//        String pjId = ;
//        int canAction = ;
//        String tmpTitle = zipPath;
//        if (StrUtil.notEmptyOrNull(zipPath) && zipPath.contains("/")) {
//            try {
//                tmpTitle = zipPath.substring(zipPath.lastIndexOf("/") + 1);
//            } catch (StringIndexOutOfBoundsException e) {
//                e.printStackTrace();
//            }
//        }
        sharedTitleView.initTopBanner(zipName);
        zipFt = new ZipContentFt();
        Bundle bundle = new Bundle();
        bundle.putString("zipPath", zipPath);
        bundle.putString("nodeId", getIntent().getStringExtra("nodeId"));
        bundle.putString("portInfo", getIntent().getStringExtra("portInfo"));
        bundle.putString("pjId", getIntent().getStringExtra("pjId"));
        bundle.putInt("nodeType", nodeType);
        bundle.putInt("bCanAction", getIntent().getIntExtra("bCanAction", 1));
        zipFt.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.container, zipFt).commit();
    }

    @Override
    protected void onClickDo(View v) {
        if (v == sharedTitleView.getButtonLeft()) {
            zipFt.goBack();
        }
    }

    @Override
    public void onBackPressed() {
        zipFt.goBack();
    }

    public String getZipName() {
        return zipName;
    }
}
