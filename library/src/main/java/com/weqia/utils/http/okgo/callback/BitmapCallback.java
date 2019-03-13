package com.weqia.utils.http.okgo.callback;

import okhttp3.Response;
import android.graphics.Bitmap;

import com.weqia.utils.http.okgo.convert.BitmapConvert;

public abstract class BitmapCallback extends RequestCallBack<Bitmap> {

    @Override
    public Bitmap convertSuccess(Response response) throws Exception {
        Bitmap bitmap = BitmapConvert.create().convertSuccess(response);
        response.close();
        return bitmap;
    }
}