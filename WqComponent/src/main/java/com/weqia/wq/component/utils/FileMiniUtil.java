package com.weqia.wq.component.utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;

import com.weqia.utils.StrUtil;
import com.weqia.utils.datastorage.file.PathUtil;
import com.weqia.wq.R;

import java.io.File;

@SuppressLint("DefaultLocale")
public class FileMiniUtil {

    public static String[] getFileNameInfo(String name) {
        String start = name;
        String end = "";
        if (name.contains(".")) {
            start = name.substring(0, name.lastIndexOf("."));
            end = name.substring(name.lastIndexOf(".") + 1, name.length())
                    .toLowerCase();
            if (StrUtil.isEmptyOrNull(start)) {
                return new String[]{name, null};
            } else if (StrUtil.isEmptyOrNull(end)) {
                return new String[]{name, null};
            } else {
                return new String[]{start, end};
            }
        } else {
            return new String[]{name, null};
        }

    }
    //    if (StrUtil.isEmptyOrNull(PathUtil.getWQPath())) {
    //        return null;
    //    }

    //原始文件名称后面，增加[数字]的表现形式
    public static String getFileSaveName(String pathRoot, String name, String end, int i) {

        if (StrUtil.isEmptyOrNull(pathRoot)) {
            pathRoot = PathUtil.getWQPath();
        }
        StringBuilder sb = new StringBuilder();
        if (i > 0) {
            sb.append(name).append("(").append(i).append(")");
        } else {
            sb.append(name);
        }
        String newName = sb.toString();
        File file = null;
        if (StrUtil.notEmptyOrNull(end)) {
            file = new File(new File(pathRoot), newName
                    + "." + end);
        } else {
            file = new File(new File(pathRoot), newName
            );
        }
        if (file.exists()) {
            newName = getFileSaveName(pathRoot, name, end, ++i);
        }
        return newName;
    }

    public static boolean isZip(String fileName) {
        String end = fileName.substring(fileName.lastIndexOf(".") + 1,
                fileName.length()).toLowerCase();
        return end.equals("zip") || end.equals("autozip");
    }

    // 根据文件后缀选取合适的文件打开方式
    public static Intent openFile(String filePath) {

        if (StrUtil.isEmptyOrNull(filePath)) {
            return null;
        }

        File file = new File(filePath);
        if (file == null) {
            return null;
        }

        if (!file.exists() && !filePath.contains(".")) {
            return null;
        }
        String end = file
                .getName()
                .substring(file.getName().lastIndexOf(".") + 1,
                        file.getName().length()).toLowerCase();
        /* 依扩展名的类型决定endType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
                || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return getAudioFileIntent(filePath);
        } else if (end.equals("3gp") || end.equals("mp4")) {
            return getVideoFileIntent(filePath);
        } else if (isImage(end)) {
            return getImageFileIntent(filePath);
        } else if (end.equals("apk")) {
            return getApkFileIntent(filePath);
        } else if (end.startsWith("ppt")) {
            return getPptFileIntent(filePath);
        } else if (end.startsWith("xls")) {
            return getExcelFileIntent(filePath);
        } else if (end.startsWith("doc")) {
            return getWordFileIntent(filePath);
        } else if (end.equals("pdf")) {
            return getPdfFileIntent(filePath);
        } else if (end.equals("chm")) {
            return getChmFileIntent(filePath);
        } else if (end.equals("txt")) {
            return getTextFileIntent(filePath, false);
        } else {
            return getAllIntent(filePath);
        }
    }


    public static boolean supportPreView(String fileName) {
        if (StrUtil.isEmptyOrNull(fileName) || !fileName.contains(".")) {
            return false;
        }
        String end = fileName
                .substring(fileName.lastIndexOf(".") + 1,
                        fileName.length()).toLowerCase();
        if (end.startsWith("ppt") || end.startsWith("pptx") || end.startsWith("xls") || end.startsWith("xlsx")
                || end.startsWith("doc") || end.startsWith("docx") || end.startsWith("pdf")
                || end.startsWith("txt")
                ) {
            //            doc,docx,ppt,pptx,xls,xlsx,txt,pdf
            return true;
        }
        return false;
    }


    public static String getFileEnd(String fileName) {
        if (StrUtil.isEmptyOrNull(fileName) || !fileName.contains(".")) {
            return "";
        }
        String end = fileName
                .substring(fileName.lastIndexOf(".") + 1,
                        fileName.length()).toLowerCase();
        return end;
    }


    public static String getFileName(String filePath) {
        File file = new File(filePath);
        if (!file.exists())
            return null;
        return file.getName();
    }

    // 根据文件后缀确定文件类型,显示不同的图标
    public static int fileRId(String fileName) {
        if (StrUtil.isEmptyOrNull(fileName)) {
            return R.drawable.f_file;
        }
        if (!fileName.contains(".") || fileName.endsWith(".")) {
            return R.drawable.f_file;
        }
        String end = fileName.substring(fileName.lastIndexOf(".") + 1,
                fileName.length()).toLowerCase();
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
                || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return R.drawable.f_file;
        } else if (end.equals("3gp") || end.equals("mp4")) {
            return R.drawable.file_video;
        } else if (isImage(end)) {
            return R.drawable.f_img;
        } else if (end.equals("apk")) {
            return R.drawable.f_file;
        } else if (end.startsWith("ppt")) {
            return R.drawable.f_ppt;
        } else if (end.startsWith("xls")) {
            return R.drawable.f_xls;
        } else if (end.startsWith("doc")) {
            return R.drawable.f_doc;
        } else if (end.equals("pdf")) {
            return R.drawable.f_pdf;
        } else if (end.equals("chm")) {
            return R.drawable.f_file;
        } else if (end.equals("txt")) {
            return R.drawable.f_txt;
        } else if (end.equals("rar")) {
            return R.drawable.f_rar;
        } else if (end.equals("zip") || end.equals("autozip")) {
            return R.drawable.f_zip;
        } else if (end.equals("psd")) {
            return R.drawable.f_psd;
        } else if (end.equals("csv")) {
            return R.drawable.f_cvs;
        } else if (end.equals("html")) {
            return R.drawable.f_html;
        } else if (end.equals("key")) {
            return R.drawable.f_key;
        }
        Integer othId = bimResource(end, 3);
        if (othId == null) {
            return R.drawable.f_file;
        } else {
            return othId.intValue();
        }
    }

    public static int fileBigRId(String fileName) {
        if (StrUtil.isEmptyOrNull(fileName)) {
            return R.drawable.icon_big_weizhi;
        }
        if (!fileName.contains(".") || fileName.endsWith(".")) {
            return R.drawable.icon_big_weizhi;
        }
        String end = fileName.substring(fileName.lastIndexOf(".") + 1,
                fileName.length()).toLowerCase();
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
                || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return R.drawable.icon_big_weizhi;
        } else if (end.startsWith("ppt")) {
            return R.drawable.icon_big_ppt;
        } else if (end.startsWith("xls")) {
            return R.drawable.icon_big_xls;
        } else if (end.startsWith("doc") || end.startsWith("dot")) {
            return R.drawable.icon_big_doc;
        } else if (end.equals("pdf")) {
            return R.drawable.icon_big_pdf;
        } else if (end.equals("txt")) {
            return R.drawable.icon_big_txt;
        } else if (end.equals("psd")) {
            return R.drawable.icon_big_psd;
        } else if (end.equals("csv")) {
            return R.drawable.icon_big_csv;
        } else if (end.equals("html")) {
            return R.drawable.icon_big_html;
        } else if (end.equals("key")) {
            return R.drawable.icon_big_key;
        } else if (end.equals("rar")) {
            return R.drawable.icon_big_rar;
        } else if (end.equals("zip") || end.equals("autozip")) {
            return R.drawable.icon_big_zip;
        } else if (end.equals("3gp") || end.equals("mp4")) {
            return R.drawable.icon_big_vedio;
        } else if (isImage(end)) {
            return R.drawable.icon_big_img;
        }

        Integer othId = bimResource(end, 1);
        if (othId == null) {
            return R.drawable.f_file;
        } else {
            return othId.intValue();
        }
    }

    public static boolean isImage(String end) {
        return end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp");
    }

    public static boolean isVoice(String end) {
        return end.equals("m4a") || end.equals("mp3") || end.equals("mid")
                || end.equals("xmf") || end.equals("ogg") || end.equals("wav");
    }

    public static boolean isVedio(String end) {
        return end.equals("3gp") || end.equals("mp4");
    }

    public static boolean isImageEnd(String end) {
        return end.endsWith(".jpg") || end.endsWith(".gif") || end.endsWith(".png")
                || end.endsWith(".jpeg") || end.endsWith(".bmp");
    }

    // 根据文件后缀确定文件类型,显示不同的图标---发送-文件
    public static int sendFileRId(String fileName) {
        if (StrUtil.isEmptyOrNull(fileName)) {
            return R.drawable.bg_file;
        }
        if (!fileName.contains(".") || fileName.endsWith(".")) {
            return R.drawable.bg_file;
        }
        String end = fileName.substring(fileName.lastIndexOf(".") + 1,
                fileName.length()).toLowerCase();
        if (isVoice(end)) {
            return R.drawable.bg_file;
        } else if (isVedio(end)) {
            return R.drawable.file_video;
        } else if (isImage(end)) {
            return R.drawable.bg_file;
        } else if (end.equals("apk")) {
            return R.drawable.bg_file;
        } else if (end.startsWith("ppt")) {
            return R.drawable.bg_ppt;
        } else if (end.startsWith("xls")) {
            return R.drawable.bg_xls;
        } else if (end.startsWith("doc")) {
            return R.drawable.bg_doc;
        } else if (end.equals("pdf")) {
            return R.drawable.bg_pdf;
        } else if (end.equals("chm")) {
            return R.drawable.bg_file;
        } else if (end.equals("txt")) {
            return R.drawable.bg_txt;
        } else if (end.equals("zip") || end.equals("autozip")) {
            return R.drawable.bg_zip;
        } else if (end.equals("rar")) {
            return R.drawable.bg_rar;
        } else if (end.equals("psd")) {
            return R.drawable.bg_psd;
        }

        Integer othId = bimResource(end, 2);
        if (othId == null) {
            return R.drawable.bg_file;
        } else {
            return othId.intValue();
        }
    }

    // Android获取一个用于打开APK文件的intent
    public static Intent getAllIntent(String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    // Android获取一个用于打开APK文件的intent
    public static Intent getApkFileIntent(String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    // Android获取一个用于打开VIDEO文件的intent
    public static Intent getVideoFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    // Android获取一个用于打开AUDIO文件的intent
    public static Intent getAudioFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    // Android获取一个用于打开Html文件的intent
    public static Intent getHtmlFileIntent(String param) {

        Uri uri = Uri.parse(param).buildUpon()
                .encodedAuthority("com.android.htmlfileprovider")
                .scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    // Android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    // Android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    // Android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    // Android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    // Android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    // Android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent(String param, boolean paramBoolean) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean) {
            Uri uri1 = Uri.parse(param);
            intent.setDataAndType(uri1, "text/plain");
        } else {
            Uri uri2 = Uri.fromFile(new File(param));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }

    // Android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    public static Intent getUnKnowIntent(String param) {

        File file = new File(param);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //获取文件file的MIME类型
        String type = getMIMEType(file);
        //设置intent的data和Type属性。
        intent.setDataAndType(Uri.fromFile(file), type);
        //跳转
        return intent;
//
//        if (StrUtil.isEmptyOrNull(param)) {
//            return null;
//        }
//
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Uri uri = Uri.fromFile(new File(param));
////        intent.setDataAndType(uri, "application/*");
//        return intent;
    }


    /***根据文件后缀回去MIME类型****/

    private static String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名*/
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    private static final String[][] MIME_MapTable = {
            // {后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx",
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"}, {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"}, {".rtf", "application/rtf"},
            {".sh", "text/plain"}, {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"}, {".txt", "text/plain"},
            {".wav", "audio/x-wav"}, {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"}, {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"}, {"", "*/*"}};


    //---------------------------------------bim----

    /**
     * @param end
     * @param type 1， big图片
     *             2， 发送的背景
     *             3, 文件icon
     * @return
     */
    private static Integer bimResource(String end, int type) {
        if (end.equals("dwg")) {
            if (type == 1)
                return R.drawable.icon_big_dwg;
            else if (type == 2)
                return R.drawable.bg_dwg;
            else if (type == 3)
                return R.drawable.icon_dwg;
        } else if (end.equals("hsf")) {
            if (type == 1)
                return R.drawable.icon_big_hsf;
            else if (type == 2)
                return R.drawable.bg_hsf;
            else if (type == 3)
                return R.drawable.icon_hsf;
        } else if (end.equals("pbim")) {
            if (type == 1)
                return R.drawable.icon_big_pbim;
            else if (type == 2)
                return R.drawable.bg_pbim;
            else if (type == 3)
                return R.drawable.icon_pbim;
        } else if (end.equals("db")) {
            if (type == 1)
                return R.drawable.icon_big_db;
            else if (type == 2)
                return R.drawable.bg_db;
            else if (type == 3)
                return R.drawable.icon_db;
        } else if (end.equals("pmlod")) {
            if (type == 1)
                return R.drawable.icon_big_lod;
            else if (type == 2)
                return R.drawable.bg_lod;
            else if (type == 3)
                return R.drawable.icon_lod;
        } else if (end.equals("rvt")) {
            if (type == 1)
                return R.drawable.icon_big_rvt;
            else if (type == 2)
                return R.drawable.bg_rvt;
            else if (type == 3)
                return R.drawable.icon_rvt;
        } else if (end.equals("skp")) {
            if (type == 1)
                return R.drawable.icon_big_skp;
            else if (type == 2)
                return R.drawable.bg_skp;
            else if (type == 3)
                return R.drawable.icon_skp;
        }else if (end.equals("720z")) {
            if (type == 1)
                return R.drawable.icon_big_720z;
            else if (type == 2)
                return R.drawable.bg_720z;
            else if (type == 3)
                return R.drawable.icon_720z;
        }else if (end.equals("ifc")) {
            if (type == 1)
                return R.drawable.icon_big_ifc;
            else if (type == 2)
                return R.drawable.bg_ifc;
            else if (type == 3)
                return R.drawable.icon_ifc;
        }else if (end.equals("nwc")) {
            if (type == 1)
                return R.drawable.icon_big_nwc;
            else if (type == 2)
                return R.drawable.bg_nwc;
            else if (type == 3)
                return R.drawable.icon_nwc;
        }else if (end.equals("nwd")) {
            if (type == 1)
                return R.drawable.icon_big_nwd;
            else if (type == 2)
                return R.drawable.bg_nwd;
            else if (type == 3)
                return R.drawable.icon_nwd;
        }else if (end.equals("pmlink")) {
            if (type == 1)
                return R.drawable.icon_big_pmlink;
            else if (type == 2)
                return R.drawable.bg_pmlink;
            else if (type == 3)
                return R.drawable.icon_pmlink;
        }else if (end.equals("rte")) {
            if (type == 1)
                return R.drawable.icon_big_rte;
            else if (type == 2)
                return R.drawable.bg_rte;
            else if (type == 3)
                return R.drawable.icon_rte;
        }else if (end.equals("rfa")) {
            if (type == 1)
                return R.drawable.icon_big_rfa;
            else if (type == 2)
                return R.drawable.bg_rfa;
            else if (type == 3)
                return R.drawable.icon_rfa;
        }else if (type == 3) {
            return R.drawable.icon_mode;
        }

        return null;
    }

    /**
     * 模型列表能够上传的文件个格式
     * @param filePath
     * @return
     */
    public static boolean canUploadFile(String filePath) {
        if (isUpload(filePath)) {
            return true;
        }
        return false;
    }

    public static boolean canOpenFile(String filePath) {
        if (isDwg(filePath) || isHsf(filePath) || isDb(filePath))
            return true;
        return false;
    }

    public static boolean isDwg(String fileName) {
        String end = fileName.substring(fileName.lastIndexOf(".") + 1,
                fileName.length()).toLowerCase();
        return end.equals("dwg");
    }

    public static boolean isDb(String fileName) {
        String end = fileName.substring(fileName.lastIndexOf(".") + 1,
                fileName.length()).toLowerCase();
        return end.equals("db");
    }


    public static boolean isHsf(String fileName) {
        String end = fileName.substring(fileName.lastIndexOf(".") + 1,
                fileName.length()).toLowerCase();
        return end.equals("hsf") ||
                end.equals("pbim") ||
                end.equals("pmlod");
    }

    public static boolean isUpload(String fileName) {
        String end = fileName.substring(fileName.lastIndexOf(".") + 1,
                fileName.length()).toLowerCase();
        return end.equals("rvt") ||
                end.equals("skp") ||
                end.equals("720z") ||
                end.equals("ifc") ||
                end.equals("nwc") ||
                end.equals("nwd") ||
                end.equals("pmlink") ||
                end.equals("rte") ||
                end.equals("rfa") ||
                end.equals("pbim");
    }

    public static boolean wantConvert(String filePath) {
        if (StrUtil.isEmptyOrNull(filePath)) {
            return false;
        }
        String end = filePath.substring(filePath.lastIndexOf(".") + 1,
                filePath.length()).toLowerCase();
        if (end.equalsIgnoreCase("dwg") || end.equalsIgnoreCase("pbim") || end.equalsIgnoreCase("rvt") || end.equalsIgnoreCase("skp") || end.equalsIgnoreCase("zip"))
            return true;
        return false;
    }


}
