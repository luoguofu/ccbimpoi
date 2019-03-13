package com.weqia.wq.component.imageselect;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.View;

import com.weqia.utils.L;
import com.weqia.utils.PhotoUtil;
import com.weqia.utils.StrUtil;
import com.weqia.utils.TimeUtils;
import com.weqia.utils.bitmap.ImageUtil;
import com.weqia.utils.data.LocalNetPath;
import com.weqia.utils.datastorage.file.FileUtil;
import com.weqia.utils.datastorage.file.PathUtil;
import com.weqia.wq.component.SelectArrUtil;
import com.weqia.wq.component.activity.SharedTitleActivity;
import com.weqia.wq.component.imageselect.assist.SelectAttachEnum.CropEnum;
import com.weqia.wq.component.imageselect.assist.SelectAttachEnum.CropTypeEunm;
import com.weqia.wq.component.imageselect.assist.SelectAttachEnum.WithSourceEnum;
import com.weqia.wq.component.imageselect.file.FmActivity;
import com.weqia.wq.component.utils.DialogUtil;
import com.weqia.wq.component.utils.GlobalUtil;
import com.weqia.wq.component.utils.LnUtil;
import com.weqia.wq.component.utils.locate.LocationActivity;
import com.weqia.wq.component.utils.request.UserService;
import com.weqia.wq.component.view.picture.PictureGridView;
import com.weqia.wq.data.AttachmentData;
import com.weqia.wq.data.EnumData;
import com.weqia.wq.data.EnumData.AttachType;
import com.weqia.wq.data.MediaData;
import com.weqia.wq.data.WPf;
import com.weqia.wq.data.WaitSendData;
import com.weqia.wq.data.WaitUpFileData;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.data.global.Hks;
import com.weqia.wq.data.global.WeqiaApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by berwin on 15/8/5.
 */
public class SelectMediaUtils {

    private static Dialog fileDlg = null;
    private static Dialog selFileDlg = null;
    private static Dialog attachDlg = null;
    public static final int VideoPathCount = 3;

    public static String filePathOriginal;

    public static final int REQ_TAKE_PHOTO = 10001;
    public static final int REQ_TAKE_VIDEO = 10002;
    public static final int REQ_GET_PIC = 311;
    public static final int REQ_VIDEO_CAPTURE = 201;
    public static final int REQ_GET_FILE = 313;
    public static final int REQ_UPDATE_FILE = 317;
    public static final int REQ_GET_FILE_URL = 314;
    public static final int REQ_GET_RED_PACKET = 315;

    public static final String KEY_IMAGE_SELECT_SIZE = "select_size";
    public static final String KEY_SELECT_TYPE = "select_type";
    public static final String KEY_CROP = "crop";
    public static final String KEY_CROP_TYPE = "crop_type";
    public static final String KEY_TO_WEBO = "bChooseFirst";

    public static final String EXTRA_IMAGES = "extra_images";
    public static final String EXTRA_INDEX = "extra_index";
    public static final String EXTRA_BE_PHOTO = "extra_photo";
    public static final String KEY_WITH_SOURCE = "withSource";

    /**
     * 获取裁剪的路径
     * @param cropType
     * @return
     */
    public static String getCropPath(int cropType) {
        String des = PathUtil.getAvatarPath() + File.separator + WeqiaApplication.getgMCoId();
        String strName = CropTypeEunm.valueOf(cropType);
        if (StrUtil.notEmptyOrNull(strName)) {
            des += strName;
        } else {
            L.e("没有裁剪的类型，需要添加枚举");
        }
        return des;
    }

    /**
     * 显示附件对话框
     * @param ctx
     * @param pictrueView
     */
    public static void showAttachDialog(final SharedTitleActivity ctx,
                                        final PictureGridView pictrueView) {
        final boolean canAdd = getPicCanAdd(pictrueView);
        final int num = getPicAddSize(pictrueView);
        if (!canAdd) {
            return;
        }
        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                attachDlg.dismiss();
                switch ((Integer) v.getTag()) {
                    case 0:
                        addFile(ctx, canAdd);
                        break;
                    case 1:
                        addPic(ctx, num);
                        break;
                    case 2:
                        addVideo(ctx, num);
                        break;
                    default:
                        break;
                }
            }
        };

        attachDlg =
                DialogUtil.initLongClickDialog(ctx, null, new String[]{"文件", "图片", "视频"},
                        onClickListener);
        attachDlg.show();
    }

    public static void addFile(SharedTitleActivity ctx, PictureGridView pictrueView) {
        boolean canAdd = getPicCanAdd(pictrueView);
        addFile(ctx, canAdd);
    }

    private static void addFile(final SharedTitleActivity ctx, boolean canAdd) {
        if (!canAdd) {
            return;
        }
//        View.OnClickListener onClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fileDlg.dismiss();
//                switch ((Integer) v.getTag()) {
//                    case 0:
//                        addMyFile(ctx);
//                        break;
//                    case 1:
//                        addCompanyFile(ctx);
//                        break;
//                    case 2:
//                        addLocalFile(ctx);
//                        break;
//                    default:
//                        break;
//                }
//            }
//        };
//
//        fileDlg =
//                DialogUtil.initLongClickDialog(ctx, null, new String[]{"我的文件", "企业文件", "本地文件"},
//                        onClickListener);
//        fileDlg.show();
        /**
         *仅支持本地文件
         */
        addLocalFile(ctx);
    }

    public static void addPicByDetail(SharedTitleActivity ctx, PictureGridView pictrueView,
                                      ArrayList<String> selectStrs, int index) {
        int canAddSize = getPicAddSize(pictrueView);
        Intent i = new Intent(ctx, ImageBrowseActivity.class);
        i.putExtra(EXTRA_IMAGES, selectStrs);
        i.putExtra(EXTRA_INDEX, index);
        i.putExtra(KEY_IMAGE_SELECT_SIZE, canAddSize);
        ctx.startActivityForResult(i, REQ_GET_PIC);
    }

    public static void addVideo(SharedTitleActivity ctx, PictureGridView pictrueView) {
        int num = getPicAddSize(pictrueView);
        boolean canAdd = getPicCanAdd(pictrueView);
        if (!canAdd) {
            return;
        }
        addVideo(ctx, num);
    }

    public static void addVideoToWebo(SharedTitleActivity ctx) {
        Map<String, String> paramMap = getAddVideoMap(1, KEY_TO_WEBO);
        ctx.startToActivityForResult(ImageListActivity.class, paramMap, REQ_VIDEO_CAPTURE);
    }

    public static void addVideo(SharedTitleActivity ctx, int num) {
        L.e("bitmap add num :" + num);
        Map<String, String> paramMap = getAddVideoMap(num, null);
        ctx.startToActivityForResult(ImageListActivity.class, paramMap, REQ_VIDEO_CAPTURE);
    }

    /**
     * 发送一个地址
     * @param ctx
     */
    public static void addLoc(SharedTitleActivity ctx) {
        Intent newIntent = new Intent(ctx, LocationActivity.class);
        newIntent.putExtra(GlobalConstants.KEY_CAN_SELECT, true);
        newIntent.putExtra("bSelectedShow", true);
        ctx.startActivityForResult(newIntent, GlobalConstants.REQUESTCODE_GET_LOC);
    }

    public static String getImgRealUrl(String imageUri) {
        String realUrl = null;
        // 获取真实地址
        String netPath = LnUtil.getImageRealPath(imageUri, AttachType.TWO_IMG_PATH.value());
        if (StrUtil.notEmptyOrNull(netPath)) {
            realUrl = netPath;
        } else {
            GetRealUrl getRealUrl = new GetRealUrl();
            getRealUrl.execute(imageUri);
        }
        return realUrl;
    }


    public static class GetRealUrl extends AsyncTask<String, Void, Void> {
        String imageUri;
        String realUrl;

        @Override
        protected Void doInBackground(String... params) {
            imageUri = params[0];
            realUrl = UserService.getBitmapUrl(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (StrUtil.notEmptyOrNull(realUrl)) {
                if (WeqiaApplication.getInstance().getDbUtil() != null) {
                    LocalNetPath tmpPath =
                            new LocalNetPath(realUrl, imageUri, AttachType.TWO_IMG_PATH.value());
                    LnUtil.saveData(tmpPath);
                }
            }
        }
    }


    private static Map<String, String> getAddVideoMap(int num, String bChooseFirsts) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put(KEY_IMAGE_SELECT_SIZE, num + "");
        paramMap.put(KEY_SELECT_TYPE, AttachType.VIDEO.value() + "");
        if (bChooseFirsts != null) {
            paramMap.put(KEY_TO_WEBO, bChooseFirsts);
        }
        return paramMap;
    }

    public static void addPic(SharedTitleActivity ctx, PictureGridView pictrueView) {
        int num = getPicAddSize(pictrueView);
        boolean canAdd = getPicCanAdd(pictrueView);
        if (!canAdd) {
            return;
        }
        addPic(ctx, num);
    }

    public static void addPic(SharedTitleActivity ctx) {
        addPic(ctx, GlobalConstants.IMAGE_MAX);
    }

    /**
     * @param ctx
     * @param num
     */
    public static void addPic(SharedTitleActivity ctx, int num) {
        Map<String, String> paramMap = getAddPicMap(num);
        ctx.startToActivityForResult(ImageListActivity.class, paramMap, REQ_GET_PIC);
    }

    public static void addPicWithSource(SharedTitleActivity ctx) {
        Map<String, String> paramMap = getAddPicMap(GlobalConstants.IMAGE_MAX);
        paramMap.put(KEY_WITH_SOURCE, WithSourceEnum.YES.value());
        ctx.startToActivityForResult(ImageListActivity.class, paramMap, REQ_GET_PIC);
    }

    public static void addPicToWebo(SharedTitleActivity ctx) {
        Map<String, String> paramMap = getAddPicMap(GlobalConstants.IMAGE_MAX);
        paramMap.put(KEY_TO_WEBO, KEY_TO_WEBO);
        ctx.startToActivityForResult(ImageListActivity.class, paramMap, REQ_GET_PIC);
    }

    /**
     * @param ctx
     */
    public static void addPicWithCrop(SharedTitleActivity ctx, int cropType) {
        Map<String, String> paramMap = getAddPicMap(1);
        paramMap.put(KEY_CROP, CropEnum.YES.value());
        paramMap.put(KEY_CROP_TYPE, cropType + "");
        ctx.startToActivityForResult(ImageListActivity.class, paramMap, REQ_GET_PIC);
    }

    /**
     * 获取跳转过去的map
     * @param num
     * @return
     */
    private static Map<String, String> getAddPicMap(int num) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put(KEY_IMAGE_SELECT_SIZE, num + "");
        paramMap.put(KEY_SELECT_TYPE, AttachType.PICTURE.value() + "");
        return paramMap;
    }

    public static void onMediaResult(SharedTitleActivity ctx, PictureGridView pictrueView,
                                     int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (data != null) {
            if (requestCode == REQ_GET_FILE) {
                addLocalFileResult(pictrueView, data);
            } else if (requestCode == REQ_GET_FILE_URL) {
//                addNetFileResult(pictrueView, data);
            } else if (requestCode == REQ_VIDEO_CAPTURE) {
                addVideoResult(ctx, data, pictrueView);
            }
        }

        if (requestCode == REQ_GET_PIC) {
            addPicResult(pictrueView);
        }
    }

    private static void addLocalFileResult(PictureGridView pictrueView, Intent data) {
//        String path = data.getExtras().getString("FILE-PATH");
        for (int i = 0; i < FmActivity.getPaths().size(); i++) {
            File file = new File(FmActivity.getPaths().get(i));
            if (file.exists() && file.length() > 0) {
                if (file.length() < GlobalConstants.UPLOAD_MAX_FILE_SIZE) {
                    if (pictrueView.getAddedPaths().size() < pictrueView.getAdapter().getMaxPicture()) {
                        pictrueView.getAddedPaths().add(
                                EnumData.AttachType.FILE.value() + GlobalConstants.SPIT_SENDMEDIA
                                        + file.getName() + GlobalConstants.SPIT_SENDMEDIA + FmActivity.getPaths().get(i));
                        // pictrueView.getAdapter().setItems(pictrueView.getAddedPaths());
                        pictrueView.refresh();
                    }
                } else {
                    L.toastShort("文件大小不能超过" + GlobalConstants.UPLOAD_MAX_FILE_SIZE_STR);
                }
            } else {
                L.toastShort("文件不存在!");
            }
        }
        FmActivity.getPaths().clear();
    }

//    private static void addNetFileResult(PictureGridView pictrueView, Intent data) {
///       String attach = data.getExtras().getString("FILE-PATH");
//        for (int i = 0; i < FileActivity.getPaths().size(); i++) {
//            DocData docData = BaseData.fromString(DocData.class, FileActivity.getPaths().get(i));
//            AttachmentData attachmentData =
//                    new AttachmentData(docData.getId(), docData.getDocName(), docData.getType(),
//                            docData.getFileSize(), docData.getUrl());
//            LnUtil.saveAttachData(attachmentData, null);
//            if (pictrueView.getAddedPaths().size() < pictrueView.getAdapter().getMaxPicture()) {
//                pictrueView.getAddedPaths().add(
//                        attachmentData.getType() + GlobalConstants.SPIT_SENDMEDIA
//                                + docData.getDocName() + GlobalConstants.SPIT_SENDMEDIA
//                                + docData.getUrl());
//                // pictrueView.getAdapter().setItems(pictrueView.getAddedPaths());
//                pictrueView.refresh();
//            }
//        }
//        FileActivity.getPaths().clear();
//    }

    private static void addPicResult(PictureGridView pictrueView) {
        ArrayList<String> tmpImagePaths = new ArrayList<String>(pictrueView.getAddedPaths());
        for (String string : tmpImagePaths) {
            if (isImagePath(string)) {
                if (!SelectArrUtil.getInstance().isSelImgContain(string)) {
                    pictrueView.getAddedPaths().remove(string);
                }
            }
        }
        for (int i = 0; i < SelectArrUtil.getInstance().getSelImgSize(); i++) {
            String tmpStr = SelectArrUtil.getInstance().getSelImg(i);

            if (StrUtil.notEmptyOrNull(tmpStr)) {
                if (!pictrueView.getAddedPaths().contains(tmpStr)) {
                    pictrueView.getAddedPaths().add(tmpStr);
                }
            }
        }
        // pictrueView.getAdapter().setItems(pictrueView.getAddedPaths());
        pictrueView.refresh();
        SelectArrUtil.getInstance().clearImage();
    }

    public static void addVideoResult(SharedTitleActivity ctx, Intent data,
                                      PictureGridView pictrueView) {
        if (pictrueView == null) {
            return;
        }
        Long time = data.getExtras().getLong(GlobalConstants.KEY_VIDEO_TIME);
        String filePath = data.getExtras().getString(GlobalConstants.KEY_VIDEO_PATH);
        Uri fileUri = data.getExtras().getParcelable(GlobalConstants.KEY_VIDEO_URI);
        if (fileUri == null) {
            fileUri = GlobalUtil.getVideoUriByPath(ctx, filePath);
        }
        if (fileUri == null) {
            fileUri = Uri.parse("");
        }
        File tmp = new File(filePath);
        if (tmp.exists() && tmp.length() > 0) {
            if (tmp.length() < GlobalConstants.UPLOAD_MAX_FILE_SIZE) {
                MediaData mediaData = new MediaData();
                mediaData.setPath(filePath);
                mediaData.setDuration(time);
                mediaData.setFileUri(fileUri);
                if (pictrueView.getAddedPaths().size() < pictrueView.getAdapter().getMaxPicture()) {
                    pictrueView.getAddedPaths().add(
                            EnumData.AttachType.VIDEO.value() + GlobalConstants.SPIT_SENDMEDIA
                                    + time + GlobalConstants.SPIT_SENDMEDIA + filePath
                                    + GlobalConstants.SPIT_SENDMEDIA + fileUri.toString());
                    // pictrueView.getAdapter().setItems(pictrueView.getAddedPaths());
                    pictrueView.refresh();
                }
            } else {
                L.toastShort("文件大小不能超过" + GlobalConstants.UPLOAD_MAX_FILE_SIZE_STR);
            }
        } else {
            L.toastShort("文件不存在!");
        }
    }

    /**
     * 是否是图片路径
     * @param path
     * @return
     */
    public static boolean isImagePath(String path) {
        if (StrUtil.isEmptyOrNull(path)) {
            return false;
        }
        if (path.contains(GlobalConstants.SPIT_SENDMEDIA)) {
            if (path.startsWith(EnumData.AttachType.PICTURE.value()
                    + GlobalConstants.SPIT_SENDMEDIA)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * 是否是视频
     * @param path
     * @return
     */
    public static boolean isVideoPath(String path) {
        if (path.contains(GlobalConstants.SPIT_SENDMEDIA)) {
            if (path.startsWith(EnumData.AttachType.VIDEO.value() + GlobalConstants.SPIT_SENDMEDIA)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * /**
     * 获取路径
     * @param path
     * @return
     */
    private static String getDes(String path) {
        if (path.contains(GlobalConstants.SPIT_SENDMEDIA)) {
            String[] tmps = path.split(GlobalConstants.SPIT_SENDMEDIA);
            if (tmps != null && tmps.length >= VideoPathCount) {
                return tmps[2];
            } else {
                return "";
            }
        } else {
            return path;
        }
    }

    /**
     * 获取路径
     * @param path
     * @return
     */
    private static String getInfo(String path) {
        if (path.contains(GlobalConstants.SPIT_SENDMEDIA)) {
            String[] tmps = path.split(GlobalConstants.SPIT_SENDMEDIA);
            if (tmps != null && tmps.length >= VideoPathCount) {
                return tmps[1];
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    /*
     * 是否是文件
     *
     * @param path
     * @return
     */
    public static boolean isFilePath(String path) {
        if (path.contains(GlobalConstants.SPIT_SENDMEDIA)) {
            if (path.startsWith(EnumData.AttachType.FILE.value() + GlobalConstants.SPIT_SENDMEDIA)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * 本地图片转存
     * @return
     */
    public static ArrayList<AttachmentData> picpathToAttach(PictureGridView pictrueView,
                                                            SharedTitleActivity ctx) {
        ArrayList<AttachmentData> attachmentDatas = new ArrayList<AttachmentData>();
        if (StrUtil.listNotNull(pictrueView.getAddedPaths())) {
            ArrayList<String> atts = pictrueView.getAddedPaths();

            for (String tmpPath : atts) {
                AttachmentData picData = new AttachmentData();
                if (isImagePath(tmpPath)) {
                    picData.setType(EnumData.AttachType.PICTURE.value());
                    picData.setPicScale(ImageUtil.getImageScale(getDes(tmpPath)));
                } else if (isVideoPath(tmpPath)) {
                    try {
                        picData.setPlayTime(Integer.parseInt(getInfo(tmpPath)) / 1000);
                    } catch (Exception e) {
                    }
                    picData.setType(EnumData.AttachType.VIDEO.value());
                    picData.setPicScale(GlobalUtil.getVideoScale(ctx, getDes(tmpPath)));
                    String[] components = tmpPath.split(GlobalConstants.SPIT_SENDMEDIA);
                    if (components.length == VideoPathCount + 1) {
                        String prew = components[3];
                        picData.setVideoPrew(prew);
                    } else if (components.length == VideoPathCount) {
                        Uri prewUri = GlobalUtil.getVideoUriByPath(ctx, components[2]);
                        if (prewUri != null) {
                            picData.setVideoPrew(prewUri.toString());
                        }
                    }
                } else if (isFilePath(tmpPath)) {
                    picData.setName(getInfo(tmpPath));
                    picData.setType(EnumData.AttachType.FILE.value());
                }
                String desPath = getDes(tmpPath);
                picData.setUrl(desPath);
                picData.setFileSize(FileUtil.getFileOrFilesSize(desPath, 2) + "");
                attachmentDatas.add(picData);
            }
        }
        return attachmentDatas;
    }

    /**
     * 存储需要发送的文件
     * @param top
     */
    public static void saveSendFile(WaitSendData top, PictureGridView pictrueView,
                                    SharedTitleActivity ctx) {
        saveSendFile(top, pictrueView.getAddedPaths(), ctx);
    }

    /**
     * 存储需要发送的文件
     * @param top
     */
    public static void saveSendFile(WaitSendData top, List<String> filePaths, SharedTitleActivity ctx) {
        for (int i = 0; i < filePaths.size(); i++) {
            String path = filePaths.get(i);
            if (StrUtil.notEmptyOrNull(path)) {
                WaitUpFileData waitUpFileData = null;
                String[] dess = path.split(GlobalConstants.SPIT_SENDMEDIA);
                if (dess.length == 1) {
                    waitUpFileData =
                            new WaitUpFileData(top.getgId(), path,
                                    EnumData.AttachType.PICTURE.value());
                } else if (dess.length >= VideoPathCount) {
                    String tmpType = dess[0];
                    String des = dess[2];
                    if (StrUtil.isEmptyOrNull(des)) {
                        continue;
                    }
                    int type = Integer.parseInt(tmpType);
                    if (type == EnumData.AttachType.PICTURE.value()) {
                        waitUpFileData =
                                new WaitUpFileData(top.getgId(), des,
                                        EnumData.AttachType.PICTURE.value());
                    } else if (type == EnumData.AttachType.VIDEO.value()) {
                        waitUpFileData =
                                new WaitUpFileData(top.getgId(), des,
                                        EnumData.AttachType.VIDEO.value());
                        if (dess.length == VideoPathCount + 1) {
                            waitUpFileData.setFileUri(dess[3]);
                        }
                    } else if (type == EnumData.AttachType.FILE.value()) {
                        waitUpFileData =
                                new WaitUpFileData(top.getgId(), des,
                                        EnumData.AttachType.FILE.value());
                    }
                }
                if (waitUpFileData != null) {
                    ctx.getDbUtil().save(waitUpFileData, false);
                }
            }
        }
    }

    /**
     * 存储需要发送的文件
     * @param top
     */
    public static void saveSendFile(WaitSendData top, SharedTitleActivity ctx) {
        List<String> imgs = SelectArrUtil.getInstance().getSelectedImgs();
        for (int i = 0; i < imgs.size(); i++) {
            String path = imgs.get(i);
            if (StrUtil.notEmptyOrNull(path)) {
                WaitUpFileData waitUpFileData = null;
                String[] dess = path.split(GlobalConstants.SPIT_SENDMEDIA);
                if (dess.length == 1) {
                    waitUpFileData =
                            new WaitUpFileData(top.getgId(), path,
                                    EnumData.AttachType.PICTURE.value());
                } else if (dess.length >= VideoPathCount) {
                    String tmpType = dess[0];
                    String des = dess[2];
                    if (StrUtil.isEmptyOrNull(des)) {
                        continue;
                    }
                    int type = Integer.parseInt(tmpType);
                    if (type == EnumData.AttachType.PICTURE.value()) {
                        waitUpFileData =
                                new WaitUpFileData(top.getgId(), des,
                                        EnumData.AttachType.PICTURE.value());
                    } else if (type == EnumData.AttachType.VIDEO.value()) {
                        waitUpFileData =
                                new WaitUpFileData(top.getgId(), des,
                                        EnumData.AttachType.VIDEO.value());
                        if (dess.length == VideoPathCount + 1) {
                            waitUpFileData.setFileUri(dess[3]);
                        }
                    } else if (type == EnumData.AttachType.FILE.value()) {
                        waitUpFileData =
                                new WaitUpFileData(top.getgId(), des,
                                        EnumData.AttachType.FILE.value());
                    }
                }
                if (waitUpFileData != null) {
                    ctx.getDbUtil().save(waitUpFileData, false);
                }
            }
        }
    }

    public static void takePicture(SharedTitleActivity ctx) {
        takePicture(ctx, false);
    }


    public static void takePicture(SharedTitleActivity ctx, boolean bCameraFacing) {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            filePathOriginal =
                    PathUtil.getWQPath() + File.separator + TimeUtils.getFileSaveTime() + ".jpg";
            WPf.getInstance().put(Hks.take_photo_path, filePathOriginal);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePathOriginal)));
            if (bCameraFacing) {
                intent.putExtra("android.intent.extras.CAMERA_FACING",
                        Camera.CameraInfo.CAMERA_FACING_FRONT);
            } else {
                intent.putExtra("android.intent.extras.CAMERA_FACING",
                        Camera.CameraInfo.CAMERA_FACING_BACK);
            }
            ctx.startActivityForResult(intent, REQ_TAKE_PHOTO);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void takePictureThrow(SharedTitleActivity ctx, boolean bCameraFacing)
            throws ActivityNotFoundException {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        filePathOriginal =
                PathUtil.getWQPath() + File.separator + TimeUtils.getFileSaveTime() + ".jpg";
        WPf.getInstance().put(Hks.take_photo_path, filePathOriginal);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePathOriginal)));
        if (bCameraFacing) {
            intent.putExtra("android.intent.extras.CAMERA_FACING",
                    Camera.CameraInfo.CAMERA_FACING_FRONT);
        } else {
            intent.putExtra("android.intent.extras.CAMERA_FACING",
                    Camera.CameraInfo.CAMERA_FACING_BACK);
        }
        ctx.startActivityForResult(intent, REQ_TAKE_PHOTO);
    }

    public static void takeWithCropPictureResult(SharedTitleActivity ctx, int cropType) {
        refeshMedia(ctx);
        String des = getCropPath(cropType);
        if (L.D) L.i("裁剪到的位置 == " + des);
        if (StrUtil.isEmptyOrNull(filePathOriginal)) {
            if (SelectArrUtil.getInstance().getSelImgSize() > 0) {
                filePathOriginal = SelectArrUtil.getInstance().getSelImg(0);
            }
            if (StrUtil.isEmptyOrNull(filePathOriginal)) {
                L.e("没有裁剪的路径");
                return;
            }
        }
        ClipImageActivity.startActivity(ctx, filePathOriginal, des, PhotoUtil.REQUESTCODE_CROP);
        SelectArrUtil.getInstance().clearImage();
        SelectArrUtil.getInstance().clearSourceImage();
    }

    public static void takePictureResult(SharedTitleActivity ctx) {
        takePictureResult(ctx, false);
    }

    public static void takePictureResult(SharedTitleActivity ctx, boolean withSource) {
        refeshMedia(ctx);
        SelectArrUtil.getInstance().addImage(filePathOriginal);
        Intent i = new Intent(ctx, ImageBrowseActivity.class);
        ArrayList<String> tmpArr = new ArrayList<String>();
        tmpArr.add(filePathOriginal);

        i.putExtra(EXTRA_IMAGES, tmpArr);
        i.putExtra(EXTRA_INDEX, 0);
        i.putExtra(EXTRA_BE_PHOTO, true);
        i.putExtra(KEY_IMAGE_SELECT_SIZE, 1);
        i.putExtra(KEY_WITH_SOURCE, withSource);
//        if (bChooseFirst) {
//            i.putExtra(KEY_TO_WEBO, KEY_TO_WEBO);
//        }
        ctx.startActivityForResult(i, 102);
    }

    private static void refeshMedia(SharedTitleActivity ctx) {
        if (StrUtil.isEmptyOrNull(filePathOriginal)) {
            if (L.D) L.e("拍照导致程序重启，从本地获取保存相册的图片");
            filePathOriginal = WPf.getInstance().get(Hks.take_photo_path, String.class);// StatedPerference.getTakePhotoPath();
        }
        if (StrUtil.isEmptyOrNull(filePathOriginal)) {
            L.toastShort("抱歉，拍照失败，请重试!");
            return;
        }
        ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"
                + filePathOriginal)));
    }

    public static void talkVideoResult(ImageListActivity instance, Intent data) {
        Long time = data.getExtras().getLong(GlobalConstants.KEY_VIDEO_TIME);
        String filePath = data.getExtras().getString(GlobalConstants.KEY_VIDEO_PATH);
        Intent intent = new Intent();
        intent.putExtra(GlobalConstants.KEY_VIDEO_TIME, time);
        intent.putExtra(GlobalConstants.KEY_VIDEO_PATH, filePath);
        intent.putExtra(GlobalConstants.KEY_VIDEO_URI,
                GlobalUtil.getVideoUriByPath(instance, filePath));

//        if (bChooseFirst) {
//            intent.setClass(instance, WcAddActivity.class);
//            intent.putExtra("send_type", 2);
//            instance.startActivity(intent);
//        } else {
        instance.setResult(Activity.RESULT_OK, intent);
//        }
        instance.finish();
    }

    /**
     * 获取pictureView 可添加大小
     * @param pictrueView
     * @return
     */
    private static int getPicAddSize(PictureGridView pictrueView) {
        if (pictrueView == null) {
            L.e("应该错误了吧");
            return 0;
        }
        int nImgNum = pictrueView.getImageSelectInfo();
        int canAddSize = pictrueView.getAdapter().getMaxPicture() - nImgNum;
        return canAddSize;
    }

    /**
     * 获取pictureView 是否能添加
     * @param pictrueView
     * @return
     */
    private static boolean getPicCanAdd(PictureGridView pictrueView) {
        if (pictrueView == null) {
            return false;
        }
        return pictrueView.getAdapter().canAdd();
    }

    public static void addMyFile(Activity ctx) {
//        Intent intent = new Intent(ctx, FileActivity.class);
//        intent.putExtra("type", EnumData.DocumentType.MY.value());
//        //TODO  传递选择文件为true
//        intent.putExtra(GlobalConstants.KEY_IS_SELECT_FILE, true);
//        FileActivity.getPaths().clear();
//        ctx.startActivityForResult(intent, REQ_GET_FILE_URL);
    }


    public static void addCompanyFile(final SharedTitleActivity ctx) {
//        Intent intent = new Intent(ctx, FileActivity.class);
//        intent.putExtra("type", EnumData.DocumentType.COMPANY.value());
//        //TODO  传递选择文件为true
//        intent.putExtra(GlobalConstants.KEY_IS_SELECT_FILE, true);
//        FileActivity.getPaths().clear();
//        ctx.startActivityForResult(intent, REQ_GET_FILE_URL);
    }


    public static void addLocalFile(final SharedTitleActivity ctx) {
        addLocalFile(ctx, 9);
    }

    public static void addLocalFile(final SharedTitleActivity ctx, Integer maxSelect) {
        addLocalFile(ctx, maxSelect, FmActivity.FILE_ALL_TYPE);
    }

    public static void addLocalFile(final SharedTitleActivity ctx, Integer maxSelect, int nodeType) {
        addLocalFile(ctx, maxSelect, nodeType, REQ_GET_FILE);
    }

    /**
     * @param ctx       上下文对象
     * @param maxSelect 可以选择的最大文件数
     * @param nodeType 是否只展现特定文件的类别
     * @param requestId 指定请求码
     */
    public static void addLocalFile(final SharedTitleActivity ctx, Integer maxSelect, int nodeType, int requestId) {
        Intent intent = new Intent(ctx, FmActivity.class);
        if (maxSelect != null) {
            intent.putExtra("maxSelect", maxSelect);
        }
        intent.putExtra(FmActivity.FILE_TYPE_KEY, nodeType);
        ctx.startActivityForResult(intent, requestId);
    }

}
