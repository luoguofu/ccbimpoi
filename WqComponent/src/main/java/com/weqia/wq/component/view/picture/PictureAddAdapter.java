package com.weqia.wq.component.view.picture;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.weqia.utils.L;
import com.weqia.utils.view.CommonImageView;
import com.weqia.wq.R;
import com.weqia.wq.data.global.GlobalConstants;

// 带有+的图片选取组件
public abstract class PictureAddAdapter extends BaseAdapter {

    private List<String> paths = new ArrayList<>();
    private Context ctx;
    private boolean showDelete = false;
    // 空的时候是否显示添加的按钮
    private boolean blankShow = true;
    private boolean addEnabled = true;

    private int maxPicture = GlobalConstants.IMAGE_MAX;

    public PictureAddAdapter(Context ctx, boolean blankShow, boolean addEnabled) {
        this(ctx, blankShow, GlobalConstants.IMAGE_MAX, addEnabled);
    }

    public PictureAddAdapter(Context ctx, int maxSize, boolean addEnabled) {
        this(ctx, true, maxSize, addEnabled);
    }

    public PictureAddAdapter(Context ctx, boolean blankShow, int maxSize, boolean addEnabled) {
        this.ctx = ctx;
        this.maxPicture = maxSize;
        this.blankShow = blankShow;
        this.addEnabled = addEnabled;
    }

    public void setItems(List<String> items) {  //选择图片之后跳转到这里，集合里存放了选择图片的路径
        if (items == null) {
            this.paths.clear();
        } else {
            this.paths = items;
        }
        notifyDataSetChanged();
    }

    public List<String> getPaths() {
        return paths;
    }

    @Override
    public int getCount() {
        int addSize = 0;
        if (!addEnabled) {
            addSize = paths.size();  //获取添加的数量
        } else {
            if (paths.size() == 0) {
                if (!blankShow) {  //如果不显示加号的话
                    addSize = paths.size(); 
                } else {
                    addSize = paths.size() + 1;
                }
            } else if (paths.size() == maxPicture) {  //如果等于9
                addSize = paths.size() + 1;   //只显示减号的图片
            } else {
                addSize = paths.size() + 2;   //同时显示减号和加号
            }
        }
        return addSize;  //返回图片的数量，含带的有加减图片
    }

    @Override
    public Object getItem(int position) {
//        if (paths != null && position < paths.size()) {
//            return paths.get(position);
//        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (paths != null) {
            return position;
        } else {
            return 0;
        }
    }

    public boolean isShowDelete() {
        return showDelete;
    }

    public void setShowDelete(boolean showDelete) {
        this.showDelete = showDelete;
        notifyDataSetChanged();
    }

    /**
     * 是够可添加
     *
     * @return
     */
    public boolean canAdd() {
        if (paths.size() < maxPicture) {
            return true;
        } else {
            L.toastShort("最多添加" + maxPicture + "个内容");
            return false;
        }
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PictureViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.cell_img, null);
            holder = new PictureViewHolder();
            holder.ivIcon = (CommonImageView) convertView.findViewById(R.id.iv_icon);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.ivDelete = (CommonImageView) convertView.findViewById(R.id.iv_delete);
            holder.ivVideoPlay = (CommonImageView) convertView.findViewById(R.id.iv_video_play);
            convertView.setTag(holder);
        } else {
            holder = (PictureViewHolder) convertView.getTag();
        }
//        convertView.setBackgroundColor(ctx.getResources().getColor(R.color.red));
        setData(position, holder);
        return convertView;
    }

    public int getMaxPicture() {
        return maxPicture;
    }

    public abstract void setData(int position, PictureViewHolder holder);

    public class PictureViewHolder {
        public CommonImageView ivIcon;
        public TextView tvName;
        public CommonImageView ivDelete;
        public CommonImageView ivVideoPlay;
    }
}
