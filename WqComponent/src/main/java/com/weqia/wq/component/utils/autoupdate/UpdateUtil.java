package com.weqia.wq.component.utils.autoupdate;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

import com.weqia.utils.DeviceUtil;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.TimeUtils;
import com.weqia.utils.datastorage.file.PathUtil;
import com.weqia.wq.component.AttachService;
import com.weqia.wq.component.notification.NotificationHelper;
import com.weqia.wq.component.receiver.AttachMsgReceiver;
import com.weqia.wq.component.utils.DialogUtil;
import com.weqia.wq.component.utils.LnUtil;
import com.weqia.wq.component.utils.request.ResultEx;
import com.weqia.wq.component.utils.request.ServiceParams;
import com.weqia.wq.component.utils.request.ServiceRequester;
import com.weqia.wq.component.utils.request.UserService;
import com.weqia.wq.data.AttachmentData;
import com.weqia.wq.data.ComponentReqEnum;
import com.weqia.wq.data.EnumData;
import com.weqia.wq.data.WPf;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.data.global.Hks;
import com.weqia.wq.data.global.WeqiaApplication;

import java.io.File;

/**
 * <br/>Author:hihiwjc
 * <br/>Email:hihiwjc@live.com
 * <br/>Date:2016/7/28 0028
 * <br/>Func:流程：1.检查更新-->2.弹出提示框-->下载apk文件-->安装apk
 */

public class UpdateUtil implements DialogInterface.OnClickListener {

    public static final int UPDATE_NOTIFICATION_ID = 0x2356;
    public static final String UPDATE_TYPE_ANDROID = "1";
    private Activity mActivity;
    private VersionData mVersionData;
    private Dialog updateDialog;
    private SharedUpdateProgressDialog progressDialog;
    private boolean isApkDownloading = false;
    private static final int FREQUENCY_OF_CHECK_UPDATE = 1;//检查更新的频率，单位为：天,默认为1天;
    private int currentVersionCode;
    private int latestVersionCode;

    private boolean isAboutUI = false;

    private UpdateUtil() {
    }

    public static UpdateUtil create() {
        return new UpdateUtil();
    }

    public UpdateUtil init(Activity activity) {
        mActivity = activity;
        currentVersionCode = DeviceUtil.getVersionCode(mActivity);
        return this;
    }

    private void unInit() {
        WeqiaApplication.getInstance().unregisterReceiver(downReceiver);
    }

    /**
     * 检查App更新
     * @param isIgnoreLastTime 是否忽略上次检查更新的时间直接检查更新
     */
    public void checkAppUpdate(boolean isIgnoreLastTime, final boolean wantToast) {

        Long lastCheckTime = WPf.getInstance().get(Hks.last_check_update_time, Long.class);
        if (lastCheckTime != null && !isIgnoreLastTime) {
            long dayOver = TimeUtils.getDayOver(-1 * FREQUENCY_OF_CHECK_UPDATE);
            if (lastCheckTime >= dayOver) {
                L.e("如果上次检查更新的时间小于等于频率时间之前，则检查更新，否则不执行");
                return;
            }
        }
        if (isApkDownloading) {
            showProgressDialog();
            return;
        }
        WPf.getInstance().put(Hks.last_check_update_time, System.currentTimeMillis());//保存此次检查更新的时间
        ServiceParams params = new ServiceParams(ComponentReqEnum.APP_UPDATE.order());
        params.put("currentVersionCode", currentVersionCode + "");
        params.put("type", UPDATE_TYPE_ANDROID);
        UserService.getDataFromServer(params, new ServiceRequester() {
            @Override
            public void onResult(ResultEx resultEx) {
                if (resultEx == null) {
                    return;
                }
                if (!resultEx.isSuccess()) {
                    return;
                }
                mVersionData = resultEx.getDataObject(VersionData.class);
                verifyNeedUpdate(wantToast);
            }

            @Override
            public void onError(Integer errCode) {
                super.onError(errCode);
//                L.toastShort("检查更新出错！");
                /*initVersionData();
                verifyNeedUpdate();*/
            }
        });
    }

    /**
     * 校验是否需要更新
     */
    private void verifyNeedUpdate(boolean wantToast) {
//        if (mActivity instanceof ) {
//            isAboutUI = false;
//        } else {
        isAboutUI = wantToast;
//        }
        if (mVersionData == null) {
            if (isAboutUI) {
                //首页自动检查更新时不提示此错误
                L.toastShort("当前已是最新版本");
            }
            return;
        }
        latestVersionCode = mVersionData.getVersionCode();
        int currentVersionCode = DeviceUtil.getVersionCode(mActivity);
        int savedLatestVersionCode = WPf.getInstance().get(Hks.last_check_update_version_code, Integer.class);
        if (savedLatestVersionCode >= latestVersionCode) {
            return;
        }
        if (latestVersionCode <= currentVersionCode || mVersionData.getDetectionUpgrade() == 2) {//判断版本号||2不检查更新【有新版本，不检查更新】
            if (isAboutUI) {
                //首页自动检查更新时不提示此错误
                L.toastShort("当前已是最新版本");
            }
            return;
        }
        doUpdateTipAction();
    }

    /**
     * 判断升级的提示方式
     */
    private void doUpdateTipAction() {
        showUpdateDialog();
    }

    private void showUpdateNotification() {
        String msgTitle = "BIM360版本更新提示";
        String msgContent = "发现最新版本,点击此处查看";
        NotificationHelper.tipNotify(mActivity, msgTitle, msgContent, false, UPDATE_NOTIFICATION_ID, false, NotificationHelper.PendingIntentEnum.MAIN);
    }

    /**
     * 显示更新Dialog
     */
    private void showUpdateDialog() {
        if (!isRunningForeground()) {//不前台运行则不显示对话框
            return;
        }
        if (mVersionData == null) {
            return;
        }
        String content = mVersionData.getVersionContent();
        String versionName = mVersionData.getVersionName();
        String fileSize = mVersionData.getSize() + "";
        boolean isCoercivenessUpgrade = mVersionData.getCoercivenessUpgrade(currentVersionCode);
        updateDialog = DialogUtil.initUpdateDialog(mActivity, this, content, versionName, fileSize, isCoercivenessUpgrade);
        updateDialog.setCancelable(!isCoercivenessUpgrade);//判断是否为强制升级，强制升级对话框不能取消
        updateDialog.show();
    }

    private void showProgressDialog() {
        if (mVersionData == null) {
            return;
        }
        boolean isCoercivenessUpgrade = mVersionData.getCoercivenessUpgrade(currentVersionCode);
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog = null;
        }
        progressDialog = DialogUtil.initUpdateProgressDialog(mActivity, isCoercivenessUpgrade);
        progressDialog.show();
    }

    /**
     * 判断程序是否在前台运行
     */
    private boolean isRunningForeground() {
        ActivityManager am = (ActivityManager) mActivity.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        return !StrUtil.isEmptyOrNull(currentPackageName) && currentPackageName.equals(mActivity.getPackageName());

    }

    /**
     * 判断apk文件是不是存在
     */
    private boolean isApkFileValid() {
        String localPath = LnUtil.getLocalpath(mVersionData.getDownloadUrl(), EnumData.AttachType.FILE.value());
        if (StrUtil.isEmptyOrNull(localPath)) {
            return false;
        }
        File apkFile = new File(localPath);
        if (!apkFile.exists()) {
            return false;
        }
        if (apkFile.length() < 1) {
            return false;
        }
        long dayOver = TimeUtils.getDayOver(-1);
        long lastModified = apkFile.lastModified();//文件的最后修改时间为一天之内则认为是有效的
        boolean result = lastModified >= dayOver;
        if (!result) {
            apkFile.delete();
        }
        return result;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dismissUpdateDialog();
        switch (which) {
            case Dialog.BUTTON_POSITIVE://确定
                onUpdateNowClick();
                if (!isAboutUI) {
                    WPf.getInstance().put(Hks.last_check_update_version_code, latestVersionCode);//保存此次检查更新的时间
                }
                break;
            case Dialog.BUTTON_NEGATIVE://取消

                dialog.dismiss();
                break;
            case Dialog.BUTTON_NEUTRAL://中立
                onUpdateNowClick();
                break;
        }
    }

    private void onUpdateNowClick() {
        if (isApkDownloading) {
            showProgressDialog();
            return;
        }
        if (!isApkFileValid()) {
            startDownLoadApk();
        } else {
            installApk();
        }
    }

    /**
     * 开始下载Apk文件
     */
    private void startDownLoadApk() {
        if (mVersionData == null) {
            return;
        }
        L.toastShort("已经开始下载更新包！");
        String fileName = "_";
        String apkUrl = mVersionData.getDownloadUrl();
        /*long size = mVersionData.getSize();
        if (size < 1) {//文件大小判断
            return;
        }*/
        if (StrUtil.isEmptyOrNull(apkUrl)) {
            L.toastShort("下载地址出错~");
            return;
        }
        if (apkUrl.contains("/")) {
            fileName = apkUrl.substring(apkUrl.lastIndexOf("/") + 1);
        }
        AttachmentData tmpData = new AttachmentData();
        tmpData.setUrl(apkUrl);
        tmpData.setDownloadType(EnumData.DownloadType.REAL.value());
        if (StrUtil.notEmptyOrNull(fileName)) {
            tmpData.setName(fileName);
        }
        //tmpData.setSize(String.valueOf(size / 1024));
        tmpData.setPathRoot(PathUtil.getFilePath());
        //注册进度监听器
        IntentFilter filter = new IntentFilter();
        filter.addAction(GlobalConstants.DOWNLOAD_COUNT_SERVICE_NAME);
        filter.setPriority(Integer.MAX_VALUE);
        WeqiaApplication.getInstance().registerReceiver(downReceiver, filter);//接收器
        // 附件列表
        Intent intent = new Intent(mActivity, AttachService.class);
        intent.putExtra(GlobalConstants.KEY_ATTACH_OP, tmpData);
        mActivity.startService(intent);
        isApkDownloading = true;
        showProgressDialog();
    }

    /**
     * Apk下载完成
     */
    private void onApkDownloaded() {
        isApkDownloading = false;
        dismissUpdateDialog();
        unInit();
        installApk();
    }

    private void dismissUpdateDialog() {
        if (updateDialog != null && updateDialog.isShowing()) {
            updateDialog.dismiss();
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /**
     * 安装apk
     */
    private void installApk() {
        if (!isApkFileValid()) {
            L.toastShort("更新包文件丢失，请重新检查更新！");
            return;
        }
        dismissUpdateDialog();
        String saveFileName = LnUtil.getLocalpath(mVersionData.getDownloadUrl(), EnumData.AttachType.FILE.value());
        if (StrUtil.isEmptyOrNull(saveFileName)) {
            L.toastShort("更新包文件丢失，请重新检查更新！");
            return;
        }
        File apkFile = new File(saveFileName);
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkFile.getAbsolutePath()), "application/vnd.android.package-archive");
        WeqiaApplication.getInstance().startActivity(intent);
    }

    private AttachMsgReceiver downReceiver = new AttachMsgReceiver() {

        @Override
        public void downloadCountReceived(Intent intent) {
            if (intent != null) {
                String urlStr = intent.getStringExtra(GlobalConstants.KEY_DOWN_ID);
                if (!urlStr.equals(mVersionData.getDownloadUrl())) {
                    return;
                }
                String downPercent = intent.getStringExtra(GlobalConstants.KEY_DOWN_PERCENT);
                Boolean bComplete =
                        intent.getBooleanExtra(GlobalConstants.KEY_DOWN_COMPLETE, false);
                File downFile = (File) intent.getSerializableExtra(GlobalConstants.KEY_DOWN_FILE);
                if (StrUtil.isEmptyOrNull(urlStr) || StrUtil.isEmptyOrNull(downPercent)) {
                    return;
                }

                // tvPercent.setText("加载中... " + downPercent);
                downPercent = downPercent.replace("%", "");
                double dPercent = Double.parseDouble(downPercent);
                int progress = (int) dPercent;
                if (!bComplete && progress == 100) {
                    return;
                }
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.setTitle("更新包下载中");
                    double rate = mVersionData.getSize() * dPercent / 100d;
                    String format = String.format("%.2f", rate);
                    String fileSizeRate = format + "MB/" + mVersionData.getSize() + "MB";
                    progressDialog.updateProgress(progress, downPercent + "%", fileSizeRate);
                }
                if (bComplete) {
                    onApkDownloaded();
                }
            }
        }
    };
}
