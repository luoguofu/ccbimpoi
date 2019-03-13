package com.weqia.utils.http.okgo.cache;

public enum CacheMode {
    /** 按照HTTP协议的默认缓存规则，例如有304响应头时缓存 */
    DEFAULT(0, "DEFAULT"),

    /** 不使用缓存 */
    NO_CACHE(1, "NO_CACHE"),

    /** 请求网络失败后，读取缓存 */
    REQUEST_FAILED_READ_CACHE(2, "REQUEST_FAILED_READ_CACHE"),

    /** 如果缓存不存在才请求网络，否则使用缓存 */
    IF_NONE_CACHE_REQUEST(3, "IF_NONE_CACHE_REQUEST"),

    /** 先使用缓存，不管是否存在，仍然请求网络 */
    FIRST_CACHE_THEN_REQUEST(4, "FIRST_CACHE_THEN_REQUEST"),
    ;

    private String strName;
    private int value;

    private CacheMode(int value, String strName) {
        this.strName = strName;
        this.value = value;
    }

    public String strName() {
        return strName;
    }

    public int value() {
        return value;
    }

    public static CacheMode valueOfStr(String strName) {
        for (CacheMode typeEnum : CacheMode.values()) {
            if (typeEnum.strName.equalsIgnoreCase(strName)) {
                return typeEnum;
            }
        }
        return null;
    }
}
