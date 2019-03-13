package com.weqia.wq.data.html;

import com.alibaba.fastjson.annotation.JSONField;
import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;
import com.weqia.wq.data.BaseData;

@Table(name = "link_data")
public class LinksData extends BaseData {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String image;
    private String title;
    private String content;
    private String rimage;
    private String closeshare;
    private @Id String url;
    @JSONField(deserialize=false)
    private transient long cDate = System.currentTimeMillis();
    
    
    public LinksData() {
    }
    
    public LinksData(String image, String title, String content, String url) {
        super();
        this.image = image;
        this.title = title;
        this.content = content;
        this.url = url;
    }



    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
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
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public long getcDate() {
        return cDate;
    }

    public void setcDate(long cDate) {
        this.cDate = cDate;
    }

    public String getRimage() {
        return rimage;
    }

    public void setRimage(String rimage) {
        this.rimage = rimage;
    }

    public String getCloseshare() {
        return closeshare;
    }

    public void setCloseshare(String closeshare) {
        this.closeshare = closeshare;
    }
}
