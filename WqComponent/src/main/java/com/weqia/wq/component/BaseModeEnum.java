package com.weqia.wq.component;

/**
 * Created by berwin on 2017/9/11.
 */

public enum BaseModeEnum {

    DB("1", "db"),
    NETREQ("2", "netreq"),
    MESSAGE("3", "msg"),
    ;

    private String value;
    private String str;

    private BaseModeEnum(String value, String str) {
        this.value = value;
        this.str = str;
    }

    public String value() {
        return value;
    }

    public String str() {
        return str;
    }

    public static BaseModeEnum valueOfMode(String value) {
        for (BaseModeEnum typeEnum : BaseModeEnum.values()) {
            if (typeEnum.value.equalsIgnoreCase(value)) {
                return typeEnum;
            }
        }
        return null;
    }
}
