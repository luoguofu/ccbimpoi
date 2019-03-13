package com.weqia.wq.component;

import com.alibaba.fastjson.JSONObject;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.wq.component.utils.request.ResultEx;
import com.weqia.wq.component.utils.request.ServiceParams;
import com.weqia.wq.component.utils.request.ServiceRequester;
import com.weqia.wq.component.utils.request.UserService;
import com.weqia.wq.data.LoginUserData;
import com.weqia.wq.data.WPf;
import com.weqia.wq.data.global.Hks;
import com.weqia.wq.data.global.WeqiaApplication;

/**
 * <br/>Author:hihiwjc
 * <br/>Email:hihiwjc@live.com
 * <br/>Date:2016/6/29 0029
 * <br/>Func:支付Util
 * <br/>1.获取订单金额、id、用户mid、支付渠道
 * <br/>2.提交1中的信息到服务器获取订单
 * <br/>3.获取到订单信息，通过ping++发起支付
 * <br/>4.获取支付结果做出相应的处理
 * <br/>5.文档中心<a href="http://pingxx.com/docs/index">Ping++</a>
 */

public class PaymentUtil {
    public static String PAYMENT_THIRD_TOKEN ="";

    /**
     * 初始化支付Token
     */
    public static void refreshPaymentToken(){
        String token= WPf.getInstance().get(Hks.app_payment_token,String.class);
        if (StrUtil.notEmptyOrNull(token)){
            PAYMENT_THIRD_TOKEN =token;
            return;
        }
        WeqiaApplication application = WeqiaApplication.getInstance();
        LoginUserData loginUser = application.getLoginUser();
        if (loginUser==null){//未登录时不请求
            return;
        }
        ServiceParams params=new ServiceParams(2217);
        UserService.getDataFromServer(false, params, new ServiceRequester() {
            @Override
            public void onResult(ResultEx resultEx) {
                if (!resultEx.isSuccess()){
                    return;
                }
                JSONObject obj= JSONObject.parseObject(resultEx.getObject());
                String token = obj.getString("token");
                if (StrUtil.notEmptyOrNull(token)){
                    WPf.getInstance().put(Hks.app_payment_token,token);
                    PAYMENT_THIRD_TOKEN =token;
                }
            }

            @Override
            public void onError(Integer errCode) {
                super.onError(errCode);
                if (L.D) L.e("获取支付Token出错！errCode="+errCode);
            }
        });
    }
}
