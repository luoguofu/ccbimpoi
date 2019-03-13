package com.weqia.utils.bitmap;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import android.content.Context;
import android.graphics.Bitmap;

import com.weqia.BitmapInit;
import com.weqia.utils.MResource;
import com.weqia.utils.StrUtil;

public class MutilImageUtil {

    public static boolean addImg(ArrayList<String> mids,
            com.weqia.utils.view.CommonImageView mImageView, Context ctx) {
        if (mids == null || mids.size() == 0) {
            return false;
        }
        List<MyBitmapEntity> mEntityList = getBitmapEntitys(mids.size());
        if (StrUtil.listIsNull(mEntityList)) {
            return false;
        }
        Bitmap[] mBitmaps = new Bitmap[mEntityList.size()];
        Bitmap combineBitmap = ImageUtil.getCombineBitmaps(mEntityList, mBitmaps);
        mImageView.setImageBitmap(combineBitmap);
        ImageUtil.saveMyBitmap(ctx, combineBitmap, "flyfly" + mEntityList.size());
        return true;
    }

    public static List<MyBitmapEntity> getBitmapEntitys(int count) {
        List<MyBitmapEntity> mList = new LinkedList<MyBitmapEntity>();
        int dataId = MResource.getIdByName(BitmapInit.ctx.getPackageName(), "raw", "util_data");
        if (dataId == 0) {
            return null;
        }
        String value = readData(String.valueOf(count), dataId);
        if (value == null) {
            return null;
        }
        String[] arr1 = value.split(";");
        int length = arr1.length;
        for (int i = 0; i < length; i++) {
            String content = arr1[i];
            String[] arr2 = content.split(",");
            MyBitmapEntity entity = null;
            for (int j = 0; j < arr2.length; j++) {
                entity = new MyBitmapEntity();
                entity.setX(Float.valueOf(arr2[0]));
                entity.setY(Float.valueOf(arr2[1]));
                entity.setWidth(Float.valueOf(arr2[2]));
                entity.setHeight(Float.valueOf(arr2[3]));
            }
            mList.add(entity);
        }
        return mList;
    }

    public static String readData(String key, int resId) {
        Properties props = new Properties();
        try {
            InputStream in =
                    new BufferedInputStream(BitmapInit.ctx.getResources().openRawResource(resId));
            props.load(in);
            in.close();
            String value = props.getProperty(key);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
