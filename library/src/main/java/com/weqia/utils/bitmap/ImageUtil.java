package com.weqia.utils.bitmap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.media.ExifInterface;
import android.util.Log;

import com.nostra13.universalimageloader.core.decode.BaseImageDecoder.ExifInfo;
import com.weqia.BitmapInit;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.datastorage.file.NativeFileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("unused")
public class ImageUtil {

    // Resource → Bitmap
    public static Bitmap convertResource2Bitmap(Context context, Integer resId) {
        Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), resId);
        return decodeResource;
    }

    public static Bitmap convertResource2Bitmap(Integer resId) {
        try {
            Bitmap decodeResource =
                    BitmapFactory.decodeResource(BitmapInit.ctx.getResources(), resId);
            return decodeResource;
        } catch (OutOfMemoryError e) {
            L.w("--", e);
        }
        return null;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE
                        ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    // // Drawable → Bitmap
    // public static Bitmap convertDrawable2Bitmap(Drawable drawable) {
    // BitmapDrawable bd = (BitmapDrawable) drawable;
    // return bd.getBitmap();
    // }
    //
    // // Bitmap → Drawable
    // public static Drawable convertBitmap2Drawable(Bitmap bitmap) {
    // BitmapDrawable bd = new BitmapDrawable(bitmap);
    // // 因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
    // return bd;
    // }


    @SuppressWarnings("deprecation")
    public static Bitmap readBitMap(Context ctx, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = ctx.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int roundPx) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        float round = w / 15;
        if (round <= 8) {
            round = 8;
        }
        canvas.drawRoundRect(rectF, round, round, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    private static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        try {
            if (bitmap == null) {
                return null;
            }
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, w, h);
            final RectF rectF = new RectF(rect);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            float round = w / 15;
            if (round <= 8) {
                round = 8;
            }
            canvas.drawRoundRect(rectF, round, round, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            return output;
        } catch (Exception e) {
            L.w("--", e);
        }
        return bitmap;

    }

    private static Bitmap getRoundedCornerBitmap(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap bitmap = bd.getBitmap();
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, 8, 8, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static void setRoundedImage(Integer id, com.weqia.utils.view.CommonImageView iv) {
        Bitmap bitmap = BitmapFactory.decodeResource(BitmapInit.ctx.getResources(), id);
        try {
            iv.setImageBitmap(ImageUtil.getRoundedCornerBitmap(bitmap, 50));
        } catch (Exception e) {
            L.w("--", e);
        }

    }

    /**
     * 获取图片的旋转角
     *
     * @return
     */
    public static ExifInfo getPictureDegree(String filepath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri uri = Uri.fromFile(new File(filepath)); // the URI you've received from the other app
            InputStream in = null;
            try {
                if (BitmapInit.ctx == null)
                    return new ExifInfo(0, false);
                in = BitmapInit.ctx.getContentResolver().openInputStream(uri);
                ExifInterface exif = new ExifInterface(in);
                // Now you can extract any Exif tag you want
                // Assuming the image is a JPEG or supported raw format
                return getExifInfo(exif);
            } catch (IOException e) {
                // Handle any errors
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        } else {
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(filepath);
                return getExifInfo(exif);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ExifInfo(0, false);
    }

    private static ExifInfo getExifInfo(ExifInterface exif) {
        int rotation = 0;
        boolean flip = false;
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                flip = true;
            case ExifInterface.ORIENTATION_NORMAL:
                rotation = 0;
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                flip = true;
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotation = 90;
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                flip = true;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotation = 180;
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                flip = true;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotation = 270;
                break;
        }
        return new ExifInfo(rotation, flip);
    }

    /**
     * 旋转图片
     *
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(Bitmap subsampledBitmap, ExifInfo info) {
        if (L.D) L.i("图片旋转");
        Matrix m = new Matrix();
        if (info.flipHorizontal) {
            m.postScale(-1, 1);
        }
        if (info.rotation != 0) {
            m.postRotate(info.rotation);
        }
        Bitmap finalBitmap =
                Bitmap.createBitmap(subsampledBitmap, 0, 0, subsampledBitmap.getWidth(),
                        subsampledBitmap.getHeight(), m, true);
        if (finalBitmap != subsampledBitmap) {
            subsampledBitmap.recycle();
        }
        return finalBitmap;

        // if (L.D) L.i("图片旋转");
        // // 旋转图片 动作
        // Matrix matrix = new Matrix();
        // matrix.postRotate(angle);
        //
        // // 创建新的图片
        // Bitmap resizedBitmap =
        // Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
        // true);
        // return resizedBitmap;
    }

    /**
     * 图片内存回收
     *
     * @param bitmap
     * @Description
     */
    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    public static void recycleImageView(final com.weqia.utils.view.CommonImageView iv)
            throws ClassCastException {
        BitmapDrawable drawable = (BitmapDrawable) iv.getDrawable();
        Bitmap bmp = drawable.getBitmap();
        ImageUtil.recycleBitmap(bmp);
    }

    public static void saveMyBitmap(Bitmap bitmap, String path) {
        FileOutputStream fOut = null;

        File f = new File(path);
        if (f.exists()) {
            NativeFileUtil.delFile(f);
        }
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveMyBitmap(Context mContext, Bitmap bitmap, String desName) {
        FileOutputStream fOut = null;

        try {
            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                // LogUtil.d("sdcard doesn't exit, save png to app dir");
                fOut = mContext.openFileOutput(desName + ".png", Context.MODE_PRIVATE);
            } else {
                File f =
                        new File(Environment.getExternalStorageDirectory().getPath()
                                + "/Asst/cache/" + desName + ".png");
                f.createNewFile();
                fOut = new FileOutputStream(f);
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getScaleBitmap(Resources res, int id) {

        Bitmap bitmap = null;
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, id, opts);

            opts.inSampleSize = computeSampleSize(opts, -1, 800 * 480);
            // Log.d("BitmapUtil","inSampleSize===>"+opts.inSampleSize);
            opts.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeResource(res, id, opts);
        } catch (OutOfMemoryError err) {
            Log.d("BitmapUtil", "[getScaleBitmap] out of memory");

        }
        return bitmap;
    }

    private static int computeSampleSize(BitmapFactory.Options options, int minSideLength,
                                         int maxNumOfPixels) {

        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;

    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength,
                                                int maxNumOfPixels) {

        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound =
                (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound =
                (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }

    }

    public static Bitmap getCombineBitmaps(List<MyBitmapEntity> mEntityList, Bitmap... bitmaps) {
        Bitmap newBitmap = Bitmap.createBitmap(150, 150, Config.ARGB_8888);
        for (int i = 0; i < mEntityList.size(); i++) {
            newBitmap =
                    mixtureBitmap(newBitmap, bitmaps[i], new PointF(mEntityList.get(i).getX(),
                            mEntityList.get(i).getY()));
        }
        return newBitmap;
    }


    /**
     * @param first
     * @param second
     * @param fromPoint
     * @return
     */
    private static Bitmap mixtureBitmap(Bitmap first, Bitmap second, PointF fromPoint) {
        if (first == null || second == null || fromPoint == null) {
            return null;
        }
        Bitmap newBitmap =
                Bitmap.createBitmap(first.getWidth(), first.getHeight(), Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        cv.drawBitmap(first, 0, 0, null);
        cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();
        return newBitmap;
    }


    // 得到图片的宽/高
    public static final float getImageScale(String imagePath) {
        if (StrUtil.isEmptyOrNull(imagePath)) {
            return 1.0f;
        }
        ExifInfo degree = getPictureDegree(imagePath);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false;
        int h = options.outHeight;
        int w = options.outWidth;

        return getScaleDo(degree, h, w);
    }

    private static float getScaleDo(ExifInfo degree, int h, int w) {
        float scale = 1.00f;
        if (degree.flipHorizontal) {
            if (h != 0) {
                scale = w * 1.00f / h;
            }
        } else {
            if (degree.rotation == 0 || degree.rotation == 180) {
                if (h != 0) {
                    scale = w * 1.00f / h;
                }
            } else if (degree.rotation == 90 || degree.rotation == 270) {
                if (w != 0) {
                    scale = h * 1.00f / w;
                }
            } else {
                if (h != 0) {
                    scale = w * 1.00f / h;
                }
            }
        }

        if (scale >= 2) {
            scale = 2.00f;
        } else if (scale <= 0.5) {
            scale = 0.50f;
        }
        BigDecimal bg = new BigDecimal(scale);
        float f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

        return f1;
    }

    public static final Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap =
                ThumbnailUtils.extractThumbnail(bitmap, width, height,
                        ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 获取视频的缩略图 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。 其中，MINI_KIND: 512 x
     *                  384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    public static final Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        if (L.D) L.i("w" + bitmap.getWidth());
        if (L.D) L.i("h" + bitmap.getHeight());
        bitmap =
                ThumbnailUtils.extractThumbnail(bitmap, width, height,
                        ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    //
    // public static String compressAndSaveIma(String fileImage) throws Exception {
    // try {
    // Bitmap bitmap =
    // BitmapDecoder
    // .decodeSampledBitmapFromFile(fileImage,
    // UtilsConstants.DEFAULT_IMAGE_HEIGHT,
    // UtilsConstants.DEFAULT_IMAGE_WIDTH);
    // File original = new File(fileImage);
    // File file =
    // new File((PathUtil.getWQPath() + File.separator + "temp_" + original.getName()));
    // FileOutputStream stream = new FileOutputStream(file);
    // bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
    // stream.flush();
    // stream.close();
    // return file.getAbsolutePath();
    // } catch (IOException e) {
    // // e.printStackTrace();
    // throw e;
    // } catch (Exception e) {
    // // e.printStackTrace();
    // throw new Exception("Corrupt or deleted file???");
    // }
    // }
}
