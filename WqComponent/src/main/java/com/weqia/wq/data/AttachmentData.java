package com.weqia.wq.data;

import com.alibaba.fastjson.annotation.JSONField;
import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;

@Table(name = "attachment_data")
public class AttachmentData extends BaseData {

    /**
     * @Description 从服务器下来的文件数据
     * @author
     * @create at 2013-4-7 上午9:28:38
     * 文件分类：1-图片 2-语音 3-视频 4-其他文件
     */

    private static final long serialVersionUID = 1L;

    private String id;
    protected String name;
    protected String modeName;    //模型的名称，在模型打开的时候会用到
    /**
     * 操作人
     */
    private String mname;
    protected
    @Id
    String url;
    private String mime;
    protected int type;
    private String fileSize;
    @JSONField(name = "cdate")
    private String createDate;
    private String mid;
    private String loaclUrl;
    private String percentStr;
    private String task_id;
    @JSONField(name = "tfId")
    private String tf_id;
    //用来判断是不是下载模型的标识
    private String pjId;
    private String project_id;
    private Integer playTime;
    private String pathRoot;
    @JSONField(name = "picRadio")
    private float picScale = 1.0f;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    private String nodeId;

    private String versionId;      //版本id
    private String convertTime;      //转换版本id
    private String realUrl;


//    private String picUri;

    /**
     * 视频预览图
     */
    @JSONField(name = "preFileUrl")
    private String videoPrew;
    //    private Uri videoUri;
// 下载类型
    private
    Integer downloadType = EnumData.DownloadType.WEQIA.value().intValue();
    //自动下载
    private
    Boolean autoDownload = false;

    private boolean bCanAction = true;  //是否可以操作文件


    public AttachmentData() {
    }

    public String getPjId() {
        return pjId;
    }

    public void setPjId(String pjId) {
        this.pjId = pjId;
    }

    public AttachmentData(String url) {
        this.url = url;
    }


    public AttachmentData(String loaclUrl, String url) {
        this.loaclUrl = loaclUrl;
        this.url = url;
    }

    public AttachmentData(String loaclUrl, String name, String fileSize) {
        this.loaclUrl = loaclUrl;
        this.name = name;
        this.fileSize = fileSize;
    }

    public AttachmentData(String name, int type, String fileSize, String loaclUrl, Integer playTime) {
        this.name = name;
        this.type = type;
        this.fileSize = fileSize;
        this.loaclUrl = loaclUrl;
        this.playTime = playTime;
    }

    public AttachmentData(String id, String name, int type, String fileSize, String url) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.fileSize = fileSize;
        this.url = url;
    }

    public AttachmentData(String id, String name, int type, String url) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.url = url;
    }

    public AttachmentData(String id, String name, String url, String mime, int type, String fileSize, String loaclUrl) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.mime = mime;
        this.type = type;
        this.fileSize = fileSize;
        this.loaclUrl = loaclUrl;
    }


    public AttachmentData(String loaclUrl, Integer playTime) {
        this.loaclUrl = loaclUrl;
        this.playTime = playTime;
    }

    public AttachmentData(String url, int type) {
        this.url = url;
        this.type = type;
    }

    public boolean isbCanAction() {
        return bCanAction;
    }

    public void setbCanAction(boolean bCanAction) {
        this.bCanAction = bCanAction;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getLoaclUrl() {
        return loaclUrl;
    }

    public void setLoaclUrl(String loaclUrl) {
        this.loaclUrl = loaclUrl;
    }

    public String getPercentStr() {
        return percentStr;
    }

    public void setPercentStr(String percentStr) {
        this.percentStr = percentStr;
    }


    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTf_id() {
        return tf_id;
    }

    public void setTf_id(String tf_id) {
        this.tf_id = tf_id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public Integer getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Integer playTime) {
        this.playTime = playTime;
    }


    public String getPathRoot() {
        return pathRoot;
    }

    public void setPathRoot(String pathRoot) {
        this.pathRoot = pathRoot;
    }


    public float getPicScale() {
        return picScale;
    }


    public void setPicScale(float picScale) {
        this.picScale = picScale;
    }


    public String getVideoPrew() {
        return videoPrew;
    }


    public void setVideoPrew(String videoPrew) {
        this.videoPrew = videoPrew;
    }

//    public String getPicUri() {
//        return picUri;
//    }
//
//    public void setPicUri(String picUri) {
//        this.picUri = picUri;
//    }

//    public Uri getVideoUri() {
//        return videoUri;
//    }
//
//
//    public void setVideoUri(Uri videoUri) {
//        this.videoUri = videoUri;
//    }


    public Boolean getAutoDownload() {
        return autoDownload;
    }

    public void setAutoDownload(Boolean autoDownload) {
        this.autoDownload = autoDownload;
    }

    public Integer getDownloadType() {
        return downloadType;
    }

    public void setDownloadType(Integer downloadType) {
        this.downloadType = downloadType;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getConvertTime() {
        return convertTime;
    }

    public void setConvertTime(String convertTime) {
        this.convertTime = convertTime;
    }

    public String getRealUrl() {
        return realUrl;
    }

    public void setRealUrl(String realUrl) {
        this.realUrl = realUrl;
    }
}
