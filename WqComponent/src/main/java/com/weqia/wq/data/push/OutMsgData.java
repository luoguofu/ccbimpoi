package com.weqia.wq.data.push;

import com.weqia.wq.data.BaseData;

/**
 * @author Berwin
 * @version 1.0
 * @Description :
 * @created 2013-3-27 上午11:21:33
 * @fileName com.weqia.wq1.data.PushData.java
 */
public class OutMsgData extends BaseData {

    private static final long serialVersionUID = 1L;
    private String fromId;
    private String title;
    private String content;
    private String clickUrl;

    /**
     *2017年7月6日获取c=链接使用
     */
    private String plugNo;
    private String developerId;

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public String getPlugNo() {
        return plugNo;
    }

    public void setPlugNo(String plugNo) {
        this.plugNo = plugNo;
    }

    public String getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(String developerId) {
        this.developerId = developerId;
    }
}
