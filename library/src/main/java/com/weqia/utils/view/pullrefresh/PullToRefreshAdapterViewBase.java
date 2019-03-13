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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.weqia.BaseInit;
import com.weqia.utils.L;
import com.weqia.utils.MResource;
import com.weqia.utils.view.pullrefresh.internal.EmptyViewMethodAccessor;
import com.weqia.utils.view.pullrefresh.internal.IndicatorLayout;

public abstract class PullToRefreshAdapterViewBase<T extends AbsListView>
        extends PullToRefreshBase<T> implements OnScrollListener {

    public boolean mListLoadMore = true;

    // private long lastEndTime = 0l;

    private static FrameLayout.LayoutParams convertEmptyViewLayoutParams(ViewGroup.LayoutParams lp) {
        FrameLayout.LayoutParams newLp = null;

        if (null != lp) {
            newLp = new FrameLayout.LayoutParams(lp);

            if (lp instanceof LinearLayout.LayoutParams) {
                newLp.gravity = ((LinearLayout.LayoutParams) lp).gravity;
            } else {
                newLp.gravity = Gravity.CENTER;
            }
        }

        return newLp;
    }

    protected boolean mLastItemVisible;
    protected OnScrollListener mOnScrollListener;
    protected OnLastItemVisibleListener mOnLastItemVisibleListener;
    private View mEmptyView;

    private IndicatorLayout mIndicatorIvTop;
    private IndicatorLayout mIndicatorIvBottom;

    private boolean mShowIndicator = false;
    private boolean mScrollEmptyView = true;

    public PullToRefreshAdapterViewBase(Context context) {
        super(context);
        intScrollListen();
    }

    public PullToRefreshAdapterViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        intScrollListen();
    }

    public PullToRefreshAdapterViewBase(Context context, Mode mode) {
        super(context, mode);
        intScrollListen();
    }

    public PullToRefreshAdapterViewBase(Context context, Mode mode, AnimationStyle animStyle) {
        super(context, mode, animStyle);
        intScrollListen();
    }

    private void intScrollListen() {
        mRefreshableView.setOnScrollListener(this);
//        mRefreshableView.setOnScrollListener(new PauseOnScrollListener(UtilApplication
//                .getInstance().getBitmapUtil(), true, true, this));
    }

    /**
     * Gets whether an indicator graphic should be displayed when the View is in a state where a
     * Pull-to-Refresh can happen. An example of this state is when the Adapter View is scrolled to
     * the top and the mode is set to {@link Mode#PULL_FROM_START}. The default value is
     * <var>true</var> if {@link PullToRefreshBase#isPullToRefreshOverScrollEnabled()
     * isPullToRefreshOverScrollEnabled()} returns false.
     * 
     * @return true if the indicators will be shown
     */
    public boolean getShowIndicator() {
        return mShowIndicator;
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem,
            final int visibleItemCount, final int totalItemCount) {
        //
        // if (L.D) {
        // L.d("First Visible: " + firstVisibleItem +
        // ". Visible Count: " + visibleItemCount
        // + ". Total Items:" + totalItemCount);
        // }

        /**
         * Set whether the Last Item is Visible. lastVisibleItemIndex is a zero-based index, so we
         * minus one totalItemCount to check
         */
        if (null != mOnLastItemVisibleListener) {
            mLastItemVisible =
                    (totalItemCount > 0)
                            && (firstVisibleItem + visibleItemCount >= totalItemCount - 1)
                            && (firstVisibleItem != 0);
            // if (mLastItemVisible) {
            //
            // }
        }

        // If we're showing the indicator, check positions...
        if (getShowIndicatorInternal()) {
            updateIndicatorViewsVisibility();
        }

        // Finally call OnScrollListener if we have one
        if (null != mOnScrollListener) {
            mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @Override
    public void onScrollStateChanged(final AbsListView view, final int state) {
        /**
         * Check that the scrolling has stopped, and that the last item is visible.
         */
        // if (state == OnScrollListener.SCROLL_STATE_IDLE && null != mOnLastItemVisibleListener
        // && mLastItemVisible) {
        //
        // // if (System.currentTimeMillis() - lastEndTime > 1000) {
        // // addMoreView();
        //
        // // lastEndTime = System.currentTimeMillis();
        // // } else {
        // // L.toastShort("亲，你刷得太快了，喝杯茶歇歇吧...");
        // // lastEndTime = System.currentTimeMillis();
        // // mOnLastItemVisibleListener.onLastItemFast();
        // // }
        // }
        if (null != mOnScrollListener) {
            mOnScrollListener.onScrollStateChanged(view, state);
        }
    }

    /**
     * Pass-through method for {@link PullToRefreshBase#getRefreshableView() getRefreshableView()}.
     * {@link AdapterView#setAdapter(android.widget.Adapter)} setAdapter(adapter)}. This is just for
     * convenience!
     * 
     * @param adapter - Adapter to set
     */
    public void setAdapter(ListAdapter adapter) {
        ((AdapterView<ListAdapter>) mRefreshableView).setAdapter(adapter);
    }

    /**
     * Sets the Empty View to be used by the Adapter View.
     * <p/>
     * We need it handle it ourselves so that we can Pull-to-Refresh when the Empty View is shown.
     * <p/>
     * Please note, you do <strong>not</strong> usually need to call this method yourself. Calling
     * setEmptyView on the AdapterView will automatically call this method and set everything up.
     * This includes when the Android Framework automatically sets the Empty View based on it's ID.
     * 
     * @param newEmptyView - Empty View to be used
     */
    public final void setEmptyView(final View newEmptyView) {
        if (mEmptyView != null) {
            return;
        }
        setEmptyView(newEmptyView, false);
    }

    public final void setEmptyView(final View newEmptyView, boolean bDelay) {
        final FrameLayout refreshableViewWrapper = getRefreshableViewWrapper();
        if (null != newEmptyView) {
            // New view needs to be clickable so that Android recognizes it as a
            // target for Touch Events
            newEmptyView.setClickable(true);
            ViewParent newEmptyViewParent = newEmptyView.getParent();
            if (null != newEmptyViewParent && newEmptyViewParent instanceof ViewGroup) {
                ((ViewGroup) newEmptyViewParent).removeView(newEmptyView);
            }
            // We need to convert any LayoutParams so that it works in our
            // FrameLayout
            // if (bDelay) {
            // new Handler().postDelayed(new Runnable() {
            //
            // @Override
            // public void run() {
            // showEmptyView(newEmptyView, refreshableViewWrapper);
            // }
            // }, 5000);
            // } else {
            showEmptyView(newEmptyView, refreshableViewWrapper);
            // }

        }
        mEmptyView = newEmptyView;
    }

    private void showEmptyView(final View newEmptyView, final FrameLayout refreshableViewWrapper) {
        FrameLayout.LayoutParams lp = convertEmptyViewLayoutParams(newEmptyView.getLayoutParams());
        if (null != lp) {
            refreshableViewWrapper.addView(newEmptyView, lp);
        } else {
            refreshableViewWrapper.addView(newEmptyView);
        }

        if (mRefreshableView instanceof EmptyViewMethodAccessor) {
            ((EmptyViewMethodAccessor) mRefreshableView).setEmptyViewInternal(newEmptyView);
        } else {
            mRefreshableView.setEmptyView(newEmptyView);
        }
    }

    public void hideEmptyView() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    /**
     * Pass-through method for {@link PullToRefreshBase#getRefreshableView() getRefreshableView()}.
     * {@link AdapterView#setOnItemClickListener(OnItemClickListener)
     * setOnItemClickListener(listener)}. This is just for convenience!
     * 
     * @param listener - OnItemClickListener to use
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mRefreshableView.setOnItemClickListener(listener);
    }

    public final void setOnLastItemVisibleListener(OnLastItemVisibleListener listener) {
        mOnLastItemVisibleListener = listener;
    }

    public OnLastItemVisibleListener getmOnLastItemVisibleListener() {
        return mOnLastItemVisibleListener;
    }

    public final void setOnScrollListener(OnScrollListener listener) {
        mOnScrollListener = listener;
    }

    public final void setScrollEmptyView(boolean doScroll) {
        mScrollEmptyView = doScroll;
    }

    /**
     * Sets whether an indicator graphic should be displayed when the View is in a state where a
     * Pull-to-Refresh can happen. An example of this state is when the Adapter View is scrolled to
     * the top and the mode is set to {@link Mode#PULL_FROM_START}
     * 
     * @param showIndicator - true if the indicators should be shown.
     */
    public void setShowIndicator(boolean showIndicator) {
        mShowIndicator = showIndicator;

        if (getShowIndicatorInternal()) {
            // If we're set to Show Indicator, add/update them
            addIndicatorViews();
        } else {
            // If not, then remove then
            removeIndicatorViews();
        }
    }

    @Override
    protected void onPullToRefresh() {
        super.onPullToRefresh();

        if (getShowIndicatorInternal()) {
            switch (getCurrentMode()) {
                case PULL_FROM_END:
                    mIndicatorIvBottom.pullToRefresh();
                    break;
                case PULL_FROM_START:
                    mIndicatorIvTop.pullToRefresh();
                    break;
                default:
                    // NO-OP
                    break;
            }
        }
    }

    protected void onRefreshing(boolean doScroll) {
        super.onRefreshing(doScroll);

        if (getShowIndicatorInternal()) {
            updateIndicatorViewsVisibility();
        }
    }

    @Override
    protected void onReleaseToRefresh() {
        super.onReleaseToRefresh();

        if (getShowIndicatorInternal()) {
            switch (getCurrentMode()) {
                case PULL_FROM_END:
                    mIndicatorIvBottom.releaseToRefresh();
                    break;
                case PULL_FROM_START:
                    mIndicatorIvTop.releaseToRefresh();
                    break;
                default:
                    // NO-OP
                    break;
            }
        }
    }

    @Override
    protected void onReset() {
        super.onReset();

        if (getShowIndicatorInternal()) {
            updateIndicatorViewsVisibility();
        }
    }

    @Override
    protected void handleStyledAttributes(TypedArray a) {
        // Set Show Indicator to the XML value, or default value
        // 默认不显示箭头
        // mShowIndicator =
        // a.getBoolean(R.styleable.PullToRefresh_ptrShowIndicator,
        // !isPullToRefreshOverScrollEnabled());
    }

    protected boolean isReadyForPullStart() {
        return isFirstItemVisible();
    }

    protected boolean isReadyForPullEnd() {
        return isLastItemVisible();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null != mEmptyView && !mScrollEmptyView) {
            mEmptyView.scrollTo(-l, -t);
        }
    }

    @Override
    protected void updateUIForMode() {
        super.updateUIForMode();

        // Check Indicator Views consistent with new Mode
        if (getShowIndicatorInternal()) {
            addIndicatorViews();
        } else {
            removeIndicatorViews();
        }
    }

    @SuppressLint("RtlHardcoded")
    private void addIndicatorViews() {
        Mode mode = getMode();
        FrameLayout refreshableViewWrapper = getRefreshableViewWrapper();

        if (mode.showHeaderLoadingLayout() && null == mIndicatorIvTop) {
            // If the mode can pull down, and we don't have one set already
            mIndicatorIvTop = new IndicatorLayout(getContext(), Mode.PULL_FROM_START);
            FrameLayout.LayoutParams params =
                    new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            params.rightMargin =
                    getResources().getDimensionPixelSize(
                            MResource.getIdByName(BaseInit.ctx.getPackageName(), "dimen",
                                    "indicator_right_padding"));
            params.gravity = Gravity.TOP | Gravity.RIGHT;
            refreshableViewWrapper.addView(mIndicatorIvTop, params);

        } else if (!mode.showHeaderLoadingLayout() && null != mIndicatorIvTop) {
            // If we can't pull down, but have a View then remove it
            refreshableViewWrapper.removeView(mIndicatorIvTop);
            mIndicatorIvTop = null;
        }

        if (mode.showFooterLoadingLayout() && null == mIndicatorIvBottom) {
            // If the mode can pull down, and we don't have one set already
            mIndicatorIvBottom = new IndicatorLayout(getContext(), Mode.PULL_FROM_END);
            FrameLayout.LayoutParams params =
                    new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            params.rightMargin =
                    getResources().getDimensionPixelSize(
                            MResource.getIdByName(BaseInit.ctx.getPackageName(), "dimen",
                                    "indicator_right_padding"));
            params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
            refreshableViewWrapper.addView(mIndicatorIvBottom, params);

        } else if (!mode.showFooterLoadingLayout() && null != mIndicatorIvBottom) {
            // If we can't pull down, but have a View then remove it
            refreshableViewWrapper.removeView(mIndicatorIvBottom);
            mIndicatorIvBottom = null;
        }
    }

    private boolean getShowIndicatorInternal() {
        return mShowIndicator && isPullToRefreshEnabled();
    }

    private boolean isFirstItemVisible() {
        final Adapter adapter = mRefreshableView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            // if (L.D) {
            // L.d("isFirstItemVisible. Empty View.");
            // }
            return true;

        } else {

            /**
             * This check should really just be: mRefreshableView.getFirstVisiblePosition() == 0,
             * but PtRListView internally use a HeaderView which messes the positions up. For now
             * we'll just add one to account for it and rely on the inner condition which checks
             * getTop().
             */
            if (mRefreshableView.getFirstVisiblePosition() <= 1) {
                final View firstVisibleChild = mRefreshableView.getChildAt(0);
                if (firstVisibleChild != null) {
                    return firstVisibleChild.getTop() >= mRefreshableView.getTop();
                }
            }
        }

        return false;
    }

    private boolean isLastItemVisible() {
        final Adapter adapter = mRefreshableView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            if (L.D) {
                L.d("isLastItemVisible. Empty View.");
            }
            return true;
        } else {
            final int lastItemPosition = mRefreshableView.getCount() - 1;
            final int lastVisiblePosition = mRefreshableView.getLastVisiblePosition();

            if (L.D) {
                L.d("isLastItemVisible. Last Item Position: " + lastItemPosition
                        + " Last Visible Pos: " + lastVisiblePosition);
            }

            /**
             * This check should really just be: lastVisiblePosition == lastItemPosition, but
             * PtRListView internally uses a FooterView which messes the positions up. For me we'll
             * just subtract one to account for it and rely on the inner condition which checks
             * getBottom().
             */
            if (lastVisiblePosition >= lastItemPosition - 1) {
                final int childIndex =
                        lastVisiblePosition - mRefreshableView.getFirstVisiblePosition();
                final View lastVisibleChild = mRefreshableView.getChildAt(childIndex);
                if (lastVisibleChild != null) {
                    return lastVisibleChild.getBottom() <= mRefreshableView.getBottom();
                }
            }
        }

        return false;
    }

    private void removeIndicatorViews() {
        if (null != mIndicatorIvTop) {
            getRefreshableViewWrapper().removeView(mIndicatorIvTop);
            mIndicatorIvTop = null;
        }

        if (null != mIndicatorIvBottom) {
            getRefreshableViewWrapper().removeView(mIndicatorIvBottom);
            mIndicatorIvBottom = null;
        }
    }

    private void updateIndicatorViewsVisibility() {
        if (null != mIndicatorIvTop) {
            if (!isRefreshing() && isReadyForPullStart()) {
                if (!mIndicatorIvTop.isVisible()) {
                    mIndicatorIvTop.show();
                }
            } else {
                if (mIndicatorIvTop.isVisible()) {
                    mIndicatorIvTop.hide();
                }
            }
        }

        if (null != mIndicatorIvBottom) {
            if (!isRefreshing() && isReadyForPullEnd()) {
                if (!mIndicatorIvBottom.isVisible()) {
                    mIndicatorIvBottom.show();
                }
            } else {
                if (mIndicatorIvBottom.isVisible()) {
                    mIndicatorIvBottom.hide();
                }
            }
        }
    }
}
