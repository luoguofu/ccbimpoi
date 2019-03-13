package com.weqia.wq.component.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weqia.utils.StrUtil;
import com.weqia.wq.R;

/**
 * 头像+消息数目
 *
 * @author Dminter
 */
 public class PushCountView extends RelativeLayout {
    private com.weqia.utils.view.CommonImageView ivIcon;
    private TextView tvCount;
    private Context ctx;
    private String mid;
    private RelativeLayout mLayout;

    public RelativeLayout getmLayout() {
        return mLayout;
    }

    public void setmLayout(RelativeLayout layout) {
        mLayout = layout;
    }

    public PushCountView(Context ctx) {
        this(ctx, null);
    }

    public PushCountView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        this.ctx = ctx;
        initView();

    }

    public void iconCanClick(String mid) {
        this.mid = mid;
        if (StrUtil.notEmptyOrNull(mid)) {
            initData();
        }
    }

    private void initData() {
//        ivIcon.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                SelData contact = ContactUtil.getCMByMid(mid, null);
//                if (contact != null) {
//                    Intent newIntent = new Intent(ctx, ContactDetailActivity.class);
//                    newIntent.putExtra(GlobalConstants.KEY_CONTACT, contact);
//                    newIntent.putExtra(GlobalConstants.KEY_CONTACT_NO_TALK, false);
//                    ctx.startActivity(newIntent);
//                }
//            }
//        });
//        XUtil.viewContact(ctx, ivIcon, mid, null);
    }

    protected void initView() {
        if (isInEditMode()) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(ctx);
        RelativeLayout layout = new RelativeLayout(ctx);
        View view = inflater.inflate(R.layout.view_reused_count_icon, layout);
        if (view != null) {
            mLayout = (RelativeLayout) view.findViewById(R.id.rl_push);
            ivIcon = (com.weqia.utils.view.CommonImageView) view.findViewById(R.id.iv_reused_icon);
            tvCount = (TextView) view.findViewById(R.id.tv_reused_new_count);
        }
        this.addView(layout);

    }

    public void setIcon(Integer id) {
        if (ivIcon != null) {
            ivIcon.setImageResource(id);
        }
    }


    public void setCount(String count) {
        if (tvCount != null) {
            tvCount.setText(count);
            tvCount.setVisibility(View.VISIBLE);
        }
        if (count.equalsIgnoreCase(String.valueOf(0))) {
            tvCount.setVisibility(View.INVISIBLE);
        }
    }

    public void hideCountView() {
        if (tvCount != null) {
            tvCount.setVisibility(View.GONE);
        }
    }

    public void visibleCountView() {
        if (tvCount != null) {
            tvCount.setVisibility(View.VISIBLE);
        }
    }

    public com.weqia.utils.view.CommonImageView getIvIcon() {
        return ivIcon;
    }




    public TextView getTvCount() {
        return tvCount;
    }

    public void setIvIcon(com.weqia.utils.view.CommonImageView iView) {
        this.ivIcon = iView;
    }

    public void setTvCount(TextView iView) {
        this.tvCount = iView;
    }

    public Context getCtx() {
        return ctx;
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

}
