package com.weqia.wq.component.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.weqia.wq.data.global.GlobalConstants;

public class AttachMsgReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        if (intent.getAction().equalsIgnoreCase(GlobalConstants.DOWNLOAD_COUNT_SERVICE_NAME)) {
            downloadCountReceived(intent);
        } else if (intent.getAction().equalsIgnoreCase(GlobalConstants.UPLOAD_COUNT_SERVICE_NAME)) {
            uploadCountReceived(intent);
        }
    }

    /**
     * 下载收到数据
     * @param intent
     */
    public void downloadCountReceived(Intent intent){};
    
    /**
     * 上传收到数据
     */
    public void uploadCountReceived(Intent intent){};
    
}
