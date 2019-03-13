package com.weqia.wq.data.push;

import com.weqia.wq.data.BaseData;


public class PushDataEx extends BaseData {

	private static final long serialVersionUID = 1L;

	private String content;
	private String btype;
	private String send_no;
	private String warn_content;
	private String mid;
	private String coId;
	private String warn_type;
	private String voiceType;
	private String pjId;
	private String gmtCreate;      //推送产生的时间


	public String getPjId() {
		return pjId;
	}

	public void setPjId(String pjId) {
		this.pjId = pjId;
	}

	public String getVoiceType() {
        return voiceType;
    }

    public void setVoiceType(String voiceType) {
        this.voiceType = voiceType;
    }

    public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getBtype() {
		return btype;
	}

	public void setBtype(String btype) {
		this.btype = btype;
	}

	public String getSend_no() {
		return send_no;
	}

	public void setSend_no(String send_no) {
		this.send_no = send_no;
	}

	public String getWarn_content() {
		return warn_content;
	}

	public void setWarn_content(String warn_content) {
		this.warn_content = warn_content;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getCoId() {
		return coId;
	}

	public void setCoId(String coId) {
		this.coId = coId;
	}

	public String getWarn_type() {
		return warn_type;
	}

	public void setWarn_type(String warn_type) {
		this.warn_type = warn_type;
	}

	public String getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
}
