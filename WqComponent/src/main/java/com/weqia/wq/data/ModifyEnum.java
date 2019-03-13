package com.weqia.wq.data;

import com.weqia.wq.service.EnumInterface;

public enum ModifyEnum implements EnumInterface {
        /**
         * 新增
         */
        ITEM_NEW(1, "新增"),
        /**
         * 修改
         */
        ITEM_MODIFY(2, "修改"),
        /**
         * 删除
         */
        ITEM_DELETE(3, "删除"),
        /**
         * 临时消息
         */
        ITEM_TEMP(4, "临时消息");

        private Integer value;
        private String strName;

        private ModifyEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        @Override
        public String strName() {
            return strName;
        }

        @Override
        public int order() {
            return value;
        }
    }