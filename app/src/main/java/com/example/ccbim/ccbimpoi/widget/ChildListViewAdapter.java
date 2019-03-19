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
import com.example.ccbim.ccbimpoi.activity.FormListActivity;
import com.example.ccbim.ccbimpoi.activity.SingleFormActivity;
import com.example.ccbim.ccbimpoi.data.CellData;
import com.example.ccbim.ccbimpoi.data.CheckDetailData;
import com.example.ccbim.ccbimpoi.data.ChildItemBean;
import com.example.ccbim.ccbimpoi.util.ConstantUtil;

import java.util.List;

public class ChildListViewAdapter extends BaseAdapter {

    private FormListActivity mContext;
    private List<CheckDetailData> data;
    private int parentPos;

    public ChildListViewAdapter(FormListActivity context, List<CheckDetailData> lists,int parentPos) {
        mContext=context;
        data=lists;
        this.parentPos = parentPos;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder vh=null;
        final CheckDetailData childItemBean=data.get(position);
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
        vh.child_item_title.setText(childItemBean.getCheckName().getCellName());
        if (childItemBean.getStatus() == 0) {
            vh.child_item_status.setText("");
        }else if (childItemBean.getStatus() == 1||childItemBean.getStatus() == 2) {
            vh.child_item_status.setText("√");
        }else if (childItemBean.getStatus() == 3) {
            vh.child_item_status.setText("X");
        }

        vh.child_item_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "点击!!!" + childItemBean.getCheckName().getCellName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext,SingleFormActivity.class);
                intent.putExtra("parentPos", parentPos);
                intent.putExtra("childPos", position);
                intent.putExtra(ConstantUtil.PROJECTEXTRA, mContext.getProjectCheckData());
                intent.putExtra("childData", childItemBean);
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
