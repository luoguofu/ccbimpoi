package com.weqia.utils.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.weqia.BaseInit;
import com.weqia.utils.MResource;
import com.weqia.utils.TabsAdapter;

public class FixedTabsView extends LinearLayout {

    private Context mContext;
    private ViewPager mPager;
    private TabsAdapter mAdapter;

    private ArrayList<View> mTabs = new ArrayList<View>();
    private int mDividerResource;

    private int mDividerColor = 0xFF636363;
    private int mDividerMarginTop = 0;
    private int mDividerMarginBottom = 0;

    public FixedTabsView(Context context) {
        this(context, null);
    }

    public FixedTabsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixedTabsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

        this.mContext = context;
        final TypedArray a =
                context.obtainStyledAttributes(attrs, MResource.getIdsByName(
                        BaseInit.ctx.getPackageName(), "styleable", "ViewPagerAttr"), defStyle, 0);
        mDividerColor =
                a.getColor(MResource.getIdByName(BaseInit.ctx.getPackageName(), "styleable",
                        "ViewPagerAttr_dividerColor"), mDividerColor);

        mDividerMarginTop =
                a.getDimensionPixelSize(MResource.getIdByName(BaseInit.ctx.getPackageName(),
                        "styleable", "ViewPagerAttr_dividerMarginTop"), mDividerMarginTop);
        mDividerMarginBottom =
                a.getDimensionPixelSize(MResource.getIdByName(BaseInit.ctx.getPackageName(),
                        "styleable", "ViewPagerAttr_dividerMarginBottom"), mDividerMarginBottom);

        mDividerResource =
                a.getResourceId(MResource.getIdByName(BaseInit.ctx.getPackageName(), "styleable",
                        "ViewPagerAttr_dividerDrawable"), 0);
        a.recycle();
        this.setOrientation(LinearLayout.HORIZONTAL);
    }

    /**
     * Sets the data behind this FixedTabsView.
     * 
     * @param adapter The {@link TabsAdapter} which is responsible for maintaining the data backing
     *        this FixedTabsView and for producing a view to represent an item in that data set.
     */
    public void setAdapter(TabsAdapter adapter) {
        this.mAdapter = adapter;

        if (mPager != null && mAdapter != null) initTabs();
    }

    /**
     * Binds the {@link ViewPager} to this View
     * 
     */
    public void setViewPager(ViewPager pager) {
        this.mPager = pager;
        // mPager.setOnPageChangeListener(this);
        // mPager.setOnPageChangeListener(new OnPageChangeListener() {
        //
        // @Override
        // public void onPageSelected(int position) {
        //
        // selectTab(position);
        // }
        //
        // @Override
        // public void onPageScrolled(int arg0, float arg1, int arg2) {
        //
        //
        // }
        //
        // @Override
        // public void onPageScrollStateChanged(int arg0) {
        //
        //
        // }
        // });
        if (mPager != null && mAdapter != null) initTabs();
    }

    /**
     * Initialize and add all tabs to the layout
     */
    private void initTabs() {

        removeAllViews();
        mTabs.clear();

        if (mAdapter == null || mPager == null) return;

        for (int i = 0; i < mPager.getAdapter().getCount(); i++) {

            final int index = i;

            View tab = mAdapter.getView(i);
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
            tab.setLayoutParams(params);
            this.addView(tab);

            mTabs.add(tab);

            if (i != mPager.getAdapter().getCount() - 1) {
                this.addView(getSeparator());
            }

            tab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPager.setCurrentItem(index);
                }
            });

        }

        selectTab(mPager.getCurrentItem());
    }

    // @Override
    // public void onPageScrollStateChanged(int state) {
    //
    //
    //
    // }
    //
    // @Override
    // public void onPageScrolled(int position, float positionOffset,
    // int positionOffsetPixels) {
    // }
    //
    // @Override
    // public void onPageSelected(int position) {
    // selectTab(position);
    // }

    /**
     * Creates and returns a new Separator View
     * 
     * @return
     */
    private View getSeparator() {
        View v = new View(mContext);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(1, LayoutParams.MATCH_PARENT);
        params.setMargins(0, mDividerMarginTop, 0, mDividerMarginBottom);
        v.setLayoutParams(params);
        if (mDividerResource != 0) {
            v.setBackgroundResource(mDividerResource);
        } else {
            v.setBackgroundColor(mDividerColor);
        }

        return v;
    }

    /**
     * Runs through all tabs and sets if they are currently selected.
     * 
     * @param position The position of the currently selected tab.
     */
    public void selectTab(int position) {

        for (int i = 0, pos = 0; i < getChildCount(); i++) {

            if (this.getChildAt(i) instanceof ViewPagerTabButton) {
                this.getChildAt(i).setSelected(pos == position);
                pos++;
            }

        }
    }

    public void updateTabs(ArrayList<Integer> datas) {
        if (datas == null || datas.size() == 0) {
            return;
        }
        // for (int i = 0; i < getChildCount(); i++) {
        //
        // if (this.getChildAt(i) instanceof ViewPagerTabButton) {
        // ViewPagerTabButton button = (ViewPagerTabButton) this.getChildAt(i);
        // if (datas.size() > i) {
        // button.setText(button.getText() + "(" + datas.get(i) + ")");
        // }
        // }
        // }
    }
}
