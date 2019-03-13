package com.weqia.wq.component.adapterhelper;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by jmx on 11/25 0025.
 */
public class BaseAdapterHelper {
    Context context;
    int position;
    View convertView;
    SparseArray<View> views;

    public BaseAdapterHelper(Context context, ViewGroup viewGroup, int layoutId, int position) {
        this.context = context;
        this.position = position;
        this.views = new SparseArray<View>();
        if (viewGroup != null) {
            this.convertView = LayoutInflater.from(context).inflate(layoutId, viewGroup, false);
        } else {
            this.convertView = LayoutInflater.from(context).inflate(layoutId, null);
        }
        this.convertView.setTag(this);
    }


    public static BaseAdapterHelper get(Context context, View convertView, ViewGroup parent, int layoutId) {
        return get(context, convertView, parent, layoutId, -1);
    }


    static BaseAdapterHelper get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new BaseAdapterHelper(context, parent, layoutId, position);
        }
        BaseAdapterHelper existingHelper = (BaseAdapterHelper) convertView.getTag();
        existingHelper.position = position;
        return existingHelper;
    }

    public <T extends View> T getView(int viewId) {
        return findViewById(viewId);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T findViewById(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = convertView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }


    public View getView() {
        return convertView;
    }
}
