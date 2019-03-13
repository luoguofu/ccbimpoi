package com.weqia.utils.dialog;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.weqia.BaseInit;
import com.weqia.utils.MResource;
import com.weqia.utils.StrUtil;
import com.weqia.utils.data.TopDialogData;

public class SharedTopDialog extends Dialog {

    private Context ctx;
    private View mRoot;
    private View view;

    public SharedTopDialog(Context context) {
        this(context, 0);
    }

    public SharedTopDialog(Context context, int theme) {
        super(context, MResource.getIdByName(BaseInit.ctx.getPackageName(), "style",
                "dialog_fullscreen_top"));
        ctx = context;
        view =
                LayoutInflater.from(ctx).inflate(
                        MResource.getIdByName(BaseInit.ctx.getPackageName(), "layout",
                                "util_custom_top_dialog"), null);
    }

    public void setDialogItems(List<TopDialogData> items) {
        if (items == null || items.size() == 0 || view == null) {
            return;
        }
        LinearLayout layout =
                (LinearLayout) view.findViewById(MResource.getIdByName(
                        BaseInit.ctx.getPackageName(), "id", "llItems"));
        if (layout != null) {
            layout.removeAllViews();
        }

        for (TopDialogData item : items) {

            LinearLayout itemLayout = new LinearLayout(ctx);
            Button btn = new Button(ctx);
            TextView tvTitle = new TextView(ctx);
            if (item.getIcon() != null) {
                btn.setBackgroundResource(item.getIcon());
            }
            String title = item.getTitle();
            if (StrUtil.notEmptyOrNull(title)) {
                tvTitle.setText(title);
            }
            if (item.getTitleColor() != null) {
                tvTitle.setTextColor(item.getTitleColor());
            } else {
                tvTitle.setTextColor(ctx.getResources().getColor(
                        MResource.getIdByName(BaseInit.ctx.getPackageName(), "color", "black")));
            }
            // 设置id
            Integer id = item.getId();
            if (id != null) {
                btn.setId(id);
            }
            // 设置监听
            View.OnClickListener onClickListener = item.getOnClickListener();
            if (onClickListener != null) {
                btn.setOnClickListener(onClickListener);
            }
            tvTitle.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            tvTitle.setGravity(Gravity.CENTER);
            tvTitle.setPadding(0, 10, 0, 0);
            itemLayout.addView(btn);
            itemLayout.addView(tvTitle);
            itemLayout.setPadding(40, 35, 40, 0);
            itemLayout.setOrientation(LinearLayout.VERTICAL);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.addView(itemLayout);
        }
        layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        layout.setGravity(Gravity.CENTER);
    }

    private View getView() {

        view.findViewById(MResource.getIdByName(BaseInit.ctx.getPackageName(), "id", "viewEmpty"))
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
