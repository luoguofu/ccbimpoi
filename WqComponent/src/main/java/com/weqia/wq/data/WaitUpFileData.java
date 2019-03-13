package com.weqia.wq.data;

import com.weqia.utils.StrUtil;
import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;
import com.weqia.wq.data.EnumData.DataStatusEnum;

@Table(name = "wait_upfile")
public class WaitUpFileData extends BaseData {

    private static final long serialVersionUID = 1L;

    private
    @Id
    Integer id;
    private Integer sendId;// 发送ID
    private Integer fileId;// 文件成功ID
    private String name;
    private Integer type;// 文件分类：1-图片 2-语音 3-视频 4-其他文件
    //    NONE("0", "无附件"),
//    PICTURE("1", "图片"), VOICE("2", "语音"), VIDEO("3", "视频"), FILE("4", "其他文件");
    private String path;
    private String upfile;// 文件[upfile]
    private String mime;
    private Integer sendStatus;// 0待发,1发送成功,2发送失败
    private Integer business_id;
    private String fileUri;
    private Integer playTime;
    private String operateId;
    private String fileMd;

    public String getFileMd() {
        return fileMd;
    }

    public void setFileMd(String fileMd) {
        this.fileMd = fileMd;
    }

    public Integer getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Integer business_id) {
        this.business_id = business_id;
    }

    public WaitUpFileData() {
    }

    public WaitUpFileData(int sendId, String path, Integer type) {
        super();
        this.sendId = sendId;
        this.path = path;
        this.name = StrUtil.getFileNameByPath(path);
        this.sendStatus = DataStatusEnum.SENDIND.value();
        this.type = type;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getUpfile() {
        return upfile;
    }

    public void setUpfile(String upfile) {
        this.upfile = upfile;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSendId() {
        return sendId;
    }

    public void setSendId(Integer sendId) {
        this.sendId = sendId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer status) {
        this.sendStatus = status;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    public Integer getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Integer playTime) {
        this.playTime = playTime;
    }

    public String getOperateId() {
        return operateId;
    }

    public void setOperateId(String operateId) {
        this.operateId = operateId;
    }
}
