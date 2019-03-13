package com.weqia.wq.data;


public class MenuEnumData {

    public enum TalkMenuEnum {
        NONE(-1, ""),
        COPY(1, "复制"),
        SHARE_WEBO(2, "分享到同事圈"),
        NEW_TASK(3, "新建任务"),
        DELETE(4, "删除"),
        SHARE_TO(5, "分享到"),
        TRANSMIT(6, "转发"),
        MODE_SPEAKER(7, "使用扬声器模式"),
        MODE_HANDSET(8, "使用听筒模式"),
        FOCUS(9, "关注"),
        CANCEL_FOCUS(10, "取消关注"),
		MSG_BACK(11, "撤回"), //
        ;

        private String strName;
        private int value;

        private TalkMenuEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }


        public static TalkMenuEnum nameOf(String strName) {
            for (TalkMenuEnum typeEnum : TalkMenuEnum.values()) {
                if (typeEnum.strName().equals(strName)) {
                    return typeEnum;
                }
            }
            return null;
        }

        public static TalkMenuEnum valueOf(int value) {
            for (TalkMenuEnum typeEnum : TalkMenuEnum.values()) {
                if (typeEnum.value == value) {
                    return typeEnum;
                }
            }
            return null;
        }


        public String strName() {
            return strName;
        }

        public int value() {
            return value;
        }
    }
}
