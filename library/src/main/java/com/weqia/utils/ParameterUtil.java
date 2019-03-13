package com.weqia.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * 
 * @Description :参数处理
 * @author Sam
 * @version 1.0
 * @created 2011-9-1 下午04:37:53
 * @fileName com.weqia.common.util.ParameterUtil.java
 */
public class ParameterUtil {
    // private static final Log log = LogFactory.getLog(ParameterUtil.class);


    /**
     * 
     * @Description 把name=value&name=value&name=value&name=value的字符串转换成map
     * @param paramString
     * @return
     */
    public static Map<String, String> toMap(String paramString) {
        if (paramString == null) return null;

        String[] nameValueArr = paramString.split("\\&");
        if (nameValueArr == null || nameValueArr.length == 0) return null;

        Map<String, String> map = new HashMap<String, String>();
        for (String nameValue : nameValueArr) {
            if (nameValue == null) continue;
            String[] param = nameValue.split("=");
            if (param == null || param.length == 0) continue;

            map.put(param[0], param.length > 1 ? (param[1] == null ? "" : param[1]) : "");
        }

        return map;
    }

    /**
     * 
     * @Description 把name=value&name=value&name=value&name=value的字符串转换成map, 支持String[]
     * @param paramString
     * @return
     * 
     */
    public static Map<String, String[]> toMapArray(String paramString) {
        if (paramString == null) return null;

		String[] nameValueArr = paramString.split("\\&");
		if (nameValueArr == null || nameValueArr.length == 0)
			return null;

		Map<String, String[]> map = new HashMap<String, String[]>();
		String[] newValues;
		
		for (String nameValue : nameValueArr) {
			if (nameValue == null)
				continue;
			String[] param = nameValue.split("=");
			if (param == null || param.length == 0)
				continue;

			String[] values = map.get(param[0]);
			if (null!=values){
				newValues = new String[values.length+1];
				for (int i = 0; i < values.length; i++){
					newValues[i]=values[i];
				}	
				newValues[values.length] = param[1];
			}else{
				newValues = new String[1];
				
				if (param.length<=1){
					newValues[0] = "";
				}else{
					newValues[0] = param[1];
				}
				
			}
			
			
			map.put(param[0], newValues);
		}		
		
				
		return map;
    }

    public static String toString(Integer i) {
        return String.valueOf(i);
    }

    /**
     * 
     * @Description
     * @param map 把map转换成name=value&name=value&name=value&name=value各式的字符串
     * @return
     */
    public static String toString(Map<String, String> map) {
        if (map == null) return null;
        Set<Entry<String, String>> set = map.entrySet();
        if (set == null || set.size() == 0) return null;
        StringBuilder bu = new StringBuilder();

        int i = 0;
        for (Entry<String, String> entry : set) {
            bu.append(entry.getKey()).append("=")
                    .append(entry.getValue() == null ? "" : entry.getValue());
            if (i + 1 < set.size()) {
                bu.append("&");
            }
            i++;
        }

        return bu.toString();
    }


    /**
     * 
     * @Description
     * @param map 把map转换成name=value&name=value&name=value&name=value各式的字符串
     * @return
     */
    public static String toStringArray(Map<String, String[]> map) {
        if (map == null) return null;

        Set<Entry<String, String[]>> set = map.entrySet();
        if (set == null || set.size() == 0) return null;

        StringBuilder bu = new StringBuilder();

        int i = 0;
        for (Entry<String, String[]> entry : set) {
            // bu.append(entry.getKey()).append("=").append(entry.getValue() == null ? "" :
            // entry.getValue().toString());

            if (null != entry.getValue()) {
                int jj = 0;
                for (String s : entry.getValue()) {
                    bu.append(entry.getKey()).append("=");
                    bu.append(s == null ? "" : s);

                    if (jj + 1 < entry.getValue().length) {
                        bu.append("&");
                    }
                    jj++;
                }
            }

            if (i + 1 < set.size()) {
                bu.append("&");
            }
            i++;
        }
        return bu.toString();
    }


	/**
	 * 
	 * @Description Map返回有序的字符串
	 * @param map
	 * @return  
	 *
	 */
	public static String toStringArraySort(Map<String, String[]> map) {
	
		map = changeMaps(map);
		
		return toStringArray(map);
	}		
	
	/**
	 * 
	 * @Description 返回有序的TreeMap
	 * @param map
	 * @return  
	 *
	 */
	public static Map<String,String[]> changeMaps(Map<String,String[]> map){
		Map<String,String[]>  mapRet = new TreeMap<String, String[]>();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String[] value = map.get(key);   
			mapRet.put(key, value);
		}
		return mapRet;
	}

    /**
     * 是否忽略转换成字符串
     * 
     * @Description :
     * @author Sam
     * @version 1.0
     * @created Oct 1, 2011 11:28:36 PM
     * @fileName com.weqia.common.util.ParameterUtil.java
     * 
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @Inherited
    public static @interface IgnoreToString {}


}
