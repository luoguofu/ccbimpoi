package com.weqia.utils.datastorage.sharedperference;

import java.util.Map;

import android.content.SharedPreferences;

import com.weqia.BaseInit;

/**
 * Description: SharedPerferenceBase.java Create on 2013-1-1 上午12:10:05
 * 
 * @author Bewin berwinzheng@gmail.com
 * @version 1.0 Copyright (c) 2013 Company,Inc. All Rights Res
 */
public class SharedPerferenceBase {

    public static SharedPreferences getPreferences(String fileString) {
        return BaseInit.getPreferences(fileString);
    }

    /**
     * 
     * @Title: getBoolean
     * @param perference
     * @param keyString
     * @param defaultBoolean
     * @return
     * @return boolean
     * @throws
     */
    public static boolean getBoolean(SharedPreferences perference, String keyString,
            Boolean defaultBoolean) {
        return perference.getBoolean(keyString, defaultBoolean.booleanValue());
    }

    /**
     * 
     * @Title: putBoolean
     * @param perPreference
     * @param keyString
     * @param booleanValue
     * @return void
     * @throws
     */
    public static void putBoolean(SharedPreferences perPreference, String keyString,
            Boolean booleanValue) {
        perPreference.edit().putBoolean(keyString, booleanValue).commit();
    }

    /**
     * 
     * @Title: getFloat
     * @param perference
     * @param keyString
     * @param defaultFloat
     * @return
     * @return float
     * @throws
     */
    public static float getFloat(SharedPreferences perference, String keyString, Float defaultFloat) {
        return perference.getFloat(keyString, defaultFloat.floatValue());
    }

    /**
     * 
     * @Title: putFloat
     * @param perference
     * @param keyString
     * @param floatValue
     * @return void
     * @throws
     */
    public static void putFloat(SharedPreferences perference, String keyString, Float floatValue) {
        perference.edit().putFloat(keyString, floatValue.floatValue()).commit();
    }

    /**
     * 
     * @Title: getInt
     * @param perference
     * @param keyString
     * @param defaultInteger
     * @return
     * @return int
     * @throws
     */
    public static Integer getInt(SharedPreferences perference, String keyString,
            Integer defaultInteger) {
        return perference.getInt(keyString, defaultInteger.intValue());
    }

    /**
     * 
     * @Title: putInt
     * @param perference
     * @param keyString
     * @param integerVaule
     * @return void
     * @throws
     */
    public static void putInt(SharedPreferences perference, String keyString, Integer integerVaule) {
        perference.edit().putInt(keyString, integerVaule.intValue()).commit();
    }

    /**
     * 
     * @Title: getLong
     * @param perference
     * @param keyString
     * @param defaultLong
     * @return
     * @return Long
     * @throws
     */
    public static Long getLong(SharedPreferences perference, String keyString, Long defaultLong) {
        return Long.valueOf(perference.getLong(keyString, defaultLong.longValue()));
    }

    /**
     * 
     * @Title: putLong
     * @param perference
     * @param keyString
     * @param longVaule
     * @return void
     * @throws
     */
    public static void putLong(SharedPreferences perference, String keyString, Long longVaule) {
        perference.edit().putLong(keyString, longVaule.longValue()).commit();
    }

    /**
     * 
     * @Title: getString
     * @param perference
     * @param keyString
     * @param defaultString
     * @return
     * @return String
     * @throws
     */
    public static String getString(SharedPreferences perference, String keyString,
            String defaultString) {
        return perference.getString(keyString, defaultString);
    }

    /**
     * 
     * @Title: putString
     * @param perference
     * @param keyString
     * @param stringVaule
     * @return void
     * @throws
     */
    public static void putString(SharedPreferences perference, String keyString, String stringVaule) {
        perference.edit().putString(keyString, stringVaule).commit();
    }

    /**
     * 
     * @Title: getAllKeyValues
     * @param perference
     * @return
     * @return Map<String,?>
     * @throws
     */
    public static Map<String, ?> getAllKeyValues(SharedPreferences perference) {
        return perference.getAll();
    }

    /**
     * 
     * @Title: clearAllKeyValues
     * @param perference
     * @return void
     * @throws
     */
    public static void clearAllKeyValues(SharedPreferences perference) {
        perference.edit().clear().commit();
    }

    /**
     * 
     * @Title: containKey
     * @param perPreference
     * @param keyString
     * @return
     * @return boolean
     * @throws
     */
    public static boolean containKey(SharedPreferences perPreference, String keyString) {
        return perPreference.contains(keyString);
    }

    /**
     * 
     * @Title: removeKey
     * @param perference
     * @param keyString
     * @return void
     * @throws
     */
    public static void removeKey(SharedPreferences perference, String keyString) {
        perference.edit().remove(keyString).commit();
    }
}
