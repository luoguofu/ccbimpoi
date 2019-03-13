package com.weqia.utils.datastorage.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.weqia.utils.StrUtil;

/**
 * Description: 增加，删除，移动，重命名 <br>
 * FileUtility.java Create on 2013-1-4 上午1:27:17
 *
 * @author Bewin berwinzheng@gmail.com
 * @version 1.0 Copyright (c) 2013 Company,Inc. All Rights Res
 * @updated 12-一月-2013 18:05:17
 */
public class NativeFileUtil {
    /**
     * 创建文件
     *
     * @param path
     * @return File
     * @throws
     * @Title: createFile
     */
    public static File createFile(String path) throws IOException {
        if (!StrUtil.isEmptyOrNull(path)) {
            File file = new File(path);
            if (!file.exists()) {
                int lastIndex = path.lastIndexOf(File.separator);
                String dir = path.substring(0, lastIndex);
                if (createFolder(dir) != null) {
                    file.createNewFile();
                    return file;
                }
            } else {
                file.createNewFile();
                return file;
            }
        }
        return null;
    }

    /**
     * 删除一个文件
     *
     * @param file
     * @return boolean
     * @throws
     * @Title: delFile
     */
    public static boolean delFile(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        if (file.isDirectory())
            return false;
        return file.delete();
    }

    /**
     * 创建目录
     *
     * @param path
     * @return File
     * @throws
     * @Title: createFolder
     */
    public static File createFolder(String path) {
        if (StrUtil.notEmptyOrNull(path)) {
            File dir = new File(path);
            if (dir.exists()) {
                if (dir.isDirectory()) {
                    return dir;
                }
            }
            dir.mkdirs();
            return dir;
        } else {
            return null;
        }
    }

    /**
     * 删除目录（可以是非空目录）
     *
     * @param dir
     * @return boolean
     * @throws
     * @Title: delFolder
     */
    public static boolean delFolder(File folder) {
        if (folder == null || !folder.exists() || folder.isFile()) {
            return false;
        }
        try {
            for (File file : folder.listFiles()) {
                if (file.isFile()) {
                    file.delete();
                } else if (file.isDirectory()) {
                    delFolder(file);// 递归
                }
            }
        } catch (Exception e) {
        }

        folder.delete();
        return true;
    }

    /**
     * 修改文件或目录名
     *
     * @param oldPath
     * @param newPath
     * @return boolean
     * @throws
     * @Title: renameFile
     */
    public static boolean renameFile(String oldPath, String newPath) {
        if (!StrUtil.isEmptyOrNull(oldPath) && !StrUtil.isEmptyOrNull(newPath)) {
            File oldFile = new File(oldPath);
            File newFile = new File(newPath);

            if (oldPath != null && newFile != null) {
                return oldFile.renameTo(newFile);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 拷贝单个文件
     *
     * @param oldPath
     * @param newPath
     * @return boolean
     * @throws IOException
     * @throws
     * @Title: copySingleFile
     */
    public static boolean copySingleFile(String oldPath, String newPath)
            throws IOException {
        if (!StrUtil.isEmptyOrNull(oldPath) && !StrUtil.isEmptyOrNull(newPath)) {
            File oldFile = new File(oldPath);
            File newFile = new File(newPath);

            if (oldPath != null && newFile != null) {
                return copyFileTo(oldFile, newFile);
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    /**
     * 拷贝指定目录的所有文件
     *
     * @param oldPath
     * @param newPath
     * @return boolean
     * @throws IOException
     * @throws
     * @Title: copyAllFiles
     */
    public static boolean copyAllFiles(String oldPath, String newPath)
            throws IOException {
        if (!StrUtil.isEmptyOrNull(oldPath) && !StrUtil.isEmptyOrNull(newPath)) {
            File oldFile = new File(oldPath);
            File newFile = new File(newPath);

            if (oldPath != null && newFile != null) {
                return copyFilesTo(oldFile, newFile);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 移动单个文件
     *
     * @param oldPath
     * @param newPath
     * @return boolean
     * @throws IOException
     * @throws
     * @Title: moveSingleFileTo
     */
    public static boolean moveSingleFileTo(String oldPath, String newPath)
            throws IOException {
        if (!StrUtil.isEmptyOrNull(oldPath) && !StrUtil.isEmptyOrNull(newPath)) {
            File oldFile = new File(oldPath);
            File newFile = new File(newPath);

            if (oldPath != null && newFile != null) {
                return moveFileTo(oldFile, newFile);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 移动指定目录的所有文件
     *
     * @param oldPath
     * @param newPath
     * @return boolean
     * @throws IOException
     * @throws
     * @Title: moveSDFilesTo
     */
    public static boolean moveSDFilesTo(String oldPath, String newPath)
            throws IOException {
        if (!StrUtil.isEmptyOrNull(oldPath) && !StrUtil.isEmptyOrNull(newPath)) {
            File oldFile = new File(oldPath);
            File newFile = new File(newPath);

            if (oldPath != null && newFile != null) {
                return moveFilesTo(oldFile, newFile);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 拷贝一个文件
     *
     * @param srcFile
     * @param destFile
     * @return boolean
     * @throws IOException
     * @throws
     * @Title: copyFileTo
     */
    public static boolean copyFileTo(File srcFile, File destFile)
            throws IOException {
        if (srcFile == null || destFile == null) {
            return false;
        }
        if (srcFile.isDirectory() || destFile.isDirectory())
            return false;
        if (!srcFile.exists()) {
            return false;
        }
        if (!destFile.exists()) {
            createFile(destFile.getAbsolutePath());
        }
        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(destFile);
        int readLen = 0;
        byte[] buf = new byte[1024];
        while ((readLen = fis.read(buf)) != -1) {
            fos.write(buf, 0, readLen);
        }
        fos.flush();
        fos.close();
        fis.close();
        return true;
    }

    /**
     * 拷贝目录
     *
     * @param srcDir
     * @param destDir
     * @return
     * @throws IOException
     */
    public static boolean copyFilesTo(File srcDir, File destDir)
            throws IOException {
        if (srcDir == null || destDir == null) {
            return false;
        }

        if (!srcDir.exists()) {
            return false;
        }
        if (!destDir.exists()) {
            createFolder(destDir.getAbsolutePath());
        }

        if (!srcDir.isDirectory() || !destDir.isDirectory())
            return false;

        File[] srcFiles = srcDir.listFiles();
        for (int i = 0; i < srcFiles.length; i++) {
            if (srcFiles[i].isFile()) {
                File destFile = new File(destDir.getPath() + "//"
                        + srcFiles[i].getName());
                copyFileTo(srcFiles[i], destFile);
            } else if (srcFiles[i].isDirectory()) {
                File theDestDir = new File(destDir.getPath() + "//"
                        + srcFiles[i].getName());
                copyFilesTo(srcFiles[i], theDestDir);
            }
        }
        return true;
    }

    /**
     * 移动一个文件
     *
     * @param srcFile
     * @param destFile
     * @return
     * @throws IOException
     */
    public static boolean moveFileTo(File srcFile, File destFile)
            throws IOException {
        if (srcFile == null || destFile == null) {
            return false;
        }
        boolean iscopy = copyFileTo(srcFile, destFile);
        if (!iscopy)
            return false;
        delFile(srcFile);
        return true;
    }

    /**
     * 移动目录下的所有文件到指定目录
     *
     * @param srcDir
     * @param destDir
     * @return
     * @throws IOException
     */
    public static boolean moveFilesTo(File srcDir, File destDir)
            throws IOException {
        if (srcDir == null || destDir == null) {
            return false;
        }
        if (!srcDir.isDirectory() || !destDir.isDirectory()) {
            return false;
        }
        File[] srcDirFiles = srcDir.listFiles();
        for (int i = 0; i < srcDirFiles.length; i++) {
            if (srcDirFiles[i].isFile()) {
                File oneDestFile = new File(destDir.getPath() + "//"
                        + srcDirFiles[i].getName());
                moveFileTo(srcDirFiles[i], oneDestFile);
                delFile(srcDirFiles[i]);
            } else if (srcDirFiles[i].isDirectory()) {
                File oneDestFile = new File(destDir.getPath() + "//"
                        + srcDirFiles[i].getName());
                moveFilesTo(srcDirFiles[i], oneDestFile);
                delFolder(srcDirFiles[i]);
            }
        }
        return true;
    }

    /**
     * 获取文件大小
     *
     * @param file
     * @return long
     * @throws IOException
     * @throws
     * @Title: getFileSize
     */
    public static long getFileSize(File file) throws IOException {
        if (file.exists()) {
            long size = 0;
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
            fis.close();
            return size;
        } else {
            return 0;
        }
    }

    /**
     * 获取文件夹大小
     *
     * @param f
     * @return long
     * @throws Exception
     * @throws
     * @Title: getFolderSize
     */
    public static long getFolderSize(File file) throws IOException {
        long size = 0;
        File flist[] = file.listFiles();
        if (flist != null) {
            for (int i = 0; i < flist.length; i++) {
                if (flist[i].isDirectory()) {
                    size = size + getFolderSize(flist[i]);
                } else {
                    size = size + flist[i].length();
                }
            }
        }
        return size;
    }

    /**
     * 递归求取目录文件个数
     *
     * @param file
     * @return long
     * @throws
     * @Title: getFileNum
     */
    public static long getFileNum(File file) {
        long size = 0;
        File flist[] = file.listFiles();
        if (flist != null) {
            size = flist.length;
            for (int i = 0; i < flist.length; i++) {
                if (flist[i].isDirectory()) {
                    size += getFileNum(flist[i]);
                    size--;
                }
            }
        } else {
            System.err.println(file.getAbsolutePath());
        }
        return size;
    }


}
