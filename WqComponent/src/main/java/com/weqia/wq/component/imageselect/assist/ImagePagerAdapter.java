/**
 * ImagePagerAdapter.java ImageChooser
 * <p>
 * Created by likebamboo on 2014-4-22 Copyright (c) 1998-2014 http://likebamboo.github.io/ All
 * rights reserved.
 */

package com.weqia.wq.component.imageselect.assist;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.weqia.utils.bitmap.BitmapUtil;
import com.weqia.utils.datastorage.file.PathUtil;
import com.weqia.utils.view.photo.PhotoView;
import com.weqia.wq.data.EnumData;
import com.weqia.wq.data.global.WeqiaApplication;

/**
 * 查看大图的ViewPager适配器
 */
public class ImagePagerAdapter extends PagerAdapter {
    /**
     * 数据源
     */
    private List<String> mDatas = new ArrayList<String>();


    public ImagePagerAdapter(ArrayList<String> dataList) {
        mDatas = dataList;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public View instantiateItem(final ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
        photoView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        String imgPath = (String) getItem(position);
        BitmapUtil bitmapUtil = WeqiaApplication.getInstance().getBitmapUtil();
        if (PathUtil.isPathInDisk(imgPath)) {
            bitmapUtil.displayImage(Scheme.FILE.wrap(imgPath), photoView,
                    bitmapUtil.getLocalOptions(), null);
        } else {
            bitmapUtil.load(photoView, imgPath, EnumData.ImageThumbTypeEnums.THUMB_BIG.value());
        }

        container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        return photoView;
    }

    public Object getItem(int position) {
        if (position < mDatas.size()) {
            return mDatas.get(position);
        } else {
            return null;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
