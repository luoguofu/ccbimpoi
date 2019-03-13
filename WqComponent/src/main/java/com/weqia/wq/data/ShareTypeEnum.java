package com.weqia.wq.data;

public enum ShareTypeEnum {


        WEBO(1, "同事圈-工作圈-》规则不一致"),
        OTHER(2, "其他");


        private String strName;
        private int value;

        private ShareTypeEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public String strName() {
            return strName;
        }

        public int value() {
            return value;
        }

    }