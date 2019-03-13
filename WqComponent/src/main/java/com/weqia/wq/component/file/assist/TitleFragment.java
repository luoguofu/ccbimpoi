package com.weqia.wq.component.file.assist;

import android.support.v4.app.Fragment;
import android.view.View;

public abstract class TitleFragment extends Fragment implements View.OnClickListener

{
    /**
     * 当前item的位置
     */
    public static int itemPosition;

    private boolean bLoaded;


    /**
     * 右边按钮点击事件
     */
    public abstract void btRightClick();

    public boolean isbLoaded() {
        return bLoaded;
    }

    public void setbLoaded(boolean bLoaded) {
        this.bLoaded = bLoaded;
    }


}
