package com.weqia.wq.data.base;

import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;
import com.weqia.wq.data.BaseData;

@Table(name = "push_unique_data")
public class PushUniqueData extends BaseData {

    private static final long serialVersionUID = 1L;
    private
    @Id
    String send_no;

    public PushUniqueData() {
    }

    public PushUniqueData(String send_no) {
        this.send_no = send_no;
    }

    public String getSend_no() {
        return send_no;
    }

    public void setSend_no(String send_no) {
        this.send_no = send_no;
    }
}
