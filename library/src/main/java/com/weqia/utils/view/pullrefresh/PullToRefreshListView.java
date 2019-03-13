/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *******************************************************************************/
package com.weqia.utils.view.pullrefresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.weqia.BaseInit;
import com.weqia.utils.L;
import com.weqia.utils.MResource;
import com.weqia.utils.exception.CheckedExceptionHandler;
import com.weqia.utils.view.LoadingMoreView;
import com.weqia.utils.view.LoadingTopView;
import com.weqia.utils.view.pullrefresh.internal.EmptyViewMethodAccessor;
import com.weqia.utils.view.pullrefresh.internal.LoadingLayout;

public class PullToRefreshListView extends PullToRefreshAdapterViewBase<ListView> {

    private LoadingLayout mHeaderLoadingView;
    private LoadingLayout mFooterLoadingView;

    private FrameLayout mLvFooterLoadingFrame;
    private boolean mListViewExtrasEnabled;
    private View moreView;
    // private View refeshView;
    private Context context;

    // 自动刷新view
    private View topView;
    // 头部是否显示
    private boolean isHeadShow = false;
    // 上次一个top位置
    private int lastTop = 0;
    private boolean mListHeadView = false;
    private boolean topCalled = false;
    private boolean bottomCalled = false;

    private OnTopViewListener topViewListener;
    private FirstVisibleItemCallBack callBack;
    // 滑动时是否加载图片
    private boolean busyMode = true;

    public PullToRefreshListView(Context context) {
        super(context);
        this.context = context;
    }

    public void initFirstVisibleItemCallBack(FirstVisibleItemCallBack callBack) {
        this.callBack = callBack;
    }

    public boolean ismListLoadMore() {
        return mListLoadMore;
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        // initView();
    }

    public PullToRefreshListView(Context context, Mode mode) {
        super(context, mode);
        this.context = context;
        // initView();
    }

    public PullToRefreshListView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
        this.context = context;
        // initView();
    }

    public boolean addMoreView() {
        ListView lv = this.getRefreshableView();
        if (lv != null) {
            if (lv.getFooterViewsCount() <= 1) {
                if (L.D) L.e("add more view");
                if (moreView == null) {
                    moreView = new LoadingMoreView(context).getMoreView();
                    moreView.setId(getRefreshableView().getId() + 1000);
                }
                lv.addFooterView(moreView);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void addHeadView() {
        if (L.D) L.e("add head view");

        // if (getHeaderViewsCount() <= 1) {
        if (topView == null) {
            topView = new LoadingTopView(context).getMoreView();
            topView.setId(getId() + 2000);
        }
        try {
            this.getRefreshableView().addHeaderView(topView);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        isHeadShow = true;
    }

    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected void onRefreshing(final boolean doScroll) {
        /**
         * If we're not showing the Refreshing view, or the list is empty, the the header/footer
         * views won't show so we use the normal method.
         */
        ListAdapter adapter = mRefreshableView.getAdapter();
        if (!mListViewExtrasEnabled || !getShowViewWhileRefreshing() || null == adapter
                || adapter.isEmpty()) {
            super.onRefreshing(doScroll);
            return;
        }

        super.onRefreshing(false);

        final LoadingLayout origLoadingView, listViewLoadingView, oppositeListViewLoadingView;
        final int selection, scrollToY;

        switch (getCurrentMode()) {
            case MANUAL_REFRESH_ONLY:
            case PULL_FROM_END:
                origLoadingView = getFooterLayout();
                listViewLoadingView = mFooterLoadingView;
                oppositeListViewLoadingView = mHeaderLoadingView;
                selection = mRefreshableView.getCount() - 1;
                scrollToY = getScrollY() - getFooterSize();
                break;
            case PULL_FROM_START:
            default:
                origLoadingView = getHeaderLayout();
                listViewLoadingView = mHeaderLoadingView;
                oppositeListViewLoadingView = mFooterLoadingView;
                selection = 0;
                scrollToY = getScrollY() + getHeaderSize();
                break;
        }

        // Hide our original Loading View
        origLoadingView.reset();
        origLoadingView.hideAllViews();

        // Make sure the opposite end is hidden too
        oppositeListViewLoadingView.setVisibility(View.GONE);

        // Show the ListView Loading View and set it to refresh.
        listViewLoadingView.setVisibility(View.VISIBLE);
        listViewLoadingView.refreshing();

        if (doScroll) {
            // We need to disable the automatic visibility changes for now
            disableLoadingLayoutVisibilityChanges();
            // if (L.D) L.e("select == " + selection);

            // We scroll slightly so that the ListView's header/footer is at the
            // same Y position as our normal header/footer
            setHeaderScroll(scrollToY);
            //
            // // Make sure the ListView is scrolled to show the loading
            // // header/footer
            //
            mRefreshableView.setSelection(selection);

            // Smooth scroll as normal
            smoothScrollTo(0);
        }
    }

    @Override
    protected void onReset() {
        // L.e("onReset");
        /**
         * If the extras are not enabled, just call up to super and return.
         */
        if (!mListViewExtrasEnabled) {
            super.onReset();
            return;
        }

        final LoadingLayout originalLoadingLayout, listViewLoadingLayout;
        final int scrollToHeight, selection;
        final boolean scrollLvToEdge;

        switch (getCurrentMode()) {
            case MANUAL_REFRESH_ONLY:
            case PULL_FROM_END:
                originalLoadingLayout = getFooterLayout();
                listViewLoadingLayout = mFooterLoadingView;
                selection = mRefreshableView.getCount() - 1;
                scrollToHeight = getFooterSize();
                scrollLvToEdge =
                        Math.abs(mRefreshableView.getLastVisiblePosition() - selection) <= 1;
                break;
            case PULL_FROM_START:
            default:
                originalLoadingLayout = getHeaderLayout();
                listViewLoadingLayout = mHeaderLoadingView;
                scrollToHeight = -getHeaderSize();
                selection = 0;
                scrollLvToEdge =
                        Math.abs(mRefreshableView.getFirstVisiblePosition() - selection) <= 1;
                break;
        }

        // If the ListView header loading layout is showing, then we need to
        // flip so that the original one is showing instead
        if (listViewLoadingLayout.getVisibility() == View.VISIBLE) {

            // Set our Original View to Visible
            originalLoadingLayout.showInvisibleViews();

            // Hide the ListView Header/Footer
            listViewLoadingLayout.setVisibility(View.GONE);

            /**
             * Scroll so the View is at the same Y as the ListView header/footer, but only scroll
             * if: we've pulled to refresh, it's positioned correctly
             */
            if (scrollLvToEdge && getState() != State.MANUAL_REFRESHING) {
                mRefreshableView.setSelection(selection);
                setHeaderScroll(scrollToHeight);
            }
        }

        // Finally, call up to super
        super.onReset();
    }

    @Override
    protected LoadingLayoutProxy createLoadingLayoutProxy(final boolean includeStart,
            final boolean includeEnd) {
        LoadingLayoutProxy proxy = super.createLoadingLayoutProxy(includeStart, includeEnd);

        if (mListViewExtrasEnabled) {
            final Mode mode = getMode();

            if (includeStart && mode.showHeaderLoadingLayout()) {
                proxy.addLayout(mHeaderLoadingView);
            }
            if (includeEnd && mode.showFooterLoadingLayout()) {
                proxy.addLayout(mFooterLoadingView);
            }
        }

        return proxy;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int state) {
        super.onScrollStateChanged(view, state);
        if (state == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            this.requestFocusFromTouch();
        }

        // if (isBusyMode()) {
        // if (state == OnScrollListener.SCROLL_STATE_IDLE) {
        // BitmapUtil.getInstance().resume();
        // } else {
        // BitmapUtil.getInstance().pause();
        // }
        // }

        // L.e("top == " + topCalled + " , bottom == " + bottomCalled);
        if (!bottomCalled) {
            if (state == OnScrollListener.SCROLL_STATE_IDLE && null != mOnLastItemVisibleListener
                    && mLastItemVisible) {
                if (mListLoadMore) {
                    mOnLastItemVisibleListener.onLastItemVisible();
                    bottomCalled = true;
                }
            }
        }


        if (!topCalled) {
            if (state == OnScrollListener.SCROLL_STATE_IDLE && null != topViewListener
                    && isHeadShow) {
                if (mListHeadView) {
                    topViewListener.onRefresh(this);
                    topCalled = true;
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
            int totalItemCount) {
        super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        if (callBack != null) {
            callBack.firstVisibleItem(firstVisibleItem);
        }
        if ((totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount - 1)
                && (firstVisibleItem != 0)) {
            if (mListLoadMore) {
                addMoreView();
                // addRefeshView();
            }
        }

        // if (L.D) {
        // L.d("First Visible: " + firstVisibleItem +
        // ". Visible Count: " + visibleItemCount
        // + ". Total Items:" + totalItemCount);
        // }


        // L.e("first == " + firstVisibleItem + " , lastTop = " + lastTop + ",  isHeadShow == " +
        // isHeadShow);
        if (mListHeadView) {
            // 判断头部是否需要添加view
            if (firstVisibleItem == lastTop) {
                return;
            } else {
                lastTop = firstVisibleItem;
            }
            if (firstVisibleItem == 0 && !isHeadShow) {
                addHeadView();
            }
        }
    }

    public boolean isBusyMode() {
        return busyMode;
    }

    public void setBusyMode(boolean busyMode) {
        this.busyMode = busyMode;
    }

    public interface FirstVisibleItemCallBack {
        public void firstVisibleItem(int position);
    }


    public void setmListLoadMore(boolean mListLoadMore) {
        this.mListLoadMore = mListLoadMore;
        if (!mListLoadMore) {
            onLoadMoreComplete();
            if (this.getRefreshableView().getFooterViewsCount() > 0) {
                if (moreView != null) {
                    this.getRefreshableView().removeFooterView(moreView);
                }
            }
        }
    }

    public void setmListHeadView(boolean mListHeadView, OnTopViewListener onTopViewListener) {
        this.mListHeadView = mListHeadView;
        this.topViewListener = onTopViewListener;
        if (!mListHeadView) {
            onLoadMoreComplete();
            if (topView != null) {
                this.getRefreshableView().removeHeaderView(topView);
            }
        }
    }

    /**
     * 加载更多完成
     */
    public void onLoadMoreComplete() {
        bottomCalled = false;
        if (this.getRefreshableView().getFooterViewsCount() > 0) {
            if (moreView != null) {
                this.getRefreshableView().removeFooterView(moreView);
            } else {
                View view = findViewById((getRefreshableView().getId() + 1000));
                if (view != null) {
                    this.getRefreshableView().removeFooterView(view);
                }
            }
        }
    }

    public void onLoadTopComplete() {
        topCalled = false;
        if (mListHeadView) {
            if (isHeadShow) {
                if (topView != null) {
                    this.getRefreshableView().removeHeaderView(topView);
                }
                isHeadShow = false;
            }
        }
    }

    protected ListView createListView(Context context, AttributeSet attrs) {
        final ListView lv;
        if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
            lv = new InternalListViewSDK9(context, attrs);
        } else {
            lv = new InternalListView(context, attrs);
        }
        return lv;
    }

    @Override
    protected ListView createRefreshableView(Context context, AttributeSet attrs) {
        ListView lv = createListView(context, attrs);

        // Set it to this so it can be used in ListActivity/ListFragment
        lv.setId(android.R.id.list);
        return lv;
    }

    @Override
    protected void handleStyledAttributes(TypedArray a) {
        super.handleStyledAttributes(a);

        mListViewExtrasEnabled =
                a.getBoolean(MResource.getIdByName(BaseInit.ctx.getPackageName(), "styleable",
                        "PullToRefresh_ptrListViewExtrasEnabled"), true);

        if (mListViewExtrasEnabled) {
            final FrameLayout.LayoutParams lp =
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);

            // Create Loading Views ready for use later
            FrameLayout frame = new FrameLayout(getContext());
            mHeaderLoadingView = createLoadingLayout(getContext(), Mode.PULL_FROM_START, a);
            mHeaderLoadingView.setVisibility(View.GONE);
            frame.addView(mHeaderLoadingView, lp);
            mRefreshableView.addHeaderView(frame, null, false);

            mLvFooterLoadingFrame = new FrameLayout(getContext());
            mFooterLoadingView = createLoadingLayout(getContext(), Mode.PULL_FROM_END, a);
            mFooterLoadingView.setVisibility(View.GONE);
            mLvFooterLoadingFrame.addView(mFooterLoadingView, lp);

            /**
             * If the value for Scrolling While Refreshing hasn't been explicitly set via XML,
             * enable Scrolling While Refreshing.
             */
            if (!a.hasValue(MResource.getIdByName(BaseInit.ctx.getPackageName(), "styleable",
                    "PullToRefresh_ptrScrollingWhileRefreshingEnabled"))) {
                setScrollingWhileRefreshingEnabled(true);
            }
        }
    }

    @TargetApi(9)
    final class InternalListViewSDK9 extends InternalListView {

        public InternalListViewSDK9(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
                int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY,
                boolean isTouchEvent) {

            final boolean returnValue =
                    super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                            scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

            // Does all of the hard work...
            OverscrollHelper.overScrollBy(PullToRefreshListView.this, deltaX, scrollX, deltaY,
                    scrollY, isTouchEvent);

            return returnValue;
        }
    }

    protected class InternalListView extends ListView implements EmptyViewMethodAccessor {

        private boolean mAddedLvFooter = false;

        public InternalListView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            /**
             * This is a bit hacky, but Samsung's ListView has got a bug in it when using
             * Header/Footer Views and the list is empty. This masks the issue so that it doesn't
             * cause an FC. See Issue #66.
             */
            try {
                super.dispatchDraw(canvas);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                // CheckedExceptionHandler.handleException(e);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            /**
             * This is a bit hacky, but Samsung's ListView has got a bug in it when using
             * Header/Footer Views and the list is empty. This masks the issue so that it doesn't
             * cause an FC. See Issue #66.
             */
            try {
                return super.dispatchTouchEvent(ev);
            } catch (IndexOutOfBoundsException e) {
                CheckedExceptionHandler.handleException(e);
                return false;
            }
        }

        @Override
        public void setAdapter(ListAdapter adapter) {
            // Add the Footer View at the last possible moment
            if (null != mLvFooterLoadingFrame && !mAddedLvFooter) {
                addFooterView(mLvFooterLoadingFrame, null, false);
                mAddedLvFooter = true;
            }

            super.setAdapter(adapter);
        }

        @Override
        public void setEmptyView(View emptyView) {
            PullToRefreshListView.this.setEmptyView(emptyView);
        }

        @Override
        public void setEmptyViewInternal(View emptyView) {
            super.setEmptyView(emptyView);
        }
    }

    public interface OnTopViewListener {
        public void onRefresh(final PullToRefreshListView refreshView);
    }


    public LoadingLayout getmHeaderLoadingView() {
        return mHeaderLoadingView;
    }

    public void setmHeaderLoadingView(LoadingLayout mHeaderLoadingView) {
        this.mHeaderLoadingView = mHeaderLoadingView;
    }
}
