/**
 * ImageGroup.java ImageChooser
 * 
 * Created by likebamboo on 2014-4-22 Copyright (c) 1998-2014 http://likebamboo.github.io/ All
 * rights reserved.
 */

package com.weqia.utils.imageselect;

import java.io.Serializable;

import android.net.Uri;


/**
 * 一级GridView中每个item的数据模型
 * 
 * @author likebamboo
 */
public class ImageEntity implements Comparable<ImageEntity>, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String path;
    private long lastModify;
    private Uri localUri;
    private long duration;
    
    public ImageEntity() {
    }

    public ImageEntity(String path, long lastModify, Uri localUri) {
        super();
        this.path = path;
        this.lastModify = lastModify;
        this.localUri = localUri;
    }

    public long getLastModify() {
        return lastModify;
    }

    public void setLastModify(long lastModify) {
        this.lastModify = lastModify;
    }

    @Override
    public int compareTo(ImageEntity another) {
        return (this.getLastModify() == another.getLastModify()
                ? 0
                : (this.getLastModify() > another.getLastModify() ? -1 : 1));
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Uri getLocalUri() {
        return localUri;
    }

    public void setLocalUri(Uri localUri) {
        this.localUri = localUri;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration2) {
        this.duration = duration2;
    }
}
