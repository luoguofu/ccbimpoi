package com.weqia.wq.component.adapterhelper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmx on 11/25 0025.
 */
public abstract class BaseFastAdapter<T, H extends BaseAdapterHelper> extends BaseAdapter {
    Context context;
    int layoutResId;
    List<T> data;


    public BaseFastAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    public BaseFastAdapter(Context context, int layoutResId, List<T> data) {
        this.context = context;
        this.layoutResId = layoutResId;
        this.data = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
    }

    public void setItems(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    @Override
    public T getItem(int position) {
        if (data != null) {
            return data.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (data != null) {
            return position;
        } else {
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        H helper = getAdapterHelper(position, convertView, parent);
        T item = getItem(position);
        bindingData(helper, item, position);
        return helper.getView();
    }


    public abstract H getAdapterHelper(int position, View convertView, ViewGroup parent);

    public abstract void bindingData(H helper, T item, int position);


}
