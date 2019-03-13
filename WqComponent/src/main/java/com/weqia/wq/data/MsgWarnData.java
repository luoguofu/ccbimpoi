package com.weqia.wq.data;

//消息提醒
public class MsgWarnData extends BaseData {

    private static final long serialVersionUID = 1L;

    private String warn;
    private Integer warnType; //提醒类型 0 - 普通提醒 小红点 1- 相关重要提醒（负责人,@人员） 2-不标记，不重要
    private Integer voiceType; //声音类型  1有声音 2无声音
    private String coId;
    private String pjId;
    private String gmtCreate;      //消息产生的时间
    /**
     *sendNo-客户端用，服务端这个值不会初始化
     */
    private Integer sendNo;

    public Integer getSendNo() {
        return sendNo;
    }

    public void setSendNo(Integer sendNo) {
        this.sendNo = sendNo;
    }

    public enum warnTypeEnum {
        GENERAL(0, "普通"), IMPORTANT(1, "相关"), NOT_IMPORTANT(2, "不标记");
        private String strName;
        private int value;

        private warnTypeEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }


        public String strName() {
            return strName;
        }

        public int value() {
            return value;
        }

    }

    public String getPjId() {
        return pjId;
    }

    public void setPjId(String pjId) {
        this.pjId = pjId;
    }

    public String getWarn() {
        return warn;
    }

    public void setWarn(String warn) {
        this.warn = warn;
    }

    public Integer getWarnType() {
        return warnType;
    }

    public void setWarnType(Integer warnType) {
        this.warnType = warnType;
    }

    public String getCoId() {
        return coId;
    }

    public void setCoId(String coId) {
        this.coId = coId;
    }


    public Integer getVoiceType() {
        return voiceType;
    }

    public void setVoiceType(Integer voiceType) {
        this.voiceType = voiceType;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
