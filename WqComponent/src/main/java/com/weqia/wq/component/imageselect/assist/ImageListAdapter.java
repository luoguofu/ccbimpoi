/**
 * ImageGroupAdapter.java ImageChooser
 * 
 * Created by likebamboo on 2014-4-22 Copyright (c) 1998-2014 http://likebamboo.github.io/ All
 * rights reserved.
 */

package com.weqia.wq.component.imageselect.assist;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weqia.utils.StrUtil;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.imageselect.ImageEntity;
import com.weqia.utils.view.CommonImageView;
import com.weqia.wq.R;
import com.weqia.wq.component.SelectArrUtil;
import com.weqia.wq.component.imageselect.ImageListActivity;

import java.util.ArrayList;

/**
 * 某个图片组中图片列表适配器
 */
public class ImageListAdapter extends BaseAdapter {
    /**
     * 上下文对象
     */
    private ImageListActivity ctx = null;

    /**
     * 图片列表
     */
    private ArrayList<ImageEntity> mDataList = new ArrayList<ImageEntity>();

//    private int clounmWidth = 0;

    /**
     * 选中的图片列表
     */

    /**
     * 容器
     */
    // private View mContainer = null;

    public ImageListAdapter(ImageListActivity context, ArrayList<ImageEntity> list, View container) {
        mDataList = list;
        ctx = context;
        // mContainer = container;
//        clounmWidth =
//                (int) ((context.getResources().getDisplayMetrics().widthPixels - context
//                        .getResources().getDimension(R.dimen.spacing_medium) * 4) / 3);
    }

    public void setmDataList(ArrayList<ImageEntity> mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }


    @Override
    public ImageEntity getItem(int position) {
        if (position < 0 || position > mDataList.size()) {
            return null;
        }
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        final ImageEntity entity = getItem(position);
        
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(ctx).inflate(R.layout.image_list_item, null);
            holder.mImageIv = (CommonImageView) view.findViewById(R.id.list_item_iv);
            holder.mClickArea = view.findViewById(R.id.list_item_cb_click_area);
            holder.mSelectedCb = (ImageView) view.findViewById(R.id.list_item_cb);
            holder.llTalkPhoto = (RelativeLayout) view.findViewById(R.id.ll_take_photo);
            holder.rlImage = (RelativeLayout) view.findViewById(R.id.rl_image_list);
            holder.tvTake = (TextView) view.findViewById(R.id.tv_take);
            holder.ivPlay = (CommonImageView) view.findViewById(R.id.iv_video_play);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (entity == null) {
            return view;
        }
        if (position == 0 && ctx.containTake) {
            if (ctx.isbVideo()) {
                ViewUtils.hideView(holder.mClickArea);
                holder.tvTake.setText("拍摄视频");
                ViewUtils.showView(holder.ivPlay);
            } else {
                ViewUtils.hideView(holder.mClickArea);
                holder.tvTake.setText("拍摄照片");
                ViewUtils.hideView(holder.ivPlay);
            }
            ViewUtils.showView(holder.llTalkPhoto);
            ViewUtils.hideView(holder.rlImage);
        } else {
            if (ctx.isbVideo()) {
                ViewUtils.showView(holder.ivPlay);
            } else {
                ViewUtils.hideView(holder.ivPlay);
            }
            ViewUtils.showView(holder.mClickArea);
            ViewUtils.hideView(holder.llTalkPhoto);
            ViewUtils.showView(holder.rlImage);
        }
        
        if (ctx.getSelectSize() == 1) {
            ViewUtils.hideView(holder.mClickArea);
        }
        
        
        if (ctx.isbVideo()) {
            ViewUtils.hideView(holder.mClickArea);
        }
        
        // 可点区域单击事件
        holder.mClickArea.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = holder.mSelectedCb.isSelected();
                if (!checked) {
                    addDo(entity);
                } else {
                    SelectArrUtil.getInstance().deleteImage(entity.getPath());
                    SelectArrUtil.getInstance().deleteSourceImage(entity.getPath());
                }
                holder.mSelectedCb.setSelected(!checked);
                ctx.setRightBt();
                notifyDataSetChanged();
            }

            private void addDo(final ImageEntity entity) {
                if (!SelectArrUtil.getInstance().addImage(entity.getPath(),
                        ctx.getSelectSize())) {
                    return;
                }
                if (ctx.wantSourc) {
                    SelectArrUtil.getInstance().addSourceImage(entity.getPath());
                }
            }
        });

        if (StrUtil.notEmptyOrNull(entity.getPath())) {
            if (entity.getLocalUri() != null) {
                ctx.getBitmapUtil().displayImage(entity.getLocalUri().toString(), holder.mImageIv,
                        ctx.getBitmapUtil().getLocalOptions());
            } else {
                holder.mImageIv.setImageResource(R.drawable.img_loading);
            }
            // 该图片是否选中
            if (SelectArrUtil.getInstance().isSelImgContain(entity.getPath())) {
                holder.mSelectedCb.setSelected(true);
                setImageSeclet(holder.mImageIv, true);
            } else {
                holder.mSelectedCb.setSelected(false);
                setImageSeclet(holder.mImageIv, false);
            }
        }
        return view;
    }

    static class ViewHolder {
        public CommonImageView mImageIv;
        public View mClickArea;
        public ImageView mSelectedCb;
        public RelativeLayout llTalkPhoto;
        public RelativeLayout rlImage;
        public TextView tvTake;
        public CommonImageView ivPlay;
    }

    @SuppressLint("NewApi")
    private void setImageSeclet(com.weqia.utils.view.CommonImageView convertView, boolean showgray) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return;
        } else {
            if (convertView != null) {
                if (showgray) {
                    convertView.setAlpha(0.4f);
                } else {
                    convertView.setAlpha(1f);
                }
            }
        }
    }
}
