package com.weqia.utils.http;

import com.weqia.utils.L;
import com.weqia.utils.MD5Util;
import com.weqia.utils.ParameterUtil;
import com.weqia.utils.StrUtil;
import com.weqia.utils.ZipUtils;
import com.weqia.utils.exception.CheckedExceptionHandler;
import com.weqia.utils.http.okgo.OkGo;
import com.weqia.utils.http.okgo.callback.FileCallback;
import com.weqia.utils.http.okgo.callback.RequestCallBack;
import com.weqia.utils.http.okgo.model.RequestParams;
import com.weqia.utils.http.okgo.request.GetRequest;
import com.weqia.utils.http.okserver.download.DownloadManager;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import okhttp3.Response;

/**
 * Created by berwin on 2016/10/28.
 */

public class HttpUtil {

    public static final HttpUtil getInstance() {
        return HttpUtilHolder.INSTANCE;
    }

    private static final class HttpUtilHolder {
        private static final HttpUtil INSTANCE = new HttpUtil();
    }

    public RequestParams buildParam(RequestParams params, String signKey) {
        HashMap<String, String[]> requestMap = new HashMap<String, String[]>();
        Iterator<String> urlIter = params.urlParams.keySet().iterator();
        while (urlIter.hasNext()) {
            String key = urlIter.next();
            // 转移&等url字符
            try {
                if (params.urlParams.get(key) == null) {
                    continue;
                }
                requestMap.put(key,
                        new String[]{URLEncoder.encode(params.urlParams.get(key), "UTF-8")});
            } catch (Exception e) {
                CheckedExceptionHandler.handleException(e);
            }
        }
        StringBuffer sb = new StringBuffer();
        // 获取签名前，排过序的参数字符串
        String sParam = ParameterUtil.toStringArray(requestMap);
        String sign = sParam;
        // MD5签名
        //不同签名，防止微洽客户端登录桩桩服务器
        sign = MD5Util.md32(sign + signKey);
        sb.append(sParam);
        sb.append("&sign=").append(sign);

        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        // 压缩
        map.put("s", ZipUtils.gzip(sb.toString()));
        params.urlParams = map;
        return params;
    }


    public void resetHttp() {
    }

    public void configUserAgent(String userAgent) {
    }

    /**
     * 设置网络连接超时时间，默认为10秒钟
     *
     * @param timeout
     */
    public void configTimeout(int timeout) {
    }

    /**
     * @param url
     * @param callBack
     * @Description
     */
    public void get(String url, RequestCallBack<? extends Object> callBack) {
        get(url, null, callBack);
    }

    /**
     * @param url
     * @param params
     * @param callBack
     * @Description
     */
    public void get(String url, RequestParams params,
                    RequestCallBack<? extends Object> callBack) {
        OkGo.get(url) //
                .tag(params) //
                .params(params) //
                .execute(callBack);
    }


    /**
     * @param url
     * @param callBack
     * @Description
     */
    public void post(String url, RequestCallBack<? extends Object> callBack) {
        post(url, null, callBack);
    }

    /**
     * @param url
     * @param params
     * @param callBack
     * @Description
     */
    public void post(String url, RequestParams params,
                     RequestCallBack<? extends Object> callBack) {
        OkGo.post(url) //
                .tag(params) //
                .params(params) //
                .execute(callBack); //
    }
    /**
     * @param url
     * @return
     * @Description
     */
    public Object getSync(String url) {
        return getSync(url, null);
    }

    /**
     * @param url
     * @param params
     * @return
     * @Description
     */
    public Object getSync(String url, RequestParams params) {
        try {
            Response response = OkGo.get(url) //
                    .tag(params) //
                    .params(params) //
                    .execute(); //
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @param url
     * @return
     * @Description
     */
    public Object postSync(String url) {
        return postSync(url, null);
    }

    /**
     * @param url
     * @param params
     * @return
     * @Description
     */
    public Object postSync(String url, RequestParams params) {
        try {
            Response response = OkGo //
                    .post(url) //
                    .tag(params) //
                    .params(params) //
                    .execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param url
     * @param target
     * @param callback
     * @return
     * @Description
     */
    public void download(String url, String target, String tag, FileCallback callback) {

        if (L.D) L.i("[下载任务] " + url);
        String rootPath = target;
        if (rootPath != null && !rootPath.endsWith("/"))
            rootPath = rootPath.substring(0, target.lastIndexOf("/"));
        if (StrUtil.isEmptyOrNull(tag))
            tag = url;
        GetRequest request = OkGo.get(url).tag(tag);
        String fileName = target.replace(rootPath, "");
        fileName = fileName.replaceAll(" ", "");
//        fileName = fileName.replace(".", "");
//        fileName = fileName.replace("=", "");
        if (L.D) L.e("文件名称=" + fileName);

        DownloadManager.getInstance().addTask(request, fileName, rootPath, callback);
    }
}
