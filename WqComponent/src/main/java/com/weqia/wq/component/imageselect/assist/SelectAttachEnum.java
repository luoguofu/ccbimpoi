package com.weqia.wq.component.imageselect.assist;

import com.weqia.wq.data.BaseData;

public class SelectAttachEnum extends BaseData {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public enum CropEnum {
        // 文档分类 1-文件 2-文档
        YES("1", "yes"), //
        NO("2", "no");

        private String strName;
        private String value;

        private CropEnum(String value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public String strName() {
            return strName;
        }

        public String value() {
            return value;
        }
    }

    public enum WithSourceEnum {
        // 文档分类 1-文件 2-文档
        YES("1", "yes"), //
        NO("2", "no");

        private String strName;
        private String value;

        private WithSourceEnum(String value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public String strName() {
            return strName;
        }

        public String value() {
            return value;
        }
    }

    public enum CropTypeEunm {
        // 文档分类 1-文件 2-文档
        USER_AVATAR(1, "me.jpg"), //
        CO_AVATAR(2, "co.jpg"), //
        WEBO_HEADER(3, "wbhead.jpg"),
        WC_HEADER(4, "wchead.jpg"),
        ;

        private String strName;
        private int value;

        private CropTypeEunm(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public String strName() {
            return strName;
        }

        public int value() {
            return value;
        }

        public static String valueOf(int value) {
            for (CropTypeEunm typeEnum : CropTypeEunm.values()) {
                if (typeEnum.value == value) {
                    return typeEnum.strName();
                }
            }
            return null;
        }
    }
}
