package com.weqia.wq.component.db;

import android.database.sqlite.SQLiteDatabase;

import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.datastorage.db.DaoConfig;
import com.weqia.utils.datastorage.db.DbUpdateListener;
import com.weqia.utils.datastorage.db.DbUtil;
import com.weqia.utils.datastorage.db.sqlite.DbModel;
import com.weqia.utils.datastorage.db.sqlite.SqlBuilder;
import com.weqia.utils.datastorage.db.sqlite.SqlInfo;
import com.weqia.utils.datastorage.db.table.TableInfo;
import com.weqia.utils.exception.CheckedExceptionHandler;
import com.weqia.wq.data.global.WeqiaApplication;

import java.util.LinkedList;
import java.util.List;

public class WeqiaDbUtil extends DbUtil {

    private static int wdbVersion = 449;// 数据库版本

    public static int getDbVersion() {
        return wdbVersion;
    }

    @SuppressWarnings("deprecation")
    public WeqiaDbUtil(DaoConfig config) {
        super(config);
        L.e("数据库版本" + getDbVersion() + "-----------------");
    }

    private synchronized static WeqiaDbUtil getInstance(DaoConfig daoConfig) {
        WeqiaDbUtil dao = (WeqiaDbUtil) getDaoMap().get(daoConfig.getDbName());
        if (dao == null) {
            setDbVersion(wdbVersion);
            dao = new WeqiaDbUtil(daoConfig);
            dao.setDbName(daoConfig.getDbName());
            getDaoMap().put(daoConfig.getDbName(), dao);
        }
        // L.w(daoMap.toString());
        return dao;
    }

    /**
     * @param daoConfig
     * @return
     */
    public static WeqiaDbUtil create(DaoConfig daoConfig) {
        if (daoConfig != null) {
            daoConfig.setDbUpdateListener(new DbUpdateListener() {
                @Override
                public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                    SqliteDbHelper.onUpgrade(db, oldVersion, newVersion);
                }

                @Override
                public void onDowngrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
                    L.e("注意出问题！");
                }
            });
        }
        return getInstance(daoConfig);
    }

//    /**
//     * @param context
//     */
//    public static WeqiaDbUtil create(Context context) {
//        DaoConfig config = new DaoConfig();
//        config.setContext(context);
//        return getInstance(config);
//
//    }
//
//    /**
//     * @param context
//     * @param dbName  数据库名称
//     */
//    public static WeqiaDbUtil create(Context context, String dbName) {
//        DaoConfig config = new DaoConfig();
//        config.setContext(context);
//        config.setDbName(dbName);
//        return getInstance(config);
//    }
//
//    /**
//     * @param context          上下文
//     * @param dbName           数据库名字
//     * @param dbVersion        数据库版本信息
//     * @param dbUpdateListener 数据库升级监听器：如果监听器为null，升级的时候将会清空所所有的数据
//     * @return
//     */
//    public static WeqiaDbUtil create(Context context, String dbName, int dbVersion,
//                                     DbUpdateListener dbUpdateListener) {
//        DaoConfig config = new DaoConfig();
//        config.setContext(context);
//        config.setDbName(dbName);
////        config.setDbVersion(dbVersion);
//        config.setDbUpdateListener(dbUpdateListener);
//        return getInstance(config);
//    }

    public void saveMsgN(Object entity, String time) {
        checkTableExist(entity.getClass());
        List<?> obj = findAllByWhereN(entity.getClass(), "time = '" + time + "'");
        if (obj == null || obj.size() == 0) {
            exeSqlInfo(SqlBuilder.buildInsertSql(entity));
        }
    }

//    public void saveMsgSafety(MsgData entity, String globalMsgId) {
//        checkTableExist(entity.getClass());
//        String whereSrt = "globalMsgId = '" + globalMsgId + "'";
//        List<MsgData> obj = findAllByWhereN(MsgData.class, whereSrt);
//        if (StrUtil.listIsNull(obj)) {
//            exeSqlInfo(SqlBuilder.buildInsertSql(entity));
//        } else if (obj.size() > 0) {
//            MsgData msgData = obj.get(0);
//            /**
//             *消息更新为未读
//             */
//            if (msgData != null) {
//                entity.setContent(msgData.getContent());
//                entity.setType(msgData.getType());
//                update(entity, whereSrt);
//            }
//        }
//    }

    public void upadteRedDot(Object entity, String btype, String coId) {
        checkTableExist(entity.getClass());
        List<?> obj =
                findAllByWhereN(entity.getClass(), "btype = '" + btype + "' AND gCoId = '" + coId
                        + "'");
        if (obj == null || obj.size() == 0) {
            exeSqlInfo(SqlBuilder.buildInsertSql(entity));
        } else {
            deleteByWhere(entity.getClass(), "btype = '" + btype + "' AND gCoId = '" + coId + "'");
            exeSqlInfo(SqlBuilder.buildInsertSql(entity));
        }
    }

    /**
     * 公共未读消息条数
     * @Description
     * @author Dminter
     */
    public <T> Integer findNoticeUnRead(Class<T> clazz) {
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        DbModel model =
                findDbModelBySQL("SELECT count(*) as count  FROM " + tableInfo.getTableName()
                        + " WHERE readed = 1");
        Integer count = null;
        try {
            if (model != null) {
                count = model.getInt("count");
            }
        } catch (Exception e) {
            CheckedExceptionHandler.handleException(e);
        }
        return count;
    }

    public <T> Integer findNoReadN(Class<T> clazz, String where) {
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        DbModel model =
                findDbModelBySQL("SELECT count(*) as count  FROM " + tableInfo.getTableName()
                        + " WHERE " + where);
        Integer count = null;
        try {
            if (model != null) {
                count = model.getInt("count");
            }
        } catch (Exception e) {
            CheckedExceptionHandler.handleException(e);
        }
        return count;
    }

    public <T> Integer findMiniMarkId(Class<T> clazz) {
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        Object idName = tableInfo.getId().getColumn();
        DbModel model =
                findDbModelBySQL("SELECT min(" + idName + ") as count  from "
                        + tableInfo.getTableName() + " WHERE pushData =1");
        Integer count = null;
        try {
            if (model != null) {
                count = model.getInt("count");
            }
        } catch (Exception e) {
            CheckedExceptionHandler.handleException(e);
        }
        return count;
    }

    public <T> Integer findNoReadNewContact(Class<T> clazz) {
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        DbModel model =
                findDbModelBySQL("SELECT count(*) as count  FROM " + tableInfo.getTableName()
                        + " WHERE readed in(1,2) AND readed = 1 AND gCoId = '"
                        + WeqiaApplication.getgMCoId() + "'");
        Integer count = null;
        try {
            if (model != null) {
                count = model.getInt("count");
            }
        } catch (Exception e) {
            CheckedExceptionHandler.handleException(e);
        }
        return count;
    }

    public <T> Integer findNoReadByWhereN(Class<T> clazz, String where) {
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        DbModel model =
                findDbModelBySQL("SELECT count(*) as count  FROM " + tableInfo.getTableName()
                        + " WHERE readed = 1 and " + where);
        Integer count = null;
        try {
            if (model != null) {
                count = model.getInt("count");
            }
        } catch (Exception e) {
            CheckedExceptionHandler.handleException(e);
        }

        return count;
    }

    public <T> T findByWhere(Class<T> clazz, String strWhere) {
        checkTableExist(clazz);
        return findBySql(clazz, SqlBuilder.getSelectSQL(clazz) + " WHERE " + strWhere);
    }

    public <T> Integer findCountBySql(Class<T> clazz, String sql) {
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        DbModel model =
                findDbModelBySQL("SELECT count(*) as count  FROM " + tableInfo.getTableName()
                        + " WHERE " + sql);
        Integer count = null;
        try {
            if (model != null) {
                count = model.getInt("count");
            }
        } catch (Exception e) {
            CheckedExceptionHandler.handleException(e);
        }
        return count;
    }

    /**
     * 全部置为已读
     * @Description
     * @author Dminter
     */
    public <T> void readAll(Class<T> clazz) {
        checkTableExist(clazz);

        TableInfo tableInfo = TableInfo.get(clazz);
        findDbModelBySQL("UPDATE  " + tableInfo.getTableName() + " SET readed = 0 ");
//        findDbModelBySQL("UPDATE  " + tableInfo.getTableName() + " SET readed = 0 WHERE gCoId = '"
//                + WeqiaApplication.getgMCoId() + "'");
    }

    public <T> void readAll(Class<T> clazz, String coId) {
        checkTableExist(clazz);
        StringBuilder where = new StringBuilder();
        if (StrUtil.notEmptyOrNull(coId)) {
            where.append(" WHERE gCoId = '").append(coId).append("'");
        }
        TableInfo tableInfo = TableInfo.get(clazz);
        findDbModelBySQL("UPDATE  " + tableInfo.getTableName() + " SET readed = 0"
                + where.toString());
    }

    /**
     * 全部置为已读
     * @Description
     * @author Dminter
     */
    public <T> void readAllByWhere(Class<T> clazz, String where) {
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        findDbModelBySQL("UPDATE  " + tableInfo.getTableName() + " SET readed = 0" + " WHERE "
                + where);
        // + " AND gCoId = '" + WeqiaApplication.getgMCoId() + "'"
    }

    public <T> void unReadByWhere(Class<T> clazz, String where) {
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        findDbModelBySQL("UPDATE  " + tableInfo.getTableName() + " SET readed = 1" + " WHERE "
                + where);
        // + " AND gCoId = '" + WeqiaApplication.getgMCoId() + "'"
    }

    public <T> void readAllByWhere(Class<T> clazz, String strWhere, String coId) {
        checkTableExist(clazz);
        StringBuilder where = new StringBuilder();
        where.append(strWhere);
        if (StrUtil.notEmptyOrNull(coId)) {
            where.append(" AND gCoId = '").append(coId).append("'");
        }
        TableInfo tableInfo = TableInfo.get(clazz);
        findDbModelBySQL("UPDATE  " + tableInfo.getTableName() + " SET readed = 0" + " WHERE "
                + where);
    }

    public <T> void readVoiceByWhere(Class<T> clazz, String where) {
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        findDbModelBySQL("UPDATE  " + tableInfo.getTableName() + " SET voiceRead = 0" + " WHERE "
                + where);

    }

    /**
     * 查找所有数据
     * @param orderBy 排序的字段
     */
    public <T> List<T> findAll(Class<T> clazz, String orderBy) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz) + " WHERE gCoId = '"
                + WeqiaApplication.getgMCoId() + "' ORDER BY " + orderBy + " DESC");
    }

    public <T> List<T> findAllWhere(Class<T> clazz, String where) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz) + " WHERE " + where);
    }

    public <T> List<T> findAllNoCoId(Class<T> clazz, String orderBy) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz) + " ORDER BY " + orderBy
                + " DESC");
    }

    public <T> List<T> findAllByCo(Class<T> clazz) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz) + " WHERE gCoId = '"
                + WeqiaApplication.getgMCoId() + "'");
    }

    public <T> List<T> findAll(Class<T> clazz, String orderBy, Integer rowId, Boolean bUp,
                               Integer size) {
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        if (bUp) {
            return findAllBySql(clazz,
                    SqlBuilder.getSelectSQL(clazz) + " WHERE " + tableInfo.getId().getColumn()
                            + " > " + rowId + " AND gCoId = '" + WeqiaApplication.getgMCoId()
                            + "' ORDER BY " + orderBy + " DESC" + SqlBuilder.getLimit(0, size));
        } else {
            return findAllBySql(clazz,
                    SqlBuilder.getSelectSQL(clazz) + " WHERE " + tableInfo.getId().getColumn()
                            + " < " + rowId + " AND gCoId = '" + WeqiaApplication.getgMCoId()
                            + "' ORDER BY " + orderBy + " DESC" + SqlBuilder.getLimit(0, size));
        }
    }

    public <T> List<T> findAllOrderBy(Class<T> clazz, String orderBy, Integer startId, Integer size) {
        checkTableExist(clazz);
        return findAllBySql(clazz,
                SqlBuilder.getSelectSQL(clazz) + " WHERE gCoId = '" + WeqiaApplication.getgMCoId()
                        + "' ORDER BY " + orderBy + SqlBuilder.getLimit(startId, size));
    }

    public <T> T findTop(Class<T> clazz) {
        T t = null;
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        // " WHERE gCoId = '" + WeqiaApplication.getgMCoId() + "'
        List<T> list =
                findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz) + " ORDER BY "
                        + tableInfo.getId().getColumn() + "+0 DESC" + SqlBuilder.getLimit(0, 1));
        if (list != null && list.size() > 0) {
            t = list.get(0);
        } else {
            t = null;
        }
        return t;
    }

    public <T> T findBlow(Class<T> clazz) {
        T t = null;
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        List<T> list =
                findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz) + " WHERE gCoId = '"
                        + WeqiaApplication.getgMCoId() + "' ORDER BY "
                        + tableInfo.getId().getColumn() + "+0 ASC" + SqlBuilder.getLimit(0, 1));
        if (list != null && list.size() > 0) {
            t = list.get(0);
        } else {
            t = null;
        }
        return t;
    }

    public <T> T findTopByWhere(Class<T> clazz, String where) {
        T t = null;
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        List<T> list =
                findAllBySql(
                        clazz,
                        SqlBuilder.getSelectSQL(clazz) + " WHERE " + where + " ORDER BY "
                                + tableInfo.getId().getColumn() + "+0 DESC"
                                + SqlBuilder.getLimit(0, 1));
        // + " AND gCoId = '" + WeqiaApplication.getgMCoId()
        if (list != null && list.size() > 0) {
            t = list.get(0);
        } else {
            t = null;
        }
        return t;
    }

    public <T> T findTopWithKey(Class<T> clazz, String key, String coId) {
        T t = null;
        checkTableExist(clazz);
        List<T> list =
                findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz) + " WHERE coId = '" + coId + "'"
                        + " ORDER BY " + key + " DESC" + SqlBuilder.getLimit(0, 1));
        if (list != null && list.size() > 0) {
            t = list.get(0);
        } else {
            t = null;
        }
        return t;
    }

    public <T> T findTopWithKeyNoCoId(Class<T> clazz, String key) {
        T t = null;
        checkTableExist(clazz);
        List<T> list =
                findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz) + " ORDER BY " + key + " DESC"
                        + SqlBuilder.getLimit(0, 1));
        if (list != null && list.size() > 0) {
            t = list.get(0);
        } else {
            t = null;
        }
        return t;
    }

    public <T> T findTopWhereWithKey(Class<T> clazz, String where, String key) {
        T t = null;
        checkTableExist(clazz);
        List<T> list =
                findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz) + " WHERE gCoId = '"
                        + WeqiaApplication.getgMCoId() + "' AND " + where + " ORDER BY " + key
                        + " DESC" + SqlBuilder.getLimit(0, 1));
        if (list != null && list.size() > 0) {
            t = list.get(0);
        } else {
            t = null;
        }
        return t;
    }

    public <T> List<T> findAllByKey(Class<T> clazz) {
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz) + " WHERE gCoId = '"
                + WeqiaApplication.getgMCoId() + "' ORDER BY " + tableInfo.getId().getColumn()
                + "+0  DESC");
    }

    public <T> List<T> findAllByKeyASC(Class<T> clazz) {
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz) + " WHERE gCoId = '"
                + WeqiaApplication.getgMCoId() + "' ORDER BY " + tableInfo.getId().getColumn()
                + "+0  ASC");
    }

    public <T> List<T> findAllByKeyWhere(Class<T> clazz, String strWhere) {
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQLByWhere(clazz, strWhere)
                + " AND gCoId = '" + WeqiaApplication.getgMCoId() + "' ORDER BY "
                + tableInfo.getId().getColumn() + "+0  DESC");
    }


    public <T> List<T> findAllByKey(Class<T> clazz, Integer startId, Integer size) {
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz) + " WHERE gCoId = '"
                + WeqiaApplication.getgMCoId() + "' ORDER BY " + tableInfo.getId().getColumn()
                + "+0  DESC" + SqlBuilder.getLimit(startId, size));
    }

    public <T> List<T> findAllOrder(Class<T> clazz, String order, Integer startId, Integer size) {
        checkTableExist(clazz);
        return findAllBySql(clazz,
                SqlBuilder.getSelectSQL(clazz) + " WHERE gCoId = '" + WeqiaApplication.getgMCoId()
                        + "' ORDER BY " + order + "+0  DESC" + SqlBuilder.getLimit(startId, size));
    }

    public <T> List<T> findAllByKeyAsc(Class<T> clazz, Integer startId, Integer size) {
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz) + " WHERE gCoId = '"
                + WeqiaApplication.getgMCoId() + "' ORDER BY " + tableInfo.getId().getColumn()
                + "+0  ASC" + SqlBuilder.getLimit(startId, size));
    }

    public <T> List<T> findAllByKeyWhere(Class<T> clazz, String strWhere, Integer startId,
                                         Integer size) {
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQLByWhere(clazz, strWhere)
                + " AND gCoId = '" + WeqiaApplication.getgMCoId() + "' ORDER BY "
                + tableInfo.getId().getColumn() + "+0 DESC" + SqlBuilder.getLimit(startId, size));
    }

    public <T> List<T> findAllByKeyWhereNoCo(Class<T> clazz, String strWhere, Integer startId,
                                             Integer size) {
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQLByWhere(clazz, strWhere) + " ORDER BY "
                + tableInfo.getId().getColumn() + "+0 DESC" + SqlBuilder.getLimit(startId, size));
    }

    public <T> List<T> findAllByKeyWhereNoCoOrderBy(Class<T> clazz, String strWhere, Integer startId,
                                                    Integer size, String orderBy) {
        checkTableExist(clazz);
        @SuppressWarnings("unused")
        TableInfo tableInfo = TableInfo.get(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQLByWhere(clazz, strWhere) + " ORDER BY "
                + orderBy + "+0 DESC" + SqlBuilder.getLimit(startId, size));
    }

    public <T> List<T> findAllByKeyWhereNoCoOrderByAsc(Class<T> clazz, String strWhere, Integer startId,
                                                    Integer size, String orderBy) {
        checkTableExist(clazz);
        @SuppressWarnings("unused")
        TableInfo tableInfo = TableInfo.get(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQLByWhere(clazz, strWhere) + " ORDER BY "
                + orderBy + "+0 ASC" + SqlBuilder.getLimit(startId, size));
    }

    public <T> List<T> findAllByWhere(Class<T> clazz, String strWhere) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQLByWhere(clazz, strWhere));
        // + " AND gCoId = '"
        // + WeqiaApplication.getgMCoId() + "'"
    }

    public <T> List<T> findAllByWhereOrderByAsc(Class<T> clazz, String strWhere, String orderBy) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQLByWhere(clazz, strWhere) + " ORDER BY "
                + orderBy + " ASC");
    }

    public <T> List<T> findAllByWhere(Class<T> clazz, String strWhere, String coId) {
        checkTableExist(clazz);
        StringBuilder where = new StringBuilder();
        where.append(strWhere);
        if (StrUtil.notEmptyOrNull(coId)) {
            where.append(" AND gCoId = '").append(coId).append("'");
        }
        return findAllBySql(clazz, SqlBuilder.getSelectSQLByWhere(clazz, where.toString()));
    }

    public <T> List<T> findAllByWhereNoCo(Class<T> clazz, String strWhere) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQLByWhere(clazz, strWhere));
    }

    /**
     * 根据条件查询多少条
     * @Description
     */
    public <T> List<T> findAllByWhere(Class<T> clazz, String strWhere, Integer startId, Integer size) {
        checkTableExist(clazz);
        return findAllBySql(
                clazz,
                SqlBuilder.getSelectSQLByWhereLimit(clazz, strWhere + " AND gCoId = '"
                        + WeqiaApplication.getgMCoId() + "'", startId, size));
    }


    public <T> List<T> findAllByWhereOrderBy(Class<T> clazz, String strWhere, String orderBy) {
        checkTableExist(clazz);
        return findAllBySql(
                clazz,
                SqlBuilder.getSelectSQLByWhere(clazz, strWhere + " AND gCoId = '"
                        + WeqiaApplication.getgMCoId() + "'")
                        + " ORDER BY " + orderBy);
    }


    public <T> List<T> findAllByWhereOrderByPjId(Class<T> clazz, String strWhere, String orderBy, String pjId) {
        checkTableExist(clazz);
        return findAllBySql(
                clazz,
                SqlBuilder.getSelectSQLByWhere(clazz, strWhere + " AND pjId = '"
                        + pjId + "'")
                        + " ORDER BY " + orderBy);
    }


    public <T> List<T> findAllByWhereOrderByNoCo(Class<T> clazz, String strWhere, String orderBy) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQLByWhere(clazz, strWhere) + " ORDER BY "
                + orderBy);
    }

    public <T> List<T> findAllByWhereOrderByNoCo(Class<T> clazz, String strWhere, String orderBy, Integer startId, Integer size) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQLByWhere(clazz, strWhere) + " ORDER BY " + orderBy + SqlBuilder.getLimit(startId, size));
    }

    public <T> List<T> findAllByWhereN(Class<T> clazz, String strWhere) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQLByWhere(clazz, strWhere));
    }

    public <T> List<T> findAllByWhere(Class<T> clazz, String strWhere, String orderBy,
                                      Integer startId, Integer size) {
        checkTableExist(clazz);
        return findAllBySql(
                clazz,
                SqlBuilder.getSelectSQLByWhere(clazz, strWhere + "  AND gCoId = '"
                        + WeqiaApplication.getgMCoId() + "'")
                        + " ORDER BY " + orderBy + " ASC" + SqlBuilder.getLimit(startId, size));
    }

    public <T> List<T> findAllByWhereNoCo(Class<T> clazz, String strWhere, String orderBy,
                                          Integer startId, Integer size) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQLByWhere(clazz, strWhere) + " ORDER BY "
                + orderBy + " ASC" + SqlBuilder.getLimit(startId, size));
    }

    public <T> List<T> findAllByWhereDesc(Class<T> clazz, String strWhere, String orderBy) {
        checkTableExist(clazz);
        return findAllBySql(
                clazz,
                SqlBuilder.getSelectSQLByWhere(clazz, strWhere + " AND gCoId = '"
                        + WeqiaApplication.getgMCoId() + "'")
                        + " ORDER BY " + orderBy + " DESC");
    }

    public <T> List<T> findAllByWhereDesc(Class<T> clazz, String strWhere, String orderBy,
                                          Integer startId, Integer size) {
        checkTableExist(clazz);
        return findAllBySql(
                clazz,
                SqlBuilder.getSelectSQLByWhere(clazz, strWhere + " AND gCoId = '"
                        + WeqiaApplication.getgMCoId() + "'")
                        + " ORDER BY " + orderBy + " DESC" + SqlBuilder.getLimit(startId, size));
    }

    public <T> List<T> findAllByWhereDescNoCo(Class<T> clazz, String strWhere, String orderBy,
                                              Integer startId, Integer size) {
        checkTableExist(clazz);
        return findAllBySql(
                clazz,
                SqlBuilder.getSelectSQLByWhere(clazz, strWhere + " ORDER BY " + orderBy + " DESC"
                        + SqlBuilder.getLimit(startId, size)));
    }

    public <T> List<T> findTalkByWhereDesc(Class<T> clazz, String strWhere, Integer startId,
                                           Integer size) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQLByWhere(clazz, strWhere)
                + " ORDER BY globalMsgId+0 DESC" + SqlBuilder.getLimit(startId, size));
    }

    public <T> List<T> findTalkByWhereAsc(Class<T> clazz, String strWhere, Integer startId,
                                          Integer size) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQLByWhere(clazz, strWhere)
                + " ORDER BY globalMsgId+0 ASC" + SqlBuilder.getLimit(startId, size));
    }

    public <T> List<T> findTalkByWhereAll(Class<T> clazz, String strWhere) {
        checkTableExist(clazz);
        return findAllBySql(
                clazz,
                SqlBuilder.getSelectSQLByWhere(clazz, strWhere + " AND gCoId = '"
                        + WeqiaApplication.getgMCoId() + "'")
                        + " ORDER BY globalMsgId+0 DESC");
    }

    // public <T> List<T> findTalkByWhereDesc(Class<T> clazz, String strWhere, Integer startId,
    // Integer size) {
    // checkTableExist(clazz);
    // return findAllBySql(clazz,
    // SqlBuilder.getSelectSQLByWhere(clazz, strWhere + " AND gCoId = '" +
    // WeqiaApplication.getgMCoId() +
    // "'")
    // + " ORDER BY sendTime + 0 DESC,globalMsgId DESC" + SqlBuilder.getLimit(startId, size)
    // );
    // }

    public <T> List<T> findAllByWhereDesc(Class<T> clazz, String strWhere, String orderBy,
                                          Integer rowId, Boolean bUp, Integer size) {
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        if (bUp) {
            return findAllBySql(clazz,
                    SqlBuilder.getSelectSQL(clazz) + " WHERE " + strWhere + " AND "
                            + tableInfo.getId().getColumn() + " > " + rowId + " AND gCoId = '"
                            + WeqiaApplication.getgMCoId() + "' ORDER BY " + orderBy + " DESC"
                            + SqlBuilder.getLimit(0, size));
        } else {
            return findAllBySql(clazz,
                    SqlBuilder.getSelectSQL(clazz) + " WHERE " + strWhere + " AND "
                            + tableInfo.getId().getColumn() + " < " + rowId + " AND gCoId = '"
                            + WeqiaApplication.getgMCoId() + "' ORDER BY " + orderBy + " DESC"
                            + SqlBuilder.getLimit(0, size));
        }
    }

    public void clearByTableName(String tabName) {
        String sql = "DELETE FROM " + tabName;
        SqlInfo sqlInfo = new SqlInfo();
        sqlInfo.setSql(sql);
        sqlInfo.setBindArgs(new LinkedList<Object>());
        exeSqlInfo(sqlInfo);
    }
}
