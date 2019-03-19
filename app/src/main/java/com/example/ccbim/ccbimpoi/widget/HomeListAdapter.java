package com.example.ccbim.ccbimpoi.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.ccbim.ccbimpoi.R;
import com.example.ccbim.ccbimpoi.activity.FormListActivity;
import com.example.ccbim.ccbimpoi.activity.HomeActivity;
import com.example.ccbim.ccbimpoi.data.ProjectCheckData;

import java.util.List;

import static com.example.ccbim.ccbimpoi.util.ConstantUtil.PROJECTEXTRA;

/**
 * Created by Administrator on 2019/3/17/017.
 */

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {
    private List<ProjectCheckData> projectCheckDataList;
    private HomeActivity mContext;

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
        holder.excelNameTv.setText(projectCheckData.getCheckPartName() + projectCheckData.getExcelName());
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

        public ViewHolder(View view) {
            super(view);
            selectBox = view.findViewById(R.id.box_select_excel);
            excelNameTv = view.findViewById(R.id.tv_excel_name);
            editBt = view.findViewById(R.id.bt_edit);

        }
    }


}
