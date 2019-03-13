package com.weqia.wq.data.base;

import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;
import com.weqia.wq.data.BaseData;


/**
 * Created by ML on 2017/5/25.
 */
@Table(name = "task_type_data")
public class TaskTypeData extends BaseData {
    @Id
    private int id;
    private String pjId;
    private String dictId;
    private String dictValue;
    private boolean selected;

    public TaskTypeData() {
    }

    public TaskTypeData(String dictId, String dictValue, boolean isSelect) {
        this.selected = isSelect;
        this.dictId = dictId;
        this.dictValue = dictValue;
    }

    public String getDictId() {
        return dictId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDictId(String dictId) {
        this.dictId = dictId;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getPjId() {
        return pjId;
    }

    public void setPjId(String pjId) {
        this.pjId = pjId;
    }
}
