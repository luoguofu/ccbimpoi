package com.weqia.wq.component.recycle;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.weqia.wq.UtilApplication;

/**
 * Created by 20161005 on 2017/6/27.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int spaceRight;
    private int spaceBottom;

    public SpacesItemDecoration(int spaceRight, int spaceBottom) {
        this.spaceRight = dip2px(spaceRight / 2);
        this.spaceBottom = dip2px(spaceBottom);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if (position == 0 || position == 1) {
            outRect.top = spaceBottom;
        }
        if (position % 2 == 0) {
            outRect.right = spaceRight;
            outRect.left = spaceBottom;
        } else if (position % 2 == 1) {
            outRect.left = spaceRight;
            outRect.right = spaceBottom;
        }
        outRect.bottom = spaceBottom;
    }

    public static int dip2px(float dipValue) {
        final float scale = UtilApplication.ctx.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
