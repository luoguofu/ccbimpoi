package com.weqia.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.weqia.data.UtilsConstants;
import com.weqia.utils.datastorage.file.FileUtil;
import com.weqia.utils.datastorage.file.PathUtil;
import com.weqia.utils.exception.CheckedExceptionHandler;

public abstract class PhotoUtil {

    private static String picPath;
    // private File mCurrentPhotoFile;
    private Activity ctx;

    public static final int REQUESTCODE_CROP = 10113;
    private static final String IMAGE_FILE_LOCATION = "file:///mnt/sdcard/temp.jpg";

    public PhotoUtil(Activity ctx) {
        this.ctx = ctx;
        picPath = PathUtil.getPicturePath();
        if (StrUtil.notEmptyOrNull(picPath)) {} else {
            if (L.D) {
                L.e("Can't cache to path, storage can not use");
            }
        }
    }

    public interface CropInterface {
        void cropSuccess();
    }

    /**
     * 获取临时的uri
     * 
     * @return
     * @Description
     */
    public Uri getTempUri() {

        if (picPath != null) {
            File file = new File(picPath, "temp.jpg");
            if (file != null) {
                if (!file.exists()) {
                    try {
                        FileUtil.getFile(picPath, "temp.jpg");
                    } catch (IOException e) {
                        CheckedExceptionHandler.handleException(e);
                    }
                }
                Uri uri = Uri.fromFile(file);
                return uri;
            }
        }
        return Uri.parse(IMAGE_FILE_LOCATION);
    }

    /**
     * 相机拍照
     * 
     * @param file
     * @Description
     */
    public void doGetFromPhoto() {
        try {

            if (DeviceUtil.isSDCardAvailable()) {
                Uri uri = getTempUri();
                if (uri == null) {
                    L.toastShort("没有可用的存储空间");
                    return;
                }
                final Intent intent = getTakePhotoIntent(uri);
                ctx.startActivityForResult(intent, UtilsConstants.REQUESTCODE_PHOTO);
            } else {
                L.toastLong("存储卡已拔出,拍照功能暂时不可用");
            }

        } catch (ActivityNotFoundException e) {
            CheckedExceptionHandler.handleException(e);
        }
    }

    /**
     * 拍照请求的intent
     * 
     * @param context
     * @Description
     */
    private Intent getTakePhotoIntent(Uri uri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    public void doGetFromPicker() {
        try {
            Uri uri = getTempUri();
            if (uri == null) {
                L.toastShort("没有可用的存储空间");
                return;
            }
            final Intent intent = getPhotoPickIntent(uri);
            ctx.startActivityForResult(intent, UtilsConstants.REQUESTCODE_PICKER);
        } catch (ActivityNotFoundException e) {
            CheckedExceptionHandler.handleException(e);
        }
    }

    /**
     * Gallery请求的intent
     * 
     * @param crop
     * @return
     * @Description
     */
    private Intent getPhotoPickIntent(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("return-data", false);
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false); // no face detection
        return intent;
    }

    /**
     * 保存图片
     * 
     * @param bitmap
     * @Description
     */
    public static void saveBitmapToDisk(final Bitmap bitmap, final String path) {
        if (bitmap == null || StrUtil.isEmptyOrNull(path)) {
            return;
        }
        FileOutputStream output = null;
        File file = new File(path);
        try {
            output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, output);// 把数据写入文件
        } catch (FileNotFoundException e) {
            CheckedExceptionHandler.handleException(e);
        } finally {
            try {
                if (output != null) {
                    output.flush();
                    output.close();
                }
            } catch (IOException e) {
                CheckedExceptionHandler.handleException(e);
            }
        }
    }

    /**
     * 保存图片
     * 
     * @param bitmap
     * @Description
     */
    public static void saveBigBitmapToDisk(final Bitmap bitmap, final String path) {
        if (bitmap == null || StrUtil.isEmptyOrNull(path)) {
            return;
        }
        FileOutputStream output = null;
        File file = new File(path);
        try {
            output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);// 把数据写入文件
        } catch (FileNotFoundException e) {
            CheckedExceptionHandler.handleException(e);
        } finally {
            try {
                if (output != null) {
                    output.flush();
                    output.close();
                }
            } catch (IOException e) {
                CheckedExceptionHandler.handleException(e);
            }
        }
    }

//    public void upLoadBitmap(final Bitmap bitmap, final Integer iType,
//            final RequestCallBack<?> requester) {
//        upLoadBitmap(true, bitmap, iType, requester);
//    }
//
//    public abstract void upLoadBitmap(boolean showDlg, final Bitmap bitmap, final Integer iType,
//            final RequestCallBack<?> requester);
}
