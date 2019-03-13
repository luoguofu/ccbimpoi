package com.weqia.wq.data;

import com.alibaba.fastjson.annotation.JSONField;


public class UpFileData extends BaseData {

    private static final long serialVersionUID = 1L;
    private String name;
    private String type;
    private String url;
    private String mime;
    private String id;
    private String fileSize;
    @JSONField(serialize = false)
    private
    float percent;
    private Integer playTime;


    public UpFileData() {

    }


    public UpFileData(String id, float percent) {
        super();
        this.id = id;
        this.percent = percent;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public Integer getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Integer playTime) {
        this.playTime = playTime;
    }
}
