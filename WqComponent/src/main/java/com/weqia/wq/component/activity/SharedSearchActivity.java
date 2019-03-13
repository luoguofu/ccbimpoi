package com.weqia.wq.component.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.exception.CheckedExceptionHandler;
import com.weqia.utils.view.CommonImageView;
import com.weqia.utils.view.pullrefresh.PullToRefreshBase.Mode;
import com.weqia.utils.view.pullrefresh.PullToRefreshBase.OnLastItemVisibleListener;
import com.weqia.utils.view.pullrefresh.PullToRefreshListView;
import com.weqia.wq.R;
import com.weqia.wq.component.activity.assist.SearchDataListener;
import com.weqia.wq.component.activity.assist.SharedSearchAdapter;
import com.weqia.wq.component.utils.GlobalUtil;
import com.weqia.wq.component.utils.request.ResultEx;
import com.weqia.wq.component.utils.request.ServiceParams;
import com.weqia.wq.component.utils.request.ServiceRequester;
import com.weqia.wq.component.utils.request.UserService;
import com.weqia.wq.data.SearchEnum;
import com.weqia.wq.data.global.GlobalConstants;

import java.util.ArrayList;
import java.util.List;

public abstract class SharedSearchActivity<T> extends SharedDetailTitleActivity
        implements
        OnItemClickListener {

    protected static final int SMALL_PAGE = 3;

    protected EditText etSearch;
    public PullToRefreshListView plSearch;
    private ListView lvSearch;
    // private SharedSearchAdapter<SelData> adapter;
    private CommonImageView ivClear;
    public Button btnSearch;
    private SearchDataListener searchListener;
    private InputMethodManager imm;
    private List<T> selDatas = new ArrayList<>();
    protected SearchEnum currentSer = null;
    protected String lastText = null;
    protected String sKey;

    // 针对startRow和endRow
    private static int page = 0;
    protected static final int pageNextId = -1;

    public boolean autoSearch = true;
    private SharedSearchActivity<T> mctx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shared_search);
        mctx = this;
        initView();
        initData();
        havaParamDo();
    }

    /**
     * 列表adapter
     *
     * @return
     */
    public abstract SharedSearchAdapter<T> getAdapter();

    /**
     * 对已采用prewId和nextId的需要重写此方法
     *
     * @param data
     * @return
     */
    public Integer getIdByData(T data) {
        return null;
    }

    /**
     * 网络加载的需要重写此方法。value > 100
     *
     * @param prewId
     * @param nextId
     * @return
     */
    protected ServiceParams getParam(Integer prewId, Integer nextId) {
        return null;
    }

    public PullToRefreshListView getPlSearch() {
        return plSearch;
    }

    private void havaParamDo() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Integer sType = bundle.getInt(GlobalConstants.KEY_SEARCH_TYPE);
            sKey = bundle.getString(GlobalConstants.KEY_SEARCH_KEY);
            if (sType == null) {
                return;
            }
            currentSer = SearchEnum.valueOf(sType);  //返回枚举类型
            if (currentSer == null) {
                return;
            }
            etSearch.setHint(currentSer.getHint());  //设置隐藏字
            if (StrUtil.notEmptyOrNull(sKey)) {
                etSearch.setText(sKey);
                etSearch.setSelection(sKey.length()); //光标定位到字的后面
//                List<T> members =
//                        (List<T>) getDbUtil().findAllByWhere(currentSer.getCls(),
//                                GlobalUtil.getSerKey(currentSer.value(), sKey, getCoIdParam()));
//                if (StrUtil.listNotNull(members)) {
//                    selDatas.addAll(members);
//                    getAdapter().setItems(selDatas);
//                }
            }

            if (StrUtil.isEmptyOrNull(sKey) && StrUtil.isEmptyOrNull(lastText)) {
                // 弹出键盘
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
//                        if (mctx instanceof ApprovalSearchActivity) {
//                            return;
//                        }
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }, 300);
            }
        }
    }

    private void initView() {
        etSearch = (EditText) findViewById(R.id.et_search);
        plSearch = (PullToRefreshListView) findViewById(R.id.pl_search_result);
        plSearch.setAdapter(getAdapter());
        btnSearch = (Button) findViewById(R.id.btnSearch);
        plSearch.setMode(Mode.DISABLED);
        plSearch.setmListLoadMore(true);
        lvSearch = plSearch.getRefreshableView();
        lvSearch.setOnItemClickListener(this);

        imm =
                (InputMethodManager) getApplicationContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);  //软键盘控制
        ivClear = (CommonImageView) findViewById(R.id.search_bar_btn_iv_clear);
        if (ivClear != null) {
            ivClear.setOnClickListener(this); //清理点击事件
        }

        if (etSearch != null) {
            etSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
            etSearch.setInputType(EditorInfo.TYPE_CLASS_TEXT);  //输入的类型是文字
            etSearch.addTextChangedListener(mTextWatcher);
        }
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard();
                }
                return false;
            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {  //搜索点击事件
            @Override
            public void onClick(View v) {
                hideKeyboard();
                beginSearch();  //开始搜索
            }
        });

        plSearch.setOnScrollListener(new OnScrollListener() {  //listview的滚动事件

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);  //隐藏软键盘
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {

            }
        });

        // 加载更多
        plSearch.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {  //下拉加载

            @Override
            public void onLastItemVisible() {
                if (getSelDatas().size() < GlobalConstants.PAGE_COUNT) {
                    loadComplete();
                } else {
                    if (isPageStyle()) {
                        page++;
                        searchDataFromNet(page, pageNextId);
                    } else {
                        T lastData = getSelDatas().get(getSelDatas().size() - 1);
                        if (lastData != null) {
                            searchDataFromNet(null, getIdByData(lastData));
                        } else {
                            if (L.D) L.e("错误，需修复");
                        }
                    }
                }
            }

            @Override
            public void onLastItemFast() {
            }
        });
        ViewUtils.bindClickListenerOnViews(this, this, R.id.iv_search_back);
    }

    private void initData() {
        lvSearch.setAdapter(getAdapter());
    }

    public void loadComplete() {
        GlobalUtil.loadComplete(plSearch, mctx, false);
    }

    public void searchDo() {
        page = 0;   //分页数
        if (currentSer.value() > 100) {  //枚举值 
            if (isPageStyle()) {
                searchDataFromNet(page, pageNextId);
            } else {
                searchDataFromNet(null, null);
            }
        } else {

        }
    }

    /**
     * 是分page的模式
     *
     * @return
     */
    private boolean isPageStyle() { //指定的搜索对象
        return currentSer.value() == SearchEnum.S_NET_COMPANY.value()  //请输入
                || currentSer.value() == SearchEnum.S_NET_MEMBER.value();  //搜索朋友
    }

    private void beginSearch() {  //开始搜索
        lastText = etSearch.getText().toString();  //得到改变后的输入框的内容
        if (StrUtil.isEmptyOrNull(lastText)) {
            return;
        }
        if (currentSer == null) {  //枚举值
            return;
        }
        searchDo();  //执行搜索操作
    }

    protected void searchDataFromNet(final Integer prewId, final Integer nextId) {
        ViewUtils.showView(sharedTitleView.getPbTitle());  //显示进度条
        ServiceParams params = getParam(prewId, nextId);
        if (params == null) {
            loadComplete();
            return;
        }

        UserService.getDataFromServer(false, params, wantReqCache(),
                new ServiceRequester(this, params.toString(), btnSearch) {

                    @Override
                    public void onResult(ResultEx resultEx) {
                        loadComplete();
                        if (resultEx.isSuccess()) {

                            List<T> list = (List<T>) resultEx.getDataArray(currentSer.getCls());  //数据类型
                            if (list == null) {
                                list = new ArrayList<>();
                            }
                            if (list == null || list.size() < GlobalConstants.PAGE_COUNT) {
                                getPlSearch().setmListLoadMore(false);
                            } else {
                                getPlSearch().setmListLoadMore(true);
                            }
                            if (nextId != null && nextId == pageNextId) {
                                if (page == 0) {
                                    setSelDatas(list);//配置集合
                                } else {
                                    getSelDatas().addAll(list);
                                }
                            } else {
                                if (nextId == null) {
                                    setSelDatas(list);
                                } else {
                                    getSelDatas().addAll(list);
                                }
                            }
                            getAdapter().setItems(getSelDatas());  //跟新数据
                        }
                    }

                    @Override
                    public void onError(Integer errCode) {
                        loadComplete();
                    }
                });
    }

    TextWatcher mTextWatcher = new TextWatcher() {  //编辑框监听器
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
                    if (ivClear != null && temp.toString().trim() != null && autoSearch) {
                        beginSearch();  //开始搜索信息
                    }
                    if (ivClear.getVisibility() == View.GONE) {   //显示清除按钮
                        ivClear.setVisibility(View.VISIBLE);
                    }
                } else if (retainCount == 0) {
                    if (ivClear != null) {
                        ivClear.setVisibility(View.GONE);
                    }
                    if (getAdapter() != null) {
                        getAdapter().setItems(null);
                    }
                    if (searchListener != null) {
                        searchListener.clearSearch();
                    }
                    loadComplete();
                }
            } catch (Exception e) {
                CheckedExceptionHandler.handleException(e);
            }

        }
    };

    @Override
    public void onClick(View v) {  //点击事件
        if (v.getId() == R.id.iv_search_back) {  //返回键
            backDo();
        } else if (v == ivClear) {   //清除编辑框
            clearSearch();
        }
    }

    public List<T> getSelDatas() {
        if (selDatas == null) {
            selDatas = new ArrayList<>();
        }
        return selDatas;
    }

    public void setSelDatas(List<T> selDatas) {
        this.selDatas = selDatas;
    }

    public void showHomeView(List<T> list) {
        if (StrUtil.listIsNull(list) && currentSer.value() != SearchEnum.S_WORKER.value()) {
            ViewUtils.showViews(this, R.id.ll_show);
        } else {
            ViewUtils.hideViews(this, R.id.ll_show);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backDo();
    }

    private void backDo() {
        SharedSearchActivity.this.finish();
        SharedSearchActivity.this.setResult(RESULT_OK);
        overridePendingTransition(R.anim.util_fading_in, R.anim.util_fading_out);
        //Activity切换时的动画效果方法，在Android2.0版本之后有效果，一个参数是Activity进入时实现的效果，第二个参数是退出是实现的效果
    }

    public void clearSearch() {  //清除编辑框
        if (etSearch != null) {
            etSearch.setText("");
            ivClear.setVisibility(View.GONE);
            if (getAdapter() != null) {
                getAdapter().setItems(null);
            }
            if (searchListener != null) {
                searchListener.clearSearch();
            }
            loadComplete();
        }
    }

    protected boolean wantReqCache() {
        return true;
    }

    protected void hideKeyboard() {
        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
    }

}
