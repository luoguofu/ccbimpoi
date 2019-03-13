package com.weqia.wq.component.view.expandlist;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.weqia.wq.data.BaseData;

public abstract class DragNDropAdapter extends BaseExpandableListAdapter implements RemoveListener, DropListener {

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<String> groups;
	private Map<Integer, List<? extends BaseData>> children;

	public LayoutInflater getmInflater() {
		return mInflater;
	}

	public void setmInflater(LayoutInflater mInflater) {
		this.mInflater = mInflater;
	}

	public ArrayList<String> getGroups() {
		return groups;
	}

	public void setGroups(ArrayList<String> groups) {
		this.groups = groups;
	}

	public Map<Integer, List<? extends BaseData>> getChildren() {
		return children;
	}

	public void setChildren(Map<Integer, List<? extends BaseData>> children) {
		this.children = children;
	}

	public DragNDropAdapter(Context context, ArrayList<String> group, Map<Integer, List<? extends BaseData>> child) {
		init(context, group, child);
	}

	private void init(Context context, ArrayList<String> group, Map<Integer, List<? extends BaseData>> child) {
		mInflater = LayoutInflater.from(context);
		groups = group;
		mContext = context;
		children = child;
	}

	public void onRemove(int which[]) {
//		if (which[0] < 0 || which[0] > groups.size() || which[1] < 0 || which[1] > getChildrenCount(which[0]))
//			return;
//		children.get(which[0]).remove(getKey(which));
	}

	public void onDrop(int[] from, int[] to) {
		
	}

//	private TaskData getValue(int[] id) {
//		return null;
////		return (TaskData) children.get(id[0]).values().toArray()[id[1]];
//	}
//
//	private Integer getKey(int[] id) {
////		return (String) children.get(id[0]).get[id[1]];
//		return null;
//	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		List<? extends BaseData> childTaskDatas = children.get(groupPosition);
		if (childTaskDatas != null) {
			return childTaskDatas.get(childPosition);
		}
		
		return null;
//		return children.get(groupPosition).values().toArray()[childPosition];
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (children.get(groupPosition) == null) {
			return 0;
		} else {
			return children.get(groupPosition).size();
		}
	}

	@Override
	public Object getGroup(int groupPosition) {
		if (groups == null) {
			return null;
		} else {
			return groups.get(groupPosition);
		}
	}

	@Override
	public int getGroupCount() {
		if (groups == null) {
			return 0;
		} else {
			return groups.size();
		}
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public TextView getGenericView() {
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		TextView textView = new TextView(mContext);
		textView.setLayoutParams(lp);
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		textView.setPadding(36, 0, 0, 0);
		return textView;
	}

	// /**-------------------其他设置-------------------------------------------------------------------*/
	// 表示孩子是否和组ID是跨基础数据的更改稳定
	@Override
	public boolean hasStableIds() {
		return true;
	}

	// 孩子在指定的位置是可选的，即：arms中的元素是可点击的
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}