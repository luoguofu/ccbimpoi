package com.weqia.wq;

import android.content.Context;
import android.support.annotation.NonNull;

import com.spinytech.macore.MaApplication;
import com.spinytech.macore.router.LocalRouter;
import com.spinytech.macore.router.MaActionResult;
import com.spinytech.macore.router.RouterRequest;
import com.spinytech.macore.router.RouterRequestUtil;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by berwin on 2017/9/7.
 */

public class RouterUtil {

    /**
     * @param ctx
     * @param provider
     * @param action
     * @param successConsumer
     */
    public static void routerAction(Context ctx,
                                    @NonNull String provider, @NonNull String action,
                                    Consumer<MaActionResult> successConsumer) {
        routerAction(ctx, provider, action, null, successConsumer);
    }

    /**
     * @param ctx
     * @param provider
     * @param action
     * @param successConsumer
     * @param key
     * @param value
     */
    public static void routerAction(Context ctx,
                                    @NonNull String provider, @NonNull String action,
                                    String key, String value,
                                    Consumer<MaActionResult> successConsumer
    ) {
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put(key, value);
        routerAction(ctx, null, provider, action, dataMap, successConsumer);
    }

    /**
     * @param ctx
     * @param provider
     * @param action
     * @param successConsumer
     * @param dataMap
     */
    public static void routerAction(Context ctx,
                                    @NonNull String provider, @NonNull String action,
                                    HashMap<String, String> dataMap,
                                    Consumer<MaActionResult> successConsumer) {
        routerAction(ctx, null, provider, action, dataMap, successConsumer);
    }

    /**
     * @param ctx
     * @param domain
     * @param provider
     * @param action
     * @param successConsumer
     * @param dataMap
     */
    public static void routerAction(Context ctx, String domain,
                                    @NonNull String provider, @NonNull String action,
                                    HashMap<String, String> dataMap,
                                    Consumer<MaActionResult> successConsumer) {
        if (L.D) {
            String dataStr = "-";
            if (dataMap != null) dataStr = dataMap.toString();
            L.e("-----------------跳转 domain = [" + domain + "], provider = [" + provider + "], action = [" + action + "], 数据 = [" + dataStr + "]");
        }
        try {
            RouterRequest routerRequest;
            if (StrUtil.isEmptyOrNull(domain)) {
                routerRequest = RouterRequestUtil.obtain(ctx)
                        .provider(provider)
                        .action(action);
            } else {
                routerRequest = RouterRequestUtil.obtain(ctx)
                        .domain(domain)
                        .provider(provider)
                        .action(action)
                ;
            }
            if (dataMap != null)
                for (String key : dataMap.keySet()) {
                    routerRequest.data(key, dataMap.get(key));
                }
            if (successConsumer == null) {
                successConsumer = new Consumer<MaActionResult>() {
                    @Override
                    public void accept(MaActionResult maActionResult) throws Exception {
                        L.e("默认的成功consumer返回=[" + maActionResult.getMsg().toString() + "]");
                    }
                };
            }
            LocalRouter.getInstance(MaApplication.getMaApplication())
                    .rxRoute(ctx, routerRequest)
                    .subscribeOn(Schedulers.from(ThreadPool.getThreadPoolSingleton()))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(successConsumer, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            L.e("错误啦，需要查询下");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param ctx
     * @param provider
     * @param action
     */
    public static Observable<MaActionResult> routerActionSync(Context ctx,
                                        @NonNull String provider, @NonNull String action) {
        return routerActionSync(ctx, null, provider, action, null);
    }

    public static Observable<MaActionResult> routerActionSync(Context ctx, String domain,
                                        @NonNull String provider, @NonNull String action,
                                        String key, String value) {
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put(key, value);
        return routerActionSync(ctx, domain, provider, action, dataMap);
    }

    /**
     * @param ctx
     * @param provider
     * @param action
     * @param dataMap
     */
    public static Observable<MaActionResult> routerActionSync(Context ctx,
                                        @NonNull String provider, @NonNull String action,
                                        HashMap<String, String> dataMap) {
        return routerActionSync(ctx, null, provider, action, dataMap);
    }

    /**
     * @param ctx
     * @param domain
     * @param provider
     * @param action
     * @param dataMap
     */
    public static Observable<MaActionResult> routerActionSync(Context ctx, String domain,
                                                              @NonNull String provider, @NonNull String action,
                                                              HashMap<String, String> dataMap) {
        if (L.D) {
            String dataStr = "-";
            if (dataMap != null) dataStr = dataMap.toString();
            L.e("sync-----------------跳转 domain = [" + domain + "], provider = [" + provider + "], action = [" + action + "], 数据 = [" + dataStr + "]");
        }
        try {
            RouterRequest routerRequest;
            if (StrUtil.isEmptyOrNull(domain)) {
                routerRequest = RouterRequestUtil.obtain(ctx)
                        .provider(provider)
                        .action(action);
            } else {
                routerRequest = RouterRequestUtil.obtain(ctx)
                        .domain(domain)
                        .provider(provider)
                        .action(action)
                ;
            }
            if (dataMap != null)
                for (String key : dataMap.keySet()) {
                    routerRequest.data(key, dataMap.get(key));
                }
            return LocalRouter.getInstance(MaApplication.getMaApplication())
                    .rxRoute(ctx, routerRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
