package com.weqia.wq.data;

/**
 * 转发数据的类
 * @author Berwin
 *
 */
public class TransData extends BaseData {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private boolean outer = true; //true 外部文件分享，false 内部文件
    private int contentType; //转发的文件类型
    private String transId; //内部分享已经发送成功的id
    private String transText; //转发的文字或地图
    private MediaData mediaData; //外部分享的文件信息
    private BaseData insideData; //内部转化的文件信息 //MsgData DiscussProgress
    private BaseData sendToData; //文件为空，聊天为EnterpriseContact，会议为DisuccData
    private String transExtend; //额外的文字
    
    public TransData() {
    }
    


    public boolean isOuter() {
        return outer;
    }

    public void setOuter(boolean outer) {
        this.outer = outer;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public MediaData getMediaData() {
        return mediaData;
    }

    public void setMediaData(MediaData mediaData) {
        this.mediaData = mediaData;
    }

    public BaseData getSendToData() {
        return sendToData;
    }

    public void setSendToData(BaseData sendToData) {
        this.sendToData = sendToData;
    }



    public String getTransText() {
        return transText;
    }



    public void setTransText(String transText) {
        this.transText = transText;
    }



    public BaseData getInsideData() {
        return insideData;
    }



    public void setInsideData(BaseData insideData) {
        this.insideData = insideData;
    }



    public String getTransExtend() {
        return transExtend;
    }



    public void setTransExtend(String transExtend) {
        this.transExtend = transExtend;
    }
}
