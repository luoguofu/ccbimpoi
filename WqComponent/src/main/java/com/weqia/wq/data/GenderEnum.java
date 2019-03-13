package com.weqia.wq.data;

import com.weqia.wq.service.EnumInterface;

public enum GenderEnum implements EnumInterface {

        /**
         *
         */
        MALE(1, "男"),
        /**
         *
         */
        FEMALE(2, "女"),
        /**
         *
         */
        UNKOWN(3, "未知");

        private Integer value;
        private String strName;

        private GenderEnum(int value, String strName) {
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

        public static GenderEnum valueOf(int value) {
            for (GenderEnum gender : GenderEnum.values()) {
                if (gender.order() == value) {
                    return gender;
                }
            }
            return null;
        }
    }
