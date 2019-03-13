package com.weqia.utils.datastorage.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import com.weqia.BaseInit;
import com.weqia.utils.L;
import com.weqia.utils.ShellUtil;

/**
 * Description:获得手机内存储器状态<br>
 * getStorageSize.java Create on 2013-1-3 下午10:20:26
 * 
 * @author Bewin berwinzheng@gmail.com
 * @version 1.0 Copyright (c) 2013 Company,Inc. All Rights Res
 */
public class StorageUtil {

    /**
     * 
     * @Title: getSdcard2StorageDirectory
     * @return
     * @return String
     * @throws
     */
    public static final String getSdcard2StorageDirectory() {
        return "/mnt/sdcard2";
    }

    /**
     * 
     * @Title: getEmmcStorageDirectory
     * @return
     * @return String
     * @throws
     */
    public static final String getEmmcStorageDirectory() {
        return "/mnt/emmc";
    }

    // 额外挂载点目录
    private static String otherExternalStorageDirectory = null;
    // 额外挂载点状态未知
    private static int kOtherExternalStorageStateUnknow = -1;
    // 额外挂载点不可用
    private static int kOtherExternalStorageStateUnable = 0;
    private static int kOtherExternalStorageStateIdle = 1;
    // 默认额外挂载点状态未知
    private static int otherExternalStorageState = kOtherExternalStorageStateUnknow;

    /**
     * 
     * @Title: getOtherExternalStorageDirectory
     * @return
     * @return String
     * @throws
     */
    public static final String getOtherExternalStorageDirectory() {
        if (otherExternalStorageState == kOtherExternalStorageStateUnable) return null;
        if (otherExternalStorageState == kOtherExternalStorageStateUnknow) {
            FstabReader fsReader = new FstabReader();
            if (fsReader.size() <= 0) {
                otherExternalStorageState = kOtherExternalStorageStateUnable;
                return null;
            }
            List<StorageInfo> storages = fsReader.getStorages();
            /* 对于可用空间小于100M的挂载节点忽略掉 */
            long availableSpace = 100 << (20);
            String path = null;
            for (int i = 0; i < storages.size(); i++) {
                StorageInfo info = storages.get(i);
                if (info != null && info.getAvailableSpace() > availableSpace) {
                    availableSpace = info.getAvailableSpace();
                    path = info.getPath();
                }
                if (path == null) {
                    continue;
                }
                if (path.equalsIgnoreCase(getEmmcStorageDirectory())
                        || path.equalsIgnoreCase(getExternalStorageDirectory())
                        || path.equalsIgnoreCase(getSdcard2StorageDirectory())) {
                    path = null;
                }
            }
            otherExternalStorageDirectory = path;
            if (otherExternalStorageDirectory != null) {
                otherExternalStorageState = kOtherExternalStorageStateIdle;
            } else {
                otherExternalStorageState = kOtherExternalStorageStateUnable;
            }
        }
        return otherExternalStorageDirectory;
    }

    /**
     * 获取外部存储指定的目录
     * 
     * @Title: getExternalStoragePublicDirectory
     * @param type
     * @return
     * @return String
     * @throws
     */
    public static final String getExternalStoragePublicDirectory(String type) {
        File file = Environment.getExternalStoragePublicDirectory(type);
        if (file != null) return file.getAbsolutePath();
        return null;
    }

    /**
     * 是否有SD卡
     * 
     * @return
     */
    public static boolean hasExternalStorage() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取外部存储私有目录
     * 
     * @Title: getExternalPrivateFilesDirectory
     * @return
     * @return String
     * @throws
     */
    public static final String getExternalPrivateFilesDirectory(Context ctx) {
        String externalStoragePrivateDirectory = null;
        File file = ctx.getExternalFilesDir(null);
        if (file != null) {
            externalStoragePrivateDirectory = file.getAbsolutePath();
        }
        return externalStoragePrivateDirectory;
    }

    /**
     * 获取内部存储Directory
     * 
     * @Title: getInternalStorageDirectory
     * @return
     * @return String
     * @throws
     */
    public static final String getInternalStorageDirectory() {
        String internalStorageDirectory = null;
        File file = BaseInit.ctx.getFilesDir();
        if (file != null) {
            internalStorageDirectory = file.getAbsolutePath();
            if (!file.exists()) file.mkdirs();
        }
        return internalStorageDirectory;
    }

    /**
     * 获取外部存储目录
     * 
     * @Title: getExternalStorageDirectory
     * @return
     * @return String
     * @throws
     */
    public static final String getExternalStorageDirectory() {
        File file = Environment.getExternalStorageDirectory();
        if (file != null) return file.getAbsolutePath();
        return null;
    }

    /**
     * 
     * @Title: getAvailableSpace
     * @param path
     * @return
     * @return long
     * @throws
     */
    @SuppressWarnings("deprecation")
    public static long getAvailableSpace(String path) {
        if (path == null) {
            return -1;
        }
        File file = new File(path);
        if (!file.exists()) {
            return -1;
        }
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
        // return file.getUsableSpace();
        // }
        StatFs statfs = new StatFs(path);
        long blockSize = statfs.getBlockSize();
        long availableBlocks = statfs.getAvailableBlocks();
        return blockSize * availableBlocks;
    }

    /**
     * 
     * @Title: getTotalSpace
     * @param path
     * @return
     * @return long
     * @throws
     */
    @SuppressWarnings("deprecation")
    public static long getTotalSpace(String path) {
        if (path == null) {
            return -1;
        }
        File file = new File(path);
        if (!file.exists()) {
            return -1;
        }
        StatFs statfs = new StatFs(path);
        long blockSize = statfs.getBlockSize();
        long totalBlocks = statfs.getBlockSize();
        return blockSize * totalBlocks;
    }

    /**
     * 获取手机内部总的存储空间
     * 
     * @Title: getInternalStorageAvailableSpace
     * @return
     * @return long
     * @throws
     */
    public static long getInternalStorageAvailableSpace() {
        String path = getInternalStorageDirectory();
        return getAvailableSpace(path);
    }

    /**
     * 获取手机内部总的存储空间
     * 
     * @Title: getInternalStorageTotalSpace
     * @return
     * @return long
     * @throws
     */
    public static long getInternalStorageTotalSpace() {
        File path = Environment.getDataDirectory();
        if (path != null) {
            return getTotalSpace(path.getPath());
        } else {
            return 0;
        }
    }

    /**
     * 
     * @Title: getExternaltStorageAvailableSpace
     * @return
     * @return long
     * @throws
     */
    public static long getExternaltStorageAvailableSpace() {
        if (!(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))) {
            return 0;
        }
        String path = getExternalStorageDirectory();
        return getAvailableSpace(path);
    }

    /**
     * 
     * @Title: getExternaltStorageTotalSpace
     * @return
     * @return long
     * @throws
     */
    public static long getExternaltStorageTotalSpace() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return 0;
        }
        String path = getExternalStorageDirectory();
        return getTotalSpace(path);
    }

    /**
     * 
     * @Title: getSdcard2StorageAvailableSpace
     * @return
     * @return long
     * @throws
     */
    public static long getSdcard2StorageAvailableSpace() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return 0;
        }
        String path = getSdcard2StorageDirectory();
        return getAvailableSpace(path);
    }

    /**
     * 
     * @Title: getSdcard2StorageTotalSpace
     * @return
     * @return long
     * @throws
     */
    public static long getSdcard2StorageTotalSpace() {
        String path = getSdcard2StorageDirectory();
        return getTotalSpace(path);
    }

    /**
     * 
     * @Title: getEmmcStorageAvailableSpace
     * @return
     * @return long
     * @throws
     */
    public static long getEmmcStorageAvailableSpace() {
        String path = getEmmcStorageDirectory();
        return getAvailableSpace(path);
    }

    /**
     * 
     * @Title: getEmmcStorageTotalSpace
     * @return
     * @return long
     * @throws
     */
    public static long getEmmcStorageTotalSpace() {
        String path = getEmmcStorageDirectory();
        return getTotalSpace(path);
    }

    /**
     * 
     * @Title: getOtherExternaltStorageAvailableSpace
     * @return
     * @return long
     * @throws
     */
    public static long getOtherExternaltStorageAvailableSpace() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return -1;
        }
        if (otherExternalStorageState == kOtherExternalStorageStateUnable) return -1;
        if (otherExternalStorageDirectory == null) {
            getOtherExternalStorageDirectory();
        }
        if (otherExternalStorageDirectory == null) return -1;
        return getAvailableSpace(otherExternalStorageDirectory);
    }

    public static long getOtherExternaltStorageTotalSpace() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return -1;
        }
        if (otherExternalStorageState == kOtherExternalStorageStateUnable) return -1;
        if (otherExternalStorageDirectory == null) {
            getOtherExternalStorageDirectory();
        }
        if (otherExternalStorageDirectory == null) return -1;
        return getTotalSpace(otherExternalStorageDirectory);
    }

    /**
     * 在很多手机上，虽然我们使用Context的openFileOutput(FILENAME, Context.MODE_WORLD_READABLE) 的方式来创建文件，而且使用ls
     * -l看到该文件对别的应用程序来说其实已经有读的权限， 但是别的应用程序实际上还是无法读取这些
     * 
     * @Title: changeAccessForInternalStorage
     * @return void
     * @throws
     */
    public static void changeAccessForInternalStorage(Context ctx) {
        String shellScript = "chmod 705 " + getInternalStorageDirectory();
        try {
            runShellScriptForWait(shellScript);
        } catch (SecurityException e) {
            L.w("--", e);
        }
    }

    /**
     * 同步运行
     * 
     * @Title: runShellScriptForWait
     * @param cmd
     * @return
     * @throws SecurityException
     * @return boolean
     * @throws
     */
    public static boolean runShellScriptForWait(final String cmd) throws SecurityException {
        ShellUtil thread = new ShellUtil(cmd);
        thread.setDaemon(true);
        thread.start();
        int k = 0;
        while (!thread.isReturn() && k++ < 20) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                L.w("--", e);
            }
        }
        if (k >= 20) {
            thread.interrupt();
        }
        return thread.isSuccess();
    }

    /**
     * 是否为手机内存路径
     * 
     * @Title: isInternalStoragePath
     * @param path
     * @return
     * @return boolean
     * @throws
     */
    public static boolean isInternalStoragePath(String path) {
        String rootPath = getInternalStorageDirectory();
        if (path != null && path.startsWith(rootPath)) return true;
        return false;
    }

    /**
     * 获取一个大文件的存储位置
     * 
     * @Title: getSaveDiskPath
     * @param saveSize
     * @param path 相对路径
     * @return
     * @return String
     * @throws
     */
    public static String getSaveDiskPath(long saveSize) {
        String savePath = null;
        if (getExternaltStorageAvailableSpace() > saveSize) {
            savePath = getExternalStorageDirectory();
        } else if (getSdcard2StorageAvailableSpace() > saveSize) {
            savePath = getSdcard2StorageDirectory();
        } else if (getEmmcStorageAvailableSpace() > saveSize) {
            savePath = getEmmcStorageDirectory();
        } else if (getOtherExternaltStorageAvailableSpace() > saveSize) {
            savePath = getOtherExternalStorageDirectory();
        } else if (getInternalStorageAvailableSpace() > saveSize) {
            savePath = getInternalStorageDirectory();
        }
        return savePath;
    }

    /**
     * 获取可用的SD卡路径
     * 
     * @param saveSize
     * @return
     */
    public static String getSDcardPath(long saveSize) {
        String savePath = null;
        if (getExternaltStorageAvailableSpace() > saveSize) {
            savePath = getExternalStorageDirectory();
        } else if (getSdcard2StorageAvailableSpace() > saveSize) {
            savePath = getSdcard2StorageDirectory();
        } else if (getEmmcStorageAvailableSpace() > saveSize) {
            savePath = getEmmcStorageDirectory();
        }
        return savePath;
    }

    public static class FstabReader {
        final List<StorageInfo> storages = new ArrayList<StorageInfo>();

        public FstabReader() {
            init();
        }

        public int size() {
            return storages == null ? 0 : storages.size();
        }

        public List<StorageInfo> getStorages() {
            return storages;
        }

        public void init() {
            File file = new File("/system/etc/vold.fstab");
            if (file.exists()) {
                FileReader fr = null;
                BufferedReader br = null;
                try {
                    fr = new FileReader(file);
                    if (fr != null) {
                        br = new BufferedReader(fr);
                        String s = br.readLine();
                        while (s != null) {
                            if (s.startsWith("dev_mount")) {
                                /* "\s"转义符匹配的内容有：半/全角空格 */
                                String[] tokens = s.split("\\s");
                                String path = tokens[2]; // mount_point

                                long availableSpace = getAvailableSpace(path);
                                if (availableSpace > 0) {
                                    StorageInfo storage =
                                            new StorageInfo(path, availableSpace,
                                                    getTotalSpace(path));
                                    storages.add(storage);
                                }
                            }
                            s = br.readLine();
                        }
                    }
                } catch (Exception e) {
                    L.e("--", e);
                } finally {
                    if (fr != null) try {
                        fr.close();
                    } catch (IOException e) {
                        L.e("--", e);
                    }
                    if (br != null) try {
                        br.close();
                    } catch (IOException e) {
                        L.e("--", e);
                    }
                }
            }
        }
    }

    static class StorageInfo implements Comparable<StorageInfo> {
        private String path;
        private long availableSpace;
        private long totalSpace;

        StorageInfo(String path, long availableSpace, long totalSpace) {
            this.path = path;
            this.availableSpace = availableSpace;
            this.totalSpace = totalSpace;
        }

        @Override
        public int compareTo(StorageInfo another) {
            if (null == another) return 1;

            return this.totalSpace - another.totalSpace > 0 ? 1 : -1;
        }

        long getAvailableSpace() {
            return availableSpace;
        }

        long getTotalSpace() {
            return totalSpace;
        }

        String getPath() {
            return path;
        }
    }
}
