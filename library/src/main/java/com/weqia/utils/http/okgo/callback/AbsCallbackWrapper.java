package com.weqia.utils.http.okgo.callback;

import okhttp3.Call;
import okhttp3.Response;

public class AbsCallbackWrapper<T> extends RequestCallBack<T> {
    @Override
    public T convertSuccess(Response value) throws Exception {
        value.close();
        return (T) value;
    }

    @Override
    public void onSuccessData(T t, Call call, Response response) {
    }
}