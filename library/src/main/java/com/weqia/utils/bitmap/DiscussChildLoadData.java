package com.weqia.utils.bitmap;

import com.weqia.data.UtilData;
import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;

/**
 * Created by berwin on 2016/11/17.
 */

@Table(name = "discuss_child_load")
public class DiscussChildLoadData extends UtilData {

    private static final long serialVersionUID = 1L;

    private @Id String singleImg;
    private String discussKey;
    private long loadTime = System.currentTimeMillis();

    public DiscussChildLoadData(){

    }

    public DiscussChildLoadData(String singleImg, String discussKey) {
        this.singleImg = singleImg;
        this.discussKey = discussKey;
    }

    public String getDiscussKey() {
        return discussKey;
    }

    public void setDiscussKey(String discussKey) {
        this.discussKey = discussKey;
    }

    public String getSingleImg() {
        return singleImg;
    }

    public void setSingleImg(String singleImg) {
        this.singleImg = singleImg;
    }

    public long getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(long loadTime) {
        this.loadTime = loadTime;
    }
}
