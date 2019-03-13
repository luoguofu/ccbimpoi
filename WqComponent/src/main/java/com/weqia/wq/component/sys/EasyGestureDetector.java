package com.weqia.wq.component.sys;

import android.content.Context;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.View;


public class EasyGestureDetector extends GestureDetector {
    
    private View gesView;
    
    public EasyGestureDetector(Context context, OnGestureListener listener) {
        super(context, listener);
    }

    public EasyGestureDetector(Context context, OnGestureListener listener, Handler handler) {
        super(context, listener, handler);
    }

    public EasyGestureDetector(Context context, OnGestureListener listener, Handler handler,
            boolean unused) {
        super(context, listener, handler, unused);
    }

    public void setGesView(View gesView) {
        this.gesView = gesView;
    }
    
    public View getGesView() {
        return gesView;
    }
}
