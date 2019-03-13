package com.weqia.wq.component.view.picture;

import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.datastorage.file.PathUtil;
import com.weqia.utils.view.CommonImageView;
import com.weqia.wq.R;
import com.weqia.wq.component.activity.SharedTitleActivity;
import com.weqia.wq.global.ComponentUtil;
import com.weqia.wq.component.utils.GlobalUtil;
import com.weqia.wq.component.utils.LnUtil;
import com.weqia.wq.component.utils.bitmap.PictureUtil;
import com.weqia.wq.component.video.VideoPlayActivity;
import com.weqia.wq.data.AttachmentData;
import com.weqia.wq.data.EnumData.AttachType;
import com.weqia.wq.data.EnumData.ImageThumbTypeEnums;
import com.weqia.wq.data.global.WeqiaApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// 图片显示组件
public class PictureAdapter extends BaseAdapter {

    private List<AttachmentData> items;
    private SharedTitleActivity ctx;
    private Point point;

    public PictureAdapter(SharedTitleActivity ctx, Point point) {
        this.ctx = ctx;
        this.point = point;
    }

    public void setItems(List<AttachmentData> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    @Override
    public AttachmentData getItem(int position) {
        if (items != null) {
            return items.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (items != null) {
            return position;
        } else {
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PictureViewHolder holder;
        if (convertView == null) {
//            if (ctx instanceof WeboSerachAcitivity) {
//                convertView = LayoutInflater.from(ctx).inflate(R.layout.cell_img_show_sreach, null);
//            } else {
                convertView = LayoutInflater.from(ctx).inflate(R.layout.cell_img_show, null);
//            }
            holder = new PictureViewHolder();
            holder.ivIcon = (CommonImageView) convertView.findViewById(R.id.iv_icon);
            holder.ivVideoPlay = (CommonImageView) convertView.findViewById(R.id.iv_video_play);
            convertView.setTag(holder);
        } else {
            holder = (PictureViewHolder) convertView.getTag();
        }
        setData(position, holder);
        return convertView;
    }

    public void setData(int position, final PictureViewHolder holder) {
        final AttachmentData data = getItem(position);
//        L.e("AttachmentData:" + data.toString());
        if (data == null) {
            return;
        }
//        if (ctx instanceof WeboSerachAcitivity) {
//            FrameLayout.LayoutParams moreParam =
//                    new FrameLayout.LayoutParams(PictureUtil.SERACH_SINGLE_WIDTH,PictureUtil.SERACH_SINGLE_WIDTH);
//            holder.ivIcon.setLayoutParams(moreParam);
//            holder.ivIcon.setScaleType(ScaleType.CENTER_CROP); //设置图片的显示的缩放比例，
//        } else {
            if (point == null) { //多张图片
                FrameLayout.LayoutParams moreParam =
                        new FrameLayout.LayoutParams(ComponentUtil.SINGLE_WIDTH,
                                ComponentUtil.SINGLE_WIDTH);
                holder.ivIcon.setLayoutParams(moreParam);
                holder.ivIcon.setScaleType(ScaleType.CENTER_CROP);
            } else {
                holder.ivIcon.setScaleType(ScaleType.FIT_START);  //一张图片
                holder.ivIcon.setAdjustViewBounds(true);
                ctx.resetCellImgSize(point, holder.ivIcon);
            }
//        }

        // final String path =
        // StrUtil.isEmptyOrNull(data.getPicUri())?data.getUrl():data.getPicUri();
        if (data.getType() == AttachType.PICTURE.value()) {
            ViewUtils.hideView(holder.ivVideoPlay);

            String netPath = data.getUrl();
            String path = "";
            if (PathUtil.isPathInDisk(netPath)) {
                path = netPath;
            } else {
                path = LnUtil.getLocalpath(netPath, data.getType());
            }
            if (StrUtil.isEmptyOrNull(path) || !new File(path).exists()) {
                path = netPath;
            }
            if (StrUtil.notEmptyOrNull(path)) {
                holder.ivIcon.setVisibility(View.VISIBLE);
                if (PathUtil.isPathInDisk(path)) {
                    ctx.getBitmapUtil().load(holder.ivIcon, Scheme.FILE.wrap(path), null);
                } else {
                    ctx.getBitmapUtil().load(holder.ivIcon, path,
                            ImageThumbTypeEnums.THUMB_SMALL.value());
                }
            }
        } else if (data.getType() == AttachType.VIDEO.value()) {
            ViewUtils.showView(holder.ivVideoPlay);
            holder.ivIcon.setVisibility(View.VISIBLE);
            String netPath = data.getUrl();
            String path = "";
            if (PathUtil.isPathInDisk(netPath)) {
                ctx.getBitmapUtil().displayImage(
                        GlobalUtil.getVideoUriByPath(ctx, netPath).toString(), holder.ivIcon,
                        ctx.getBitmapUtil().getLocalOptions());
//                return;
            } else {
                path = LnUtil.getContentPath(netPath, data.getType());
                if (StrUtil.isEmptyOrNull(path)) {
                    path = netPath;
                    String prew = data.getVideoPrew();
                    if (StrUtil.notEmptyOrNull(prew)) {
                        if (PathUtil.isPathInDisk(prew)) {
                            WeqiaApplication.getInstance().getBitmapUtil()
                                    .load(holder.ivIcon, prew, null);
                        } else {
                            WeqiaApplication
                                    .getInstance()
                                    .getBitmapUtil()
                                    .load(holder.ivIcon, data.getVideoPrew(),
                                            ImageThumbTypeEnums.THUMB_VERY_SMALL.value());
                        }
                    } else {
                        holder.ivIcon.setImageResource(R.drawable.video_icon);
                    }
                } else {
                    ctx.getBitmapUtil().displayImage(path, holder.ivIcon,
                            ctx.getBitmapUtil().getLocalOptions());
                }
            }
        }
        final ArrayList<String> listPic = new ArrayList<String>();
        for (AttachmentData item : items) {
            if (item.getType() == AttachType.PICTURE.value()) {
                listPic.add(item.getUrl());
            }
        }
        holder.ivIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                L.e("data:" + data.toString());
                if (data.getType() == AttachType.PICTURE.value()) {
                    // 多图浏览
                    String url = data.getUrl();
                    int pos = 0;
                    if (StrUtil.listNotNull(listPic)) {

                        for (int i = 0; i < listPic.size(); i++) {
                            if (listPic.get(i).equals(url)) {
                                pos = i;
                                break;
                            }
                        }
                        PictureUtil.viewPicture(ctx, listPic, pos,holder.ivIcon);
                    }
                } else if (data.getType() == AttachType.VIDEO.value()) {
                    // 视频
                    if (data != null) {
                        ctx.startToActivity(VideoPlayActivity.class, data);
                    }
                }
            }
        });
    }

    public class PictureViewHolder {
        public CommonImageView ivIcon;
        public CommonImageView ivVideoPlay;
    }
}
