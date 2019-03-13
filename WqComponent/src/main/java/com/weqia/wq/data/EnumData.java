
package com.weqia.wq.data;

import com.weqia.wq.service.EnumInterface;

public class EnumData extends BaseData {
    private static final long serialVersionUID = 1L;

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

//    public enum PartInMemberEnum {
//        PROJECT_MEMBER_EDIT(1, "项目人员编辑"),
//        //
//        ;
//        private int value;
//        private String strName;
//
//        PartInMemberEnum(int value, String strName) {
//            this.value = value;
//            this.strName = strName;
//        }
//
//        public int getValue() {
//            return value;
//        }
//
//        public String getStrName() {
//            return strName;
//        }
//
//        // value -> name
//        public static String getNameByValue(int value) {
//            for (PartInMemberEnum typeEnum : PartInMemberEnum.values()) {
//                if (typeEnum.getValue() == value) {
//                    return typeEnum.strName;
//                }
//            }
//            return null;
//        }
//    }

    public enum BindType {
        BINDED(1, "已绑定"), UNBIND(2, "未绑定");
        private String strName;
        private int value;

        private BindType(int value, String strName) {
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

    public enum ShareType {
        WEBO("1", "同事圈分享"), TASK("2", "任务进展分享"), WORK_CENTER("3", "工作圈分享"), MODE("4", "模型文件分享");
        private String strName;
        private String value;

        private ShareType(String value, String strName) {
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

    public enum QRResultType {
        // 文档分类 1-文件 2-文档
        CO(1, "企业二维码"), DISCUSS(2, "微会议二维码"), UER(3, "个人二维码"), PATROL(4, "电子巡更二维码"), CP_JOIN(5, "加入项目"),MODE(6,"模型二维码");
        //
        ;

        private String strName;
        private Integer value;

        QRResultType(Integer value, String strName) {
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

    public enum RefeshKey {
        PROJECT(2, "项目"), //
        CCPROJECT(3, "工程项目"), //
        PROJECT_DYNAMIC(4, "项目动态"), //
        PROJECT_TASK(5, "项目任务"), //
        PROJECT_ATTACH(6, "项目附件"), //
        PROJECT_MEM(18, "项目成员"), //
        CC_PROJECT_DYNAMIC(7, "工程项目动态"), //
        CC_PROJECT_TASK(8, "工程项目任务"), //
        CC_PROJECT_ATTACH(9, "工程项目任务"), //
        WEBO(10, "微博"), //
        USER_INFO(11, "用户信息"), //
        CONTACT(12, "通讯录"), //
        ENTERPRISE_INFO(13, "企业信息"), //
        MEMBER(15, "朋友"), //
        DISCUSS(14, "会议"), //
        DISCUSS_DETAIL(16, "会议详情"), //
        DISCUSS_BG(17, "会议背景"), //
        TASK(1, "任务"), //
        TASK_DYNAMIC(11, "任务动态"), //
        TASK_MEM(19, "任务成员"), //
        DEPARTMENT(20, "添加部门"), //
        TALK_STATE(21, "聊天状态"), //
        MSG_NEW(22, "新聊天或者会议消息"), //
        APPROVAL_STATUS(23, "审批状态"), //
        APPROVAL_MY_STATUS(24, "我的审批状态"), //
        APPROVAL_DETAIL(25, "刷新审批详情"), //
        CS_PROJECT_DYNAMIC(26, "BIM360咨询项目动态"), //
        CS_PROJECT(27, "BIM360咨询项目动态"), //
        CS_PROJECT_MAN(28, "BIM360咨询项目成员变动"), //
        CS_CONTRACT_MAN(29, "咨询合同成员变动"), //
        WORKER_NEX(26, "新工人"), //
        CS_CONTRACT_DYNAMIC(30, "咨询合同动态"), //
        MODE_LIST_REFRESH(31, "咨询合同动态"), //
        MODE_DYNAMIC_LIST_REFRESH(32, "模型列表刷新"), //
        FILE_LIST_REFRESH(33, "文件列表刷新"), //
        ;

        /**
         * 状态值
         */
        private int value;
        /**
         * 状态的描述
         */
        private String description;

        private RefeshKey(int value, String description) {
            this.value = value;
            this.description = description;
        }

        public int value() {
            return value;
        }

        public String description() {
            return description;
        }

        // 把整数映射到枚举值
        public static RefeshKey valueOf(int value) {
            for (RefeshKey typeEnum : RefeshKey.values()) {
                if (typeEnum.value() == value) {
                    return typeEnum;
                }
            }
            return null;
        }
    }

    public enum FontSizeType {
        SMALL(14, "小", 0.875f), NORMAL(16, "标准", 1.0f), BIG(18, "大", 1.125f), VERY_BIG(20, "超大", 1.25f), VERY_VERY_BIG(22, "特大", 1.375f);

        private String strName;
        private int value;
        private float portion;

        private FontSizeType(int value, String strName, float portion) {
            this.value = value;
            this.strName = strName;
            this.portion = portion;
        }

        public String strName() {
            return strName;
        }

        public int value() {
            return value;
        }

        public float portion() {
            return portion;
        }

        public static FontSizeType valueOf(int value) {
            for (FontSizeType typeEnum : FontSizeType.values()) {
                if (typeEnum.value == value) {
                    return typeEnum;
                }
            }
            return null;
        }
    }

    public enum DownloadType {
        // 文档分类 1-文件 2-文档
        WEQIA(1, "WEQIA"),

        REAL(2, "REAL");

        private String strName;
        private Integer value;

        private DownloadType(Integer value, String strName) {
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

    public enum CompanyPlugEnum {
        PLUG_IN(1, " PLUG_IN"), URL(2, "URL");

        private String strName;
        private Integer value;

        private CompanyPlugEnum(Integer value, String strName) {
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

    public enum MsgTypeEnum {
        SYSINFO(-1, "系统消息"), //
        TEXT(0, "文本"), //
        IMAGE(1, "图片"), //
        VOICE(2, "语音"), //
        VIDEO(3, "视频"), //
        LOCATION(4, "位置"), //
        FILE(5, "文件"), //
        LINK(6, "链接"), //
        READ(7, "通告"), //
        RED_PACKET(8, "红包"), //
        BACK_MSG(10, "撤回消息"), //
        BUSINESS_CARD(11, "个人名片"), //
        ;
        private String strName;
        private int value;

        private MsgTypeEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public String strName() {
            return strName;

        }

        public int value() {
            return value;
        }

        // value -> name
        public static String getNameByValue(int value) {
            for (MsgTypeEnum typeEnum : MsgTypeEnum.values()) {
                if (typeEnum.value() == value) {
                    return typeEnum.strName;
                }
            }
            return null;
        }

        public static AttachType getVauleOf(int value) {
            if (value == MsgTypeEnum.TEXT.value) {
                return AttachType.NONE;
            } else if (value == MsgTypeEnum.IMAGE.value) {
                return AttachType.PICTURE;
            } else if (value == MsgTypeEnum.VOICE.value) {
                return AttachType.VOICE;
            } else if (value == MsgTypeEnum.VIDEO.value) {
                return AttachType.VIDEO;
            } else if (value == MsgTypeEnum.LOCATION.value) {
                return AttachType.NONE;
            } else if (value == MsgTypeEnum.FILE.value) {
                return AttachType.FILE;
            } else {
                return AttachType.NONE;
            }
        }
    }

    // 文件分类：1-图片 2-语音 3-视频 4-其他文件
    public enum AttachType {
        NONE(0, "无附件"), //
        PICTURE(1, "图片"), //
        VOICE(2, "语音"), //
        VIDEO(3, "视频"), //
        FILE(4, "其他文件"), //

        PICTURE_WITH_SOURCE(101, "原图"),//
        TWO_IMG_PATH(100, "图片二次地址");//

        private String strName;
        private int value;

        private AttachType(int value, String strName) {
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


    public enum HttpServer implements EnumInterface {

        /**
         * 测试XX weqia.chinacloudapp.cn weixun.chint.com
         */
        ZT_SERV(6, "weixun.chint.com"),
        /**
         * 预发布
         */
        SERV_PREPARE(3, "121.199.29.64"),
        /**
         * 正式环境
         */
        SERV_FORMAL(4, "120.55.139.243");

        private Integer value;
        private String strName;

        private HttpServer(int value, String strName) {
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

        public static HttpServer valueOf(int value) {
            for (HttpServer server : HttpServer.values()) {
                if (server.order() == value) {
                    return server;
                }
            }
            return null;
        }

    }


    public enum SureErrorCodeType implements EnumInterface {

        SYS_ERROR(-10, " 系统异常"), //
        PROJECT_NOT_OPEN(-636, " 项目模块限制"), //
        ENTERPRISES_COUNT_LIMIT(-950, " 企业数量限制");

        private Integer value;
        private String strName;

        private SureErrorCodeType(int value, String strName) {
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

    public enum ErrorCodeType implements EnumInterface {

        AUTH_MOBILE_SEND_LIMIT(-251, "请5分钟后，再次申请验证码"),

        USER_IS_LEAVE(-213, "您从当前企业退出或离职"),

        SIGN_VALID_ERROR(-19, "签名错误"),

        BO_TASK_OP_FAIL_IS_NOT_EXIST(-552, "记录不存在!"),
        /**
         *
         */
        NETWORK_ERROR(-10000, "网络连接不可用，请稍后重试"),

        NETWORK_ERROR_NEW(-10007, "网络连接不可用，请稍后重试"),
        /**
         *
         */
        RECEIVE_NOTHING(-10001, "没有返回数据"),

        /**
         *
         */
        ILLEGAL_DATA_FORMAT(-10002, "请检查私有云配置是否正确！"),
        SERVER_ERROR(-10003, "网络请求失败！"),

        /**
         *
         */
        RET_NULL(-10003, "没有ret类型值"),

        /**
         * 系统未知错误
         */
        CLIENT_SYS_ERROR(-10005, "系统未知错误"),

        /**
         * 服务器无法连接
         */
        CLIENT_NOT_CONNECT(-10006, "服务器无法连接"),

        /**
         *
         */
        REL_ILLEGAL(-10004, "ret不是数字");

        private Integer value;
        private String strName;

        private ErrorCodeType(int value, String strName) {
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

        public static ErrorCodeType valueOf(int value) {
            for (ErrorCodeType errorCodeType : ErrorCodeType.values()) {
                if (errorCodeType.order() == value) {
                    return errorCodeType;
                }
            }
            return null;
        }

    }

    public enum ImageThumbTypeEnums {

        /**
         * 原图
         */
        THUMB_BIG(1, "thumb_big", "原图"),
        /**
         * 中图
         */
        THUMB_MIDDLE(2, "thumb_middle", "中图,440*440"),
        /**
         * 小图
         */
        THUMB_SMALL(3, "thumb_small_240", "小图,220*220"),
        /**
         * 很小的图
         */
        THUMB_VERY_SMALL(4, "thumb_small_120", "很小的图,120*120"),;

        /**
         * 状态值
         */
        private int value;

        /**
         *
         */
        private String key;
        /**
         * 状态的描述
         */
        private String description;

        private ImageThumbTypeEnums(int value, String key, String description) {
            this.value = value;
            this.key = key;
            this.description = description;
        }

        public int value() {
            return value;
        }

        public String key() {
            return key;
        }

        public String description() {
            return description;
        }

        // 把整数映射到枚举值
        public static ImageThumbTypeEnums valueOf(int value) {
            for (ImageThumbTypeEnums typeEnum : ImageThumbTypeEnums.values()) {
                if (typeEnum.value() == value) {
                    return typeEnum;
                }
            }
            return null;
        }
    }


    /**
     * 数据表的数据状态
     *
     * @author Berwin
     */
    public enum DataStatusEnum {
        DRAFT(-2, "draft"),     //
        SENDIND(-1, "sending"),     //
        SEND_ERROR(0, "send_error"),    //
        SEND_SUCCESS(1, "send_success"),    //
        ;

        private String strName;
        private int value;

        private DataStatusEnum(int value, String strName) {
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

    // 发送失败0 发成功1 对方已经收到 2 对方已读 3 -1 发送中
    public enum MsgSendStatusEnum {

        SENDING(-1, ""), //
        ERROR(0, "发送失败"), //
        SUCCEED(1, "发成功"), //
        RECEIVED(2, "对方已经收到"), //
        READ(3, "对方已读"), //
        NONE(-2, "不显示"), //
        BACK(9, "系统消息（消息撤回）"); //

        private String strName;
        private int value;

        private MsgSendStatusEnum(int value, String strName) {
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

    //任务类型
    public enum TaskTypeEnum {
        SECURITYTASK(2, "安全任务"),
        QUALITYTASK(1, "质量任务"),
        PROGRESSTASK(3, "进度任务"),
        OTHERTASK(5, "其他任务");

        private String strName;
        private int value;

        private TaskTypeEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public String strName() {
            return strName;
        }

        public int value() {
            return value;
        }
        // 把整数映射到枚举值
        public static TaskTypeEnum valueOf(int value) {
            for (TaskTypeEnum typeEnum : TaskTypeEnum.values()) {
                if (typeEnum.value() == value) {
                    return typeEnum;
                }
            }
            return null;
        }
    }

}

