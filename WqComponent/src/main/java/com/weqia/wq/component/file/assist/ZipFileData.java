package com.weqia.wq.component.file.assist;

import com.weqia.utils.StrUtil;
import com.weqia.wq.data.BaseData;

import org.apache.tools.zip.ZipEntry;

/**
 * Created by 20161005 on 2017/6/30.
 */

public class ZipFileData extends BaseData {
    /**
     * 原始zip里文件  文件名
     */
    private String entryName;
    /**
     * 列表显示用的文件名
     */
    private String fileName;
    /**
     * 列表文件大小
     */
    private long fileSize;
    /**
     * 是否是文件夹
     */
    private boolean bDir;
    /**
     * 分解原始文件名
     */
    private String[] dirFile;

    private ZipEntry zipEntry;

    public ZipFileData(String entryName, long fileSize, ZipEntry zipEntry) {
        this.entryName = entryName;
        this.fileName = entryName;
        this.fileSize = fileSize;
        this.zipEntry = zipEntry;
    }

    public boolean isDir() {
        if (StrUtil.isEmptyOrNull(fileName)) {
            return false;
        }
        return fileName.endsWith("/");
    }

    /**
     * @return 获取到文件夹里的文件列表
     */
    public String[] getDirFile() {
        if (StrUtil.isEmptyOrNull(entryName) || !entryName.contains("/")) {
            return null;
        } else {
            dirFile = entryName.split("/");
        }
        return dirFile;
    }

    public void setDir(boolean dir) {
        bDir = dir;
    }

    public void setDirFile(String[] dirFile) {
        this.dirFile = dirFile;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public ZipEntry getZipEntry() {
        return zipEntry;
    }

    public void setZipEntry(ZipEntry zipEntry) {
        this.zipEntry = zipEntry;
    }
}
