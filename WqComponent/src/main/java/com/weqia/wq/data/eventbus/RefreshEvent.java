package com.weqia.wq.data.eventbus;

import android.content.Intent;
import android.os.Bundle;

import com.weqia.wq.data.BaseData;

/**
 * Created by MX on 2014/8/21.
 */
public class RefreshEvent {

    public int type;
    public String key = "";
    public Integer punchType;
    public Intent intent;
    public Bundle bundle;
    public BaseData baseData;
    public Object obj;

    public RefreshEvent() {
    }


    public RefreshEvent(String key, Object obj) {
        this.key = key;
        this.obj = obj;
    }

    public RefreshEvent(int type, Object obj) {
        this.type = type;
        this.obj = obj;
    }

//    public RefreshEvent(int type, BaseData baseData) {
//        this.type = type;
//        this.baseData = baseData;
//    }

    public RefreshEvent(String key) {
        this.key = key;
    }

    public RefreshEvent(int type) {
        this.type = type;
    }

    public RefreshEvent(int type, Integer punchType) {
        this.type = type;
        this.punchType = punchType;
    }

    public RefreshEvent(int type, Integer punchType, Intent intent) {
        this.type = type;
        this.punchType = punchType;
        this.intent = intent;
    }

    public RefreshEvent(int type, Bundle bundle) {
        this.type = type;
        this.bundle = bundle;
    }
}
