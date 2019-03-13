package com.weqia.wq.component.view.picture;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.bitmap.BitmapUtil;
import com.weqia.utils.bitmap.big.XImageView;
import com.weqia.utils.data.DialogData;
import com.weqia.utils.data.LocalNetPath;
import com.weqia.utils.datastorage.file.FileUtil;
import com.weqia.utils.datastorage.file.NativeFileUtil;
import com.weqia.utils.datastorage.file.PathUtil;
import com.weqia.utils.dialog.SharedFullScreenDialog;
import com.weqia.utils.http.okgo.callback.FileCallback;
import com.weqia.utils.http.okserver.download.DownloadManager;
import com.weqia.utils.view.CommonImageView;
import com.weqia.wq.R;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;
import com.weqia.wq.component.imageselect.ClipImageActivity;
import com.weqia.wq.component.imageselect.SelectMediaUtils;
import com.weqia.wq.component.imageselect.assist.SelectAttachEnum;
import com.weqia.wq.component.utils.FileMiniUtil;
import com.weqia.wq.component.utils.LnUtil;
import com.weqia.wq.component.utils.NetworkUtil;
import com.weqia.wq.component.utils.bitmap.PictureUtil;
import com.weqia.wq.component.utils.request.UserService;
import com.weqia.wq.component.view.GifView;
import com.weqia.wq.data.EnumData.AttachType;
import com.weqia.wq.data.EnumData.ImageThumbTypeEnums;
import com.weqia.wq.data.TransData;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.data.global.WeqiaApplication;
import com.weqia.wq.global.ComponentUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static android.graphics.BitmapFactory.decodeResource;

// 图片预览组件
public class PicturePagerActivity extends SharedDetailTitleActivity
        implements
        OnClickListener,
        OnPageChangeListener {
    private HackyViewPager mViewPager;
    private PicturePagerActivity ctx;
    private int current;
    private ArrayList<String> datas = null;
    private Dialog saveImgDialog;
    private static int idBegin = 1000;
    private LinearLayout llDot;
    private FrameLayout mTitle;
    private boolean bShowDot = true;
    private int downloadKey = -1000;
    private int forwardKey = -1001;
    private int qrInfoKey = -1002;
    private boolean bChangeHead = false;
    private LinearLayout bottomView;
    private int pictureType = 1;
    private SamplePagerAdapter mAdapter;
    private Rect startBounds = null;
    /**
     * qrPath里面存在路径，表示图片含有二维码
     */
    private String qrPath = "";
    private String qrInfo = "";

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            View view = (View) msg.obj;
            String path = msg.getData().getString("fileAbsolutePath");
            String url = msg.getData().getString("urlName");
            if (StrUtil.notEmptyOrNull(path)) {
                setImageDo(view, url, new File(path), false);
            }
        }
    };

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        setContentView(R.layout.view_scan_picture_base);
        ctx = this;
        mTitle = (FrameLayout) findViewById(R.id.top_layout);
        mViewPager = (HackyViewPager) findViewById(R.id.vpPicture);
        bottomView = (LinearLayout) findViewById(R.id.ll_bottom_view);
        ViewUtils.hideView(bottomView);
        llDot = (LinearLayout) findViewById(R.id.llDot);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            bundle = new Bundle();
        }
        bChangeHead = getIntent().getBooleanExtra(GlobalConstants.CHANGE_HEAD_PICS, false);
        if (bundle.get("startBounds") != null) {
            startBounds = (Rect) bundle.get("startBounds");
        }
        if (bChangeHead) {
            bChange = false;//更换头像添加标题栏！
            sharedTitleView.initTopBanner("头像", R.drawable.bg_title);
            pictureType = bundle.getInt(PictureUtil.PITURE_TYPE, 1);
        }
        current = bundle.getInt("current");
        bShowDot = bundle.getBoolean("bShowDot", true);
        datas = bundle.getStringArrayList(GlobalConstants.LIST_PICS);
        mAdapter = new SamplePagerAdapter(datas);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(current);
        // 进度显示两张图片的
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setOnPageChangeListener(this);
//        saveImgDialog = initSaveImgDialog(PicturePagerActivity.this, this);
        if (!bShowDot) {
            mTitle.setVisibility(View.GONE);
        } else {
            initDot();
            mTitle.setVisibility(View.GONE);
        }
        if (bChangeHead) {
            ViewUtils.showView(mTitle);
        }
        ViewUtils.hideView(sharedTitleView.getTvRight());
        ViewUtils.bindClickListenerOnViews(ctx, ctx, R.id.tv_change_pic_from_phone, R.id.tv_save_pic_to_phone);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            L.e("此时为横屏模式");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            L.e("此时为竖屏模式");
        }
    }

    private void backDo() {
        if (StrUtil.listNotNull(datas)) {
            String endStr = "&th=" + ImageThumbTypeEnums.THUMB_BIG.value();
            for (String str : datas) {
                if (!str.endsWith(endStr))
                    str = str + endStr;
                DownloadManager.getInstance().pauseTask(str);
            }
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, 50);
    }

    private void hideStatusBar() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
    }

    @Override
    public void onBackPressed() {
        backDo();
    }

    // 指示器
    private void initDot() {
        for (int i = 0; i < datas.size(); i++) {
            CommonImageView ivDot =
                    new CommonImageView(this);
            ivDot.setMinimumHeight(20);
            ivDot.setMinimumWidth(20);
            ivDot.setScaleType(CommonImageView.ScaleType.CENTER_INSIDE);
            ivDot.setAdjustViewBounds(true);
            ivDot.setPadding(10, 20, 10, 20);
            ivDot.setId(i + idBegin);
            ivDot.setImageResource(R.drawable.dot_blue);
            llDot.addView(ivDot, i);
            if (current != -1) {
                if (current == i) {
                    ivDot.setSelected(true);
                } else {
                    ivDot.setSelected(false);
                }
            }
        }
    }

    class SamplePagerAdapter extends PagerAdapter {
        private ArrayList<String> datas;

        public SamplePagerAdapter(ArrayList<String> datas) {
            this.datas = datas;
        }

        @Override
        public int getItemPosition(Object object) {
            //该方法会在调用 notifyDataSetChanged()方法时候将所有子View置为空，这样就会重新加载生成子VIew达到更新效果，但是此方法只是用于子View简单的情况，当子View复杂的时候为增加系统开销效率很差！
            return POSITION_NONE;
        }

        public void setDatas(ArrayList<String> lists) {
            this.datas = lists;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (datas == null) {
                return 0;
            } else {
                return datas.size();
            }
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            final Context ctx = container.getContext();
            final String path = datas.get(position);
            View view = View.inflate(ctx, R.layout.layout_page, null);
            loadDo(view, path, getPreBitmap(path));
            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private Bitmap getPreBitmap(final String path) {
        Bitmap preBitmap;
        if (path.equalsIgnoreCase("storage_people_picture")) {
            int picId;
            if (pictureType == SelectAttachEnum.CropTypeEunm.CO_AVATAR.value()) {
                picId = R.drawable.enterprise_default;
            } else {
                picId = R.drawable.people;
            }
            preBitmap = decodeResource(getResources(), picId);
            return preBitmap;
        }
        BitmapUtil bitmapUtil = WeqiaApplication.getInstance().getBitmapUtil();
        if (PathUtil.isPathInDisk(path)) {
            preBitmap = bitmapUtil.getBitmapFromCache(FileUtil.getFormatFilePath(path));
        } else {
            String localPath = LnUtil.getLocalpath(path, AttachType.PICTURE.value());
            if (StrUtil.notEmptyOrNull(localPath)) {
                String loadKey = FileUtil.getFormatFilePath(localPath);
                preBitmap = bitmapUtil.getBitmapFromCache(loadKey);
            } else {
                String key = path + "&th=" + ImageThumbTypeEnums.THUMB_SMALL.value();
                preBitmap = bitmapUtil.getBitmapFromCache(key);
                if (preBitmap == null) {
                    preBitmap =
                            bitmapUtil.getBitmapFromCache(path + "&th="
                                    + ImageThumbTypeEnums.THUMB_MIDDLE.value());
                }
            }
        }
        return preBitmap;
    }

    private void loadDo(View view, String path, Bitmap preBitmap) {
        XImageView imageView = (XImageView) view.findViewById(R.id.xImageView);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
        final CommonImageView preImg = (CommonImageView) view.findViewById(R.id.iv_pre_img);


        if (PathUtil.isPathInDisk(path)) {
            if (path.equalsIgnoreCase("storage_people_picture")) {
                if (preBitmap != null) {
                    ViewUtils.showView(preImg);
                    preImg.setImageBitmap(preBitmap);
                }
                ViewUtils.hideViews(progressBar, imageView);
                ViewUtils.showView(preImg);
                preImg.setScaleType(ImageView.ScaleType.FIT_CENTER);
                preImg.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                return;
            } else {
                if (new File(path).exists()) {
                    setImageDo(view, path, new File(path), true);
                } else {
                    String netPath = LnUtil.getNetpath(path, AttachType.PICTURE.value());
                    if (StrUtil.notEmptyOrNull(netPath)) {
                        ViewUtils.showView(progressBar);
                        new DownPicTask(view, netPath, path).execute();
                    } else {
                        ViewUtils.hideView(progressBar);
                        L.toastShort("加载图片失败");
                    }
                }
            }
        } else {
            final String rPath = path + "&th=" + ImageThumbTypeEnums.THUMB_BIG.value();
            String localPath = LnUtil.getLocalpath(rPath, AttachType.PICTURE_WITH_SOURCE.value());
            if (StrUtil.notEmptyOrNull(localPath)) {
                setImageDo(view, path, new File(localPath), true);
            } else {
                if (!NetworkUtil.detect(ctx)) {
                    L.toastShort(R.string.lose_network_hint);
                }
                ViewUtils.showView(progressBar);
                new DownPicTask(view, rPath, path).execute();
            }
        }

        imageView.setActionListener(new XImageView.SimpleActionListener() {
            @Override
            public void onSingleTapped(XImageView view, MotionEvent event, boolean onImage) {
                backDo();
            }

            @Override
            public void onSetImageFinished(XImageView view, boolean success, Rect image) {
                ViewUtils.hideViews(progressBar, preImg);
            }

            @Override
            public void onLongPressed(XImageView view, MotionEvent event) {
                if (!bChangeHead) {
                    saveImgDialog = initSaveImgDialog(PicturePagerActivity.this, PicturePagerActivity.this);
                    saveImgDialog.show();
                }
            }
        });
    }

    private void setImageDo(View view, String path, File file, boolean local) {
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
        CommonImageView preImg = (CommonImageView) view.findViewById(R.id.iv_pre_img);
        final XImageView imageView = (XImageView) view.findViewById(R.id.xImageView);
        if (startBounds != null) {
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        imgAnimation(imageView);
                    }
                }
            });
        }
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        opts.inJustDecodeBounds = true;
//
//        decodeFile(file.getPath(), opts);
//        boolean bigPic = false;
//        if (opts.outHeight >= DeviceUtil.getDeviceHeight()
//                || opts.outWidth >= DeviceUtil.getDeviceWidth()) {
//            bigPic = true;
//        }
//        ViewUtils.hideViews(progressBar, preImg);
//        if (local && !bigPic) {
//            L.e("本地，且已下载"+"file::"+file.getAbsolutePath());
//            /**
//             * 本地，且已下载file::/storage/emulated/0/zhuangzhuang/cache/501_2c9180845c8a6677015c8a7a79df0003.gif&th=1
//             */
//        } else {
//            imageView.setImage(file);
//        }


        /**
         *gif支持
         */
        ViewUtils.hideViews(progressBar, preImg);

        try {
            final GifView gifView = (GifView) view.findViewById(R.id.gifView);
            String imgPath = file.getAbsolutePath();
            if (StrUtil.notEmptyOrNull(imgPath) && imgPath.contains(".gif")) {
                if (gifView != null) {
                    imageView.setVisibility(View.GONE);
                    gifView.setVisibility(View.VISIBLE);
                    gifView.setmMovieFilePath(file.getAbsolutePath());
                }
            } else {
                gifView.setVisibility(View.GONE);
                if (path.endsWith("bmp")) {
                    preImg.setVisibility(View.VISIBLE);
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    if (bitmap != null) {
                        preImg.setImageBitmap(bitmap);
                    }
                } else {
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImage(file);
                }
            }
        } catch (Exception e) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImage(file);
            e.printStackTrace();
        }
    }

    class DownPicTask extends AsyncTask<Void, Void, Void> {
        private String picUrl;
        private View view;
        private String url;

        public DownPicTask(View view, String picUrl, String url) {
            this.picUrl = picUrl;
            this.view = view;
            this.url = url;
        }

        @Override
        protected Void doInBackground(Void... params) {
            String realUrl = UserService.getBitmapUrl(picUrl);
            if (StrUtil.notEmptyOrNull(realUrl)) {
                final String path = picUrl;
                String target = PathUtil.getCachePath() + File.separator + path.replace("/", "_");
                UserService.downloadFile(realUrl, target, picUrl, new FileCallback() {
                    @Override
                    public void onSuccess(File file) {
                        L.e("网络加载成功");
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("fileAbsolutePath", file.getAbsolutePath());
                        bundle.putString("urlName", url);
                        L.e("file.getAbsolutePath():" + file.getAbsolutePath());
                        message.setData(bundle);
                        message.obj = view;
                        mHandler.sendMessage(message);
                        LocalNetPath localNetPath =
                                new LocalNetPath(file.getAbsolutePath(), picUrl,
                                        AttachType.PICTURE_WITH_SOURCE.value());
                        LnUtil.saveData(localNetPath);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        L.e("下载文件出错了");
                    }
                });
            }
            return null;
        }
    }

    // 保存到手机
    public Dialog initSaveImgDialog(Context ctx, android.view.View.OnClickListener onClickListener) {
        SharedFullScreenDialog dialog = new SharedFullScreenDialog(ctx);
//        DialogData data2 = new DialogData(forwardKey, "转发", onClickListener, null);
        DialogData data1 = new DialogData(downloadKey, "保存到手机", onClickListener, null);
//        DialogData data3 = new DialogData(qrInfoKey, "识别图中二维码", onClickListener, null);
        List<DialogData> datas = new ArrayList<DialogData>();
        data1.setTitleColor(ctx.getResources().getColor(R.color.black));
//        data2.setTitleColor(ctx.getResources().getColor(R.color.black));
//        data3.setTitleColor(ctx.getResources().getColor(R.color.black));
//        datas.add(data2);
        datas.add(data1);

//        String path = this.datas.get(mViewPager.getCurrentItem());
//        if (StrUtil.notEmptyOrNull(path)) {
//            qrInfo = ScanTools.scanBitmap(getPreBitmap(path));
//        }
//        if (StrUtil.notEmptyOrNull(qrInfo)) {
//            datas.add(data3);
//        }
//        dialog.setDialogTitle("保存图片");
        dialog.setCancelable(true);
        dialog.setDialogButton(datas);
        return dialog;
    }

    public void forwardImageData() {
        String path = datas.get(mViewPager.getCurrentItem());
        TransData transData = new TransData();
//        MsgData msg = new MsgData(path, 1);
//        transData.setOuter(false);
//        transData.setContentType(1);
//        transData.setInsideData(msg);
//        WeqiaApplication.transData = transData;
//        ctx.startToActivity(OpenFileActivity.class);
    }

    /**
     * 保存图片到手机
     */
    public void savePicToPhone() {
        String path = datas.get(mViewPager.getCurrentItem());
        if (!PathUtil.isPathInDisk(path)) {
            final String key = path + "&th=" + ImageThumbTypeEnums.THUMB_BIG.value();
            String localPath = LnUtil.getLocalpath(key, AttachType.PICTURE_WITH_SOURCE.value());
            if (StrUtil.notEmptyOrNull(localPath)) {
                path = localPath;
            }
        }

        if (PathUtil.isPathInDisk(path)) {
            String fileName = new File(path).getName();
            if (!FileMiniUtil.isImageEnd(fileName)) {
                if (fileName.contains("&th")) {
                    fileName = fileName.substring(0, fileName.indexOf("&th"));
                }
                if (fileName.endsWith("jpg")) {
                    fileName = fileName.replace("jpg", ".jpg");
                }
                if (fileName.endsWith("gif")) {
                    fileName = fileName.replace("gif", ".gif");
                }
                if (fileName.endsWith("png")) {
                    fileName = fileName.replace("png", ".png");
                }
                if (fileName.endsWith("jpeg")) {
                    fileName = fileName.replace("jpeg", ".jpeg");
                }
                if (fileName.endsWith("bmp")) {
                    fileName = fileName.replace("bmp", ".bmp");
                }
            }
            L.e(fileName);
            try {
                String newPath = PathUtil.getWQPath() + File.separator + fileName;
                NativeFileUtil.copySingleFile(path, newPath);
                ComponentUtil.refreshGallery(this, newPath);
                L.toastShort("保存图片成功");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        // 保存到手机
        if (v.getId() == downloadKey) {
            savePicToPhone();
            saveImgDialog.dismiss();
        }
// else if (v.getId() == forwardKey) {
//            forwardImageData();
//            saveImgDialog.dismiss();
//        } else if (v.getId() == qrInfoKey) {
////            if (StrUtil.notEmptyOrNull(qrInfo)) {
////                QRScanActivity.getCoInfoByQRCodeUrl(ctx, qrInfo);
////            }
//            saveImgDialog.dismiss();
//        } else

        if (v == sharedTitleView.getButtonRight()) {
            bottomView.setVisibility(bottomView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        } else if (v.getId() == R.id.tv_change_pic_from_phone) {
            SelectMediaUtils.addPicWithCrop(ctx, pictureType);
            ViewUtils.hideView(bottomView);
        } else if (v.getId() == R.id.tv_save_pic_to_phone) {
            savePicToPhone();
            ViewUtils.hideView(bottomView);
        } else if (v == sharedTitleView.getButtonLeft()) {
            ctx.setResult(RESULT_OK);
            finish();
        } else if (v == sharedTitleView.getTvRight()) {
        }
    }

    public static boolean bChange = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SelectMediaUtils.REQ_GET_PIC) {
                if (datas.size() == 1 && data != null) {
                    String clipPath = data.getStringExtra(ClipImageActivity.CLIPIMAGEPATH);
                    if (StrUtil.notEmptyOrNull(clipPath)) {
                        bChange = true;
                        datas.clear();
                        datas.add(clipPath);
                        mAdapter.setDatas(datas);
                        L.e("设置成功！");
                    } else {
                        bChange = false;
                    }
                    return;
                } else {
                    ctx.setResult(RESULT_OK);
                    finish();
                }
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        if (bShowDot) {
            updateTabs(arg0);
        } else {
            sharedTitleView.initTopBanner((arg0 + 1) + " / " + datas.size());
        }
    }

    private void updateTabs(int position) {
        for (int i = 0; i < llDot.getChildCount(); i++) {
            CommonImageView iv =
                    (CommonImageView) findViewById(i + idBegin);
            if (iv != null) {
                if (position == i) {
                    iv.setSelected(true);
                } else {
                    iv.setSelected(false);
                }
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void imgAnimation(final XImageView imageView) {
        final Rect finalBounds = new Rect(0, 0, 1080, 1920);
        final Point globalOffset = new Point();
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }
        AnimatorSet set = new AnimatorSet();
        imageView.setPivotX(0f);
        imageView.setPivotY(0f);
        set
                .play(ObjectAnimator.ofFloat(imageView, View.X, startBounds.left,
                        finalBounds.left))
                .with(ObjectAnimator.ofFloat(imageView, View.Y, startBounds.top,
                        finalBounds.top))
                .with(ObjectAnimator.ofFloat(imageView, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(imageView, View.SCALE_Y, startScale, 1f));
        set.setDuration(250);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                imageView.setVisibility(View.VISIBLE);
            }
        });
        set.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
