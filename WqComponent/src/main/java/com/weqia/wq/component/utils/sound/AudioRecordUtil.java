package com.weqia.wq.component.utils.sound;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Build;

import com.weqia.utils.L;
import com.weqia.utils.datastorage.file.PathUtil;
import com.weqia.utils.exception.CheckedExceptionHandler;
import com.weqia.wq.data.global.WeqiaApplication;

/**
 * 录音工具包
 */
public class AudioRecordUtil {

    static final private double EMA_FILTER = 0.6;
    private MediaRecorder mRecorder = null;
    private double mEMA = 0.0;

    @SuppressWarnings("deprecation")
    @SuppressLint("InlinedApi")
    public void init(String name) {
        stop();

        mRecorder = new MediaRecorder();
        // 指定音频来源（麦克风）
        AudioManager audoManager =
                (AudioManager) WeqiaApplication.ctx.getSystemService(Context.AUDIO_SERVICE);
        if (audoManager.isWiredHeadsetOn()) {
            mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        } else {
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        }
        // 指定音频输出格式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        } else {
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        }
        // 指定音频编码方式
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        // 指定录制音频输出信息的文件
        mRecorder.setOutputFile(PathUtil.getVoicePath() + "/" + name);
        // 设置比特率
        // mRecorder.setAudioEncodingBitRate(160 * 1024);
        // 设置音轨数 1 单声道,2.立体声
        mRecorder.setAudioChannels(1);
        try {
            mRecorder.prepare();
        } catch (Exception e) {
            CheckedExceptionHandler.handleException(e);
        }
    }


    public boolean start() {
        try {
            if (mRecorder != null) {
                mRecorder.start();
                mEMA = 0.0;
            }
        } catch (Exception e) {
            // L.toastShort("录音准备中,请稍候~");
            CheckedExceptionHandler.handleException(e);
            L.e("start 失败");
            return false;
        }
        return true;
    }

    public void stop() {
        try {
            if (mRecorder != null) {
                mRecorder.stop();
                mRecorder.reset();
                mRecorder.release();
                mRecorder = null;
            }
        } catch (Exception e) {
            // L.toastShort("录音准备中,请稍候~");
            L.e("stop 失败");
            CheckedExceptionHandler.handleException(e);
        } finally {
            mRecorder = null;
        }

    }


    public void pause() {
        if (mRecorder != null) {
            mRecorder.stop();
        }
    }


    public double getAmplitude() {
        if (mRecorder != null)
            // 获取在前一次调用此方法之后录音中出现的最大振幅
            return (mRecorder.getMaxAmplitude() / 2700.0);
        else
            return 0;
    }

    public double getAmplitudeEMA() {
        double amp = getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }
}
