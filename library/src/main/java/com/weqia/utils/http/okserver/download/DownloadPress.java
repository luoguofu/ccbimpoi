package com.weqia.utils.http.okserver.download;

import com.weqia.utils.http.okgo.callback.FileCallback;
import com.weqia.data.UtilData;

/**
 * Created by berwin on 2016/11/4.
 */

public class DownloadPress extends UtilData {

    private String taskKey;             //下载的标识键
    private String targetPath;          //保存文件地址
    private float progress;             //下载进度
    private long totalLength;           //总大小
    private long downloadLength;        //已下载大小
    private long networkSpeed;          //下载速度
    private int state = 0;              //当前状态
    private FileCallback listener;  //当前下载任务的监听
    private String errorMsg;
    private Exception e;

    public DownloadPress() {
    }

    public DownloadPress(String taskKey, String targetPath, float progress,
                          long totalLength, long downloadLength,
                          long networkSpeed, int state, FileCallback listener) {
        this.taskKey = taskKey;
        this.targetPath = targetPath;
        this.progress = progress;
        this.totalLength = totalLength;
        this.downloadLength = downloadLength;
        this.networkSpeed = networkSpeed;
        this.state = state;
        this.listener = listener;
    }


    public DownloadPress initData(String taskKey, String targetPath, float progress,
                         long totalLength, long downloadLength,
                         long networkSpeed, int state, FileCallback listener) {
        this.taskKey = taskKey;
        this.targetPath = targetPath;
        this.progress = progress;
        this.totalLength = totalLength;
        this.downloadLength = downloadLength;
        this.networkSpeed = networkSpeed;
        this.state = state;
        this.listener = listener;
        return this;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public long getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(long totalLength) {
        this.totalLength = totalLength;
    }

    public long getDownloadLength() {
        return downloadLength;
    }

    public void setDownloadLength(long downloadLength) {
        this.downloadLength = downloadLength;
    }

    public long getNetworkSpeed() {
        return networkSpeed;
    }

    public void setNetworkSpeed(long networkSpeed) {
        this.networkSpeed = networkSpeed;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public FileCallback getListener() {
        return listener;
    }

    public void setListener(FileCallback listener) {
        this.listener = listener;
    }

    public String getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {
        this.e = e;
    }
}
