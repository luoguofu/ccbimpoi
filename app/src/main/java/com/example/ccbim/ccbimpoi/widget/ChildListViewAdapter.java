package com.example.ccbim.ccbimpoi.widget;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ccbim.ccbimpoi.R;
import com.example.ccbim.ccbimpoi.activity.SingleFormActivity;
import com.example.ccbim.ccbimpoi.data.ChildItemBean;
import java.util.List;

public class ChildListViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<ChildItemBean> data;

    public ChildListViewAdapter(Context context, List<ChildItemBean> lists) {
        mContext=context;
        data=lists;
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
        ViewHolder vh=null;
        ChildItemBean childItemBean=data.get(position);
        if(convertView==null){
            convertView=LayoutInflater.from(mContext).inflate(R.layout.child_form_item_layout, null);
            vh=new ViewHolder();
            vh.child_item_title=(TextView)convertView.findViewById(R.id.child_item_title);
            vh.child_item_status=(TextView)convertView.findViewById(R.id.child_item_status);
            vh.child_item_btn=(Button)convertView.findViewById(R.id.child_item_btn);
            convertView.setTag(vh);
        }else{
            vh=(ViewHolder)convertView.getTag();
        }
        vh.child_item_title.setText(childItemBean.getTitle());
        vh.child_item_status.setText(childItemBean.getStatus()+"");
        vh.child_item_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击!!!"+childItemBean.getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext,SingleFormActivity.class);
                mContext.startActivity(intent);

            }
        });
        return convertView;
    }

    class ViewHolder{
        TextView child_item_title;
        TextView child_item_status;
        Button child_item_btn;
    }

}
