/**
 * ImageLoadTask.java ImageSelector
 * 
 * Created by likebamboo on 2014-4-22 Copyright (c) 1998-2014 http://likebamboo.github.io/ All
 * rights reserved.
 */

package com.weqia.utils.imageselect;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;

import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.imageselect.service.OnTaskResultListener;

/**
 * 使用contentProvider扫描图片异步任务
 * 
 */
public class ImageLoadTask extends BaseTask {

    /**
     * 上下文对象
     */
    private Context mContext = null;

    /**
     * 存放图片<文件夹,该文件夹下的图片列表>键值对
     */
    private ArrayList<ImageGroup> mGruopList = new ArrayList<ImageGroup>();

    public ImageLoadTask(Context context) {
        super();
        mContext = context;
        result = mGruopList;
    }

    public ImageLoadTask(Context context, OnTaskResultListener listener) {
        super();
        mContext = context;
        result = mGruopList;
        setOnResultListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        Uri mUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = mContext.getContentResolver();
        Uri mImageUri = null;
        // 构建查询条件，且只查询jpeg和png的图片
        StringBuilder selection = new StringBuilder();
        selection.append(Media.MIME_TYPE).append("=?");
        selection.append(" or ");
        selection.append(Media.MIME_TYPE).append("=?");
        selection.append(" or ");
        selection.append(Media.MIME_TYPE).append("=?");
        selection.append(" or ");
        selection.append(Media.MIME_TYPE).append("=?");
        selection.append(" or ");
        selection.append(Media.MIME_TYPE).append("=?");

        Cursor mCursor = null;
        String[] projection =
                new String[] {MediaStore.MediaColumns._ID, MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.DATE_MODIFIED};
        try {
            // 初始化游标
            mCursor =
                    mContentResolver.query(mUri, projection, selection.toString(), new String[] {
                            "image/jpeg", "image/png", "image/jpg", "image/bmp", "image/gif"},
                            Media.DATE_MODIFIED + " desc");

            if (mCursor == null) {
                return false;
            }
            // 遍历结果
            while (mCursor.moveToNext()) {
                // 获取图片的路径
                String path = mCursor.getString(mCursor.getColumnIndex(Media.DATA));
                long time = mCursor.getLong(mCursor.getColumnIndex(Media.DATE_MODIFIED));
                
                int ringtoneID = mCursor.getInt(mCursor.getColumnIndex(MediaStore.MediaColumns._ID));
                mImageUri = Uri.withAppendedPath(mUri, "" + ringtoneID);
                
                // 获取该图片的所在文件夹的路径
                File file = new File(path);
                if (!file.exists() || file.length() == 0) {
                    continue;
                }
                String parentName = "";
                if (file.getParentFile() != null) {
                    parentName = file.getParentFile().getName();
                } else {
                    parentName = file.getName();
                }
                if (StrUtil.isEmptyOrNull(parentName)) {
                    continue;
                }
                // 构建一个imageGroup对象
                ImageGroup item = new ImageGroup();
                // 设置imageGroup的文件夹名称
                item.setDirName(parentName);
                // 寻找该imageGroup是否是其所在的文件夹中的第一张图片
                int searchIdx = mGruopList.indexOf(item);
                ImageEntity imageEntity = new ImageEntity(path, time, mImageUri);

                if (searchIdx >= 0) {
                    // 如果是，该组的图片数量+1
                    ImageGroup imageGroup = mGruopList.get(searchIdx);
                    imageGroup.addImage(imageEntity);
                } else {
                    // 否则，将该对象加入到groupList中
                    item.addImage(imageEntity);
                    mGruopList.add(item);
                }
            }
        } catch (Exception e) {
            // 输出日志
            L.w("--", e);
            return false;
        } finally {
            // 关闭游标
            if (mCursor != null && !mCursor.isClosed()) {
                mCursor.close();
            }
        }
        return true;
    }

}
