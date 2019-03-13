package com.weqia.wq.component.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.weqia.wq.R;

/**
 * Created by 20161005 on 2017/6/27.
 */

public class RoundImageView extends AppCompatImageView {
    //圆角所对应圆的半径
    private int mBorderRadius = 12;
    // true代表绘制成弧形
    private boolean cornerTopLeft;
    private boolean cornerTopRight;
    private boolean cornerBottomLeft;
    private boolean cornerBottomRight;

    //当前控件的宽高
    private float width;
    private float hight;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundImage);
        cornerTopLeft = array.getBoolean(R.styleable.RoundImage_cornerTopLeft, false);
        cornerTopRight = array.getBoolean(R.styleable.RoundImage_cornerTopRight, false);
        cornerBottomLeft = array.getBoolean(R.styleable.RoundImage_cornerBottomLeft, false);
        cornerBottomRight = array.getBoolean(R.styleable.RoundImage_cornerBottomRight, false);
        mBorderRadius = array.getInteger(R.styleable.RoundImage_borderRadius, 12);
        mBorderRadius = Math.abs(mBorderRadius);
        array.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //获取当前控件的宽高
        width = getWidth();
        hight = getHeight();
    }

    //剪切成一个弧形最少需要三个点：起点，控制点，终点

    @Override
    protected void onDraw(Canvas canvas) {
        //如果不需要圆角或者宽高小于圆角的大小，则不进行剪切
        if ((cornerTopLeft || cornerTopRight || cornerBottomLeft || cornerBottomRight) && width > mBorderRadius && hight > mBorderRadius) {
            Path path = new Path();
            //将画笔移动到初始坐标点，也是起点
            if (cornerTopLeft) {
                path.moveTo(mBorderRadius, 0);
            } else {
                path.moveTo(0, 0);
            }
            //根据判断，配置是否绘制当前角和起点
            if (cornerTopRight) {
                //连接初始坐标点和指定坐标（width-12,0）为起点
                path.lineTo(width - mBorderRadius, 0);
                //绘制贝尔曲线； (x1,y1) 为控制点，(x2,y2)为结束点
                path.quadTo(width, 0, width, mBorderRadius);
            } else {
                path.lineTo(width, 0);
            }

            if (cornerBottomRight) {
                path.lineTo(width, hight - mBorderRadius);
                path.quadTo(width, hight, width - mBorderRadius, hight);
            } else {
                path.lineTo(width, hight);
            }

            if (cornerBottomLeft) {
                path.lineTo(mBorderRadius, hight);
                path.quadTo(0, hight, 0, hight - mBorderRadius);
            } else {
                path.lineTo(0, hight);
            }

            if (cornerTopLeft) {
                path.lineTo(0, mBorderRadius);
                path.quadTo(0, 0, mBorderRadius, 0);
            } else {
                path.lineTo(0, 0);
            }
            //最后根据以上绘制的路径（path）剪切画布（canvas）
            canvas.clipPath(path);
        }
        super.onDraw(canvas);
    }
}
