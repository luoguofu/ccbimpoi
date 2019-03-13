package com.weqia.wq.data;

public class ViewEnum {

    public enum ZanReplyEnum {
        INIT(-1, "init"), //
        TYPE_PJ_DYNAMIC(1, "pj_dynamic"), //
        TYPE_WEBO(2, "webo"), //
        TYPE_WC(3, "wc"), //
        TYPE_APPROVAL(4, "approval"), //
        TYPE_TASK(5, "task"), //
        ;

        private String strName;
        private int value;

        private ZanReplyEnum(int value, String strName) {
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

}
