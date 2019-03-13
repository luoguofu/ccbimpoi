package com.weqia.utils.dialog;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weqia.BaseInit;
import com.weqia.utils.MResource;
import com.weqia.utils.StrUtil;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.data.DialogData;

public class SharedFullScreenDialog extends Dialog {

    private Context ctx;
    private View mRoot;
    private View view;

    public SharedFullScreenDialog(Context context) {
        this(context, 0);
    }

    public SharedFullScreenDialog(Context context, int theme) {
        super(context, MResource.getIdByName(BaseInit.ctx.getPackageName(), "style",
                "dialog_fullscreen"));
        ctx = context;
        view =
                LayoutInflater.from(ctx).inflate(
                        MResource.getIdByName(BaseInit.ctx.getPackageName(), "layout",
                                "util_custom_full_screen_dialog"), null);
    }

    /**
     * 设置dialog tilte
     * 
     * @param tilte
     * @Description
     */
    public void setDialogTitle(String tilte) {
        if (StrUtil.notEmptyOrNull(tilte)) {
            if (view != null) {
                TextView tv =
                        (TextView) view.findViewById(MResource.getIdByName(
                                BaseInit.ctx.getPackageName(), "id", "tv_full_dialog_title"));
                if (tv != null) {
                    ViewUtils.showView(tv);
                    ViewUtils.setTextView(tv, tilte);
                } else {
                    ViewUtils.hideView(tv);
                }
            }
        }
    }

    /**
     * 设置dialog的button
     * 
     * @Description
     */
    public void setDialogButton(List<DialogData> buttons) {
        if (buttons == null || buttons.size() == 0 || view == null) {
            return;
        }

        LinearLayout layout =
                (LinearLayout) view.findViewById(MResource.getIdByName(
                        BaseInit.ctx.getPackageName(), "id", "linear_full_dialog_buttons"));
        if (layout != null) {
            layout.removeAllViews();
        }
        for (DialogData buttonData : buttons) {
            Button button = new Button(ctx);

            button.setTextAppearance(ctx, MResource.getIdByName(BaseInit.ctx.getPackageName(),
                    "style", "dialog_full_normal"));
            button.setBackgroundResource(MResource.getIdByName(BaseInit.ctx.getPackageName(),
                    "drawable", "util_com_alert_normal"));
            button.setTextColor(Color.WHITE);
            // 设置button text
            String title = buttonData.getTitle();
            if (StrUtil.notEmptyOrNull(title)) {
                button.setText(title);
            }

            if (buttonData.getTitleColor() != null) {
                button.setTextColor(buttonData.getTitleColor());
            }


            // 设置id
            Integer id = buttonData.getId();
            if (id != null) {
                button.setId(id);
            }

            // 设置背景
            Integer type = buttonData.getType();
            if (type != null) {
                button.setBackgroundResource(type);
            } else {}

            // 设置监听
            View.OnClickListener onClickListener = buttonData.getOnClickListener();
            if (onClickListener != null) {
                button.setOnClickListener(onClickListener);
            }
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 10, 0, 10);
            button.setLayoutParams(params);
            layout.addView(button);
        }
    }

    private View getView() {

        view.findViewById(
                MResource.getIdByName(BaseInit.ctx.getPackageName(), "id",
                        "button_full_dialog_cancel")).setOnClickListener(
                new View.OnClickListener() {

                    public void onClick(View v) {
                        cancel();
                    }
                });

        view.findViewById(MResource.getIdByName(BaseInit.ctx.getPackageName(), "id", "view_empty"))
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });

        return view;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mRoot == null) {
            mRoot = getView();
            setContentView(mRoot);
        }
    }
}
