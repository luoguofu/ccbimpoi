package com.weqia.wq.component.utils.html;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.umeng.socialize.media.UMImage;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.datastorage.file.PathUtil;
import com.weqia.utils.http.HttpUtil;
import com.weqia.utils.http.okgo.callback.StringCallback;
import com.weqia.wq.R;
import com.weqia.wq.component.activity.SharedTitleActivity;
import com.weqia.wq.component.db.WeqiaDbUtil;
import com.weqia.wq.component.utils.AttachUtils;
import com.weqia.wq.component.utils.DialogUtil;
import com.weqia.wq.component.utils.ShareUtil;
import com.weqia.wq.component.utils.request.UserService;
import com.weqia.wq.component.view.TitleView;
import com.weqia.wq.component.view.title_pop.TitleItem;
import com.weqia.wq.component.view.title_pop.TitlePopup;
import com.weqia.wq.data.AttachmentData;
import com.weqia.wq.data.EnumData;
import com.weqia.wq.data.EnumData.MsgTypeEnum;
import com.weqia.wq.data.TransData;
import com.weqia.wq.data.global.WeqiaApplication;
import com.weqia.wq.data.html.LinksData;
import com.weqia.wq.global.ComponentUtil;

import java.util.Timer;
import java.util.TimerTask;

public class WebViewUtil {

    private SharedTitleActivity ctx;
    private WebView webView;
    private ImageView ivLoadError;
    private LinksData linksData;
    private String curUrl;
    private String originalUrl;
    private TitlePopup titlePopup = null;
    private static WebViewUtil instanse;
    private HtmlFetchInterface htmlFetchInterface;
    private MyTask loadTask;
    private Timer loaderTimer = new Timer();
    boolean loadError = false;
    private Timer timeoutTimer;
    private Timer priseDataTimer;

    private long timeout = 30000;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (webView.getProgress() < 100) {
                    webwClient.onReceivedError(webView, -6, "timeout", curUrl);
                }
            } else if (msg.what == 2) {
                try {
                    if (priseDataTimer != null) {
                        priseDataTimer.cancel();
                        priseDataTimer.purge();
                    }
                } catch (IllegalStateException e) {
                }
                webwClient.onReceivedError(webView, -7, "loaderror", curUrl);
            }
        }
    };

    private WebViewUtil(SharedTitleActivity ctx) {
        this.ctx = ctx;
        initView();
    }

    public WebViewUtil(SharedTitleActivity ctx, WebView tmWebView, ImageView ivLoadError) {
        this.ctx = ctx;
        this.webView = tmWebView;
        this.ivLoadError = ivLoadError;
        initView();
    }

    public static WebViewUtil getInstanse(SharedTitleActivity ctx) {
        if (instanse == null) {
            instanse = new WebViewUtil(ctx);
        }
        return instanse;
    }

    public void reload() {
        this.webView.reload();
    }

    WebViewClient webwClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            L.e("++++++++++ " + url);
            super.onPageStarted(view, url, favicon);
            if (loadTask != null) {
                loadTask.cancel();
            }
            if (timeoutTimer != null) {
                timeoutTimer.cancel();
                timeoutTimer.purge();
            }
            mHandler.removeMessages(1);


            timeoutTimer = new Timer();
            TimerTask tt = new TimerTask() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                    timeoutTimer.cancel();
                    timeoutTimer.purge();
                }
            };
            timeoutTimer.schedule(tt, timeout, timeout);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (L.D) L.e("跳转的url = " + url);
            Uri tmpUri = Uri.parse(url);
            if (tmpUri != null) {
                if (tmpUri.getScheme().equalsIgnoreCase("http")
                        || tmpUri.getScheme().equalsIgnoreCase("https")) {
                    if (url.contains("114so")) {
                        if (ivLoadError != null) {
                            ivLoadError.setVisibility(View.VISIBLE);
                        }
                        webView.setVisibility(View.GONE);
                        if (getShareTitleView() != null) {
                            getShareTitleView().getPbTitle().setVisibility(View.INVISIBLE);
                        }
                        return true;
                    }
                    loadContent(url);
                    return true;
                } else {
                    if (url.startsWith("tel:")) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                        ctx.startActivity(intent);
                        return true;
                    }
                }
            }
            return super.shouldOverrideUrlLoading(view, url);
        }


        @Override
        public void onPageFinished(final WebView view, String url) {
//            L.e("---------- " + url);
            view.bringToFront();

            if (timeoutTimer != null) {
                timeoutTimer.cancel();
                timeoutTimer.purge();
            }
            if (loadTask != null)
                loadTask.cancel();

            if (loadError) {
                return;
            }

            loadTask = new MyTask(ctx, url);
            try {
                loaderTimer.schedule(loadTask, 1000);
            } catch (IllegalStateException e) {
                loaderTimer = new Timer();
                loaderTimer.schedule(loadTask, 1000);
            }

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (!loadError && view.getProgress() == 100) {
                        view.setVisibility(View.VISIBLE);
                    }
                }
            }, 5000);

            // super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description,
                                    String failingUrl) {
//            L.e("页面出错了 " + failingUrl);
            if (L.D) L.e(errorCode + "----" + description + " ++++" + failingUrl);
            view.stopLoading();
            try {
                loaderTimer.cancel();
                loaderTimer.purge();
                timeoutTimer.cancel();
                timeoutTimer.purge();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            view.setVisibility(View.GONE);
            if (ivLoadError != null) {
                ivLoadError.setVisibility(View.VISIBLE);
            }
            loadError = true;
            if (errorCode == -7 && ctx != null && htmlFetchInterface != null) {
                DialogUtil.initCommonDialog(ctx, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case -2:
                                htmlFetchInterface.fetchCancel();
                                instanse = null;
                                break;
                            case -1:
                                htmlFetchInterface.fetchError();
                                break;
                            default:
                                break;
                        }
                        dialog.dismiss();
                    }
                }, "网页抓取失败", "原文发送", "返回修改").show();
            } else {
                if (htmlFetchInterface != null) {
                    htmlFetchInterface.fetchError();
                }
            }
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setMessage("提示：SSL 出错!");
            builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }
    };

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void initView() {
        if (webView == null) {
            webView = new WebView(ctx);
        }
        if (ivLoadError != null) {
            ivLoadError.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webView.loadUrl(curUrl);
                    loadError = false;
                }
            });
        }

        webView.setWebViewClient(webwClient);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    if (getShareTitleView() != null) {
                        getShareTitleView().getPbTitle().setVisibility(View.INVISIBLE);
                    }
                } else {
                    if (getShareTitleView() != null) {
                        getShareTitleView().getPbTitle().setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);

                if (ctx instanceof WebViewActivity) {
                    WebViewActivity webViewActivity = (WebViewActivity) WebViewUtil.this.ctx;
                    if (!webViewActivity.isChangeTitleByPage()) {//某些情况下不需要根据网页更改标题
                        return;
                    }
                }

                if (StrUtil.notEmptyOrNull(view.getTitle())) {
                    if (getShareTitleView() != null) {
                        getShareTitleView().initTopBanner(view.getTitle());
                    }
                }
            }


            //android>5.0调用这个方法
            @Override
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                if (ctx instanceof WebViewActivity) {
                    WebViewActivity webViewActivity = (WebViewActivity) WebViewUtil.this.ctx;
                    webViewActivity.mUploadCallbackAboveL = filePathCallback;
                    webViewActivity.take();
                }
                return true;
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                if (ctx instanceof WebViewActivity) {
                    WebViewActivity webViewActivity = (WebViewActivity) WebViewUtil.this.ctx;
                    webViewActivity.mUploadMessage = uploadMsg;
                    webViewActivity.take();
                }
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                if (ctx instanceof WebViewActivity) {
                    WebViewActivity webViewActivity = (WebViewActivity) WebViewUtil.this.ctx;
                    webViewActivity.mUploadMessage = uploadMsg;
                    webViewActivity.take();
                }
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                if (ctx instanceof WebViewActivity) {
                    WebViewActivity webViewActivity = (WebViewActivity) WebViewUtil.this.ctx;
                    webViewActivity.mUploadMessage = uploadMsg;
                    webViewActivity.take();
                }

            }

        });
        webView.getSettings().setSupportZoom(true);
        //扩大比例的缩放
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setDomStorageEnabled(true);
        // webView.getSettings().setAppCacheEnabled(false);
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "localCall");
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {
                // L.toastShort("文件正在下载... ");
                String fileName = null;//"_";
                String result = contentDisposition;
                if (StrUtil.notEmptyOrNull(contentDisposition)) {
                    int location = result.indexOf("filename");
                    if (location >= 0) {
                        result = result.substring(location + "filename".length());
                        fileName = result.substring(result.indexOf("=") + 1);
                        fileName = fileName.replaceAll("\"", "");
                    }
                }
                if (StrUtil.isEmptyOrNull(fileName)) {
                    fileName = "_";
                    if (url.contains("/")) {
                        fileName = url.substring(url.lastIndexOf("/") + 1);
                    }
                }

                AttachmentData tmpData = new AttachmentData();
                tmpData.setUrl(url);
                tmpData.setDownloadType(EnumData.DownloadType.REAL.value());
                if (StrUtil.notEmptyOrNull(fileName)) {
                    tmpData.setName(fileName);
                }
                tmpData.setFileSize(String.valueOf(contentLength / 1024));
                tmpData.setPathRoot(PathUtil.getFilePath());
                // 附件列表
                AttachUtils.attachClick(ctx, tmpData, webView);
            }
        });

        titlePopup =
                new TitlePopup(ctx, ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        ComponentUtil.initWebViewPopData(ctx, titlePopup, new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(TitleItem item, int position) {

                switch (item.id.intValue()) {
                    case 0:
                        if (linksData != null) {
                            TransData transData = new TransData();
                            transData.setOuter(false);
                            transData.setTransText(linksData.getUrl());
                            transData.setInsideData(linksData);
                            transData.setContentType(MsgTypeEnum.LINK.value());
//                            WeqiaApplication.transData = transData;
//                            if (ctx instanceof SharedTitleActivity) {
//                                ((SharedTitleActivity) ctx).startToActivity(OpenFileActivity.class);
//                            }
                        }
                        break;
                    case 1:
                        if (linksData != null) {
//                            Intent intent = new Intent(ctx, WeBoAddActivity.class);
//                            intent.putExtra("LinksData", linksData.toString());
//                            ctx.startActivity(intent);
                        }
                        break;

                    case 2:
                        if (linksData != null) {
//                            Intent intent = new Intent(ctx, WcAddActivity.class);
//                            intent.putExtra("LinksData", linksData.toString());
//                            ctx.startActivity(intent);
                        }
                        break;
                    case 3:
                        if (linksData != null) {
                            shareTo(linksData);
                        }
                        break;
                    case 4:
                        String url = originalUrl;
                        if (linksData != null && StrUtil.notEmptyOrNull(linksData.getUrl())) {
                            url = linksData.getUrl();
                        }
                        if (StrUtil.notEmptyOrNull(url)) {
                            try {
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                Uri content_url = Uri.parse(url);
                                intent.setData(content_url);
                                ctx.startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                L.toastShort("未安装浏览器~");
                            }

                        }

                        break;
                    case 5:
                        String myUrl = originalUrl;
                        if (linksData != null && StrUtil.notEmptyOrNull(linksData.getUrl())) {
                            myUrl = linksData.getUrl();
                        }
                        if (StrUtil.notEmptyOrNull(myUrl)) {
                            StrUtil.copyText(myUrl);
                            L.toastShort("已复制到剪切板");
                        }
                        break;
                }
            }
        });
    }

    public void clearCache() {
        if (webView != null) {
            webView.clearCache(true);
            webView.clearHistory();
        }
    }


    public void zoomControls(boolean flag) {
        if (webView != null) {
            webView.getSettings().setBuiltInZoomControls(flag);
        }
    }


    public WebView getWebView() {
        return webView;
    }

    // public void setLoadInterface(HtmlLoadInterface loadInterface) {
    // this.loadInterface = loadInterface;
    // }

    /**
     * 显示操作菜单
     *
     * @param view
     */
    public void showMenu(View view) {
        titlePopup.show(view);
    }

    public void loadContent(String url) {
        if (StrUtil.isEmptyOrNull(url)) {
            return;
        }
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        curUrl = url;
        originalUrl = url;
        webView.loadUrl(url);
    }

    public void initOriginalUrl(String url) {
        this.originalUrl = url;
    }

    public void onBackPressed() {
        removeHandler();
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            if (ctx instanceof SharedTitleActivity) {
                ((SharedTitleActivity) ctx).finish();
            }
        }
    }

    private void removeHandler() {
        if (loadTask != null) {
            loadTask.cancel();
        }
        try {
            if (loaderTimer != null) {
                loaderTimer.cancel();
                loaderTimer.purge();
            }
        } catch (IllegalStateException e) {
        }

        try {
            if (timeoutTimer != null) {
                timeoutTimer.cancel();
                timeoutTimer.purge();
            }
        } catch (IllegalStateException e) {
        }

        try {
            if (priseDataTimer != null) {
                priseDataTimer.cancel();
                priseDataTimer.purge();
            }
        } catch (IllegalStateException e) {
        }

        if (mHandler != null) {
            mHandler.removeMessages(1);
            mHandler.removeMessages(2);
        }
    }

    public void removeWebView() {
        removeHandler();
        if (webView != null && webView.getParent() != null) {
            webView.stopLoading();
            ((ViewGroup) webView.getParent()).removeAllViews();
        } else {
            webView = null;
            instanse = null;
        }
    }

    protected TitleView getShareTitleView() {
        if (ctx instanceof WebViewActivity) {
            return ((WebViewActivity) ctx).sharedTitleView;
        } else {
            return null;
        }
    }

    public HtmlFetchInterface getHtmlFetchInterface() {
        return htmlFetchInterface;
    }

    public void setHtmlFetchInterface(HtmlFetchInterface htmlFetchInterface) {
        this.htmlFetchInterface = htmlFetchInterface;
    }

    final class InJavaScriptLocalObj {

        /**
         * 供外部调用的商商支付接口
         */
        @JavascriptInterface
        public void needMsg(String content) {
            if (StrUtil.notEmptyOrNull(content)) {
//                Intent newIntent = new Intent(ctx, QRScanActivity.class);
//                ctx.startActivity(newIntent);
            }
        }

        @JavascriptInterface
        public void showSource(final String content) {

            ctx.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        if (priseDataTimer != null) {
                            priseDataTimer.cancel();
                            priseDataTimer.purge();
                        }
                    } catch (IllegalStateException e) {
                    }
                    if (StrUtil.notEmptyOrNull(content)) {
                        String fcontent =
                                content.replaceAll("&lt;", "<").replaceAll("&quot;", "'")
                                        .replaceAll("&gt;", ">").replaceAll("amp;", "")
                                        .replaceAll("&nbsp;", " ");
                        linksData = JSON.parseObject(fcontent, LinksData.class);
                        if (linksData != null) {
                            if (StrUtil.isEmptyOrNull(linksData.getUrl())) {
                                linksData.setUrl(curUrl);
                            }
                        }
                    }
                    if (htmlFetchInterface != null) {
                        htmlFetchInterface.fetchComplete(linksData);
                    }
                    WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
                    if (dbUtil != null && linksData != null) {
                        dbUtil.save(linksData, true);
                    }
                }
            });
        }
    }

    private class MyTask extends TimerTask {
        private SharedTitleActivity ctx;
        private String url;

        // private WebView taskWebView;

        public MyTask(SharedTitleActivity ctx, String url) {
            this.ctx = ctx;
            this.url = url;
            // this.taskWebView = webView;
        }

        @Override
        public void run() {
            ctx.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (L.D) L.e("解析最后的地址" + url);
                    String jshttpStr = UserService.httpServ + "/phone/js/load.js";
                    if (L.D) L.e(jshttpStr);
                    HttpUtil.getInstance().get(jshttpStr, new StringCallback() {
                        @Override
                        public void onSuccess(String s) {
                            if (L.D) L.i("成功啦，用网上的");
                            String jsUrl = "javascript:" + s;
                            webView.loadUrl(jsUrl);

                            if (htmlFetchInterface != null) {
                                priseDataTimer = new Timer();
                                TimerTask tt = new TimerTask() {
                                    @Override
                                    public void run() {
                                        Message msg = new Message();
                                        msg.what = 2;
                                        mHandler.sendMessage(msg);
                                        priseDataTimer.cancel();
                                        priseDataTimer.purge();
                                    }
                                };
                                priseDataTimer.schedule(tt, 3000, 2000);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable, String s) {
                            if (L.D) L.i("加载失败，从本地加载");
                            String jsUrl = "javascript:" + StrUtil.getFromRaw(ctx, R.raw.load);
                            webView.loadUrl(jsUrl);
                        }
                    });
                }
            });
        }
    }


    private void shareTo(final LinksData data) {
        if (data == null) {
            return;
        }
        String img = data.getImage();
        if (StrUtil.notEmptyOrNull(img) && !img.startsWith("http")
                && StrUtil.notEmptyOrNull(data.getRimage())) {
            img = data.getRimage();
        }
        L.e(img);
        String shareUrl = data.getUrl();
        if (StrUtil.notEmptyOrNull(img)) {
            // Bitmap bitmap = ctx.getBitmapUtil().loadImageSync(img);

            // if (bitmap != null) {
            ShareUtil.getInstance(ctx).share(ctx, data.getTitle(), data.getContent(),
                    new UMImage(ctx, img), shareUrl);
            // } else {
            // ShareUtil.getInstance(ctx).share(ctx, data.getTitle(), data.getContent(), shareUrl);
            // }
        } else {
            ShareUtil.getInstance(ctx).share(ctx, data.getTitle(), data.getTitle(), shareUrl);
        }
    }

}
