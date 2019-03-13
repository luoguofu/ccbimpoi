package com.weqia.wq.component.video;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weqia.utils.DeviceUtil;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.datastorage.file.PathUtil;
import com.weqia.utils.dialog.SharedCommonDialog;
import com.weqia.utils.http.okserver.download.DownloadManager;
import com.weqia.utils.view.CommonImageView;
import com.weqia.utils.view.RoundProgressBar;
import com.weqia.wq.R;
import com.weqia.wq.component.AttachService;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;
import com.weqia.wq.component.receiver.AttachMsgReceiver;
import com.weqia.wq.component.utils.GlobalUtil;
import com.weqia.wq.component.utils.LnUtil;
import com.weqia.wq.component.utils.NetworkUtil;
import com.weqia.wq.data.AttachmentData;
import com.weqia.wq.data.EnumData.AttachType;
import com.weqia.wq.data.EnumData.ImageThumbTypeEnums;
import com.weqia.wq.data.MediaData;
import com.weqia.wq.data.global.GlobalConstants;
import com.weqia.wq.data.global.WeqiaApplication;

import java.io.File;
import java.text.DecimalFormat;

@SuppressWarnings("unused")
public class VideoPlayActivity extends SharedDetailTitleActivity implements SurfaceHolder.Callback {
    private SurfaceView surfaceView;
    private TextView tvTime;
    private TextView tvTips;
    private String filePath;
    private MediaPlayer mediaPlayer;

    private CommonImageView ivPlay;
    private CommonImageView ivPrew;

    private AttachmentData fileData = null;
    private Uri uri;
    private boolean readyToPlay = false;
    private FrameLayout frPrew;
    private RelativeLayout rlPlayControl;
    private boolean bPause = false;
    private boolean bError = false;
    private boolean downloadCom = false;
    private VideoPlayActivity ctx;
    private TextView tvPercent;
    private int totalCount;
    private RoundProgressBar progressBar;
    private boolean downlLoadError = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        ctx = this;
        initViews();
        initData();
    }

    @SuppressWarnings("deprecation")
    private void initViews() {

        progressBar = (RoundProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(1);
        progressBar.setMax(100);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvTips = (TextView) findViewById(R.id.tvTips);
        ivPlay = (CommonImageView) findViewById(R.id.ivPlay);
        ivPrew = (CommonImageView) findViewById(R.id.iv_prew);
        frPrew = (FrameLayout) findViewById(R.id.fr_prew);
        rlPlayControl = (RelativeLayout) findViewById(R.id.rl_play_control);
        tvPercent = (TextView) findViewById(R.id.tv_percent);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(this);

        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readyToPlay && mediaPlayer != null) {
                    finish();
                }
            }
        });

        ivPlay.setVisibility(View.GONE);
        tvTime.setVisibility(View.GONE);
        tvTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void initData() {
        mediaPlayer = new MediaPlayer();// 创建多媒体对象
        if (dataParam == null) {
            return;
        }
        fileData = (AttachmentData) dataParam;
        if (StrUtil.notEmptyOrNull(fileData.getLoaclUrl())) {
            filePath = fileData.getLoaclUrl();
            isReady(true);
        } else if (PathUtil.isPathInDisk(fileData.getUrl())) {
            filePath = fileData.getUrl();
            isReady(true);
        } else {
            String tmpVideoPath = LnUtil.getLocalpath(fileData.getUrl(), AttachType.VIDEO.value());
            if (StrUtil.notEmptyOrNull(tmpVideoPath)) {
                filePath = tmpVideoPath;
                isReady(true);
            } else {
                if (!NetworkUtil.detect(ctx)) {
                    L.toastShort(R.string.lose_network_hint);
                }
                AttachmentData attachmentData =
                        WeqiaApplication.getInstance().getDbUtil()
                                .findById(fileData.getUrl(), AttachmentData.class);
                String localUrl = "";
                float picScale = fileData.getPicScale();
                int picHeight = (int) (DeviceUtil.getDeviceWidth() / picScale);
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivPrew.getLayoutParams();
                params.width = DeviceUtil.getDeviceWidth();
                params.height = picHeight;
                ivPrew.setLayoutParams(params);
                if (attachmentData != null && StrUtil.notEmptyOrNull(attachmentData.getLoaclUrl())) {
                    localUrl = attachmentData.getLoaclUrl();
                }
                if (StrUtil.notEmptyOrNull(localUrl) && (new File(localUrl)).exists()) {
                    filePath = localUrl;
                    isReady(true);
                } else {
                    isReady(false);
                    if (StrUtil.notEmptyOrNull(fileData.getVideoPrew())) {
                        ViewUtils.showView(ivPrew);
                        getBitmapUtil().load(ivPrew, fileData.getVideoPrew(),
                                ImageThumbTypeEnums.THUMB_BIG.value());
                    }
                    AttachmentData tmpData = new AttachmentData();
                    tmpData.setUrl(fileData.getUrl());
                    tmpData.setName(fileData.getName());
                    tmpData.setFileSize(fileData.getFileSize());
                    tmpData.setVideoPrew(fileData.getVideoPrew());
                    tmpData.setPathRoot(PathUtil.getFilePath());
                    Intent intent = new Intent(this, AttachService.class);
                    intent.putExtra(GlobalConstants.KEY_ATTACH_OP, tmpData);
                    startService(intent);
                }

            }
        }
    }

    public void isReady(boolean ready) {
        readyToPlay = ready;
        if (ready) {
            ViewUtils.hideView(frPrew);
            ViewUtils.showView(rlPlayControl);
            setup();
        } else {
            ViewUtils.showView(frPrew);
            ViewUtils.hideView(rlPlayControl);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    int currentPosition = 0;
                    if (mediaPlayer != null) {
                        currentPosition = mediaPlayer.getCurrentPosition();
//                        if (tvTime != null) {
//                            tvTime.setText(toTime(totalCount - currentPosition));
//                        }
                    }
                    if (!bPause && !bError) {
                        handler.sendEmptyMessageDelayed(1, 500);
                    }
                    break;

                default:
                    break;
            }

        }
    };// 用于进度条

    public String toTime(int time) {
        time /= 1000;
        int minute = time / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mediaPlayer != null) {
            mediaPlayer.setDisplay(holder);
        }
        if (readyToPlay && !bError) {
            play();
        }
    }

    private void loadClip() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                //循环播放
                play();
                tvTips.setVisibility(View.VISIBLE);
            }
        });

        mediaPlayer.setOnErrorListener(new OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(ctx);
                builder
                        .setPositiveButton(ctx.getString(R.string.dialog_confirm),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        onBackPressed();
                                    }
                                }).setMessage("无法播放此视频!").create().show();
                bError = true;
                return true;
            }
        });

        int tmpWidth = DeviceUtil.getDeviceWidth();
        int tmpHeight = DeviceUtil.getDeviceHeight();
        MediaData mediaData = GlobalUtil.getVideoInfoByPath(this, filePath);
        if (mediaData != null) {
            if (mediaData.getWidthHeight() != null && mediaData.getWidthHeight().x != 0
                    && mediaData.getWidthHeight().y != 0) {
                tmpHeight = (int) ((float) tmpWidth / mediaData.getVideoScale());
            }
        }
        if (fileData != null) {
            tmpHeight = (int) ((float) tmpWidth / fileData.getPicScale());
        }
        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) surfaceView.getLayoutParams();
        layoutParams.width = tmpWidth;
        layoutParams.height = tmpHeight;

        surfaceView.setLayoutParams(layoutParams);

        try {
            mediaPlayer.setDataSource(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setup() {
        if (bError) {
            return;
        }
        loadClip();
        try {
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

                @Override
                public void onPrepared(final MediaPlayer mp) {
                    totalCount = mp.getDuration();
                    mp.seekTo(0);// 初始化MediaPlayer播放位置
                    if (downloadCom) {
                        play();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void play() {
        bPause = false;
        handler.sendEmptyMessage(1);
        mediaPlayer.start();
        ivPlay.setImageResource(R.drawable.btn_zanting);
    }

    private void pause() {
        ivPlay.setImageResource(R.drawable.btn_bofang);
        bPause = true;
        mediaPlayer.pause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        if (fileData != null && StrUtil.notEmptyOrNull(fileData.getUrl()))
            DownloadManager.getInstance().pauseTask(fileData.getUrl());

        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        finish();
    }


    private AttachMsgReceiver downReceive = new AttachMsgReceiver() {

        @Override
        public void downloadCountReceived(Intent intent) {
            if (intent != null) {
                String urlStr = intent.getStringExtra(GlobalConstants.KEY_DOWN_ID);
                if (!urlStr.equalsIgnoreCase(fileData.getUrl())) {
                    return;
                }
                String downPercent = intent.getStringExtra(GlobalConstants.KEY_DOWN_PERCENT);
                Boolean bComplete =
                        intent.getBooleanExtra(GlobalConstants.KEY_DOWN_COMPLETE, false);
                File downFile = (File) intent.getSerializableExtra(GlobalConstants.KEY_DOWN_FILE);
                if (StrUtil.isEmptyOrNull(urlStr) || StrUtil.isEmptyOrNull(downPercent)
                        || bComplete == null) {
                    return;
                }

                if (!bComplete && downPercent.equalsIgnoreCase("100%")) {
                    L.toastShort("视频下载失败~");
                    downlLoadError = true;
                    return;
                }

                downPercent = downPercent.replace("%", "");
                Double progress = Double.parseDouble(downPercent);
                int showPb = Integer.parseInt(new DecimalFormat("##").format(progress));
                progressBar.setProgress(showPb);
                if (bComplete) {
                    progressBar.setVisibility(View.GONE);
                    downloadCom = true;
                    filePath = downFile.getPath();
                    isReady(true);
                }
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        if (downReceive != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(GlobalConstants.DOWNLOAD_COUNT_SERVICE_NAME);
            filter.setPriority(Integer.MAX_VALUE);
            registerReceiver(downReceive, filter);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (downReceive != null) {
            unregisterReceiver(downReceive);
        }
    }
}
