package com.weqia.wq.component.view.title_pop;

import android.content.Context;


public class TitleItem {
    public Context ctx;
    public Integer id;
    public Integer drawableId;
    public Integer drawableRightId;
    public CharSequence mTitle;


    public TitleItem(Context context, CharSequence title, Integer drawableId) {
        this.ctx = context;
        this.mTitle = title;
        this.drawableId = drawableId;
    }

    public TitleItem(Context context, Integer id, CharSequence title, Integer drawableId) {
        this.ctx = context;
        this.id = id;
        this.mTitle = title;
        this.drawableId = drawableId;
    }

    public TitleItem(Context context, CharSequence title, Integer drawableId, Integer drawableRightId) {
        this.ctx = context;

        this.mTitle = title;
        this.drawableId = drawableId;
        this.drawableRightId = drawableRightId;
    }


}
