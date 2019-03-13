package com.weqia.wq.component.view.face;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.weqia.wq.R;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.data.global.WeqiaApplication;

// 表情适配器
public class FaceAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private int currentPage = 0;
    private Map<String, Integer> mFaceMap;
    private List<Integer> faceList = new ArrayList<Integer>();

    public FaceAdapter(Context context, int currentPage) {
        this.inflater = LayoutInflater.from(context);
        this.currentPage = currentPage;
        mFaceMap = WeqiaApplication.getInstance().getFaceMap();
        initData();
    }

    private void initData() {
        for (Map.Entry<String, Integer> entry : mFaceMap.entrySet()) {
            faceList.add(entry.getValue());
        }
    }

    @Override
    public int getCount() {
        return GlobalConstants.FACE_NUM + 1;
    }

    @Override
    public Object getItem(int position) {
        return faceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.cell_face_item, null, false);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == GlobalConstants.FACE_NUM) {
            ((ImageView) convertView).setImageResource(R.drawable.emotion_del_selector);

        } else {
            int count = GlobalConstants.FACE_NUM * currentPage + position;
            if (count < 121) {
                ((ImageView) convertView).setImageResource(faceList.get(count));
            } else {
                ((ImageView) convertView).setImageDrawable(null);
            }
        }
        return convertView;
    }

    public static class ViewHolder {
        // CommonImageView faceIV;
    }
}
