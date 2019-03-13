package com.weqia.wq.data;

/**
 * Created by berwin on 15/8/9.
 */
public class ZanData extends BaseData {

    private static final long serialVersionUID = 1L;

    private String mid;
    private String mName;

    public ZanData() {
    }

    public ZanData(String mid, String mName) {
        this.mid = mid;
        this.mName = mName;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
}
