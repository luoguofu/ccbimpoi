package com.weqia.wq.component.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weqia.utils.ViewUtils;
import com.weqia.utils.exception.CheckedExceptionHandler;
import com.weqia.utils.view.CommonImageView;
import com.weqia.wq.R;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;
import com.weqia.wq.component.activity.assist.SearchDataListener;

public class RoundSearchView extends LinearLayout implements OnClickListener {

    public EditText etReused;
    private CommonImageView ivClear;
    private SearchDataListener searchListener;
    private View rooView;
    private int sType;
    protected InputMethodManager imm;
    public String lastText = null;
    private SharedDetailTitleActivity ctx;
    private String serCoId = null;

    public RoundSearchView(SharedDetailTitleActivity ctx) {
        this(ctx, null);
    }

    public RoundSearchView(SharedDetailTitleActivity ctx, EditText et) {
        super(ctx);
        this.ctx = ctx;
        if (et != null) {
            etReused = et;
        }
        initView();
    }

    public void setSearchInfo(int sType, SearchDataListener searchListener) {
        this.searchListener = searchListener;
        this.sType = sType;
    }

    public void initView() {
        if (isInEditMode()) {
            return;
        }
        imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (etReused == null) {
            rooView = LayoutInflater.from(ctx).inflate(R.layout.search_bar_with_btn, null);
            if (rooView != null) {
                etReused = (EditText) rooView.findViewById(R.id.search_bar_btn_et_input);

                ivClear = (CommonImageView) rooView.findViewById(R.id.search_bar_btn_iv_clear);
                if (ivClear != null) {
                    ivClear.setOnClickListener(this);
                }
                this.addView(rooView);
            }
        }

        if (etReused != null) {
            etReused.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
            etReused.setInputType(EditorInfo.TYPE_CLASS_TEXT);
            etReused.addTextChangedListener(mTextWatcher);
            etReused.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                    if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                        hideKeyboard();
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v == ivClear) {
            if (etReused != null) {
                etReused.setText("");
            }
        }
    }

    public void clearSearch() {
        ViewUtils.hideView(ivClear);
        if (searchListener != null) {
            searchListener.clearSearch();
        }
    }

    public String getInputText() {
        if (etReused != null) {
            return etReused.getText().toString();
        } else {
            return "";
        }
    }

    public EditText getEtReused() {
        return etReused;
    }

    public CommonImageView getIvClear() {
        return ivClear;
    }

    public TextWatcher getmTextWatcher() {
        return mTextWatcher;
    }

    public void setmTextWatcher(TextWatcher mTextWatcher) {
        this.mTextWatcher = mTextWatcher;
    }

    public int getsType() {
        return sType;
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                int retainCount = temp.length();
                if (retainCount > 0) {
                    if (temp.toString().trim() != null) {
                        searchDo();
                    }
                    ViewUtils.showView(ivClear);
                } else if (retainCount == 0) {
                    clearSearch();
                }
            } catch (Exception e) {
                CheckedExceptionHandler.handleException(e);
            }
        }
    };


    /**
     * 可重载来加载,如果不是本地sql就可以重载这个来获取，可不设sType
     */
    public void searchDo() {
//
    }

    public void hideKeyboard() {
        if (imm != null && etReused != null) {
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(etReused.getWindowToken(), 0);
            }
        }
    }

    public String getSerCoId() {
        return serCoId;
    }

    public void setSerCoId(String serCoId) {
        this.serCoId = serCoId;
    }
}
