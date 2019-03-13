package com.nostra13.universalimageloader.core.listener;

import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;

public interface SyncLoadInterface {
    void onLoadError(String imageUri, View view, FailReason failReason);
    
    void onLoadingComplete(String imageUri, View view, Bitmap loadedImage);
}
