/**
 * ImageListActivity.java ImageChooser
 * <p/>
 * Created by likebamboo on 2014-4-23 Copyright (c) 1998-2014 http://likebamboo.github.io/ All
 * rights reserved.
 */

package com.weqia.wq.component.imageselect;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.datastorage.file.FileUtil;
import com.weqia.utils.datastorage.file.StorageUtil;
import com.weqia.utils.imageselect.ImageEntity;
import com.weqia.utils.imageselect.ImageGroup;
import com.weqia.utils.imageselect.ImageLoadTask;
import com.weqia.utils.imageselect.TaskUtil;
import com.weqia.utils.imageselect.VideoLoadTask;
import com.weqia.utils.imageselect.service.OnTaskResultListener;
import com.weqia.wq.R;
import com.weqia.wq.component.SelectArrUtil;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;
import com.weqia.wq.component.imageselect.assist.ImageListAdapter;
import com.weqia.wq.component.imageselect.assist.PicViewAdapter;
import com.weqia.wq.component.imageselect.assist.SelectAttachEnum.CropEnum;
import com.weqia.wq.component.imageselect.assist.SelectAttachEnum.WithSourceEnum;
import com.weqia.wq.component.utils.DialogUtil;
import com.weqia.wq.component.video.VideoRecoderActivity;
import com.weqia.wq.data.EnumData.AttachType;
import com.weqia.wq.data.global.GlobalConstants;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 某个目录下的所有图片列表
 */
public class ImageListActivity extends SharedDetailTitleActivity implements OnItemClickListener {

    private GridView mImagesGv = null;
    private ArrayList<ImageEntity> mImages = new ArrayList<ImageEntity>();
    private ImageListAdapter mImageAdapter = null;
    private ImageLoadTask mImageLoadTask = null;
    private VideoLoadTask mVideoLoadTask = null;

    private ArrayList<ImageGroup> groups = new ArrayList<ImageGroup>();

    private TextView tvCatelog;
    private TextView tvPerview;
    private LinearLayout llBottomBanner;

    public boolean containTake = false;
    private int selectSize;
    public boolean wantSourc = false;

    private PopupWindow popupWindow;
    private ListView lv_group;
    private View view;
    private boolean bVideo = false;
    private boolean crop = false;
    private int cropType = -1;


    // 工作圈先选择图片或者视频再到新增界面
//    private boolean bToWebo = false;

    public static ImageListActivity instance;

    public static ImageListActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        instance = this;

        int type;
        String tmptype = getIntent().getStringExtra(SelectMediaUtils.KEY_SELECT_TYPE);
        if (StrUtil.isEmptyOrNull(tmptype)) {
            type = AttachType.PICTURE.value();
        } else {
            type = Integer.parseInt(tmptype);
        }
        String string = getIntent().getStringExtra(SelectMediaUtils.KEY_IMAGE_SELECT_SIZE);
        if (string != null) {
            selectSize = Integer.parseInt(string);
        } else {
            selectSize = GlobalConstants.IMAGE_MAX;
        }

        String wSource = getIntent().getStringExtra(SelectMediaUtils.KEY_WITH_SOURCE);
        if (StrUtil.notEmptyOrNull(wSource) && wSource.equalsIgnoreCase(WithSourceEnum.YES.value())) {
            wantSourc = true;
        }

        String cropStr = getIntent().getStringExtra(SelectMediaUtils.KEY_CROP);
        if (StrUtil.notEmptyOrNull(cropStr) && cropStr.equalsIgnoreCase(CropEnum.YES.value())) {
            crop = true;
        }

        String cType = getIntent().getStringExtra(SelectMediaUtils.KEY_CROP_TYPE);
        if (StrUtil.notEmptyOrNull(cType)) {
            int tmpType = -1;
            try {
                tmpType = Integer.parseInt(cType);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            cropType =tmpType;
        }

        String tmp = getIntent().getStringExtra(SelectMediaUtils.KEY_TO_WEBO);
//        if (StrUtil.notEmptyOrNull(tmp) && tmp.equals(SelectMediaUtils.KEY_TO_WEBO)) {
//            bToWebo = true;
//        }
        initView();

        containTake = true;
        tvCatelog = (TextView) findViewById(R.id.tv_catelog);
        tvPerview = (TextView) findViewById(R.id.tv_perview);
//        llBottomBanner = (LinearLayout) findViewById(R.id.ll_bottom_banner);
//        if (llBottomBanner != null) {
//            llBottomBanner.getBackground().setAlpha(225);
//        }

        ViewUtils.bindClickListenerOnViews(this, tvCatelog, tvPerview);

        if (type == AttachType.VIDEO.value()) {
            bVideo = true;
        }

        if (selectSize == 1) {
            ViewUtils.hideViews(tvPerview, sharedTitleView.getButtonStringRight());
        } else {
            ViewUtils.showView(sharedTitleView.getButtonStringRight());
        }

        if (bVideo) {
            sharedTitleView.initTopBanner("视频选择");
            tvCatelog.setText("所有视频");
            loadVideo();
            ViewUtils.hideViews(tvPerview, sharedTitleView.getButtonRight());
        } else {
            sharedTitleView.initTopBanner("图片选择");
            tvCatelog.setText("所有图片");
            loadImages();
        }
    }


    private void loadImages() {
        if (!StorageUtil.hasExternalStorage()) {
            return;
        }

        // 线程正在执行
        if (mImageLoadTask != null && mImageLoadTask.getStatus() == Status.RUNNING) {
            return;
        }

        mImageLoadTask = new ImageLoadTask(this, new OnTaskResultListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResult(boolean success, String error, Object result) {
                // 如果加载成功
                if (success && result != null && result instanceof ArrayList) {
                    // setImageAdapter((ArrayList<ImageGroup>)result);
                    groups = (ArrayList<ImageGroup>) result;
                    ArrayList<ImageEntity> tmpEntities = getAllEntits();
                    mImages = tmpEntities;
                    ImageGroup allGroup = new ImageGroup();
                    allGroup.setDirName("所有图片");
                    allGroup.setSelected(true);
                    allGroup.setImages(tmpEntities);
                    groups.add(0, allGroup);
                    setAdapter(mImages);
                } else {
                    // 加载失败，显示错误提示
                }
            }
        });
        TaskUtil.execute(mImageLoadTask);
    }

    private void loadVideo() {
        if (!StorageUtil.hasExternalStorage()) {
            return;
        }

        // 线程正在执行
        if (mVideoLoadTask != null && mVideoLoadTask.getStatus() == Status.RUNNING) {
            return;
        }

        mVideoLoadTask = new VideoLoadTask(this, new OnTaskResultListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onResult(boolean success, String error, Object result) {
                // 如果加载成功
                if (success && result != null && result instanceof ArrayList) {
                    // setImageAdapter((ArrayList<ImageGroup>)result);
                    groups = (ArrayList<ImageGroup>) result;
                    ArrayList<ImageEntity> tmpEntities = getAllEntits();
                    mImages = tmpEntities;
                    ImageGroup allGroup = new ImageGroup();
                    allGroup.setDirName("所有视频");
                    allGroup.setSelected(true);
                    allGroup.setImages(tmpEntities);
                    groups.add(0, allGroup);
                    setAdapter(mImages);
                } else {
                    // 加载失败，显示错误提示
                }
            }
        });
        TaskUtil.execute(mVideoLoadTask);
    }


    public int getSelectSize() {
        return selectSize;
    }

    /**
     * 初始化界面元素
     */
    private void initView() {
        mImagesGv = (GridView) findViewById(R.id.images_gv);
    }

    /**
     * 构件并初始化适配器
     *
     * @param datas
     */
    private void setAdapter(ArrayList<ImageEntity> datas) {
        mImageAdapter = new ImageListAdapter(this, datas, mImagesGv);
        mImagesGv.setAdapter(mImageAdapter);
        mImagesGv.setOnItemClickListener(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget .AdapterView,
     * android.view.View, int, long)
     */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (bVideo) {
            if (containTake) {
                if (position == 0) {
                    try {
                        if (SelectArrUtil.getInstance().getSelImgSize() >= selectSize) {
                            L.toastShort("最多只能选择" + selectSize + "张照片");
                            return;
                        }
                        takeVideo();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }

            ImageEntity posImageEntity = mImageAdapter.getItem(position);
            if (posImageEntity != null) {
                sendConfirm(posImageEntity);
            }
        } else {
            int size = position;
            if (containTake) {
                if (position == 0) {
                    try {
                        if (SelectArrUtil.getInstance().getSelImgSize() >= selectSize) {
                            L.toastShort("最多只能选择" + selectSize + "张照片");
                            return;
                        }
                        SelectMediaUtils.takePicture(instance);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
                size -= 1;
            }

            if (selectSize == 1) {
                ImageEntity imageEntity = (ImageEntity) parent.getItemAtPosition(position);
                if (imageEntity != null) {
                    SelectArrUtil.getInstance().addImage(imageEntity.getPath(), 1);
                }
                if (crop && cropType != -1) {
                    SelectMediaUtils.filePathOriginal = imageEntity.getPath();
                    SelectMediaUtils.takeWithCropPictureResult(instance, cropType);
                    return;
                }
                onClickDo(sharedTitleView.getButtonStringRight());
                return;
            }

            Intent i = new Intent(this, ImageBrowseActivity.class);
            ArrayList<String> paramStrs = new ArrayList<String>();
            for (ImageEntity imageEntity : mImages) {
                if (StrUtil.notEmptyOrNull(imageEntity.getPath())) {
                    paramStrs.add(imageEntity.getPath());
                }
            }
            i.putExtra(SelectMediaUtils.EXTRA_IMAGES, paramStrs);
            i.putExtra(SelectMediaUtils.EXTRA_INDEX, size);
            i.putExtra(SelectMediaUtils.KEY_IMAGE_SELECT_SIZE, selectSize);
            i.putExtra(SelectMediaUtils.KEY_WITH_SOURCE, wantSourc);
//            if (bToWebo) {
//                i.putExtra(SelectMediaUtils.KEY_TO_WEBO, SelectMediaUtils.KEY_TO_WEBO);
//            }
            startActivityForResult(i, 101);
        }
    }

    /**
     * 删除确认
     */
    private void sendConfirm(final ImageEntity imageEntity) {
        String path = imageEntity.getPath();
        String fileSize = FileUtil.getAutoFileOrFilesSize(path);
        Dialog mDialog = DialogUtil.initCommonDialog(this, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case -2:
                        break;
                    case -1:
                        sendVideo(imageEntity);
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        }, "确定发送吗(大小" + fileSize + ")?");
        mDialog.show();
    }

    private void sendVideo(ImageEntity imageEntity) {
        Intent intent = new Intent();
        intent.putExtra(GlobalConstants.KEY_VIDEO_TIME, imageEntity.getDuration());
        intent.putExtra(GlobalConstants.KEY_VIDEO_PATH, imageEntity.getPath());
        intent.putExtra(GlobalConstants.KEY_VIDEO_URI, imageEntity.getLocalUri());
//        if (bToWebo) {
//            intent.putExtra("send_type", 2);
//            intent.setClass(this, WcAddActivity.class);
//            startActivity(intent);
//        } else {
            setResult(RESULT_OK, intent);
//        }
        this.finish();
    }

    /* 保存界面状态，如果activity意外被系统killed，返回时可以恢复状态值 */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("filePathOriginal", SelectMediaUtils.filePathOriginal);
        super.onSaveInstanceState(savedInstanceState); // 实现父类方法 放在最后 防止拍照后无法返回当前activity
    }

    private void takeVideo() {
        startToActivityForResult(VideoRecoderActivity.class, SelectMediaUtils.REQ_TAKE_VIDEO);
    }

    @Override
    public void onBackPressed() {
        backDo();
        super.onBackPressed();
    }

    private void backDo() {
        SelectArrUtil.getInstance().clearImage();
        SelectArrUtil.getInstance().clearSourceImage();
    }

    @Override
    public void onClickDo(View v) {
        if (v == sharedTitleView.getButtonLeft()) {
            backDo();
            this.finish();
        } else if (v == sharedTitleView.getButtonStringRight()) {
//            if (bToWebo) {
//                if (SelectArrUtil.getInstance().getSelImgSize() == 0) {
//                    L.toastShort("请先选择照片");
//                    return;
//                }
//                Intent intent = new Intent(this, WcAddActivity.class);
//                intent.putExtra("send_type", 1);
//                startActivity(intent);
//            } else {
                setResult(RESULT_OK);
//            }
            this.finish();
        } else if (v == tvCatelog) {
            showWindow(tvCatelog);
        } else if (v == tvPerview) {
            ArrayList<String> selectStrs = (ArrayList<String>) SelectArrUtil.getInstance().getSelectedImgs();
            if (SelectArrUtil.getInstance().getSelImgSize() == 0) {
                return;
            } else {
                Intent i = new Intent(this, ImageBrowseActivity.class);
                i.putExtra(SelectMediaUtils.EXTRA_IMAGES, selectStrs);
                i.putExtra(SelectMediaUtils.EXTRA_INDEX, 0);
                i.putExtra(SelectMediaUtils.KEY_WITH_SOURCE, wantSourc);
//                if (bToWebo) {
//                    i.putExtra(SelectMediaUtils.KEY_TO_WEBO, SelectMediaUtils.KEY_TO_WEBO);
//                }
                startActivityForResult(i, 101);
            }
        } else if (v.getId() == R.id.view_empty || v.getId() == R.id.view_empty_two) {
            if (popupWindow != null) {
                popupWindow.dismiss();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRightBt();

        if (mImageAdapter != null) {
            mImageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImages != null) {
            mImages.clear();
        }
        if (groups != null) {
            groups.clear();
        }
        if (mImageLoadTask != null) {
            mImageLoadTask.cancel();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SelectMediaUtils.REQ_TAKE_PHOTO) {
                if (crop && cropType != -1) {
                    SelectMediaUtils.takeWithCropPictureResult(instance, cropType);
                } else {
                    SelectMediaUtils.takePictureResult(instance, wantSourc);
                }
                return;
            } else if (requestCode == SelectMediaUtils.REQ_TAKE_VIDEO) {
                SelectMediaUtils.talkVideoResult(instance, data);
                return;
            }
            if (data!=null) {
                String clipPath = data.getStringExtra(ClipImageActivity.CLIPIMAGEPATH);
                if (StrUtil.notEmptyOrNull(clipPath)) {
                    Intent intent = new Intent();
                    intent.putExtra(ClipImageActivity.CLIPIMAGEPATH, clipPath);
                    this.setResult(RESULT_OK,intent);
                }else {
                    this.setResult(RESULT_OK);
                }
            }else {
                this.setResult(RESULT_OK);
            }
            SelectMediaUtils.filePathOriginal = null;
            this.finish();
        }
    }

    /**
     * 显示
     *
     * @param parent
     */
    @SuppressLint("InflateParams")
    @SuppressWarnings("deprecation")
    private void showWindow(View parent) {

        final PicViewAdapter groupAdapter = new PicViewAdapter(this);
        if (popupWindow == null) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.custom_pic_full_screen_dialog, null);
            lv_group = (ListView) view.findViewById(R.id.lv_pic_item);

            lv_group.setAdapter(groupAdapter);
            groupAdapter.setItems(groups);

            ViewUtils.bindClickListenerOnViews(view, this, R.id.view_empty, R.id.view_empty_two);
            // 创建一个PopuWidow对象
            popupWindow =
                    new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,
                            true);
        }

        lv_group.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                ImageGroup group = groups.get(position);
                if (group.isSelected()) {
                    return;
                }
                if (group != null) {
                    for (int i = 0; i < groups.size(); i++) {
                        if (i == position) {
                            groups.get(i).setSelected(true);
                        } else {
                            groups.get(i).setSelected(false);
                        }
                    }
                    // mImages.clear();
                    if (position == 0) {
                        ArrayList<ImageEntity> tmpEntities = group.getImages();
                        if (tmpEntities == null || tmpEntities.size() == 0) {
                            tmpEntities = getAllEntits();
                        }
                        mImages = tmpEntities;
                        containTake = true;
                    } else {
                        mImages = group.getImages();
                        containTake = false;
                    }
                    groupAdapter.setItems(groups);
                    mImageAdapter.setmDataList(mImages);
                }
            }
        });
        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);

        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void setRightBt() {
        if (bVideo) {
            return;
        } else {
            super.setRightBt(false, selectSize);
            int selectSize = SelectArrUtil.getInstance().getSelImgSize();
            if (selectSize == 0) {
                tvPerview.setText("预览");
            } else {
                tvPerview.setText("预览(" + selectSize + ")");
            }
        }
    }


    private ArrayList<ImageEntity> getAllEntits() {
        ArrayList<ImageEntity> tmpEntities = new ArrayList<ImageEntity>();
        for (ImageGroup imageGroup : groups) {
            if (bVideo) {
                if (imageGroup.getDirName() == "所有视频") {
                    continue;
                }
            } else {
                if (imageGroup.getDirName() == "所有图片") {
                    continue;
                }
            }
            tmpEntities.addAll(imageGroup.getImages());
        }
        Collections.sort(tmpEntities);
        tmpEntities.add(0, new ImageEntity("", 0, null));
        return tmpEntities;
    }

    public boolean isbVideo() {
        return bVideo;
    }
}
