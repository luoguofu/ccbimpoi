package com.weqia.utils.http.okgo.convert;

import okhttp3.Response;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapConvert implements Converter<Bitmap> {

    public static BitmapConvert create() {
        return BitmapConvert.ConvertHolder.convert;
    }

    private static class ConvertHolder {
        private static BitmapConvert convert = new BitmapConvert();
    }

    @Override
    public Bitmap convertSuccess(Response value) throws Exception {
        return BitmapFactory.decodeStream(value.body().byteStream());
    }
}