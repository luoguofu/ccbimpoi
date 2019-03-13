package com.weqia.wq.component.activity;

import android.annotation.SuppressLint;
import android.view.View;

import com.weqia.utils.ViewUtils;
import com.weqia.utils.exception.CheckedExceptionHandler;
import com.weqia.wq.component.SelectArrUtil;
import com.weqia.wq.data.global.GlobalConstants;

import java.io.Serializable;

@SuppressLint("Registered")
public class SharedDetailTitleActivity extends SharedTitleActivity {

    protected Serializable dataParam;
    protected String key;
    public String coIdParam;

    @Override
    public void loadView() {
        super.loadView();

        try {
            ViewUtils.showView(sharedTitleView.getButtonLeft());
            if (sharedTitleView.getButtonLeft() != null) {
                sharedTitleView.getButtonLeft().setOnClickListener(this);
            }
        } catch (Exception e) {
            CheckedExceptionHandler.handleException(e);
        }
        getBound();
    }

    private void getBound() {
        String title = getIntent().getStringExtra(GlobalConstants.KEY_TOP_BANNER_TITLE);
        sharedTitleView.initTopBanner(title);
        key = getIntent().getStringExtra(GlobalConstants.KEY_TOP_BANNER_TITLE);
        coIdParam = getIntent().getStringExtra(GlobalConstants.KEY_COID);
        dataParam = getIntent().getSerializableExtra(GlobalConstants.KEY_PARAM_DATA);
    }

    public Serializable getDataParam() {
        return dataParam;
    }

    public String getKey() {
        return key;
    }

    public String getCoIdParam() {
        return coIdParam;
    }

    /**
     * 刷新数据
     */
    public void setRightBt() {
        setRightBt(false, GlobalConstants.IMAGE_MAX);
    }

    /**
     * 刷新数据
     */
    public void setRightBt(boolean showNum, int number) {
        int selecSize = SelectArrUtil.getInstance().getSelImgSize();
        ViewUtils.showView(sharedTitleView.getButtonStringRight());
        sharedTitleView.getButtonStringRight().setCompoundDrawables(null, null, null, null);
        // sharedTitleView.getButtonStringRight().setBackgroundResource(R.drawable.finish_button_highlight);
        String btString = "完成";
        if (showNum) {
            if (selecSize != 0) {
                if (selecSize >= number) {
                    selecSize = number;
                }
                btString = btString + selecSize + "/" + number;
            } else {

            }
        } else {
            if (selecSize >= number) {
                selecSize = number;
            }
            btString = btString + selecSize + "/" + number;
        }
//        if (selecSize == 0) {
//            sharedTitleView.getButtonStringRight().setTextColor(getResources().getColor(R.color.bg_topright_btn));
//        } else {
//            sharedTitleView.getButtonStringRight().setTextColor(getResources().getColor(R.color.bg_topright_btn));
//        }
        sharedTitleView.getButtonStringRight().setText(btString);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        onClickDo(v);
    }

    protected void onClickDo(View v) {
        if (v == sharedTitleView.getButtonLeft()) {
            this.finish();
        }
    }
}
