package com.example.ccbim.ccbimpoi.widget;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ccbim.ccbimpoi.R;
import com.example.ccbim.ccbimpoi.data.FormListItemBean;
import java.util.List;

class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    List<FormListItemBean> moduleItems;
    private OnItemClickListener mOnItemClickListener;

    public RecycleViewAdapter(List<FormListItemBean> moduleItems) {
        this.moduleItems = moduleItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_form_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
//        viewHolder.title.setText(moduleItems.get(i).getTitle());
//        if(!TextUtils.isEmpty(moduleItems.get(i).getDrawableName())) {
//            int id = viewHolder.imageView.getResources().getIdentifier(moduleItems.get(i).getDrawableName(), "drawable", viewHolder.imageView.getContext().getPackageName());
//            if(id>0)
//                viewHolder.imageView.setImageResource(id);
//        }
//        if (mOnItemClickListener != null){
//            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnItemClickListener.onItemClick(v,i);
//                }
//            });
//        }
    }

    public OnItemClickListener getmOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    interface OnItemClickListener {
        void onItemClick(View view, int position);

        boolean onItemLongClick(View view, int position);
    }

    @Override
    public int getItemCount() {
        return moduleItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.icon);
        }
    }
}



