package com.weqia.wq;

import android.content.Context;

import com.weqia.wq.global.ModulesConstants;

/**
 * Created by berwin on 2017/9/13.
 */

public class QuickRouterUtil {

    public static void getUnArrived(Context ctx, Integer btype){
        RouterUtil.routerActionSync(ctx, null,"pvremotemsg", "acgetmsg", ModulesConstants.ROUTER_PARAM, btype == null?null:btype.toString());
    }
}
