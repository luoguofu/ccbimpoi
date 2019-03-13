package com.weqia.wq.data.base;

import java.io.Serializable;

import com.weqia.wq.data.BaseData;

public class SettingRowData extends BaseData implements Serializable {
    private static final long serialVersionUID = 8838725426885988959L;
    private Integer id;
    private Integer type;
    private Integer drawableRightId;
    private Integer drawableLeftId;
    private CharSequence title;
    private CharSequence summary;
    private CharSequence content;
    private BaseData data;
    private String number;


    public SettingRowData() {
    }


    public SettingRowData(BaseData data) {
        this.data = data;
    }

    public SettingRowData(Integer id, CharSequence title) {
        this.id = id;
        this.title = title;
    }


    public SettingRowData(Integer id, CharSequence title, BaseData data) {
        this.id = id;
        this.title = title;
        this.data = data;
    }

    public SettingRowData(CharSequence title, CharSequence content, BaseData data) {
        this.title = title;
        this.content = content;
        this.data = data;
    }

    public SettingRowData(CharSequence title) {
        this.title = title;
    }

    public SettingRowData(CharSequence title, CharSequence summary) {
        this.title = title;
        this.summary = summary;
    }

    public SettingRowData(CharSequence title, CharSequence summary, Integer drawableRightId) {
        this.title = title;
        this.summary = summary;
        this.drawableRightId = drawableRightId;
    }

    public SettingRowData(int id, int type, BaseData data, CharSequence title, CharSequence summary) {
        this.id = id;
        this.type = type;
        this.data = data;
        this.title = title;
        this.summary = summary;
    }


    public SettingRowData(Integer id, CharSequence title, CharSequence summary) {
        this.id = id;
        this.title = title;
        this.summary = summary;
    }

    public SettingRowData(Integer id, CharSequence title, CharSequence summary, Integer drawableRightId) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.drawableRightId = drawableRightId;
    }


    public SettingRowData(Integer drawableLeftId, CharSequence title, CharSequence summary, CharSequence content, BaseData data) {
        this.drawableLeftId = drawableLeftId;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.data = data;
    }

    public Integer getDrawableLeftId() {
        return drawableLeftId;
    }

    public void setDrawableLeftId(Integer drawableLeftId) {
        this.drawableLeftId = drawableLeftId;
    }

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public CharSequence getSummary() {
        return summary;
    }

    public void setSummary(CharSequence summary) {
        this.summary = summary;
    }


    public BaseData getData() {
        return data;
    }

    public void setData(BaseData data) {
        this.data = data;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDrawableRightId() {
        return drawableRightId;
    }

    public void setDrawableRightId(Integer drawableRightId) {
        this.drawableRightId = drawableRightId;
    }

    public Integer getId() {
        return id;
    }

    public Integer getType() {
        return type;
    }


    public CharSequence getContent() {
        return content;
    }

    public void setContent(CharSequence content) {
        this.content = content;
    }
    
    public void setNumber(String number) {
        this.number = number;
    }
    
    public String getNumber() {
        return number;
    }
    
}
