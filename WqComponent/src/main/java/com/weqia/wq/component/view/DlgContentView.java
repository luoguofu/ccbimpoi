package com.weqia.wq.component.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weqia.utils.StrUtil;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.dialog.quickaction.ActionItem;
import com.weqia.wq.R;

import java.util.List;

public abstract class DlgContentView extends LinearLayout {

    private Context ctx;
    private List<ActionItem> ops;
    private List<ActionItem> changes;
    private LinearLayout llOp;
    private LinearLayout llChange;

    public DlgContentView(Context context) {
        super(context);
        this.ctx = context;
        initView();
    }

    public void initWorkOpInfo(List<ActionItem> ops, List<ActionItem> changes) {
        this.ops = ops;
        this.changes = changes;
        initData();
        this.setBackgroundColor(Color.RED);
    }

    public void initData() {
        if (StrUtil.listNotNull(ops)) {
            for (int i = 0; i < ops.size(); i++) {
                View view = LayoutInflater.from(ctx).inflate(R.layout.view_reused_dialigtext, null);
                TextView textView = (TextView) view.findViewById(R.id.tv_dlg_title); // new
                textView.setTag(ops.get(i).getActionId());
                textView.setText(ops.get(i).getTitle());
                final int _id = i;
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doClick(ops.get(_id).getActionId());
                    }
                });
                if (changes == null || changes.size() == 0) {
                    if (i == ops.size() -1) {
                        View dvLine = view.findViewById(R.id.iv_dlg_dv);
                        ViewUtils.hideView(dvLine);
                    }
                }
                llOp.addView(view);
            }
        }
        if (StrUtil.listNotNull(changes)) {
            for (int i = 0; i < changes.size(); i++) {
                View view = LayoutInflater.from(ctx).inflate(R.layout.view_reused_dialigtext, null);
                TextView textView = (TextView) view.findViewById(R.id.tv_dlg_title); // new
                textView.setTag(changes.get(i).getActionId());
                textView.setText(changes.get(i).getTitle());
                final int _id = i;
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doClick(changes.get(_id).getActionId());
                    }
                });
                if (i == changes.size() -1) {
                    View dvLine = view.findViewById(R.id.iv_dlg_dv);
                    ViewUtils.hideView(dvLine);
                }
                llChange.addView(view);
            }
        }
    }

    public DlgContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        initView();
    }

    public void initView() {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.view_work_op, null);
        if (view != null) {
            llChange = (LinearLayout) view.findViewById(R.id.ll_work_change);
            llOp = (LinearLayout) view.findViewById(R.id.ll_work_op);
        }
        this.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }


    public abstract void doClick(int actionId);

}
