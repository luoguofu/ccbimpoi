package com.weqia.utils.http.okgo.adapter;

public interface CallAdapter<T> {

    /** call执行的代理方法 */
    <R> T adapt(Call<R> call);
}