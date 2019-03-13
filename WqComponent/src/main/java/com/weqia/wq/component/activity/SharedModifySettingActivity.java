package com.weqia.wq.component.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;

import com.umeng.analytics.MobclickAgent;
import com.weqia.utils.StrUtil;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.exception.CheckedExceptionHandler;
import com.weqia.wq.R;
import com.weqia.wq.component.utils.DialogUtil;

@SuppressLint("Registered")
public class SharedModifySettingActivity extends SharedDetailTitleActivity {

    @Override
    public void loadView() {
        super.loadView();
        initModifyViews();
    }

    protected void initModifyViews() {
        ViewUtils.showView(sharedTitleView.getButtonStringRight());
        ViewUtils.setTextView(sharedTitleView.getButtonStringRight(),
                getString(R.string.title_button_save));
        ViewUtils.bindClickListenerOnViews(this, sharedTitleView.getButtonStringRight());

        String title = sharedTitleView.getTextViewTitle().getText().toString();
        sharedTitleView.initTopBanner(getString(R.string.title_modify_prefix) + title);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DialogUtil.DLG_ONLOADING) {
            Dialog dialog = DialogUtil.commonLoadingDialog(this, "");
            dialog.setCancelable(false);
            return dialog;
        }
        return super.onCreateDialog(id);
    }


    public static Integer judgeWeqiaModify(String mNoCount) {
        if (StrUtil.isEmptyOrNull(mNoCount)) {
            return 0;
        }
        try {
            Integer iNo = Integer.parseInt(mNoCount);
            return iNo;
        } catch (NumberFormatException e) {
            CheckedExceptionHandler.handleException(e);
            return 0;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
