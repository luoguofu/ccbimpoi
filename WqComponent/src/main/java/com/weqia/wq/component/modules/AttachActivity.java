package com.weqia.wq.component.modules;

import android.content.IntentFilter;
import android.os.Bundle;

import com.weqia.utils.StrUtil;
import com.weqia.wq.R;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;
import com.weqia.wq.component.utils.AttachUtils;
import com.weqia.wq.data.AttachmentData;
import com.weqia.wq.data.BaseData;
import com.weqia.wq.data.global.GlobalConstants;

//附件
public class AttachActivity extends SharedDetailTitleActivity {

    private AttachUtils attachUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach);
        sharedTitleView.initTopBanner("下载列表");
        attachUtils = new AttachUtils(this, null);

        if (getIntent().getExtras() != null) {
            String attach = getIntent().getExtras().getString(GlobalConstants.KEY_ATTACH);
            if (StrUtil.notEmptyOrNull(attach)) {
                attachUtils.setAttachDatas(BaseData.fromList(AttachmentData.class, attach));
            }
        }
        attachUtils.initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (attachUtils != null) {
            attachUtils.onResume();
            if (attachUtils.getDownReceive() != null) {
                IntentFilter filter = new IntentFilter();
                filter.addAction(GlobalConstants.DOWNLOAD_COUNT_SERVICE_NAME);
                filter.setPriority(Integer.MAX_VALUE);
                registerReceiver(attachUtils.getDownReceive(), filter);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (attachUtils != null && attachUtils.getDownReceive() != null)
            unregisterReceiver(attachUtils.getDownReceive());
    }
}
