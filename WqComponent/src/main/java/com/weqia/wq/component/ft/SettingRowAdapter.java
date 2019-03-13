package com.weqia.wq.component.ft;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.weqia.utils.view.CommonImageView;
import com.weqia.wq.R;
import com.weqia.wq.data.BaseData;

import java.util.List;

public abstract class SettingRowAdapter extends BaseAdapter {

    private List<? extends BaseData> items;
    private Activity ctx;

    public SettingRowAdapter(Activity ctx) {
        this.ctx = ctx;
    }

    public void setItems(List<? extends BaseData> items) {
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
    public Object getItem(int position) {
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
        SRViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.cell_setting_row, null);
            holder = new SRViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvTitleRight = (TextView) convertView.findViewById(R.id.tvTitleRight);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            holder.tvSummary = (TextView) convertView.findViewById(R.id.tvSummary);
            holder.tvRight = (TextView) convertView.findViewById(R.id.tvRight);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
            holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.mCheckBox);
            holder.ivAvatar = (CommonImageView) convertView.findViewById(R.id.ivAvatar);
//            holder.llCell = (LinearLayout) convertView.findViewById(R.id.llCell);
            holder.vRight = convertView.findViewById(R.id.rlRight);
            holder.ivArrow = (CommonImageView) convertView.findViewById(R.id.iv_arrow);
            holder.tvContent.setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (SRViewHolder) convertView.getTag();
        }
        setData(position, holder);

        return convertView;
    }

    public abstract void setData(int position, SRViewHolder holder);

    public class SRViewHolder {
        public TextView tvTitle;
        public TextView tvTitleRight;
        public TextView tvContent;
        public TextView tvSummary;
        public TextView tvRight;
        public ImageView ivIcon;
        public CheckBox mCheckBox;
        public CommonImageView ivAvatar;
        public View vRight;
        public CommonImageView ivArrow;
//        public LinearLayout llCell;
    }
}