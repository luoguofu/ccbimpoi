package com.weqia.utils.datastorage.file;

import java.io.File;

import com.weqia.data.StatedPerference;
import com.weqia.data.UtilsConstants;
import com.weqia.data.UtilsNotice;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;

/**
 * Description:获取存储位置的相对路径<br>
 * PathUtils.java Create on 2013-1-23 下午10:56:13
 * 
 * @author Bewin berwinzheng@gmail.com
 * @version 1.0 Copyright (c) 2013 Company,Inc. All Rights Reserved.
 */
public class PathUtil {

    public static final String PATH_CACHE = "cache"; // 缓存目录
    public static String PATH_WEQIA = "WeQia"; // 下载目录
    public static final String PATH_CONFIG = "config"; // 配置目录
    public static final String PATH_PICTURE = "picture"; // 图片目录
    public static final String PATH_FILE = "file"; // 文件目录
    public static final String PATH_DATA = "data"; // 数据目录
    public static final String PATH_AVATAR = "avatar"; // 头像目录
    public static final String PATH_EXCEPTION = "exception"; // 异常信息目录
    public static final String PATH_VOICE = "voice"; // 录音目录
    public static final String PATH_CRASH = "crash"; // 录音目录

    private static String defaultDiskPath = null;

    private static String RootPath = "Weqia";

    /**
     * @Description 获取磁盘缓存
     */
    public static void initUtil(String rootPath) {
        initUtil(rootPath, null);
    }
    
    /**
     * @Description 获取磁盘缓存
     */
    public static void initUtil(String rootPath, String wqPath) {
        if (StrUtil.notEmptyOrNull(rootPath)) {
            RootPath = rootPath;
            if (StrUtil.notEmptyOrNull(wqPath)) {
                PATH_WEQIA = wqPath;
            } else {
            	if (rootPath.equalsIgnoreCase("ssstai")) {
                    PATH_WEQIA = "shangshang";
                }
			}
        }
        defaultDiskPath = StorageUtil.getSaveDiskPath(UtilsConstants.DEFAULT_DISK_CACHE_SIZE);
        if (defaultDiskPath == null) {
            if (L.D) L.e(UtilsNotice.ERROR_NO_ENOUGH_SPACE_TO_USE);
            return;
        }
        StatedPerference.getInstance().put(UtilsConstants.pr_default_disk_path, defaultDiskPath);
    }

    private static String getPathFolder(String path) {
        if (StrUtil.notEmptyOrNull(defaultDiskPath)) {
            return FileUtil.getFolder(defaultDiskPath + File.separator + RootPath + File.separator
                    + path + File.separator);
        } else {
            String tempPath = getTemporaryDiskPath(UtilsConstants.DEFAULT_DISK_CACHE_SIZE);
            if (StrUtil.notEmptyOrNull(tempPath)) {
                return FileUtil.getFolder(tempPath + File.separator + RootPath + File.separator
                        + path + File.separator);
            }
        }
        return null;
    }


    public static String getDefaultdiskpath() {
        return defaultDiskPath;
    }

    /**
     * 获取临时的文件夹
     * 
     * @return String
     * @throws
     * @Title: getTemporaryDiskPath
     */
    public static String getTemporaryDiskPath(long fileSize) {
        String temporayDiskPath =
                StatedPerference.getInstance().get(UtilsConstants.pr_temporary_disk_path,
                        String.class, "");
        if (temporayDiskPath != null && StorageUtil.getTotalSpace(temporayDiskPath) > 0) {
            return temporayDiskPath;
        } else {
            String diskPath = StorageUtil.getSaveDiskPath(fileSize);
            if (StrUtil.isEmptyOrNull(diskPath)) {
                StatedPerference.getInstance().put(UtilsConstants.pr_temporary_disk_path, diskPath);
            }
            return diskPath;
        }
    }

    /**
     * 判断是否在磁盘中
     * 
     * @param path
     * @return
     */
    public static boolean isPathInDisk(String path) {
        if (StrUtil.isEmptyOrNull(path)) {
            return false;
        }

        if (path.startsWith("/")) {
            return true;
        }

        if (path.contains("storage")) {
            return true;
        }

        if (path.contains("emulated")) {
            return true;
        }

        if (path.contains("sdcard")) {
            return true;
        }

        if (path.contains("/mnt")) {
            return true;
        }

        if (path.startsWith("content:")) {
            return true;
        }

        String path1 = StorageUtil.getExternalStorageDirectory();
        if (StrUtil.isEmptyOrNull(path1)) {
            path1 = "------------------------------++";
        }

        String path2 = StorageUtil.getSdcard2StorageDirectory();
        if (StrUtil.isEmptyOrNull(path2)) {
            path2 = "---------------------++00";
        }
        String path3 = StorageUtil.getEmmcStorageDirectory();
        if (StrUtil.isEmptyOrNull(path3)) {
            path3 = "--0-0-0-0-0-0-0-0-0-0-";
        }
        String path4 = StorageUtil.getOtherExternalStorageDirectory();
        if (StrUtil.isEmptyOrNull(path4)) {
            path4 = "909-90909-9090-909";
        }

        String path5 = StorageUtil.getInternalStorageDirectory();
        if (StrUtil.isEmptyOrNull(path5)) {
            path5 = "678-9098-dfafdfasdf-------------";
        }

        if (path.contains(path1) || path.contains(path2) || path.contains(path3)
                || path.contains(path4) || path.contains(path5)) {
            return true;
        }
        return false;
    }

    /**
     * 获取默认缓存路径
     * 
     * @return String
     * @throws
     * @Title: getCachePath
     */
    public static String getCachePath() {
        String path = getPathFolder(PATH_CACHE);
        if (path == null) {
            return null;
        } else {
            // if (StorageUtil.getAvailableSpace(path) < UtilsConstants.DEFAULT_DISK_CACHE_SIZE) {
            // return null;
            // } else {
            return path;
            // }
        }
    }

    /**
     * 获取默认下载路径
     * 
     * @return String
     * @throws
     * @Title: getWQPath
     */
    public static String getWQPath() {
        return getDownPathFolder(PATH_WEQIA);
    }

    public static String getFilePath() {
        return getDownPathFolder(PATH_FILE);
    }


    private static String getDownPathFolder(String path) {
        if (StrUtil.notEmptyOrNull(StorageUtil.getSDcardPath(UtilsConstants.SD_DOWNLOAD_SIZE))) {
            return FileUtil.getFolder(defaultDiskPath + File.separator + RootPath + File.separator
                    + path + File.separator);
        }
        return null;
    }

    /**
     * 获取默认配置路径
     * 
     * @return String
     * @throws
     * @Title: getConfigPath
     */
    public static String getConfigPath() {
        return getPathFolder(PATH_CONFIG);
    }

    /**
     * 获取默认图片路径
     * 
     * @return String
     * @throws
     * @Title: getPicturePath
     */
    public static String getPicturePath() {
        return getPathFolder(PATH_PICTURE);
    }

    /**
     * 获取默认数据路径
     * 
     * @return String
     * @throws
     * @Title: getDataPath
     */
    public static String getDataPath() {
        return getPathFolder(PATH_DATA);
    }

    public static String getCrashPath() {
        return getPathFolder(PATH_CRASH);
    }

    /**
     * 获取默认头像路径
     * 
     * @return String
     * @throws
     * @Title: getAvatarPath
     */
    public static String getAvatarPath() {
        return getPathFolder(PATH_AVATAR);
    }

    /**
     * 获取默认异常路径
     * 
     * @return String
     * @throws
     * @Title: getAvatarPath
     */
    public static String getExceptionPath() {
        String exceptionPath = getDataPath() + File.separator + PATH_EXCEPTION;
        return FileUtil.getFolder(exceptionPath);
    }

    public static String getVoicePath() {
        return getPathFolder(PATH_VOICE);
    }

}
