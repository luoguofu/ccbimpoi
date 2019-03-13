package com.weqia.wq;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.view.KeyEvent;

import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.datastorage.file.PathUtil;
import com.weqia.utils.exception.CheckedExceptionHandler;
import com.weqia.utils.http.okgo.callback.FileCallback;
import com.weqia.wq.component.utils.GlobalUtil;
import com.weqia.wq.component.utils.LnUtil;
import com.weqia.wq.component.utils.request.ResultEx;
import com.weqia.wq.component.utils.request.ServiceParams;
import com.weqia.wq.component.utils.request.ServiceRequester;
import com.weqia.wq.component.utils.request.UserService;
import com.weqia.wq.data.AttachmentData;
import com.weqia.wq.data.EnumData;
import com.weqia.wq.data.EnumData.AttachType;
import com.weqia.wq.data.UpFileData;
import com.weqia.wq.data.WPf;
import com.weqia.wq.data.global.Hks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PlayerManager implements SensorEventListener {

    /**
     * 外放模式
     */
    public static final int MODE_SPEAKER = 0;

    /**
     * 耳机模式
     */
    public static final int MODE_HEADSET = 1;

    /**
     * 听筒模式
     */
    public static final int MODE_EARPIECE = 2;

    private static PlayerManager playerManager;

    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private PlayCallback callback;
    // private Context context;
    private SensorManager sensorManager;
    private Sensor sensor;
    private HeadsetReceiver receiver;
//    private NoisyAudioStreamReceiver noisyReceiver;

    private boolean isPause = false;
    // private String filePath;
    private Activity ctx;

    private int currentMode = MODE_SPEAKER;
    private boolean debug_player = true;

    public static PlayerManager getManager(Activity ctx) {
        if (playerManager == null) {
            synchronized (PlayerManager.class) {
                playerManager = new PlayerManager(ctx);
            }
        }
        return playerManager;
    }

    private PlayerManager(Activity ctx) {
        this.ctx = ctx;
        initMediaPlayer();
        initAudioManager();
    }

    public static PlayerManager getPlayerManager() {
        return playerManager;
    }

    /**
     * 初始化播放器
     */
    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    /**
     * 初始化音频管理器
     */
    @SuppressLint("InlinedApi")
    private void initAudioManager() {
        audioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.isBluetoothA2dpOn()) {
            if (debug_player) L.e("蓝牙在使用,不更改默认播放mode");
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        } else {
            audioManager.setMode(AudioManager.MODE_IN_CALL);
        }
        audioManager.setSpeakerphoneOn(true); // 默认为扬声器播放
    }

    public void onStart() {
        if (ctx != null) {
            ctx.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    addSensorAndHeadset();

                }
            });
        }
    }

    private void addSensorAndHeadset() {
        sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorManager.registerListener(PlayerManager.this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        receiver = new HeadsetReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        filter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        ctx.registerReceiver(receiver, filter);
    }

    public void onStop() {
        if (ctx != null) {
            ctx.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (sensorManager != null)
                        sensorManager.unregisterListener(PlayerManager.this);
                    try {
                        if (receiver != null)
                            ctx.unregisterReceiver(receiver);
                    } catch (IllegalArgumentException e) {

                    }
                }
            });
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
//            case KeyEvent.KEYCODE_VOLUME_UP:
//                playerManager.raiseVolume();
//                return true;
//            case KeyEvent.KEYCODE_VOLUME_DOWN:
//                playerManager.lowerVolume();
//                return true;
            default:
                return false;
        }
    }

//    /**
//     * 调大音量
//     */
//    private void raiseVolume() {
//        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE,
//                AudioManager.FX_FOCUS_NAVIGATION_UP);
//    }
//
//    /**
//     * 调小音量
//     */
//    private void lowerVolume() {
//        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER,
//                AudioManager.FX_FOCUS_NAVIGATION_UP);
//    }


    /**
     * 播放回调接口
     */
    public interface PlayCallback {

        /**
         * 音乐准备完毕
         */
        void onPlaying();

        /**
         * 音乐播放完成
         */

        void onPlayError();

        /**
         * 音乐停止播放
         */
        void onPlayEnd();
    }

    /**
     * 播放音乐
     *
     * @param path      音乐文件路径
     * @param tCallback 播放回调函数
     */
    public void play(String path, PlayCallback tCallback) {

        // this.filePath = path;
        this.callback = tCallback;
        if (PathUtil.isPathInDisk(path)) {
            playLocal(path, null);
            return;
        } else {
            String localPath = LnUtil.getLocalpath(path, AttachType.VOICE.value());
            if (StrUtil.notEmptyOrNull(localPath)) {
                playLocal(localPath, path);
                return;
            }
        }
        playNet(path);
    }

    private void playNet(final String path) {
        // 得到真实下载路径
        ServiceParams params = new ServiceParams(10000);
        params.put("urls", path);
        UserService.getDataFromServer(params, new ServiceRequester() {

            @Override
            public void onResult(ResultEx resultEx) {
                UpFileData objUrl = resultEx.getDataObject(UpFileData.class);
                if (objUrl != null && StrUtil.notEmptyOrNull(objUrl.getUrl())) {
                    String realUrl = objUrl.getUrl();
                    downPlay(ctx, path, realUrl);
                }
            }

            private void downPlay(final Activity ctx, final String voiceUrl, String realUrl) {
                if (StrUtil.notEmptyOrNull(realUrl)) {
                    UserService.downloadVoiceFile(realUrl, voiceUrl, new FileCallback() {

                        @Override
                        public void onSuccess(File file) {
                            String voicePath = PathUtil.getVoicePath() + "/" + file.getName();
                            // File tmpFile = new File(voicePath);
                            AttachmentData attData = new AttachmentData();
                            attData.setType(EnumData.AttachType.VOICE.value());
                            attData.setUrl(voiceUrl);
                            attData.setLoaclUrl(voicePath);
                            LnUtil.saveAttachData(attData, voicePath);
                            playLocal(voicePath, voiceUrl);
                        }
                    });
                }
            }
        });
    }

    private void playLocal(String path, final String netPath) {

        try {
            mediaPlayer.reset();

            if (path.startsWith("content:")) {
                mediaPlayer.setDataSource(ctx, Uri.parse(path));
            } else {
                final File voiceFile = new File(path);
                if (!voiceFile.exists()) {
//                    L.toastShort("音频文件不存在");
                    if (callback != null) {
                        callback.onPlayError();
                    }
                    return;
                }
                if (voiceFile.length() == 0) {
                    L.toastShort("音频文件长度为0");
                    voiceFile.delete();
                    if (callback != null) {
                        callback.onPlayError();
                    }
                    return;
                }
                mediaPlayer.setDataSource(new FileInputStream(voiceFile).getFD());
            }

            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    GlobalUtil.muteAudioFocus(ctx, true);
                    if (callback != null) {
                        callback.onPlaying();
                    }
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    GlobalUtil.muteAudioFocus(ctx, false);
                    if (L.D) L.e("what = " + what + ", extra = " + extra);
                    return false;
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    GlobalUtil.muteAudioFocus(ctx, false);
                    if (callback != null) {
                        callback.onPlayEnd();
                    }
                }
            });
        } catch (IllegalStateException e) {
            if (callback != null) {
                callback.onPlayError();
            }
            CheckedExceptionHandler.handleException(e);
            return;
        } catch (FileNotFoundException e) {
            if (callback != null) {
                callback.onPlayError();
            }
            CheckedExceptionHandler.handleException(e);
            return;
        } catch (IllegalArgumentException e) {
            if (callback != null) {
                callback.onPlayError();
            }
            CheckedExceptionHandler.handleException(e);
            return;
        } catch (IOException e) {
            if (callback != null) {
                callback.onPlayError();
            }
            CheckedExceptionHandler.handleException(e);
            return;
        }
    }

    public boolean isPause() {
        return isPause;
    }

    public void pause() {
        if (isPlaying()) {
            isPause = true;
            mediaPlayer.pause();
        }
    }

    public void resume() {
        if (isPause) {
            isPause = false;
            mediaPlayer.start();
        }
    }

    /**
     * 获取当前播放模式
     *
     * @return
     */
    public int getCurrentMode() {
        return currentMode;
    }

    /**
     * 切换到听筒模式
     */
    @SuppressLint("InlinedApi")
    public void changeToEarpieceMode() {
        if (playerManager.getCurrentMode() == PlayerManager.MODE_HEADSET || audioManager.isBluetoothA2dpOn()) {
            if (debug_player) L.e("目前是耳机模式,不更改到听筒");
            return;
        }
        changeToEarpieceModeDo();
    }

    private void changeToEarpieceModeDo() {
        if (debug_player) L.e("切换到听筒模式");
        /**
         *不可以切换到听筒，存在问题
         */
        currentMode = MODE_EARPIECE;
        audioManager.setSpeakerphoneOn(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    audioManager.getStreamMaxVolume(AudioManager.MODE_IN_COMMUNICATION),
                    AudioManager.FX_KEY_CLICK);
        } else {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    audioManager.getStreamMaxVolume(AudioManager.MODE_IN_CALL),
                    AudioManager.FX_KEY_CLICK);
        }


    }

    /**
     * 切换到外放模式
     */
    public void changeToSpeakerMode() {
        if (playerManager.getCurrentMode() == PlayerManager.MODE_HEADSET || audioManager.isBluetoothA2dpOn()) {
            if (debug_player) L.e("目前是耳机模式,不更改");
            return;
        }

        changeToSpeakerModeDo();
    }

    private void changeToSpeakerModeDo() {
//        if (debug_player) L.e("切换到外放模式");
        currentMode = MODE_SPEAKER;
        audioManager.setSpeakerphoneOn(true);
    }

    /**
     *正确的扬声器模式
     */
    public void changeToNormal() {
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setSpeakerphoneOn(true);
    }

    /**
     * 切换到耳机模式
     */
    private void changeToHeadsetMode() {
        currentMode = MODE_HEADSET;
        audioManager.setSpeakerphoneOn(false);

    }

    /**
     * 停止播放
     */
    public void stop() {
        if (isPlaying()) {
            try {
                mediaPlayer.stop();
                /**
                 *传感器临时切换为听筒，需要及时切换回外放模式
                 */
                if (playerManager.getCurrentMode() == PlayerManager.MODE_EARPIECE && !WPf.getInstance().get(Hks.voice_mode, Boolean.class, false)) {
                    changeToSpeakerMode();
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }

        if (currentMode != MODE_SPEAKER) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            } else {
                audioManager.setMode(AudioManager.MODE_IN_CALL);
            }
        }

    }

    /**
     * 是否正在播放
     *
     * @return 正在播放返回true, 否则返回false
     */
    public boolean isPlaying() {
        try {
            return mediaPlayer != null && mediaPlayer.isPlaying();
        } catch (IllegalStateException e) {
            return  false;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 耳机模式下直接返回
        if (playerManager.getCurrentMode() == PlayerManager.MODE_HEADSET || audioManager.isBluetoothA2dpOn()) {
            if (L.D) L.i("目前是耳机模式");
            return;
        }
        if (WPf.getInstance().get(Hks.voice_mode, Boolean.class, false)) {
            if (L.D) L.i("目前是听筒模式");
            return;
        }
        /**
         *传感器临时切换为听筒，需要及时切换回外放模式
         */
        if (playerManager.getCurrentMode() == PlayerManager.MODE_EARPIECE && !WPf.getInstance().get(Hks.voice_mode, Boolean.class, false)) {
            changeToSpeakerMode();
        }

        float value = event.values[0];
        if (isPlaying()) {
            if (value == sensor.getMaximumRange()) {
                changeToSpeakerMode();
            } else {
                changeToEarpieceMode();
            }
        } else {
            if (value == sensor.getMaximumRange()) {
                changeToSpeakerMode();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    class HeadsetReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_HEADSET_PLUG.equals(action)) {
                int state = intent.getIntExtra("state", 0);
                if (state == 1) {
                    playerManager.changeToHeadsetMode();
                } else if (state == 0) {
                    playerManager.resume();
                    if (playerManager.isPlaying()) {
                    }
                }
            } else if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(action)) {
//                playerManager.pause();
//                if (playerManager.isPause()) {
//                }
//                playerManager.changeToSpeakerMode();
                L.e("耳机断了断了====");
                if (WPf.getInstance().get(Hks.voice_mode, Boolean.class, false)) {
                    changeToEarpieceModeDo();
                } else {
                    audioManager.setMode(AudioManager.MODE_NORMAL);
                    changeToSpeakerModeDo();
                }

            } else {
            }
        }
    }
}
