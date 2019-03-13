package com.weqia.wq.component.view;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weqia.utils.ViewUtils;
import com.weqia.utils.view.pullrefresh.PullToRefreshListView;
import com.weqia.wq.R;

/**
 * 全文,收起VIEW
 */
public class MoreTextView extends LinearLayout {

    private TextView tvMore;
    private Context ctx;
    //    private boolean isClosed = true;
    private TextView tvShow;
    private PullToRefreshListView plListView;
    private Integer lines;

    public MoreTextView(Context ctx) {
        this(ctx, null);
    }

    public MoreTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        if (isInEditMode()) {
            return;
        }
        initView();
    }

    public void initTv(final TextView tvShow, final PullToRefreshListView plListView) {
        this.tvShow = tvShow;
        this.plListView = plListView;
        new GetLinesAsyncTask().execute();
    }

    public void initTv(final TextView tvShow, final PullToRefreshListView plListView,Integer lines) {
        this.tvShow = tvShow;
        this.plListView = plListView;
        this.lines = lines;
        new GetLinesAsyncTask().execute();
    }

    public void loadComplete() {
        new Handler().post(new Runnable() {

            @Override
            public void run() {
                plListView.onRefreshComplete();
                plListView.onLoadMoreComplete();
            }
        });
    }

    public void initView() {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        LinearLayout layout = new LinearLayout(ctx);
        View view = inflater.inflate(R.layout.view_more_tv, layout);
        tvMore = (TextView) view.findViewById(R.id.tvMore);
        OnClickListener onClickListener = new OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                if (plListView != null) {
                    loadComplete();
                }
                if (tvMore.getText().toString().equalsIgnoreCase("收起")) {
                    tvShow.setMaxLines(lines == null ?5:lines);
                    tvMore.setText(R.string.tv_more);
//                    isClosed = false;
                } else {
                    tvShow.setMaxLines(ctx.getWallpaperDesiredMinimumHeight());
                    tvMore.setText(R.string.tv_close);
//                    isClosed = true;
                }
            }
        };
        ViewUtils.bindClickListenerOnViews(onClickListener, tvMore);
        this.addView(layout);
    }

    private class GetLinesAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(final Void result) {
            super.onPostExecute(result);
            int lineCount = tvShow.getLineCount();
            tvMore.setVisibility(lineCount <= 5 ? View.GONE : View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }
}
