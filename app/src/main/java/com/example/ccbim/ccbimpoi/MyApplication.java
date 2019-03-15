package com.example.ccbim.ccbimpoi;

import com.weqia.wq.data.global.WeqiaApplication;

/**
 * Created by lgf on 2019/3/14.
 */

public class MyApplication extends WeqiaApplication {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void initializeAllProcessRouter() {

    }

    @Override
    protected void initializeLogic() {

    }

    @Override
    public boolean needMultipleProcess() {
        return false;
    }
}
