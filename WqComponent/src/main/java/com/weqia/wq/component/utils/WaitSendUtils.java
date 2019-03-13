package com.weqia.wq.component.utils;

import android.content.Context;

import com.weqia.utils.StrUtil;
import com.weqia.wq.component.AttachService;
import com.weqia.wq.component.db.WeqiaDbUtil;
import com.weqia.wq.component.utils.request.ServiceParams;
import com.weqia.wq.data.AttachmentData;
import com.weqia.wq.data.EnumData;
import com.weqia.wq.data.EnumData.DataStatusEnum;
import com.weqia.wq.data.UpAttachParams;
import com.weqia.wq.data.WaitSendData;
import com.weqia.wq.data.WaitUpFileData;
import com.weqia.wq.data.global.WeqiaApplication;
import com.weqia.wq.global.ComponentUtil;

import java.util.ArrayList;
import java.util.List;

// 后台发送
public class WaitSendUtils {
    // private static WaitSendData sendData;
    // private Integer itype;
    // private static WeqiaDbUtil dbUtil;
    // private Context ctx;

    public WaitSendUtils(Context ctx, WaitSendData sendData, Integer itype) {
        // this.sendData = sendData;
        // this.itype = itype;
        // this.ctx = ctx;
        // dbUtil = WeqiaApplication.getInstance().getDbUtil();
    }

    // public void doSend() {
    //
    // }

    // 发送
    public static void doSend(final WaitSendData sendData, ServiceParams params) {
        if (sendData == null) {
            return;
        }
        boolean sendNow = true;
        final WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        dbUtil.updateBySql(WaitSendData.class, "sendStatus = " + DataStatusEnum.SENDIND.value()
                + " WHERE gId= " + sendData.getgId() + "");
        if (sendData.getItype().intValue() == 1111
                || sendData.getItype().intValue() == 728
//                || sendData.getItype().intValue() == 10000
                ) {
            params = UpAttachParams.fromString(UpAttachParams.class, sendData.getParams());
        }
        if (sendNow && params != null) {
            // 是否含有文件
            if (ComponentUtil.checkbFile(sendData.getgId())) {
                List<WaitUpFileData> upFileDatas =
                        dbUtil.findAllByWhereOrderByAsc(WaitUpFileData.class, "sendId = "
                                + sendData.getgId(), "id");
                AttachService.upLoadPicture(upFileDatas, sendData.getgId(), params, sendData);
            } else {
                AttachService.startSend(params, sendData);
            }
        }
    }

    // 发送成的图片IDs
    public static String getFileIds(int sendId) {
        List<WaitUpFileData> upFileDatas =
                WeqiaApplication.getInstance().getDbUtil()
                        .findAllByWhereOrderByAsc(WaitUpFileData.class, "sendId = " + sendId, "id");
        StringBuffer idBuffer = new StringBuffer();
        if (StrUtil.listNotNull(upFileDatas)) {
            for (int i = 0; i < upFileDatas.size(); i++) {
                WaitUpFileData data = upFileDatas.get(i);
                if (data != null) {
                    if (idBuffer.length() == 0) {
                        idBuffer.append(data.getFileId());
                    } else {
                        idBuffer.append(",").append(data.getFileId());
                    }
                }
            }
        }
        if (idBuffer.length() > 0) {
            return idBuffer.toString();
        } else {
            return "";
        }
    }

    // 直接拼接好发送成功文件后返回的URL
    public static String getFileUrl(int sendId) {
        List<WaitUpFileData> upFileDatas =
                WeqiaApplication.getInstance().getDbUtil()
                        .findAllByWhereOrderByAsc(WaitUpFileData.class, "sendId = " + sendId, "id");
        StringBuffer urlBuffer = new StringBuffer();
        for (WaitUpFileData file : upFileDatas) {
            if (file != null) {
                AttachmentData tmpData =
                        AttachmentData
                                .fromString(AttachmentData.class, file.getUpfile().toString());
                if (tmpData != null) {
                    if (urlBuffer.length() == 0) {
                        urlBuffer.append(tmpData.getUrl());
                    } else {
                        urlBuffer.append(",").append(tmpData.getUrl());
                    }
                }
            }
        }
        if (urlBuffer.length() > 0) {
            return urlBuffer.toString();
        } else {
            return "";
        }
    }

    // 得到发送成功文件的集合
    public static String getFileUrls(int sendId) {
        List<WaitUpFileData> upFileDatas =
                WeqiaApplication.getInstance().getDbUtil()
                        .findAllByWhereOrderByAsc(WaitUpFileData.class, "sendId = " + sendId, "id");
        List<AttachmentData> attachmentDatas = new ArrayList<AttachmentData>();
        for (WaitUpFileData file : upFileDatas) {
            if (file != null) {
                AttachmentData tmpData =
                        AttachmentData
                                .fromString(AttachmentData.class, file.getUpfile().toString());
                attachmentDatas.add(tmpData);
            }
        }
        String arrStr = attachmentDatas.toString();
        // L.e(arrStr);
        return arrStr;
    }

    // 得到发送成功图片的URL
    public static List<AttachmentData> getPicUrls(int sendId) {
        List<WaitUpFileData> upFileDatas =
                WeqiaApplication.getInstance().getDbUtil()
                        .findAllByWhereOrderByAsc(WaitUpFileData.class, "sendId = " + sendId, "id");
        List<AttachmentData> attachmentDatas = new ArrayList<AttachmentData>();
        for (WaitUpFileData file : upFileDatas) {
            if (file != null && file.getType().intValue() == EnumData.AttachType.PICTURE.value()) {
                AttachmentData tmpData =
                        AttachmentData
                                .fromString(AttachmentData.class, file.getUpfile().toString());
                attachmentDatas.add(tmpData);
            }
        }
        // L.e(arrStr);
        return attachmentDatas;
    }
}
