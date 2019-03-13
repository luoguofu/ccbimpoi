package com.weqia.wq.data;


import com.weqia.wq.R;
import com.weqia.wq.data.global.WeqiaApplication;

/**
 * 100以上表示是网络搜索，100一下是本地搜索
 * @author Berwin
 */
public enum SearchEnum {
    S_MEMBER(1, "搜索好友"), //, MemberData.class), //通讯录
    S_CONTACT(2, "搜索联系人"), //, EnterpriseContact.class), //
    S_TALKLIST(3, "搜索"), //, BaseData.class), //
    S_WORKER(4, "搜索工人"), //, WorkerData.class), //
    S_PROJECTMEMBER(5, "搜索项目联系人"), //, WorkerData.class), //

    S_NET_COMPANY(101, "请输入" + WeqiaApplication.getInstance().getResources().getString(R.string.co_co_wq_str)), // CompanyInfoData.class), //
    S_NET_DISCUSS(102, "搜索微会议"), //, DiscussData.class), //
    S_NET_MEMBER(103, "搜索朋友"), //, MemberData.class), //  朋友
    S_NET_MSG(104, "搜索聊天记录"), //, MsgData.class), //
    S_NET_DPROGRESS(105, "搜索会议记录"), //, DiscussProgress.class), //
    S_NET_POIINFO(106, "搜索附近位置"), //, PoiInfo.class), //
    S_NET_CUSTOMER(107, "搜索客户"), //, Customer.class), //
    S_NET_APPROVAL(108, "搜索审批"), //, ApprovalData.class), //
    ;

    private String hint;
    private Integer value;
    private Class<?> cls;

    SearchEnum(int value, String hint) {
        this.value = value;
        this.hint = hint;
        this.cls = cls;
    }

    public String getHint() {
        return hint;
    }

    public int value() {
        return value;
    }

    public Class<?> getCls() {
        return cls;
    }

    public static SearchEnum valueOf(int tmpValue) {
        for (SearchEnum typeEnum : SearchEnum.values()) {
            if (typeEnum.value() == tmpValue) {
                return typeEnum;
            }
        }
        return S_PROJECTMEMBER;
    }
}