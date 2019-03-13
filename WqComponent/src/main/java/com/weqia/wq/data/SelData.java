package com.weqia.wq.data;

import com.weqia.utils.annotation.sqlite.Transient;
import com.weqia.wq.data.global.WeqiaApplication;

public class SelData extends BaseData {

    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private String mNo;// wq号

    private String mobile;// 电话
    private String email;// 邮箱

    private transient
    @Transient
    boolean bHead = false;

    private transient
    @Transient
    Boolean checked = false;
    private String gCoId = WeqiaApplication.getgMCoId();

    private String mLogo;
    private String mName;
    public String pinyin;


    public SelData() {
    }

    public SelData(String mNo, String mName, String mLogo) {
        this.mNo = mNo;
        this.mName = mName;
        this.mLogo = mLogo;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public boolean isbHead() {
        return bHead;
    }

    public void setbHead(boolean bHead) {
        this.bHead = bHead;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getsId() {
        return null;
    }

    public void setsId(String sId) {
    }

    public String getgCoId() {
        return gCoId;
    }

    public void setgCoId(String gCoId) {
        this.gCoId = gCoId;
    }
    
    public String getsSign(){
        return null;
    }

    public String getmLogo() {
        return mLogo;
    }

    public void setmLogo(String mLogo) {
        this.mLogo = mLogo;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmNo() {
        return mNo;
    }

    public void setmNo(String mNo) {
        this.mNo = mNo;
    }

    public String getNo() {
        return mNo;
    }

    public void setNo(String No) {
        this.mNo = No;
    }
}
