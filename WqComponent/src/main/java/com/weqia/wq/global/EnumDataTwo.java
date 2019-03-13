package com.weqia.wq.global;

/**
 * Created by MX on 4/20 0020.
 */
public class EnumDataTwo {

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

    // 任务级别
    public enum TaskLevel {
        TK_LEVEL_COMMON(1, "普通"), //
        TK_LEVEL_URGENT(2, "紧急");
        private String nameString;
        private int value;

        private TaskLevel(int value, String nameString) {
            this.value = value;
            this.nameString = nameString;

        }

        public String strName() {
            return nameString;
        }

        public int value() {
            return value;
        }


        public static TaskLevel valueOf(int value) {
            for (TaskLevel typeEnum : TaskLevel.values()) {
                if (typeEnum.value == value) {
                    return typeEnum;
                }
            }
            return TaskLevel.TK_LEVEL_COMMON;
        }

    }


    public enum MsgBusinessType {
        TASK(1, "任务"), //
        PROJECT(2, "项目"), //
        PROJECT_INFO(4, "项目信息"), //
        TALK(7, "聊天"), //
        DISCUSS(8, "会议"), //
        MSG_CENTER(6, "消息"), //
        MODE_FILE(32, "模型文件进展"),
        MC_MEMBER(12, "好友"), //
        MC_NEW_PROJECT_MAN(33, "项目新成员审核"), //


        //暂时无用
        NONE(0, "一级列表"), //
        CC_PROJECT(3, "咨询项目"), //
        INVITE(5, "邀请"), //
        MC_OUT_MSG(9, "外部消息"), //
        MC_WEBO(10, "微博"), //
        MC_DISCUSS(11, "会议申请"), //

        MC_WORK_CENTER(15, "工作圈"), //
        MC_ENTERPRISE_CONTACT(16, "企业联系人"), //
        MC_VISIT(17, "客户"), //
        APPROVAL(18, "审批"), //
        PUNCH_RANK(19, "考勤"), //
        HB_MSG(20, "我的钱包"), //
        HB_REMOVE_MSG(21, "提现发起"), //
        HB_COME_MSG(22, "提现到帐"), //
        FOLD_ADMIN(21, "文件"), //
        CS_PROJECT(23, "业务动态"), //
        CS_NEEDTODO(24, "业务待办"),
        CS_PROJECT_RECORD(25, "业务动态-成果接受"), //
        CS_PROJECT_PLAN(26, "业务动态-计划提交"), //
        MC_WORKER(27, "工人"),
        CS_CONTRACT_DYNAMIC(28, "咨询合同动态"), //
        CS_CONTRACT(29, "咨询合同"), //
        SALE(30, "销售单"), //
        INSPECT_ADD(31, "易检"),
        //
        ;
        private String strName;
        private int value;

        private MsgBusinessType(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public String strName() {
            return strName;
        }

        public int value() {
            return value;
        }

        public static MsgBusinessType valueOf(int value) {
            for (MsgBusinessType typeEnum : MsgBusinessType.values()) {
                if (typeEnum.value == value) {
                    return typeEnum;
                }
            }
            return null;
        }
    }

    public enum MsgListLevelType {
        ONE(1, "一级列表"), //
        TWO(2, "二级列表");
        private String strName;
        private int value;

        private MsgListLevelType(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public String strName() {
            return strName;
        }

        public int value() {
            return value;
        }

        public static MsgBusinessType valueOf(int value) {
            for (MsgBusinessType typeEnum : MsgBusinessType.values()) {
                if (typeEnum.value == value) {
                    return typeEnum;
                }
            }
            return null;
        }
    }


}
