package com.weqia.wq.component.utils;

import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.data.LocalNetPath;
import com.weqia.utils.datastorage.file.PathUtil;
import com.weqia.wq.component.SelectArrUtil;
import com.weqia.wq.component.db.WeqiaDbUtil;
import com.weqia.wq.data.AttachmentData;
import com.weqia.wq.data.EnumData.AttachType;
import com.weqia.wq.data.global.WeqiaApplication;

import java.io.File;

public class LnUtil {

    private static boolean debug_ln = false;

    private static int getSourcedType(int type, String localPath) {
        if (type == AttachType.PICTURE.value() && StrUtil.notEmptyOrNull(localPath)) {
            boolean bSource = SelectArrUtil.getInstance().isSelImgSourceContain(localPath);
            if (bSource) {
                type = AttachType.PICTURE_WITH_SOURCE.value();
            }
        }
        return type;
    }

    public static void saveAttachData(AttachmentData attachmentData, String localPath) {
        LocalNetPath tmpPath =
                new LocalNetPath(null, attachmentData.getUrl(), attachmentData.getId(),
                        attachmentData.toString(), attachmentData.getType());
        tmpPath.setLocalPath(localPath);
        tmpPath.setNetPath(attachmentData.getUrl());
        saveData(tmpPath);
    }

    public static void removeLocalPathData(String where) {
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (debug_ln) L.e("注意，这里删除啦 -------------" + where);
        if (dbUtil != null) {
            dbUtil.deleteByWhere(LocalNetPath.class, where);
        }
    }

    public static void saveData(LocalNetPath data) {
        
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (dbUtil != null && data != null) {
            data.setType(getSourcedType(data.getType(), data.getLocalPath()));
            WeqiaApplication.getInstance().getDbUtil().save(data, true);
        }
    }

    public static String getLocalpath(String netPath, int type) {
        if (debug_ln) L.i("获取本地 key:" + netPath + ", type = " + type);
        String localPath = null;
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        String where = "netPath='" + netPath + "' and type = " + type;
        if (dbUtil != null && StrUtil.notEmptyOrNull(netPath)) {
            LocalNetPath localNetPath = dbUtil.findByWhere(LocalNetPath.class, where);
            if (localNetPath != null) {
                localPath = localNetPath.getLocalPath();
                // 路径不存在
                if (StrUtil.isEmptyOrNull(localPath)) {
                    removeLocalPathData(where);
                    localPath = null;
                } else {
                    if (!isFileCanUse(localPath, where, localNetPath)) {
                        localPath = null;
                    }
                }
            } else {
                localPath = null;
            }
        } else {
            localPath = null;
        }

        if (debug_ln) L.i("本地local:" + localPath);
        return localPath;
    }
    

    /**
     * type 为MsgTypeEnum
     * 
     * @param locPath
     * @param type
     * @return
     */
    public static String getNetpath(String locPath, int type) {
        if (debug_ln) L.i("获取本地 key:" + locPath + ", type = " + type);

        String netPath = null;
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (StrUtil.notEmptyOrNull(locPath) && dbUtil != null) {
            type = getSourcedType(type, locPath);
            String where = "localPath='" + locPath + "' and type = " + type;
            LocalNetPath localNetPath = dbUtil.findByWhere(LocalNetPath.class, where);
            if (localNetPath == null) {
                netPath = null;
            } else {
                netPath = localNetPath.getNetPath();
                if (!isFileCanUse(locPath, where, localNetPath)) {
                    netPath = null;
                }
            }
        } else {
            netPath = null;
        }
        if (debug_ln) L.i("网络net:" + netPath);
        return netPath;
    }

    /**
     * 本地文件是否存在，修改
     * 
     * @param localpath
     * @param where
     * @param localNetPath
     * @return
     */
    private static boolean isFileCanUse(String localpath, String where, LocalNetPath localNetPath) {

        if (StrUtil.isEmptyOrNull(localpath) || localNetPath == null) {
            return false;
        }
        if (PathUtil.isPathInDisk(localpath)) {
            File file = new File(localpath);
            if (!file.exists() || file.lastModified() > localNetPath.getcTime()) {
                removeLocalPathData(where);
                return false;
            }
        }

        if (debug_ln) L.i("本地文件可用," + localpath);

        return true;
    }

    /**
     * 图片地址是否过期
     * 
     * @param where
     * @param localNetPath
     * @return
     */
    private static boolean isImageCanUse(String localPath, String where, LocalNetPath localNetPath) {

        if (StrUtil.isEmptyOrNull(localPath) || localNetPath == null) {
            return false;
        }
        if (StrUtil.isExpired(localPath)) {
            removeLocalPathData(where);
            return false;
        }

        if (debug_ln) L.i("图片未过期," + localPath);
        return true;
    }

    /**
     * type 为MsgTypeEnum
     * 
     * @param type
     * @return
     */
    public static String getImageRealPath(String netPath, int type) {
        if (debug_ln) L.i("获取本地 key:" + netPath + ", type = " + type);
        String localPath = null;
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        String where = "netPath='" + netPath + "' and type = " + type;
        if (dbUtil != null && StrUtil.notEmptyOrNull(netPath)) {
            LocalNetPath localNetPath = dbUtil.findByWhere(LocalNetPath.class, where);
            if (localNetPath != null) {
                localPath = localNetPath.getLocalPath();
                // 路径不存在
                if (StrUtil.isEmptyOrNull(localPath)) {
                    removeLocalPathData(where);
                    localPath = null;
                } else {
                    if (!isImageCanUse(localPath, where, localNetPath)) {
                        localPath = null;
                    }
                }
            } else {
                localPath = null;
            }
        } else {
            localPath = null;
        }

        if (debug_ln) L.i("本地local:" + localPath);
        return localPath;
    }

    /**
     * type 为MsgTypeEnum
     * 
     * @param locPath
     * @param type
     * @return
     */
    public static AttachmentData getAttachment(String locPath, int type) {
        if (debug_ln) L.i("获取本地 key:" + locPath + ", type = " + type);
        type = getSourcedType(type, locPath);
        AttachmentData attachmentData = null;
        String netId = null;
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (StrUtil.notEmptyOrNull(locPath) && dbUtil != null) {
            String where = "localPath='" + locPath + "' and type = " + type;
            LocalNetPath localNetPath = dbUtil.findByWhere(LocalNetPath.class, where);
            if (localNetPath == null) {
                where = "netPath='" + locPath + "' and type = " + type;
                localNetPath = dbUtil.findByWhere(LocalNetPath.class, where);
            }
            if (localNetPath == null) {
                netId = null;
            } else {
                if (StrUtil.notEmptyOrNull(localNetPath.getAttData())) {
                    attachmentData =
                            AttachmentData.fromString(AttachmentData.class,
                                    localNetPath.getAttData());
                    if (attachmentData != null && StrUtil.notEmptyOrNull(attachmentData.getId())) {
                        netId = attachmentData.getId();
                    }

                    if (!isFileCanUse(locPath, where, localNetPath)) {
                        netId = null;
                        attachmentData = null;
                    }
                }
            }
        } else {
            netId = null;
        }
        if (debug_ln) L.i("网络net_id:" + netId);
        return attachmentData;
    }

    public static String getContentPath(String netPath, int type) {
        if (debug_ln) L.i("获取本地 key:" + netPath + ", type = " + type);
        String contentUri = null;
        if (StrUtil.notEmptyOrNull(netPath)) {
            LocalNetPath localNetPath =
                    WeqiaApplication
                            .getInstance()
                            .getDbUtil()
                            .findByWhere(LocalNetPath.class,
                                    "netPath='" + netPath + "' and type = " + type);
            if (localNetPath != null) {
                contentUri = localNetPath.getContentUri();
            } else {
                contentUri = null;
            }
        } else {
            contentUri = null;
        }
        if (debug_ln) L.i("获取到的本地contentURI地址为:" + contentUri);
        return contentUri;
    }
}
