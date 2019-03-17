package com.example.ccbim.ccbimpoi.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.ccbim.ccbimpoi.R;
import com.example.ccbim.ccbimpoi.data.ProjectCheckData;

import java.util.List;

/**
 * Created by Administrator on 2019/3/17/017.
 */

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {
    private List<ProjectCheckData> projectCheckDataList;
    private Context mContext;

    /**
     * 构造方法
     *
     * @param projectCheckDataList 数据源
     */
    public HomeListAdapter(Context context, List<ProjectCheckData> projectCheckDataList) {
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProjectCheckData projectCheckData = projectCheckDataList.get(position);
        holder.excelNameTv.setText(projectCheckData.getCheckPartName());

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
