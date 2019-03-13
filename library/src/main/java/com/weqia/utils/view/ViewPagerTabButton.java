package com.weqia.utils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;

import com.weqia.BaseInit;
import com.weqia.utils.MResource;

public class ViewPagerTabButton extends Button {

    private int mLineColor = 0x55ad18;
    private int mLineColorSelected = 0x55ad18;

    private int mLineHeight = 0;
    private int mLineHeightSelected = 3;

    private int mTextColor = 0x001231;
    private int mTextSelectColor = 0x000000;

    public ViewPagerTabButton(Context context) {
        this(context, null);
    }

    public ViewPagerTabButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerTabButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mLineHeight =
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mLineHeight, context
                        .getResources().getDisplayMetrics());
        mLineHeightSelected =
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mLineHeightSelected,
                        context.getResources().getDisplayMetrics());

        final TypedArray a =
                context.obtainStyledAttributes(attrs, MResource.getIdsByName(
                        BaseInit.ctx.getPackageName(), "styleable", "ViewPagerAttr"), defStyle, 0);

        mLineColor =
                a.getColor(MResource.getIdByName(BaseInit.ctx.getPackageName(), "styleable",
                        "ViewPagerAttr_lineColor"), mLineColor);
        mLineColorSelected =
                a.getColor(MResource.getIdByName(BaseInit.ctx.getPackageName(), "styleable",
                        "ViewPagerAttr_lineColorSelected"), mLineColorSelected);

        mTextSelectColor =
                a.getColor(MResource.getIdByName(BaseInit.ctx.getPackageName(), "styleable",
                        "ViewPagerAttr_textColorSelected"), mTextSelectColor);
        mTextColor =
                a.getColor(MResource.getIdByName(BaseInit.ctx.getPackageName(), "styleable",
                        "ViewPagerAttr_textColor"), mTextColor);

        mLineHeight =
                a.getDimensionPixelSize(MResource.getIdByName(BaseInit.ctx.getPackageName(),
                        "styleable", "ViewPagerAttr_lineHeight"), mLineHeight);
        mLineHeightSelected =
                a.getDimensionPixelSize(MResource.getIdByName(BaseInit.ctx.getPackageName(),
                        "styleable", "ViewPagerAttr_lineHeightSelected"), mLineHeightSelected);

        a.recycle();

    }

    private Paint mLinePaint = new Paint();

    protected synchronized void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        final Paint linePaint = mLinePaint;

        linePaint.setColor(isSelected() ? mLineColorSelected : mLineColor);

        final int height = isSelected() ? mLineHeightSelected : mLineHeight;

        // draw the line
        canvas.drawRect(0, getMeasuredHeight() - height, getMeasuredWidth(), getMeasuredHeight(),
                linePaint);

        this.setTextColor(this.isSelected() ? mTextSelectColor : mTextColor);

    }

    public void setLineColorSelected(int color) {
        this.mLineColorSelected = color;
        invalidate();
    }

    public int getLineColorSelected() {
        return this.mLineColorSelected;
    }

    public void setLineColor(int color) {
        this.mLineColor = color;
        invalidate();
    }

    public int getLineColor() {
        return this.mLineColor;
    }

    public void setLineHeight(int height) {
        this.mLineHeight = height;
        invalidate();
    }

    public int getLineHeight() {
        return this.mLineHeight;
    }

    public void setLineHeightSelected(int height) {
        this.mLineHeightSelected = height;
        invalidate();
    }

    public int getLineHeightSelected() {
        return this.mLineHeightSelected;
    }

}
