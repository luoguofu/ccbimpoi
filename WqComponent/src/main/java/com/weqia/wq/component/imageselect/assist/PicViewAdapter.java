package com.weqia.wq.component.imageselect.assist;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.weqia.utils.StrUtil;
import com.weqia.utils.imageselect.ImageEntity;
import com.weqia.utils.imageselect.ImageGroup;
import com.weqia.wq.R;
import com.weqia.wq.data.global.WeqiaApplication;

public class PicViewAdapter extends BaseAdapter {

    private Context ctx;
    private ArrayList<ImageGroup> items;

    public PicViewAdapter(Context context) {
        this.ctx = context;
    }

    public void setItems(ArrayList<ImageGroup> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public List<ImageGroup> getItems() {
        return items;
    }

    @Override
    public int getCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (items != null) {
            return items.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        PicViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(ctx);
            convertView = mInflater.inflate(R.layout.cell_dialog_pic, null);
            holder = new PicViewHolder();
            holder.ivCatelog =
                    (com.weqia.utils.view.CommonImageView) convertView
                            .findViewById(R.id.iv_catelog_image);
            holder.tvCatelogName = (TextView) convertView.findViewById(R.id.tv_catelog_title);
            holder.btSelected = (ImageButton) convertView.findViewById(R.id.ib_catelog_select);
            convertView.setTag(holder);
        } else {
            holder = (PicViewHolder) convertView.getTag();
        }
        setDatas(position, holder);
        return convertView;
    }


    public void setDatas(int position, PicViewHolder holder) {
        ImageGroup imageGroup = (ImageGroup) getItem(position);
        if (imageGroup == null) {
            return;
        }
        ImageEntity imageEntity = imageGroup.getFirstImgPath();
        if (imageEntity != null) {
            if (imageEntity.getLocalUri() != null) {
                WeqiaApplication.getInstance().getBitmapUtil()
                        .load(holder.ivCatelog, imageEntity.getLocalUri().toString(), null);
            } else if (StrUtil.notEmptyOrNull(imageEntity.getPath())) {
                WeqiaApplication.getInstance().getBitmapUtil()
                        .load(holder.ivCatelog, Scheme.FILE.wrap(imageEntity.getPath()), null);
            }
        }
        holder.tvCatelogName.setText(imageGroup.getDirName());
        holder.btSelected.setSelected(imageGroup.isSelected());

    }

    public class PicViewHolder {
        public com.weqia.utils.view.CommonImageView ivCatelog;
        public TextView tvCatelogName;
        public ImageButton btSelected;
    }
}
