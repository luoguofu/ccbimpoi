package com.weqia.wq.data.push;

import com.weqia.wq.data.BaseData;

import java.io.Serializable;

/**
 * 
 * @Description :
 * @author Berwin
 * @version 1.0
 * @created 2013-3-27 上午11:21:33
 * @fileName com.weqia.wq1.data.PushData.java
 * 
 */
public class PushData extends BaseData implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer msgType;
	private String mid;
	private String message;

	public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
