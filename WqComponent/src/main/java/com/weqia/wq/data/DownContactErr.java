package com.weqia.wq.data;

import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;

@Table(name = "down_contact_err")
public class DownContactErr extends BaseData {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public DownContactErr() {
    }
    
    public DownContactErr(String mid) {
        this.mid = mid;
    }

    @Id
    private String mid;
    private long cDate = System.currentTimeMillis();

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public long getcDate() {
        return cDate;
    }

    public void setcDate(long cDate) {
        this.cDate = cDate;
    }
}
