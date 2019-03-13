package com.weqia.wq.msg;

import android.widget.TextView;

import com.weqia.utils.StrUtil;
import com.weqia.utils.TimeUtils;
import com.weqia.utils.datastorage.db.sqlite.DbModel;
import com.weqia.utils.exception.CheckedExceptionHandler;
import com.weqia.wq.R;
import com.weqia.wq.component.activity.SharedTitleActivity;
import com.weqia.wq.component.db.WeqiaDbUtil;
import com.weqia.wq.component.utils.request.ResultEx;
import com.weqia.wq.component.utils.request.ServiceParams;
import com.weqia.wq.component.utils.request.ServiceRequester;
import com.weqia.wq.component.utils.request.UserService;
import com.weqia.wq.data.ComponentReqEnum;
import com.weqia.wq.data.base.SilenceData;
import com.weqia.wq.data.eventbus.RefreshEvent;
import com.weqia.wq.data.global.WeqiaApplication;
import com.weqia.wq.global.EnumDataTwo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiaomx on 2017/10/9.
 */

public class MsgUtils {

    //刷新项目列表动态图标小红点显示
    public static final String REFRESH_PROJECT_LIST_UNREAD_COUNT = "REFRESH_PROJECT_LIST_UNREAD_COUNT";

    public static void mcRead(int[] business_type, ArrayList<String> ids) {

        StringBuffer bType = new StringBuffer();
        if (business_type != null) {
            bType.append("business_type in ( ");
            for (int i = 0; i < business_type.length; i++) {
                if (i == business_type.length - 1) {
                    bType.append(business_type[i]).append(")");
                } else {
                    bType.append(business_type[i]).append(",");
                }
            }
        } else {
            bType.append(" 1 = 1 ");
        }
        if (StrUtil.listNotNull(ids)) {
            bType.append(" AND id in ( ");
            for (int i = 0; i < ids.size(); i++) {
                if (i == ids.size() - 1) {
                    bType.append("'").append(ids.get(i)).append("'").append(" )");
                } else {
                    bType.append("'").append(ids.get(i)).append("'").append(",");
                }
            }
        }
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (dbUtil != null) {
            dbUtil.readAllByWhere(MsgCenterData.class, bType.toString());
        }
    }

    public static void mcRead(int[] business_type, String pjId) {
//        StringBuffer bType = new StringBuffer();
//        if (business_type != null) {
//            bType.append("business_type in (");
//            for (int i = 0; i < business_type.length; i++) {
//                if (i == business_type.length - 1) {
//                    bType.append(business_type[i]).append(")");
//                } else {
//                    bType.append(business_type[i]).append(",");
//                }
//            }
//        } else {
//            bType.append(" 1 = 1 ");
//        }
//        if (StrUtil.notEmptyOrNull(pjId)) {
//            bType.append(" AND pjId = '" + pjId + "'");
//        }
//        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
//        if (dbUtil != null) {
//            dbUtil.readAllByWhere(MsgCenterData.class, bType.toString());
//        }
    }

    public static void mcRead(String supId) {
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (dbUtil != null) {
            StringBuffer readIds = new StringBuffer();
            List<MsgCenterData> list = new ArrayList<MsgCenterData>();
            if (supId.equals("readAll")) {
                list = dbUtil.findAllByWhere(MsgCenterData.class, "1=1");
                dbUtil.readAll(MsgCenterData.class);
            } else {
                list =
                        dbUtil.findAllByWhere(MsgCenterData.class, "readed = 1 AND supId = '"
                                + supId + "'");
                dbUtil.readAllByWhere(MsgCenterData.class, "readed = 1 AND supId = '" + supId + "'");
            }
            // 上报已读
            if (StrUtil.listNotNull(list)) {
                int size = list.size();
                for (int i = 0; i < list.size(); i++) {
                    if (i == size - 1) {
                        readIds.append(list.get(i).getSendNo());
                    } else {
                        readIds.append(list.get(i).getSendNo()).append(",");
                    }
                }
                netRead(readIds.toString());
            }
        }
    }

    public static void netRead(String readIds) {
        if (StrUtil.isEmptyOrNull(readIds)) {
            return;
        }
        ServiceParams serviceParams = new ServiceParams(ComponentReqEnum.REPORT_MSG_READ.order());
        serviceParams.put("sendNos", readIds);
        UserService.getDataFromServer(serviceParams, new ServiceRequester() {
            @Override
            public void onResult(ResultEx resultEx) {
            }
        });
    }

    public static void mcReadbBusinessType(int business_type) {
        mcReadbBusinessType(business_type, null);
    }


    public static void mcReadbBusinessType(int business_type, String pjId) {
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (dbUtil != null) {
            StringBuffer readIds = new StringBuffer();
            List<MsgCenterData> list = new ArrayList<MsgCenterData>();
            String where = "readed = 1 AND business_type = " + business_type;
            if (StrUtil.notEmptyOrNull(pjId)) {
                where = where + " AND pjId = '" + pjId + "'";
            }
            list =
                    dbUtil.findAllByWhere(MsgCenterData.class, where);
            dbUtil.readAllByWhere(MsgCenterData.class, where);
            if (StrUtil.listNotNull(list)) {
                int size = list.size();
                for (int i = 0; i < list.size(); i++) {
                    if (i == size - 1) {
                        readIds.append(list.get(i).getSendNo());
                    } else {
                        readIds.append(list.get(i).getSendNo()).append(",");
                    }
                }
                netRead(readIds.toString());
            }
        }
    }

    public static void mcReadbBusinessType(int business_type, String pjId, String superId) {
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (dbUtil != null) {
            StringBuffer readIds = new StringBuffer();
            List<MsgCenterData> list = new ArrayList<MsgCenterData>();
            String where = "readed = 1 AND business_type = " + business_type;
            if (StrUtil.notEmptyOrNull(pjId)) {
                where = where + " AND pjId = '" + pjId + "'";
            }
            if (StrUtil.notEmptyOrNull(superId)) {
                where = where + " AND supId = '" + superId + "'";
            }
            list =
                    dbUtil.findAllByWhere(MsgCenterData.class, where);
            dbUtil.readAllByWhere(MsgCenterData.class, where);
            if (StrUtil.listNotNull(list)) {
                int size = list.size();
                for (int i = 0; i < list.size(); i++) {
                    if (i == size - 1) {
                        readIds.append(list.get(i).getSendNo());
                    } else {
                        readIds.append(list.get(i).getSendNo()).append(",");
                    }
                }
                netRead(readIds.toString());
            }
        }
    }

    // 移除列表
    public static void removeTalkList(TalkListData data) {
        try {
            WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
            if (dbUtil != null) {
                if (data.getBusiness_type() == EnumDataTwo.MsgBusinessType.MSG_CENTER.value()) {
                    return;
                }
                if (StrUtil.notEmptyOrNull(data.getBusiness_id())) {
                    // 去掉小红点
                    mcRead(data.getBusiness_id());
                }
                if (data.getBusiness_type() == EnumDataTwo.MsgBusinessType.TASK
                        .value()
                        || data.getBusiness_type() == EnumDataTwo.MsgBusinessType.PROJECT_INFO
                        .value()
                        ) {
                    // 删除二级
                    dbUtil.deleteByWhere(TalkListData.class,
                            "business_id=" + data.getBusiness_id() + " and  pjId = '"
                                    + data.getPjId() + "'");
                    mcRead(data.getBusiness_id());
                } else if (data.getBusiness_type() == EnumDataTwo.MsgBusinessType.PROJECT_INFO
                        .value()) {
                    // 删除一级
                    dbUtil.deleteByWhere(TalkListData.class,
                            "business_type=" + data.getBusiness_type() + " and  pjId = '"
                                    + data.getPjId() + "'");
                    mcReadbBusinessType(data.getBusiness_type());
                }
            }
            dbUtil.deleteById(TalkListData.class, data.getId());
        } catch (Exception e) {
            CheckedExceptionHandler.handleException(e);
        }

    }

    // 删除会议等
    public static void deleteTalkList(String business_id) {
        if (StrUtil.isEmptyOrNull(business_id)) {
            return;
        }
        try {
            WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
            if (dbUtil != null) {
                mcRead(business_id);
                dbUtil.deleteByWhere(TalkListData.class, "business_id = '" + business_id + "'");
            }
        } catch (Exception e) {
            CheckedExceptionHandler.handleException(e);
        }

    }


    public static void upadteTalkList(String business_id, int business_type, String title) {

        if (StrUtil.isEmptyOrNull(business_id) || StrUtil.isEmptyOrNull(title)) {
            return;
        }
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (dbUtil != null) {
            TalkListData tmp =
                    dbUtil.findByWhere(TalkListData.class, "business_id = '" + business_id
                            + "' and business_type = " + business_type);
            if (tmp != null && !title.equals(tmp.getTitle())) {
                tmp.setTitle(title);
                upadteTalkList(tmp);
            }
        }
    }

    public static boolean hasTalkList(int business_type, String pjId) {
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (dbUtil == null) {
            return false;
        }
        TalkListData tmp =
                dbUtil.findByWhere(TalkListData.class, "business_type = " + business_type
                        + " and level = " + EnumDataTwo.MsgListLevelType.ONE.value() + " and pjId = '" + pjId + "'");
        if (tmp != null && StrUtil.notEmptyOrNull(tmp.getPjId())) {
            return true;
        }
        return false;
    }


    public static TalkListData getTalkListByBusinessId(int business_type, String business_id) {
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (dbUtil == null) {
            return null;
        }
        TalkListData tmp = dbUtil.findByWhere(TalkListData.class, "business_type = " + business_type + " and level = " + EnumDataTwo.MsgListLevelType.ONE.value() + " and business_id = '" + business_id + "'");
        if (tmp != null) {
            return tmp;
        }
        return null;
    }

    // 如果有指定业务的一级列表，则更新一级消息列表；没有指定业务的一级列表，则生成相应的一级列表数据
    public static void upadteTalkListOne(TalkListData data, boolean isUpdateTime) {
        int business_type = data.getBusiness_type();
        String content = data.getContent();
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (dbUtil == null) {
            return;
        }
        if (business_type == EnumDataTwo.MsgBusinessType.TASK.value()
                || business_type == EnumDataTwo.MsgBusinessType.PROJECT_INFO.value()
                || business_type == EnumDataTwo.MsgBusinessType.DISCUSS.value()
                || business_type == EnumDataTwo.MsgBusinessType.MC_MEMBER.value()
                || business_type == EnumDataTwo.MsgBusinessType.MC_NEW_PROJECT_MAN.value()
                || business_type == EnumDataTwo.MsgBusinessType.MODE_FILE.value()) {
            TalkListData tmp = null;
            if (business_type == EnumDataTwo.MsgBusinessType.MODE_FILE.value()) {
                tmp = dbUtil.findByWhere(TalkListData.class, "business_type = " + business_type + " and level = " + EnumDataTwo.MsgListLevelType.ONE.value() + " and pjId = '" + data.getPjId() + "' and business_id ='" + data.getBusiness_id() + "'");
            } else {
                if (business_type == EnumDataTwo.MsgBusinessType.TASK.value() || business_type == EnumDataTwo.MsgBusinessType.PROJECT_INFO.value()) {
                    tmp = dbUtil.findByWhere(TalkListData.class, "business_type = " + business_type + " and level = " + EnumDataTwo.MsgListLevelType.ONE.value() + "");
                } else {
                    tmp = dbUtil.findByWhere(TalkListData.class, "business_type = " + business_type + " and level = " + EnumDataTwo.MsgListLevelType.ONE.value() + " and pjId = '" + data.getPjId() + "'");
                }
            }
            if (tmp != null && StrUtil.notEmptyOrNull(tmp.getPjId())) {
                if (StrUtil.isEmptyOrNull(content)) {
                    return;
                }
                //已经有指定业务的一级列表存在，直接更新列表的相关信息
                if (business_type == EnumDataTwo.MsgBusinessType.DISCUSS.value()
                        || business_type == EnumDataTwo.MsgBusinessType.MODE_FILE.value()) {
                    //配置动态一级列表的标题
                    if (StrUtil.notEmptyOrNull(data.getTitle())) {
                        tmp.setTitle(data.getTitle());
                    }
                }
                if (StrUtil.notEmptyOrNull(data.getMid())) {
                    tmp.setMid(data.getMid());
                }
                tmp.setAvatar(data.getAvatar());
                tmp.setContent(content);
//                if (business_type == EnumDataTwo.MsgBusinessType.TASK.value()
//                        || business_type == EnumDataTwo.MsgBusinessType.PROJECT_INFO.value()) {
//
//                }else {
//
//                }
                tmp.setBusiness_id(data.getBusiness_id());
                if (isUpdateTime) {
                    tmp.setTime(TimeUtils.getLongTime());
                }
                dbUtil.update(tmp);
            } else {
                if (StrUtil.isEmptyOrNull(data.getPjId())) {
                    return;
                }
                //没有指定业务的一级列表存在，生成相应的一级列表信息
                TalkListData talkListData = new TalkListData();
                if (business_type == EnumDataTwo.MsgBusinessType.DISCUSS.value()
                        || business_type == EnumDataTwo.MsgBusinessType.MC_MEMBER.value()
                        || business_type == EnumDataTwo.MsgBusinessType.MC_NEW_PROJECT_MAN.value()
                        || business_type == EnumDataTwo.MsgBusinessType.MODE_FILE.value()) {
                    //配置动态一级列表的标题
                    if (StrUtil.notEmptyOrNull(data.getTitle())) {
                        talkListData.setTitle(data.getTitle());
                    }
                } else {
                    talkListData.setTitle(EnumDataTwo.MsgBusinessType.valueOf(business_type).strName());
                }
                talkListData.setContent(content);
                talkListData.setBusiness_type(business_type);
                talkListData.setSort_number(data.getSort_number());
                talkListData.setBusiness_id(data.getBusiness_id());
                talkListData.setLevel(EnumDataTwo.MsgListLevelType.ONE.value());
                talkListData.setTime(TimeUtils.getLongTime());
                talkListData.setPjId(data.getPjId());
                //直接存入数据库，直接检测更新已存在的数据
                dbUtil.save(talkListData);
            }
        }
    }

    public static void upadteTalkListOne(TalkListData data) {
        upadteTalkListOne(data, true);
    }

    // 更新消息列表 公告 单聊 群聊
    public static void upadteTalkList(TalkListData data) {
        upadteTalkListOne(data, true);
        int business_type = data.getBusiness_type();
        if (business_type == EnumDataTwo.MsgBusinessType.TASK.value() || business_type == EnumDataTwo.MsgBusinessType.PROJECT_INFO.value()) {
            if (data != null) {
                if (StrUtil.isEmptyOrNull(data.getPjId())) {
                    return;
                }
                WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
                if (dbUtil != null) {
                    TalkListData tmp =
                            dbUtil.findByWhere(TalkListData.class, "business_id = '" + data.getBusiness_id() + "' and business_type = " + data.getBusiness_type() + " and level = 2 ");
                    if (tmp != null) {
                        data.setId(tmp.getId());
                        data.setSort_number(tmp.getSort_number());
                        if (StrUtil.notEmptyOrNull(tmp.getTitle())) {
                            data.setTitle(tmp.getTitle());
                        }
                        if (StrUtil.notEmptyOrNull(tmp.getCoId())) {
                            data.setCoId(tmp.getCoId());
                        }
                        dbUtil.update(data);
                    } else {
                        dbUtil.save(data, false);
                    }
                }
            }
        }

        /**
         *刷新消息列表
         */
        EventBus.getDefault().post(new RefreshEvent("upadteTalkList"));
    }


    // 查询聊天列表
    public static TalkListData findTalkListByBId(String business_id, int business_type) {
        return findTalkListByBId(business_id, business_type, -1);
    }

    // 查询聊天列表
    public static TalkListData findTalkListByBId(String business_id, int business_type, int level) {
        TalkListData tmp = null;
        if (StrUtil.notEmptyOrNull(business_id)) {
            WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
            if (dbUtil != null) {
                if (level == -1) {
                    tmp =
                            dbUtil.findByWhere(TalkListData.class, "business_id = '" + business_id
                                    + "' and business_type = " + business_type);
                } else {
                    tmp =
                            dbUtil.findByWhere(TalkListData.class, "business_id = '" + business_id
                                    + "' and business_type = " + business_type + " and level = " + level);
                }

            }
        }
        return tmp;
    }

    // 置顶(取消置顶)
    public static void topList(int id, boolean flag) {
        try {
            WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
            if (dbUtil != null) {
                if (flag) {
                    int maxSortNumber = dbUtil.findDbModelBySQL("select max(sort_number+0) as max_sort  from talk_list where sort_number <> " + 10000).getInt("max_sort");
                    dbUtil.updateBySql(TalkListData.class, "sort_number = '" + (maxSortNumber + 1) + "' where id = " + id);
                } else {
                    dbUtil.updateBySql(TalkListData.class, "sort_number = '" + 0 + "' where id = " + id);
                }
            }
        } catch (Exception e) {
            CheckedExceptionHandler.handleException(e);
        }
    }

    public static void readAllList(TalkListData data, boolean isReaded, boolean isNoPjId) {
        try {
            String business_id = data.getBusiness_id();
            int business_type = data.getBusiness_type();
            String pjId = isNoPjId ? null : data.getPjId();
            WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
            if (dbUtil != null) {
                if (business_type == EnumDataTwo.MsgBusinessType.DISCUSS.value()
                        || business_type == EnumDataTwo.MsgBusinessType.TASK.value()
                        || business_type == EnumDataTwo.MsgBusinessType.MODE_FILE.value()
                        || business_type == EnumDataTwo.MsgBusinessType.MC_MEMBER.value()
                        || business_type == EnumDataTwo.MsgBusinessType.MC_NEW_PROJECT_MAN.value()
                        || business_type == EnumDataTwo.MsgBusinessType.PROJECT_INFO.value()) {
                    if (isReaded) {
                        MsgCenterData tmp = new MsgCenterData(business_type, business_id, data.getPjId());
                        dbUtil.save(tmp, true);
                    } else {
                        String where = "business_type =" + business_type + " AND  sendNo IS NULL";
                        if (!isNoPjId) {
                            where = where + " AND  pjId = '" + pjId + "'";
                        }
                        if (business_type == EnumDataTwo.MsgBusinessType.DISCUSS.value()) {
                            WeqiaApplication.getInstance().getDbUtil().deleteByWhere(MsgCenterData.class, where);
                            mcRead(data.getBusiness_id());
                        } else if (business_type == EnumDataTwo.MsgBusinessType.MODE_FILE.value() || business_type == EnumDataTwo.MsgBusinessType.TASK.value()) {
                            where = where + " AND  supId = '" + business_id + "'";
                            WeqiaApplication.getInstance().getDbUtil().deleteByWhere(MsgCenterData.class, where);
                            mcReadbBusinessType(business_type, pjId, business_id);
                        } else {
                            WeqiaApplication.getInstance().getDbUtil().deleteByWhere(MsgCenterData.class, where);
                            mcReadbBusinessType(data.getBusiness_type(), pjId);
                        }
                    }
                }
            }
        } catch (Exception e) {
            CheckedExceptionHandler.handleException(e);
        }

    }

    public static void readAllLevelTwoList(TalkListData data) {
        try {
            // String business_id = data.getBusiness_id();
            int business_type = data.getBusiness_type();
            WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
            if (dbUtil != null) {
                if (business_type == EnumDataTwo.MsgBusinessType.TASK.value()
                        || business_type == EnumDataTwo.MsgBusinessType.PROJECT_INFO.value()) {
                    mcRead(data.getBusiness_id());
                }
            }
        } catch (Exception e) {
            CheckedExceptionHandler.handleException(e);
        }

    }


    //获取一种业务类型的未读消息
    public static int msgListUnReadCount(int business_type, String business_id, String pjId) {
        return mcCountBusinessType(true, new int[]{business_type}, StrUtil.notEmptyOrNull(business_id) ? "supId = '" + business_id + "'" + " and pjId = '" + pjId + "'" : "pjId = '" + pjId + "'");
    }

    public static boolean bSilence(String business_id) {
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (dbUtil == null) {
            return false;
        }
        SilenceData silenceData = dbUtil.findById(business_id, SilenceData.class);
        if (silenceData != null) {
            if (silenceData.getVoiceType().intValue() == EnumDataTwo.VoiceTypeEnum.SILENCE.value()
                    .intValue()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param where 特定的查询条件（例如 查询查询一个项目的所有业务的未读消息）
     * @return 获取所有业务类别未读消息数量
     */
    public static int getAllUnReadCount(String where) {
        int ret = 0;
        ret = mcCountBusinessType(true, new int[]{EnumDataTwo.MsgBusinessType.PROJECT_INFO.value(),
                EnumDataTwo.MsgBusinessType.MODE_FILE.value(),
                EnumDataTwo.MsgBusinessType.TASK.value(),
                EnumDataTwo.MsgBusinessType.DISCUSS.value(),
        }, where);
        return ret;
    }

    /**
     * @param in            是否包含传入的业务类别
     * @param business_type 业务类别数组
     * @param where         具体的查询语句，可为空
     * @return 未读消息数量
     */
    public static Integer mcCountBusinessType(boolean in, int[] business_type, String where) {
        StringBuffer sqlInfo = new StringBuffer();
        if (business_type != null) {
            if (in) {
                sqlInfo.append("business_type in");
            } else {
                sqlInfo.append("business_type not in");
            }
            sqlInfo.append(" ( ");
            for (int i = 0; i < business_type.length; i++) {
                if (i == business_type.length - 1) {
                    sqlInfo.append(business_type[i]).append(")");
                } else {
                    sqlInfo.append(business_type[i]).append(",");
                }
            }
        } else {
            sqlInfo.append(" 1 = 1 ");
        }
        if (StrUtil.notEmptyOrNull(where)) {
            sqlInfo.append(" AND ").append(where);
        }
        Integer count = 0;
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (dbUtil != null) {
            String sql = "SELECT ifnull(count(*),0)  as count FROM msg_center WHERE " + sqlInfo.toString() + " AND readed=1 ";
            DbModel dbModel = dbUtil.findDbModelBySQL(sql);
//            if (L.D) L.e("未读消息数据SQL查询语句：" + sql);
            if (dbModel != null) {
                try {
                    count = dbModel.getInt("count");
                } catch (Exception e) {
                    CheckedExceptionHandler.handleException(e);
                }
            }
        }
        return count;
    }


    public static void toPageError(SharedTitleActivity ctx, String errorMsgStr) {
        ctx.setContentView(R.layout.ac_page_error);
        ctx.sharedTitleView.initTopBanner("提示");
        TextView errorMsg = (TextView) ctx.findViewById(R.id.errorMsg);
        if (StrUtil.notEmptyOrNull(errorMsgStr)) {
            errorMsg.setText(errorMsgStr);
        }
    }

}
