package com.weqia.wq.component.utils.locate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.weqia.utils.StrUtil;
import com.weqia.wq.R;
import com.weqia.wq.data.PosData;

import java.util.List;

public class LocateListAdapter extends BaseAdapter {

    private List<PosData> items;
    private Context ctx;
    private int checkPos = -1;

    public LocateListAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public void setItems(List<PosData> items, int checkPos) {
        if (items == null) {
            return;
        }
        this.items = items;
        this.checkPos = checkPos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    public List<PosData> getItems() {
        return items;
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
        LocateViewHolder holder = null;
        if (convertView != null) {
            holder = (LocateViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.cell_lv_pos, null);
            holder = new LocateViewHolder();
            holder.tvTitle = (TextView) convertView.findViewById(R.id.item_title_tv);
            holder.tvContent = (TextView) convertView.findViewById(R.id.item_content_tv);
            holder.ivChecked = (ImageView) convertView.findViewById(R.id.ivChecked);
            convertView.setTag(holder);
        }

        PosData posData = items.get(position);
        if (posData != null) {
            if (StrUtil.notEmptyOrNull(posData.getName())) {
                holder.tvTitle.setText(posData.getName());
            } else {
                holder.tvTitle.setText("");
            }
            if (StrUtil.notEmptyOrNull(posData.getAddr())) {
                holder.tvContent.setText(posData.getAddr());
            } else {
                holder.tvContent.setText("");
            }

            if (position == checkPos) {
                holder.ivChecked.setVisibility(View.VISIBLE);
            } else {
                holder.ivChecked.setVisibility(View.GONE);
            }
//            if (posData.isSelect()) {
//                holder.ivChecked.setVisibility(View.VISIBLE);
//            } else {
//                holder.ivChecked.setVisibility(View.GONE);
//            }

        }

        return convertView;
    }


    public class LocateViewHolder {
        public TextView tvTitle;
        public ImageView ivChecked;
        public TextView tvContent;
    }
}
