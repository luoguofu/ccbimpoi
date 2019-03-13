package com.weqia.utils.http.okgo.cache;

import com.alibaba.fastjson.JSON;
import com.weqia.data.UtilData;
import com.weqia.utils.StrUtil;
import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;
import com.weqia.utils.annotation.sqlite.Transient;
import com.weqia.utils.http.okgo.model.HttpHeaders;

import java.util.LinkedHashMap;

@Table(name = "cache_entity")
public class CacheEntity<T> extends UtilData {

    private static final long serialVersionUID = -4337711009801627866L;

    public @Transient static final long CACHE_NEVER_EXPIRE = -1;        //缓存永不过期

//    private long id;
    private @Id String key;
    private HttpHeaders responseHeaders;
    private T data;
    private long localExpire;
    private String dataStr;
    private String headerStr;

    //该变量不必保存到数据库,程序运行起来后会动态计算
    private @Transient boolean isExpire;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public HttpHeaders getResponseHeaders() {
        if (responseHeaders == null) {
            this.responseHeaders = new HttpHeaders();
            this.responseHeaders.headersMap = JSON.parseObject(headerStr, LinkedHashMap.class);
        }
        return responseHeaders;
    }

    public void setResponseHeaders(HttpHeaders responseHeaders) {
        this.responseHeaders = responseHeaders;
        this.headerStr = responseHeaders.toJSONString();
    }

    public String getDataStr() {
        return dataStr;
    }

    public void setDataStr(String dataStr) {
        this.dataStr = dataStr;
    }


    public String getHeaderStr() {
//        return responseHeaders.toJSONString();
        return headerStr;
    }

    public void setHeaderStr(String headerStr) {
        this.headerStr = headerStr;
    }

    public T getData() {
        if (StrUtil.notEmptyOrNull(this.dataStr))
            this.data = (T)this.dataStr;
        return data;
    }

    public void setData(T data) {
        this.data = data;
        this.dataStr = this.data.toString();
    }

    public long getLocalExpire() {
        return localExpire;
    }

    public void setLocalExpire(long localExpire) {
        this.localExpire = localExpire;
    }

    public void setIsExpire(boolean expire) {
        isExpire = expire;
    }

    public boolean isIsExpire() {
        return isExpire;
    }

    /**
     * @param cacheTime 允许的缓存时间
     * @param baseTime  基准时间,小于当前时间视为过期
     * @return 是否过期
     */
    public boolean checkExpire(CacheMode cacheMode, long cacheTime, long baseTime) {
        //304的默认缓存模式,设置缓存时间无效,需要依靠服务端的响应头控制
        if (cacheMode == CacheMode.DEFAULT) return getLocalExpire() < baseTime;
        if (cacheTime == CACHE_NEVER_EXPIRE) return false;
        return getLocalExpire() + cacheTime < baseTime;
    }

//    public static <T> ContentValues getContentValues(CacheEntity<T> cacheEntity) {
//        ContentValues values = new ContentValues();
//        values.put(CacheHelper.KEY, cacheEntity.getKey());
//        values.put(CacheHelper.LOCAL_EXPIRE, cacheEntity.getLocalExpire());
//
//        HttpHeaders headers = cacheEntity.getResponseHeaders();
//        ByteArrayOutputStream headerBAOS = null;
//        ObjectOutputStream headerOOS = null;
//        try {
//            if (headers != null) {
//                headerBAOS = new ByteArrayOutputStream();
//                headerOOS = new ObjectOutputStream(headerBAOS);
//                headerOOS.writeObject(headers);
//                headerOOS.flush();
//                byte[] headerData = headerBAOS.toByteArray();
//                values.put(CacheHelper.HEAD, headerData);
//            }
//        } catch (IOException e) {
//            OkLogger.e(e);
//        } finally {
//            try {
//                if (headerOOS != null) headerOOS.close();
//                if (headerBAOS != null) headerBAOS.close();
//            } catch (IOException e) {
//                OkLogger.e(e);
//            }
//        }
//
//        T data = cacheEntity.getData();
//        ByteArrayOutputStream dataBAOS = null;
//        ObjectOutputStream dataOOS = null;
//        try {
//            if (data != null) {
//                dataBAOS = new ByteArrayOutputStream();
//                dataOOS = new ObjectOutputStream(dataBAOS);
//                dataOOS.writeObject(data);
//                dataOOS.flush();
//                byte[] dataData = dataBAOS.toByteArray();
//                values.put(CacheHelper.DATA, dataData);
//            }
//        } catch (IOException e) {
//            OkLogger.e(e);
//        } finally {
//            try {
//                if (dataOOS != null) dataOOS.close();
//                if (dataBAOS != null) dataBAOS.close();
//            } catch (IOException e) {
//                OkLogger.e(e);
//            }
//        }
//        return values;
//    }
//
//    public static <T> CacheEntity<T> parseCursorToBean(Cursor cursor) {
//        CacheEntity<T> cacheEntity = new CacheEntity<>();
//        cacheEntity.setId(cursor.getInt(cursor.getColumnIndex(CacheHelper.ID)));
//        cacheEntity.setKey(cursor.getString(cursor.getColumnIndex(CacheHelper.KEY)));
//        cacheEntity.setLocalExpire(cursor.getLong(cursor.getColumnIndex(CacheHelper.LOCAL_EXPIRE)));
//
//        byte[] headerData = cursor.getBlob(cursor.getColumnIndex(CacheHelper.HEAD));
//        ByteArrayInputStream headerBAIS = null;
//        ObjectInputStream headerOIS = null;
//        try {
//            if (headerData != null) {
//                headerBAIS = new ByteArrayInputStream(headerData);
//                headerOIS = new ObjectInputStream(headerBAIS);
//                Object header = headerOIS.readObject();
//                cacheEntity.setResponseHeaders((HttpHeaders) header);
//            }
//        } catch (Exception e) {
//            OkLogger.e(e);
//        } finally {
//            try {
//                if (headerOIS != null) headerOIS.close();
//                if (headerBAIS != null) headerBAIS.close();
//            } catch (IOException e) {
//                OkLogger.e(e);
//            }
//        }
//
//        byte[] dataData = cursor.getBlob(cursor.getColumnIndex(CacheHelper.DATA));
//        ByteArrayInputStream dataBAIS = null;
//        ObjectInputStream dataOIS = null;
//        try {
//            if (dataData != null) {
//                dataBAIS = new ByteArrayInputStream(dataData);
//                dataOIS = new ObjectInputStream(dataBAIS);
//                T data = (T) dataOIS.readObject();
//                cacheEntity.setData(data);
//            }
//        } catch (Exception e) {
//            OkLogger.e(e);
//        } finally {
//            try {
//                if (dataOIS != null) dataOIS.close();
//                if (dataBAIS != null) dataBAIS.close();
//            } catch (IOException e) {
//                OkLogger.e(e);
//            }
//        }
//
//        return cacheEntity;
//    }



    @Override
    public String toString() {
        return "CacheEntity{" +
//                "id=" + id +
                "key='" + key + '\'' +
                ", responseHeaders=" + responseHeaders +
                ", data=" + data +
                ", localExpire=" + localExpire +
                '}';
    }
}