package com.weqia.utils;
import com.weqia.data.UtilData;

/**
 * Created by MX on 2014/8/21.
 */
public class RefreshObjEvent {


    public int type;
    public UtilData obj;

    public RefreshObjEvent() {
    }

    public RefreshObjEvent(int type, UtilData obj) {
        this.type = type;
        this.obj = obj;
    }

    public UtilData getObj() {
        return obj;
    }
}
