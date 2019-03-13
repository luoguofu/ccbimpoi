package com.weqia.wq.data.base;

import com.weqia.wq.data.BaseData;

/**
 * Created by lgf on 2018/12/11.
 */

public class CheckData extends BaseData{
    private boolean check;
    private String tagId;      //单选的id
    private String tagValue;   //单选的值
    private String name;         //多选的值
    private String value;         //单选的id

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
