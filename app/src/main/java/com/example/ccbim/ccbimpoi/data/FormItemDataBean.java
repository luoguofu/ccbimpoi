package com.example.ccbim.ccbimpoi.data;

import java.util.List;

public class FormItemDataBean {

    public String title;

    public List<ChildItemBean> childItemBeanList;

    public FormItemDataBean(String title, List<ChildItemBean> childItemBeanList) {
        this.title = title;
        this.childItemBeanList = childItemBeanList;
    }

    public String getTitle() {
        return title;
    }

    public List<ChildItemBean> getChildItemBeanList() {
        return childItemBeanList;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setChildItemBeanList(List<ChildItemBean> childItemBeanList) {
        this.childItemBeanList = childItemBeanList;
    }
}
