package com.weqia.wq.data;

import com.alibaba.fastjson.annotation.JSONField;

/**
 *
 *  "port": {                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
4              "mpId": 43,                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
5              "nodeId": "a7f2011b-8c59-8ff8-08bf-be8b334d95a9",                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
7              "floorName": "1",                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
8              "name": "",                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
9              "type": 1,                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
.850              "info": "1,0.143449,-0.171943,0.558134,6.484412,6.484412,-9.278697,-5.103284,-11.743459,-0.520728,0.893878,0.511208,-0.353011,0.914970,-0.195482",                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
.850              "photo": {                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
.851                  "imgTransform": null,                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
 .852                  "id": 962,                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
 .852                  "name": "pos.jpg",                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
 .853                  "type": 1,                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
 .853                  "url": "10000\/402882f35e93e603015e944379c60025.jpg",                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
 .854                  "mime": "image\/png",                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
 .855                  "createDate": 1505726006000,                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
 .856                  "mid": "402880825b622bed015b622d071001e6",                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
 .856                  "fileSize": 89.52,                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
 .857                  "picRadio": 0.58                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
.858              },                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
.858              "versionId": "a7f2011b-8c5b-6f5f-cfec-6e6ac24c8ec7",                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
.859              "bucket": "pmbimcloud-test1",                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
.860              "key": "402880825e93f6b4015e94427f750049.pbim",                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
.861              "accountType": 2,                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
.861              "cId": "焦明鑫",                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
.862              "cDate": 1505726008000                                                             at com.weqia.utils.LHelper.log2(LHelper.java:76)
.862          }, 
 */
public class PortData extends BaseData {
    private static final long serialVersionUID = 1L;

    private String mpId;
    private String nodeId;
    private String floorName;
    @JSONField(name = "queryFloorId")
    private int floorId;
    private String name;
    private String type;
    private String info;
    private String viewInfo;
    private String photo;
    private String versionId;
    private String bucket;
    private String key;
    private Integer accountType;
    private String cId;
    private String cDate;
    private Integer fileSize;
    private String fileName;
    private String orderId;
    private String pjId;
    /**
     *这里面有后缀，否则第一次下载模型打不开
     */
    private String modelName;

    private boolean isQr;   //判断是否通过构建二维码扫描进入模型，是就高亮显示否则就红点标记

    public String getModelName() {
        return modelName;
    }
    public String getViewInfo() {
        return viewInfo;
    }

    public void setViewInfo(String viewInfo) {
        this.viewInfo = viewInfo;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getMpId() {
        return mpId;
    }

    public void setMpId(String mpId) {
        this.mpId = mpId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getcDate() {
        return cDate;
    }

    public void setcDate(String cDate) {
        this.cDate = cDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }

    public boolean isQr() {
        return isQr;
    }

    public void setQr(boolean qr) {
        isQr = qr;
    }

    public String getPjId() {
        return pjId;
    }

    public void setPjId(String pjId) {
        this.pjId = pjId;
    }
}
