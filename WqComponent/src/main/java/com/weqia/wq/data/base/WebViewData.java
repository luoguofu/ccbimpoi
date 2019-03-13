package com.weqia.wq.data.base;

import com.weqia.wq.data.BaseData;

public class WebViewData extends BaseData {

    private static final long serialVersionUID = 1L;

    private String title;
    private String url;

    public WebViewData(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
