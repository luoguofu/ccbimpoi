package com.weqia.wq.component.file;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.weqia.utils.StrUtil;
import com.weqia.wq.R;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;
import com.weqia.wq.component.utils.html.WebViewUtil;
import com.weqia.wq.component.view.title_pop.TitleItem;
import com.weqia.wq.component.view.title_pop.TitlePopup;
import com.weqia.wq.data.AttachmentData;
import com.weqia.wq.data.EnumData;
import com.weqia.wq.data.TransData;
import com.weqia.wq.data.base.WebViewData;

/**
 * Created by MX on 2014/7/23.
 */
public class FileWebViewActivity extends SharedDetailTitleActivity {

    private WebViewData webViewData;
    private WebViewUtil webViewUtil;
    private WebView webView;
    private ImageView ivLoadError;
    private TitlePopup titlePopup = null;
    private boolean canDown = false;
    private Activity ctx;
    private AttachmentData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ctx = this;
        initView();
        if (webViewData != null && StrUtil.notEmptyOrNull(webViewData.getUrl())) {
            //清除缓存导致预览缓慢
//            getWebViewUtil().clearCache();
            getWebViewUtil().zoomControls(true);
            getWebViewUtil().initOriginalUrl(webViewData.getUrl());
            getWebViewUtil().loadContent(webViewData.getUrl());
        }
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            bundle = new Bundle();
        }
        webView = (WebView) findViewById(R.id.wb_webview);
        ivLoadError = (ImageView) findViewById(R.id.ivLoadError);
        webViewData = (WebViewData) bundle.getSerializable("WebViewData");
        data = (AttachmentData) bundle.getSerializable("attachmentData");
        canDown = bundle.getBoolean("canDown");
        sharedTitleView.initTopBanner(webViewData.getTitle(), R.drawable.selector_btn_details);
        sharedTitleView.getButtonLeft().setImageResource(R.drawable.btn_close_bold);
        sharedTitleView.getTextViewTitle().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                getWebViewUtil().reload();
                return true;
            }
        });
        if (canDown) {
            sharedTitleView.getButtonRight().setVisibility(View.VISIBLE);
        } else {
            sharedTitleView.getButtonRight().setVisibility(View.GONE);
        }
    }

    @Override
    protected void onClickDo(View v) {
        if (v == sharedTitleView.getButtonLeft()) {
            this.finish();
        } else if (v.getId() == R.id.topbanner_button_right) {
            titleOp(v);
        }
    }

    private void titleOp(View view) {
        titlePopup =
                new TitlePopup(this, ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        //仅预览不可转发和下载
        titlePopup.addAction(new TitleItem(this, "转发", null));
        titlePopup.addAction(new TitleItem(this, "其他应用打开", null));
        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(TitleItem item, int position) {
                switch (position) {
                    case 0:
                        //转发
                        TransData transData = new TransData();
                        transData.setOuter(false);
                        transData.setInsideData(data);
                        transData.setContentType(EnumData.MsgTypeEnum.FILE.value());
//                        WeqiaApplication.transData = transData;
//                        Intent intent = new Intent(ctx, OpenFileActivity.class);
//                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent = new Intent(ctx, FileScanActivity.class);
                        intent.putExtra("attachmentData", data);
                        intent.putExtra("canDown", canDown);
                        startActivity(intent);
                        break;
                }
            }
        });
        titlePopup.show(view);
    }

    @Override
    public void onBackPressed() {
        getWebViewUtil().onBackPressed();
    }

    private WebViewUtil getWebViewUtil() {
        if (webViewUtil == null) {
            webViewUtil = new WebViewUtil(this, webView, ivLoadError);
        }
        return webViewUtil;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.onPause();
        }
    }

}
