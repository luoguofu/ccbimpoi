package com.weqia.wq.component.view.ssq;

import java.util.ArrayList;
import java.util.List;

import com.weqia.utils.datastorage.db.DaoConfig;
import com.weqia.wq.component.db.WeqiaDbUtil;
import com.weqia.wq.data.db.AreaData;
import com.weqia.wq.data.db.CityData;
import com.weqia.wq.data.db.ProvinceData;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.data.global.WeqiaApplication;


public class SSQUtil {

    WeqiaDbUtil db;

    public SSQUtil() {
        DaoConfig config = new DaoConfig();
        config.setContext(WeqiaApplication.ctx);
        config.setDbName(GlobalConstants.TALK_DB_NAME);
        db = WeqiaDbUtil.create(config);
    }

    public void resetDb() {
        if (db != null) {
            db.removeDbUtil(db);
        }
    }


    //获得全国省份的列表
    public List<ProvinceData> getProvinces() {
        List<ProvinceData> provinceList = new ArrayList<ProvinceData>();
        if (db != null) {
            provinceList = db.findAll(ProvinceData.class);
        }
        return provinceList;
    }


    //根据省份查找对应的城市列表
    public List<CityData> getCitysByProvince(Integer provinceId) {
        List<CityData> cityList = new ArrayList<CityData>();
        if (db != null) {
            cityList = db.findAllByWhereNoCo(CityData.class, "province_i =" + provinceId);
        }
        return cityList;
    }

    //根据城市查找对应的区县列表
    public List<AreaData> getAreasByCity(Integer cityId) {
        List<AreaData> areaList = new ArrayList<AreaData>();
        if (db != null) {
            areaList = db.findAllByWhereNoCo(AreaData.class, "city_id =" + cityId);
        }
        return areaList;
    }


    //根据区县ID查询区县信息
    public AreaData getAreasByAreaId(Integer areaId) {
        AreaData data = null;
        if (db != null) {
            data = db.findByWhere(AreaData.class, "area_id =" + areaId);
        }
        return data;
    }

    /**
     *获取城市信息
     */
    public CityData getCityDataByCityId(Integer cityId) {
        CityData data = null;
        if (db != null) {
            data = db.findByWhere(CityData.class, "city_id =" + cityId);
        }
        return data;
    }


}
