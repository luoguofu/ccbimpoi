package com.weqia.wq.data;

import com.weqia.data.StatedPerference;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.datastorage.sharedperference.SharedPerferenceBase;
import com.weqia.wq.data.global.WeqiaApplication;

public class WPf extends StatedPerference {

    private static WPf instance;

    public static WPf getInstance() {
        if (instance == null) {
            instance = new WPf();
        }
        return instance;
    }
    
    public static void setInstance(WPf instance) {
        if (debug_per) L.e("重置WPF--------------------------");
        WPf.instance = instance;
    }

    @Override
    public String getPerName() {
        String keyStr = "";
        LoginUserData loginUserData = WeqiaApplication.getInstance().getLoginUser();
        if (loginUserData != null) {
            if (StrUtil.notEmptyOrNull(WeqiaApplication.getgMCoId())) {
                keyStr = loginUserData.getMid() + WeqiaApplication.getgMCoId();
            } else {
                keyStr = loginUserData.getMid();
            }
        }
        return keyStr;
    }
    
    public static void removeAll() {
        SharedPerferenceBase.clearAllKeyValues(instance.getSharedPreferences());
    }
}
