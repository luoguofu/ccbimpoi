package com.weqia.wq.component.utils.autoupdate;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.weqia.utils.StrUtil;
import com.weqia.utils.ViewUtils;
import com.weqia.wq.R;
import com.weqia.wq.global.ComponentUtil;

/**
 * <br/>Author:hihiwjc
 * <br/>Email:hihiwjc@live.com
 * <br/>Date:2016/8/2 0002
 * <br/>Func:
 */

public class SharedUpdateProgressDialog extends Dialog implements View.OnClickListener {
    private Context ctx;
    private ProgressBar mProgressBar;
    private TextView mTvProgress;
    private TextView mTvFileSize;

    public SharedUpdateProgressDialog(Context context) {
        this(context, 0);
    }

    public SharedUpdateProgressDialog(Context context, int theme) {
        super(context, theme);
        this.ctx = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager m = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display d = m.getDefaultDisplay();

        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.x = 0; // 设置位置 默认为居中
        p.y = 0; // 设置位置 默认为居中
        // p.height = (int) (d.getHeight() * 0.9); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.85); // 宽度设置为屏幕的0.8
        // p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.95
        getWindow().setAttributes((WindowManager.LayoutParams) p);
    }

    /**
     * @param progress progress must between 0 and 100)
     */
    public void updateProgress(int progress,String progressText,String fileSizeRate){
        if (progress<0){
            mProgressBar.setVisibility(View.GONE);
        }
        mProgressBar.setVisibility(View.VISIBLE);
        if (progress<0||progress>100) {
            return;
        }
        if (mProgressBar==null){
            return;
        }
        mProgressBar.setProgress(progress);
        mTvProgress.setText(progressText);
        mTvFileSize.setText(fileSizeRate);
    }
    /**
     * progress>=0&&progress<=100
     */
    protected void setProgressBar(View layout,boolean showProgress) {
        mProgressBar = (ProgressBar) layout.findViewById(R.id.dialog_progress_bar);
        mTvProgress = (TextView) layout.findViewById(R.id.tv_progress);
        mTvFileSize = (TextView) layout.findViewById(R.id.tv_file_size);
        if (!showProgress){
            mProgressBar.setVisibility(View.GONE);
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder {

        protected Context context;
        private String title;
        private String message;

        private String fileSizeText;
        private View contentView;
        protected Integer bg;
        private boolean showBar = false;
        private boolean singLine = false;
        private boolean messageLeft = true;
        private Integer titleColor;

        private OnClickListener positiveButtonClickListener,
                negativeButtonClickListener, neutralButtononClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * @param message
         * @return
         * @Description
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }
        public Builder setFileSizeText(String fileSizeText) {
            this.fileSizeText = fileSizeText;
            return this;
        }


        public Builder setBg(Integer bg) {
            this.bg = bg;
            return this;
        }

        public Builder showBar(boolean show) {
            showBar = show;
            return this;
        }

        public Builder setMessageLeft(boolean left) {
            messageLeft = left;
            return this;
        }

        public Builder setTitleAttr(boolean single, Integer tColor) {
            singLine = single;
            titleColor = tColor;
            return this;
        }

        /**
         * @param title
         * @return
         * @Description
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * @param v
         * @return
         * @Description
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public SharedUpdateProgressDialog build() {

            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final SharedUpdateProgressDialog dialog =
                    new SharedUpdateProgressDialog(context, R.style.dialog_common);
            View layout = inflater.inflate(R.layout.dialog_app_update_progress, null);
            dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));

            if (bg != null && bg != 0) {
                layout.setBackgroundResource(bg);
            }
            // set title
            setTitle(layout);
            // set the content message
            setContentView(dialog, layout);
            dialog.setProgressBar(layout,true);
            return dialog;
        }


        /**
         * @param dialog
         * @param layout
         * @Description
         */
        protected void setContentView(final SharedUpdateProgressDialog dialog, View layout) {
             if (contentView != null) {
                LinearLayout linearLayout = (LinearLayout) layout.findViewById(R.id.content);
                linearLayout.removeAllViews();
                LayoutParams params =
                        new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                try {
                    contentView.measure(w, h);
                    int height = contentView.getMeasuredHeight();
                    if (height > ComponentUtil.dip2px(360)) {
                        params.height = ComponentUtil.dip2px(360);
                    }
                } catch (NullPointerException e) {
                }
                linearLayout.addView(contentView, params);
            }
            dialog.setContentView(layout);
        }

        /**
         * @param layout
         * @Description
         */
        protected void setTitle(View layout) {
            TextView titleView = (TextView) layout.findViewById(R.id.title);
            titleView.setSingleLine(singLine);
            if (!singLine) {
                titleView.setMaxLines(2);
            }
            if (titleColor != null) {
                titleView.setTextColor(titleColor);
            }
            if (StrUtil.isEmptyOrNull(title)) {
                ViewUtils.hideView(titleView);
            } else {
                ViewUtils.showView(titleView);
                ViewUtils.setTextView(titleView, title);
            }

            TextView barText = (TextView) layout.findViewById(R.id.tile_bar);
            if (showBar) {
                ViewUtils.showView(barText);
            } else {
                ViewUtils.hideView(barText);
            }
        }

    }
}
