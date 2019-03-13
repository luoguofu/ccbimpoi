package com.weqia.utils.http.okgo.adapter;

import com.weqia.utils.http.okgo.callback.RequestCallBack;
import com.weqia.utils.http.okgo.model.Response;
import com.weqia.utils.http.okgo.request.BaseRequest;

public interface Call<T> {
    /** 同步执行 */
    Response<T> execute() throws Exception;

    /** 异步回调执行 */
    void execute(RequestCallBack<T> callback);

    /** 是否已经执行 */
    boolean isExecuted();

    /** 取消 */
    void cancel();

    /** 是否取消 */
    boolean isCanceled();

    Call<T> clone();

    BaseRequest getBaseRequest();
}