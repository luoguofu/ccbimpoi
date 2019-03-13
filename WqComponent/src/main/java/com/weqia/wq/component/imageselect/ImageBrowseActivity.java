/**
 * ImageBrowseActivity.java ImageChooser
 * <p/>
 * Created by likebamboo on 2014-4-22 Copyright (c) 1998-2014 http://likebamboo.github.io/ All
 * rights reserved.
 */

package com.weqia.wq.component.imageselect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.weqia.utils.StrUtil;
import com.weqia.utils.TimeUtils;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.datastorage.file.FileUtil;
import com.weqia.utils.datastorage.file.PathUtil;
import com.weqia.wq.R;
import com.weqia.wq.component.SelectArrUtil;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;
import com.weqia.wq.component.imageselect.assist.ImagePagerAdapter;
import com.weqia.wq.data.global.GlobalConstants;
import com.yjing.imageeditlibrary.editimage.EditImageActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * 大图浏览Activity
 *
 * @author likebamboo
 */
public class ImageBrowseActivity extends SharedDetailTitleActivity {

    public String saveFilePath;// 生成的新图片路径


    private ArrayList<String> mDatas = new ArrayList<String>();

    private int mPageIndex = 0;
    private ImagePagerAdapter mImageAdapter = null;
    private ViewPager mViewPager = null;

    private ImageButton btSel;
    private TextView tvSel;

    private ImageButton btSource;
    private TextView tvSource;
    private TextView tvEdit;
    private int selectSize;
    private boolean bPhoto;
    // 工作圈先选择图片或者视频再到新增界面
//    private boolean toWebo = false;
    private boolean wantSource = false;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browse);
        sharedTitleView.initTopBanner("图片选择");
        String tmp = getIntent().getStringExtra(SelectMediaUtils.KEY_TO_WEBO);
//        if (StrUtil.notEmptyOrNull(tmp) && tmp.equals(SelectMediaUtils.KEY_TO_WEBO)) {
//            toWebo = true;
//        }
        /**
         *通知栏没有透明度，导致和通知栏不一致，故移除
         */
        // sharedTitleView.setAlpha(0.7f);
        mViewPager = (ViewPager) findViewById(R.id.image_vp);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                setSelectedInfo(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        wantSource = getIntent().getExtras().getBoolean(SelectMediaUtils.KEY_WITH_SOURCE, false);

        btSel = (ImageButton) findViewById(R.id.bt_sel);
        tvSel = (TextView) findViewById(R.id.tv_sel);
        btSource = (ImageButton) findViewById(R.id.bt_source);
        tvSource = (TextView) findViewById(R.id.tv_source);
        tvEdit = (TextView) findViewById(R.id.tv_edit);

        ViewUtils.bindClickListenerOnViews(this, btSel, tvSel, btSource, tvSource, tvEdit);
        Intent intent = getIntent();
        if (intent.hasExtra(SelectMediaUtils.EXTRA_IMAGES)) {
            mDatas = intent.getStringArrayListExtra(SelectMediaUtils.EXTRA_IMAGES);
            mPageIndex = intent.getIntExtra(SelectMediaUtils.EXTRA_INDEX, 0);
            bPhoto = intent.getBooleanExtra(SelectMediaUtils.EXTRA_BE_PHOTO, false);
            selectSize =
                    intent.getIntExtra(SelectMediaUtils.KEY_IMAGE_SELECT_SIZE,
                            GlobalConstants.IMAGE_MAX);
            mImageAdapter = new ImagePagerAdapter(mDatas);
            mViewPager.setAdapter(mImageAdapter);
            mViewPager.setCurrentItem(mPageIndex);
            setSelectedInfo(mPageIndex);
        }
    }

    protected void setSelectedInfo(int arg0) {
        String path = mDatas.get(arg0);
        if (StrUtil.notEmptyOrNull(path)) {
            sharedTitleView.initTopBanner(new File(path).getName());
            if (SelectArrUtil.getInstance().isSelImgContain(path)) {
                btSel.setSelected(true);
            } else {
                btSel.setSelected(false);
            }

            if (wantSource && !btSel.isSelected()) {
                SelectArrUtil.getInstance().addSourceImage(path);
            }

            if (SelectArrUtil.getInstance().isSelImgSourceContain(path)) {
                btSource.setSelected(true);
            } else {
                btSource.setSelected(false);
            }

            tvSource.setText("原图(" + FileUtil.getAutoFileOrFilesSize(path) + ")");
            if (PathUtil.isPathInDisk(path)) {
                ViewUtils.showViews(tvSource, btSource);
            } else {
                ViewUtils.hideViews(tvSource, btSource);
            }
        }
    }


    @Override
    protected void onClickDo(View v) {
        if (v == sharedTitleView.getButtonLeft()) {
            if (bPhoto) {
                SelectArrUtil.getInstance().clearImage();
                SelectArrUtil.getInstance().clearSourceImage();
            }
            this.finish();

        } else if (v == sharedTitleView.getButtonStringRight()) {
//            if (toWebo) {
//                Intent intent = new Intent();
//                intent.putExtra("send_type", 1);
//                intent.setClass(this, WcAddActivity.class);
//                startActivity(intent);
//                if (ImageListActivity.getInstance() != null) {
//                    ImageListActivity.getInstance().finish();
//                }
//                this.finish();
//            } else {
                setResult(RESULT_OK);
                this.finish();
//            }
            return;
        }
        if (mViewPager == null || mDatas == null) {
            return;
        }
        int index = mViewPager.getCurrentItem();
        if (mDatas.size() <= index) {
            return;
        }
        String path = mDatas.get(index);
        if (StrUtil.isEmptyOrNull(path)) {
            return;
        }
        if (v == btSel || v == tvSel) {
            if (!btSel.isSelected()) {
                if (!SelectArrUtil.getInstance().addImage(path, selectSize)) {
                    return;
                }
            } else {
                SelectArrUtil.getInstance().deleteImage(path);
            }
            btSel.setSelected(!btSel.isSelected());
            if (bPhoto) {
                setRightBt(true, 1);
            } else {
                setRightBt(true, selectSize);
            }
        } else if (v == btSource || v == tvSource) {
            btSourceClick(path);
        } else if (v == tvEdit) {
            /**
             *编辑图片
             */
            saveFilePath =
                    PathUtil.getWQPath() + File.separator + TimeUtils.getFileSaveTime() + ".jpg";
            Intent it = new Intent(this, EditImageActivity.class);
            it.putExtra(EditImageActivity.FILE_PATH, path);
            it.putExtra(EditImageActivity.EXTRA_OUTPUT, saveFilePath);
            startActivityForResult(it, 111);
        }
    }

    protected void btSourceClick(String path) {
        if (!btSource.isSelected()) {
            if (!SelectArrUtil.getInstance().addSourceImage(path)) {
                return;
            }
        } else {
            SelectArrUtil.getInstance().deleteSourceImage(path);
        }
        btSource.setSelected(!btSource.isSelected());
        if (bPhoto) {
            setRightBt(true, 1);
        } else {
            setRightBt(true, selectSize);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (bPhoto) {
            SelectArrUtil.getInstance().clearImage();
            SelectArrUtil.getInstance().clearSourceImage();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111) {
            String newFilePath = data.getStringExtra(EditImageActivity.SAVE_FILE_PATH);
            if (StrUtil.notEmptyOrNull(newFilePath)&&new File(newFilePath).exists()){
                int mPageIndex = mViewPager.getCurrentItem();
                /**
                 *把原来的图片路径替换为新的图片地址，需在选中图片数组里面移除老地址
                 */
                if (StrUtil.listNotNull(SelectArrUtil.getInstance().getSelectedImgs())){
                    String oldStr = mDatas.get(mPageIndex);
                    if (StrUtil.notEmptyOrNull(oldStr)&&SelectArrUtil.getInstance().getSelectedImgs().contains(oldStr)){
                        SelectArrUtil.getInstance().getSelectedImgs().remove(oldStr);
                    }
                }

                /**
                 *编辑的图片经过压缩，上传再次压缩就会失真，需要原图上传，由于已经压缩，原图也不会太大
                 */
                SelectArrUtil.getInstance().addSourceImage(newFilePath);
                SelectArrUtil.getInstance().addImage(newFilePath);
                mDatas.set(mPageIndex, newFilePath);
                mViewPager.setAdapter(mImageAdapter);
                mViewPager.setCurrentItem(mPageIndex);
                setSelectedInfo(mPageIndex);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (bPhoto) {
            setRightBt(true, 1);
        } else {
            setRightBt(true, selectSize);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
