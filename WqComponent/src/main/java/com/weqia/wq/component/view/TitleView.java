package com.weqia.wq.component.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.weqia.utils.StrUtil;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.view.CommonImageView;
import com.weqia.wq.R;

/**
 * 公共头部
 * @author Berwin
 * @version 1.0
 * @Description :
 * @created 2013-4-6 上午1:52:55
 * @fileName com.weqia.wq1.component.view.TitleView.java
 */
public class TitleView {

    private Activity ctx;
    private FrameLayout rlBanner;
    //左边总布局
    private LinearLayout rlTitle;
    //左边标题文本
    private TextView tvTitle;
    //标题文本左边的下表图标
    private ImageView imIndex;
    //左边后退键
    private ImageButton buttonLeft;
    //左边圆形头像+红点总布局
    private FrameLayout flLeftHeader;
    //左边圆形头像
    private CircularImage ivLeftHeader;  //圆形框头部
    //头像框的小红点
    private TextView tvMsg;
    //    private ImageView ivLeftIcon;
    //左边间隔线
    private CommonImageView ivTitleIcon;
    //中间Tab页(默认隐藏)
    private TabLayout tabTitle;
    //右边文本
    private TextView tvRight;
    //右边添加键
    private ImageButton buttonRight;
    //右边确定键
    private TextView buttonStringRight;
    //右边进度条
    private ProgressBar pbTitle;
    //右边查询键
    private ImageButton ivSer;
    //右边圆形头像
//    private CircleImageView ivRightWeboAt;  //圆形框头部

    public TitleView(Activity ctx) {
        this.ctx = ctx;
    }

    public void loadView() {
        rlBanner = (FrameLayout) ctx.findViewById(com.weqia.wq.R.id.rlBanner);
        rlTitle = (LinearLayout) ctx.findViewById(com.weqia.wq.R.id.rlTitle);
        tvTitle = (TextView) ctx.findViewById(com.weqia.wq.R.id.topbanner_text_title);
        imIndex = ctx.findViewById(com.weqia.wq.R.id.im_index);
        buttonLeft = (ImageButton) ctx.findViewById(com.weqia.wq.R.id.topbanner_button_left);
        ivLeftHeader = (CircularImage) ctx.findViewById(com.weqia.wq.R.id.iv_left_header);
        flLeftHeader = (FrameLayout) ctx.findViewById(R.id.fl_left_header);
        tvMsg = (TextView) ctx.findViewById(R.id.tv_msg);

//        ivLeftIcon = (CircleImageView) ctx.findViewById(com.weqia.wq.R.id.ivLeftIcon);
        ivTitleIcon = (CommonImageView) ctx.findViewById(com.weqia.wq.R.id.topbanner_middle_icon);

        tabTitle = (TabLayout) ctx.findViewById(com.weqia.wq.R.id.tab_title);

        tvRight = (TextView) ctx.findViewById(com.weqia.wq.R.id.tvRight);
        buttonRight = (ImageButton) ctx.findViewById(com.weqia.wq.R.id.topbanner_button_right);
        buttonStringRight = (TextView) ctx.findViewById(com.weqia.wq.R.id.topbanner_button_string_right);
//        ivRightWeboAt = (CircleImageView) ctx.findViewById(com.weqia.wq.R.id.ivRightWeboAt);
        pbTitle = (ProgressBar) ctx.findViewById(com.weqia.wq.R.id.pbTitle);
        ivSer = (ImageButton) ctx.findViewById(com.weqia.wq.R.id.topbanner_iv_ser);
    }

    @SuppressLint("NewApi")
    public void setAlpha(float alpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return;
        } else {
            if (alpha >= 0 && alpha <= 1) {
                View view = ctx.findViewById(com.weqia.wq.R.id.rlBanner);
                view.setAlpha(alpha);
            }
        }
    }

    public void setListener(OnClickListener listener) {
        if (listener != null) {
            ViewUtils.bindClickListenerOnViews(listener, buttonLeft, buttonRight, buttonStringRight, ivTitleIcon, ivSer, ivLeftHeader, tvTitle);
            //, ivRightWeboAt, ivLeftHeader
        }
    }

    /**
     * 初始化title
     * @param title
     * @Description
     */
    public void initTopBanner(String title) {
        initTopBanner(title, null, null, null);
    }

    /**
     * 初始化右方按钮
     * @param title
     * @Description
     */
    public void initTopBanner(String title, Integer rightId) {
        initTopBanner(title, null, rightId);
    }

    public void initTopBanner(String title, Integer leftId, Integer rightId) {
        initTopBanner(title, leftId, rightId, null);
    }


    public void initTopBanner(String title, String rightString) {
        initTopBanner(title, null, null, rightString);
    }

    /**
     * @param title
     * @param leftId
     * @param rightId
     * @param rightString
     */
    public void initTopBanner(String title, Integer leftId, Integer rightId, String rightString) {
        if (buttonLeft != null && leftId != null && leftId != 0) {
            buttonLeft.setImageResource(leftId);
            ViewUtils.showView(buttonLeft);
        }

        if (buttonRight != null && rightId != null && rightId != 0) {
            buttonRight.setImageResource(rightId);
            ViewUtils.showView(buttonRight);
        }

        if (buttonStringRight != null && StrUtil.notEmptyOrNull(rightString)) {
            buttonStringRight.setText(rightString);
            ViewUtils.showView(buttonStringRight);
        }

        if (tvTitle != null) {
            ViewUtils.setTextView(tvTitle, title);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvTitle.getLayoutParams();
            int padding = ctx.getResources().getDimensionPixelSize(com.weqia.wq.R.dimen.default_new_interval);
            if (buttonLeft.getVisibility() == View.GONE) {
                params.setMargins(padding, 0, 0, 0);
//                tvTitle.setCompoundDrawables(null, null, tvTitle.getCompoundDrawables()[2], null);
//                tvTitle.setCompoundDrawables(null, null, null, null);
            } else {
                params.setMargins(0, 0, 0, 0);
//                Drawable drawable = ctx.getResources().getDrawable(R.drawable.title_divider);
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 必须设置图片大小，否则不显示
//                tvTitle.setCompoundDrawables(drawable, null, tvTitle.getCompoundDrawables()[2], null);
//                tvTitle.setCompoundDrawables(drawable, null, null, null);
//                tvTitle.setCompoundDrawablePadding(padding);
            }
            tvTitle.setLayoutParams(params);
        }
    }

    public void initTabView(String tabLeft, String tabRight) {
        tabTitle.addTab(tabTitle.newTab().setText(tabLeft));
        tabTitle.addTab(tabTitle.newTab().setText(tabRight));
    }

    public void initTabView(View tabLeft, View tabRight) {
        TabLayout.Tab tab1 = tabTitle.newTab().setCustomView(tabLeft);
        tabTitle.addTab(tab1);
        tabTitle.addTab(tabTitle.newTab().setCustomView(tabRight));
    }

    public FrameLayout getRlBanner() {
        return rlBanner;
    }

    public Activity getCtx() {
        return ctx;
    }

    public LinearLayout getRlTitle() {
        return rlTitle;
    }

    public TextView getTextViewTitle() {
        return tvTitle;
    }

    public ImageView getImIndex() {
        return imIndex;
    }

    public ImageButton getButtonLeft() {
        return buttonLeft;
    }

    public CircularImage getIvLeftHeader() {
        return ivLeftHeader;
    }

    public CommonImageView getIvTitleIcon() {
        return ivTitleIcon;
    }

    public TabLayout getTabTitle() {
        return tabTitle;
    }

    public TextView getTvRight() {
        return tvRight;
    }

    public ImageButton getButtonRight() {
        return buttonRight;
    }

    public TextView getButtonStringRight() {
        return buttonStringRight;
    }

    public ProgressBar getPbTitle() {
        return pbTitle;
    }

    public ImageButton getIvSer() {
        return ivSer;
    }

//    public CircleImageView getIvRightWeboAt() {
//        return ivRightWeboAt;
//    }

    public TextView getTvMsg() {
        return tvMsg;
    }

    public FrameLayout getFlLeftHeader() {
        return flLeftHeader;
    }
}
