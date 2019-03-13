package com.weqia.utils.http.okgo.cache;

import com.weqia.HttpInit;
import com.weqia.utils.datastorage.db.DbUtil;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CacheManager<T> {


    private static class CacheManagerHolder {
        private static CacheManager instance = new CacheManager();
    }

    public static CacheManager getInstance() {
        return CacheManagerHolder.instance;
    }

    private Lock mLock;

    CacheManager() {
        mLock = new ReentrantLock();
    }

    /**
     * 获取缓存
     *
     * @param key 缓存的Key
     * @return 缓存的对象实体
     */
    public CacheEntity get(String key) {
        mLock.lock();
        try {
            DbUtil dbUtil = HttpInit.getInstance().getDbUtil();
            if (dbUtil != null) {
                return dbUtil.findById(key, CacheEntity.class);
            }
            return null;
        } finally {
            mLock.unlock();
        }
    }

////    /** 返回带泛型的对象,注意必须确保泛型和对象对应才不会发生类型转换异常 */
//    @SuppressWarnings("unchecked")
//    public <T> CacheEntity get(String key, Class<T> clazz) {
//        return (CacheEntity<T>) get(key);
//    }

    /**
     * 获取所有缓存
     *
     * @return 缓存的对象实体
     */
    public List<CacheEntity> getAll() {
        mLock.lock();
        try {
//            return cacheDao.getAll();
            DbUtil dbUtil = HttpInit.getInstance().getDbUtil();
            if (dbUtil != null) {
                return dbUtil.findAll(CacheEntity.class);
            }
            return null;
        } finally {
            mLock.unlock();
        }
    }

    /**
     * 更新缓存，没有就创建，有就替换
     *
     * @param key    缓存的key
     * @param entity 需要替换的的缓存
     * @return 被替换的缓存
     */
    @SuppressWarnings("unchecked")
    public CacheEntity replace(String key, CacheEntity entity) {
        mLock.lock();
        try {
            entity.setKey(key);
            DbUtil dbUtil = HttpInit.getInstance().getDbUtil();
            if (dbUtil != null) {
                dbUtil.save(entity, true);
            }

//            cacheDao.replace((CacheEntity<Object>) entity);
            return entity;
        } finally {
            mLock.unlock();
        }
    }

    /**
     * 移除缓存
     *
     * @param key 缓存的key
     * @return 是否移除成功
     */
    public boolean remove(String key) {
        if (key == null) return true;
        mLock.lock();
        try {
            DbUtil dbUtil = HttpInit.getInstance().getDbUtil();
            if (dbUtil != null) {
                dbUtil.deleteById(CacheEntity.class, key);
                return true;
            }
            return false;
//            return cacheDao.remove(key);
        } finally {
            mLock.unlock();
        }
    }

    /**
     * 清空缓存
     *
     * @return 是否清空成功
     */
    public boolean clear() {
        mLock.lock();
        try {
            DbUtil dbUtil = HttpInit.getInstance().getDbUtil();
            if (dbUtil != null) {
                dbUtil.clear(CacheEntity.class);
                return true;
            }
            return false;
//            return cacheDao.deleteAll() > 0;
        } finally {
            mLock.unlock();
        }
    }
}