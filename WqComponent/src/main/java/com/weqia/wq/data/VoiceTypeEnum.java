package com.weqia.wq.data;

public enum VoiceTypeEnum {
        VOICE(1, " VOICE"), SILENCE(2, "SILENCE");
        // //声音类型 1有声音 2无声音
        private String strName;
        private Integer value;

        private VoiceTypeEnum(Integer value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public String strName() {
            return strName;
        }

        public Integer value() {
            return value;
        }
    }