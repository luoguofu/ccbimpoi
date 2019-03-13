package com.weqia.data;

import com.alibaba.fastjson.JSON;
import com.weqia.utils.StrUtil;
import com.weqia.utils.annotation.sqlite.Id;

import java.io.Serializable;
import java.util.List;

public class UtilData implements Serializable {

    private static final long serialVersionUID = 1L;
    private @Id
    Integer table_id;

    /**
     * 返回 table_id 的值
     * 
     * @return table_id
     * 
     */

    public Integer getTable_id() {
        return table_id;
    }

    /**
     * 设置 table_id 的值
     * 
     * @param table_id
     * 
     */
    public void setTable_id(Integer table_id) {
        this.table_id = table_id;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @SuppressWarnings("unchecked")
    public static <T extends UtilData> T fromString(Class<? extends UtilData> cls, String jsonString) {
        T t = null;
        if (StrUtil.isEmptyOrNull(jsonString)) {
            return null;
        }
        try {
            t = (T) JSON.parseObject(jsonString, cls);
        } catch (Exception e) {
            t = null;
            // CheckedExceptionHandler.handleException(e);
        }
        return t;
    }

    public static <T extends UtilData> List<T> fromList(Class<T> cls, String list) {
        if (list == null) {
            return null;
        }
        List<T> arrayList = JSON.parseArray(list, cls);
        return arrayList;
    }
}
