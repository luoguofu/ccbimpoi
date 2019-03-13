package com.weqia.wq.component;

import android.app.Activity;

import com.jrmf360.rplib.JrmfRpClient;
import com.jrmf360.rplib.http.model.BaseModel;
import com.jrmf360.tools.http.OkHttpModelCallBack;

public class HbUpdateUserInfo {

    public static void updateInfo(Activity ctx, String uid, String uName, String uIcon) {
        JrmfRpClient.updateUserInfo(ctx, uid, PaymentUtil.PAYMENT_THIRD_TOKEN,
                uName, uIcon, new OkHttpModelCallBack<BaseModel>() {
                    @Override
                    public void onSuccess(BaseModel baseModel) {

                    }

                    @Override
                    public void onFail(String result) {

                    }
                });
    }

}