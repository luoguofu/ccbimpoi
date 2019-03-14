package com.example.ccbim.ccbimpoi.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.ccbim.ccbimpoi.R;
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
    private PictureGridView mPictureGridView;
    /**
     * sample
     */
    private Button mTextSample;
    private ImageButton mTextTakePicture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_form_activity_layout);
        initView();
    }

    private void initView() {
        mTextQualified = (Button) findViewById(R.id.text_qualified);
        mTextQualified.setOnClickListener(this);
        mTextNotInvolve = (Button) findViewById(R.id.text_not_involve);
        mTextNotInvolve.setOnClickListener(this);
        mTextRectification = (Button) findViewById(R.id.text_rectification);
        mTextRectification.setOnClickListener(this);
        mPictureGridView = (PictureGridView) findViewById(R.id.picture_grid_view);
        mTextSample = (Button) findViewById(R.id.text_sample);
        mTextSample.setOnClickListener(this);
        mTextTakePicture = (ImageButton) findViewById(R.id.text_take_picture);
        mTextTakePicture.setOnClickListener(this);
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
                break;
            case R.id.text_sample:
                break;
            case R.id.text_take_picture:
                break;
        }
    }
}
