package com.weqia.wq.component.adapterhelper;

import android.content.Context;
import android.widget.LinearLayout;

import static com.weqia.wq.component.adapterhelper.BaseAdapterHelper.get;

/**
 * Created by jmx on 11/27 0027.
 */
public abstract class HeadViewHelper<T> extends LinearLayout {

    T data;
    Context context;
    BaseAdapterHelper helper;

    public void setItem(T data) {
        this.data = data;
        bindingData(helper, data);
    }


    public HeadViewHelper(Context context, int layoutResId) {
        super(context);
        this.context = context;
        helper = getAdapterHelper(layoutResId);
        addView(helper.getView(), new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
    }

    public BaseAdapterHelper getAdapterHelper(int layoutResId) {
        return get(context, null, null, layoutResId, 0);
    }

    public abstract void bindingData(BaseAdapterHelper helper, T item);
}
