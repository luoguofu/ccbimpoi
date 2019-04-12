package com.example.ccbim.ccbimpoi.util;

import android.app.Activity;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.TimeUtils;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.dialog.SharedDateDialog;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by lgf on 2019/4/4.
 */

public class BaseUtil {
    public interface TimeSelectDialogClickListener {
        void timeSelectedListener(View view);
    }

    public static TimeSelectDialogClickListener mClickListener;
    public static void setmClickListener(TimeSelectDialogClickListener mClickListener) {
        BaseUtil.mClickListener = mClickListener;
    }

    public static int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }
    public static void selectDateTimeDialog(Activity ctx, Long initDate, final TextView showTvDate, String dialogTitle, final boolean showTimeToTv, final Long compareTime, final String toastMsg) {
        SharedDateDialog bDateDialog =
                new SharedDateDialog(ctx, false, initDate, dialogTitle,
                        new SharedDateDialog.onDateChangedListener() {

                            @Override
                            public void dateChanged(Long date) {
                                if (compareTime != null && compareTime > date) {
                                    if (StrUtil.notEmptyOrNull(toastMsg)) {
                                        L.toastShort(toastMsg);
                                    } else {
                                        L.toastShort("选择时间有误！");
                                    }
                                    return;
                                }
                                if (showTimeToTv) {
                                    ViewUtils.setTextView(showTvDate,
                                            TimeUtils.getDateYMDFromLong(date));
                                }
                                showTvDate.setTag(date);
                                if (mClickListener != null) {
                                    mClickListener.timeSelectedListener(showTvDate);
                                }
                            }
                        });
        bDateDialog.show();
    }


    /**
     * 删除目录
     * @param dirPath 目录路径
     * @return {@code true}: 删除成功<br>{@code false}: 删除失败
     */
    public static void deleteDir(final String dirPath) {
        deleteDir(getFileByPath(dirPath));
    }

    /**
     * 删除目录
     * @param dir 目录
     * @return {@code true}: 删除成功<br>{@code false}: 删除失败
     */
    public static void deleteDir(final File dir) {
        if (dir == null) return;
        // 目录不存在返回true
        if (!dir.exists()) return;
        // 不是目录返回false
        if (!dir.isDirectory()) return;
        // 现在文件存在且是文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    String fileName = file.getName();
                    if (fileName.equals("hsf") && file.isDirectory()) {
//                        L.e("hsf是材质文件夹，不能删除！");
                        continue;
                    } else {
                        deleteFile(file);
                    }
                }
                file.delete();
            }
        }
    }

    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 根据文件路径获取文件
     * @param filePath 文件路径
     * @return 文件
     */
    public static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
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


    /**
     * 压缩文件
     *
     * @param folderString
     * @param fileString
     * @param zipOutputSteam
     * @throws Exception
     */
    private static void ZipFiles(String folderString, String fileString, ZipOutputStream zipOutputSteam) throws Exception {
        L.e("folderString:" + folderString + "\n" +
                "fileString:" + fileString + "\n==========================");
        if (zipOutputSteam == null)
            return;
        File file = new File(folderString + fileString);
        if (file.isFile()) {
            ZipEntry zipEntry = new ZipEntry(fileString);
            FileInputStream inputStream = new FileInputStream(file);
            zipOutputSteam.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1) {
                zipOutputSteam.write(buffer, 0, len);
            }
            zipOutputSteam.closeEntry();
        } else {
            //文件夹
            String fileList[] = file.list();
            //没有子文件和压缩
            if (fileList.length <= 0) {
                ZipEntry zipEntry = new ZipEntry(fileString + File.separator);
                zipOutputSteam.putNextEntry(zipEntry);
                zipOutputSteam.closeEntry();
            }
            //子文件和递归
            for (int i = 0; i < fileList.length; i++) {
                ZipFiles(folderString + fileString + "/", fileList[i], zipOutputSteam);
            }
        }
    }


}
