package com.weqia.utils.reflect;

import java.lang.reflect.Field;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.weqia.utils.L;

public class RefUtil {

    /**
     * toMap
     * 
     * @Description
     * @param bean
     * @return
     * 
     */
    @SuppressWarnings("unchecked")
    public static Map<String, ?> getFieldValueMap(Object bean) {
        if (bean == null) {
            if (L.D) {
                L.e("input object is null, can't get value map");
            }
            return null;
        }

        return (Map<String, ?>) JSON.toJSON(bean);
    }

    public static Object setFieldValue(Class<?> cls, String string) {

        if (cls == null) {
            if (L.D) {
                L.e("input cls is null, can't set value");
            }
            return null;
        }

        if (string == null) {
            if (L.D) {
                L.e("input string is null, can't set value");
            }
            return null;
        }

        return JSON.parseObject(string, cls);
    }

    public static Object reflectMethod(Object a, String str) {
        try {
            Field field = a.getClass().getDeclaredField(str);
            field.setAccessible(true);
            return field.get(a);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;

    }
}
