package com.weqia.wq.data;

import com.weqia.wq.component.utils.request.ServiceParams;

public class MediaParams extends ServiceParams {

    private String content;
    
    private String prov; // 省份
    private String city; // 城市
    private String dist; // 区县
    private String street; // 街道
    private String stNum; // 门址
    private Double pointx; // x坐标
    private Double pointy; // y坐标
    private String addr;
    private String adName;
    
    //WEBO
    private String files;
    private String isPub;
    private String clId;//分类ID
    
    private String realContent;
    private String link;

    public MediaParams(Integer iType) {
        super(iType);
    }

    public MediaParams() {}


    public String getContent() {
        return content;
    }
    
    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }
    public String getIsPub() {
        return isPub;
    }

    public void setIsPub(String isPub) {
        this.isPub = isPub;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
    public String getClId() {
        return clId;
    }

    public void setClId(String clId) {
        this.clId = clId;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStNum() {
        return stNum;
    }

    public void setStNum(String stNum) {
        this.stNum = stNum;
    }

    public Double getPointx() {
        return pointx;
    }

    public void setPointx(Double pointx) {
        this.pointx = pointx;
    }

    public Double getPointy() {
        return pointy;
    }

    public void setPointy(Double pointy) {
        this.pointy = pointy;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public void setLocateData(String addr, String prov, String city, String dist, String street,
            String stNum, Double pointx, Double pointy, String adName) {
        this.addr = addr;
        this.prov = prov;
        this.city = city;
        this.dist = dist;
        this.street = street;
        this.stNum = stNum;
        this.pointx = pointx;
        this.pointy = pointy;
        this.adName = adName;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getRealContent() {
        return realContent;
    }

    public void setRealContent(String realContent) {
        this.realContent = realContent;
    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
