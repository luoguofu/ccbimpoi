package com.weqia.wq.component.activity.assist;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.weqia.utils.StrUtil;
import com.weqia.wq.R;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;
import com.weqia.wq.data.SelData;
import com.weqia.wq.global.ComponentUtil;
import com.weqia.wq.component.utils.FileMiniUtil;
import com.weqia.wq.data.AttachmentData;
import com.weqia.wq.data.EnumData;
import com.weqia.wq.data.global.WeqiaApplication;

import java.util.List;

//附件适配
public class AttachAdapter extends BaseAdapter {

    private List<? extends AttachmentData> items;
    private SharedDetailTitleActivity ctx;

    public AttachAdapter(SharedDetailTitleActivity ctx) {
        this.ctx = ctx;
    }

    public AttachAdapter(SharedDetailTitleActivity ctx, List<? extends AttachmentData> items) {
        this.ctx = ctx;
        this.items = items;
        setItems(items);
    }

    public void setItems(List<? extends AttachmentData> items) {
        // if (items != null) {
        //
        // }
        this.items = items;
        notifyDataSetChanged();
    }

    public List<? extends AttachmentData> getItems() {
        return items;
    }

    @Override
    public int getCount() {
        if (items != null) {
            return items.size();
        } else {
            return 0;
        }

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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.cell_lv_attach, null);
            holder = new ViewHolder();
            holder.ivIcon = (com.weqia.utils.view.CommonImageView) convertView.findViewById(R.id.iv_attachment_icon);
            holder.ivOperate = (com.weqia.utils.view.CommonImageView) convertView.findViewById(R.id.iv_operate);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_attachment_name);
//			holder.tvCreate = (TextView) convertView.findViewById(R.id.tv_create_time);
            holder.tvSize = (TextView) convertView.findViewById(R.id.tv_attachment_size);
            holder.tvFrom = (TextView) convertView.findViewById(R.id.tvFrom);
            holder.tvStatus = (TextView) convertView.findViewById(R.id.tv_download_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setData(position, holder);
        return convertView;
    }

    @SuppressLint("DefaultLocale")
    private void setData(int position, ViewHolder holder) {
        AttachmentData data = items.get(position);

        String name = "";
        String fileSize = "";

        name = data.getName();
        fileSize = data.getFileSize();
        String frontStr = "";
        if (StrUtil.notEmptyOrNull(data.getCreateDate())) {
            try {
                frontStr = ComponentUtil.getTimeM_D(data.getCreateDate());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        String author = data.getMname();
        if ( StrUtil.notEmptyOrNull(author)) {
            author = "来自" + author;
        }
        holder.ivIcon.setImageResource(FileMiniUtil.fileRId(name));
        holder.tvName.setText(name);
        String sizeStr = StrUtil.formatFileSize(fileSize);
        if (StrUtil.isEmptyOrNull(frontStr)) {
            holder.tvSize.setText(sizeStr);
        } else {
            holder.tvSize.setText(sizeStr + "    " + frontStr);
        }

        if (StrUtil.notEmptyOrNull(author)) {
            holder.tvFrom.setText(author);
        }


//        if (StrUtil.notEmptyOrNull(data.getType())) {
        if (data.getType() == EnumData.AttachType.PICTURE.value() || data.getType() == EnumData.AttachType.VIDEO.value() || data.getType() == EnumData.AttachType.FILE.value()) {
//            holder.tvStatus.setVisibility(View.GONE);
            if (data.getType() == EnumData.AttachType.PICTURE.value() && StrUtil.notEmptyOrNull(data.getUrl())) {
                WeqiaApplication
                        .getInstance()
                        .getBitmapUtil()
                        .load(holder.ivIcon, data.getUrl(), EnumData.ImageThumbTypeEnums.THUMB_VERY_SMALL.value());
            }
//            holder.ivOperate.setImageResource(R.drawable.arrow_right);
        } else {
//            holder.tvStatus.setVisibility(View.VISIBLE);
//            holder.ivOperate.setImageResource(R.drawable.arrow_bottom);
        }
        holder.ivOperate.setImageResource(R.drawable.arrow_right);
//        } else {
//            holder.ivOperate.setImageResource(R.drawable.arrow_right);
//        }

    }

    public class ViewHolder {
        public com.weqia.utils.view.CommonImageView ivIcon;
        public com.weqia.utils.view.CommonImageView ivOperate;
        public TextView tvName;
        //		public TextView tvCreate;
        public TextView tvSize;
        public TextView tvFrom;
        public TextView tvStatus;
    }
}
