package com.weqia.wq.component.activity.assist;

import android.widget.BaseAdapter;

import com.weqia.wq.component.activity.SharedTitleActivity;

import java.util.List;

public abstract class SharedSearchAdapter<T> extends BaseAdapter {

    protected List<T> items;

    public SharedSearchAdapter(SharedTitleActivity ctx) {
        super();
    }

    public void setItems(List<T> items) {
        this.items = items;
        notifyDataSetChanged();
        //notifyDataSetChanged方法通过一个外部的方法控制如果适配器的内容改变时需要强制调用getView来刷新每个Item的内容,
        //可以实现动态的刷新列表的功能。
    }
    
    public List<T> getItems() {
        return items;
    }

    @Override
    public int getCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (items != null) {
            return items.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    protected abstract void dataAdapter(int position, SharedSearchHolder holder);

}
