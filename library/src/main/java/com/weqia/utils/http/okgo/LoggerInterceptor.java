package com.weqia.utils.http.okgo;

import android.text.TextUtils;

import com.weqia.utils.L;
import com.weqia.utils.http.okgo.utils.OkLogger;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class LoggerInterceptor implements Interceptor {
    private boolean showResponse;
    
    public LoggerInterceptor() {
    }

    public LoggerInterceptor(boolean showResponse) {
        this.showResponse = showResponse;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        logForRequest(request);
        Response response = chain.proceed(request);

        return logForResponse(response);
    }

    private void logForRequest(Request request) {
        try {
            String url = request.url().toString();
            Headers headers = request.headers();

            L.e("---------------------request log start---------------------");
            L.e("method : " + request.method());
            L.e("url : " + url);
            if (headers != null && headers.size() > 0) {
            	L.e("headers : \n");
            	L.e(headers.toString());
            }
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null) {
                	L.e("contentType : " + mediaType.toString());
                    if (isText(mediaType)) {
                    	L.e("content : " + bodyToString(request));
                    } else {
                    	L.e("content : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }
        } catch (Exception e) {
            OkLogger.e(e);
        } finally {
        	L.e("---------------------request log end-----------------------");
        }
    }

    private Response logForResponse(Response response) {
        try {
            L.e("---------------------response log start---------------------");
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            L.e("url : " + clone.request().url());
            L.e("code : " + clone.code());
            L.e("protocol : " + clone.protocol());
            if (!TextUtils.isEmpty(clone.message())) L.e("message : " + clone.message());

            if (showResponse) {
                ResponseBody body = clone.body();
                if (body != null) {
                    MediaType mediaType = body.contentType();
                    if (mediaType != null) {
                        L.e("contentType : " + mediaType.toString());
//                        if (isText(mediaType)) {
//                            String resp = body.string();
//                            L.e("content : " + resp);
//                            body = ResponseBody.create(mediaType, resp);
//                            return response.newBuilder().body(body).build();
//                        } else {
//                            L.e("content : " + " maybe [file part] , too large too print , ignored!");
//                        }
                    }
                }
            }
        } catch (Exception e) {
            OkLogger.e(e);
        } finally {
            L.e("---------------------response log end-----------------------");
        }

        return response;
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.toString().equals("application/x-www-form-urlencoded") ||
                    mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml")) //
                return true;
        }
        return false;
    }

    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "something error when show requestBody.";
        }
    }
}
