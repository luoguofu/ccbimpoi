package com.weqia.wq.component.adapterhelper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by jmx on 11/25 0025.
 */
public abstract class BaseHeadAdapter<T, H extends BaseAdapterHelper> extends LinearLayout {
    Context context;
    int layoutResId;
    T data;

    public BaseHeadAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }


    public BaseHeadAdapter(Context context, int layoutResId, T data) {
        super(context);
        this.context = context;
        this.layoutResId = layoutResId;
        this.data = data;
        H helper = getAdapterHelper(0, null, this);
        bindingData(helper, data);
        addView(helper.getView());
    }


    public void setItems(T data) {
        this.data = data;
        invalidate();
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public abstract H getAdapterHelper(int position, View convertView, ViewGroup parent);

    public abstract void bindingData(H helper, T item);

}
