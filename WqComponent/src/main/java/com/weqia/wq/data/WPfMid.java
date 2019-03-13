package com.weqia.wq.data;

import com.weqia.data.StatedPerference;
import com.weqia.utils.L;
import com.weqia.utils.datastorage.sharedperference.SharedPerferenceBase;
import com.weqia.wq.data.global.WeqiaApplication;

public class WPfMid extends StatedPerference {

    private static WPfMid instance;

    public static WPfMid getInstance() {
        if (instance == null) {
            instance = new WPfMid();
        }
        return instance;
    }

    public static void setInstance(WPfMid instance) {
        if (debug_per) L.e("重置WPFMid--------------------------");
        WPfMid.instance = instance;
    }

    @Override
    public String getPerName() {
        String keyStr = "";
        LoginUserData loginUserData = WeqiaApplication.getInstance().getLoginUser();
        if (loginUserData != null) {
            keyStr = loginUserData.getMid();
        }
        return keyStr;
    }

    public static void removeAll() {
        if (instance != null)
            SharedPerferenceBase.clearAllKeyValues(instance.getSharedPreferences());
    }
}
