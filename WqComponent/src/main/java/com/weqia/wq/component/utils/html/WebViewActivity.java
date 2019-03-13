package com.weqia.wq.component.utils.html;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.ImageView;

import com.weqia.utils.StrUtil;
import com.weqia.wq.R;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;
import com.weqia.wq.data.WPfCommon;
import com.weqia.wq.data.base.WebViewData;
import com.weqia.wq.data.global.Hks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MX on 2014/7/23.
 */
public class WebViewActivity extends SharedDetailTitleActivity {

    // 隐藏操作按钮
    private boolean bHideMore = false;
    private boolean bKf = false; //小桩客服
    private WebViewData webViewData;
    private WebViewUtil webViewUtil;
    private WebView webView;
    private ImageView ivLoadError;
    private boolean isChangeTitleByPage = true;//是否根据网页更换页面标题，默认为是
    private boolean bZoomWebView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initView();
        if (webViewData != null && StrUtil.notEmptyOrNull(webViewData.getUrl())) {
            getWebViewUtil().initOriginalUrl(webViewData.getUrl());
            getWebViewUtil().loadContent(webViewData.getUrl());
            getWebViewUtil().zoomControls(bZoomWebView);
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
        bHideMore = bundle.getBoolean("bHideMore", false);
        isChangeTitleByPage = bundle.getBoolean("isChangeTitleByPage", true);
        bZoomWebView = bundle.getBoolean("bZoomWebView", false);
        bKf = bundle.getBoolean("bKf", false);
        if (bHideMore) {
            sharedTitleView.initTopBanner(webViewData.getTitle());
        } else if (bKf) {
            if (WPfCommon.getInstance().get(Hks.is_private_ip, Boolean.class, false)) {
                sharedTitleView.initTopBanner(webViewData.getTitle());
            } else {
                sharedTitleView.initTopBanner(webViewData.getTitle(), "联系客服");
            }
        } else {
            sharedTitleView.initTopBanner(webViewData.getTitle(), R.drawable.selector_btn_details);
        }
        // close_icon
        sharedTitleView.getButtonLeft().setImageResource(R.drawable.btn_close_bold);
        sharedTitleView.getTextViewTitle().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                getWebViewUtil().reload();
                return true;
            }
        });




    }

    @Override
    protected void onClickDo(View v) {
        if (v == sharedTitleView.getButtonLeft()) {
            // getWebViewUtil().removeWebView();
            this.finish();
        } else if (v.getId() == R.id.topbanner_button_right) {
            getWebViewUtil().showMenu(v);
        } else if (v.getId() == R.id.topbanner_button_string_right) {
            if (bKf) {
//                MemberData weqiaTeam = ContactUtil.getTeam();
//                if (weqiaTeam != null && StrUtil.notEmptyOrNull(weqiaTeam.getFriend_member_id())) {
//                    Intent newIntent = new Intent(WebViewActivity.this, TalkActivity.class);
//                    newIntent.putExtra("friend_id", weqiaTeam.getFriend_member_id());// 对方ID
//                    startActivity(newIntent);
//                    this.finish();
//                }
            }
        }
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
        if (webView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                webView.onResume();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                webView.onPause();
            }
        }
    }

    //    @Override
    //    protected void onStop() {
    //        if (webView != null) {
    //            webView.loadUrl("about:blank");//解决网页声音和视频不能停止的问题
    //        }
    //        super.onStop();
    //    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
//            webView.removeAllViews();
            if (bZoomWebView) {
                webView.setVisibility(View.GONE);
            }
            webView.destroy();
            webView = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                if (requestCode == 1012) {
                    if (webView != null) {
                        String mid = data.getStringExtra("mid");
                        String str = "javascript:document.getElementById(\"noInput\").value= '" + mid + "'";
//                        L.e("得到的JS链接为：" + str);
                        webView.loadUrl(str);
                    }
                }
            }
        }

        if(requestCode==FILECHOOSER_RESULTCODE)
        {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            }
            else  if (mUploadMessage != null) {
                Log.e("result",result+"");
                if(result==null){
//                   mUploadMessage.onReceiveValue(imageUri);
                    mUploadMessage.onReceiveValue(imageUri);
                    mUploadMessage = null;

                    Log.e("imageUri",imageUri+"");
                }else {
                    mUploadMessage.onReceiveValue(result);
                    mUploadMessage = null;
                }


            }
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    /**
     * 是否根据网页更换页面标题，默认为是
     */
    public boolean isChangeTitleByPage() {
        return isChangeTitleByPage;
    }

    public boolean isbZoomWebView() {
        return bZoomWebView;
    }






    public ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    public ValueCallback<Uri[]> mUploadCallbackAboveL;
    public final static int FILECHOOSER_RESULTCODE = 1;// 表单的结果回调
    public Uri imageUri;


    @SuppressWarnings("null")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE
                || mUploadCallbackAboveL == null) {
            return;
        }

        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                results = new Uri[]{imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();

                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }

                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        if(results!=null){
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        }else{
            results = new Uri[]{imageUri};
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        }

        return;
    }

    public void take(){
        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp");
        // Create the storage directory if it does not exist
        if (! imageStorageDir.exists()){
            imageStorageDir.mkdirs();
        }
        File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        imageUri = Uri.fromFile(file);

        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent i = new Intent(captureIntent);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            i.setPackage(packageName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntents.add(i);

        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        Intent chooserIntent = Intent.createChooser(i,"Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        startActivityForResult(chooserIntent,  FILECHOOSER_RESULTCODE);
    }


}
