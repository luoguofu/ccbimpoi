package com.example.ccbim.ccbimpoi.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ccbim.ccbimpoi.R;
import com.example.ccbim.ccbimpoi.activity.FormListActivity;
import com.example.ccbim.ccbimpoi.activity.HomeActivity;
import com.example.ccbim.ccbimpoi.data.ExcelEnum;
import com.example.ccbim.ccbimpoi.data.ProjectCheckData;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.datastorage.db.DbUtil;
import com.weqia.wq.component.db.WeqiaDbUtil;
import com.weqia.wq.component.utils.DialogUtil;
import com.weqia.wq.data.global.WeqiaApplication;

import java.io.File;
import java.util.List;

import static com.example.ccbim.ccbimpoi.MainNewActivity.getPoiExcelDir;
import static com.example.ccbim.ccbimpoi.util.ConstantUtil.PROJECTEXTRA;

/**
 * Created by Administrator on 2019/3/17/017.
 */

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {
    private List<ProjectCheckData> projectCheckDataList;
    private HomeActivity mContext;
    private Dialog dialog;

    /**
     * 构造方法
     *
     * @param projectCheckDataList 数据源
     */
    public HomeListAdapter(HomeActivity context, List<ProjectCheckData> projectCheckDataList) {
        this.mContext = context;
        this.projectCheckDataList = projectCheckDataList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.home_item_layout,parent, false);
        ViewHolder holder= new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final ProjectCheckData projectCheckData = projectCheckDataList.get(position);
        if (projectCheckData.getFileType() == ExcelEnum.ProjectFileType.FILE.value()) {
            ViewUtils.showViews(holder.selectBox, holder.editBt);
            ViewUtils.hideView(holder.DirIv);
        } else {
            ViewUtils.hideViews(holder.selectBox, holder.editBt);
            ViewUtils.showView(holder.DirIv);
            holder.excelNameTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.getNavHandler().addNavData(projectCheckData);
                    mContext.setLevel(projectCheckData.getLevel());
//                    DbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
//                    List<ProjectCheckData> list = dbUtil.findAllByWhereN(ProjectCheckData.class, "parentId = " + projectCheckData.getId(), "id");
                    mContext.setParentId(projectCheckData.getId());
                    mContext.initData();
                }
            });
        }
        holder.excelNameTv.setText(projectCheckData.getExcelFullName());
        holder.selectBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mContext.getSelectProjectData().add(projectCheckData);
                } else {
                    mContext.getSelectProjectData().remove(projectCheckData);
                }
            }
        });

        holder.editBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FormListActivity.class);
                intent.putExtra(PROJECTEXTRA, projectCheckDataList.get(position));
                mContext.startActivity(intent);
            }
        });
        holder.excelNameTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dialog= DialogUtil.initLongClickDialog(mContext, null, new String[]{"删除", "导出"}, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        switch ((Integer) view.getTag()) {
                            case 0:
                                Dialog confirmDialof = DialogUtil.initCommonDialog(mContext, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        switch (i) {
                                            case -2:
                                                 break;
                                            case -1:
                                                DbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
                                                dbUtil.deleteById(ProjectCheckData.class, projectCheckData.getId());
                                                break;

                                        }
                                        dialog.dismiss();
                                    }
                                }, "确定要删除吗？");
                                confirmDialof.show();
                                break;
                            case 1:
                                mContext.exportExcel(projectCheckData);
                                break;
                        }
                    }
                });
                dialog.show();
                return false;
            }
        });
    }
    //android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    @Override
    public int getItemCount() {
        return projectCheckDataList.size();
    }

    /**
     * RecyclerView的持有者类
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox selectBox;
        TextView excelNameTv;
        Button editBt;
        ImageView DirIv;

        public ViewHolder(View view) {
            super(view);
            selectBox = view.findViewById(R.id.box_select_excel);
            excelNameTv = view.findViewById(R.id.tv_excel_name);
            editBt = view.findViewById(R.id.bt_edit);
            DirIv=view.findViewById(R.id.iv_dir);

        }
    }


}
