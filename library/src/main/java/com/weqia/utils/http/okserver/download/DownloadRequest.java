package com.weqia.utils.http.okserver.download;

import com.weqia.data.UtilData;
import com.weqia.utils.http.okgo.cache.CacheMode;
import com.weqia.utils.http.okgo.model.HttpHeaders;
import com.weqia.utils.http.okgo.model.RequestParams;
import com.weqia.utils.http.okgo.request.BaseRequest;
import com.weqia.utils.http.okgo.request.DeleteRequest;
import com.weqia.utils.http.okgo.request.GetRequest;
import com.weqia.utils.http.okgo.request.HeadRequest;
import com.weqia.utils.http.okgo.request.OptionsRequest;
import com.weqia.utils.http.okgo.request.PostRequest;
import com.weqia.utils.http.okgo.request.PutRequest;

public class DownloadRequest extends UtilData {

    private static final long serialVersionUID = -6883956320373276785L;

    public String method;
    public String url;
    public CacheMode cacheMode;
    public String cacheKey;
    public long cacheTime;
    public RequestParams params;
    public HttpHeaders headers;

    public static String getMethod(BaseRequest request) {
        if (request instanceof GetRequest) return "get";
        if (request instanceof PostRequest) return "post";
        if (request instanceof PutRequest) return "put";
        if (request instanceof DeleteRequest) return "delete";
        if (request instanceof OptionsRequest) return "options";
        if (request instanceof HeadRequest) return "head";
        return "";
    }

    public static BaseRequest createRequest(String url, String method) {
        if (method.equals("get")) return new GetRequest(url);
        if (method.equals("post")) return new PostRequest(url);
        if (method.equals("put")) return new PutRequest(url);
        if (method.equals("delete")) return new DeleteRequest(url);
        if (method.equals("options")) return new OptionsRequest(url);
        if (method.equals("head")) return new HeadRequest(url);
        return null;
    }
}