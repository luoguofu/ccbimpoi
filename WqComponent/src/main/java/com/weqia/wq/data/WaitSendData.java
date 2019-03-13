package com.weqia.wq.data;

import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;
import com.weqia.wq.data.EnumData.DataStatusEnum;
import com.weqia.wq.data.global.WeqiaApplication;

@Table(name = "wait_send")
public class WaitSendData extends BaseData {

	private static final long serialVersionUID = 1L;

	private @Id
	int gId;
	private Integer itype;
	private String mid;
	private String content;
	private String params;
	private String gmtCreate;
	private Integer sendStatus;// 0待发,1发送成功,2发送失败
	private Integer saveId;
    private String realContent;
	private String coId;

    public WaitSendData() {
	}

	public WaitSendData(Integer itype, String content, String gmtCreate,
			String params, String coId) {
		super();
		this.itype = itype;
		this.mid = WeqiaApplication.getInstance().getLoginUser().getMid();
		this.content = content;
		this.gmtCreate = gmtCreate;
		this.params = params;
		this.sendStatus = DataStatusEnum.SENDIND.value();
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public int getgId() {
		return gId;
	}

	public void setgId(int gId) {
		this.gId = gId;
	}

	public Integer getItype() {
		return itype;
	}

	public void setItype(Integer itype) {
		this.itype = itype;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Integer getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(Integer status) {
		this.sendStatus = status;
	}

    public Integer getSaveId() {
        return saveId;
    }

    public void setSaveId(Integer saveId) {
        this.saveId = saveId;
    }

    public String getRealContent() {
        return realContent;
    }

    public void setRealContent(String realContent) {
        this.realContent = realContent;
    }

	public String getCoId() {
		return coId;
	}

	public void setCoId(String coId) {
		this.coId = coId;
	}
}
