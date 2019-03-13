package com.weqia.utils.http.okserver.download;

import android.text.TextUtils;

import com.weqia.HttpInit;
import com.weqia.utils.L;
import com.weqia.utils.RefreshObjEvent;
import com.weqia.utils.datastorage.db.DbUtil;
import com.weqia.utils.datastorage.file.PathUtil;
import com.weqia.utils.http.okgo.OkGo;
import com.weqia.utils.http.okgo.callback.FileCallback;
import com.weqia.utils.http.okgo.request.BaseRequest;
import com.weqia.utils.http.okserver.task.ExecutorWithListener;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class DownloadManager {

    //定义下载状态常量
    public static final int NONE = 0;         //无状态  --> 等待
    public static final int WAITING = 1;      //等待    --> 下载，暂停
    public static final int DOWNLOADING = 2;  //下载中  --> 暂停，完成，错误
    public static final int PAUSE = 3;        //暂停    --> 等待，下载
    public static final int FINISH = 4;       //完成    --> 重新下载
    public static final int ERROR = 5;        //错误    --> 等待

    private static DownloadManager mInstance;       //使用单例模式
    private DownloadThreadPool threadPool;          //下载的线程池
    private Map<String, DownloadInfo> downloadMap;

    public static DownloadManager getInstance() {
        if (null == mInstance) {
            synchronized (DownloadManager.class) {
                if (null == mInstance) {
                    mInstance = new DownloadManager();
                }
            }
        }
        return mInstance;
    }

    private DownloadManager() {
        downloadMap = new LinkedHashMap<>();
        threadPool = new DownloadThreadPool();

        DbUtil dbUtil = HttpInit.getInstance().getDbUtil();
        if (dbUtil != null) {
            dbUtil.updateWhere(DownloadInfo.class, "state='0',networkSpeed='0'", "state in ('1','2')");
        }
    }

    /**
     * 添加一个下载任务,一句taskTag标识是否属于同一个任务
     */
    public void addTask(BaseRequest request, FileCallback listener) {
        addTask(request, null, listener);
    }

    /**
     * 添加一个下载任务,一句taskTag标识是否属于同一个任务
     */
    public void addTask(BaseRequest request, String target, FileCallback listener) {
        addTask(request, null, target, listener);
    }

    /**
     * 添加一个下载任务,一句taskTag标识是否属于同一个任务
     */
    public void addTask(BaseRequest request, String fileName, String target, FileCallback listener) {
        addTask(request, fileName, target, listener, false);
    }

    /**
     * 添加一个下载任务
     *
     * @param request   下载的网络请求
     * @param listener  下载监听
     * @param isRestart 是否重新开始下载
     */
    private void addTask(BaseRequest request, String fileName, String target, FileCallback listener, boolean isRestart) {
        String taskTag = request.getTag().toString();
        DownloadInfo downloadInfo = getDownloadInfo(taskTag);
        if (downloadInfo == null) {
            downloadInfo = new DownloadInfo();
            downloadInfo.setUrl(request.getBaseUrl());
            downloadInfo.setTaskKey(taskTag);
            downloadInfo.setFileName(fileName);
            downloadInfo.setRequest(request);
            downloadInfo.setState(DownloadManager.NONE);
            if (target == null)
                target = PathUtil.getWQPath();
            downloadInfo.setTargetFolder(target);
            DownloadDBManager.INSTANCE.replace(downloadInfo);
            downloadMap.put(target, downloadInfo);
        }
        //无状态，暂停，错误才允许开始下载
        if (L.D) L.e("下载状态为 == " + downloadInfo.getState());
        if (downloadInfo.getState() == DownloadManager.NONE
                || downloadInfo.getState() == DownloadManager.PAUSE
                || downloadInfo.getState() == DownloadManager.ERROR) {
            //构造即开始执行
            DownloadTask downloadTask = new DownloadTask(downloadInfo, isRestart, listener);
            downloadInfo.setTask(downloadTask);
        } else {
            if (downloadInfo.getState() == DownloadManager.FINISH) {
                String path = downloadInfo.getTargetFolder() + File.separator + downloadInfo.getFileName();
                File downloadFile = new File(path);
                if (downloadFile != null && downloadFile.exists() &&
                        downloadInfo.getTotalLength() == downloadInfo.getDownloadLength() && downloadInfo.getDownloadLength() == downloadFile.length()) {
                    if (L.D) L.e("文件存在=====");
                    DownloadPress downloadPress = new DownloadPress();
                    downloadPress = downloadPress.initData(downloadInfo.getTaskKey(), downloadInfo.getTargetPath(),
                            downloadInfo.getProgress(), downloadInfo.getDownloadLength(),
                            downloadInfo.getNetworkSpeed(), downloadInfo.getNetworkSpeed(),
                            downloadInfo.getState(), downloadInfo.getListener());
                    EventBus.getDefault().post(new RefreshObjEvent(OkGo.DOWNLOAD_PROGRESS, downloadPress));
                    if (listener != null)
                        listener.onSuccess(downloadFile);
                } else {
                    DownloadTask downloadTask = new DownloadTask(downloadInfo, true, listener);
                    downloadInfo.setTask(downloadTask);
                    if (L.D) L.i("文件不存在+++++++++++");
                }
            } else {
                if (L.D) L.e("已经下载或者错误啦，或者暂停啦, 继续或者重新下载");
                DownloadTask downloadTask = new DownloadTask(downloadInfo, true, listener);
                downloadInfo.setTask(downloadTask);
            }

        }
    }

//    /** 开始所有任务 */
//    public void startAllTask() {
//        for (DownloadInfo downloadInfo : mDownloadInfoList) {
//            addTask(downloadInfo.getRequest(), downloadInfo.getListener());
//        }
//    }

    public void updateTask(DownloadInfo downloadInfo) {
        if (downloadInfo != null) {
            downloadMap.put(downloadInfo.getTaskKey(), downloadInfo);
            DownloadDBManager.INSTANCE.replace(downloadInfo);
        }
    }

    /**
     * 暂停
     */
    public void pauseTask(String taskKey) {
        if (!downloadMap.containsKey(taskKey)) {
            if (L.D) L.i("不在下载队列中。。。");
            return;
        }
        DownloadInfo downloadInfo = getDownloadInfo(taskKey);
        if (downloadInfo == null) return;
        int state = downloadInfo.getState();
        //等待和下载中才允许暂停
        if ((state == DOWNLOADING || state == WAITING) && downloadInfo.getTask() != null) {
            downloadInfo.getTask().pause();
            if (L.D) L.e("暂停任务::" + taskKey);
        }
    }

    /**
     * 暂停全部任务,先暂停没有下载的，再暂停下载中的
     */
    public void pauseAllTask() {
        for (DownloadInfo info : downloadMap.values()) {
            if (info.getState() != DOWNLOADING) pauseTask(info.getTaskKey());
        }
        for (DownloadInfo info : downloadMap.values()) {
            if (info.getState() == DOWNLOADING) pauseTask(info.getTaskKey());
        }
    }

    /**
     * 停止
     */
    public void stopTask(final String taskKey) {
        DownloadInfo downloadInfo = getDownloadInfo(taskKey);
        if (downloadInfo == null) return;
        //无状态和完成状态，不允许停止
        if ((downloadInfo.getState() != NONE && downloadInfo.getState() != FINISH) && downloadInfo.getTask() != null) {
            downloadInfo.getTask().stop();
        }
    }

//    /** 停止全部任务,先停止没有下载的，再停止下载中的 */
//    public void stopAllTask() {
//        for (DownloadInfo info : mDownloadInfoList) {
//            if (info.getState() != DOWNLOADING) stopTask(info.getUrl());
//        }
//        for (DownloadInfo info : mDownloadInfoList) {
//            if (info.getState() == DOWNLOADING) stopTask(info.getUrl());
//        }
//    }

    /**
     * 删除一个任务,会删除下载文件
     */
    public void removeTask(String taskKey) {
        removeTask(taskKey, false);
    }

    /**
     * 删除一个任务,会删除下载文件
     */
    public void removeTask(String taskKey, boolean isDeleteFile) {
        if (L.D) L.e("remove 任务," + taskKey);
        final DownloadInfo downloadInfo = getDownloadInfo(taskKey);
        if (downloadInfo == null) return;
        pauseTask(taskKey);                         //暂停任务
        removeTaskByKey(taskKey);                   //移除任务
        if (isDeleteFile) deleteFile(downloadInfo.getTargetPath());   //删除文件
        DownloadDBManager.INSTANCE.delete(taskKey);            //清除数据库
    }

//    /** 删除所有任务 */
//    public void removeAllTask() {
//        //集合深度拷贝，避免迭代移除报错
//        List<String> taskKeys = new ArrayList<>();
//        for (DownloadInfo info : mDownloadInfoList) {
//            taskKeys.add(info.getTaskKey());
//        }
//        for (String url : taskKeys) {
//            removeTask(url);
//        }
//    }

    /**
     * 重新下载
     */
    public void restartTask(final String taskKey) {
        final DownloadInfo downloadInfo = getDownloadInfo(taskKey);
        if (downloadInfo != null && downloadInfo.getState() == DOWNLOADING) {
            //如果正在下载中，先暂停，等任务结束后再添加到队列开始下载
            pauseTask(taskKey);
            threadPool.getExecutor().addOnTaskEndListener(new ExecutorWithListener.OnTaskEndListener() {
                @Override
                public void onTaskEnd(Runnable r) {
                    if (r == downloadInfo.getTask().getRunnable()) {
                        //因为该监听是全局监听，每次任务被移除都会回调，所以以下方法执行一次后，必须移除，否者会反复调用
                        threadPool.getExecutor().removeOnTaskEndListener(this);
                        //此时监听给空，表示会使用之前的监听，true表示重新下载，会删除临时文件
                        addTask(downloadInfo.getRequest(), downloadInfo.getFileName(), null, downloadInfo.getListener());
                    }
                }
            });
        } else {
            pauseTask(taskKey);
            restartTaskByKey(taskKey);
        }
    }

    /**
     * 重新开始下载任务
     */
    private void restartTaskByKey(String taskKey) {
        DownloadInfo downloadInfo = getDownloadInfo(taskKey);
        if (downloadInfo == null) return;
        if (downloadInfo.getState() != DOWNLOADING) {
            DownloadTask downloadTask = new DownloadTask(downloadInfo, true, downloadInfo.getListener());
            downloadInfo.setTask(downloadTask);
        }
    }

    /**
     * 是否存在下载
     *
     * @param taskKey
     * @return
     */
    public boolean isExistDown(String taskKey) {
        DownloadInfo downloadInfo = downloadMap.get(taskKey);
        if (downloadInfo == null)
            downloadInfo = DownloadDBManager.INSTANCE.get(taskKey);
        if (downloadInfo == null)
            return false;
        return true;
    }

    /**
     * 获取一个任务
     */
    public DownloadInfo getDownloadInfo(String taskKey) {
        DownloadInfo downloadInfo = downloadMap.get(taskKey);
        if (downloadInfo == null) {
            downloadInfo = DownloadDBManager.INSTANCE.get(taskKey);
            if (downloadInfo != null)
                downloadMap.put(taskKey, downloadInfo);
//            if (L.D) L.e("从数据库里面加载-=========");
        }
//        else {
//            if (L.D) L.e("从内存里面获取任务");
//        }
        return downloadInfo;
    }

    /**
     * 移除一个任务
     */
    private void removeTaskByKey(String taskKey) {
        DownloadInfo info = downloadMap.get(taskKey);
        if (info != null) {
//                FileCallback listener = info.getListener();
//                if (listener != null) listener. .onRemove(info);
            info.removeListener();     //清除回调监听
            downloadMap.remove(taskKey);
//            iterator.remove();         //清除任务
        }
    }

    /**
     * 根据路径删除文件
     */
    private boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) return true;
        File file = new File(path);
        if (!file.exists()) return true;
        if (file.isFile()) return file.delete();
        return false;
    }

    /**
     * 设置下载目标目录
     */

    public DownloadThreadPool getThreadPool() {
        return threadPool;
    }

    public void clearTask() {
        downloadMap.clear();
        DownloadDBManager.INSTANCE.clear();
    }
}