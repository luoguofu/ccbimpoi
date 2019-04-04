package com.example.ccbim.ccbimpoi.data;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ccbim.ccbimpoi.R;
import com.weqia.component.rcmode.RcBaseViewHolder;
import com.weqia.component.rcmode.adapter.RcFastAdapter;
import com.weqia.utils.L;
import com.weqia.utils.ViewUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lgf on 2019/4/3.
 */

public abstract class NavigHandler {
    private List<ProjectCheckData> navDatas = new ArrayList<>();
    private RcFastAdapter navAdapter;
    private RecyclerView rcNavView;
    private String root = "";
    private LinearLayout headerView;

    private Activity ctx;
    private boolean isBackReqNet;

    public NavigHandler(Activity ctx, LinearLayout headerView) {
        this.ctx = ctx;
        this.headerView = headerView;
        initView();
    }
    private void initView() {
        ViewUtils.showView(headerView);
        View ftView = LayoutInflater.from(ctx).inflate(R.layout.fragment_top_navigate_view, null);
        LinearLayout.LayoutParams dvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        headerView.addView(ftView, dvParams);

        rcNavView = (RecyclerView) ftView.findViewById(R.id.rc_title_path);
        LinearLayoutManager titleManager = new LinearLayoutManager(ctx);
        titleManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcNavView.setLayoutManager(titleManager);
        navAdapter = new RcFastAdapter<ProjectCheckData>(ctx, R.layout.cell_top_navigate_item_view) {

            @Override
            public void bindingData(RcBaseViewHolder holder, final ProjectCheckData item) {
/*                TextView tvTitle = holder.getView(R.id.tv_path_title);
                tvTitle.setText(item.getExcelFullName());
                tvTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClick(item);
                    }
                });*/
            }

            @Override
            public void onBindViewHolder(RcBaseViewHolder holder, final int position) {
                final ProjectCheckData item = getItem(position);
                TextView tvTitle = holder.getView(R.id.tv_path_title);
                tvTitle.setText(item.getExcelFullName());
                tvTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClick(item, position);
                    }
                });
            }
        };
        rcNavView.setAdapter(navAdapter);
        navAdapter.setAll(navDatas);
    }
    private void itemClick(ProjectCheckData clickData,int position) {
        if (clickData != null) {
//            if (clickData.getIndex() == navDatas.size() - 1) {
//                return;
//            }
            Iterator<ProjectCheckData> sListIterator = navDatas.iterator();
            int count = 0;
            while (sListIterator.hasNext()) {
                ProjectCheckData tmpPath = sListIterator.next();
                if (count > position) {
                    sListIterator.remove();
                }
                count++;
            }
            navAdapter.setAll(navDatas);
            refreshView();
            loadData(clickData.getId());
        }
    }

    public abstract void loadData(int currentId);

    private void refreshView() {
        rcNavView.scrollToPosition(navDatas.size() - 1);
    }


    public void addNavData(ProjectCheckData tmpData) {
        navDatas.add(tmpData);
        navAdapter.add(tmpData);
        navAdapter.notifyDataSetChanged();
        refreshView();
    }
}
