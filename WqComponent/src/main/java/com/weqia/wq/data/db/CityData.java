package com.weqia.wq.data.db;

import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;
import com.weqia.wq.data.BaseData;

/**
 * 城市
 *
 * @author Berwin
 * @version 1.0
 * @Description :
 * @created 2013-4-9 下午3:48:22
 * @fileName com.weqia.wq1.data.ProvinceData.java
 */
@Table(name = "city_data")
public class CityData extends BaseData {

    private static final long serialVersionUID = 1L;
    private Integer city_id;
    private String city_name;
    @Id
    private Integer id;


    public CityData() {
    }

    public CityData(Integer city_id, String city_name) {
        this.city_id = city_id;
        this.city_name = city_name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCity_id() {
        return city_id;
    }

    public void setCity_id(Integer city_id) {
        this.city_id = city_id;
    }


    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

}
