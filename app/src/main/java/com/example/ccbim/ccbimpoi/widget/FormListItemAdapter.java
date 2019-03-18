package com.example.ccbim.ccbimpoi.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.ccbim.ccbimpoi.R;
import com.example.ccbim.ccbimpoi.activity.FormListActivity;
import com.example.ccbim.ccbimpoi.data.CellData;
import com.example.ccbim.ccbimpoi.data.FormItemDataBean;
import java.util.List;

public class FormListItemAdapter extends BaseAdapter {

    public FormListActivity mContext;
    public List<CellData> data;

    public FormListItemAdapter(FormListActivity context, List<CellData> lists){
        mContext = context;
        data = lists;
    }

    @Override
    public int getCount() {
        return data !=null ? data.size():0;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        CellData formItemDataBean = data.get(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.form_item_view_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.child_item_title=(TextView)convertView.findViewById(R.id.child_item_title);
            viewHolder.childListView = (ChildListView) convertView.findViewById(R.id.list_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.child_item_title.setText(formItemDataBean.getCellName());
        ChildListViewAdapter adapter = new ChildListViewAdapter(mContext, formItemDataBean.getSubCellList(), position);
        viewHolder.childListView.setAdapter(adapter);
        return convertView;
    }

    class ViewHolder{
        TextView child_item_title;
        ChildListView childListView;
    }

}
