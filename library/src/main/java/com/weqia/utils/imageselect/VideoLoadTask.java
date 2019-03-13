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
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Media;

import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.imageselect.service.OnTaskResultListener;

/**
 * 使用contentProvider扫描图片异步任务
 * 
 * @author likebamboo
 */
public class VideoLoadTask extends BaseTask {

    /**
     * 上下文对象
     */
    private Context mContext = null;

    /**
     * 存放图片<文件夹,该文件夹下的图片列表>键值对
     */
    private ArrayList<ImageGroup> mGruopList = new ArrayList<ImageGroup>();

    public VideoLoadTask(Context context) {
        super();
        mContext = context;
        result = mGruopList;
    }

    public VideoLoadTask(Context context, OnTaskResultListener listener) {
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
        Cursor cursor = null;
        Uri mUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Uri mImageUri = null;
        try {
            ContentResolver contentResolver = mContext.getContentResolver();
            String[] projection =
                    new String[] {MediaStore.MediaColumns._ID, MediaStore.Video.Media.DATA,
                            MediaStore.Video.Media.DATE_MODIFIED, MediaStore.Video.Media.DURATION};
            cursor =
                    contentResolver.query(mUri, projection, null, null,
                            MediaStore.Video.Media.DEFAULT_SORT_ORDER);
            cursor.moveToFirst();
            int fileNum = cursor.getCount();

            for (int counter = 0; counter < fileNum; counter++) {

                int ringtoneID = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                mImageUri = Uri.withAppendedPath(mUri, "" + ringtoneID);

                String path = cursor.getString(cursor.getColumnIndex(Media.DATA));
                long time = cursor.getLong(cursor.getColumnIndex(Media.DATE_MODIFIED));
                long duration =
                        cursor.getLong(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                // 获取该图片的所在文件夹的路径
                File file = new File(path);
                if (!file.exists()) {
                    mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri
                            .parse("file://" + path)));
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
                imageEntity.setDuration(duration);
                if (searchIdx >= 0) {
                    // 如果是，该组的图片数量+1
                    ImageGroup imageGroup = mGruopList.get(searchIdx);
                    imageGroup.addImage(imageEntity);
                } else {
                    // 否则，将该对象加入到groupList中
                    item.addImage(imageEntity);
                    mGruopList.add(item);
                }
                cursor.moveToNext();
            }
        } catch (Exception e) {
            // 输出日志
            L.w("--", e);
            return false;
        } finally {
            // 关闭游标
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return true;
    }
}
