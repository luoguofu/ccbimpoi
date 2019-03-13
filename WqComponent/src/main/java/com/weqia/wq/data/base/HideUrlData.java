package com.weqia.wq.data.base;

import java.util.ArrayList;

import com.weqia.wq.data.BaseData;

public class HideUrlData extends BaseData {

    private static final long serialVersionUID = 1L;

    private ArrayList<String > urls;
    private String ret;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public ArrayList<String> getUrls() {
        return urls;
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }
}
