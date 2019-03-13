package com.weqia.wq.data;

import com.weqia.wq.service.EnumInterface;

/**
 * Created by berwin on 2017/9/12.
 */

public enum ComponentReqEnum implements EnumInterface{

    REPORT_GETUI_CLIENTID(312, "report_getui_clientid"), //
    REPORTE_MSG_ARRIVED(210, "reporte_log"),    //上报消息已达
    CLEAN_GETUI_CLIENTID(313, "clean_getui_clientid"), //退出登录清空clientID
    GET_FILE_PREVIEW_URL(2400, "GET_FILE_PREVIEW_URL"), //
    APP_UPDATE(1551, "app_update"), //程序更新接口
    GET_SYSTIME(1550, "get_systime"), //
    GET_MSG_UNARRIVED_NEW(371, "get_msg_unarrived"),    //
    REPORT_MSG_ARRIVED(372, "report_msg_arrived"),  //
    UP_FILE(10000, "up_file"),  //
    FAST_UP_FILE(11002 , "文件秒传查询接口"),  //
    CONVERT_URL(3611, "获取转化地址"),    //
    UP_LOAD_FILE_SIZE(11000, "预占用接口"),  //
    UP_LOAD_FILE_SIZE_RELEASE(11001, "预占用接口释放"),    //
    QR_SCAN(2200, "QR_SCAN"), //
    /**
     * 上报ids 已读
     */
    REPORT_MSG_READ(373, "report_msg_read"),
    MODE_LIST(3603, "模型列表"),
    GET_PROJECT_MEMBER_POWER(5031,"获取项目分组成员权限信息"),
    COMPONENT_INFO(3620,"构件信息"),
    ;

    private Integer value;
    private String strName;

    private ComponentReqEnum(int value, String strName) {
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

    public static ComponentReqEnum valueOf(int value) {
        for (ComponentReqEnum requestType : ComponentReqEnum.values()) {
            if (requestType.order() == value) {
                return requestType;
            }
        }
        return null;
    }
}
