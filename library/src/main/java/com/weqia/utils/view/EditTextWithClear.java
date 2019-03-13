package com.weqia.utils.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.weqia.BaseInit;
import com.weqia.utils.DeviceUtil;
import com.weqia.utils.MResource;
import com.weqia.utils.StrUtil;

public class EditTextWithClear extends LinearLayout
        implements
            OnClickListener,
            View.OnFocusChangeListener {

    private EditText etReused;
    private CommonImageView ivClear;

    private boolean isPw = false;
    private boolean isNum = false;

    private boolean singleLine = false;
    private int hint = 0;
    private int text = 0;
    private int maxLength = -1;

    private String hintText = "";
    private String textText = "";

    private int bg = -1;
    private int tvColor;
    private boolean isLeft;
    private boolean focuseChange;
    private float tvSize;

    public EditTextWithClear(Context context) {
        this(context, null);
    }

    public EditTextWithClear(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray a =
                context.obtainStyledAttributes(attrs, MResource.getIdsByName(
                        BaseInit.ctx.getPackageName(), "styleable", "tvClear"));
        isPw =
                a.getBoolean(MResource.getIdByName(BaseInit.ctx.getPackageName(), "styleable",
                        "tvClear_isPw"), false);
        isNum =
                a.getBoolean(MResource.getIdByName(BaseInit.ctx.getPackageName(), "styleable",
                        "tvClear_isNum"), false);
        singleLine =
                a.getBoolean(MResource.getIdByName(BaseInit.ctx.getPackageName(), "styleable",
                        "tvClear_singleLine"), false);

        hint =
                a.getResourceId(MResource.getIdByName(BaseInit.ctx.getPackageName(), "styleable",
                        "tvClear_hint"), 0);
        hintText =
                a.getString(MResource.getIdByName(BaseInit.ctx.getPackageName(), "styleable",
                        "tvClear_hint"));

        text =
                a.getResourceId(MResource.getIdByName(BaseInit.ctx.getPackageName(), "styleable",
                        "tvClear_text"), 0);
        textText =
                a.getString(MResource.getIdByName(BaseInit.ctx.getPackageName(), "styleable",
                        "tvClear_text"));

        bg =
                a.getResourceId(MResource.getIdByName(BaseInit.ctx.getPackageName(), "styleable",
                        "tvClear_bg"), -1);
        tvColor =
                a.getColor(
                        MResource.getIdByName(BaseInit.ctx.getPackageName(), "styleable",
                                "tvClear_tvColor"),
                        getResources().getColor(
                                MResource.getIdByName(BaseInit.ctx.getPackageName(), "color",
                                        "black")));
        maxLength =
                a.getInteger(MResource.getIdByName(BaseInit.ctx.getPackageName(), "styleable",
                        "tvClear_maxLength"), -1);
        isLeft =
                a.getBoolean(MResource.getIdByName(BaseInit.ctx.getPackageName(), "styleable",
                        "tvClear_isLeft"), false);
        focuseChange =
                a.getBoolean(MResource.getIdByName(BaseInit.ctx.getPackageName(), "styleable",
                        "tvClear_focuseChange"), true);

        tvSize =
                a.getDimensionPixelSize(MResource.getIdByName(BaseInit.ctx.getPackageName(),
                        "styleable", "tvClear_tvSize"), (int) (16 * DeviceUtil.getDeviceDensity()));
        a.recycle();
        initView(context);
    }

    @SuppressLint({"InflateParams", "RtlHardcoded"})
    public void initView(Context ctx) {
        if (isInEditMode()) {
            return;
        }
        View rooView =
                LayoutInflater.from(ctx).inflate(
                        MResource.getIdByName(BaseInit.ctx.getPackageName(), "layout",
                                "util_view_reused_edittext"), null);
        etReused =
                (EditText) rooView.findViewById(MResource.getIdByName(
                        BaseInit.ctx.getPackageName(), "id", "et_reused_input"));
        ivClear =
                (CommonImageView) rooView.findViewById(MResource.getIdByName(
                        BaseInit.ctx.getPackageName(), "id", "iv_clear_et"));
        if (etReused != null) {
            if (hint != 0) {
                etReused.setHint(hint);
            }
            etReused.setSingleLine(true);
            if (StrUtil.notEmptyOrNull(hintText)) {
                etReused.setHint(hintText);
            }
            if (text != 0) {
                etReused.setText(text);
            }
            if (StrUtil.notEmptyOrNull(textText)) {
                etReused.setText(textText);
            }
            if (isPw) {// chenjh add
                etReused.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            } else {
                etReused.setInputType(InputType.TYPE_CLASS_TEXT);
            }

            if (isNum) {// chenjh add
                // etReused.setInputType(InputType.TYPE_CLASS_NUMBER);
                etReused.setInputType(InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            } else {
                etReused.setInputType(InputType.TYPE_CLASS_TEXT);
            }

            etReused.setTextSize(tvSize / DeviceUtil.getDeviceDensity());

            etReused.setTextColor(tvColor);

            if (singleLine) {
                etReused.setSingleLine(true);
            } else {
                etReused.setSingleLine(false);
            }

            if (bg != -1) {
                etReused.setBackgroundResource(bg);
            }

            if (maxLength != -1) {
                etReused.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
            }
            if (isLeft) {
                etReused.setGravity(Gravity.LEFT | Gravity.CENTER);
            } else {
                etReused.setGravity(Gravity.RIGHT | Gravity.CENTER);
            }
            etReused.addTextChangedListener(mTextWatcher);
            etReused.setOnFocusChangeListener(this);
        }
        if (ivClear != null) {
            ivClear.setOnClickListener(this);
            ivClear.setVisibility(View.INVISIBLE);
        }

        this.addView(rooView,
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void onClick(View v) {
        if (v == ivClear) {
            if (etReused != null) {
                etReused.setText("");
                ivClear.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void setMaxLength(int length) {
        if (length != -1) {
            etReused.setFilters(new InputFilter[] {new InputFilter.LengthFilter(length)});
        }
    }

    public void setHint(Integer id) {
        if (etReused != null) {
            etReused.setHint(id);
        }
    }

    public void setHint(CharSequence str) {
        if (etReused != null) {
            etReused.setHint(str);
        }
    }

    public EditText getEtReused() {
        return etReused;
    }

    public CommonImageView getIvClear() {
        return ivClear;
    }

    @Override
    public void setEnabled(boolean enabled) {
        etReused.setEnabled(enabled);
        ivClear.setEnabled(enabled);
        ivClear.setVisibility(View.GONE);
        super.setEnabled(enabled);
    }

    @Override
    public void setDuplicateParentStateEnabled(boolean enabled) {
        etReused.setDuplicateParentStateEnabled(enabled);
        ivClear.setDuplicateParentStateEnabled(enabled);
        super.setDuplicateParentStateEnabled(enabled);
    }

    public void setInputText(String str) {
        if (etReused != null && StrUtil.notEmptyOrNull(str)) {
            etReused.setText(str);
            // etReused.setSelection(str.length());
            // ivClear.setVisibility(View.VISIBLE);
        }
    }

    public void setBgNull() {
        if (etReused != null) {
            etReused.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public void setSignLine(boolean bSignLine) {
        if (etReused != null) {
            etReused.setSingleLine(bSignLine);
        }
    }

    public String getInputText() {
        if (etReused != null) {
            return etReused.getText().toString();
        } else {
            return "";
        }
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // mTextView.setText(s);

        }

        @Override
        public void afterTextChanged(Editable s) {
            // chenjh modify
            if (!"".equals(s) && null != s && s.length() == 1) {
                etReused.setSelection(s.length());
            }
            if (isPw) {
                etReused.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            int retainCount = temp.length();
            if (retainCount > 0) {
                if (ivClear != null && ivClear.getVisibility() == View.INVISIBLE) {
                    ivClear.setVisibility(View.VISIBLE);
                }
            } else if (retainCount == 0) {
                if (ivClear != null) {
                    ivClear.setVisibility(View.INVISIBLE);
                }
            }
        }

    };

    @SuppressLint("RtlHardcoded")
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {// 获得焦点
            if (v == etReused) {
                if (ivClear != null) {
                    if (getInputText().length() > 0) {
                        ivClear.setVisibility(View.VISIBLE);
                    }
                }
                if (isFocuseChange()) {
                    etReused.setGravity(Gravity.LEFT | Gravity.CENTER);
                }

            }
        } else {
            if (ivClear != null) {
                ivClear.setVisibility(View.INVISIBLE);
            }
            if (isFocuseChange()) {
                etReused.setGravity(Gravity.RIGHT | Gravity.CENTER);
            }
        }
    }

    public boolean isFocuseChange() {
        return focuseChange;
    }

    public void setFocuseChange(boolean focuseChange) {
        this.focuseChange = focuseChange;
    }
}
