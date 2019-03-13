package com.weqia.wq.data;


public class MyLocData extends BaseData implements Cloneable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Double latitude;// 纬度 x
    private Double longitude;// 经度 y
    private String provinc;// 省
    private String city;// 城市
    private String district;// 区域
    private String street;
    private String strNum;
    private String radius;// 半径
    private String poi;// 点
    private String addrStr;// 地址
    private String locType;// 位置类型
    private String time;// 定位时间

    private boolean canDelete = true;
    private boolean bJustMap = true;

    private String addrName;// 地址名称


    public MyLocData() {
    }


    public void poiToLocData(String provinc, String city, String district, String street, String strNum) {
        setProvinc(provinc);
        setCity(city);
        setDistrict(district);
        setStreet(street);
        setStrNum(strNum);
    }

    public void setPoiInfo(Double latitude, Double longitude, String addrName, String addrStr) {
        setLatitude(latitude);
        setLongitude(longitude);
        setAddrName(addrName);
        setAddrStr(addrStr);
    }

    public MyLocData(Double latitude, Double longitude, String addrStr) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.addrStr = addrStr;
    }

    public MyLocData(Double latitude, Double longitude, String addrStr, String addrName) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.addrStr = addrStr;
        this.addrName = addrName;
    }

    public MyLocData(Double latitude, Double longitude, String provinc, String city, String district, String street,
                     String strNum, String radius, String poi, String addrStr, String locType, String time, String addrName, boolean bJustMap) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.provinc = provinc;
        this.city = city;
        this.district = district;
        this.street = street;
        this.strNum = strNum;
        this.radius = radius;
        this.poi = poi;
        this.addrStr = addrStr;
        this.locType = locType;
        this.time = time;
        this.addrName = addrName;
        this.bJustMap = bJustMap;
    }


    public MyLocData(Double latitude, Double longitude, String provinc, String city, String district, String street, String strNum, String addrStr, String addrName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.provinc = provinc;
        this.city = city;
        this.district = district;
        this.street = street;
        this.strNum = strNum;
        this.addrStr = addrStr;
        this.addrName = addrName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStrNum() {
        return strNum;
    }

    public void setStrNum(String strNum) {
        this.strNum = strNum;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getProvinc() {
        return provinc;
    }

    public void setProvinc(String provinc) {
        this.provinc = provinc;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getPoi() {
        return poi;
    }

    public void setPoi(String poi) {
        this.poi = poi;
    }

    public String getAddrStr() {
        return addrStr;
    }

    public void setAddrStr(String addrStr) {
        this.addrStr = addrStr;
    }

    public String getLocType() {
        return locType;
    }

    public void setLocType(String locType) {
        this.locType = locType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddrName() {
        return addrName;
    }

    public void setAddrName(String addrName) {
        this.addrName = addrName;
    }


    public boolean isCanDelete() {
        return canDelete;
    }

    public boolean isbJustMap() {
        return bJustMap;
    }

    public void setbJustMap(boolean bJustMap) {
        this.bJustMap = bJustMap;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }


    @Override
    public Object clone() {
        MyLocData tmp = null;
        try {
            tmp = (MyLocData) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return tmp;
    }

}
