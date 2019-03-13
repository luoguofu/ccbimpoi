package com.weqia.wq.data;

public class PosData extends BaseData {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String addr; //": "浙江省杭州市西湖区萍水西路118号",
    private String dis;//: "62",
    private double y;//: "30.312461", 纬度 latitude
    private String name;//: "萍西茶吧",
    private String tel;//: "",
    private double x;//"
    private boolean select;

    public PosData() {

    }

    public PosData(String addr, String dis, double y, String name, String tel, double x) {
        super();
        this.addr = addr;
        this.dis = dis;
        this.y = y;
        this.name = name;
        this.tel = tel;
        this.x = x;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getDis() {
        return dis;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

}
