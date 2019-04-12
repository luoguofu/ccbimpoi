package com.example.ccbim.ccbimpoi.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.example.ccbim.ccbimpoi.R;
import com.example.ccbim.ccbimpoi.data.CellData;
import com.example.ccbim.ccbimpoi.data.CheckDetailData;
import com.example.ccbim.ccbimpoi.data.ExcelEnum;
import com.example.ccbim.ccbimpoi.data.ProjectCheckData;
import com.example.ccbim.ccbimpoi.util.BaseUtil;
import com.example.ccbim.ccbimpoi.util.ConstantUtil;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.datastorage.db.DbUtil;
import com.weqia.utils.dialog.SharedCommonDialog;
import com.weqia.wq.component.SelectArrUtil;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;
import com.weqia.wq.component.view.picture.PictureGridView;
import com.weqia.wq.data.eventbus.RefreshEvent;
import com.weqia.wq.data.global.WeqiaApplication;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.ccbim.ccbimpoi.activity.BatchAddActivity.SPLITEXCEL;
import static com.weqia.wq.component.imageselect.SelectMediaUtils.REQ_GET_PIC;

public class SingleFormActivity extends SharedDetailTitleActivity implements View.OnClickListener {
    private int parentPos;
    private int childPos;
    private ProjectCheckData projectCheckData;
    private CheckDetailData checkDetailData;
    private boolean isPass = true;
    private boolean isNOtInvolve = false;
    private CellData parentCellData;
    private String companyName;
    private String projectName;

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
        parentCellData = projectCheckData.getTabBody().get(parentPos);
        status = checkDetailData.getStatus();
        if (checkDetailData != null) {
            checkDetailData.setCheckPath(projectCheckData.getTabBody().get(parentPos).getCellName() + "-" + checkDetailData.getCheckName().getCellName());
        }
        SharedPreferences sharedPreferences= getSharedPreferences("setting", Context.MODE_PRIVATE);
        companyName = sharedPreferences.getString("companyName", "");
        projectName = sharedPreferences.getString("projectName", "");
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
        Drawable drawable = getResources().getDrawable(R.drawable.btn_pitchon);
        drawable.setBounds(0, 0, BaseUtil.dpToPx(getResources(), 20), BaseUtil.dpToPx(getResources(), 15));
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
                mTextQualified.setCompoundDrawables(null, null, drawable, null);
                mTextNotInvolve.setCompoundDrawables(null, null, null, null);
                mTextRectification.setCompoundDrawables(null, null, null, null);
                break;
            case 2:
                mTextQualified.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_normal));
                mTextNotInvolve.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_pressed));
                mTextRectification.setBackground(getResources().getDrawable(R.drawable.bg_btn_red_normal));
                mTextNotInvolve.setCompoundDrawables(null, null, drawable, null);
                mTextQualified.setCompoundDrawables(null, null, null, null);
                mTextRectification.setCompoundDrawables(null, null, null, null);
                break;
            case 3:
                mTextQualified.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_normal));
                mTextNotInvolve.setBackground(getResources().getDrawable(R.drawable.bg_btn_blue_normal));
                mTextRectification.setBackground(getResources().getDrawable(R.drawable.bg_btn_red_pressed));
                mTextRectification.setCompoundDrawables(null, null, drawable, null);
                mTextQualified.setCompoundDrawables(null, null, null, null);
                mTextNotInvolve.setCompoundDrawables(null, null, null, null);
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
                back();
                break;
            case R.id.text_rectification:
                showRectificationDialog(v);
//                checkDetailData.setProblemDemand("需要整改");
                status = 3;
                initStatus();
//                Intent zgIntent = new Intent(this, PictureShowActivity.class);
//                zgIntent.putExtra("assetsName", "zhenggai.png");
//                startActivity(zgIntent);
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
/*                        ProjectCheckData data = new ProjectCheckData();
                        data.setExcelType(2);
                        data.setExcelFullName("整改单");
                        ArrayList<CellData> tabHead = new ArrayList<>();
                        CellData nameCell = new CellData("质量整改通知单","0", "0", "0", "7");
                        nameCell.setFontBlodWeight((short) 800);
                        tabHead.add(nameCell);
//                        tabHead.add(new CellData("质量整改通知单","0", "0", "0", "7"));
                        tabHead.add(new CellData("项目名称","1", "1", "0", "0"));
                        tabHead.add(new CellData("","1", "1", "1", "3"));
                        tabHead.add(new CellData("项目编码","1", "1", "4", "4"));
                        tabHead.add(new CellData("","1", "1", "5", "7"));
                        tabHead.add(new CellData("检查部位","2", "2", "0", "0"));
                        tabHead.add(new CellData("", "2", "2", "1", "2"));
//                        tabHead.add(new CellData(projectCheckData.getCheckPartName(), "2", "2", "1", "2"));
                        tabHead.add(new CellData("检查日期","2", "2", "3", "3"));
                        tabHead.add(new CellData("","2", "2", "4", "4"));
                        tabHead.add(new CellData("分包单位","2", "2", "5", "5"));
                        tabHead.add(new CellData("","2", "2", "6", "7"));
                        tabHead.add(new CellData("整改内容","3", "3", "0", "3"));
                        tabHead.add(new CellData("整改要求（应根据整改内容逐项表述）","3", "3", "4", "7"));
                        data.setTabHead(tabHead);
                        ArrayList<CellData> tabFoot = new ArrayList<>();
//                        tabFoot.add(new CellData(StrUtil.notEmptyOrNull(problem.getText().toString()) ? problem.getText().toString() : "", "4", "6", "0", "3"));
//                        tabFoot.add(new CellData(StrUtil.notEmptyOrNull(rectifivation.getText().toString()) ? rectifivation.getText().toString() : "", "4", "6", "4", "7"));
                        tabFoot.add(new CellData("", "4", "6", "0", "3"));
                        tabFoot.add(new CellData("", "4", "6", "4", "7"));
                        tabFoot.add(new CellData("签发人","7", "7", "0", "1"));
                        tabFoot.add(new CellData("","7", "7", "2", "7"));
                        tabFoot.add(new CellData("复查情况","8", "20", "0", "1"));
                        CellData contentCell=new CellData("                     （应根据整改内容逐项表述）\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "  复查人：（质量总监、质量工程师）\n" +
                                "\n" +
                                "                                              年     月     日" +
                                "\n" ,"8", "20", "2", "7");
                        contentCell.setCellLayout(2);
                        tabFoot.add(contentCell);
                        *//*tabFoot.add(new CellData("              （应根据整改内容逐项表述）\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "\n" +
                                "复查人：（质量总监、质量工程师）\n" +
                                "                                              年     月     日","8", "20", "2", "7"));*//*
                        CellData fileCell = new CellData("附图", "22", "22", "0", "7");
                        fileCell.setCellColor("ffff00");
                        tabFoot.add(fileCell);
                        data.setTabFoot(tabFoot);*/
                        ProjectCheckData data = JSON.parseObject(ExcelEnum.RectifyEnum.RECTIFYEXCEL.getValue(), ProjectCheckData.class);
                        data.setExcelFullName(projectCheckData.getCheckPartName() + SPLITEXCEL + parentCellData.getCellName() + SPLITEXCEL + checkDetailData.getCheckName().getCellName() + "质量整改单");
                        if (StrUtil.notEmptyOrNull(getSharedPreferences("setting", Context.MODE_PRIVATE).getString("projectName", ""))) {
                            data.getTabHead().get(2).setCellName(getSharedPreferences("setting", Context.MODE_PRIVATE).getString("projectName", ""));
                        }
                        data.getTabHead().get(6).setCellName(projectCheckData.getCheckPartName());
                        data.getTabFoot().get(0).setCellName(StrUtil.notEmptyOrNull(problem.getText().toString()) ? problem.getText().toString() : "");
                        data.getTabFoot().get(1).setCellName(StrUtil.notEmptyOrNull(rectifivation.getText().toString()) ? rectifivation.getText().toString() : "");
                        data.setTabHeadStr(data.getTabHead().toString());
                        data.setTabFootStr(data.getTabFoot().toString());
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
                            data.setTabPicStr(buffer.toString());
                        }
                        DbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
                        dbUtil.save(data);
                        L.toastShort("生成整改单成功！");
//                        SaveToExcelUtil.exportRectifyEccel(SingleFormActivity.this,getPoiExcelDir() + File.separator + data.getExcelFullName() + ".xls",data);
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
