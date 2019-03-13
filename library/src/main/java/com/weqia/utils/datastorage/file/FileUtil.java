package com.weqia.utils.datastorage.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.weqia.data.UtilsConstants;
import com.weqia.data.UtilsNotice;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:文件工具类<br>
 * FileUtils.java Create on 2013-1-4 下午10:12:37
 *
 * @author Bewin berwinzheng@gmail.com
 * @version 1.0 Copyright (c) 2013 Company,Inc. All Rights Res
 */
public class FileUtil {


    public static String getMd5ByFile(File file) throws FileNotFoundException {
        String value = null;
        FileInputStream in = new FileInputStream(file);
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    /**
     * 写图片文件到SD卡
     *
     * @throws IOException
     */
    public static void saveImageToSD(String filePath, Bitmap bitmap, int quality)
            throws IOException {
        if (bitmap != null) {
            File file = new File(filePath.substring(0, filePath.lastIndexOf(File.separator)));
            if (!file.exists()) {
                file.mkdirs();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
            bitmap.compress(CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
        }
    }


    /**
     * 获取已选中个图片列表
     *
     * @param context
     * @return
     */
    public static ArrayList<String> getSeletedImages(Context context) {
        ArrayList<String> selectedList = new ArrayList<String>();
        // String images = StatedPerference.getSelectImg();
        // if (TextUtils.isEmpty(images)) {
        // return selectedList;
        // }
        // String[] imgArr = images.split(",");
        // for (String item : imgArr) {
        // if (TextUtils.isEmpty(item)) {
        // continue;
        // }
        // selectedList.add(item);
        // }
        return selectedList;
    }

    /**
     * 保存选中的图片
     *
     * @param context
     * @param imgList
     */
    public static void saveSelectedImags(Context context, ArrayList<String> imgList) {
        // if (imgList == null) {
        // return;
        // }
        // StringBuffer result = new StringBuffer("");
        // for (String item : imgList) {
        // result.append(item).append(",");
        // }
        // StatedPerference.setSelectImg(result.toString());
    }

    /**
     * <p>
     * 对本地文件路径格式化
     * <p>
     * 其实就是加上 file://
     *
     * @return
     */
    public static String getFormatFilePath(String path) {
        if (TextUtils.isEmpty(path)) {
            return "";
        }
        return "file://" + path;
//        if (path.startsWith(UtilsConstants.FILE_START)) {
//            return path;
//        }
//        
//        return UtilsConstants.FILE_START + path.trim();
    }

    /**
     * 根据path创建文件夹，创建成功则返回路径，不成功返回空
     *
     * @param folderName
     * @return String
     * @throws
     * @Title: getFolder
     */
    public static String getFolder(String folderName) {
        if (folderName == null) {
            return null;
        }
        File dir = NativeFileUtil.createFolder(folderName);
        if (dir != null) {
            return dir.getAbsolutePath();
        } else {
            return null;
        }
    }

    /**
     * 创建文件
     *
     * @param folderString
     * @param fileName
     * @return String
     * @throws
     * @Title: getFile
     */
    public static String getFile(String folderString, String fileName) throws IOException {
        if (folderString == null | fileName == null) {
            return null;
        }
        File folder = new File(folderString);
        if (folder.exists()) {
            File file = NativeFileUtil.createFile(folderString + File.separator + fileName);
            if (file != null) {
                return file.getAbsolutePath();
            } else {
                return null;
            }
        } else {
            folder = NativeFileUtil.createFolder(folderString);
            if (folder != null) {
                File file =
                        NativeFileUtil.createFile(folder.getAbsolutePath() + File.separator
                                + fileName);
                if (file != null) {
                    return file.getAbsolutePath();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    /**
     * 判断文件是否存在，不存在则创建
     *
     * @param filePath
     * @return
     * @throws IOException
     * @Description
     */
    public static File createOrReadFile(String filePath) throws IOException {
        if (filePath == null) {
            if (L.D) {
                L.e(UtilsNotice.ERROR_FILE_PATH_NULL);
            }
            return null;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            String fileName = StrUtil.getFileNameByPath(filePath);
            if (fileName == null) {
                if (L.D) {
                    L.e("write path error");
                }
                return null;
            } else {
                String path = getFile(file.getParent(), fileName);
                if (path == null || !path.equals(filePath)) {
                    if (L.D) {
                        L.e("create file error");
                    }
                    return null;
                }
            }
        }
        return file;
    }

    /**
     * @param filePath
     * @param content
     * @return void
     * @throws
     * @Title: writeStringToFile
     */
    public static void writeStringToFile(String filePath, String content) {
        if (filePath == null) {
            if (L.D) {
                L.e(UtilsNotice.ERROR_FILE_PATH_NULL);
            }
            return;
        }
        BufferedWriter writer = null;
        try {
            File file = createOrReadFile(filePath);
            if (file == null) {
                return;
            }
            writer = new BufferedWriter(new FileWriter(file), UtilsConstants.IO_BUFFER_SIZE);
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // CheckedExceptionHandler.handleException(e);
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception tr) {
                    // CheckedExceptionHandler.handleException(tr);
                    tr.printStackTrace();
                }
            }
        }
    }

    public static List<String> getArrayFromFile(String filePath) {

        BufferedReader reader = null;
        try {

            File file = createOrReadFile(filePath);
            if (file == null) {
                return null;
            }
            List<String> contentList = new ArrayList<String>();
            reader = new BufferedReader(new FileReader(file), UtilsConstants.IO_BUFFER_SIZE);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                contentList.add(tempString);
            }
            reader.close();
            return contentList;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException tr) {
                    L.w("--", tr);
                }
            }
        }
        return null;
    }

    /**
     * @param filePath
     * @return
     * @Description
     */
    public static String getStringFromFile(String filePath) {

        BufferedReader reader = null;
        try {
            File file = createOrReadFile(filePath);
            if (file == null) {
                return null;
            }
            StringBuilder buffer = new StringBuilder();
            reader = new BufferedReader(new FileReader(file), UtilsConstants.IO_BUFFER_SIZE);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                buffer.append(tempString);
            }
            return buffer.toString();
        } catch (IOException e) {
            L.w("--", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException tr) {
                    L.w("--", tr);
                }
            }
        }
        return null;
    }

    /**
     * 从本地获取bitmap
     *
     * @param url
     * @return
     * @Description
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            L.w("--", e);
            return null;
        }
    }

    /**
     * input获取String
     *
     * @param is
     * @return
     * @throws IOException
     * @Description
     */
    public static String inputStreamToString(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }

    /**
     * @param is
     * @param os
     * @throws IOException
     * @Description
     */
    public static void inputStreamToOutputStream(InputStream is, OutputStream os)
            throws IOException {
        final int bufsize = UtilsConstants.IO_BUFFER_SIZE * 10;
        final byte[] cbuf = new byte[bufsize];

        for (int readBytes = is.read(cbuf, 0, bufsize); readBytes > 0; readBytes =
                is.read(cbuf, 0, bufsize)) {
            os.write(cbuf, 0, readBytes);
        }
    }

    public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        if (!file.exists()) {
            return 0;
        }
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            L.w("--", e);
        }
        return formetFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            L.w("--", e);
        }
        return formetFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        FileInputStream fis = null;
        if (file.exists()) {
            try {
                fis = new FileInputStream(file);
                size = fis.available();
                fis.close();
            } catch (Exception e) {
            } finally {
                try {
                    fis.close();
                } catch (Exception e2) {
                }
            }

        } else {
//            file.createNewFile();
            // Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String formetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    public static double formetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

}
