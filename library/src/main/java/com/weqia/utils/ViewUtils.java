package com.weqia.utils;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.SpannableString;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Description: ViewUtils.java Create on 2013-3-26 下午1:57:00
 * 
 * @author Bewin berwinzheng@gmail.com
 * @version 1.0 Copyright (c) 2013 Company,Inc. All Rights Reserved.
 */
public class ViewUtils {

    /**
     * @param activity
     * @param onClickListen
     * @param ids void
     * @throws
     * @Title: bindClickListenerOnViews
     */
    public static void bindClickListenerOnViews(Activity activity, OnClickListener onClickListen,
            int... ids) {
        int i = ids.length;
        for (int j = 0;; ++j) {
            if (j >= i) return;
            View view = activity.findViewById(ids[j]);
            if (view == null) continue;
            view.setOnClickListener(onClickListen);
        }
        // throw new IllegalArgumentException("A view id not found in current activity.");
    }


    public static void bindLongClickListenerOnViews(Activity activity,
            OnLongClickListener onClickListen, int... ids) {
        int i = ids.length;
        for (int j = 0;; ++j) {
            if (j >= i) return;
            View view = activity.findViewById(ids[j]);
            if (view == null) continue;
            view.setOnLongClickListener(onClickListen);
        }
        // throw new IllegalArgumentException("A view id not found in current activity.");
    }

    /**
     * @param root
     * @param onClickListen
     * @param ids
     * @Description
     */
    public static void bindClickListenerOnViews(View root, OnClickListener onClickListen,
            int... ids) {
        int i = ids.length;
        for (int j = 0;; ++j) {
            if (j >= i) return;
            View view = root.findViewById(ids[j]);
            if (view == null) continue;
            view.setOnClickListener(onClickListen);
        }
        // throw new IllegalArgumentException("A view id not found in current activity.");
    }

    /**
     * @param onClickListen
     * @param views void
     * @throws
     * @Title: bindClickListenerOnViews
     */
    public static void bindClickListenerOnViews(OnClickListener onClickListen, View... views) {
        int i = views.length;
        for (int j = 0;; ++j) {
            if (j >= i) return;
            View view = views[j];
            if (view == null) continue;
            view.setOnClickListener(onClickListen);
        }
        // throw new IllegalArgumentException("A view id not found in current activity.");
    }

    public static void bindCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener,
            CheckBox... boxs) {
        int i = boxs.length;
        for (int j = 0;; ++j) {
            if (j >= i) return;
            CheckBox view = boxs[j];
            if (view == null) continue;
            view.setOnCheckedChangeListener(onCheckedChangeListener);
        }
    }


    public static void enableIds(Activity activity, int... ids) {
        int i = ids.length;
        if (activity == null) {
            return;
        }
        for (int j = 0;; ++j) {
            if (j >= i) return;
            View view = activity.findViewById(ids[j]);
            if (view == null) continue;
            view.setEnabled(true);
        }
        // throw new IllegalArgumentException("A view id not found in current activity.");
    }

    public static void enableIds(View rootView, int... ids) {
        int i = ids.length;
        if (rootView == null) {
            return;
        }
        for (int j = 0;; ++j) {
            if (j >= i) return;
            View view = rootView.findViewById(ids[j]);
            if (view == null) continue;
            view.setEnabled(true);
        }
        // throw new IllegalArgumentException("A view id not found in current activity.");
    }

    public static void enableIds(View... views) {
        int i = views.length;
        for (int j = 0;; ++j) {
            if (j >= i) return;
            View view = views[j];
            if (view == null) continue;
            view.setEnabled(true);
        }
        // throw new IllegalArgumentException("A view id not found in current activity.");
    }

    public static void enableId(View view) {
        if (view != null) view.setEnabled(true);
        // throw new IllegalArgumentException("A view id not found in current activity.");
    }

    public static void disableIds(Activity activity, int... ids) {
        int i = ids.length;
        for (int j = 0;; ++j) {
            if (j >= i) return;
            View view = activity.findViewById(ids[j]);
            if (view == null) {
                L.e("界面为空");
                continue;
            }
            view.setEnabled(false);
        }
        // throw new IllegalArgumentException("A view id not found in current activity.");
    }

    public static void disableIds(View rootView, int... ids) {
        int i = ids.length;
        for (int j = 0;; ++j) {
            if (j >= i) return;
            View view = rootView.findViewById(ids[j]);
            if (view == null) continue;
            view.setEnabled(false);
        }
        // throw new IllegalArgumentException("A view id not found in current activity.");
    }

    public static void disableIds(View... views) {
        int i = views.length;
        for (int j = 0;; ++j) {
            if (j >= i) return;
            View view = views[j];
            if (view == null) continue;
            view.setEnabled(false);
        }
        // throw new IllegalArgumentException("A view id not found in current activity.");
    }
    
    public static void disableId(View view) {
        if (view != null) view.setEnabled(false);
        // throw new IllegalArgumentException("A view id not found in current activity.");
    }

    /**
     * @param tvs void
     * @throws
     * @Title: clearTextViews
     */
    public static void clearTextViews(TextView... tvs) {
        int i = tvs.length;
        for (int j = 0;; ++j) {
            if (j >= i) return;
            tvs[j].setText("");
        }
    }

    /**
     * @param view void
     * @throws
     * @Title: hideView
     */
    public static void hideView(View view) {
        if (view != null) {
            view.setVisibility(View.GONE);
            return;
        }
        // throw new IllegalArgumentException("A view is null in your parameter.");
    }

    /**
     * @param activity
     * @param ids void
     * @throws
     * @Title: hideViews
     */
    public static void hideViews(Activity activity, int... ids) {
        int i = ids.length;
        for (int j = 0;; ++j) {
            if (j >= i) return;
            View view = activity.findViewById(ids[j]);
            if (view == null) continue;
            view.setVisibility(View.GONE);
        }
        // throw new IllegalArgumentException("A view id not found in current activity.");
    }

    public static void hideViews(View rootView, int... ids) {
        int i = ids.length;
        if (rootView == null) {
            return;
        }
        for (int j = 0;; ++j) {
            if (j >= i) return;
            View view = rootView.findViewById(ids[j]);
            if (view == null) continue;
            view.setVisibility(View.GONE);
        }
        // throw new IllegalArgumentException("A view id not found in current activity.");
    }

    /**
     * @param views void
     * @throws
     * @Title: hideViews
     */
    public static void hideViews(View... views) {
        int i = views.length;
        for (int j = 0;; ++j) {
            if (j >= i) return;
            View localView = views[j];
            if (localView == null) continue;
            if (localView.getVisibility() == View.GONE) continue;
            localView.setVisibility(View.GONE);
        }
        // throw new IllegalArgumentException("A view is null in your parameters.");
    }

    public static void invisibleViews(Activity activity, int... ids) {
        int i = ids.length;
        for (int j = 0;; ++j) {
            if (j >= i) return;
            View view = activity.findViewById(ids[j]);
            if (view == null) continue;
            view.setVisibility(View.INVISIBLE);
        }
        // throw new IllegalArgumentException("A view id not found in current activity.");
    }

    /**
     * @param iv
     * @param drawable void
     * @throws
     * @Title: setDrawable
     */
    public static void setDrawable(ImageView iv, Drawable drawable) {
        if ((iv == null) || (drawable == null)) return;
        iv.setImageDrawable(drawable);
    }

    public static void setImageRes(ImageView iv, Integer rId) {
        if ((iv == null) || (rId == null)) return;
        iv.setImageResource(rId);
    }

    public static void setBgRes(View iv, Integer rId) {
        if ((iv == null) || (rId == null)) return;
        iv.setBackgroundResource(rId);
    }

    /**
     * @param view void
     * @throws
     * @Title: setViewInvisible
     */
    public static void setViewInvisible(View view) {
        if (view != null) {
            view.setVisibility(View.INVISIBLE);
            return;
        }
        // throw new IllegalArgumentException("A view is null in your parameter.");
    }


    /**
     * @param view void
     * @throws
     * @Title: showView
     */
    public static void showView(View view) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
            return;
        }
        // throw new IllegalArgumentException("A view is null in your parameters.");
    }

    /**
     * @param activity
     * @param ids void
     * @throws
     * @Title: showViews
     */
    public static void showViews(Activity activity, int... ids) {
        int i = ids.length;
        if (activity == null) {
            return;
        }
        for (int j = 0;; ++j) {
            if (j >= i) return;
            View view = activity.findViewById(ids[j]);
            if (view == null) continue;
            view.setVisibility(View.VISIBLE);
        }
        // throw new IllegalArgumentException("A view id not found in current activity.");
    }

    public static void showViews(View rootView, int... ids) {
        int i = ids.length;
        if (rootView == null) {
            return;
        }
        for (int j = 0;; ++j) {
            if (j >= i) return;
            View view = rootView.findViewById(ids[j]);
            if (view == null) continue;
            view.setVisibility(View.VISIBLE);
        }
        // throw new IllegalArgumentException("A view id not found in current activity.");
    }

    /**
     * @param views void
     * @throws
     * @Title: showViews
     */
    public static void showViews(View... views) {
        int i = views.length;
        for (int j = 0;; ++j) {
            if (j >= i) return;
            View localView = views[j];
            if (localView == null) continue;
            localView.setVisibility(View.VISIBLE);
        }
        // throw new IllegalArgumentException("A view id not found in current activity.");
    }

    /**
     * @param activity
     * @param id
     * @param text
     * @Description
     */
    public static void setTextView(Activity activity, int id, String text) {
        if (activity != null) {
            TextView tv = (TextView) activity.findViewById(id);
            setTextView(tv, text);
        }
    }

    /**
     * @param activity
     * @param id
     * @param text
     * @Description
     */
    public static void setTextView(Activity activity, int id, SpannableString text) {
        if (activity != null) {
            TextView tv = (TextView) activity.findViewById(id);
            if (tv != null && text != null) {
                tv.setText(text);
            }
        }
    }

    public static void setTextViewFromHtml(Activity activity, int id, String text) {
        if (activity != null) {
            TextView tv = (TextView) activity.findViewById(id);
            if (tv != null && text != null) {
                tv.setText(Html.fromHtml(text));
            }
        }
    }

    /**
     * @param rootView
     * @param id
     * @param text
     * @Description
     */
    public static void setTextView(View rootView, int id, String text) {
        if (rootView != null) {
            TextView tv = (TextView) rootView.findViewById(id);
            setTextView(tv, text);
        }
    }

    /**
     * @param tv
     * @param text
     * @Description
     */
    public static void setTextView(TextView tv, String text) {
        if (tv != null && text != null) {
            tv.setText(text);
        }
    }

    /**
     * @param activity
     * @param id
     * @param text
     * @Description
     */
    public static String getTextView(Activity activity, int id) {
        if (activity != null) {
            TextView tv = (TextView) activity.findViewById(id);
            return getTextView(tv);
        } else {
            return "";
        }
    }

    /**
     * @param activity
     * @param id
     * @param text
     * @Description
     */
    public static String getTextView(View rootView, int id) {
        if (rootView != null) {
            TextView tv = (TextView) rootView.findViewById(id);
            return getTextView(tv);
        } else {
            return "";
        }
    }


    /**
     * @param tv
     * @param text
     * @Description
     */
    public static String getTextView(TextView tv) {
        if (tv != null) {
            return tv.getText().toString();
        } else {
            return "";
        }
    }

    public static float getTextLength(final float textSize, String text) {
        Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // Define the string.
        mTextPaint.setTextSize(textSize);
        float textWidth = mTextPaint.measureText(text);
        return textWidth;
    }

    public static int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent) + 2;
    }

}
