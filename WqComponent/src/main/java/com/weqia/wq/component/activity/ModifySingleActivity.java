package com.weqia.wq.component.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.umeng.analytics.MobclickAgent;
import com.weqia.utils.StrUtil;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.view.EditTextWithClear;
import com.weqia.wq.R;
import com.weqia.wq.component.utils.DialogUtil;
import com.weqia.wq.data.global.GlobalConstants;

public class ModifySingleActivity extends SharedModifySettingActivity {

    private EditTextWithClear editTextName;
    private ModifySingleActivity ctx;
    private String oldName;
    private String depart;
    private String group;
    private Integer maxLength = 5000;
    private Integer inputType;  //可输入内容的类型
    protected InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_common);
        initView();
    }

    @Override
    public void initModifyViews() {
        depart = getIntent().getStringExtra("depart");
        if (getIntent().hasExtra("group")) {
            group = getIntent().getStringExtra("group");
        }
        ViewUtils.showView(sharedTitleView.getButtonStringRight());
        if (StrUtil.notEmptyOrNull(depart) || StrUtil.notEmptyOrNull(group)) {
            ViewUtils.setTextView(sharedTitleView.getButtonStringRight(), getString(R.string.title_button_sure));
        } else {
            ViewUtils.setTextView(sharedTitleView.getButtonStringRight(), getString(R.string.title_button_save));
        }
        ViewUtils.bindClickListenerOnViews(this, sharedTitleView.getButtonStringRight());

        String title = sharedTitleView.getTextViewTitle().getText().toString();
        sharedTitleView.initTopBanner(title);
    }

    private void initView() {
        ctx = this;
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        editTextName = (EditTextWithClear) findViewById(R.id.edittxt_name);
        editTextName.getEtReused().setLineSpacing((float) 1.2, (float) 1.2);
        oldName = getIntent().getStringExtra(GlobalConstants.KEY_MODIFY_NAME);
        maxLength = getIntent().getIntExtra(GlobalConstants.KEY_MODIFY_LENGTH, 20);
        inputType = getIntent().getIntExtra(GlobalConstants.KEY_MODIFY_TYPE, -1);
        if (StrUtil.notEmptyOrNull(key)) {
            if (key.equalsIgnoreCase(getString(R.string.discuss_title))) {
                editTextName.setSignLine(false);
                editTextName.setMaxLength(260);
                // editTextName
            } else if (key.equalsIgnoreCase(getString(R.string.discuss_content))) {
                editTextName.setSignLine(false);
                editTextName.setMaxLength(5000);
            } else if (key.equalsIgnoreCase(getString(R.string.str_phone))) {
                editTextName.setSignLine(true);
                editTextName.setMaxLength(20);
                editTextName.getEtReused().setInputType(InputType.TYPE_CLASS_PHONE);
            } else if (key.equalsIgnoreCase(getString(R.string.title_project_target))) {
                editTextName.setSignLine(false);
                editTextName.setMaxLength(5000);
            } else if (key.equalsIgnoreCase(getString(R.string.task_describe))) {
                editTextName.setSignLine(false);
                editTextName.setMaxLength(5000);
            } else if (key.equalsIgnoreCase(getString(R.string.tv_project_name))) {
                editTextName.setSignLine(false);
                editTextName.setMaxLength(256);
            }else if (key.equalsIgnoreCase(getString(R.string.tv_group_name))) {
                editTextName.setSignLine(false);
                editTextName.setMaxLength(256);
            } else {
                if (inputType != -1) {
                    editTextName.getEtReused().setInputType(inputType);
                }
                editTextName.setMaxLength(maxLength);
            }
        } else {
            editTextName.setSignLine(false);
            editTextName.setMaxLength(maxLength);
            if (inputType != -1) {
                editTextName.getEtReused().setInputType(inputType);
            }
        }
        editTextName.setFocusable(true);
        if (StrUtil.notEmptyOrNull(oldName)) {
            editTextName.setInputText(oldName);
            editTextName.getEtReused().setSelection(oldName.length());
        }
        showSoftInput();
    }

    @Override
    public void onClickDo(View v) {
        if (v == sharedTitleView.getButtonStringRight()) {
            String text = editTextName.getInputText();
            if (StrUtil.isEmptyOrNull(text)
                    && sharedTitleView.getTextViewTitle().getText().toString().trim()
                    .equals("任务描述")) {
                DialogUtil.commonShowDialog(ctx, ctx.getString(R.string.task_publish_no_title))
                        .show();
            } else if (StrUtil.isEmptyOrNull(text)
                    && sharedTitleView.getTextViewTitle().getText().toString().trim()
                    .equals("项目名称")) {
                DialogUtil.commonShowDialog(ctx, "项目名称不能为空").show();

            }else if (StrUtil.isEmptyOrNull(text)
                    && sharedTitleView.getTextViewTitle().getText().toString().trim()
                    .equals("分组名称")) {
                DialogUtil.commonShowDialog(ctx, "分组名称不能为空").show();

            } else if (StrUtil.isEmptyOrNull(text)
                    && sharedTitleView.getTextViewTitle().getText().toString().trim()
                    .equals("会议主题")) {
                DialogUtil.commonShowDialog(ctx, "会议主题不能为空").show();
            } else if (StrUtil.isEmptyOrNull(text)
                    && sharedTitleView.getTextViewTitle().getText().toString().trim()
                    .equals("合同费用")) {
                DialogUtil.commonShowDialog(ctx, "合同费用不能为空").show();
            } else {
                Intent newIntent = new Intent();
                newIntent.putExtra(GlobalConstants.KEY_MODIFY_NAME, text);
                this.setResult(RESULT_OK, newIntent);
                hideSoftInput();
                this.finish();
            }
        } else if (v == sharedTitleView.getButtonLeft()) {
            hideSoftInput();
            this.finish();
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

    /**
     * 隐藏软件盘
     */
    private void hideSoftInput() {
        imm.hideSoftInputFromWindow(editTextName.getEtReused().getWindowToken(), 0);
    }

    /**
     * 显示软件盘
     */
    private void showSoftInput() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imm.showSoftInput(editTextName.getEtReused(), 0);
            }
        }, 100);
    }

}
