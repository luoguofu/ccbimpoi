package com.example.ccbim.ccbimpoi.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.example.ccbim.ccbimpoi.R;
import com.example.ccbim.ccbimpoi.data.CheckDetailData;
import com.example.ccbim.ccbimpoi.data.ProjectCheckData;
import com.example.ccbim.ccbimpoi.util.ConstantUtil;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.dialog.SharedCommonDialog;
import com.weqia.wq.component.SelectArrUtil;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;
import com.weqia.wq.component.utils.DialogUtil;
import com.weqia.wq.component.view.picture.PictureGridView;
import com.weqia.wq.data.eventbus.RefreshEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.weqia.wq.component.imageselect.SelectMediaUtils.REQ_GET_PIC;

public class SingleFormActivity extends SharedDetailTitleActivity implements View.OnClickListener {
    private int parentPos;
    private int childPos;
    private ProjectCheckData projectCheckData;
    private CheckDetailData checkDetailData;
    private boolean isPass = true;
    private boolean isNOtInvolve = false;

    /**
     * qualified
     */
    private Button mTextQualified;
    /**
     * not_involve
     */
    private Button mTextNotInvolve;
    /**
     * rectification
     */
    private Button mTextRectification;
    private LinearLayout mPictureGridView;
    /**
     * sample
     */
    private Button mTextSample;
    private ImageButton mTextTakePicture;
    private EditText mEditText;
    /**
     * 纪录点什么...
     */
    private EditText mEditRecord;
    private PictureGridView pictrueView;
    private LinearLayout llPicture;
    private int status = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_form_activity_layout);
        EventBus.getDefault().register(this);
        parentPos = getIntent().getIntExtra("parentPos", 0);
        childPos = getIntent().getIntExtra("childPos", 0);
        projectCheckData = (ProjectCheckData) getIntent().getSerializableExtra(ConstantUtil.PROJECTEXTRA);
//        checkDetailData = (CheckDetailData) getIntent().getSerializableExtra("childData");
        checkDetailData = projectCheckData.getTabBody().get(parentPos).getSubCellList().get(childPos);
        status = checkDetailData.getStatus();
        if (checkDetailData != null) {
            checkDetailData.setCheckPath(projectCheckData.getTabBody().get(parentPos).getCellName() + "-" + checkDetailData.getCheckName().getCellName());
        }
        sharedTitleView.initTopBanner(checkDetailData.getCheckName().getCellName());
        initView();
        iniData();
        setTitle("");
    }

    @SuppressLint("NewApi")
    private void iniData() {
        mEditText.setText(checkDetailData.getCheckStandard().getCellName());
        isPass = checkDetailData.getCheckPass().isCellSelected();
        isNOtInvolve = checkDetailData.getCheckInvolve().isCellSelected();
/*        if (isPass) {
            mTextQualified.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_pressed));
        } else {
            mTextQualified.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_normal));
        }
        if (isNOtInvolve) {
            mTextNotInvolve.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_pressed));
        } else {
            mTextNotInvolve.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_normal));
        }*/
        if (StrUtil.notEmptyOrNull(checkDetailData.getRemind())) {
            mEditRecord.setText(checkDetailData.getRemind());
        }
        initStatus();
        if (StrUtil.notEmptyOrNull(checkDetailData.getPicPathsStr())) {
//            String paths = checkDetailData.getPicPathsStr().substring(1, checkDetailData.getPicPathsStr().length() - 1);
            String paths = checkDetailData.getPicPathsStr();
            List<String> list = Arrays.asList(paths.split(","));
            pictrueView.getAddedPaths().addAll(list);
            pictrueView.refresh();
        }
    }

    @SuppressLint("NewApi")
    private void initStatus() {
        switch (status) {
            case 0:
                mTextQualified.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_normal));
                mTextNotInvolve.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_normal));
                mTextRectification.setBackground(getResources().getDrawable(R.drawable.bg_btn_red_normal));
                break;
            case 1:
                mTextQualified.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_pressed));
                mTextNotInvolve.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_normal));
                mTextRectification.setBackground(getResources().getDrawable(R.drawable.bg_btn_red_normal));
                break;
            case 2:
                mTextQualified.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_normal));
                mTextNotInvolve.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_pressed));
                mTextRectification.setBackground(getResources().getDrawable(R.drawable.bg_btn_red_normal));
                break;
            case 3:
                mTextQualified.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_normal));
                mTextNotInvolve.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_normal));
                mTextRectification.setBackground(getResources().getDrawable(R.drawable.bg_btn_red_pressed));
                break;

        }
    }

    private void initView() {
        mTextQualified = (Button) findViewById(R.id.text_qualified);
        mTextQualified.setOnClickListener(this);
        mTextNotInvolve = (Button) findViewById(R.id.text_not_involve);
        mTextNotInvolve.setOnClickListener(this);
        mTextRectification = (Button) findViewById(R.id.text_rectification);
        mTextRectification.setOnClickListener(this);
//        mPictureGridView = (LinearLayout) findViewById(R.id.picture_grid_view);
        mTextSample = (Button) findViewById(R.id.text_sample);
        mTextSample.setOnClickListener(this);
//        mTextTakePicture = (ImageButton) findViewById(R.id.text_take_picture);
//        mTextTakePicture.setOnClickListener(this);
        mEditText = (EditText) findViewById(R.id.edit_text);
        mEditRecord = (EditText) findViewById(R.id.edit_record);
        llPicture = (LinearLayout) findViewById(R.id.llPicture);
        pictrueView = new PictureGridView(this) {

            @Override
            public void deletePic(String path) {
                super.deletePic(path);
                SelectArrUtil.getInstance().deleteImage(path);
            }
        };
        if (pictrueView != null) {
            llPicture.addView(pictrueView);
        }
        llPicture.setOnClickListener(this);
//        mPictureGridView.setOnClickListener(this);
//        pictrueView = new PictureGridView(this) {
//
//            @Override
//            public void deletePic(String path) {
//                super.deletePic(path);
//                SelectArrUtil.getInstance().deleteImage(path);
//            }
//        };
//        if (pictrueView != null) {
//            mPictureGridView.addView(pictrueView);
//        }

    }
    // 添加成功 刷新界面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final RefreshEvent event) {

    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.text_qualified:
/*                isPass = !isPass;
                if (isPass) {
                    mTextQualified.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_pressed));
                } else {
                    mTextQualified.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_normal));
                }
                checkDetailData.getCheckPass().setCellSelected(isPass);*/
                status = 1;
                initStatus();
                break;
            case R.id.text_not_involve:
/*                isNOtInvolve = !isNOtInvolve;
                if (isNOtInvolve) {
                    mTextNotInvolve.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_pressed));
                } else {
                    mTextNotInvolve.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_normal));
                }
                checkDetailData.getCheckInvolve().setCellSelected(isNOtInvolve);*/
                status = 2;
                initStatus();
                break;
            case R.id.text_rectification:
//                showRectificationDialog(v);
//                checkDetailData.setProblemDemand("需要整改");
                status = 3;
                initStatus();
                Intent zgIntent = new Intent(this, PictureShowActivity.class);
                zgIntent.putExtra("assetsName", "zhenggai.png");
                startActivity(zgIntent);
                break;
            case R.id.text_sample:
//                Bitmap bitmap = getImageFromAssetsFile(this, "fengjinbaohu");
//                ImageView imageView = new ImageView(this);
//                imageView.setImageBitmap(bitmap);
                if (StrUtil.notEmptyOrNull(checkDetailData.getSamplePic())) {
                    Intent intent = new Intent(this, PictureShowActivity.class);
                    intent.putExtra("assetsName", checkDetailData.getSamplePic());
                    startActivity(intent);
                } else {
                    L.toastShort("暂无示例图片");
                }
                break;
            case R.id.topbanner_button_left:
                back();
                break;
//            case R.id.text_take_picture:
//                break;
//            case R.id.picture_grid_view:
//                break;
        }
    }

    private void showRectificationDialog(View v) {
        SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.rectification_dialog_layout, null);
        final EditText problem = (EditText) view.findViewById(R.id.tv_problem);
        final EditText rectifivation = (EditText) view.findViewById(R.id.tv_rectifivation);
        builder.setContentView(view)
                .showBar(false)
                .setPositiveButton(getString(com.weqia.wq.R.string.dialog_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkDetailData.setProblemInfo(problem.getText().toString());
                        checkDetailData.setProblemDemand(rectifivation.getText().toString());
                        dialog.dismiss();

                    }
                })
                .setNegativeButton(getString(com.weqia.wq.R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_GET_PIC) {
                if (pictrueView != null) {
                    pictrueView.getAddedPaths().clear();
                    for (int i = 0; i < SelectArrUtil.getInstance().getSelImgSize(); i++) {
                        pictrueView.getAddedPaths().add(SelectArrUtil.getInstance().getSelImg(i));
                    }
                    pictrueView.refresh();
                }
            }
        }
    }

/* 读取Assets文件夹中的图片资源
 * @param context
 * @param fileName 图片名称
 * @return
  */
    public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }

    private void back() {
        checkDetailData.setRemind(mEditRecord.getText().toString());
        if (StrUtil.listNotNull(pictrueView.getAddedPaths())) {
            ArrayList<String> list = pictrueView.getAddedPaths();
            StringBuffer buffer = new StringBuffer();
            for (String str : list) {
                str = SelectArrUtil.getDecodePath(str);
                if (buffer.length() == 0) {
                    buffer.append(str);
                } else {
                    buffer.append("," + str);
                }
            }
            checkDetailData.setPicPathsStr(buffer.toString());
        }
//        checkDetailData.setPicPathsStr(pictrueView.getAddedPaths().toString());
/*        if (StrUtil.notEmptyOrNull(checkDetailData.getProblemDemand())) {
            checkDetailData.setStatus(2);
        }else {
            if (checkDetailData.getCheckPass().isCellSelected() || checkDetailData.getCheckInvolve().isCellSelected()) {
                checkDetailData.setStatus(1);
            } else {
                checkDetailData.setStatus(0);
            }
        }*/
        checkDetailData.setStatus(status);
        if (checkDetailData.getStatus() == 1) {
            checkDetailData.getCheckPass().setCellName("☑");
            checkDetailData.getCheckInvolve().setCellName("口");
        } else if (checkDetailData.getStatus() == 2) {
            checkDetailData.getCheckPass().setCellName("口");
            checkDetailData.getCheckInvolve().setCellName("☑");
        } else {
            checkDetailData.getCheckPass().setCellName("口");
            checkDetailData.getCheckInvolve().setCellName("口");
        }
/*        if (checkDetailData.getCheckInvolve().isCellSelected()) {
            checkDetailData.getCheckInvolve().setCellName("☑");
        } else {
            checkDetailData.getCheckInvolve().setCellName("口");
        }*/
        pictrueView.getAddedPaths().clear();
        SelectArrUtil.getInstance().clearImage();
        EventBus.getDefault().post(new RefreshEvent("projectdata",projectCheckData));
        finish();
    }
}
