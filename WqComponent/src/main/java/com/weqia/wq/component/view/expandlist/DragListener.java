package com.weqia.wq.component.view.expandlist;

import android.view.View;
import android.widget.ExpandableListView;

public interface DragListener {
	void onStartDrag(View itemView);
	
	void onDrag(int x, int y, ExpandableListView listView);
	
	void onStopDrag(View itemView);
}
