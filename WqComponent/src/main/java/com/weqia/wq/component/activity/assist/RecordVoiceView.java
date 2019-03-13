package com.weqia.wq.component.activity.assist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weqia.utils.DeviceUtil;
import com.weqia.utils.L;
import com.weqia.utils.datastorage.file.FileUtil;
import com.weqia.utils.datastorage.file.PathUtil;
import com.weqia.utils.view.CommonImageView;
import com.weqia.wq.R;
import com.weqia.wq.component.activity.SharedTitleActivity;
import com.weqia.wq.component.utils.GlobalUtil;
import com.weqia.wq.component.utils.sound.AudioRecordUtil;
import com.weqia.wq.component.utils.sound.SoundPoolUtil;
import com.weqia.wq.data.global.GlobalConstants;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordVoiceView {

    private SharedTitleActivity ctx;

    // 录音
    public final static int RECORDER_STATE_RECORDING = 0;
    // 录制时间太短
    public final static int RECORDER_STATE_SHORT = 1;
    // 发送中
    public final static int VOICE_PUBLISHING = 2;
    // 取消发送
    public final static int RECORDER_STATE_CANCEL = 3;
    // 语音最短时间(秒)
    public final static double RECORDER_TIME_MINTIME = 1;
    // 语音最长时间(秒)
    public final static int RECORDER_TIME_MAXTIME = 60;

    // 是否正在录音中
    private boolean isRecording = false;
    // 是否超时
    private boolean IS_OVERTIME = false;
    private LinearLayout llRecordStatus;// 录音对话框
    // 录制语音时涉及操作的控件
    private FrameLayout flVoice;
    private RelativeLayout rlVolume;// 录制声音大小布局
    private CommonImageView ivVolume;// 录音音量
    private RelativeLayout rlVoiceCancel;// 取消发布布局
    private LinearLayout llTooShort;// 录音太短
    private TextView tvVoiceTimeLeft;// 录音剩余时间提示
    private AudioRecordUtil recordUtils;// 录音工具类

    private int recordTime = 0;
    private String fileName;
    private Handler mHandler;
    private Button btnRecorder;
    private TalkBarInterface talkBarInterface;

    public RecordVoiceView(SharedTitleActivity ctx, Button btnRecorder,
                           TalkBarInterface talkBarInterface) {
        this.ctx = ctx;
        this.btnRecorder = btnRecorder;
        this.talkBarInterface = talkBarInterface;
    }

    // 录音
    public void initVoice() { // 录制语音时涉及操作的控件

        mHandler = new Handler();
        recordUtils = new AudioRecordUtil();
        llRecordStatus = (LinearLayout) ctx.findViewById(R.id.llRecordStatus);

        flVoice = (FrameLayout) ctx.findViewById(R.id.flVoice);
        rlVolume = (RelativeLayout) ctx.findViewById(R.id.rlVolume);// 录制声音大小布局
        ivVolume = (CommonImageView) ctx.findViewById(R.id.ivVolume);// 录音状态动画
        rlVoiceCancel = (RelativeLayout) ctx.findViewById(R.id.rlVoiceCancel);// 取消发布布局
        llTooShort = (LinearLayout) ctx.findViewById(R.id.llTooShort);// 录音太短
        tvVoiceTimeLeft = (TextView) ctx.findViewById(R.id.tvVoiceTimeLeft);// 录音剩余时间提示

        btnRecorder.setOnTouchListener(recorderTouchListener);
    }

    // 语音录制按钮触摸事件
    private View.OnTouchListener recorderTouchListener = new View.OnTouchListener() {
        long startVoiceT = 0;// 开始时间
        long endVoiceT = 0;// 结束世间
        int startY = 0;// 开始的Y
        byte state = RECORDER_STATE_RECORDING;

        @SuppressLint({"SimpleDateFormat", "ClickableViewAccessibility"})
        public boolean onTouch(View v, MotionEvent event) {
            // 超时
            if (IS_OVERTIME) {
                tvVoiceTimeLeft.setVisibility(View.INVISIBLE);
                stopRecorder();
                btnRecorder.setText("按住  说话");
                // 状态为取消
                if (state == RECORDER_STATE_CANCEL || state == VOICE_PUBLISHING) {
                    deleteVoiceFile();
                    if (state == RECORDER_STATE_CANCEL) IS_OVERTIME = false;
                    return false;
                }
                if (state != VOICE_PUBLISHING) {
                    state = VOICE_PUBLISHING;
                    IS_OVERTIME = false;
                    // 超时自动发送
                    String sendName = fileName;
                    L.toastLong("正在发送语音");
                    // sendVoice(PathUtil.getVoicePath() + File.separator + sendName, 60);
                    String vPath = PathUtil.getVoicePath() + File.separator + sendName;
                    if (talkBarInterface != null) {
                        talkBarInterface.sendVoice(vPath, 60);
                    }
                }
                return false;
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (talkBarInterface != null) {
                        talkBarInterface.onSendVoice();
                    }
                    state = RECORDER_STATE_RECORDING;
                    showRecarderStatus(RECORDER_STATE_RECORDING);
                    isRecording = true;
                    IS_OVERTIME = false;
                    btnRecorder.setText("松开  结束");
                    // 按下时记录录音文件名
                    String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    fileName = ctx.getMid() + "_" + timeStamp + GlobalConstants.VOICE_FORMAT;
                    startY = (int) event.getY();
                    startVoiceT = System.currentTimeMillis();
                    // 隐藏软键盘
                    llRecordStatus.setVisibility(View.VISIBLE);
                    initRecorder(fileName);


                    SoundPoolUtil.playSound(ctx);
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            startRecorder();
                        }
                    }, 200);
                    break;
                case MotionEvent.ACTION_MOVE:
                    int tempY = (int) event.getY();
                    // DeviceUtil.getDeviceHeight() / 5)
                    if (Math.abs(startY - tempY) > 20 * DeviceUtil.getDeviceDensity()) {
                        // 取消
                        state = RECORDER_STATE_CANCEL;
                        showRecarderStatus(RECORDER_STATE_CANCEL);
                    } else {
                        // 录音
                        state = RECORDER_STATE_RECORDING;
                        showRecarderStatus(RECORDER_STATE_RECORDING);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    isRecording = false;
                    btnRecorder.setText("按住  说话");
                    endVoiceT = System.currentTimeMillis();
                    final long voiceT = endVoiceT - startVoiceT;

                    if (voiceT < RECORDER_TIME_MINTIME * 1000 || state == RECORDER_STATE_CANCEL) {
                        deleteVoiceFile();
                        if (voiceT < RECORDER_TIME_MINTIME * 1000) {
                            showRecarderStatus(RECORDER_STATE_SHORT);
                        }
                        if (state == RECORDER_STATE_CANCEL) {


                            tvVoiceTimeLeft.setVisibility(View.INVISIBLE);
                            llRecordStatus.setVisibility(View.INVISIBLE);
                        }
                        return false;
                    }

                    keyUpDo(voiceT);
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    private void keyUpDo(final long voiceT) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // 停止录音
                stopRecorder();
                // 发送录音
                String sendName = fileName;
                if (((int) voiceT / 1000) < 60) {
                    String vPath = PathUtil.getVoicePath() + File.separator + sendName;
                    double fileSize = FileUtil.getFileOrFilesSize(vPath, FileUtil.SIZETYPE_B);
                    if (fileSize < 1) {
                        if (L.D) L.e("文件大小太小，不发送");
                        L.toastShort("录音失败，请重试");
                        return;
                    }
                    int second = (int) voiceT / 1000;
                    if (talkBarInterface != null) {
                        talkBarInterface.sendVoice(vPath, second);
                    }
                }
            }
        }, 500);
    }

    // 根据录制音量的大小定时更新状态图片
    private void updateDisplay(double signalEMA) {
        switch ((int) signalEMA) {
            case 0:
            case 1:
                ivVolume.setImageResource(R.drawable.amp1);
                break;
            case 2:
            case 3:
                ivVolume.setImageResource(R.drawable.amp2);
                break;
            case 4:
            case 5:
                ivVolume.setImageResource(R.drawable.amp3);
                break;
            case 6:
            case 7:
                ivVolume.setImageResource(R.drawable.amp4);
                break;
            case 8:
            case 9:
                ivVolume.setImageResource(R.drawable.amp5);
                break;
            case 10:
            case 11:
                ivVolume.setImageResource(R.drawable.amp6);
                break;
            default:
                ivVolume.setImageResource(R.drawable.amp7);
                break;
        }
    }

    private void showRecarderStatus(int type) {
        switch (type) {
            case RECORDER_STATE_RECORDING:
                rlVolume.setVisibility(View.VISIBLE);
                rlVoiceCancel.setVisibility(View.GONE);
                break;
            case RECORDER_STATE_CANCEL:
                rlVolume.setVisibility(View.GONE);
                rlVoiceCancel.setVisibility(View.VISIBLE);
                break;
            case RECORDER_STATE_SHORT:
                flVoice.setVisibility(View.GONE);
                llRecordStatus.setVisibility(View.VISIBLE);
                llTooShort.setVisibility(View.VISIBLE);
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        llRecordStatus.setVisibility(View.GONE);
                        llTooShort.setVisibility(View.GONE);
                        flVoice.setVisibility(View.VISIBLE);
                    }
                }, 1000);
                break;
            default:
                break;
        }
    }

    // 定时任务间隔时间
    private static final int POLL_INTERVAL = 300;

    // 录音音量状态展示
    private Runnable mPollTask = new Runnable() {
        public void run() {
            double amp = recordUtils.getAmplitude();
            updateDisplay(amp);
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);
        }
    };
    // 录音计时器
    private Runnable mTimerTask = new Runnable() {
        public void run() {
            if (!isRecording) return;
            recordTime++;
            if (recordTime == RECORDER_TIME_MAXTIME) {
                IS_OVERTIME = true;
                recordTime = 0;
                tvVoiceTimeLeft.setVisibility(View.INVISIBLE);
                return;
            }
            if (recordTime >= RECORDER_TIME_MAXTIME - 10) {
                if (recordTime == RECORDER_TIME_MAXTIME - 10) {
                    Vibrator vibrator = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(300);
                }
                tvVoiceTimeLeft.setVisibility(View.VISIBLE);
                tvVoiceTimeLeft.setText("录音时间还剩下" + (RECORDER_TIME_MAXTIME - recordTime) + "秒");
            }else {
                tvVoiceTimeLeft.setVisibility(View.INVISIBLE);
            }
            mHandler.postDelayed(mTimerTask, 1000);
        }
    };

    // 录音初始化
    private void initRecorder(String name) {
        recordUtils.init(name);
        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
        mHandler.postDelayed(mTimerTask, 1000);
    }

    // 开始录制
    private boolean startRecorder() {
        GlobalUtil.muteAudioFocus(ctx, true);
        return recordUtils.start();
    }

    // 停止录音操作
    private void stopRecorder() {
        GlobalUtil.muteAudioFocus(ctx, false);
        llRecordStatus.setVisibility(View.GONE);
        mHandler.removeCallbacks(mPollTask);
        mHandler.removeCallbacks(mTimerTask);
        recordTime = 0;
        recordUtils.stop();
    }

    // 删除录音文件
    private void deleteVoiceFile() {
        File newPath = new File(PathUtil.getVoicePath() + fileName);
        newPath.delete();
    }
}
