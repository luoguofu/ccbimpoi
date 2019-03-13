package com.weqia.utils.http.okgo.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

import android.text.TextUtils;

import com.weqia.utils.http.okgo.model.HttpHeaders;
import com.weqia.utils.http.okgo.model.RequestParams;

public class ParamsUtils {
    /**
     * 将传递进来的参数拼接成 url
     */
    public static String createUrlFromParams(String url,
                                             Map<String, String> params) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(url);
            if (url.indexOf('&') > 0 || url.indexOf('?') > 0)
                sb.append("&");
            else
                sb.append("?");
            for (Map.Entry<String, String> urlParams : params.entrySet()) {
                // 对参数进行 utf-8 编码,防止头信息传中文
                String urlValue = URLEncoder.encode(urlParams.getValue(),
                        "UTF-8");
                sb.append(urlParams.getKey()).append("=").append(urlValue)
                        .append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            OkLogger.e(e);
        }
        return url;
    }

    /**
     * 通用的拼接请求头
     */
    public static Request.Builder appendHeaders(HttpHeaders headers) {
        Request.Builder requestBuilder = new Request.Builder();
        if (headers.headersMap.isEmpty())
            return requestBuilder;
        Headers.Builder headerBuilder = new Headers.Builder();
        try {
            for (Map.Entry<String, String> entry : headers.headersMap
                    .entrySet()) {
                // 对头信息进行 utf-8 编码,防止头信息传中文,这里暂时不编码,可能出现未知问题,如有需要自行编码
                // String headerValue = URLEncoder.encode(entry.getValue(),
                // "UTF-8");
                headerBuilder.add(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            OkLogger.e(e);
        }
        requestBuilder.headers(headerBuilder.build());
        return requestBuilder;
    }

    /**
     * 生成类似表单的请求体
     */
    public static RequestBody generateMultipartRequestBody(RequestParams params) {
        if (params.fileParams.isEmpty()) {
            // 表单提交，没有文件
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            for (String key : params.urlParams.keySet()) {
                bodyBuilder.add(key, params.urlParams.get(key));
            }
            return bodyBuilder.build();
        } else {
            // 表单提交，有文件
            MultipartBody.Builder multipartBodybuilder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            // 拼接键值对
            if (!params.urlParams.isEmpty()) {
                for (Map.Entry<String, String> entry : params.urlParams
                        .entrySet()) {
                    multipartBodybuilder.addFormDataPart(entry.getKey(),
                            entry.getValue());
                }
            }
            // 拼接文件
            for (Map.Entry<String, RequestParams.FileWrapper> entry : params.fileParams
                    .entrySet()) {
                RequestParams.FileWrapper fileWrapper = entry.getValue();
                if (fileWrapper.file != null) {
                    RequestBody fileBody = RequestBody.create(
                            fileWrapper.contentType, fileWrapper.file);
                    multipartBodybuilder.addFormDataPart(entry.getKey(),
                            fileWrapper.fileName, fileBody);
                } else if (fileWrapper.inputStream != null) {
                    RequestBody fileBody = create(fileWrapper.contentType,
                            fileWrapper.inputStream);
                    multipartBodybuilder.addFormDataPart(entry.getKey(),
                            fileWrapper.fileName, fileBody);
                }
            }
            return multipartBodybuilder.build();
        }
    }

    public static RequestBody create(final MediaType mediaType,
                                     final InputStream inputStream) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return mediaType;
            }

            @Override
            public long contentLength() {
                try {
                    return inputStream.available();
                } catch (IOException e) {
                    return 0;
                }
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(inputStream);
                    sink.writeAll(source);
                } finally {
                    Util.closeQuietly(source);
                }
            }
        };
    }

    /**
     * 根据响应头或者url获取文件名
     */
    public static String getNetFileName(Response response, String url) {
        String fileName = getHeaderFileName(response);
        if (TextUtils.isEmpty(fileName))
            fileName = getUrlFileName(url);
        if (TextUtils.isEmpty(fileName))
            fileName = "nofilename";
        return fileName;
    }

    /**
     * 解析文件头 Content-Disposition:attachment;filename=FileName.txt
     */
    private static String getHeaderFileName(Response response) {
        String dispositionHeader = response
                .header(HttpHeaders.HEAD_KEY_CONTENT_DISPOSITION);
        if (dispositionHeader != null) {
            String split = "filename=";
            int indexOf = dispositionHeader.indexOf(split);
            if (indexOf != -1) {
                String fileName = dispositionHeader.substring(
                        indexOf + split.length(), dispositionHeader.length());
                fileName = fileName.replaceAll("\"", ""); // 文件名可能包含双引号,需要去除
                return fileName;
            }
        }
        return null;
    }

    /**
     * 通过 ‘？’ 和 ‘/’ 判断文件名
     */
    private static String getUrlFileName(String url) {
        int index = url.lastIndexOf('?');
        String filename;
        if (index > 1) {
            filename = url.substring(url.lastIndexOf('/') + 1, index);
        } else {
            filename = url.substring(url.lastIndexOf('/') + 1);
        }
        return filename;
    }

    /**
     * 根据路径删除文件
     */
    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path))
            return true;
        File file = new File(path);
        if (!file.exists())
            return true;
        if (file.isFile()) {
            boolean delete = file.delete();
            OkLogger.e("deleteFile:" + delete + " path:" + path);
            return delete;
        }
        return false;
    }
}