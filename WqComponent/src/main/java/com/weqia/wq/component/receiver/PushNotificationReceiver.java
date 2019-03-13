package com.weqia.wq.component.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.data.push.PushData;

public abstract class PushNotificationReceiver extends BroadcastReceiver {  //广播！！！！！！！！！

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            receivePushNotification(null);
            return;
        }
        PushData datas =
                (PushData) intent.getSerializableExtra(GlobalConstants.PUSH_CONTENT_KEY);  //接收到刷新广播时获得刷新的数据
        receivePushNotification(datas);
    }

    public abstract void receivePushNotification(PushData datas);  //抽象方法~

}
