package com.weqia.utils.http.okserver.download;

import android.text.TextUtils;

import com.weqia.utils.L;
import com.weqia.utils.RefreshObjEvent;
import com.weqia.utils.http.okgo.OkGo;
import com.weqia.utils.http.okgo.callback.FileCallback;
import com.weqia.utils.http.okgo.convert.StringConvert;
import com.weqia.utils.http.okgo.utils.OkLogger;
import com.weqia.utils.http.okgo.utils.ParamsUtils;
import com.weqia.utils.http.okserver.task.PriorityAsyncTask;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class DownloadTask extends PriorityAsyncTask<Void, DownloadInfo, DownloadInfo> {

    private static final int BUFFER_SIZE = 1024 * 8; //读写缓存大小

    private DownloadInfo mDownloadInfo;              //当前任务的信息
    private long mPreviousTime;                      //上次更新的时间，用于计算下载速度
    private boolean isRestartTask;                   //是否重新下载的标识位
    private boolean isPause;                         //当前任务是暂停还是停止， true 暂停， false 停止
    private DownloadPress downloadPress;

    public DownloadTask(DownloadInfo downloadInfo, boolean isRestart, FileCallback downloadListener) {
        mDownloadInfo = downloadInfo;
        isRestartTask = isRestart;
        mDownloadInfo.setListener(downloadListener);
        //将当前任务在定义的线程池中执行
        executeOnExecutor(DownloadManager.getInstance().getThreadPool().getExecutor());
    }

    private DownloadPress getDownloadPressFromInfo(DownloadInfo info) {
        if (downloadPress == null)
            downloadPress = new DownloadPress(info.getTaskKey(), info.getTargetPath(),
                    info.getProgress(), info.getDownloadLength(),
                    info.getNetworkSpeed(), info.getNetworkSpeed(),
                    info.getState(), info.getListener());
        else {
            downloadPress = downloadPress.initData(info.getTaskKey(), info.getTargetPath(),
                    info.getProgress(), info.getDownloadLength(),
                    info.getNetworkSpeed(), info.getNetworkSpeed(),
                    info.getState(), info.getListener());
        }
        return downloadPress;
    }

    /**
     * 暂停的方法
     */
    public void pause() {
        if (mDownloadInfo.getState() == DownloadManager.WAITING || isPause) {
            //如果是等待状态的,因为该状态取消不会回调任何方法，需要手动触发
            mDownloadInfo.setNetworkSpeed(0);
            mDownloadInfo.setState(DownloadManager.PAUSE);
            postMessage(mDownloadInfo.getProgress(), mDownloadInfo.getState(), null, null);
        } else {
            isPause = true;
        }
        super.cancel(false);
    }

    /**
     * 停止的方法
     */
    public void stop() {
        if (mDownloadInfo.getState() == DownloadManager.PAUSE || mDownloadInfo.getState() == DownloadManager.ERROR || mDownloadInfo.getState() == DownloadManager.WAITING) {
            //如果状态是暂停或错误的,停止不会回调任何方法，需要手动触发
            mDownloadInfo.setNetworkSpeed(0);
            mDownloadInfo.setState(DownloadManager.NONE);
            postMessage(mDownloadInfo.getProgress(), mDownloadInfo.getState(), null, null);
        } else {
            isPause = false;
        }
        super.cancel(false);
    }

    /**
     * 每个任务进队列的时候，都会执行该方法
     */
    @Override
    protected void onPreExecute() {
        OkLogger.e("onPreExecute:" + mDownloadInfo.getFileName());

        //添加成功的回调
        FileCallback listener = mDownloadInfo.getListener();
        if (listener != null)
            listener.onBefore(mDownloadInfo.getRequest());    //onAdd(mDownloadInfo);

        //如果是重新下载，需要删除临时文件
        if (isRestartTask) {
            ParamsUtils.deleteFile(mDownloadInfo.getTargetPath());
            mDownloadInfo.setProgress(0);
            mDownloadInfo.setDownloadLength(0);
            mDownloadInfo.setTotalLength(0);
            isRestartTask = false;
        }

        mDownloadInfo.setNetworkSpeed(0);
        mDownloadInfo.setState(DownloadManager.WAITING);
        postMessage(0, mDownloadInfo.getState(), null, null);
    }

    /**
     * 如果调用了Cancel，就不会执行该方法，所以任务结束的回调不放在这里面
     */
    @Override
    protected void onPostExecute(DownloadInfo downloadInfo) {
    }

    /**
     * 一旦该方法执行，意味着开始下载了
     */
    @Override
    protected DownloadInfo doInBackground(Void... params) {
        if (isCancelled()) return mDownloadInfo;
        OkLogger.e("doInBackground:" + mDownloadInfo.getFileName());
        mPreviousTime = System.currentTimeMillis();
        mDownloadInfo.setNetworkSpeed(0);
        mDownloadInfo.setState(DownloadManager.DOWNLOADING);
        postMessage(mDownloadInfo.getProgress(), mDownloadInfo.getState(), null, null);

        long startPos = mDownloadInfo.getDownloadLength();
        Response response = null;
        try {
            response = mDownloadInfo.getRequest()
                    .headers("RANGE", "bytes=" + startPos + "-")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
            mDownloadInfo.setNetworkSpeed(0);
            mDownloadInfo.setState(DownloadManager.ERROR);
            String errStr = "下载服务器无法连接";
            postMessage(mDownloadInfo.getProgress(), mDownloadInfo.getState(), errStr, new DownErrException(errStr));
            return mDownloadInfo;
        }

        //check network data
        int code = response.code();
        if (code == 403 || code == 404 || code >= 500) {
            mDownloadInfo.setNetworkSpeed(0);
            mDownloadInfo.setState(DownloadManager.ERROR);
            try {
                if (L.D) L.e(StringConvert.create().convertSuccess(response));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String errStr = "下载文件未找到，NoSuchKey";
            if (code == 403)
                errStr = "服务器资源出错，403";
            postMessage(mDownloadInfo.getProgress(), mDownloadInfo.getState(), errStr, new DownErrException(errStr));
            return mDownloadInfo;
        }

        ResponseBody body = response.body();
        if (body == null) {
            mDownloadInfo.setNetworkSpeed(0);
            mDownloadInfo.setState(DownloadManager.ERROR);
            String errStr = "服务器返回数据为空，nobody";
            postMessage(mDownloadInfo.getProgress(), mDownloadInfo.getState(), errStr, new DownErrException(errStr));
            return mDownloadInfo;
        }

        //获取流对象，准备进行读写文件
        long totalLength = response.body().contentLength();
        if (mDownloadInfo.getTotalLength() == 0) {
            mDownloadInfo.setTotalLength(totalLength);
        }

        //构建下载文件路径，如果有设置，就用设置的，否者就自己创建
        String url = mDownloadInfo.getUrl();
        String fileName = mDownloadInfo.getFileName();
        if (TextUtils.isEmpty(fileName) && response != null) {
            fileName = ParamsUtils.getNetFileName(response, url);
            try {
                fileName = URLDecoder.decode(fileName, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (fileName.length() > 64) {
                fileName = fileName.substring(fileName.length() - 64);
            }
            mDownloadInfo.setFileName(fileName);
        }
        if (TextUtils.isEmpty(mDownloadInfo.getTargetPath())) {
            File file = new File(mDownloadInfo.getTargetFolder(), fileName);
            mDownloadInfo.setTargetPath(file.getAbsolutePath());
        }
        //检查文件有效性，文件大小大于总文件大小
        if (startPos > mDownloadInfo.getTotalLength()) {
            mDownloadInfo.setNetworkSpeed(0);
            mDownloadInfo.setState(DownloadManager.ERROR);
            String errStr = "断点文件异常，需要删除后重新下载";
            postMessage(mDownloadInfo.getProgress(), mDownloadInfo.getState(), errStr, new DownErrException(errStr));
            return mDownloadInfo;
        }
        if (startPos == mDownloadInfo.getTotalLength() && startPos > 0) {
            mDownloadInfo.setProgress(1.0f);
            mDownloadInfo.setNetworkSpeed(0);
            mDownloadInfo.setState(DownloadManager.FINISH);
            postMessage(mDownloadInfo.getProgress(), mDownloadInfo.getState(), null, null);
            return mDownloadInfo;
        }
        //设置断点写文件
        File file = new File(mDownloadInfo.getTargetPath());
        ProgressRandomAccessFile randomAccessFile;
        try {
            randomAccessFile = new ProgressRandomAccessFile(file, "rw", startPos);
            randomAccessFile.seek(startPos);
        } catch (Exception e) {
            e.printStackTrace();
            mDownloadInfo.setNetworkSpeed(0);
            mDownloadInfo.setState(DownloadManager.ERROR);
            String errStr = "没有找到已存在的断点文件";
            postMessage(mDownloadInfo.getProgress(), mDownloadInfo.getState(), errStr, new DownErrException(e));
            return mDownloadInfo;
        }

        InputStream is = response.body().byteStream();
        //读写文件流
        try {
            download(is, randomAccessFile);
        } catch (IOException e) {
            e.printStackTrace();
            mDownloadInfo.setNetworkSpeed(0);
            mDownloadInfo.setState(DownloadManager.ERROR);
            String errStr = "文件读写异常";
            postMessage(mDownloadInfo.getProgress(), mDownloadInfo.getState(), errStr, new DownErrException(errStr));
            return mDownloadInfo;
        }

        //循环结束走到这里，a.下载完成     b.暂停      c.判断是否下载出错
        if (isCancelled()) {
            OkLogger.e("state: 暂停 " + mDownloadInfo.getState());
            mDownloadInfo.setNetworkSpeed(0);
            if (isPause) mDownloadInfo.setState(DownloadManager.PAUSE); //暂停
            else mDownloadInfo.setState(DownloadManager.NONE);          //停止
            postMessage(mDownloadInfo.getProgress(), mDownloadInfo.getState(), null, null);
        } else if (mDownloadInfo.getTotalLength() == -1 && mDownloadInfo.getState() == DownloadManager.DOWNLOADING) {
            mDownloadInfo.setNetworkSpeed(0);
            mDownloadInfo.setState(DownloadManager.FINISH); //下载完成
            postMessage(mDownloadInfo.getProgress(), mDownloadInfo.getState(), null, null);
        } else if (mDownloadInfo.getTotalLength() == mDownloadInfo.getDownloadLength() && mDownloadInfo.getState() == DownloadManager.DOWNLOADING) {
            mDownloadInfo.setNetworkSpeed(0);
            mDownloadInfo.setState(DownloadManager.FINISH); //下载完成
            postMessage(mDownloadInfo.getProgress(), mDownloadInfo.getState(), null, null);
        } else if (mDownloadInfo.getTotalLength() != mDownloadInfo.getDownloadLength()) {
            mDownloadInfo.setNetworkSpeed(0);
            mDownloadInfo.setState(DownloadManager.ERROR); //由于不明原因，文件保存有误
            String errStr = "未知原因";
            postMessage(mDownloadInfo.getProgress(), mDownloadInfo.getState(), errStr, new DownErrException(errStr));
        }
        return mDownloadInfo;
    }

    private void postMessage(float progress, int state, String errorMsg, Exception e) {
        downloadPress = getDownloadPressFromInfo(mDownloadInfo);
        DownloadManager.getInstance().updateTask(mDownloadInfo);
        downloadPress.setProgress(progress);
        downloadPress.setState(state);
        downloadPress.setErrorMsg(errorMsg);
        downloadPress.setE(e);
        EventBus.getDefault().post(new RefreshObjEvent(OkGo.DOWNLOAD_PROGRESS, downloadPress));
    }

    /**
     * 执行文件下载
     */
    private int download(InputStream input, RandomAccessFile out) throws IOException {
        if (input == null || out == null) return -1;

        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        int downloadSize = 0;
        int len;
        try {
            while ((len = in.read(buffer, 0, BUFFER_SIZE)) != -1 && !isCancelled()) {
                out.write(buffer, 0, len);
                downloadSize += len;
            }
        } finally {
            try {
                out.close();
                in.close();
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return downloadSize;
    }

    /**
     * 文件读写
     */
    private final class ProgressRandomAccessFile extends RandomAccessFile {
        private long lastDownloadLength = 0; //总共已下载的大小
        private long curDownloadLength = 0;  //当前已下载的大小（可能分几次下载）
        private long lastRefreshUiTime;

        public ProgressRandomAccessFile(File file, String mode, long lastDownloadLength) throws FileNotFoundException {
            super(file, mode);
            this.lastDownloadLength = lastDownloadLength;
            this.lastRefreshUiTime = System.currentTimeMillis();
        }

        @Override
        public void write(byte[] buffer, int offset, int count) throws IOException {
            super.write(buffer, offset, count);

            //已下载大小
            long downloadLength = lastDownloadLength + count;
            curDownloadLength += count;
            lastDownloadLength = downloadLength;
            mDownloadInfo.setDownloadLength(downloadLength);

            //计算下载速度
            long totalTime = (System.currentTimeMillis() - mPreviousTime) / 1000;
            if (totalTime == 0) {
                totalTime += 1;
            }
            long networkSpeed = curDownloadLength / totalTime;
            mDownloadInfo.setNetworkSpeed(networkSpeed);

            //下载进度
            float progress = downloadLength * 1.0f / mDownloadInfo.getTotalLength();
            mDownloadInfo.setProgress(progress);
            long curTime = System.currentTimeMillis();

            //每200毫秒刷新一次数据
            if (curTime - lastRefreshUiTime >= 200 || progress == 1.0f) {
                postMessage(mDownloadInfo.getProgress(), mDownloadInfo.getState(), null, null);
                lastRefreshUiTime = System.currentTimeMillis();
            }
        }
    }
}