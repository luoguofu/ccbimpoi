package com.weqia.utils.http.okserver.download;

import com.weqia.data.UtilData;
import com.weqia.utils.L;
import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;
import com.weqia.utils.annotation.sqlite.Transient;
import com.weqia.utils.http.okgo.callback.FileCallback;
import com.weqia.utils.http.okgo.request.BaseRequest;
import com.weqia.utils.http.okgo.utils.OkLogger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@Table(name = "util_download_info")
public class DownloadInfo extends UtilData implements Comparable<DownloadInfo> {

    private @Id String taskKey;             //下载的标识键
    private String url;                 //文件URL
    private String targetFolder;        //保存文件夹
    private String targetPath;          //保存文件地址
    private String fileName;            //保存的文件名
    private float progress;             //下载进度
    private long totalLength;           //总大小
    private long downloadLength;        //已下载大小
    private long networkSpeed;          //下载速度
    private int state = 0;              //当前状态
    private @Transient BaseRequest request;        //当前任务的网络请求
    private byte[] requestArr;

    private DownloadTask task;          //执行当前下载的任务
    private FileCallback listener;  //当前下载任务的监听

    public BaseRequest getRequest() {
        if (request == null && this.requestArr != null) {
            L.e("+++++++++++++++++++++++++++得到downloadRequest");
            ByteArrayInputStream bais = null;
            ObjectInputStream ois = null;
            try {
                if (this.requestArr != null) {
                    bais = new ByteArrayInputStream(this.requestArr);
                    ois = new ObjectInputStream(bais);
                    DownloadRequest downloadRequest = (DownloadRequest) ois.readObject();
                    BaseRequest request = DownloadRequest.createRequest(downloadRequest.url, downloadRequest.method);
                    if (request != null) {
                        request.cacheMode(downloadRequest.cacheMode);
                        request.cacheTime(downloadRequest.cacheTime);
                        request.cacheKey(downloadRequest.cacheKey);
                        request.params(downloadRequest.params);
                        request.headers(downloadRequest.headers);
                        this.request = request;
                    }
                }
            } catch (Exception e) {
                OkLogger.e(e);
            } finally {
                try {
                    if (ois != null) ois.close();
                    if (bais != null) bais.close();
                } catch (IOException e) {
                    OkLogger.e(e);
                }
            }
        }
        return request;
    }

    public void setRequest(BaseRequest trequest) {
        this.request = trequest;
        L.e("---------------------------设置downloadRequest");
        DownloadRequest downloadRequest = new DownloadRequest();
        downloadRequest.cacheKey = request.getCacheKey();
        downloadRequest.cacheTime = request.getCacheTime();
        downloadRequest.cacheMode = request.getCacheMode();
        downloadRequest.url = request.getBaseUrl();
        downloadRequest.params = request.getParams();
        downloadRequest.headers = request.getHeaders();
        downloadRequest.method = DownloadRequest.getMethod(request);

        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(downloadRequest);
            oos.flush();
            byte[] requestData = baos.toByteArray();
            this.requestArr = requestData;
        } catch (IOException e) {
            OkLogger.e(e);
        } finally {
            try {
                if (oos != null) oos.close();
                if (baos != null) baos.close();
            } catch (IOException e) {
                OkLogger.e(e);
            }
        }
    }

    public String getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getTargetFolder() {
        return targetFolder;
    }

    public void setTargetFolder(String targetFolder) {
        this.targetFolder = targetFolder;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public DownloadTask getTask() {
        return task;
    }

    public void setTask(DownloadTask task) {
        this.task = task;
    }

    public FileCallback getListener() {
        return listener;
    }

    public void setListener(FileCallback listener) {
        this.listener = listener;
    }

    public void removeListener() {
        listener = null;
    }

    public byte[] getRequestArr() {
        return requestArr;
    }

    public void setRequestArr(byte[] requestArr) {
        this.requestArr = requestArr;
    }

    /** taskKey 相同就认为是同一个任务 */
    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof DownloadInfo) {
            DownloadInfo info = (DownloadInfo) o;
            return getTaskKey().equals(info.getTaskKey());
        }
        return false;
    }

    /** 两个任务排序按照 id 的大小排序 */
    @Override
    public int compareTo(DownloadInfo another) {
        if (another == null) return 0;
        return (getTaskKey()).compareTo(another.getTaskKey());
    }
}