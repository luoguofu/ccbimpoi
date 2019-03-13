package com.weqia.utils.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weqia.BaseInit;
import com.weqia.utils.MResource;

public class LoadingTopView {

    private ProgressBar progressBar; // 进度条
    private TextView tvWait;// 底部等待
    private RelativeLayout moreView; // 加载中

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public TextView getTvWait() {
        return tvWait;
    }

    public void setTvWait(TextView tvWait) {
        this.tvWait = tvWait;
    }

    public RelativeLayout getMoreView() {
        return moreView;
    }

    public void setMoreView(RelativeLayout moreView) {
        this.moreView = moreView;
    }

    public LoadingTopView(Context ctx) {

        moreView =
                (RelativeLayout) LayoutInflater.from(ctx).inflate(
                        MResource.getIdByName(BaseInit.ctx.getPackageName(), "layout",
                                "util_view_list_topview"), null);

        if (moreView != null) {
            progressBar =
                    (ProgressBar) moreView.findViewById(MResource.getIdByName(
                            BaseInit.ctx.getPackageName(), "id", "pg_wait"));
            tvWait =
                    (TextView) moreView.findViewById(MResource.getIdByName(
                            BaseInit.ctx.getPackageName(), "id", "tv_wait"));
        }
    }
}
