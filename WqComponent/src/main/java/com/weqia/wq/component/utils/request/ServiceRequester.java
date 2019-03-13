package com.weqia.wq.component.utils.request;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weqia.LruMemoryCache;
import com.weqia.data.UtilsConstants;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.exception.CheckedExceptionHandler;
import com.weqia.utils.http.okgo.callback.StringCallback;
import com.weqia.wq.global.ComponentUtil;
import com.weqia.wq.component.utils.DialogUtil;
import com.weqia.wq.data.ComponentReqEnum;
import com.weqia.wq.data.EnumData;
import com.weqia.wq.data.EnumData.ErrorCodeType;
import com.weqia.wq.data.eventbus.RefreshEvent;
import com.weqia.wq.data.global.WeqiaApplication;

import org.greenrobot.eventbus.EventBus;

import java.util.IllegalFormatException;

/**
 * 请求类
 *
 * @author Dminter
 */
public abstract class ServiceRequester extends StringCallback {

    private Context ctx;
    private Dialog dialog;// 显示请求loading对话框
    private boolean cache;
    private static LruMemoryCache<String, ResultEx> reqCache;
    private String id;
    private String reqKey;
    private View clickView;

    public static final int LOGIN_OTH_DEVICE = 37;
    public static final int LEAVE_COMPANY = 38;

    public ServiceRequester() {
        super();
    }

    public ServiceRequester(String id) {
        this(id, null);
    }

    public ServiceRequester(Context ctx) {
        this(ctx, null, null);
    }

    public ServiceRequester(String id, View clickView) {
        this(null, id, clickView);
    }

    public ServiceRequester(Context ctx, String id) {
        this(null, id, null);
    }

    public ServiceRequester(Context ctx, View clickView) {
        this(ctx, null, clickView);
    }

    // ID判断是哪个请求的数据
    public ServiceRequester(Context ctx, String id, View clickView) {

        super();
        this.id = id;
        this.ctx = ctx;
        this.clickView = clickView;

        if (getClickView() != null) {
            L.e("把按钮禁用掉-----------------");
            getClickView().setEnabled(false);
        }
    }

    // 显示请求等待对话框与否
    public void setbShowDlg(boolean bShowDlg) {
        if (bShowDlg && ctx != null) {
            dialog = DialogUtil.commonLoadingDialog(ctx, "");
            dialog.setCancelable(false);// 是否可点击取消
            dialog.show();
        }
    }

    public void setbShowDlg(boolean bShowDlg, String loadingStr, boolean cancelable) {
        if (bShowDlg) {
            dialog = DialogUtil.commonLoadingDialog(ctx, loadingStr);
            dialog.setCancelable(cancelable);// 是否可点击取消
            dialog.show();
        }
    }

    private void dismissDlg() {
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (IllegalFormatException e) {
                if (L.D) L.e("对话框消失错误");
            }
        }
    }

    @Override
    public void onSuccess(String content) {

        if (clickView != null) {
            L.e("按钮复原------" + content);
            clickView.setEnabled(true);
        }
        dismissDlg();
        if (WeqiaApplication.isLev) {
            return;
        }
        if (StrUtil.isEmptyOrNull(content)) {
            if (L.D) {
                L.e(ErrorCodeType.RECEIVE_NOTHING.strName());
            }
            onError(ErrorCodeType.RECEIVE_NOTHING.order());
            return;
        }
        if (L.D) {
            String name = getId(); // 接口的类型
//            try {
//                int itype = Integer.parseInt(name);
//                RequestType type = RequestType.valueOf(itype);
//                if (type != null) {
//                    name += "::" + type.strName() + "::";
//                }
//            } catch (NumberFormatException e) {
//            }
            // if (name.contains("357")) {
            // if(L.D) L.i("同步通讯录数据");
            // } else {

            L.i("return::" + name);
//                    L.json(content);
            L.i(content);

//            if (L.D) L.i("return::" + name + "[" + content.trim() + "]");
            // }

        }
        ParseResultEx(content);
    }

    private void ParseResultEx(String content) {
        try {
            Log.e("Response", getId() + "::" + content.trim());
            JSONObject obj = JSON.parseObject(content);
            Integer retNo = 1;
            retNo = obj.getInteger("ret");
            String errorMsg = obj.getString("msg");
            if (ctx == null) {
                ctx = WeqiaApplication.ctx;
            }
            if (retNo != null && retNo < 0 && StrUtil.notEmptyOrNull(errorMsg)) {
                if (EnumData.SureErrorCodeType.PROJECT_NOT_OPEN.order() == retNo.intValue()
                        || EnumData.SureErrorCodeType.ENTERPRISES_COUNT_LIMIT.order() == retNo
                        .intValue()) {
                    DialogUtil.commonShowWithLinksDialog(ctx, errorMsg).show();

                    /**
                     *系统异常也需要显示
                     * EnumData.SureErrorCodeType.SYS_ERROR.order() == retNo.intValue()
                     */
                } else if (-151 == retNo.intValue()
                        ) {
                    //// TODO: 2017/2/20  提示系统异常 用于测试
//                    if (L.D) {
//                        L.toastShort(errorMsg);
//                    }
                } else if (retNo.intValue() == ErrorCodeType.USER_IS_LEAVE.order()) {
                    // 数据库清除
                    WeqiaApplication.isLev = true;
                    EventBus.getDefault().post(new RefreshEvent(LEAVE_COMPANY));
                    return;
                } else if (retNo.intValue() == ErrorCodeType.SIGN_VALID_ERROR.order()) {
                    if (getId().equalsIgnoreCase(ComponentReqEnum.CLEAN_GETUI_CLIENTID.order() + "")) {
                        if (L.D) L.e("已经是登出接口,不再弄isLogout");
                        WeqiaApplication.isLogout = false;
                    } else {
                        WeqiaApplication.isLogout = true;
                        L.toastShort(errorMsg);
                        EventBus.getDefault().post(new RefreshEvent(LOGIN_OTH_DEVICE));
                    }
                    return;
                } else {
                    if (retNo != -94034 //
                            && retNo != -552 //
                            && retNo != -573 //
                            && retNo != -94053 //
                            && retNo != -651 //
                            && retNo != -657 //
                            && retNo != -625 //
                            && retNo != -93131 //
                            && retNo != -351 //
                            && retNo != -91139 //
                            && retNo != -303 //
                            && retNo != -1252 //
                            && retNo != -94038 //
                            && retNo != ErrorCodeType.RECEIVE_NOTHING.order()
                            && retNo != -937
                            && retNo != -177014  //咨询项目详情产值查看无权限不需要提示
                            /**
                             *缺少参数或你还未加入当前企业 不要提示
                             */
                            && retNo != -12
                            /**
                             *输入参数不能为空
                             */
                            && retNo != -15

                            ) {
                        if (retNo == -153) {
                            // && XUtil.isEnterpriseExternalStaff()
                        } else {
                            if (StrUtil.notEmptyOrNull(errorMsg)) {
                                if (EnumData.SureErrorCodeType.SYS_ERROR.order() == retNo.intValue()) {
                                    String log = "";
                                    String name = getId(); // 接口的类型
                                    try {
                                        int itype = Integer.parseInt(name);
//                                        RequestType type = RequestType.valueOf(itype);
//                                        if (type != null) {
//                                            name += "::" + type.strName() + "::";
//                                        }
                                        name += errorMsg;
                                    } catch (NumberFormatException e) {
                                    }
                                    log = name;
                                    if (StrUtil.notEmptyOrNull(log)) {
                                        ComponentUtil.writeLog(log);
                                    }
                                }
                                L.toastShort(errorMsg);
                            }
                        }
                    }
                }
                onError(retNo);
                onErrorMsg(retNo, errorMsg);
                return;
            }
            ResultEx resultEx = ResultEx.fromString(ResultEx.class, content.trim());
            if (resultEx == null) {
                onError(ErrorCodeType.ILLEGAL_DATA_FORMAT.order());
                return;
            } else {
                successWithData(resultEx);
            }
        } catch (Exception e) {
            // L.toastShort("网络连接异常,请检查网络设置是否正常！");
            // onError(ErrorCodeType.REL_ILLEGAL.order());
//             onError(ErrorCodeType.SERVER_ERROR.order());
//            L.toastShort("");
            CheckedExceptionHandler.handleException(e);
        }
    }

    private void successWithData(ResultEx resultEx) {
        try {
            String ret = resultEx.getRet(); // 代表数量
            if (ret != null) {
                Integer retInteger = Integer.valueOf(ret);
                if (retInteger < 0) {
                    onError(retInteger);
                } else {
                    if (isCache() && StrUtil.notEmptyOrNull(getId())) {
                        if (L.D) L.i("放入请求缓存, key = " + getId());
                        getReqCache().put(getId(), resultEx);
                    }
                    onResult(resultEx);
                }
            } else {
                if (L.D) {
                    L.e(ErrorCodeType.RET_NULL.strName());
                }
                onError(ErrorCodeType.RET_NULL.order());
            }
        } catch (NumberFormatException e) {
            // if (L.D) {
            // L.e(ErrorCodeType.REL_ILLEGAL.strName());
            // }
            onError(ErrorCodeType.REL_ILLEGAL.order());
            CheckedExceptionHandler.handleException(e);
        }
    }

    @Override
    public void onFailure(Throwable t, String strMsg) {

//        XUtil.debug("----onFailure>>>" + reqKey);
//        if (StrUtil.notEmptyOrNull(reqKey)) {
//            WeqiaApplication.getReqSet().remove(reqKey);
//        }
        if (clickView != null) {
            L.e("按钮复原------" + strMsg);
            clickView.setEnabled(true);
        }
        dismissDlg();
        if (L.D) {
            //org.apache.http.client.HttpResponseException: Request Entity Too Large
//            L.e(strMsg, t);
        }
        if (t != null && StrUtil.notEmptyOrNull(t.getMessage())
                && t.getMessage().contains("Connection reset by peer")) {
            L.i("关闭无用连接");
//            HttpUtil.getInstance().closeAllConnect();
        }
        if (WeqiaApplication.isLev) {
            return;
        }
        if (StrUtil.notEmptyOrNull(strMsg)) {
            if (strMsg.contains("error code:500")) {
                onError(ErrorCodeType.CLIENT_NOT_CONNECT.order());
            } else if (strMsg.equalsIgnoreCase(UtilsConstants.NETWORK_MSG)) {
                onError(ErrorCodeType.NETWORK_ERROR_NEW.order());
            } else {
//
//                if (t instanceof HttpResponseException) {
//                    HttpResponseException httpResponseException = (HttpResponseException) t;
//                    if (httpResponseException != null) {
//                        int statusCode = httpResponseException.getStatusCode();
//                        L.toastShort("网络请求失败，错误码：" + statusCode);
//                    }
//                }

                onError(ErrorCodeType.CLIENT_SYS_ERROR.order());
            }
        } else {
            onError(ErrorCodeType.NETWORK_ERROR.order());
        }

    }

    public abstract void onResult(ResultEx resultEx);

    public void onError(Integer errCode) {
        if (ctx == null) {
            ctx = WeqiaApplication.ctx;
        }
        if (ctx != null) {
            ErrorCodeType type = ErrorCodeType.valueOf(errCode);
            if (type != null) {
                if (type.order() == ErrorCodeType.CLIENT_NOT_CONNECT.order()) {
                    L.d(ErrorCodeType.CLIENT_NOT_CONNECT.strName());
                } else if (type.order() == ErrorCodeType.NETWORK_ERROR.order()) {
                    // L.toastShort(ErrorCodeType.NETWORK_ERROR.strName());
                } else if (type.order() == ErrorCodeType.CLIENT_SYS_ERROR.order()) {
                    // try {
                    // // 显示错误消息对话框
                    // DialogUtil.commonShowDialog(ctx, type.strName()).show();
                    // } catch (Exception e) {
                    // CheckedExceptionHandler.handleException(e);
                    // }

                } else if (type.order() == ErrorCodeType.NETWORK_ERROR_NEW.order()) {
                    L.d(ErrorCodeType.NETWORK_ERROR_NEW.strName());
                } else {
                    L.d(type.strName());
                }
            }
        }
    }

    public void onErrorMsg(Integer errCode, String errorMsg) {

    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public static LruMemoryCache<String, ResultEx> getReqCache() {
        if (reqCache == null) {
            reqCache = new LruMemoryCache<>(100);
        }
        return reqCache;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReqKey() {
        return reqKey;
    }

    public void setReqKey(String reqKey) {
        this.reqKey = reqKey;
    }


    public View getClickView() {
        return clickView;
    }

    public void setClickView(View clickView) {
        this.clickView = clickView;
    }
}
