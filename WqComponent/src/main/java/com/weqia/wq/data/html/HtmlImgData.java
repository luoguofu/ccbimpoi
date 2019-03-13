package com.weqia.wq.data.html;

import com.weqia.wq.data.BaseData;

public class HtmlImgData extends BaseData {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private Integer width;
    private Integer height;
    private String url;
    private String alt;
    
    public HtmlImgData() {
    }
    
    public HtmlImgData(Integer width, Integer height, String url, String alt) {
        super();
        this.width = width;
        this.height = height;
        this.url = url;
        this.alt = alt;
    }



    public Integer getWidth() {
        return width;
    }
    public void setWidth(Integer width) {
        this.width = width;
    }
    public Integer getHeight() {
        return height;
    }
    public void setHeight(Integer height) {
        this.height = height;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }
    
}
