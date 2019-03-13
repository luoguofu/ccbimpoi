package com.weqia.wq.data.db;

import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;
import com.weqia.wq.data.BaseData;

/**
 * 省
 *
 * @author Berwin
 * @version 1.0
 * @Description :
 * @created 2013-4-9 下午3:48:22
 * @fileName com.weqia.wq1.data.ProvinceData.java
 */
@Table(name = "province_data")
public class ProvinceData extends BaseData {

    private static final long serialVersionUID = 1L;
    private Integer province_i;
    private String province_n;
    @Id
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProvince_i() {
        return province_i;
    }

    public void setProvince_i(Integer province_i) {
        this.province_i = province_i;
    }

    public String getProvince_n() {
        return province_n;
    }

    public void setProvince_n(String province_n) {
        this.province_n = province_n;
    }

}
