package com.weqia.data;

import java.util.List;

import android.content.SharedPreferences;
import android.util.Base64;

import com.alibaba.fastjson.JSON;
import com.weqia.LruMemoryCache;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.datastorage.sharedperference.SharedPerferenceBase;

/**
 * 数据存储XML
 * 
 * @author Bewin berwinzheng@gmail.com
 * @version 1.0 Copyright (c) 2013 Company,Inc. All Rights Reserved.
 */
public class StatedPerference extends SharedPerferenceBase {

    // private final String STATE_PREFERENCE = "4.0_perf";
    private SharedPreferences sharedPreferences;
    private static LruMemoryCache<String, Object> cachedObjs;
    protected static boolean debug_per = false;

    private static StatedPerference instance;

    public static StatedPerference getInstance() {
        if (instance == null) {
            instance = new StatedPerference();
        }
        return instance;
    }

    public SharedPreferences getSharedPreferences() {
        if (StrUtil.isEmptyOrNull(getPerName())) {
            return getPreferences("no_vale_pp");
        }
        if (sharedPreferences == null) sharedPreferences = getPreferences(getPerName());
        return sharedPreferences;
    }

    public String getPerName() {
        if(debug_per) L.e("从共同配置里面获取");
        return "4.0_new_perf";
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key, Class<T> cls) {

        if(debug_per) L.e("获取列表 key = " + key + "从文件" + getPerName());

        Object obj = getCachedObjs().get(key);
        if (obj != null) {
            return (List<T>) obj;
        }
        String mList = getSharedPreferences().getString(key, "");
        if (StrUtil.isEmptyOrNull(mList)) {
            return null;
        }
        try {
            byte[] tmpBytes = Base64.decode(mList, Base64.DEFAULT);
            String realStr = new String(tmpBytes);
            List<T> list = JSON.parseArray(realStr, cls);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T get(String key, Class<T> cls) {
        return get(key, cls, null);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> cls, T dvalue) {
        if(debug_per) L.e("获取单个 key = " + key + "从文件" + getPerName());
        Object obj = getCachedObjs().get(key);
        if (obj != null) {
            return (T) obj;
        }
        String stoStr = getString(getSharedPreferences(), key, "");
        if (StrUtil.isEmptyOrNull(stoStr)) {
            if (dvalue == null) {
                return (T) getDefault(cls);
            } else {
                return dvalue;
            }
        }

        try {
            byte[] tmpBytes = Base64.decode(stoStr, Base64.DEFAULT);
            String realStr = new String(tmpBytes);
            T data = JSON.parseObject(realStr, cls);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            if (dvalue == null) {
                return (T) getDefault(cls);
            } else {
                return dvalue;
            }
        }
    }

    public <T> Object getDefault(Class<T> cls) {
        Object dvalue = null;
        if (cls == Integer.class) {
            dvalue = 0;
        } else if (cls == Float.class || cls == Double.class) {
            dvalue = 0.0f;
        } else if (cls == Boolean.class) {
            dvalue = true;
        } else if (cls == Long.class) {
            return 0l;
        }
        return dvalue;
    }

    /**
     * 默认不需要内存缓存
     * 
     * @param key
     * @param value
     */
    public <T> void put(String key, T value) {
        put(key, value, false);
    }

    /**
     * 是否需求内存缓存
     * 
     * @param key
     * @param value
     * @param bCache
     */
    public <T> void put(String key, T value, Boolean bCache) {
        if(debug_per) L.e("存入 key = " + key + ", 到文件" + getPerName());
        if (value == null) {
            return;
        }
        if (getCachedObjs().get(key) != null) {
            getCachedObjs().remove(key);
        }
        if (bCache != null && bCache) {
            getCachedObjs().put(key, value);
        }
        String realStr = "";
        if (value instanceof UtilData) {
            realStr = value.toString();
        } else {
            realStr = JSON.toJSONString(value);
        }
        byte[] tmpBytes = Base64.encode(realStr.getBytes(), Base64.DEFAULT);
        if (getSharedPreferences() != null) {
            putString(getSharedPreferences(), key, new String(tmpBytes));
        } else {
            if(debug_per)  L.e("没有配置文件信息，不能写入");
        }
    }

    public void remove(String key) {
        getCachedObjs().remove(key);
        if (getSharedPreferences() != null) {
            removeKey(getSharedPreferences(), key);
        } else {
            if(debug_per)  L.e("没有配置文件信息，不能写入");
        }
    }

    public LruMemoryCache<String, Object> getCachedObjs() {
        if (cachedObjs == null) {
            cachedObjs = new LruMemoryCache<>(100);
        }
        return cachedObjs;
    }
}
