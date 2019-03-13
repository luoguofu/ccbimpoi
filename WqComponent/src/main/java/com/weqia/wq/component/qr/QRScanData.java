package com.weqia.wq.component.qr;

import com.weqia.wq.data.BaseData;

public class QRScanData extends BaseData {


    private static final long serialVersionUID = 1L;
    private int sType;//1,2 CoQRActivity qrType
    private String sId;
    private String coId;
    private String cId;
    /**
     * 扩展字段配合sType使用，当是电子巡更里面还有点信息和项目信息【pointId+pjId】
     */
    private String info;


    public QRScanData() {
    }

    public String getCoId() {
        return coId;
    }

    public void setCoId(String coId) {
        this.coId = coId;
    }

    @Override
    public int getsType() {
        return sType;
    }

    @Override
    public void setsType(int sType) {
        this.sType = sType;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }


    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }
}
