package com.weqia.wq.data.base;

import com.weqia.wq.data.BaseData;

/**
 *视口ID信息
 */
public class MpIdData extends BaseData {

    private static final long serialVersionUID = 1L;

    private String mpId;

    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMpId() {
        return mpId;
    }

    public void setMpId(String mpId) {
        this.mpId = mpId;
    }
}
