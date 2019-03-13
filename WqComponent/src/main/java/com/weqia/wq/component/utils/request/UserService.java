package com.weqia.wq.component.utils.request;

import com.weqia.utils.L;
import com.weqia.utils.MD5Util;
import com.weqia.utils.ParameterUtil;
import com.weqia.utils.StrUtil;
import com.weqia.utils.ZipUtils;
import com.weqia.utils.datastorage.file.PathUtil;
import com.weqia.utils.exception.CheckedExceptionHandler;
import com.weqia.utils.http.HttpUtil;
import com.weqia.utils.http.okgo.callback.FileCallback;
import com.weqia.utils.reflect.RefUtil;
import com.weqia.wq.RouterUtil;
import com.weqia.wq.data.EnumData.HttpServer;
import com.weqia.wq.data.LoginUserData;
import com.weqia.wq.data.UpFileData;
import com.weqia.wq.data.WPfCommon;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.data.global.Hks;
import com.weqia.wq.data.global.WeqiaApplication;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Http请求入口
 */
public class UserService {
    // 42.121.254.25 正式环境，带负载均衡功能
    // 121.199.29.64 预发布
    // 121.199.31.70 正式环境
    // weqia.com
    private static HttpUtil httpUtil;
    public static String SERV_IP;
    private static String URL;
    private static String UP_FILE_URL;
    private static String GET_FILE_URL;
    public static boolean bFlag = true;
    private static boolean bDebug = false;
    public static String httpServ;

    public static void initServer(String serverIp) {
        // 私有云IP
        if (WPfCommon.getInstance().get(Hks.is_private_ip, Boolean.class, false)) {
            serverIp = WPfCommon.getInstance().get(Hks.private_ip, String.class);// StatedPerference.getPrivateIp();
        }
        httpServ = "http://" + serverIp;
        httpServ = httpServ.replace(" ", "");
        URL = httpServ + "/gateWay.do";
        UP_FILE_URL = httpServ + "/fileUpload.do";
        GET_FILE_URL = httpServ + "/fileUrl.do";
    }

    public static HttpUtil getHttpUtil() {
        if (bFlag) {
//            if (CoConfig.zt) {
//                SERV_IP = WeqiaApplication.ctx.getResources().getString(R.string.home_url);// HttpServer.ZT_SERV.strName();
//            } else {
            SERV_IP = HttpServer.SERV_FORMAL.strName();
//            }
            initServer(SERV_IP);
            bFlag = false;
        }

        if (httpUtil == null) {
            httpUtil = HttpUtil.getInstance();
        }
        return httpUtil;
    }


    public static void resetHttp() {
        if (httpUtil != null) {
            RouterUtil.routerActionSync(WeqiaApplication.ctx, "pvremotemsg", "acmsginit");
            httpUtil.resetHttp();
        }
    }

    public static boolean getDataFromServer(Boolean bShowDlg, ServiceParams params,
                                            ServiceRequester serviceRequester) {
        return getDataFromServer(bShowDlg, params, false, serviceRequester);
    }

    public static boolean getDataFromServer(Boolean bShowDlg, final ServiceParams params,
                                            boolean cache, final ServiceRequester serviceRequester) {

        if (cache && serviceRequester != null && StrUtil.notEmptyOrNull(serviceRequester.getId())) {
            ResultEx resultEx = ServiceRequester.getReqCache().get(serviceRequester.getId());
            if (resultEx != null) {
                if (L.D) L.e("从缓存里面获取数据 key =" + serviceRequester.getId());
                if (serviceRequester.getClickView() != null)
                    serviceRequester.getClickView().setEnabled(true);
                serviceRequester.onResult(resultEx);
                return true;
            }
        }

        if (StrUtil.isEmptyOrNull(serviceRequester.getId())) { // 请求码为是空的话 701-1
            serviceRequester.setId(params.getItype() + "");
        }

        if (params.getItype() > 300 && StrUtil.isEmptyOrNull(params.getMid())) {
            String errMsg = "mid == null";
            if (WeqiaApplication.getInstance().getLoginUser() != null) {
                params.setMid(WeqiaApplication.getInstance().getLoginUser().getMid());
            }
        }
        Map<String, ?> paramsMap = RefUtil.getFieldValueMap(params);
        if (bShowDlg == null || !bShowDlg) {
            serviceRequester.setbShowDlg(false); // 不显示请求等待对话框
        } else {
            serviceRequester.setbShowDlg(true);
        }
        setParams(params, paramsMap);
        String reqKey = paramsMap.toString();
        serviceRequester.setReqKey(reqKey);
//        if (L.D) L.e("getReqSet：" + WeqiaApplication.getReqSet());
//        if (!WeqiaApplication.getReqSet().add(reqKey)) {
//            if (L.D) L.e("重复请求~");
//            return true;
//        } else {
//            if (L.D) L.e("执行---" + reqKey);
//        }
        serviceRequester.setCache(cache);
        sendRequest(params, serviceRequester);
//        }
        return true;
    }

    public static boolean getDataFromServer(Boolean bShowDlg, String loadingStr,
                                            boolean cancelable, ServiceParams params, ServiceRequester serviceRequester) {
        Map<String, ?> paramsMap = RefUtil.getFieldValueMap(params);
        if (bShowDlg == null || !bShowDlg) {
            serviceRequester.setbShowDlg(false, loadingStr, cancelable);
        } else {
            serviceRequester.setbShowDlg(true, loadingStr, cancelable);
        }
        setParams(params, paramsMap);
        if (StrUtil.isEmptyOrNull(serviceRequester.getId())) {
            serviceRequester.setId(params.getItype() + ":");
            //+ RequestType.valueOf(params.getItype()).strName() + ":");
        }
        sendRequest(params, serviceRequester);
//        }
        return true;
    }

    public static String setParams(ServiceParams params, Map<String, ?> paramsMap) {

        paramsMap.remove("paramString");
        paramsMap.remove("requestString");
        paramsMap.remove("fileParams");
        paramsMap.remove("httpEntity");
        paramsMap.remove("urlParams");
        paramsMap.remove("hasMore");
        paramsMap.remove("progress");
        paramsMap.remove("realContent");
        for (String key : paramsMap.keySet()) {
            Object obj = paramsMap.get(key);
            if (obj != null) {
                if (key.equalsIgnoreCase("mCoId") && StrUtil.isEmptyOrNull(obj.toString())) {
                    if (L.D) L.e("mCoID = '',字段忽略");
                    continue;
                }
                params.put(key, obj.toString());
            }
        }

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

        String sign;
        LoginUserData loginUser = WeqiaApplication.getInstance().getLoginUser();


        if (params.getItype() >= 300) {
            if (loginUser != null) {
                sign = sParam + loginUser.getKey();
            } else {
                sign = sParam;
            }
        } else {
            sign = sParam;
        }

        // MD5签名
        //不同签名，防止微洽客户端登录BIM360服务器
        sign = sign + "BIM";
        sign = MD5Util.md32(sign);
        sb.append(sParam);
        sb.append("&sign=").append(sign);

        if (L.D && params.getItype().intValue() != 10000) {
//            RequestType requestType = RequestType.valueOf(params.getItype());
            String itypeName = "";
//            if (requestType == null) {
//                itypeName = "";
//            } else {
//                itypeName = requestType.strName();
//            }
            if (L.D)
                L.e("request::" + params.getItype() + "::" + itypeName + "::" + " [" + sb + "]");
        }
        String returnStr = new String(sb);
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        // 压缩
        if (!bDebug) {
            map.put("s", ZipUtils.gzip(sb.toString()));
            params.urlParams = map;
        }
        return returnStr;
    }


    /**
     * 发送请求调用的方法
     * @param params
     * @param serviceRequester
     * @Description
     */
    public static boolean getDataFromServer(ServiceParams params, ServiceRequester serviceRequester) {
        return getDataFromServer(null, params, serviceRequester);
    }

    /**
     * 获取真实url
     * @param key
     * @return
     * @Description
     */
    public static String getBitmapUrl(String key) {
        if (StrUtil.isEmptyOrNull(key)) {
            if (L.D) L.e("key is null");
            return null;
        }
        L.e("获取图片url === " + key);
        String accountType = "", bucket = "";
        if (key.contains(GlobalConstants.BUCKET_SPIT)) {
            String[] tmpStrs = key.split(GlobalConstants.BUCKET_SPIT);

            if (tmpStrs.length >= 3) {
                accountType = tmpStrs[0];
                bucket = tmpStrs[1];
                key = tmpStrs[2];
            }
        }
        // 判断mid是否存在导致查询企业没有图片
        LoginUserData loginUserData = WeqiaApplication.getInstance().getLoginUser();
        if (loginUserData != null && StrUtil.notEmptyOrNull(loginUserData.getMid())) {
            ServiceParams params = new ServiceParams(10000);
            try {
                if (key.contains("&")) {
                    String keyStr[] = key.split("&");
                    params.put("urls", keyStr[0]);
                    if (keyStr[1].contains("=")) {
                        String thStr[] = keyStr[1].split("=");
                        params.put("th", thStr[1]);
                    }
                } else {
                    params.put("urls", key);
                }

                if (StrUtil.notEmptyOrNull(bucket))
                    params.put("bucket", bucket);
                if (StrUtil.notEmptyOrNull(accountType))
                    params.put("accountType", accountType);
            } catch (Exception e) {
                CheckedExceptionHandler.handleException(e);
            }
            Map<String, ?> paramsMap = RefUtil.getFieldValueMap(params);
            if (paramsMap == null) {
                return null;
            } else {
                setParams(params, paramsMap);
                String data = (String) sendSyncRequest(params);
//                if (L.D) L.i("return::10000::" + "[" + data.trim() + "]");
                if (StrUtil.isEmptyOrNull(data)) {
                    return null;
                } else {
                    ResultEx resultEx = ResultEx.fromString(ResultEx.class, data.trim());
                    if (resultEx == null) {
                        return null;
                    } else {
                        UpFileData objUrl = resultEx.getDataObject(UpFileData.class);
                        if (objUrl != null) {
                            String tmpUrl = objUrl.getUrl();
                            if (L.D) L.e("tmpKey = " + key + ", realKey = " + tmpUrl);
                            return tmpUrl;
                        }
                        return null;
                    }
                }
            }
        } else {
            return null;
        }
    }

    public static String getDownloadUrl(String key) {
        if (StrUtil.isEmptyOrNull(key)) {
            if (L.D) L.e("key is null");
            return null;
        }

        L.e("获取下载文件url === " + key);
        String accountType = "", bucket = "";
        if (key.contains(GlobalConstants.BUCKET_SPIT)) {
            String[] tmpStrs = key.split(GlobalConstants.BUCKET_SPIT);

            if (tmpStrs.length >= 3) {
                accountType = tmpStrs[0];
                bucket = tmpStrs[1];
                key = tmpStrs[2];
            }
        }

        LoginUserData loginUserData = WeqiaApplication.getInstance().getLoginUser();
        if (loginUserData != null && StrUtil.notEmptyOrNull(loginUserData.getMid())) {
            ServiceParams params = new ServiceParams(10000);
            params.put("urls", key);
            if (StrUtil.notEmptyOrNull(bucket))
                params.put("bucket", bucket);
            if (StrUtil.notEmptyOrNull(accountType))
                params.put("accountType", accountType);
            Map<String, ?> paramsMap = RefUtil.getFieldValueMap(params);
            if (paramsMap == null) {
                return null;
            } else {
                setParams(params, paramsMap);
                String data = (String) sendSyncRequest(params);
//                if (L.D) L.i("return::10000::" + "[" + data.trim() + "]");
                if (StrUtil.isEmptyOrNull(data)) {
                    return null;
                } else {
                    ResultEx resultEx = ResultEx.fromString(ResultEx.class, data.trim());
                    if (resultEx == null) {
                        return null;
                    } else {
                        UpFileData objUrl = resultEx.getDataObject(UpFileData.class);
                        if (objUrl != null) {
                            if (L.D) {
                                L.i("img-url:" + objUrl.getUrl());
                            }
                            return objUrl.getUrl();
                        }
                        return null;
                    }
                }
            }
        } else {
            return null;
        }

    }

    private static void sendRequest(ServiceParams params, ServiceRequester serviceRequester) {
        HttpUtil httpUtil = getHttpUtil();
        // if (L.D) L.e("url = " + URL);
        String url = URL;
        if (params.getItype().intValue() == 10000) {
            url = GET_FILE_URL;
        }
        httpUtil.post(url, params, serviceRequester);
    }

    public static void downloadFile(String url, String target, String tag, FileCallback callback) {
        getHttpUtil().download(url, target, tag, callback);
    }

    // 下载录音文件
    public static void downloadVoiceFile(String realurl, String voiceUrl, final FileCallback callback) {
        String saveName = voiceUrl;
        saveName = saveName.replaceAll("/", "_");
        String target = PathUtil.getVoicePath() + File.separator + saveName;
        getHttpUtil().download(realurl, target, voiceUrl, callback);
    }

    private static Object sendSyncRequest(ServiceParams params) {
        return getHttpUtil().postSync(GET_FILE_URL, params);
    }

    public static Object sendSyncPost(ServiceParams params) {
        return getHttpUtil().postSync(URL, params);
    }

    /**
     * 文件上传
     * @param params
     * @param serviceRequester
     * @Description
     * @author Dminter
     */
    public static void getDataFromServerForUpFile(Boolean bShow, ServiceParams params,
                                                  ServiceRequester serviceRequester) {


        HashMap<String, String[]> requestMap = new HashMap<String, String[]>();
        Iterator<String> urlIter = params.urlParams.keySet().iterator();
        while (urlIter.hasNext()) {
            String key = urlIter.next();
            // 转移&等url字符
            try {
                requestMap.put(key,
                        new String[]{URLEncoder.encode(params.urlParams.get(key), "UTF-8")});
            } catch (Exception e) {
                CheckedExceptionHandler.handleException(e);
            }
        }

        Map<String, ?> paramsMap = RefUtil.getFieldValueMap(params);
        if (paramsMap == null) {
            String errMsg = "you must input a correct serviceparams to get data";
            serviceRequester.onFailure(new IllegalArgumentException(errMsg), errMsg);
            return;
        } else {
            serviceRequester.setbShowDlg(bShow);
            setParams(params, paramsMap);
            if (StrUtil.isEmptyOrNull(serviceRequester.getId())) {
                serviceRequester.setId(params.getItype() + ":");
//                        + RequestType.valueOf(params.getItype()).strName() + ":");
            }
            getHttpUtil().post(UP_FILE_URL, params, serviceRequester);
        }
    }

    public static void getDataFromServerForUpFile(ServiceParams params,
                                                  ServiceRequester serviceRequester) {
        getDataFromServerForUpFile(true, params, serviceRequester);
    }


    public static ResultEx getSyncInfo(ServiceParams params) {
        Map<String, ?> paramsMap = RefUtil.getFieldValueMap(params);
        if (paramsMap == null) {
            return null;
        } else {
            setParams(params, paramsMap);
            String data = (String) sendSyncPost(params);
            if (StrUtil.isEmptyOrNull(data)) {
                return null;
            } else {
                ResultEx resultEx = ResultEx.fromString(ResultEx.class, data.trim());
                if (resultEx == null) {
                    return null;
                } else {
                    // if (L.D) L.i(resultEx.toString());
                    return resultEx;
                }
            }
        }
    }

}
