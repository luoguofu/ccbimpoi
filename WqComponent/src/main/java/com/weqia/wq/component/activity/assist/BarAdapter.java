package com.weqia.wq.component.activity.assist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.weqia.utils.view.CommonImageView;
import com.weqia.wq.R;

import java.util.ArrayList;

// 选择图片表情适配器
public class BarAdapter extends BaseAdapter {

    Context context;
    ArrayList<TalkGridData> talkGrids;

    public BarAdapter(Context context, ArrayList<TalkGridData> talkGrids) {
        this.context = context;
        this.talkGrids = talkGrids;
    }

    @Override
    public int getCount() {
        return talkGrids.size();
    }

    @Override
    public Object getItem(int position) {
        return talkGrids.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        BarViewHolder holder = new BarViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.view_talk_grid_item, null);
            holder.gv_vh_iv01 = (CommonImageView) convertView.findViewById(R.id.app_grid_item_icon);
            holder.gv_vh_tv01 = (TextView) convertView.findViewById(R.id.app_grid_item_name);
            convertView.setTag(holder);
        } else {
            holder = (BarViewHolder) convertView.getTag();
        }
        holder.gv_vh_iv01.setImageResource(talkGrids.get(position).getIcon());
        holder.gv_vh_iv01.setBackgroundResource(R.drawable.stoke_bg);
        holder.gv_vh_tv01.setText(talkGrids.get(position).getTitle());// 文字
        return convertView;
    }
    
    class BarViewHolder {
        CommonImageView gv_vh_iv01;
        TextView gv_vh_tv01;
    }
}