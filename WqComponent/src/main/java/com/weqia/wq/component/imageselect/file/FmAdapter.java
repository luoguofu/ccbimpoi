package com.weqia.wq.component.imageselect.file;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.weqia.utils.L;
import com.weqia.utils.TimeUtils;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.datastorage.file.FileUtil;
import com.weqia.wq.R;
import com.weqia.wq.component.utils.FileMiniUtil;
import com.weqia.wq.component.utils.bitmap.PictureUtil;
import com.weqia.wq.data.global.WeqiaApplication;

import java.io.File;

public class FmAdapter extends BaseAdapter {
    private File[] files;
    private FmActivity context;

    public FmAdapter(FmActivity context, File[] files) {
        this.context = context;
        this.files = files;
    }

    @Override
    public int getCount() {
        if (files == null) {
            return 0;
        }
        return files.length;
    }

    @Override
    public Object getItem(int position) {
        return files[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(context, R.layout.fm_cell, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.adapter_icon);
            holder.tv = (TextView) convertView.findViewById(R.id.adapter_txt);
            holder.size = (TextView) convertView.findViewById(R.id.adapter_size);
            holder.select = (CheckBox) convertView.findViewById(R.id.file_select);
            holder.ivOperate = (ImageView) convertView.findViewById(R.id.ivOperate);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        //设置convertView中控件的值
        setconvertViewRow(position, holder);
        return convertView;
    }


    private void setconvertViewRow(int position, final Holder holder) {
        final File f = files[position];
        holder.tv.setText(f.getName());
        if (f.isFile()) {//是文件
            //加载文件图标
            ViewUtils.showView(holder.select);
            ViewUtils.hideView(holder.ivOperate);
            final String filePath = f.getAbsolutePath();
            int rId = FileMiniUtil.fileRId(f.getName());
            if (rId == R.drawable.f_img) {
                WeqiaApplication
                        .getInstance()
                        .getBitmapUtil()
                        .load(holder.iv, FileUtil.getFormatFilePath(filePath), null);
                holder.iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PictureUtil.viewPicture(context, filePath, holder.iv);
                    }
                });
            } else {
                holder.iv.setImageResource(rId);
            }
            holder.size.setVisibility(View.VISIBLE);
            String sizeStr = FileUtil.formetFileSize(f.length()) + " | " + TimeUtils.getDateTimeShort(f.lastModified() + "");
            holder.size.setText(sizeStr); //FileUtil.formetFileSize(f.length())
            if (FmActivity.getPaths().toString().contains(f.getAbsolutePath())) {
                holder.select.setChecked(true);
                holder.select.setSelected(true);
            } else {
                holder.select.setChecked(false);
                holder.select.setChecked(false);
            }
            if (FmActivity.getPaths().size() >= context.getMaxSelect()) {
                if (holder.select.isSelected()) {
                    holder.select.setClickable(true);
                } else {
                    holder.select.setClickable(false);
                }
            } else {
                holder.select.setClickable(true);
            }
            holder.select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean c = holder.select.isSelected();
                    if (FmActivity.getPaths().size() >= context.getMaxSelect()) {
                        if (c) {
                            holder.select.setSelected(!holder.select.isSelected());
                            if (!holder.select.isSelected()) {  //当文件大于9个的时候可以移除
                                holder.select.setChecked(false);
                                FmActivity.getPaths().remove(f.getAbsolutePath());
                            }
                            context.sharedTitleView.getButtonStringRight().setText("完成" + FmActivity.getPaths().size() + "/" + context.getMaxSelect());
                        } else {
                            //不能添加，提示！
                            L.toastShort("最多上传" + context.getMaxSelect() + "个文件！");
                            holder.select.setSelected(false);
                            holder.select.setChecked(false);
                        }
                    } else {
                        holder.select.setSelected(!holder.select.isSelected());
                        if (holder.select.isSelected()) {
                            holder.select.setChecked(true);
                            FmActivity.getPaths().add(f.getAbsolutePath());
                        } else if (!holder.select.isSelected()) {
                            holder.select.setChecked(false);
                            FmActivity.getPaths().remove(f.getAbsolutePath());
                        }
                        context.sharedTitleView.getButtonStringRight().setText("完成" + FmActivity.getPaths().size() + "/" + context.getMaxSelect());
                    }
                }
            });
        } else {//目录
            //加载目录图标
            ViewUtils.hideView(holder.select);
            ViewUtils.showView(holder.ivOperate);
            holder.iv.setImageResource(R.drawable.f_folder);
//            holder.size.setVisibility(View.GONE);

            Integer fcount = context.getFileCount().get(f.getName());
            if (fcount == null) {
                File[] strs = f.listFiles(context.fileFilter);
                fcount = strs == null ? 0 : strs.length;
                context.getFileCount().put(f.getName(), fcount);
            }
            holder.size.setText(fcount + "项 | " + TimeUtils.getDateTimeShort(f.lastModified() + ""));
        }
    }

    class Holder {
        public ImageView iv;
        public TextView tv;
        public TextView size;
        public CheckBox select;
        public ImageView ivOperate;
    }

}
