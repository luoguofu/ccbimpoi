package com.weqia.wq.data;

import com.alibaba.fastjson.annotation.JSONField;
import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;
import com.weqia.utils.annotation.sqlite.Transient;
import com.weqia.wq.data.EnumData.DataStatusEnum;

@Table(name = "up_attach_data")
public class UpAttachData extends BaseData {

    private static final long serialVersionUID = 1L;

    private
    @Id
    Integer globalId;
    private int bid;//业务ID
    private int itype;//业务type
    private String name;
    private String mime;
    private Long fileSize;
    private Long createTime;
    private String path;
    private int sendStatus;//上传中0,上传失败1,上传成功2
    @JSONField(serialize = false)
    private
    @Transient
    Integer progress;


    public UpAttachData(int bid, int itype, String name, String mime, Long fileSize, String path) {
        this.bid = bid;
        this.itype = itype;
        this.name = name;
        this.mime = mime;
        this.fileSize = fileSize;
        this.path = path;
        this.createTime = System.currentTimeMillis();
        this.sendStatus = DataStatusEnum.SENDIND.value();
    }

    public UpAttachData() {

    }


    public Integer getGlobalId() {
        return globalId;
    }

    public void setGlobalId(Integer globalId) {
        this.globalId = globalId;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getItype() {
        return itype;
    }

    public void setItype(int itype) {
        this.itype = itype;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(int status) {
        this.sendStatus = status;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }
}
