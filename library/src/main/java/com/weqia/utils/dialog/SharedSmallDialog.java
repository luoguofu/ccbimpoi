package com.weqia.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weqia.BaseInit;
import com.weqia.utils.MResource;
import com.weqia.utils.StrUtil;
import com.weqia.utils.ViewUtils;

@SuppressWarnings("deprecation")
public class SharedSmallDialog extends Dialog {

    private Context ctx;

    public SharedSmallDialog(Context context) {
        this(context, 0);
    }

    public SharedSmallDialog(Context context, int theme) {
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
        // p.height = (int) (d.getHeight() * 0.8); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.85); // 宽度设置为屏幕的0.95
        // p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.95
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) p);

    }

    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder {

        protected Context context;
        private String message;
        private View contentView;
        protected Integer bg;

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

        public Builder setBg(Integer bg) {
            this.bg = bg;
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

        /**
         * @return
         * @Description
         */
        public SharedSmallDialog create() {

            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final SharedSmallDialog dialog =
                    new SharedSmallDialog(context, MResource.getIdByName(
                            BaseInit.ctx.getPackageName(), "style", "dialog_common"));
            View layout =
                    inflater.inflate(MResource.getIdByName(BaseInit.ctx.getPackageName(), "layout",
                            "util_custom_small_dialog"), null);
            ViewGroup.LayoutParams params =
                    new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            dialog.addContentView(layout, params);

            if (bg != null && bg != 0) {
                layout.setBackgroundResource(bg);
            }
            // set the content message
            setContentView(dialog, layout);
            return dialog;
        }

        /**
         * @param dialog
         * @param layout
         * @Description
         */
        protected void setContentView(final SharedSmallDialog dialog, View layout) {
            if (StrUtil.notEmptyOrNull(message)) {
                ViewUtils.setTextView(
                        (TextView) layout.findViewById(MResource.getIdByName(
                                BaseInit.ctx.getPackageName(), "id", "message")), message);
            } else if (contentView != null) {
                LinearLayout linearLayout =
                        (LinearLayout) layout.findViewById(MResource.getIdByName(
                                BaseInit.ctx.getPackageName(), "id", "content"));
                linearLayout.removeAllViews();
                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                                LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER_VERTICAL;
                linearLayout.addView(contentView, params);
            }
            dialog.setContentView(layout);
        }

    }
}
