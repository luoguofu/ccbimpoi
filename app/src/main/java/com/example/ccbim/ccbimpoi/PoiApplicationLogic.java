package com.example.ccbim.ccbimpoi;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.TimeUtils;
import com.weqia.wq.ComponentApplicationLogic;
import com.weqia.wq.component.db.WeqiaDbUtil;
import com.weqia.wq.component.utils.DialogUtil;
import com.weqia.wq.component.utils.request.ResultEx;
import com.weqia.wq.component.utils.request.ServiceParams;
import com.weqia.wq.component.utils.request.ServiceRequester;
import com.weqia.wq.component.utils.request.UserService;
import com.weqia.wq.data.AttachmentData;
import com.weqia.wq.data.BaseData;
import com.weqia.wq.data.EnumData;
import com.weqia.wq.data.ModifyEnum;
import com.weqia.wq.data.MsgWarnData;
import com.weqia.wq.data.WPf;
import com.weqia.wq.data.WPfMid;
import com.weqia.wq.data.eventbus.RefreshEvent;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.data.global.Hks;
import com.weqia.wq.data.global.WeqiaApplication;
import com.weqia.wq.data.newdemand.AppManager;
import com.weqia.wq.data.newdemand.MemberProjectPower;
import com.weqia.wq.global.EnumDataTwo;
import com.weqia.wq.msg.MsgCenterData;
import com.weqia.wq.msg.MsgUtils;
import com.weqia.wq.msg.TalkListData;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;



public class PoiApplicationLogic extends ComponentApplicationLogic {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void createTable() {
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (dbUtil == null) {
            L.e("dbUtil为空，---");
            return;
        }
        L.e("主创建表");

    }

    @Override
    protected void clearTable() {
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (dbUtil == null) {
            L.e("dbUtil为空，---");
            return;
        }
        L.i("main模块清理数据库");

    }

    @Override
    protected void clearCoTable(String coId) {
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (dbUtil == null) {
            L.e("dbUtil为空，---");
            return;
        }
        L.i("main模块coId = [" + coId + "]清理数据库");

    }

    @Override
    public boolean onSendSuccessOp(String serviceParamsStr, String sendDataStr, String resultExStr) {

        return false;
    }

    @Override
    public boolean onDownloadFileOp(String attachmentDataStr, int status) {

        return false;
    }

    @Override
    public boolean onMessageOp(int type, String param, String warn) {
        return false;
    }

    /**
     * 推送相关处理逻辑
     * @param pushType 数据操作类别（增删改）
     * @param msgType  接口号
     * @param baseData 传递的数据信息
     * @param warnData 提示消息数据
     */
    public void toModifyDataAction(int pushType, int msgType, BaseData baseData, MsgWarnData warnData) {
        if (pushType == ModifyEnum.ITEM_NEW.order()) {
            getNewMessage(msgType, baseData, warnData);
        } else if (pushType == ModifyEnum.ITEM_MODIFY.order()) {
            getModifyMessage(msgType, baseData);
        } else if (pushType == ModifyEnum.ITEM_DELETE.order()) {
            getDeleteMessage(msgType, baseData);
        }
    }

    private void getNewMessage(int msgType, BaseData baseData, MsgWarnData warnData) {

    }

    private void sendChangeMsg(Integer nodeType, String pjId) {

    }

    private void getModifyMessage(int msgType, BaseData baseData) {

    }

    private void getDeleteMessage(int msgType, BaseData baseData) {

    }


    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                //前台程序
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }
}
