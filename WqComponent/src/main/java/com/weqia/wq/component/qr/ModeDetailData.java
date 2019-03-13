package com.weqia.wq.component.qr;

import com.weqia.wq.data.BaseData;

/**
 * <pre>
 *     author: MLLWF
 *     time  : 2017/12/5
 *     desc  :  3616接口 返回的数据
 * </pre>
 */
public class ModeDetailData extends BaseData {
    private String projectId; //项目ID
    private String qrCodeId; //二维码ID
    private String subProjectId;  //子项目ID
    private String nodeId;  //模型节点ID
    private String cId;// 创建人
    private String floor; //楼层ID
    private Long cDate;
    private int type;  //类型 1-模型二维码 2-构建二维码
    private String info;  //模型构建信息（如果为模型二维码，则为空）
    private String memo;  //备注
    private String viewInfo;
    private int isMember;  //是否是项目成员 1是  2 不是
    private String versionId;
    private String node;  //模型数据

    public int getIsMember() {
        return isMember;
    }

    public void setIsMember(int isMember) {
        this.isMember = isMember;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getViewInfo() {
        return viewInfo;
    }

    public void setViewInfo(String viewInfo) {
        this.viewInfo = viewInfo;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(String qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    public String getSubProjectId() {
        return subProjectId;
    }

    public void setSubProjectId(String subProjectId) {
        this.subProjectId = subProjectId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Long getcDate() {
        return cDate;
    }

    public void setcDate(Long cDate) {
        this.cDate = cDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
