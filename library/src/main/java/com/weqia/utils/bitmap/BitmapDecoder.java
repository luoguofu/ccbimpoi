package com.weqia.utils.bitmap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;

import com.nostra13.universalimageloader.core.decode.BaseImageDecoder.ExifInfo;
import com.nostra13.universalimageloader.utils.UILL;
import com.weqia.BitmapInit;
import com.weqia.utils.StrUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
public class BitmapDecoder {

    @SuppressWarnings("deprecation")
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth,
                                                         int reqHeight) {

        UILL.i("decode bitmap from resource, width = " + reqWidth + " height = " + reqHeight);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeResource(res, resId, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    public static Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth, int reqHeight) {
        File file = new File(filename);
        if (!file.exists()) {
            BitmapInit.ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri
                    .parse("file://" + filename)));
            return null;
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        BitmapFactory.decodeFile(filename, options);

        final int height = options.outHeight;
        final int width = options.outWidth;
        UILL.e(options.outHeight + "--------" + options.outWidth);
        float inSampleSize = 1;

//        int div = 1;
        if (height > reqWidth || width > reqWidth) {
            if (width > height) {
//                div = divisor(width, height);
                inSampleSize = (float) width / (float) reqWidth;
                // reqHeight = (int) (((float) (height / div) / (width / div)) * reqWidth);
            } else {
//                div = divisor(height, width);
                inSampleSize = (float) height / (float) reqWidth;
                // reqHeight = (int) (((float) (width / div) / (height / div)) * reqWidth);
            }
            // final float totalPixels = width * height;
            // final float totalReqPixelsCap = reqWidth * reqHeight * 2;
            // while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            // inSampleSize++;
            // }
        }
        int wantSampe = (int) Math.ceil(inSampleSize);
        if (wantSampe % 2 != 0) {
            wantSampe += 1;
        }
        options.inSampleSize = wantSampe;// calculateInSampleSize(options, reqWidth,
        options.inJustDecodeBounds = false;
        options.inScaled = true;
        try {
            Bitmap bitmap = getBitmap(filename, options);
//            L.e("width = " + bitmap.getWidth() + ", height = " + bitmap.getHeight());
            return bitmap;
        } catch (OutOfMemoryError e) {
            UILL.e("图片溢出一次");
            options.inSampleSize = options.inSampleSize * 4;
            try {
                return getBitmap(filename, options);
            } catch (OutOfMemoryError e2) {
                options.inSampleSize = options.inSampleSize * 8;
                UILL.e("图片溢出两次");
                try {
                    return getBitmap(filename, options);
                } catch (OutOfMemoryError e3) {
                    UILL.e("图片溢出三次，内存真心太小了，不管了");
                    return null;
                }
            }
        }
    }

    private static Bitmap getBitmap(String filename, BitmapFactory.Options options) {
        Bitmap bitmap = BitmapFactory.decodeFile(filename, options);
        ExifInfo info = ImageUtil.getPictureDegree(filename);
        if (info.flipHorizontal || info.rotation != 0) {
            bitmap = ImageUtil.rotaingImageView(bitmap, info);
        }
        return bitmap;
    }

    // @SuppressWarnings("deprecation")
    // public static Bitmap decodeSampledBitmapFromByteArray(byte[] data, int offset, int length,
    // int reqWidth, int reqHeight) {
    // final BitmapFactory.Options options = new BitmapFactory.Options();
    // options.inJustDecodeBounds = true;
    // options.inPurgeable = true;
    // BitmapFactory.decodeByteArray(data, offset, length, options);
    // options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
    // options.inJustDecodeBounds = false;
    //
    // return BitmapFactory.decodeByteArray(data, offset, length, options);
    // }


    public static int divisor(int m, int n) {
        if (m % n == 0) {
            return n;
        } else {
            return divisor(n, m % n);
        }
    }

//    private static int calculateInSampleSize(BitmapFactory.Options options, int bigLength,
//            int reqHeights) {
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        UILL.e(options.outHeight + "--------" + options.outWidth);
//        float inSampleSize = 1;
//
//        int div;
//        L.e("width == " + width + ", height == " + height);
//        if (height > bigLength || width > bigLength) {
//            if (width > height) {
//                div = divisor(width, height);
//                L.e(div + "----");
//                inSampleSize = (float) width / (float) bigLength;
//            } else {
//                div = divisor(height, width);
//                L.e(div + "----+++++++++");
//                inSampleSize = (float) height / (float) bigLength;
//            }
//            // final float totalPixels = width * height;
//            // final float totalReqPixelsCap = reqWidth * reqHeight * 2;
//            // while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
//            // inSampleSize++;
//            // }
//        }
//
//
//        return (int) inSampleSize;
//    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    public static Bitmap getCompressedBitmap(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    @SuppressLint("NewApi")
    public static int getBitmapSize(Bitmap bitmap) {
        int size = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // API 19
            size = bitmap.getAllocationByteCount();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {// API 12
            size = bitmap.getByteCount();
        } else
            size = bitmap.getRowBytes() * bitmap.getHeight(); // earlier version
        UILL.e("bitmap size = " + StrUtil.formatFileSize((float) (size / 1024.0) + ""));
        return size;
    }
}
