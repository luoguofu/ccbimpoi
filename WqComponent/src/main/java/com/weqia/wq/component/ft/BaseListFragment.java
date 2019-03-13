package com.weqia.wq.component.ft;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.weqia.utils.view.pullrefresh.PullToRefreshBase;
import com.weqia.utils.view.pullrefresh.PullToRefreshListView;
import com.weqia.wq.R;
import com.weqia.wq.component.utils.GlobalUtil;


public abstract class BaseListFragment extends BaseFt {
    protected ListView listView;
    public PullToRefreshListView plListView = null;
    protected LinearLayout headerView;
    public ImageView ivAddButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != view) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.fragment_base, null);  //添加list布局
            initBaseView(view);// 控件初始化
        }
        return view;

    }

    private void initBaseView(View view) {
        headerView = (LinearLayout) view.findViewById(R.id.headerView);
        ivAddButton = (ImageView) view.findViewById(R.id.ivAddButton);
        plListView = (PullToRefreshListView) view.findViewById(R.id.plListView);
        listView = plListView.getRefreshableView();
        initListView();
        initCustomView();
    }

    /**
     * 自定义自己需要的界面
     */
    public void initCustomView() {

    }

    public void initListView() {  //刷新数据
        // 下拉刷新
        plListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                onPullMore();  //抽象方法
            }
        });
        // 加载更多
        plListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
                onLoadMore(); //抽象方法
            }

            @Override
            public void onLastItemFast() {
            }
        });
    }

    public void loadComplete() {
        if (emptyOthView() != null) {
            GlobalUtil.loadComplete(plListView, getActivity(), emptyOthView(), null);
        } else {
            GlobalUtil.loadComplete(plListView, getActivity(), canAdd());
        }
    }

    public abstract void onPullMore();

    public abstract void onLoadMore();

//    private abstract View emptyView();

    public abstract Boolean canAdd();

    public View emptyOthView() {
        return null;
    }
}
