package com.weqia.wq.data;

import com.weqia.data.UtilData;
import com.weqia.utils.annotation.sqlite.Transient;
import com.weqia.wq.data.global.WeqiaApplication;

public class BaseData extends UtilData {

    private static final long serialVersionUID = 1L;
    private Integer readed;// 0已读,1未读
    private String gCoId = WeqiaApplication.getgMCoId();
    private String mid = "";
    
    private @Transient transient String stitle;
    private @Transient transient boolean sheader;
    private @Transient transient boolean send;
    private @Transient int sType;
    private String parameter;

    public Integer getReaded() {
        return readed;
    }

    public void setReaded(Integer readed) {
        this.readed = readed;
    }

    public String getgCoId() {
        return gCoId;
    }

    public void setgCoId(String gCoId) {
        this.gCoId = gCoId;
    }
    
    public String getStitle() {
        return stitle;
    }

    public void setStitle(String stitle) {
        this.stitle = stitle;
    }

    public boolean isSheader() {
        return sheader;
    }

    public void setSheader(boolean sheader) {
        this.sheader = sheader;
    }

    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }

    public int getsType() {
        return sType;
    }

    public void setsType(int sType) {
        this.sType = sType;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
