package com.weqia.utils.http.okserver.download;

import com.weqia.HttpInit;
import com.weqia.utils.datastorage.db.DbUtil;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public enum DownloadDBManager {

    INSTANCE;

    private Lock mLock;
//    private DownloadInfoDao infoDao;

    DownloadDBManager() {
        mLock = new ReentrantLock();

//        infoDao = new DownloadInfoDao();
    }

    /**
     * 获取下载任务
     */
    public DownloadInfo get(String key) {
        mLock.lock();
        try {
            DbUtil dbUtil = HttpInit.getInstance().getDbUtil();
            if (dbUtil != null) {
                return dbUtil.findById(key, DownloadInfo.class);
            }
            return null;
//            return infoDao.get(key);
        } finally {
            mLock.unlock();
        }
    }

    /**
     * 获取所有下载信息
     */
    public List<DownloadInfo> getAll() {
        mLock.lock();
        try {
            DbUtil dbUtil = HttpInit.getInstance().getDbUtil();
            if (dbUtil != null) {
                return dbUtil.findAll(DownloadInfo.class);
            }
            return null;
//            return infoDao.getAll();
        } finally {
            mLock.unlock();
        }
    }

    /**
     * 更新下载任务，没有就创建，有就替换
     */
    public DownloadInfo replace(DownloadInfo entity) {
        mLock.lock();
        try {
            DbUtil dbUtil = HttpInit.getInstance().getDbUtil();
            if (dbUtil != null) {
                if (entity.getDownloadLength() > 0l)
                    dbUtil.update(entity);
                else
                    dbUtil.save(entity, true);
            }
//            infoDao.replace(entity);
            return entity;
        } finally {
            mLock.unlock();
        }
    }

    /**
     * 移除下载任务
     */
    public void delete(String key) {
        mLock.lock();
        try {
            DbUtil dbUtil = HttpInit.getInstance().getDbUtil();
            if (dbUtil != null) {
                dbUtil.deleteById(DownloadInfo.class, key);
            }
//            infoDao.delete(key);
        } finally {
            mLock.unlock();
        }
    }

    /**
     * 创建下载任务
     */
    public void create(DownloadInfo entity) {
        mLock.lock();
        try {
            DbUtil dbUtil = HttpInit.getInstance().getDbUtil();
            if (dbUtil != null) {
                dbUtil.save(entity, true);
            }
//            infoDao.create(entity);
        } finally {
            mLock.unlock();
        }
    }

    /**
     * 更新下载任务
     */
    public void update(DownloadInfo entity) {
        mLock.lock();
        try {
            DbUtil dbUtil = HttpInit.getInstance().getDbUtil();
            if (dbUtil != null) {
                dbUtil.update(entity);
            }
//            infoDao.update(entity);
        } finally {
            mLock.unlock();
        }
    }

    /**
     * 清空下载任务
     */
    public boolean clear() {
        mLock.lock();
        try {
            DbUtil dbUtil = HttpInit.getInstance().getDbUtil();
            if (dbUtil != null) {
                dbUtil.clear(DownloadInfo.class);
            }
            return true;
//            return infoDao.deleteAll() > 0;
        } finally {
            mLock.unlock();
        }
    }
}