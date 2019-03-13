package com.weqia.wq.component;

import com.weqia.wq.component.utils.request.ServiceParams;

import java.util.HashMap;
import java.util.Map;

public class ReqMap {
    private static Map<Integer, Class<? extends ServiceParams>> serviceParamMap;

    public static Class<? extends ServiceParams> getServiceParamCls(int itype) {
        if (serviceParamMap == null) {
            initReqMap();
        }
        return serviceParamMap.get(itype);
    }

    private static void initReqMap() {
        serviceParamMap = new HashMap<>();
//        serviceParamMap.put(RequestType.WEBO_ADD.order(), WeboParams.class);
//        serviceParamMap.put(RequestType.WC_ADD.order(), WcParams.class);
//        serviceParamMap.put(RequestType.VISIT_ADD.order(), VisitParams.class);
//        serviceParamMap.put(RequestType.CHECKIN_ADD.order(), CheckInParams.class);
//        serviceParamMap.put(RequestType.PUBLISH_TASK.order(), TaskDataParams.class);
//        serviceParamMap.put(RequestType.MODIFY_TASK.order(), TaskDataParams.class);
//        serviceParamMap.put(RequestType.ADD_TASK_PROGRESS.order(), TaskProgressParams.class);
//        serviceParamMap.put(RequestType.PUBLISH_DISCUSS.order(), DiscussDataParams.class);
//        serviceParamMap.put(RequestType.EDIT_DISCUSS.order(), DiscussDataParams.class);
//        serviceParamMap.put(RequestType.PK_ADD.order(), PkParams.class);
//        serviceParamMap.put(RequestType.PUBLISH_NOTICE.order(), NoticeParams.class);
//        serviceParamMap.put(RequestType.PROJECT_REPLY.order(), ProjectProgressParams.class);
//        serviceParamMap.put(RequestType.CC_PROJECT_REPLY.order(), ProjectProgressParams.class);
//        serviceParamMap.put(RequestType.TALK.order(), TalkParams.class);
//        serviceParamMap.put(RequestType.CC_PROJECT_UP_FILE_ADD.order(), UpAttachParams.class);
//        serviceParamMap.put(RequestType.PROJECT_UP_FILE_ADD.order(), UpAttachParams.class);
//        serviceParamMap.put(RequestType.TASK_UP_FILE_ADD.order(), UpAttachParams.class);
//        serviceParamMap.put(RequestType.FILE_ADD.order(), UpAttachParams.class);
//        serviceParamMap.put(RequestType.REPLY_DISCUSS.order(), DiscussProgressParams.class);
//        serviceParamMap.put(RequestType.APPROVAL_ADD.order(), ApprovalParam.class);
    }
}
