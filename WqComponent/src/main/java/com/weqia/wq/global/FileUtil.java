package com.weqia.wq.global;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

/**
 * <pre>
 *     author: MLLWF
 *     time  : 2017/10/31
 *     blog  :
 *     desc  :
 * </pre>
 */
public class FileUtil {

    public static String formetFileSize(double fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0L) {
            return wrongSize;
        } else {
            if (fileS < 1024L) {
                fileSizeString = df.format((double) fileS) + "B";
            } else if (fileS < 1048576L) {
                fileSizeString = df.format((double) fileS / 1024.0D) + "KB";
            } else if (fileS < 1073741824L) {
                fileSizeString = df.format((double) fileS / 1048576.0D) + "MB";
            } else {
                fileSizeString = df.format((double) fileS / 1.073741824E9D) + "GB";
            }
            return fileSizeString;
        }
    }

    public static double calculateFileSize(double fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeString;
        double wrongSize = 0;
        if (fileS == 0L) {
            return wrongSize;
        } else {
            if (fileS < 1024L) {
                fileSizeString = fileS;
            } else if (fileS < 1048576L) {
                fileSizeString = fileS / 1024.0D;
            } else if (fileS < 1073741824L) {
                fileSizeString = fileS / 1048576.0D;
            } else {
                fileSizeString = fileS / 1.073741824E9D;
            }
            return fileSizeString;
        }
    }

    public static double formetFileSize(double fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0.0D;
        switch (sizeType) {
            case 1:
                fileSizeLong = Double.valueOf(df.format((double) fileS)).doubleValue();
                break;
            case 2:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024.0D)).doubleValue();
                break;
            case 3:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576.0D)).doubleValue();
                break;
            case 4:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1.073741824E9D)).doubleValue();
        }
        return fileSizeLong;
    }

    /**
     * 获取文件的MD5校验码
     *
     * @param filePath 文件路径
     * @return 文件的MD5校验码
     */
    public static String getFileMD5ToString(final String filePath) {
        File file = isSpace(filePath) ? null : new File(filePath);
        return getFileMD5ToString(file);
    }


    /**
     * 获取文件的MD5校验码
     *
     * @param file 文件
     * @return 文件的MD5校验码
     */
    public static String getFileMD5ToString(final File file) {
        return bytes2HexString(getFileMD5(file));
    }
    /**
     * 获取文件的MD5校验码
     *
     * @param file 文件
     * @return 文件的MD5校验码
     */
    public static byte[] getFileMD5(final File file) {
        if (file == null) return null;
        DigestInputStream dis = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            dis = new DigestInputStream(fis, md);
            byte[] buffer = new byte[1024 * 256];
            while (true) {
                if (!(dis.read(buffer) > 0)) break;
            }
            md = dis.getMessageDigest();
            return md.digest();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(dis);
        }
        return null;
    }

    /**
     * 关闭IO
     * @param closeables closeables
     */
    public static void closeIO(final Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * byteArr转hexString
     * <p>例如：</p>
     * bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns 00A8
     *
     * @param bytes 字节数组
     * @return 16进制大写字符串
     */
    private static String bytes2HexString(final byte[] bytes) {
        if (bytes == null) return null;
        int len = bytes.length;
        if (len <= 0) return null;
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = hexDigits[bytes[i] >>> 4 & 0x0f];
            ret[j++] = hexDigits[bytes[i] & 0x0f];
        }
        return new String(ret);
    }
}
