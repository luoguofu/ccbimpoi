package com.weqia.wq.component.qr;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.weqia.utils.L;
import com.weqia.utils.MD5Util;
import com.weqia.utils.StrUtil;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.dialog.SharedCommonDialog;
import com.weqia.wq.R;
import com.weqia.wq.RouterUtil;
import com.weqia.wq.component.SelectArrUtil;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;
import com.weqia.wq.component.activity.SharedTitleActivity;
import com.weqia.wq.component.imageselect.ImageListActivity;
import com.weqia.wq.component.utils.DialogUtil;
import com.weqia.wq.component.utils.html.WebViewActivity;
import com.weqia.wq.component.utils.request.ResultEx;
import com.weqia.wq.component.utils.request.ServiceParams;
import com.weqia.wq.component.utils.request.ServiceRequester;
import com.weqia.wq.component.utils.request.UserService;
import com.weqia.wq.data.ComponentReqEnum;
import com.weqia.wq.data.EnumData;
import com.weqia.wq.data.base.WebViewData;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.global.ModulesConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dminter on 2017/1/11.
 */


public class QRScanActivity extends SharedDetailTitleActivity {
    private QRScanActivity ctx;
    private CaptureFragment captureFragment;
    public boolean isFlashlightOpen = false;
    private ImageView ivLight;
    private TextView tvLight;
    private static QRScanActivity instance;

    public static QRScanActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        instance = this;
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.ac_qrscan);
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.qr_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
        sharedTitleView.initTopBanner("二维码", "相册");
        ivLight = (ImageView) findViewById(R.id.capture_flashlight);
        tvLight = (TextView) findViewById(R.id.tvLight);
        ViewUtils.bindClickListenerOnViews(this, this, R.id.rlFlashlight);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalConstants.REQUESTCODE_GET_PIC) {
            if (SelectArrUtil.getInstance().getSelImgSize() == 0) {
                return;
            }
            String photoPath = SelectArrUtil.getInstance().getSelImg(0);
            if (StrUtil.notEmptyOrNull(photoPath)) {
                try {
                    CodeUtils.analyzeBitmap(photoPath, new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            qrSuccess(result);
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            qrFailed();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            SelectArrUtil.getInstance().clearImage();
            SelectArrUtil.getInstance().clearSourceImage();
        }
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            qrSuccess(result);
        }

        @Override
        public void onAnalyzeFailed() {
            qrFailed();
        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.topbanner_button_string_right) {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put(GlobalConstants.KEY_IMAGE_SELECT_SIZE, 1 + "");
            paramMap.put(GlobalConstants.KEY_SELECT_TYPE, EnumData.AttachType.PICTURE.value() + "");
            startToActivityForResult(ImageListActivity.class, paramMap, GlobalConstants.REQUESTCODE_GET_PIC);
        }
        if (v.getId() == R.id.rlFlashlight) {
            flashLight(isFlashlightOpen);
        }
    }

    private void flashLight(boolean isOpen) {
        if (isOpen) {
            tvLight.setText("打开闪光灯");
            ivLight.setImageResource(R.drawable.icon_sgd);
            CodeUtils.isLightEnable(false);
            isFlashlightOpen = false;
        } else {
            tvLight.setText("关闭闪光灯");
            ivLight.setImageResource(R.drawable.icon_sgd_sel);
            CodeUtils.isLightEnable(true);
            isFlashlightOpen = true;
        }
    }


    public static void qrFailed() {
        L.toastShort("解析二维码失败");
    }

    public void qrSuccess(final String result) {
        if (!StrUtil.notEmptyOrNull(result)) {
            return;
        }
        resultAction(result);
    }


    private void resultAction(String qrResult) {
        getCoInfoByQRCodeUrl(ctx, qrResult);
    }


    public static void getCoInfoByQRCodeUrl(final SharedDetailTitleActivity ctx, final String result) {
        if (StrUtil.isEmptyOrNull(result)) {
            return;
        }
        ServiceParams serviceParams = new ServiceParams(ComponentReqEnum.QR_SCAN.order());
        serviceParams.put("qrcode", result);
        UserService.getDataFromServer(true, serviceParams, new ServiceRequester(ctx) {
                    @Override
                    public void onResult(ResultEx resultEx) {
                        if (resultEx.isSuccess()) {
                            QRScanData data = resultEx.getDataObject(QRScanData.class);
                            if (data != null) {
                                L.e("二维码解析结果：" + data.toString());
                                if (data.getsType() == EnumData.QRResultType.CP_JOIN.value()) {
                                    RouterUtil.routerActionSync(ctx, null, "pvmain", "actoJoinProject", ModulesConstants.ROUTER_PARAM, data.getInfo());
                                } else if (data.getsType() == EnumData.QRResultType.UER.value()) {
                                    String mid = data.getsId();
                                    RouterUtil.routerActionSync(ctx, null, "pvcontact", "actomemberdetail", ModulesConstants.ROUTER_PARAM, mid);
                                } else if (data.getsType() == EnumData.QRResultType.MODE.value()) {
                                    toOpenModeAction(ctx, data.getInfo());
                                } else {
                                    toCommon(result, ctx);
                                }
                            } else {
                                toCommon(result, ctx);
                            }
                        }
                    }

                    @Override
                    public void onErrorMsg(Integer errCode, String errorMsg) {
                        if (StrUtil.notEmptyOrNull(errorMsg)) {
                            qrConfirm(ctx, errorMsg);
                        }
                    }
                }
        );
    }

    private static void toOpenModeAction(final SharedTitleActivity ctx, String info) {
        if (StrUtil.isEmptyOrNull(info)) {
            return;
        }
        QRModeData data = JSONObject.parseObject(info, QRModeData.class);
        if (data == null) {
            return;
        }
        if (data.getNeedPassword() == QRModeData.QRModeEnum.YES.getValue()) {
            newFoldDialog(ctx, data.getQrCodeId());
        } else {
            requestModeDetail(ctx, null, data.getQrCodeId());
        }
    }

    private static void requestModeDetail(final SharedTitleActivity ctx, String password, String qrCodeId) {
        ServiceParams params = new ServiceParams(3616);
        params.put("qrCodeId", qrCodeId);
        if (StrUtil.notEmptyOrNull(password)) {
            params.put("qrCodePassword", password);
        }
        UserService.getDataFromServer(params, new ServiceRequester() {
            @Override
            public void onResult(ResultEx resultEx) {
                if (resultEx.isSuccess()) {
                    ModeDetailData detailData = resultEx.getDataObject(ModeDetailData.class);
                    if (detailData != null) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("info", detailData.getInfo());
//                        map.put("floor", detailData.getFloor() + "");
//                        map.put("projectId", detailData.getProjectId());
//                        map.put("subProjectId", detailData.getSubProjectId());
                        map.put("nodeId", detailData.getNodeId());
                        map.put("type", detailData.getType() + "");
                        map.put("isMember", detailData.getIsMember() + "");
                        map.put("node", detailData.getNode());
//                        map.put("qrCodeId", detailData.getQrCodeId());
                        map.put("versionId", detailData.getVersionId());
//                        map.put("memo", detailData.getMemo());
                        map.put("cDate", detailData.getcDate() + "");
                        map.put("cId", detailData.getcId());
                        RouterUtil.routerActionSync(ctx, "pvmain", "acopenmode", map);
                    } else {
                        L.toastLong("解析数据出错！");
                    }
                }
            }

            @Override
            public void onError(Integer errCode) {
                super.onError(errCode);
                ctx.finish();
            }
        });
    }

    /**
     * 输入对话框
     */
    public static void newFoldDialog(final SharedTitleActivity ctx, final String qrCodeId) {
        SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(ctx);
        LayoutInflater mInflater = LayoutInflater.from(ctx);
        View view = mInflater.inflate(R.layout.view_new_fold, null);
        final EditText etInput = (EditText) view.findViewById(R.id.et_Input);
        etInput.setHint("请输入密码");
        builder.setTitle("密码");
        builder.showBar(false);
        builder.setTitleAttr(true, null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String input = etInput.getText().toString().trim();
                if (StrUtil.notEmptyOrNull(input)) {
                    input = MD5Util.md32(input);
                    requestModeDetail(ctx, input, qrCodeId);
                    dialog.dismiss();
                } else {
                    L.toastShort("密码不能为空，请输入密码！");
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ctx.finish();
            }
        });
        builder.setContentView(view);
        builder.create().show();
    }

    private static void toCommon(String result, SharedDetailTitleActivity ctx) {
        if (result.startsWith("http") || result.startsWith("www")) {
            Intent intent = new Intent(ctx, WebViewActivity.class);
            String appName = ctx.getResources().getString(R.string.app_name);
            intent.putExtra("WebViewData", new WebViewData(appName, result));
            ctx.startActivity(intent);
            ctx.finish();
        } else {
            ScanTools.toPageQRResukt(ctx, result);
        }
    }

    public static void qrConfirm(Context ctx, String what) {
        Dialog mDialog = DialogUtil.initCommonDialog(ctx, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case -2:
                        break;
                    case -1:
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        }, what);
        mDialog.show();
    }
}
