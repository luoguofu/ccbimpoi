package com.example.ccbim.ccbimpoi.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.ccbim.ccbimpoi.R;
import com.weqia.utils.dialog.SharedCommonDialog;
import com.weqia.wq.component.SelectArrUtil;
import com.weqia.wq.component.utils.DialogUtil;
import com.weqia.wq.component.view.picture.PictureGridView;

public class SingleFormActivity extends AppCompatActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_form_activity_layout);
        initView();
        setTitle("");
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
        mTextTakePicture = (ImageButton) findViewById(R.id.text_take_picture);
        mTextTakePicture.setOnClickListener(this);
        mEditText = (EditText) findViewById(R.id.edit_text);
        mEditRecord = (EditText) findViewById(R.id.edit_record);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.text_qualified:
                break;
            case R.id.text_not_involve:
                break;
            case R.id.text_rectification:
                showRectificationDialog(v);
                break;
            case R.id.text_sample:
                break;
            case R.id.text_take_picture:
                break;
//            case R.id.picture_grid_view:
//                break;
        }
    }

    private void showRectificationDialog(View v) {
        SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.rectification_dialog_layout, null);
        EditText problem = (EditText) view.findViewById(R.id.tv_problem);
        EditText rectifivation = (EditText) view.findViewById(R.id.tv_rectifivation);
        builder.setContentView(view)
                .showBar(false)
                .setPositiveButton(getString(com.weqia.wq.R.string.dialog_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
}
