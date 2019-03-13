package com.weqia.wq.component.adapterhelper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import static com.weqia.wq.component.adapterhelper.BaseAdapterHelper.get;

/**
 * Created by jmx on 11/25 0025.
 */
@SuppressWarnings("rawtypes")
public abstract class HeadViewAdapter<T> extends BaseHeadAdapter {
    public HeadViewAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    @SuppressWarnings("unchecked")
    public HeadViewAdapter(Context context, int layoutResId, T data) {
        super(context, layoutResId, data);
    }

    public BaseAdapterHelper getAdapterHelper(int position, View convertView, ViewGroup parent) {
        return get(context, convertView, parent, layoutResId, position);
    }

}
