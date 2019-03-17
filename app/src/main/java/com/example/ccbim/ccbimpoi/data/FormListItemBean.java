package com.example.ccbim.ccbimpoi.data;

public class FormListItemBean {

    public String name;
    public String address;
    public int status;

    public FormListItemBean(String name, String address, int status) {
        this.name = name;
        this.address = address;
        this.status = status;
    }

    public FormListItemBean() {
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
