package com.weqia.wq.component;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.spinytech.macore.MaApplication;
import com.weqia.BitmapInit;
import com.weqia.data.UtilData;
import com.weqia.utils.L;
import com.weqia.utils.MD5Util;
import com.weqia.utils.RefreshObjEvent;
import com.weqia.utils.StrUtil;
import com.weqia.utils.bitmap.BitmapDecoder;
import com.weqia.utils.data.LocalNetPath;
import com.weqia.utils.datastorage.file.FileUtil;
import com.weqia.utils.datastorage.file.PathUtil;
import com.weqia.utils.exception.CheckedExceptionHandler;
import com.weqia.utils.http.okgo.OkGo;
import com.weqia.utils.http.okgo.callback.FileCallback;
import com.weqia.utils.http.okserver.download.DownErrException;
import com.weqia.utils.http.okserver.download.DownloadManager;
import com.weqia.utils.http.okserver.download.DownloadPress;
import com.weqia.wq.RouterUtil;
import com.weqia.wq.component.db.WeqiaDbUtil;
import com.weqia.wq.component.utils.FileMiniUtil;
import com.weqia.wq.component.utils.GlobalUtil;
import com.weqia.wq.component.utils.LnUtil;
import com.weqia.wq.component.utils.WaitSendUtils;
import com.weqia.wq.component.utils.request.ResultEx;
import com.weqia.wq.component.utils.request.ServiceParams;
import com.weqia.wq.component.utils.request.ServiceRequester;
import com.weqia.wq.component.utils.request.UserService;
import com.weqia.wq.data.AttachmentData;
import com.weqia.wq.data.ComponentReqEnum;
import com.weqia.wq.data.EnumData.AttachType;
import com.weqia.wq.data.EnumData.DataStatusEnum;
import com.weqia.wq.data.EnumData.DownloadType;
import com.weqia.wq.data.EnumData.MsgSendStatusEnum;
import com.weqia.wq.data.PercentData;
import com.weqia.wq.data.UpAttachData;
import com.weqia.wq.data.UpAttachParams;
import com.weqia.wq.data.UpFileData;
import com.weqia.wq.data.WPf;
import com.weqia.wq.data.WaitSendData;
import com.weqia.wq.data.WaitUpFileData;
import com.weqia.wq.data.base.MpIdData;
import com.weqia.wq.data.eventbus.RefreshEvent;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.data.global.WeqiaApplication;
import com.weqia.wq.data.push.PushData;
import com.weqia.wq.global.ComponentUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class ModeFileService extends Service {


    private static ModeFileService attachService;
    private static Map<String, UpFileData> uploadmMap;

    /**
     * 临时存放语音时间
     */
    public static Integer playTime;


    public static String keyUpDowmAttch = null;
    public static final int LOCAL_DOWNLOAD_FILE_SUCCESS = -5;

    @Override
    public void onCreate() {
        super.onCreate();
        attachService = this;
        if (L.D)
            L.e("发送服务启动");
        //注册EventBus
        EventBus.getDefault().register(this);
        DownloadManager.getInstance();
        GlobalUtil.resetSendingStatus();
    }

    public static ModeFileService getInstance() {
        return attachService;
    }

    // Handler类
    class MyHandler extends Handler {
        public MyHandler() {

        }

        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {// 接收message发过来的消息
            Intent intent = (Intent) msg.obj;
            if (intent != null && intent.getExtras() != null) {
                List<AttachmentData> data = // 这里的到由新建任务传递过来的要上传的文件的数据
                        (List<AttachmentData>) intent.getExtras().getSerializable(GlobalConstants.KEY_ATTACH_OP);
                ServiceParams params = (ServiceParams) intent.getExtras().getSerializable("ServiceParams");
                if (data != null) {
/*                    if (data instanceof AttachmentData) {
                        AttachmentData attachmentData = (AttachmentData) data;
                        if (L.D)
                            L.e("[下载任务] " + attachmentData.getUrl());
                        downloadAttach(attachmentData, true);
                    } else if (data instanceof WaitSendData) {
                        WaitSendData sendData = (WaitSendData) data;
                        WeqiaApplication.getInstance().getgSendingIds().add(sendData.getgId());
                        WaitSendUtils.doSend(sendData, params);
                    }*/
                    for (AttachmentData attachmentData : data) {
                        if (L.D)
                            L.e("[下载任务] " + attachmentData.getUrl());
                        downloadAttach(attachmentData, true);
                    }
                    if (L.D) {
                        L.e("start send Service;"); // 开始发送服务
                    }
                }
            }
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        HandlerThread handlerThread = new HandlerThread("handler_thread");
        handlerThread.start();
        MyHandler myHandler = new MyHandler(handlerThread.getLooper());
        Message msg = myHandler.obtainMessage();
        msg.obj = intent;
        msg.sendToTarget();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        L.e("attachService Destory");
        EventBus.getDefault().unregister(this);
        stopForeground(true);
//        Intent intent = new Intent(PunchNotificationReceiver.ACT_RESTART);
//        sendBroadcast(intent);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(RefreshObjEvent event) {
        int type = event.type;
        if (type == OkGo.DOWNLOAD_PROGRESS) {
            UtilData baseData = event.getObj();
            if (baseData != null && baseData instanceof DownloadPress) {
                executeListener((DownloadPress) baseData);
            }
        } else if (type == BitmapInit.AVATAR_DOWNLOAD_SUCCESS) {
            //RefreshEnum.DISCUSS_AVATAR.getValue()
            EventBus.getDefault().post(new RefreshEvent(4));
        }
    }

    private void executeListener(DownloadPress info) {
        FileCallback listener = info.getListener();
        if (listener == null) {
            L.e("没有监听，直接退出噢");
            return;
        }
        int state = info.getState();
        switch (state) {
            case DownloadManager.NONE:
            case DownloadManager.WAITING:
            case DownloadManager.DOWNLOADING:
            case DownloadManager.PAUSE:
                listener.downloadProgress(info.getDownloadLength(), info.getTotalLength(), info.getProgress(), info.getNetworkSpeed());
                break;
            case DownloadManager.FINISH:
                listener.downloadProgress(info.getDownloadLength(), info.getTotalLength(), info.getProgress(), info.getNetworkSpeed());
                //结束前再次回调进度，避免最后一点数据没有刷新
                listener.onSuccess(new File(info.getTargetPath()));
                break;
            case DownloadManager.ERROR:
                listener.downloadProgress(info.getDownloadLength(), info.getTotalLength(), info.getProgress(), info.getNetworkSpeed());
                //结束前再次回调进度，避免最后一点数据没有刷新
                L.e(info.getErrorMsg());
                listener.onError(null, null, info.getE());
                break;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // ///////////////////////////////////////// 下载 //////////////////////////
//    private void downloadAttach(final AttachmentData data) {
//        downloadAttach(data, false);
//    }

    private void downloadAttach(final AttachmentData data, boolean isPause) {
        // 得到真实下载路径
        String downName = data.getName();
        String realUrl = "";
        if (data.getType() == AttachType.PICTURE.value())
            realUrl = UserService.getBitmapUrl(data.getUrl());
        else {
            String key = data.getUrl();
            boolean convered = false;
            if (key.contains(GlobalConstants.BUCKET_SPIT)) {
                String[] tmpStrs = key.split(GlobalConstants.BUCKET_SPIT);
                if (tmpStrs.length == 4) {
                    L.e("漏网之鱼需要转化，代码排查下，出错啦");
                }
            }
            if (!convered)
                realUrl = UserService.getDownloadUrl(data.getUrl());
        }
        if (StrUtil.isEmptyOrNull(realUrl)) {
            downloadFileFailed(data, new DownErrException("真实下载地址为空"));
        }
        // 1-文件，2-文档
        if (data.getDownloadType() != null && data.getDownloadType().intValue() == DownloadType.REAL.value().intValue()) {
            realUrl = data.getUrl();
        }
        data.setPercentStr("0%");
        WeqiaApplication.getInstance().getDbUtil().save(data);
        if (StrUtil.isEmptyOrNull(realUrl)) {
            return;
        }
        String filePath;
        String _url = data.getUrl();
        String downLoadPath;
        if (StrUtil.notEmptyOrNull(data.getProject_id())) {
            //下载文件- 存储到
            downLoadPath = PathUtil.getWQPath();
        } else {
            //缓存文件-存储到bim360/file/文件加下
            if (StrUtil.notEmptyOrNull(data.getVersionId())) {
                downLoadPath = PathUtil.getFilePath() + File.separator + MD5Util.md32(data.getVersionId());
            } else {
                downLoadPath = PathUtil.getFilePath() + File.separator + data.getModeName();
            }

        }
        if (StrUtil.notEmptyOrNull(_url) && _url.contains(GlobalConstants.BUCKET_SPIT)) {
/*            //模型文件----需要加versionId做唯一路径
            _url = _url.replace(GlobalConstants.BUCKET_SPIT, "");
            if (_url.contains("/")) {
                _url = data.getUrl().replaceAll("/", "_");
            }

            int a = _url.lastIndexOf(".");
            if (a == -1) {
                filePath = downLoadPath + File.separator + MD5Util.md32(_url);
            } else {
                filePath = downLoadPath + File.separator + MD5Util.md32(_url.substring(0, a)) + _url.substring(a);
            }*/
            filePath = downLoadPath + File.separator + data.getName();
        } else {
            //普通文件-之前的逻辑
            if (StrUtil.notEmptyOrNull(downName)) {
                filePath = downLoadPath + File.separator + downName;
            } else {
                filePath = downLoadPath + File.separator;
            }
        }
        final String pjId = data.getProject_id();
        Log.e("ModeFileService", "ModeFileService: down");
        UserService.getHttpUtil().download(realUrl, filePath, data.getUrl(), new FileCallback() {

            @Override
            public void onSuccess(File file) {
                Log.e("onSuccess", "onSuccess: down");
                data.setProject_id(pjId);
                downloadFileSuccess(data, file);
            }

            @Override
            public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
                downloadFileLoading(data, progress);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                downloadFileFailed(data, e);
            }
        });
    }

    public void downloadFileFailed(final AttachmentData data, Exception e) {
        MaApplication.getMaApplication().onDownloadFileOp(data.toString(), -1);
        if (L.D) {
            L.e("文件“" + data.getUrl() + "”,下载失败!");
        }
        Intent intent = new Intent();
        intent.setAction(GlobalConstants.DOWNLOAD_COUNT_SERVICE_NAME);
        intent.putExtra(GlobalConstants.KEY_DOWN_COMPLETE, false);
        intent.putExtra(GlobalConstants.KEY_DOWN_PERCENT, "100%");
        intent.putExtra(GlobalConstants.KEY_DOWN_ID, data.getUrl());
        intent.putExtra(GlobalConstants.KEY_DOWN_NODEID, data.getNodeId());
        if (e != null)
            intent.putExtra(GlobalConstants.KEY_DOWN_ERR_MSG, e.getMessage());
        sendBroadcast(intent);
        opSuccess();
    }

    public void downloadFileSuccess(final AttachmentData data, File file) {
        if (L.D) {
            L.e("文件“" + file.getName() + "”,下载成功!" + file.getAbsolutePath());
        }

        /**
         *文件下载成功告知BIM360 module存到数据库
         */
//        EventBus.getDefault().post(new RefreshEvent("downloadFileSuccess", data));
        LocalNetPath localNetPath =
                new LocalNetPath(file.getAbsolutePath(), data.getUrl(), data.getId(),
                        data.toString(), AttachType.FILE.value());
        LnUtil.saveData(localNetPath);
        data.setLoaclUrl(file.getAbsolutePath());
        MaApplication.getMaApplication().onDownloadFileOp(data.toString(), 1);
        PushData pushData = new PushData();
        pushData.setMsgType(LOCAL_DOWNLOAD_FILE_SUCCESS);
        pushData.setMessage(data.toString());
        Intent newIntent = new Intent();
        newIntent.setAction(GlobalConstants.PUSH_NOTIFICATION_SERVICE_NAME);
        newIntent.putExtra(GlobalConstants.PUSH_CONTENT_KEY, pushData);
        sendBroadcast(newIntent); // 这里发送的广播~！！

        // 刷新下载的数据
        ComponentUtil.refreshGallery(this, file.getAbsoluteFile().toString());
        // download ok
        data.setLoaclUrl(file.getPath());
        data.setFileSize(String.valueOf(new DecimalFormat(".##").format(file.length() / 1024.0)));
        data.setPercentStr("100%");
        if (WeqiaApplication.getInstance().getDbUtil() != null) {
            WeqiaApplication.getInstance().getDbUtil().save(data);
        }

        Intent intent = new Intent();
        intent.setAction(GlobalConstants.DOWNLOAD_COUNT_SERVICE_NAME);
        intent.putExtra(GlobalConstants.KEY_DOWN_COMPLETE, true);
        intent.putExtra(GlobalConstants.KEY_DOWN_PERCENT, "100%");
        intent.putExtra(GlobalConstants.KEY_DOWN_ID, data.getUrl());
        if (StrUtil.notEmptyOrNull(data.getProject_id())) {
            //如果是模型文件的下载，则不打开模型，所以这里不发送广播
            intent.putExtra(GlobalConstants.KEY_NO_OPEN_MODE, true);
        }
        intent.putExtra(GlobalConstants.KEY_DOWN_NODEID, data.getNodeId());
        intent.putExtra(GlobalConstants.KEY_DOWN_FILE, file);
        intent.putExtra("fileName", data.getName());
        sendBroadcast(intent);
        opSuccess();
    }

    public void downloadFileLoading(final AttachmentData data, float perent) {
        Intent intent = new Intent();
        intent.setAction(GlobalConstants.DOWNLOAD_COUNT_SERVICE_NAME);
        intent.putExtra(GlobalConstants.KEY_DOWN_COMPLETE, false);
        intent.putExtra(GlobalConstants.KEY_DOWN_ID, data.getUrl());
        intent.putExtra(GlobalConstants.KEY_DOWN_NODEID, data.getNodeId());
        String per = String.format("%.2f", perent * 100) + "%";
        if (GlobalConstants.debug_upfile)
            L.e("url = " + data.getUrl() + " -- " + per);
        intent.putExtra(GlobalConstants.KEY_DOWN_PERCENT, per);
        attachService.sendBroadcast(intent);
    }

    // ///////////////////////////////////////// 下载 //////////////////////////

    /**
     * 操作成功，结束服务
     */
    protected static void opSuccess() {
//        if (getDownloakdMap().size() == 0
//                && WeqiaApplication.getInstance().getgSendingIds().size() == 0) {
//            if (L.D) L.e("没有任务，结束线程");
//            SelectArrUtil.getInstance().clearSourceImage();
//            // attachService.stopSelf();
//        } else {
//            if (L.D)
//                L.e("还有" + getDownloakdMap().size() + "个下载, "
//                        + WeqiaApplication.getInstance().getgSendingIds().size() + "上传");
//        }
    }

    public static Map<String, UpFileData> getUploadmMap() {
        if (uploadmMap == null) {
            uploadmMap = new HashMap<String, UpFileData>();
        }
        return uploadmMap;
    }

    // 上传  先秒-预占用-上传
    public static void upLoadPicture(final List<WaitUpFileData> upFileDatas, final int sendId,
                                     final ServiceParams contentParams, final WaitSendData sendData) {
        if (!StrUtil.listNotNull(upFileDatas)) {
            if (L.D) L.e("发送完了噢，没有可以再发的");
            return;
        }
        final WaitUpFileData currentFile = upFileDatas.get(0);
        // 选择的是网上的文件
        final String filePath = currentFile.getPath();
        L.i("上传地址 == " + filePath);
        if (StrUtil.isEmptyOrNull(filePath)) {
            L.e("文件地址为空，上传失败");
            upFileErrorDo(contentParams, sendData, currentFile.getId() + "", null);
            return;
        }
        // 选择的是已经发送过的文件
        if (PathUtil.isPathInDisk(filePath) && FileUtil.getFileOrFilesSize(filePath, 1) <= 0.00) {
            L.toastShort("出错啦，文件大小为零或没有权限读取该文件");
            upFileErrorDo(contentParams, sendData, currentFile.getId() + "", null);
            return;
        }
        if (filePath != null && filePath.contains(GlobalConstants.BUCKET_SPIT)) {
            if (L.D) L.e("是已经上传过的文件，不需要再传一次" + " " + filePath);
            getUploadmMap().remove(currentFile.getId());
            upFileDatas.remove(0);
            String wfilePath = GlobalUtil.unWrapBucketUrl(filePath);
            AttachmentData tmpAtt = new AttachmentData(wfilePath);
            tmpAtt.setType(currentFile.getType());
            upFileSuccessDo(contentParams, tmpAtt, currentFile.getId().toString());
            // 检查多张图片是否全部发送完毕
            updatePicOver(upFileDatas, sendId, contentParams, sendData);
            return;
        }
        final String tmpPjId;
        if (StrUtil.isEmptyOrNull(contentParams.getPjId())) {
            tmpPjId = WPf.getInstance().get("pjId", String.class);
        } else {
            tmpPjId = contentParams.getPjId();
        }
        fastUpload(upFileDatas, sendId, contentParams, sendData, filePath, tmpPjId, null);
    }

    private static void previewLoadPicture(final List<WaitUpFileData> upFileDatas, final int sendId, final ServiceParams contentParams, final WaitSendData sendData, WaitUpFileData currentFile, final String filePath) {
        //调用文件预占用接口
        final String tmpPjId = WPf.getInstance().get("pjId", String.class);
        ServiceParams params = new ServiceParams(ComponentReqEnum.UP_LOAD_FILE_SIZE.order());
        params.put("fileSize", FileUtil.getFileOrFilesSize(filePath, 2) + "");
        params.put("pjId", tmpPjId);
        UserService.getDataFromServer(params, new ServiceRequester() {

            @Override
            public void onResult(ResultEx resultEx) {
                if (resultEx.isSuccess()) {
                    realUpload(upFileDatas, sendId, contentParams, sendData, filePath, tmpPjId, resultEx);
                }
            }
        });
    }


    private static void fastUpload(final List<WaitUpFileData> upFileDatas, final int sendId, final ServiceParams contentParams, final WaitSendData sendData, final String filePath, final String tmpPjId, final ResultEx result) {
        final WaitUpFileData currentFile = upFileDatas.get(0);
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (contentParams instanceof UpAttachParams) {
            if (filePath.contains("/") && !PathUtil.isPathInDisk(filePath)) {
                if (L.D) L.e("特例，聊天发语音、图片");
                getUploadmMap().remove(currentFile.getId());
                upFileDatas.remove(0);
                dbUtil.deleteById(WaitUpFileData.class, currentFile.getId().toString());
                if (contentParams instanceof UpAttachParams) {
                    if (StrUtil.notEmptyOrNull(filePath)) {
                        ((UpAttachParams) contentParams).setFiUrls(filePath);
                    }
                }
                updatePicOver(upFileDatas, sendId, contentParams, sendData);
                return;
            }
        }
        ServiceParams tmpParams;
        String fileMd5 = com.weqia.wq.global.FileUtil.getFileMD5ToString(filePath);
        currentFile.setFileMd(fileMd5);
        try {
            tmpParams = getFastParams(currentFile);
            tmpParams.setHasCoId(false);
        } catch (OutOfMemoryError e) {
            CheckedExceptionHandler.handleException(e);
            upFileErrorDo(contentParams, sendData, currentFile.getId() + "", "");
            return;
        }
        final ServiceParams fileParams = tmpParams;
        if (StrUtil.notEmptyOrNull(tmpPjId)) {
            fileParams.setPjId(tmpPjId);
        }
        UserService.getDataFromServer(false, fileParams, new ServiceRequester(String.valueOf(currentFile.getId())) {

            @Override
            public void onResult(ResultEx resultEx) {
                if (resultEx.isSuccess()) {
                    if (StrUtil.isEmptyOrNull(resultEx.getDataObjectStr())) {
                        previewLoadPicture(upFileDatas, sendId, contentParams, sendData, currentFile, filePath);
                    } else {
                        AttachmentData fileData = resultEx.getDataObject(AttachmentData.class);
                        if (fileData != null && StrUtil.notEmptyOrNull(fileData.getUrl())) {
                            // 发送成功广播
                            upFileFullPercent(fileParams, resultEx, getId());
                            if (GlobalConstants.debug_upfile)
                                getUploadmMap().remove(getId());
                            // 返回成功图片的fileID
                            upFileDatas.remove(0);

                            if (filePath != null) {
                                LnUtil.saveAttachData(fileData, filePath);
                            }
                            upFileSuccessDo(contentParams, fileData, getId());
                            updatePicOver(upFileDatas, sendId, contentParams, sendData);
                        }
                    }
                }
            }

            @Override
            public void onError(Integer errCode) {
                super.onError(errCode);
                if (errCode == -10001) {
                    //没有返回数据，说明之前没有上传过，需要上传
                    previewLoadPicture(upFileDatas, sendId, contentParams, sendData, currentFile, filePath);
                }
            }
        });
    }

    private static void realUpload(final List<WaitUpFileData> upFileDatas, final int sendId, final ServiceParams contentParams, final WaitSendData sendData, final String filePath, String tmpPjId, ResultEx result) {
        final WaitUpFileData currentFile = upFileDatas.get(0);
        final String operateId = result.getObject();
        if (StrUtil.isEmptyOrNull(operateId)) {
            L.toastShort(result.getMsg());
            upFileErrorDo(contentParams, sendData, currentFile.getId() + "", null);
            return;
        }
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        dbUtil.updateWhere(WaitUpFileData.class, "operateId = '" + operateId + "'", "id = " + currentFile.getId());
        ServiceParams tmpParams;
        try {
            tmpParams = getFileParams(contentParams, currentFile);
            tmpParams.setHasCoId(false);
        } catch (FileNotFoundException e) {
            CheckedExceptionHandler.handleException(e);
            L.toastShort("抱歉，文件不存在");
            upFileErrorDo(contentParams, sendData, currentFile.getId() + "", operateId);
            return;
        } catch (OutOfMemoryError e) {
            CheckedExceptionHandler.handleException(e);
            upFileErrorDo(contentParams, sendData, currentFile.getId() + "", operateId);
            return;
        }
        final ServiceParams fileParams = tmpParams;
        if (StrUtil.notEmptyOrNull(tmpPjId)) {
            fileParams.setPjId(tmpPjId);
        }
        if (StrUtil.notEmptyOrNull(operateId)) {
            fileParams.setOperateId(operateId);
        }
        UserService.getDataFromServerForUpFile(false, fileParams, new ServiceRequester(String.valueOf(currentFile.getId())) {

            @Override
            public void onResult(ResultEx resultEx) {
                // 发送成功广播
                upFileFullPercent(fileParams, resultEx, getId());
                if (GlobalConstants.debug_upfile) L.e(getId() + "------------------上传成功");
                getUploadmMap().remove(getId());
                // 返回成功图片的fileID
                upFileDatas.remove(0);

                AttachmentData fileData = resultEx.getDataObject(AttachmentData.class);
                if (filePath != null) {
                    LnUtil.saveAttachData(fileData, filePath);
                }

                upFileSuccessDo(contentParams, fileData, getId());
                updatePicOver(upFileDatas, sendId, contentParams, sendData);
            }

            @Override
            public void onError(Integer errCode) {
                super.onError(errCode);
                upFileEmptyPercent(fileParams, errCode, getId());
                if (L.D) L.e(getId() + "------------------上传失败");
                L.toastLong("上传文件失败, 请检查网络...");
                upFileErrorDo(contentParams, sendData, getId(), operateId);
            }

            @Override
            public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                L.e("上传进度 ---- " + progress);
                super.upProgress(currentSize, totalSize, progress, networkSpeed);
                UpFileData tmpData = getUploadmMap().get(getId() + "");
                float lastPercent = tmpData.getPercent();
                if (progress >= 1) {
                    if (lastPercent >= 2) {
                        return;
                    }
                    Intent intent = new Intent();
                    PercentData percentData =
                            new PercentData(getId() + "", fileParams.getItype(), null,
                                    null, null, 100);
                    intent.setAction(GlobalConstants.UPLOAD_COUNT_SERVICE_NAME);
                    intent.putExtra(GlobalConstants.KEY_UPLOAD_DATA, percentData);
                    WeqiaApplication.ctx.sendBroadcast(intent);
                    getUploadmMap().get(getId() + "").setPercent(2);
                    if (GlobalConstants.debug_upfile) L.e(getId() + "------上传到1了");
                } else {
                    if (progress - lastPercent >= 0.05) {
                        float wantNum = (float) (Math.round(progress * 10000)) / 10000;

                        getUploadmMap().get(getId() + "").setPercent(wantNum);
                        float tpercent = wantNum * 100;
                        int retPercent = (int) tpercent;
                        Intent intent = new Intent();
                        intent.setAction(GlobalConstants.UPLOAD_COUNT_SERVICE_NAME);
                        PercentData percentData =
                                new PercentData(getId() + "", fileParams.getItype(),
                                        null, null, null, retPercent);
                        intent.putExtra(GlobalConstants.KEY_UPLOAD_DATA, percentData);
                        WeqiaApplication.ctx.sendBroadcast(intent);
                        if (GlobalConstants.debug_upfile)
                            L.e(getId() + "------ " + retPercent);
                    }
                }
            }
        });
    }

    private static void updatePicOver(final List<WaitUpFileData> upFileDatas,
                                      final int sendId,
                                      final ServiceParams contentParams, final WaitSendData sendData) {
        boolean flag = ComponentUtil.checkFileSendStutus(sendId);
        if (flag) {
            uploadAllSuccess(sendId, contentParams, sendData);
        } else {
            upLoadPicture(upFileDatas, sendId, contentParams, sendData);
        }
    }

    private static void upFileFullPercent(final ServiceParams params, ResultEx resultEx,
                                          String reqId) {
        PercentData percentData =
                new PercentData(reqId, params.getItype(), true, resultEx, null, 100);
        Intent intent = new Intent();
        intent.setAction(GlobalConstants.UPLOAD_COUNT_SERVICE_NAME);
        intent.putExtra(GlobalConstants.KEY_UPLOAD_DATA, percentData);
        WeqiaApplication.ctx.sendBroadcast(intent);
    }

    private static void upFileEmptyPercent(final ServiceParams params, Integer errCode, String
            reqId) {
        PercentData percentData =
                new PercentData(reqId, params.getItype(), false, null, errCode, 0);
        Intent intent = new Intent();
        intent.setAction(GlobalConstants.UPLOAD_COUNT_SERVICE_NAME);
        intent.putExtra(GlobalConstants.KEY_UPLOAD_DATA, percentData);
        WeqiaApplication.ctx.sendBroadcast(intent);
    }

    private static void upFileSuccessDo(final ServiceParams sParams, AttachmentData fileData,
                                        String reqId) {
        final WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        // 更新本地发送成功记录
        dbUtil.updateBySql(
                WaitUpFileData.class,
                "sendStatus = " + MsgSendStatusEnum.SUCCEED.value() + " , fileId = "
                        + fileData.getId() + " , upfile = '" + fileData.toString() + "' WHERE id= "
                        + reqId + "");
        if (sParams instanceof UpAttachParams) {
            WaitUpFileData tmp = dbUtil.findById(reqId, WaitUpFileData.class);
            UpAttachData data = dbUtil.findById(tmp.getBusiness_id(), UpAttachData.class);
            if (data != null) {
                data.setSendStatus(MsgSendStatusEnum.SUCCEED.value());// error
                dbUtil.update(data);
            }
        }
        dbUtil.save(fileData);
    }

    private static void upFileErrorDo(final ServiceParams sParams,
                                      final WaitSendData sendData,
                                      String reqId, String operateId) {
        final WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        PushData pushData = new PushData();
        getUploadmMap().remove(reqId);
        removeSendingIds(sendData);
        dbUtil.updateBySql(WaitUpFileData.class, "sendStatus = " + MsgSendStatusEnum.ERROR.value()
                + " WHERE id= " + reqId + "");

        dbUtil.updateBySql(WaitSendData.class, "sendStatus = " + MsgSendStatusEnum.ERROR.value()
                + " WHERE gId= " + sendData.getgId() + "");

        if (sParams instanceof UpAttachParams) {
            try {
                WaitUpFileData fileData = dbUtil.findById(reqId, WaitUpFileData.class);
                UpAttachData data = dbUtil.findById(fileData.getBusiness_id(), UpAttachData.class);
                if (data != null) {

                    data.setSendStatus(MsgSendStatusEnum.ERROR.value());// error
                    dbUtil.update(data);
                }
            } catch (Exception e) {
            }
        }

        //上传失败删除
        if (StrUtil.notEmptyOrNull(operateId)) {
            ServiceParams params = new ServiceParams(ComponentReqEnum.UP_LOAD_FILE_SIZE_RELEASE.order());
            params.put("operateId", operateId);
            UserService.getDataFromServer(params, new ServiceRequester() {
                @Override
                public void onResult(ResultEx resultEx) {

                }
            });
        }

        // 通知界面发送成功,插入记录
        Intent newIntent = new Intent();
        newIntent.setAction(GlobalConstants.PUSH_NOTIFICATION_SERVICE_NAME);
        newIntent.putExtra(GlobalConstants.PUSH_CONTENT_KEY, pushData);
        WeqiaApplication.ctx.sendBroadcast(newIntent);
    }

    /**
     * 获取文件上传param
     */
    protected static ServiceParams getFileParams(final ServiceParams sParams,
                                                 final WaitUpFileData upFileData) throws FileNotFoundException, OutOfMemoryError {
        boolean bProjectAttach = false;
        ServiceParams params = new ServiceParams(ComponentReqEnum.UP_FILE.order());
        if (upFileData != null && upFileData.getPlayTime() != null) {
            params.put("playTime", String.valueOf(upFileData.getPlayTime()));
        }
        setuploadParamDo(upFileData, bProjectAttach, params);
        return params;
    }

    protected static ServiceParams getFastParams(final WaitUpFileData upFileData) {
        ServiceParams params = new ServiceParams(ComponentReqEnum.FAST_UP_FILE.order());
        if (upFileData != null) {
            if (StrUtil.notEmptyOrNull(upFileData.getPath())) {
                File file = new File(upFileData.getPath());
                params.put("fileByteSize", file.length());
                params.put("fileName", file.getName());
            }
            params.put("fileMd5", upFileData.getFileMd());
        }
        return params;
    }

    private static void setuploadParamDo(final WaitUpFileData upFileData,
                                         boolean bProjectAttach,
                                         ServiceParams params) throws FileNotFoundException {
        if (upFileData != null && StrUtil.notEmptyOrNull(upFileData.getPath())) {
            InputStream inputStream = null;
            File tmp = new File(upFileData.getPath());
            if (upFileData.getType() == AttachType.PICTURE.value()) {
                double fileSize = FileUtil.getFileOrFilesSize(upFileData.getPath(), 2);
                if (L.D) L.i("文件大小：" + fileSize + "kb");
                int maxFileSize = GlobalConstants.MAX_FILE_SIZE;
                if (SelectArrUtil.getInstance().isSelImgSourceContain(upFileData.getPath())) {
                    if (L.D) L.e("用户选择上传原图");
                    inputStream = new FileInputStream(tmp);
                } else {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    options.inPurgeable = true;
                    BitmapFactory.decodeFile(upFileData.getPath(), options);
                    int height = options.outHeight;
                    int width = options.outWidth;
                    float picQuear = (float) height / (float) width;
                    if (picQuear >= 4.0 || picQuear <= 0.25) {
                        if (L.D) L.i("图片比例太大,原图上传" + picQuear);
                        inputStream = new FileInputStream(tmp);
                    } else {
                        if (fileSize > maxFileSize) {
                            if (L.D) L.i("文件太大，压缩");
                            Bitmap bm =
                                    BitmapDecoder.decodeSampledBitmapFromFile(upFileData.getPath(),
                                            1280, 1280);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.JPEG,
                                    GlobalConstants.DEFAULT_COMPRESS_QUALITY, baos);
                            inputStream = new ByteArrayInputStream(baos.toByteArray());
                            if (L.D)
                                L.e(StrUtil.formatFileSize(baos.toByteArray().length / 1024 + ""));
                            try {
                                bm.recycle();
                                bm = null;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (L.D) L.i("原图上传");
                            inputStream = new FileInputStream(tmp);
                        }
                    }
                }
            } else {
                inputStream = new FileInputStream(tmp);
            }

            String mime = "";
            L.e("----------------" + upFileData.toString());
            if (upFileData.getType() == null) {
                String fileName = upFileData.getPath();
                String end = fileName.substring(fileName.lastIndexOf(".") + 1,
                        fileName.length()).toLowerCase();
                if (FileMiniUtil.isVoice(end)) {
                    params.put("fileType", AttachType.VOICE.value() + "");
                    mime = "audio/amr";
                } else if (FileMiniUtil.isVedio(end)) {
                    params.put("fileType", AttachType.VIDEO.value() + "");
                    mime = "video/mp4";
                } else if (FileMiniUtil.isImage(end)) {
                    params.put("fileType", AttachType.PICTURE.value() + "");
                    mime = "image/jpeg";
                } else {
                    params.put("fileType", AttachType.FILE.value() + "");
                    mime = "";
                }
            } else {
                if (upFileData.getType() != null) {
                    switch (upFileData.getType().intValue()) {
                        case 1:
                            mime = "image/jpeg";
                            break;
                        case 2:
                            mime = "audio/amr";
                            break;
                        case 3:
                            mime = "video/mp4";
                            break;
                        case 4:
                            mime = "";
                            break;
                    }
                }
                params.put("fileType", upFileData.getType() + "");


                if (playTime != null) {
                    params.put("playTime", playTime + "");
                    playTime = null;
                }

            }
            if (upFileData != null && StrUtil.notEmptyOrNull(upFileData.getgCoId())) {
                params.setmCoId(upFileData.getgCoId());
            }
            params.put(GlobalConstants.KEY_UPLOAD_FILE, inputStream, tmp.getName(), mime);
            UpFileData fileData = new UpFileData(upFileData.getId() + "", 0);
            getUploadmMap().put(upFileData.getId() + "", fileData);
        }
    }

    /**
     * 上传所有文件都成功，发送
     */
    protected static void uploadAllSuccess(final int sendId, final ServiceParams sParams,
                                           final WaitSendData sendData) {
        String fileIds = WaitSendUtils.getFileIds(sendId);
        String urls = WaitSendUtils.getFileUrl(sendId);
        if (sParams instanceof UpAttachParams) {
            UpAttachParams params = (UpAttachParams) sParams;
            params.setUrls(urls);
            L.i("params==>" + params);
            startSend(params, sendData);
        } else {
            sParams.setUrls(urls);
            startSend(sParams, sendData);
        }
    }

    // 正式发送
    public static void startSend(final ServiceParams serviceParams,
                                 final WaitSendData sendData) {
        UserService.getDataFromServer(serviceParams, new ServiceRequester() {
            @Override
            public void onResult(ResultEx resultEx) {
                if (resultEx.isSuccess()) {
                    L.e("得到返回的参数了！：：：" + resultEx.toString());
                    sendSuccess(serviceParams, sendData, resultEx);
                    MaApplication.getMaApplication().onSendSuccessOp(serviceParams.toString(), sendData.toString(), resultEx.toString());
                }
            }

            @Override
            public void onError(Integer errCode) {
                super.onError(errCode);
                sendError(serviceParams, sendData);
            }

        });
    }

    private static void sendSuccess(ServiceParams serviceParams, WaitSendData sendData,
                                    ResultEx resultEx) {
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil(); // 获得数据库的实例

        Intent newIntent = new Intent();
        newIntent.setAction(GlobalConstants.PUSH_NOTIFICATION_SERVICE_NAME); // 广播的action
        PushData data = new PushData();
        int itype = sendData.getItype();
        data.setMsgType(sendData.getItype());
        if (serviceParams != null) {
            RouterUtil.routerActionSync(WeqiaApplication.ctx, "pvmain", "acattachservice");
            /**
             *任务进展
             */
            if (serviceParams.getItype() == 706) {
                EventBus.getDefault().post(new RefreshEvent(13, resultEx));
            }
            /**
             *任务新建
             */
            if (serviceParams.getItype() == 704) {
                MpIdData mpIdData = resultEx.getDataObject(MpIdData.class);
                WPf.getInstance().put("orderId", mpIdData.getOrderId());
                EventBus.getDefault().post("taskNew");
                EventBus.getDefault().post("getPosDatas");
            }

            if (serviceParams.getItype() == 3708) {
                MpIdData mpIdData = resultEx.getDataObject(MpIdData.class);
                WPf.getInstance().put("orderId", mpIdData.getOrderId());
                EventBus.getDefault().post("getPosDatas");
            }

            if (serviceParams.getItype() == 3615) {
                EventBus.getDefault().post(new RefreshEvent("BIM_UPLOAD_FILE_REFRESH"));
            }

            if (serviceParams.getItype() == 728) {
                EventBus.getDefault().post(new RefreshEvent("ATTACH_UPLOAD_SUCCESS"));
            }
        }
        // 通知界面发送成功,插入记录
        newIntent.putExtra(GlobalConstants.PUSH_CONTENT_KEY, data); // 这里已经拼接完成数据
        WeqiaApplication.ctx.sendBroadcast(newIntent);// 发送广播！
        // 发送成功删除本地数据
        doDel(sendData);
    }

    private static String setNetUrls(String files, Integer type) {
        if (StrUtil.notEmptyOrNull(files)) {
            List<AttachmentData> atts = JSON.parseArray(files, AttachmentData.class);
            if (StrUtil.listNotNull(atts)) {
                for (AttachmentData ttmp : atts) {
                    if (type != null && type != ttmp.getType())
                        continue;
                    if (StrUtil.isEmptyOrNull(ttmp.getUrl())) {
                        String netPath = LnUtil.getNetpath(ttmp.getLoaclUrl(), ttmp.getType());
                        if (StrUtil.notEmptyOrNull(netPath)) {
                            ttmp.setUrl(netPath);
                        }
                    }
                }
            }
            files = atts.toString();
        }
        return files;
    }

    private static void sendError(final ServiceParams serviceParams,
                                  final WaitSendData sendData) {
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        int itype = sendData.getItype();
        PushData pushData = new PushData();
        if (dbUtil != null) {
            removeSendingIds(sendData);
            dbUtil.updateWhere(WaitSendData.class,
                    "sendStatus = " + DataStatusEnum.SEND_ERROR.value(),
                    "gId= " + sendData.getgId());
        }
        // 通知界面发送成功,插入记录
        Intent newIntent = new Intent();
        newIntent.setAction(GlobalConstants.PUSH_NOTIFICATION_SERVICE_NAME);
        newIntent.putExtra(GlobalConstants.PUSH_CONTENT_KEY, pushData);
        if (pushData.getMsgType() == null) {
            return;
        }
        WeqiaApplication.ctx.sendBroadcast(newIntent);
    }

    // 发送成功删除本地记录
    public static void doDel(WaitSendData sendData) {
        removeSendingIds(sendData);
        WeqiaApplication.getInstance().getDbUtil()
                .deleteById(WaitSendData.class, sendData.getgId());
        delFile(sendData);
    }

    private static void delFile(WaitSendData sendData) {
        WeqiaApplication.getInstance().getDbUtil()
                .deleteByWhere(WaitUpFileData.class, "sendId = " + sendData.getgId());
    }

    public static void removeSendingIds(WaitSendData sendData) {
        WeqiaApplication.getInstance().getgSendingIds().remove((Integer) sendData.getgId());
        opSuccess();
    }

}
