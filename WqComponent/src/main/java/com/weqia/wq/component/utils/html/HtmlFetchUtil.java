package com.weqia.wq.component.utils.html;

import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.TimeUtils;
import com.weqia.wq.component.activity.SharedTitleActivity;
import com.weqia.wq.component.db.WeqiaDbUtil;
import com.weqia.wq.data.global.WeqiaApplication;
import com.weqia.wq.data.html.LinksData;

public class HtmlFetchUtil {

    /**
     * webview 加载完成后解析是不需要ctx, null则是调用本地的
     * 
     * @param ctx
     * @param url
     * @param fetchInterface
     * @return
     */
    public static void getHtmlLinkData(final SharedTitleActivity ctx, String url,
            final HtmlFetchInterface fetchInterface) {
        LinksData linksData = null;
        boolean haveData = false;
        if (StrUtil.notEmptyOrNull(url)) {
            if (!url.startsWith("http")) {
                url = "http://" + url;
            }
            // 从本地加载
            linksData = getLocalLinkData(url);
            if (linksData == null && ctx != null) {
                final String loadUrl = url;
                ctx.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        final WebViewUtil webViewUtil = WebViewUtil.getInstanse(ctx);
                        webViewUtil.setHtmlFetchInterface(fetchInterface);
                        webViewUtil.loadContent(loadUrl);
                    }
                });
            } else {
                haveData = true;
            }
        } else {
            haveData = true;
        }

        if (fetchInterface != null && haveData) {
            if (fetchInterface != null) {
                fetchInterface.fetchComplete(linksData);
            }
        }
    }

    public static LinksData getLocalLinkData(String url) {
        LinksData urlLinkData = null;
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (StrUtil.notEmptyOrNull(url) && dbUtil != null) {
            urlLinkData = dbUtil.findById(url, LinksData.class);
            if (urlLinkData != null) {
                if (System.currentTimeMillis() - urlLinkData.getcDate() < TimeUtils.ONE_DAY) {
                    if (L.D) L.e("从数据库获取linkdata");
                } else {
                    if (L.D) L.e("linkdata 过期，删除");
                    dbUtil.deleteById(LinksData.class, url);
                    urlLinkData = null;
                }
            }
        }
        return urlLinkData;
    }
}
