package com.weqia.wq.component.db;

/**
 * Created by berwin on 2017/9/11.
 */

public enum DBOpEnum {

    CRATETABLE("1", "createTable"),
    CLEARTABLE("2", "cleartable"),
    CLEARBYCO("3", "clearbyco");

    private String value;
    private String str;

    private DBOpEnum(String value, String str) {
        this.value = value;
        this.str = str;
    }

    public String value() {
        return value;
    }

    public String str() {
        return str;
    }

    public static DBOpEnum valueOfOp(String value) {
        for (DBOpEnum typeEnum : DBOpEnum.values()) {
            if (typeEnum.value.equalsIgnoreCase(value)) {
                return typeEnum;
            }
        }
        return null;
    }
}
