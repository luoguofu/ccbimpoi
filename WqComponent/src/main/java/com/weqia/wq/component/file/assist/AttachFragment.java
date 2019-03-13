package com.weqia.wq.component.file.assist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weqia.utils.L;
import com.weqia.utils.ViewUtils;
import com.weqia.wq.R;
import com.weqia.wq.component.SelectArrUtil;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;
import com.weqia.wq.component.imageselect.SelectMediaUtils;
import com.weqia.wq.component.imageselect.file.FmActivity;
import com.weqia.wq.component.utils.AttachUtils;
import com.weqia.wq.component.utils.DialogUtil;
import com.weqia.wq.component.view.scrollablelayout.ScrollableHelper;
import com.weqia.wq.data.EnumData.AttachType;
import com.weqia.wq.data.MediaData;
import com.weqia.wq.data.global.GlobalConstants;

import java.io.File;

public class AttachFragment extends TitleFragment implements ScrollableHelper.ScrollableContainer {

    protected SharedDetailTitleActivity ctx;
    protected AttachUtils attachUtils;
    private Dialog uploadFileDialog;

    private boolean needAdd = true;
    private String bussinessId;
    private Integer bussinessType;
    private AttachUploadInterface uploadInterface;

    public AttachFragment() {
        super();
    }

    public AttachFragment(String bussinessId, int bussinessType) {
        super();
        this.bussinessId = bussinessId;
        this.bussinessType = bussinessType;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ctx = (SharedDetailTitleActivity) getActivity();
        attachUtils = new AttachUtils(ctx, null);
        attachUtils.getPlAttach().hideEmptyView();
        Bundle bundle = getArguments();
        if (bundle != null) {
            boolean canClickItem = bundle.getBoolean("canClickItem", true);
            boolean canLongClickItem = bundle.getBoolean("canLongClickItem", false);
            attachUtils.setCanClickItem(canClickItem);
            attachUtils.setCanLongClickItem(canLongClickItem);
            String dId = bundle.getString("dId", null);
            attachUtils.setdId(dId);
        }
        initAttachUtil();
    }

    protected void initAttachUtil() {
        if (attachUtils == null) {
            return;
        }
        if (bussinessId != null) {
            attachUtils.setBusinessId(bussinessId);
        }
        if (bussinessType != null) {
            attachUtils.setBusinessItype(bussinessType);
        }
//        if (tkData != null) {
//            attachUtils.setBusinessId(tkData.getTkId());
//            attachUtils.setBusinessItype(EnumData.RequestType.TASK_ATTACH_LIST.order());
//        }
        attachUtils.setNeedAdd(isNeedAdd());
        attachUtils.initData();
    }

    public void getAttachData() {
        if (attachUtils != null) {
            attachUtils.getAttach(null, null);
        }
    }

    public void setUploadInterface(AttachUploadInterface uploadInterface) {
        this.uploadInterface = uploadInterface;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_project_attache, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void btRightClick() {
        showBt();
    }

    private void showBt() {
        if (ctx != null) {
            ViewUtils.showView(ctx.sharedTitleView.getButtonRight());
            ViewUtils.bindClickListenerOnViews(this, ctx.sharedTitleView.getButtonRight());
//            if (WeqiaApplication.wantRf(TaskEnum.RefeshKey.PROJECT_ATTACH, true)) {
//                attachUtils.getAttach(null, null);
//            }
        }
    }

    public void hideBt() {
        if (ctx != null) {
            ViewUtils.hideView(ctx.sharedTitleView.getButtonRight());
        }
    }

    public void addAttach() {
        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                uploadFileDialog.dismiss();
                switch ((Integer) v.getTag()) {
                    case 0:
                        SelectMediaUtils.addPic(ctx);
                        break;
                    case 1:
                        SelectMediaUtils.addVideo(ctx, 1);
                        break;
//                    case 2:
//                        SelectMediaUtils.addMyFile(ctx);
//                        break;
//                    case 3:
//                        SelectMediaUtils.addCompanyFile(ctx);
//                        break;
                    case 2:
                        SelectMediaUtils.addLocalFile(ctx);
                        break;
                    default:
                        break;
                }
            }
        };
        uploadFileDialog =
                DialogUtil.initLongClickDialog(ctx, null, new String[]{"图片", "视频",
                        "本地文件"}, onClickListener);
        uploadFileDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == ctx.sharedTitleView.getButtonRight()) {
            addAttach();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (attachUtils != null) {
            attachUtils.onResume();
            if (attachUtils.getDownReceive() != null) {
                IntentFilter filter = new IntentFilter();
                filter.addAction(GlobalConstants.DOWNLOAD_COUNT_SERVICE_NAME);
                filter.setPriority(Integer.MAX_VALUE);
                ctx.registerReceiver(attachUtils.getDownReceive(), filter);
            }

            if (attachUtils.getUploadReceive() != null) {
                IntentFilter filter = new IntentFilter();
                filter.addAction(GlobalConstants.UPLOAD_COUNT_SERVICE_NAME);
                filter.setPriority(Integer.MAX_VALUE);
                ctx.registerReceiver(attachUtils.getUploadReceive(), filter);
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (attachUtils != null) {
            if (attachUtils.getDownReceive() != null) {
                ctx.unregisterReceiver(attachUtils.getDownReceive());
            }
            if (attachUtils.getUploadReceive() != null) {
                ctx.unregisterReceiver(attachUtils.getUploadReceive());
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case SelectMediaUtils.REQ_GET_FILE:
                upFileDo(data);
                break;
            case SelectMediaUtils.REQ_GET_PIC:
                upPic();
                break;
            case SelectMediaUtils.REQ_GET_FILE_URL:
                upFileById(data);
                break;
            case SelectMediaUtils.REQ_VIDEO_CAPTURE:
                upVideo(data);
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void upFileById(Intent data) {
        if (data == null) {
            return;
        }
//        String attach = data.getExtras().getString("FILE-PATH");
//        for (int i = 0; i < FmActivity.getPaths().size(); i++) {
//            DocData docData = BaseData.fromString(DocData.class, FmActivity.getPaths().get(i));
//            AttachmentData attachmentData =
//                    new AttachmentData(docData.getId(), docData.getDocName(), docData.getType(),
//                            docData.getFileSize(), docData.getUrl());
//            LnUtil.saveAttachData(attachmentData, null);
//            upDo(attachmentData.getUrl(), attachmentData.getType());
//        }
//        FmActivity.getPaths().clear();
    }

    protected void upPic() {
        for (int i = 0; i < SelectArrUtil.getInstance().getSelImgSize(); i++) {
            upDo(SelectArrUtil.getInstance().getSelImg(i), AttachType.PICTURE.value());
        }
        SelectArrUtil.getInstance().clearImage();
    }

    protected void upVideo(Intent data) {
        Long time = data.getExtras().getLong(GlobalConstants.KEY_VIDEO_TIME);
        String filePath = data.getExtras().getString(GlobalConstants.KEY_VIDEO_PATH);
        Uri fileUri = data.getExtras().getParcelable(GlobalConstants.KEY_VIDEO_URI);
        MediaData mediaData = new MediaData();
        mediaData.setPath(filePath);
        mediaData.setDuration(time);
        mediaData.setFileUri(fileUri);
        upDo(mediaData.getPath(), AttachType.VIDEO.value());
    }

    protected void upFileDo(Intent data) {
        for (int i = 0; i < FmActivity.getPaths().size(); i++) {
            File tmp = new File(FmActivity.getPaths().get(i));
            if (tmp.exists() && tmp.length() > 0) {
                if (tmp.length() < GlobalConstants.UPLOAD_MAX_FILE_SIZE) {
                    upDo(FmActivity.getPaths().get(i), AttachType.FILE.value());
                } else {
                    L.toastShort("文件大小不能超过" + GlobalConstants.UPLOAD_MAX_FILE_SIZE_STR);
                }
            } else {
                L.toastShort("文件不存在!");
            }
        }
        FmActivity.getPaths().clear();
    }

    protected void upDo(String path, int type) {
        if (uploadInterface != null)
            uploadInterface.upDo(path, type);
    }

    @Override
    public View getScrollableView() {
        return attachUtils.lvAttach;
    }

    public boolean isNeedAdd() {
        return needAdd;
    }

    public void setNeedAdd(boolean needAdd) {
        this.needAdd = needAdd;
    }
}
