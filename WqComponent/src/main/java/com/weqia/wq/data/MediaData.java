package com.weqia.wq.data;

import android.graphics.Point;
import android.net.Uri;

import com.weqia.utils.StrUtil;
import com.weqia.utils.datastorage.file.FileUtil;

import java.io.File;

public class MediaData extends BaseData {


    private static final long serialVersionUID = 1L;

    private String name;
    private String path;
    private String mime;
    private long duration;
    private long size;
    private Uri fileUri;
    private Point widthHeight;
    private boolean wantRotaion = false;
    private float videoScale = 1.0f;    //宽高比
    
    private Integer fileIType; //发送文件用的itype
    private int contentType;

//    private MyLocData locData;
    private Integer playTime;   //播放时间，视频
    private String fiUrls; //已经发送成功的url
    private String content; //发送的文字内容
    private String locContent; //位置信息的文字
    
    // berwin add,做转发用
    private String sizeStr;
    private String nameStr;
    

    public MediaData(String path) {
        this.path = path;
    }


    public MediaData(String name, String path, String mime, long duration, long size) {
        this.name = name;
        this.path = path;
        this.mime = mime;
        this.duration = duration;
        this.size = size;
    }

    public MediaData() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Uri getFileUri() {
        return fileUri;
    }

    public void setFileUri(Uri fileUri) {
        this.fileUri = fileUri;
    }

    public Point getWidthHeight() {
        return widthHeight;
    }

    public void setWidthHeight(Point widthHeight) {
        this.widthHeight = widthHeight;
    }

    public boolean isWantRotaion() {
        return wantRotaion;
    }

    public void setWantRotaion(boolean wantRotaion) {
        this.wantRotaion = wantRotaion;
    }

    public float getVideoScale() {
        return videoScale;
    }

    public void setVideoScale(float videoScale) {
        this.videoScale = videoScale;
    }

    public String getSizeStr() {
        if (StrUtil.isEmptyOrNull(sizeStr)) {
            if (StrUtil.notEmptyOrNull(path)) {
                sizeStr = FileUtil.getAutoFileOrFilesSize(path);
            }
        }
        return sizeStr;
    }

    public void setSizeStr(String sizeStr) {
        this.sizeStr = sizeStr;
    }


    public String getNameStr() {
        if (StrUtil.isEmptyOrNull(nameStr)) {
            if (StrUtil.notEmptyOrNull(path)) {
                nameStr = new File(path).getName();
            }
        }
        return nameStr;
    }


    public void setNameStr(String nameStr) {
        this.nameStr = nameStr;
    }


    public Integer getFileIType() {
        return fileIType;
    }


    public void setFileIType(Integer fileIType) {
        this.fileIType = fileIType;
    }


    public int getContentType() {
        return contentType;
    }


    public void setContentType(int contentType) {
        this.contentType = contentType;
    }


//    public MyLocData getLocData() {
//        return locData;
//    }
//
//
//    public void setLocData(MyLocData locData) {
//        this.locData = locData;
//    }


    public Integer getPlayTime() {
        return playTime;
    }


    public void setPlayTime(Integer playtime) {
        this.playTime = playtime;
    }


    public String getFiUrls() {
        return fiUrls;
    }


    public void setFiUrls(String fiUrls) {
        this.fiUrls = fiUrls;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public String getLocContent() {
        return locContent;
    }


    public void setLocContent(String locContent) {
        this.locContent = locContent;
    }
}
