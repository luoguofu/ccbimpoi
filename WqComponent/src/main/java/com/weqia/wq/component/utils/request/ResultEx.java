package com.weqia.wq.component.utils.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.weqia.wq.data.BaseData;

import java.util.List;

/**
 * 接口返回结果
 *
 * @author Berwin
 * @version 1.0
 * @Description :
 * @created 2013-3-29 下午1:43:39
 * @fileName com.weqia.wq1.data.net.ResultEx.java
 */
public class ResultEx extends Result {

    private static final long serialVersionUID = 1L;

    /**
     * 获取单条object数据
     *
     * @param cls
     * @return
     * @Description
     */

    public <T extends BaseData> T getDataObject(Class<T> cls) {
        String obj = getObject();
        if (obj == null) {
            return null;
        }
        T data = JSON.parseObject(obj, cls);
        return data;
    }


    public String getDataObjectStr() {
        String obj = getObject();
        if (obj == null) {
            return null;
        }
        return obj;
    }
    public String getDataStr() {
        String obj = getList();
        if (obj == null) {
            return null;
        }
        return obj;
    }

    /**
     * 数组形式数据
     *
     * @param cls
     * @return
     * @Description
     */

    public <T> List<T> getDataArray(Class<T> cls) {
        String list = getList();
        if (list == null) {
            return null;
        }
        List<T> datas = (List<T>) JSONArray.parseArray(list, cls);
        return datas;
    }

    /**
     * result为-1则代表失败
     *
     * @return
     * @Description
     */
    public boolean isFailed() {
        String ret = getRet();
        if (ret != null) {
            if (Integer.valueOf(ret) < 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否成功
     *
     * @return
     * @Description
     */
    public boolean isSuccess() {
        String ret = getRet();
        if (ret != null) {
            if (Integer.valueOf(ret) >= 0) {
                return true;
            }
        }

        if (getObject() != null) {
            return true;
        }

        if (getList() != null) {
            return true;
        }

        return false;
    }

    public boolean isEmpty() {
        String ret = getRet();
        if (ret != null) {
            if (Integer.valueOf(ret) == 0) {
                return true;
            }
        }
        return false;
    }
}
