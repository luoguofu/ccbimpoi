package com.example.ccbim.ccbimpoi.data;

/**
 * 单个表单实体类
 */
public class ChildItemBean {

    public String title;

    public int status;

    public ChildItemBean(String title, int status) {
        this.title = title;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public int getStatus() {
        return status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
