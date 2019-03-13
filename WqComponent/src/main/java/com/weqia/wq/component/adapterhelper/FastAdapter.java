package com.weqia.wq.component.adapterhelper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import static com.weqia.wq.component.adapterhelper.BaseAdapterHelper.get;

/**
 * Created by jmx on 11/25 0025.
 */
public abstract class FastAdapter<T> extends BaseFastAdapter<T, BaseAdapterHelper> {
    public FastAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    public FastAdapter(Context context, int layoutResId, List<T> data) {
        super(context, layoutResId, data);
    }

    public BaseAdapterHelper getAdapterHelper(int position, View convertView, ViewGroup parent) {
        return get(context, convertView, parent, layoutResId, position);
    }


}
