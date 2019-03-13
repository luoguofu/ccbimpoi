package com.weqia.wq.component.utils.locate;

import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.search.core.PoiInfo;
import com.weqia.wq.component.activity.SharedTitleActivity;
import com.weqia.wq.component.activity.assist.SharedSearchAdapter;


public class LocateSerchAdapter extends SharedSearchAdapter<PoiInfo> {

    private SharedTitleActivity ctx;

    public LocateSerchAdapter(SharedTitleActivity ctx) {
        super(ctx);
        this.ctx = ctx;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        SharedWorkViewHolder holder = null;
//        if (convertView != null) {
//            holder = (SharedWorkViewHolder) convertView.getTag();
//        } else {
//            convertView = LayoutInflater.from(ctx).inflate(R.layout.cell_lv_pos, null);
//            holder = new SharedWorkViewHolder();
//            holder.tvTitle = (TextView) convertView.findViewById(R.id.item_title_tv);
//            holder.tvContent = (TextView) convertView.findViewById(R.id.item_content_tv);
//            holder.pushView = (PushCountView) convertView.findViewById(R.id.v_push_count);
//            convertView.setTag(holder);
//        }
//
//        PoiInfo posData = (PoiInfo) getItem(position);
//        if (posData != null) {
//            holder.tvTitle.setText(posData.name);
//            holder.tvContent.setText(posData.address);
//        }

        return convertView;
    }
}
