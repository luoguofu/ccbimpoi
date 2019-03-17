package com.example.ccbim.ccbimpoi;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.weqia.wq.data.global.WeqiaApplication;

/**
 * Created by lgf on 2019/3/14.
 */

public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
