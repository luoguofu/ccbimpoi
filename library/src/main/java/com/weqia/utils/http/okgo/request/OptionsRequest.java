package com.weqia.utils.http.okgo.request;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.RequestBody;

import com.weqia.utils.http.okgo.model.HttpHeaders;
import com.weqia.utils.http.okgo.utils.ParamsUtils;
import com.weqia.utils.http.okgo.utils.OkLogger;

public class OptionsRequest extends BaseBodyRequest<OptionsRequest> {

    public OptionsRequest(String url) {
        super(url);
    }

    @Override
    public Request generateRequest(RequestBody requestBody) {
        try {
            headers.put(HttpHeaders.HEAD_KEY_CONTENT_LENGTH, String.valueOf(requestBody.contentLength()));
        } catch (IOException e) {
            OkLogger.e(e);
        }
        Request.Builder requestBuilder = ParamsUtils.appendHeaders(headers);
        return requestBuilder.method("OPTIONS", requestBody).url(url).tag(tag).build();
    }
}