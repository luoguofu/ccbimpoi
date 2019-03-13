package com.weqia.wq.component.utils.autoupdate;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
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

public class SharedUpdateDialog extends Dialog implements View.OnClickListener {
    private Context ctx;

    public SharedUpdateDialog(Context context) {
        this(context, 0);
    }

    public SharedUpdateDialog(Context context, int theme) {
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
        // p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.85); // 宽度设置为屏幕的0.8
        // p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.95
        getWindow().setAttributes((WindowManager.LayoutParams) p);
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
        private String positiveButtonText;
        private String negativeButtonText;
        private String neutralButtonText;
        private String versionNameText;

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

        public Builder setVersionNameText(String versionNameText) {
            this.versionNameText = versionNameText;
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

        /**
         * @param positiveButtonText
         * @param listener
         * @return
         * @Description
         */
        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * @param neutralButtonText
         * @param listener
         * @return
         * @Description
         */
        public Builder setNaturalButton(String neutralButtonText,
                                        OnClickListener listener) {
            this.neutralButtonText = neutralButtonText;
            this.neutralButtononClickListener = listener;
            return this;
        }

        /**
         * @param negativeButtonText
         * @param listener
         * @return
         * @Description
         */
        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public SharedUpdateDialog build() {

            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final SharedUpdateDialog dialog =
                    new SharedUpdateDialog(context, R.style.dialog_common);
            View layout = inflater.inflate(R.layout.dialog_app_update, null);
            dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));

            if (bg != null && bg != 0) {
                layout.setBackgroundResource(bg);
            }
            // set title
            setTitle(layout);
            // set the confirm button
            setButtons(dialog, layout);
            // set the content message
            setContentView(dialog, layout);

            return dialog;
        }

        /**
         * @param dialog
         * @param layout
         * @Description
         */
        protected void setContentView(final SharedUpdateDialog dialog, View layout) {
            if (StrUtil.notEmptyOrNull(versionNameText)){
                TextView tvVersionName = (TextView) layout.findViewById(R.id.tv_dialog_lastest_version_name);
                tvVersionName.setText(versionNameText);
            }
            if (StrUtil.notEmptyOrNull(fileSizeText)){
                TextView tvFileSize = (TextView) layout.findViewById(R.id.tv_dialog_lastest_version_size);
                tvFileSize.setText(fileSizeText);
            }
            if (StrUtil.notEmptyOrNull(message)) {
                TextView tvMessage = (TextView) layout.findViewById(R.id.message);
                if (messageLeft) {
                    tvMessage.setGravity(Gravity.LEFT);
                }
                ViewUtils.setTextView(tvMessage, message);
                StrUtil.stripUnderlines(tvMessage);
            } else if (contentView != null) {
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

        /**
         * @param dialog
         * @param layout
         * @Description
         */
        protected void setButtons(final SharedUpdateDialog dialog, View layout) {

            Button positiveButton = (Button) layout.findViewById(R.id.positiveButton);
            Button negativeButton = (Button) layout.findViewById(R.id.negativeButton);
            Button neutralButton = (Button) layout.findViewById(R.id.neutralButton);

            // ImageView ivDv1 = (ImageView) layout.findViewById(R.id.iv_dv1);
            // ImageView ivDv2 = (ImageView) layout.findViewById(R.id.iv_dv2);

            View llButtons = layout.findViewById(R.id.ll_op_buttons);

            if (negativeButtonText == null && neutralButtonText == null
                    && positiveButtonText == null) {
                ViewUtils.hideView(llButtons);
                return;
            } else {
                ViewUtils.showView(llButtons);
            }

            if (positiveButtonText != null) {
                positiveButton.setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            } else {
                positiveButton.setVisibility(View.GONE);
            }

            // set the cancel button
            if (negativeButtonText != null) {
                negativeButton.setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    negativeButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            negativeButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            } else {
                negativeButton.setVisibility(View.GONE);
            }
            // set the neutralButton
            if (neutralButtonText != null) {
                neutralButton.setVisibility(View.VISIBLE);
                neutralButton.setText(neutralButtonText);
                if (neutralButtononClickListener != null) {
                    neutralButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            neutralButtononClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEUTRAL);
                        }
                    });
                }
            } else {
                neutralButton.setVisibility(View.GONE);
            }
        }
    }
}
