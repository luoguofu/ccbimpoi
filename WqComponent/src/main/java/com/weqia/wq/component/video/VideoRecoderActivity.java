package com.weqia.wq.component.video;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.weqia.utils.L;
import com.weqia.utils.TimeUtils;
import com.weqia.utils.datastorage.file.PathUtil;
import com.weqia.utils.exception.CheckedExceptionHandler;
import com.weqia.wq.R;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;
import com.weqia.wq.data.global.GlobalConstants;

import java.io.File;
import java.lang.reflect.Method;

public class VideoRecoderActivity extends SharedDetailTitleActivity
        implements
            SurfaceHolder.Callback {
    private SurfaceView surfaceView;
    private Chronometer timeChronometer;
    private com.weqia.utils.view.CommonImageView ivOperate;
    private MediaRecorder mediaRecorder;
    private Camera mCamera;
    private boolean bRecording = false;
    private FrameLayout titleView;
    private String filePath;
    private Long start = 0l;
    private Long end = 0l;
    private MediaPlayer mediaPlayer;
    private int position;
    private com.weqia.utils.view.CommonImageView ivPlay;
    private ImageView ivCameraChange;
    private Camera.Size size;
    private boolean backCamera = true;
    private int cameraPosition = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    public Camera getmCamera() {
        if (mCamera == null) {
            try {
                mCamera = Camera.open();
                // camerParams = mCamera.getParameters();
                size =
                        getBestPreviewSize(GlobalConstants.VIDEO_WIDTH,
                                GlobalConstants.VIDEO_HEIGHT, mCamera.getParameters());
                if (size == null) {
                    size =
                            mCamera.new Size(GlobalConstants.VIDEO_WIDTH,
                                    GlobalConstants.VIDEO_HEIGHT);
                }
                return mCamera;
            } catch (Exception e) {
                L.toastShort("相机被其他程序占用,暂时无法打开~");
            }
        }
        return mCamera;
    }

    public void setmCamera(Camera mCamera) {
        this.mCamera = mCamera;
    }


    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    void changeCamera() {
        // 切换前后摄像头
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();// 得到摄像头的个数

        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);// 得到每一个摄像头的信息
            if (cameraPosition == 1) {
                // 现在是后置，变更为前置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
                                                                                 // CAMERA_FACING_BACK后置
                    mCamera.stopPreview();// 停掉原来摄像头的预览
                    mCamera.release();// 释放资源
                    mCamera = null;// 取消原来摄像头
                    mCamera = Camera.open(i);// 打开当前选中的摄像头
                    initCameraDisplay();
                    cameraPosition = 0;
                    break;
                }
            } else {
                // 现在是前置， 变更为后置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {// 代表摄像头的方位，CAMERA_FACING_FRONT前置
                                                                                // CAMERA_FACING_BACK后置
                    mCamera.stopPreview();// 停掉原来摄像头的预览
                    mCamera.release();// 释放资源
                    mCamera = null;// 取消原来摄像头
                    mCamera = Camera.open(i);// 打开当前选中的摄像头
                    initCameraDisplay();
                    cameraPosition = 1;
                    break;
                }
            }

        }
    }


    @SuppressWarnings("deprecation")
    private void initViews() {
        setContentView(R.layout.activity_video);
        sharedTitleView.initTopBanner("拍摄视频", "发送");
        titleView = (FrameLayout) findViewById(R.id.topTitle);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        timeChronometer = (Chronometer) findViewById(R.id.timeChronometer);
        ivOperate = (com.weqia.utils.view.CommonImageView) findViewById(R.id.ivOperate);
        ivPlay = (com.weqia.utils.view.CommonImageView) findViewById(R.id.ivPlay);
        ivCameraChange = (ImageView) findViewById(R.id.ivCameraChange);
        ivCameraChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCamera();
            }
        });
        titleView.setVisibility(View.GONE);
        ivOperate.setVisibility(View.VISIBLE);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(this);
        getmCamera();
        ivOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bRecording) {
                    stopDo();
                } else {
                    timeChronometer.setBase(SystemClock.elapsedRealtime());
                    timeChronometer.start();
                    start = System.currentTimeMillis();
                    bRecording = true;
                    ivPlay.setVisibility(View.GONE);
                    ivOperate.setVisibility(View.VISIBLE);
                    ivOperate.setImageResource(R.drawable.video_recorder_stop_btn);
                    startRecoder();
                    ivCameraChange.setVisibility(View.INVISIBLE);
                }
            }
        });

        // timeChronometer.setFormat("%s");
        timeChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (chronometer.getText().toString().equals("00:30")) {
                    L.toastShort("视频已达最长30s~");
                    stopDo();
                }
            }
        });
        mediaPlayer = new MediaPlayer();
        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(0);
                } else {
                    play();
                }
            }
        });
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(0);
                }
            }
        });
    }

    private void stopDo() {
        timeChronometer.stop();
        end = System.currentTimeMillis();
        bRecording = false;
        ivOperate.setVisibility(View.GONE);
        titleView.setVisibility(View.VISIBLE);
        ivPlay.setVisibility(View.VISIBLE);
        stopRecorder();
    }

    @SuppressWarnings("deprecation")
    @SuppressLint({"InlinedApi", "NewApi"})
    private void startRecoder() {
        try {
            filePath =
                    PathUtil.getWQPath() + File.separator + TimeUtils.getFileSaveTime()
                            + GlobalConstants.VIDEO_FORMAT;
            File videoFile = new File(filePath);
            getmCamera().unlock();
            if (mediaRecorder == null) {
                mediaRecorder = new MediaRecorder();
            }
            if (Integer.parseInt(Build.VERSION.SDK) >= 8)
                setDisplayOrientation(getmCamera(), 90);
            else {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    getmCamera().getParameters().set("orientation", "portrait");
                    getmCamera().getParameters().set("rotation", 90);
                }
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    getmCamera().getParameters().set("orientation", "landscape");
                    getmCamera().getParameters().set("rotation", 90);
                }
            }
            mediaRecorder.setCamera(getmCamera());
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            // mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mediaRecorder.setAudioSamplingRate(8000);
            mediaRecorder.setVideoEncodingBitRate(600000);
            // mediaRecorder.setVideoSize(GlobalConstants.VIDEO_WIDTH,
            // GlobalConstants.VIDEO_HEIGHT);
            // size = getBestPreviewSize(GlobalConstants.VIDEO_WIDTH, GlobalConstants.VIDEO_HEIGHT);
            // if (size != null) {
            mediaRecorder.setVideoSize(size.width, size.height);
            if (L.D) L.e("实际录制大小 w + h" + size.width + ":" + size.height);
            // } else {
            // if (L.D) L.e("没有可用的录制大小，用系统的吧");
            // }

            mediaRecorder.setVideoFrameRate(30);
            mediaRecorder.setMaxDuration(30000);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                if (cameraPosition == 1) {
                    mediaRecorder.setOrientationHint(90);
                } else {
                    mediaRecorder.setOrientationHint(270);
                }

            }
            mediaRecorder.setOutputFile(videoFile.getAbsolutePath());
            mediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {
            L.toastShort("很抱歉,录像失败!");
            CheckedExceptionHandler.handleException(e);
            // e.printStackTrace();
            stopRecorder();
        }
    }

    private Camera.Size getBestPreviewSize(int width, int height, Parameters camerParams) {
        Camera.Size result = null;
        if (camerParams == null) {
            return result;
        }
        for (Camera.Size size : camerParams.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea > resultArea) {
                        result = size;
                    }
                }
            }
        }
        return result;
    }


    private void stopRecorder() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder = null;
                getmCamera().stopPreview();
                getmCamera().lock();
                getmCamera().release();
                setmCamera(null);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"
                        + filePath)));
            } catch (Exception e) {
                L.toastShort("很抱歉,录像保存失败!");
            }

        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        initCameraDisplay();

        if (position > 0 && filePath != null) {
            play();
            mediaPlayer.seekTo(position);
            position = 0;
        }


    }

    private void initCameraDisplay() {
        if (surfaceView.getHolder() != null && getmCamera() != null) {
            try {
                Parameters parameters = getmCamera().getParameters();
                parameters.setPreviewSize(size.width, size.height);
                getmCamera().setParameters(parameters);

                getmCamera().setDisplayOrientation(90);
                getmCamera().setPreviewDisplay(surfaceView.getHolder());
                getmCamera().startPreview();
            } catch (Exception e) {

            }
        }
    }


    private void play() {
        try {
            ivPlay.setVisibility(View.GONE);
            mediaPlayer.reset();// 重置为初始状态
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDisplay(surfaceView.getHolder());
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();// 缓冲
            mediaPlayer.start();// 播放
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    ivPlay.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            L.toastShort("播放失败~");
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (getmCamera() != null) {
            getmCamera().stopPreview();
            getmCamera().release();
        }

        if (mediaPlayer.isPlaying()) {
            position = mediaPlayer.getCurrentPosition();
            mediaPlayer.stop();
        }
    }


    protected void setDisplayOrientation(Camera camera, int angle) {
        Method downPolymorphic;
        try {
            downPolymorphic =
                    camera.getClass().getMethod("setDisplayOrientation", new Class[] {int.class});
            if (downPolymorphic != null) downPolymorphic.invoke(camera, new Object[] {angle});
        } catch (Exception e1) {}
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.topbanner_button_string_right) {
            Intent intent = new Intent();
            intent.putExtra(GlobalConstants.KEY_VIDEO_TIME, (end - start));
            intent.putExtra(GlobalConstants.KEY_VIDEO_PATH, filePath);
            intent.putExtra(GlobalConstants.KEY_VIDEO_URI, Uri.parse("file://" + filePath));
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public boolean isBackCamera() {
        return backCamera;
    }

    public void setBackCamera(boolean backCamera) {
        this.backCamera = backCamera;
    }
}
