package com.example.ccbim.ccbimpoi.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.ccbim.ccbimpoi.R;
import com.weqia.utils.view.CommonImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lgf on 2019/3/18.
 */

public class PictureShowActivity extends AppCompatActivity{
    private CommonImageView civPicture;
    private String assetsName;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        civPicture = (CommonImageView) findViewById(R.id.civ_picture);
        assetsName = getIntent().getStringExtra("assetsName");
        Bitmap bitmap = getImageFromAssetsFile(this, assetsName);
//        civPicture.setImageDrawable(this.getDrawable(R.drawable.fengjinbaohu));
        civPicture.setImageBitmap(bitmap);
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

    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
