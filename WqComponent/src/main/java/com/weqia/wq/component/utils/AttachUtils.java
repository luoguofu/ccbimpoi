package com.weqia.wq.component.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.spinytech.macore.MaApplication;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.datastorage.file.PathUtil;
import com.weqia.utils.http.okserver.download.DownloadInfo;
import com.weqia.utils.http.okserver.download.DownloadManager;
import com.weqia.utils.view.pullrefresh.PullToRefreshBase;
import com.weqia.utils.view.pullrefresh.PullToRefreshListView;
import com.weqia.wq.R;
import com.weqia.wq.RouterUtil;
import com.weqia.wq.component.AttachService;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;
import com.weqia.wq.component.activity.SharedTitleActivity;
import com.weqia.wq.component.activity.assist.AttachAdapter;
import com.weqia.wq.component.file.FileScanActivity;
import com.weqia.wq.component.file.FileWebViewActivity;
import com.weqia.wq.component.file.ZipFileContentListActivity;
import com.weqia.wq.component.receiver.AttachMsgReceiver;
import com.weqia.wq.component.utils.bitmap.PictureUtil;
import com.weqia.wq.component.utils.request.ResultEx;
import com.weqia.wq.component.utils.request.ServiceParams;
import com.weqia.wq.component.utils.request.ServiceRequester;
import com.weqia.wq.component.utils.request.UserService;
import com.weqia.wq.component.video.VideoPlayActivity;
import com.weqia.wq.data.AttachmentData;
import com.weqia.wq.data.ComponentReqEnum;
import com.weqia.wq.data.EnumData;
import com.weqia.wq.data.PercentData;
import com.weqia.wq.data.base.WebViewData;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.global.ComponentUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AttachUtils {

    private PullToRefreshListView plAttach;
    public ListView lvAttach;
    //文件列表项是否可以点击,默认情况可以点击。当是在模型里面打开任务详情的时候，文件列表不能打开
    private boolean canClickItem = true;
    //文件列表项是否可以长按,默认情况不可以长按。当是没有操作文件权限的时候，不能长按; 该值由其他模块的具体业务提前判断好传入
    private boolean canLongClickItem;
    private List<AttachmentData> attachDatas = new ArrayList<>();
    private AttachAdapter lvAttachAdapter;

    private SharedDetailTitleActivity ctx;
    private ServiceParams attachParams;
    private AttachInterface comInterface;
    private boolean debug_percent = false;
    private boolean needAdd = true;
    private String businessId;
    private Integer businessItype;
    private String dId; // 微会议ID


    private AttachMsgReceiver uploadReceive = new AttachMsgReceiver() {

        public void uploadCountReceived(Intent intent) {
            if (intent != null) {
                if (debug_percent)
                    L.e("进度广播");
                PercentData percentData =
                        (PercentData) intent.getSerializableExtra(GlobalConstants.KEY_UPLOAD_DATA);
                if (percentData == null) {
                    return;
                }
                // 进度状态
                if (percentData.isSuccess() == null) {
                    if (percentData.getPercent() != null) {
                        if (debug_percent)
                            L.e("id percent = " + percentData.getId() + " == "
                                    + percentData.getPercent());
                    }
                } else {
                    // 完成或失败状态
                    if (percentData.isSuccess()) {
                        if (L.D)
                            L.e("id  = " + percentData.getId() + "发送成功");

                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                // L.toastLong("文件上传成功!");
                                if (comInterface != null) {
                                    comInterface.uploadComplete();
                                }
                                // getAttach(null, null);
                                // WeqiaApplication.addRf(RefeshKey.PROJECT_ATTACH);
                                // EventBus.getDefault().post(
                                // new
                                // RefreshEvent(EnumDataTwo.RefreshEnum.WORK_ATTACH.getValue()));

                            }
                        }, 3 * GlobalConstants.DELAY_TIME_SECOND);


                    } else {
                        if (L.D)
                            L.e("id  = " + percentData.getId() + "发送失败");
                    }
                }
            }
        }
    };


    private AttachMsgReceiver downReceive = new AttachMsgReceiver() {

        @Override
        public void downloadCountReceived(Intent intent) {
            if (intent != null) {
                String urlStr = intent.getStringExtra(GlobalConstants.KEY_DOWN_ID);
                String downPercent = intent.getStringExtra(GlobalConstants.KEY_DOWN_PERCENT);
                Boolean bComplete =
                        intent.getBooleanExtra(GlobalConstants.KEY_DOWN_COMPLETE, false);

                File downFile = (File) intent.getSerializableExtra(GlobalConstants.KEY_DOWN_FILE);
                if (StrUtil.isEmptyOrNull(urlStr) || StrUtil.isEmptyOrNull(downPercent)
                        || bComplete == null) {
                    return;
                }

                if (attachDatas == null || attachDatas.size() == 0) {
                    return;
                }

                for (AttachmentData attData : attachDatas) {
                    if (attData.getUrl().equalsIgnoreCase(urlStr)) {
                        attData.setPercentStr(downPercent);
                        if (lvAttachAdapter != null) {
                            lvAttachAdapter.notifyDataSetChanged();
                        }
                        if (bComplete && downFile != null) {
                            AttachUtils.downloadFinishDialog(ctx, downFile, attData.getName(), attData.getNodeId());
                        }
                    }
                }
            }
        }
    };

    public AttachUtils(SharedDetailTitleActivity ctx, AttachInterface comInterface) {
        this.ctx = ctx;
        this.comInterface = comInterface;
        initView();
    }

    private void initView() {
        plAttach = (PullToRefreshListView) ctx.findViewById(R.id.pl_attach);
        if (plAttach != null) {
            lvAttach = plAttach.getRefreshableView();
            attachDatas = new ArrayList<AttachmentData>();
            initListView();
        }
    }

    public void initData() {
        initAttachData();
        getAttach(null, null);
    }

    OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 查询是否已经下载过,已下载 可打开和重新下载
            if (position > attachDatas.size()) {
                return;
            }
            if (!canClickItem) {
                L.toastLong("当前状态不能查看浏览文件详情！");
                return;
            }
            position = position - lvAttach.getHeaderViewsCount();
            AttachmentData data = attachDatas.get(position);
            AttachmentData temp = ctx.getDbUtil().findById(data.getUrl(), AttachmentData.class);
            View iv = view.findViewById(R.id.iv_attachment_icon);
            if (temp != null) {
                temp.setType(data.getType());
                attachClick(ctx, temp, iv);
            } else {
                attachClick(ctx, data, iv);
            }
        }
    };

    private void initAttachData() {
        if (lvAttach == null) {
            return;
        }
        lvAttach.setOnItemClickListener(onItemClickListener);
        lvAttach.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int i = lvAttach.getHeaderViewsCount();
                int beginIndex = position - i;
                if (beginIndex < 0 || beginIndex > attachDatas.size() - 1) {
                    return true;
                }
                position = position - lvAttach.getHeaderViewsCount();
                AttachmentData data = attachDatas.get(position);
                if (data != null) {
                    if (canLongClickItem || ctx.getMid().equals(data.getMid())) {
                        deleteConfirm(position, 721, data.getId());
                    }
                    if (StrUtil.notEmptyOrNull(dId)) {
                        //本人发的和超级管理员才有删除权限
                        if ((StrUtil.notEmptyOrNull(data.getMid()) && ctx.getMid().equals(data.getMid()))) {
                            deleteConfirm(position, 947, data.getTf_id());
                        } else {
                            L.toastShort("没有操作权限！");
                        }
                    }
                }
                return true;
            }
        });

        lvAttachAdapter = new AttachAdapter(ctx);

        lvAttach.setAdapter(lvAttachAdapter);
        lvAttachAdapter.setItems(attachDatas);

        if (StrUtil.listNotNull(attachDatas)) {
            for (AttachmentData tmp : attachDatas) {
                if (tmp.getAutoDownload() != null && tmp.getAutoDownload()) {
                    downloadMyFile(ctx, tmp);
                }
            }
        }
    }

    // 删除确认对话框
    private void deleteConfirm(final int position, final int itype, final String tf_id) {
        Dialog mDialog = DialogUtil.initCommonDialog(ctx, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case -2:
                        break;
                    case -1:
                        // 删除分享
                        delAttach(position, itype, tf_id);
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        }, "确定要删除吗?");
        mDialog.show();
    }


    private void delAttach(final int position, int itype, String tf_id) {
        ServiceParams params = new ServiceParams(itype);
        params.put("tfId", tf_id);
        UserService.getDataFromServer(params, new ServiceRequester(ctx) {

            @Override
            public void onResult(ResultEx resultEx) {
                if (StrUtil.listNotNull(attachDatas) && position >= 0) {
                    attachDatas.remove(position);
                    lvAttachAdapter.setItems(attachDatas);
                    if (comInterface != null) {
                        comInterface.freshView();
                    }
                }
                loadComplete();
            }

            @Override
            public void onError(Integer errCode) {
                loadComplete();
            }
        });
    }

    /**
     * 获取任务附件列表
     */

    public void getAttach(Integer prevId, Integer nextId) {

        loadComplete();
        getParams().setPrevId(GlobalUtil.getStringByInt(prevId));
        getParams().setNextId(GlobalUtil.getStringByInt(nextId));

//        if (taskData != null && taskData.getTkId() != null) {
//            getParams().setItype(EnumData.RequestType.TASK_ATTACH_LIST.order());
//            getParams().put("tkId", taskData.getTkId());
//            getDataFromNet(getParams());
//        } else if (pjData != null && pjData.getProjectId() != null) {
//            // ServiceParams params = null;
//            if (bCC) {
//                getParams().setItype(EnumData.RequestType.CC_GET_PROJECT_ATTACHMENT.order());
//            } else {
//                getParams().setItype(EnumData.RequestType.GET_PROJECT_ATTACHMENT.order());
//            }
//            getParams().put("pjId", pjData.getProjectId());
//            getDataFromNet(getParams());
//        } else if (StrUtil.notEmptyOrNull(dId)) {
//            getParams().setItype(EnumData.RequestType.DISCUSS_FILES_LIST.order());
//            getParams().put("dId", dId);
//            getDataFromNet(getParams());
//        } else if (StrUtil.notEmptyOrNull(customerId)) {
//            getParams().setItype(EnumData.RequestType.CUSTOMTER_FILE_LIST.order());
//            getParams().put("customerId", customerId);
//            getDataFromNet(getParams());
//        } else {
//            loadComplete();
//        }


        if (businessItype != null) {
            getParams().setItype(businessItype);
            if (businessItype == 720) {
                if (StrUtil.notEmptyOrNull(businessId)) {
                    getParams().put("tkId", businessId);
                }
            }
            if (businessItype == 936) {
                if (StrUtil.notEmptyOrNull(businessId)) {
                    getParams().put("dId", businessId);
                }
            }
            getDataFromNet(getParams());
        } else {
            loadComplete();
        }
    }

    private void getDataFromNet(final ServiceParams params) {
        UserService.getDataFromServer(params, new ServiceRequester(ctx) {

            @Override
            public void onResult(ResultEx resultEx) {
                loadComplete();
                if (params.getPrevId() == null && params.getNextId() == null) {
                    attachDatas = new ArrayList<>();
                }
                if (resultEx.isSuccess()) {
                    List<AttachmentData> tmpAttachments =
                            resultEx.getDataArray(AttachmentData.class);
                    if (StrUtil.listNotNull(tmpAttachments)) {
                        attachDatas.addAll(tmpAttachments);
                    }
                    lvAttachAdapter.setItems(attachDatas);
                }
            }

            @Override
            public void onError(Integer errCode) {
                loadComplete();
            }
        });
    }

    private void initListView() {

        plAttach.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getAttach(null, null);

            }
        });

        // 加载更多
        plAttach.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
                if (StrUtil.listNotNull(attachDatas)) {
                    AttachmentData attachmentData = attachDatas.get(attachDatas.size() - 1);
                    String nextId = attachmentData.getTf_id();
                    if (StrUtil.isEmptyOrNull(nextId)) {
                        nextId = attachmentData.getId();
                    }
                    if (StrUtil.notEmptyOrNull(nextId)) {
                        getAttach(null, Integer.parseInt(nextId));
                    } else {
                        loadComplete();
                    }
                } else {
                    loadComplete();
                }
            }

            @Override
            public void onLastItemFast() {
                loadComplete();
            }
        });
    }

    private void loadComplete() {
        GlobalUtil.loadComplete(plAttach, ctx, needAdd);
    }

    public List<AttachmentData> getAttachDatas() {
        return attachDatas;
    }

    public void setAttachDatas(List<AttachmentData> attachDatas) {
        this.attachDatas = attachDatas;
    }

    public AttachMsgReceiver getDownReceive() {
        return downReceive;
    }

    public void setDownReceive(AttachMsgReceiver downReceive) {
        this.downReceive = downReceive;
    }

    public AttachMsgReceiver getUploadReceive() {
        return uploadReceive;
    }

    public void setUploadReceive(AttachMsgReceiver uploadReceive) {
        this.uploadReceive = uploadReceive;
    }

    public PullToRefreshListView getPlAttach() {
        return plAttach;
    }

    public AttachAdapter getLvAttachAdapter() {
        return lvAttachAdapter;
    }

    private static void downloadMyFile(Context ctx, AttachmentData data) {
        if (StrUtil.isEmptyOrNull(PathUtil.getWQPath())) {
//            L.toastShort(R.string.str_sdcard_empty);
            return;
        }
        Intent intent = new Intent(ctx, AttachService.class);
        intent.putExtra(GlobalConstants.KEY_ATTACH_OP, data);
        ctx.startService(intent);
        L.toastShort("文件开始下载...");
    }

    public void onResume() {
        if (lvAttachAdapter != null) {
            lvAttachAdapter.notifyDataSetChanged();
        }
    }

    // 下载完成对话框
    public static void downloadFinishDialog(final Activity ctx, final File file, String name, final String nodeId) {

        Dialog applyDialog =
                DialogUtil.initCommonDialog(ctx, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case Dialog.BUTTON_POSITIVE:
                                dialog.dismiss();
                                openFile(ctx, GlobalUtil.wrapNodePath(nodeId, file.getPath()));// 打开文件
                                break;
                            case Dialog.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                }, "\"" + (name == null ? file.getName() : name) + "\"已下载完成", "打开", "取消");
        applyDialog.setCancelable(false);// 模式对话框
        applyDialog.show();
    }

    // 打开文件
    public static void openFile(Activity ctx, String wrapPath) {
        openFile(ctx, wrapPath, null);
    }

    public static void openFile(Activity ctx, String wrapPath, String extraStr) {
        openFile(ctx, wrapPath, extraStr, null, null);
    }

    public static void openFile(Activity ctx, String wrapPath, String extraStr, String fileName, Integer nodeType) {
        openFile(ctx, wrapPath, extraStr, fileName, nodeType, true, null);
    }

    public static void openFile(Activity ctx, String wrapPath, String extraStr, String fileName, Integer nodeType, String pjId) {
        openFile(ctx, wrapPath, extraStr, fileName, nodeType, true, pjId);
    }

    /**
     *
     * @param ctx
     * @param wrapPath
     * @param extraStr
     * @param fileName
     * @param nodeType
     * @param bCanAction bCanAction 改为1，2，3,true 为1， false 为2
     * @param pjId
     */

    public static void openFile(Activity ctx, String wrapPath, String extraStr, String fileName, Integer nodeType, boolean bCanAction, String pjId) {
        int canAction = 1;
        if (bCanAction)
            canAction = 1;
        else
            canAction = 2;
        openFile(ctx, wrapPath, extraStr, fileName, nodeType, canAction, pjId);
    }

    public static void openFile(Activity ctx, String wrapPath, String extraStr, String fileName, Integer nodeType, int canAction, String pjId) {
        String filePath = GlobalUtil.unWrapedPath(wrapPath);
        String nodeId = GlobalUtil.unWrapedNodeId(wrapPath);
//        L.e("原始地址 = " + wrapPath);
//        L.e("filePath = " + filePath);
//        L.e("下载文件的名称: " + fileName);
        if (FileMiniUtil.canOpenFile(wrapPath)) {
            HashMap<String, String> dataMap = new HashMap<>();
            boolean selectMode = false;
            if (StrUtil.notEmptyOrNull(nodeId)) {
                if (nodeId.startsWith(GlobalConstants.SELECT_TASK)) {
                    selectMode = true;
                    nodeId = nodeId.substring(GlobalConstants.SELECT_TASK.length());
                    dataMap.put("selectType", GlobalConstants.SELECT_TASK);
                }
                if (nodeId.startsWith(GlobalConstants.SELECT_DISCUSS)) {
                    selectMode = true;
                    nodeId = nodeId.substring(GlobalConstants.SELECT_DISCUSS.length());
                    dataMap.put("selectType", GlobalConstants.SELECT_DISCUSS);
                }
            }
            if (StrUtil.notEmptyOrNull(extraStr)) {
                dataMap.put("portInfo", extraStr);
            }
            dataMap.put("key_can_action", canAction + "");
            L.e("nodeId = " + nodeId);
            dataMap.put("fileName", fileName);
            dataMap.put("filePath", filePath);
            dataMap.put("nodeId", nodeId);
            dataMap.put("pjId", pjId);
            dataMap.put("nodeType", nodeType + "");
            if (selectMode)
                dataMap.put("selectMode", "1");
            RouterUtil.routerActionSync(ctx, "pvshowdraw", "acopenshow", dataMap);
//            if (ctx instanceof FileScanActivity)
//                ctx.finish();
            return;
        } else if (FileMiniUtil.isZip(filePath)) {
            L.e("nodeId = " + nodeId);
            Intent intent = new Intent(ctx, ZipFileContentListActivity.class);
            intent.putExtra("title", fileName);
            intent.putExtra("filePath", filePath);
            intent.putExtra("nodeId", nodeId);
            intent.putExtra("nodeType", nodeType);
            intent.putExtra("pjId", pjId);
            intent.putExtra("bCanAction", canAction);
            if (StrUtil.notEmptyOrNull(extraStr)) {
                intent.putExtra("portInfo", extraStr);
            }
            ctx.startActivity(intent);
//            if (ctx instanceof FileScanActivity)
//                ctx.finish();
            return;
        }
        Intent openFileIntent = FileMiniUtil.openFile(filePath);
        try {
            ctx.startActivity(openFileIntent);
        } catch (Exception e) {// 无对应打开方式,列出所有打开方式
            Intent openUnKnowFileIntent = FileMiniUtil.getUnKnowIntent(filePath);
            ctx.startActivity(openUnKnowFileIntent);
            e.printStackTrace();
        }
    }

    public ServiceParams getParams() {
        if (attachParams == null) {
            attachParams =
                    new ServiceParams(720, ctx.getMid(),
                            null, null);
        }
        return attachParams;

    }

    public void setParams(ServiceParams params) {
        this.attachParams = params;
    }

    public interface AttachInterface {
        /**
         * 上传成功
         */
        public abstract void uploadComplete();

        /**
         * 下载成功
         */
        public abstract void downloadComplete();

        public abstract void freshView();
    }

    public boolean isNeedAdd() {
        return needAdd;
    }

    public void setNeedAdd(boolean needAdd) {
        this.needAdd = needAdd;
    }

    public static void attachClick(SharedTitleActivity ctx, AttachmentData data, View view) {
        attachClick(ctx, data, true, view);
    }

    public static void attachClick(SharedTitleActivity ctx, AttachmentData data, boolean canDown, View view) {
        if (data == null)
            return;
        if (data.getType() == 0 && data.getUrl().contains(".")) {
            String fileName = data.getUrl();
            String end = fileName.substring(fileName.lastIndexOf(".") + 1,
                    fileName.length()).toLowerCase();
            if (FileMiniUtil.isImage(end))
                data.setType(EnumData.AttachType.PICTURE.value());
            else if (FileMiniUtil.isVedio(end))
                data.setType(EnumData.AttachType.VIDEO.value());
            else if (FileMiniUtil.isVoice(end))
                data.setType(EnumData.AttachType.VOICE.value());
        }
        if (data.getType() == EnumData.AttachType.PICTURE.value() && StrUtil.notEmptyOrNull(data.getUrl()) && StrUtil.isEmptyOrNull(data.getProject_id())) {
            PictureUtil.viewPicture(ctx, data.getUrl(), view);
        } else if (data.getType() == EnumData.AttachType.VIDEO.value() && StrUtil.notEmptyOrNull(data.getUrl()) && StrUtil.isEmptyOrNull(data.getProject_id())) {
            ctx.startToActivity(VideoPlayActivity.class, data);
        } else {
            fileClick(ctx, data, canDown);
        }
    }

    public static void attachClick(SharedTitleActivity ctx, AttachmentData data, List<AttachmentData> pics, View view) {
        attachClick(ctx, data, pics, true, view);
    }

    public static void attachClick(SharedTitleActivity ctx, AttachmentData data, List<AttachmentData> pics, boolean canDown, View view) {
        if (data == null)
            return;
        if (data.getType() == EnumData.AttachType.PICTURE.value() && StrUtil.listNotNull(pics)) {
            ArrayList<String> mImageLists = new ArrayList<>();
            int pos = 0;
            for (int i = 0; i < pics.size(); i++) {
                AttachmentData attachmentData = pics.get(i);
                if (attachmentData.getType() == EnumData.AttachType.VIDEO.value()) {
                    continue;
                }
                if (attachmentData != null
                        && attachmentData.getUrl().equalsIgnoreCase(data.getUrl())) {
                    pos = i;
                }
                mImageLists.add(attachmentData.getUrl());
            }
            PictureUtil.viewPicture(ctx, mImageLists, pos, view);
        } else if (data.getType() == EnumData.AttachType.VIDEO.value()
                && StrUtil.notEmptyOrNull(data.getUrl())) {
            ctx.startToActivity(VideoPlayActivity.class, data);
        } else {
            fileClick(ctx, data, canDown);
        }
    }

    /**
     * 文件点击
     */
    private static void fileClick(final Activity ctx, final AttachmentData data, final boolean canDown) {
        if (FileMiniUtil.supportPreView(data.getName()) && !GlobalUtil.isCombination(data.getUrl())) {
            Dialog mDialog = DialogUtil.initCommonDialog(ctx, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case -2:
                            break;
                        case -1:
                            getPreviewUrl(ctx, data, canDown);
                            break;
                        default:
                            break;
                    }
                    dialog.dismiss();
                }

            }, "查看文件会消耗大量流量，可能导致运营商向您收取更多费用，强烈建议您连接WiFi后再查看文件?");
            if (ComponentUtil.isWiFiActive(ctx)) {
                getPreviewUrl(ctx, data, canDown);
            } else {
                mDialog.show();
            }
        } else {
            if (canDown) {
                if (DownloadManager.getInstance().isExistDown(data.getUrl())) {
                    DownloadInfo downloadInfo = DownloadManager.getInstance().getDownloadInfo(data.getUrl());
                    if (StrUtil.notEmptyOrNull(downloadInfo.getTargetPath())
                            && new File(downloadInfo.getTargetPath()).exists()
                            && downloadInfo.getState() == DownloadManager.FINISH) {

                        if (StrUtil.notEmptyOrNull(data.getProject_id())) {
                            MaApplication.getMaApplication().onDownloadFileOp(data.toString(), 1);
                        }
                        openFile(ctx, GlobalUtil.wrapNodePath(data.getNodeId(), downloadInfo.getTargetPath()), data.getVideoPrew(), data.getName(), data.getsType(), data.isbCanAction(), data.getPjId());
                        return;
                    }
                }
            }
            if (!NetworkUtil.detect(ctx)) {
                L.toastShort(R.string.lose_network_hint);
            } else {
                downloadFile(ctx, data, canDown);
            }
        }
    }

    private static void getPreviewUrl(final Context activity, final AttachmentData tmpData, final boolean canDown) {
        if (tmpData == null || StrUtil.isEmptyOrNull(tmpData.getUrl())) {
            return;
        }
        if (!NetworkUtil.detect(activity)) {
            L.toastShort(R.string.lose_network_hint);
            return;
        }
        ServiceParams params = new ServiceParams(ComponentReqEnum.GET_FILE_PREVIEW_URL.order());
        params.setHasCoId(true);
        params.put("fileUrl", tmpData.getUrl());
        UserService.getDataFromServer(params, new ServiceRequester() {
            @Override
            public void onResult(ResultEx resultEx) {
                if (resultEx.isSuccess()) {
                    String url = resultEx.getObject();
                    Intent intent = new Intent(activity, FileWebViewActivity.class);
                    intent.putExtra("WebViewData", new WebViewData(tmpData.getName(), url));
                    intent.putExtra("canDown", canDown);
                    intent.putExtra("attachmentData", tmpData);
                    activity.startActivity(intent);
                }
            }

            @Override
            public void onError(Integer errCode) {
                super.onError(errCode);
                if (!NetworkUtil.detect(activity)) {
                    L.toastShort(R.string.lose_network_hint);
                } else {
                    downloadFile(activity, tmpData, canDown);
                }

            }
        });
    }

    public static void downloadFile(final Context ctx, final AttachmentData tmpData, final boolean canDown) {
        if (!NetworkUtil.detect(ctx)) {
            L.toastShort(R.string.lose_network_hint);
            return;
        }
        Dialog mDialog = DialogUtil.initCommonDialog(ctx, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case -2:
                        break;
                    case -1:
                        downloadFileDo(ctx, tmpData, canDown);
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }

        }, "查看文件会消耗大量流量，可能导致运营商向您收取更多费用，强烈建议您连接WiFi后再查看文件?");
        if (ComponentUtil.isWiFiActive(ctx)) {
            downloadFileDo(ctx, tmpData, canDown);
        } else {
            mDialog.show();
        }
    }

    private static void downloadFileDo(Context activity, AttachmentData tmpData, boolean canDown) {
        Intent intent = new Intent(activity, FileScanActivity.class);
        intent.putExtra("attachmentData", tmpData);
        intent.putExtra("canDown", canDown);
        activity.startActivity(intent);
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Integer getBusinessItype() {
        return businessItype;
    }

    public void setBusinessItype(Integer businessItype) {
        this.businessItype = businessItype;
    }

    public void setCanClickItem(boolean canClickItem) {
        this.canClickItem = canClickItem;
    }

    public void setCanLongClickItem(boolean canLongClickItem) {
        this.canLongClickItem = canLongClickItem;
    }

    public void setdId(String dId) {
        this.dId = dId;
    }
}
