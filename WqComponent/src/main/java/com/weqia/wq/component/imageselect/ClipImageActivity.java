package com.weqia.wq.component.imageselect;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.weqia.utils.PhotoUtil;
import com.weqia.utils.bitmap.BitmapDecoder;
import com.weqia.utils.crop.ClipImageLayout;
import com.weqia.wq.R;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;
import com.weqia.wq.component.utils.ExifInterface;

import java.io.IOException;

/**
 * 裁剪图片的Activity
 * 
 */
public class ClipImageActivity extends SharedDetailTitleActivity {
    public static final String RESULT_PATH = "crop_image";
    private static final String KEY_SOURCE = "path";
    private static final String KEY_DES = "des";
    public static final String CLIPIMAGEPATH = "clip_image_path";
    private ClipImageLayout mClipImageLayout = null;
    private String des;

    public static void startActivity(Activity activity, String source, String des, int code) {
        Intent intent = new Intent(activity, ClipImageActivity.class);
        intent.putExtra(KEY_SOURCE, source);
        intent.putExtra(KEY_DES, des);
        activity.startActivityForResult(intent, code);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_image_layout);

        mClipImageLayout = (ClipImageLayout) findViewById(R.id.clipImageLayout);
        String path = getIntent().getStringExtra(KEY_SOURCE);
        des = getIntent().getStringExtra(KEY_DES);

        sharedTitleView.initTopBanner("裁剪", "使用");

        // 有的系统返回的图片是旋转了，有的没有旋转，所以处理
        int degreee = readBitmapDegree(path);
        Bitmap bitmap = BitmapDecoder.decodeSampledBitmapFromFile(path, 1280, 1920);
        if (bitmap != null) {
            if (degreee == 0) {
                mClipImageLayout.setImageBitmap(bitmap);
            } else {
                mClipImageLayout.setImageBitmap(rotateBitmap(degreee, bitmap));
            }
        } else {
            finish();
        }
    }


    @Override
    protected void onClickDo(View v) {
        super.onClickDo(v);
        if (v == sharedTitleView.getButtonStringRight()) {
            Bitmap bitmap = mClipImageLayout.clip();
            PhotoUtil.saveBitmapToDisk(bitmap, des);
            Intent intent = new Intent();
            intent.putExtra(CLIPIMAGEPATH, des);
            setResult(RESULT_OK, intent);
            this.finish();
        }
    }

    // 读取图像的旋转度
    private int readBitmapDegree(String path) {
        int degree = 0;
        if (Build.VERSION.SDK_INT >= 25) {
            return 0;
        }
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation =
                    exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    // 旋转图片
    private Bitmap rotateBitmap(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap =
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
                        false);
        return resizedBitmap;
    }

    // private void saveBitmap(Bitmap bitmap, String path) {
    // File f = new File(path);
    // if (f.exists()) {
    // f.delete();
    // }
    //
    // FileOutputStream fOut = null;
    // try {
    // f.createNewFile();
    // fOut = new FileOutputStream(f);
    // bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
    // fOut.flush();
    // } catch (IOException e1) {
    // e1.printStackTrace();
    // } finally {
    // try {
    // if (fOut != null) fOut.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // }
}
