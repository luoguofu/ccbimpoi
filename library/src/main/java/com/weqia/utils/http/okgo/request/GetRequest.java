package com.weqia.utils.http.okgo.request;

import okhttp3.Request;
import okhttp3.RequestBody;

import com.weqia.utils.http.okgo.utils.ParamsUtils;

public class GetRequest extends BaseRequest<GetRequest> {

    public GetRequest(String url) {
        super(url);
    }

    @Override
    public RequestBody generateRequestBody() {
        return null;
    }

    @Override
    public Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = ParamsUtils.appendHeaders(headers);
        url = ParamsUtils.createUrlFromParams(baseUrl, params.urlParams);
        return requestBuilder.get().url(url).tag(tag).build();
    }
}