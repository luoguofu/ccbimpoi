package com.weqia.utils.datastorage.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.weqia.data.UtilData;
import com.weqia.data.UtilsConstants;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.datastorage.db.sqlite.CursorUtils;
import com.weqia.utils.datastorage.db.sqlite.DbModel;
import com.weqia.utils.datastorage.db.sqlite.SqlBuilder;
import com.weqia.utils.datastorage.db.sqlite.SqlInfo;
import com.weqia.utils.datastorage.db.table.KeyValue;
import com.weqia.utils.datastorage.db.table.TableInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DbUtil {

    public static int dbVersion = 1;// 数据库版本
    private static HashMap<String, DbUtil> daoMap = new HashMap<String, DbUtil>();

    private SQLiteDatabase db;
    private String dbName;

    public static int getDbVersion() {
        return dbVersion;
    }

    public static void setDbVersion(int dbVersion) {
        DbUtil.dbVersion = dbVersion;
    }

    @SuppressWarnings("deprecation")
    public DbUtil(DaoConfig config) {
        if (config == null) throw new RuntimeException("daoConfig is null");
        if (config.getContext() == null) throw new RuntimeException("android context is null");

        SQLiteDatabase db =
                new SqliteDbHelper(config.getContext().getApplicationContext(), config.getDbName(),
                        getDbVersion(), config.getDbUpdateListener()).getWritableDatabase();
        setDb(db);
    }

    private synchronized static DbUtil getDbInstance(DaoConfig daoConfig) {
        DbUtil dao = getDaoMap().get(daoConfig.getDbName());
        if (dao == null) {
            dao = new DbUtil(daoConfig);
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
    public static DbUtil createDb(DaoConfig daoConfig) {
        return getDbInstance(daoConfig);
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    public void removeDbUtil(DbUtil dbUtil) {
        if (daoMap.containsKey(dbUtil.getDbName())) {
            daoMap.remove(dbUtil.getDbName());
            this.db.close();
            this.db = null;
            dbUtil = null;
        }
    }

    public static HashMap<String, DbUtil> getDaoMap() {
        return daoMap;
    }

    public static void setDaoMap(HashMap<String, DbUtil> daoMap) {
        DbUtil.daoMap = daoMap;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbName() {
        return dbName;
    }

    /**
     * 保存数据库，速度要比save快
     *
     * @param entity
     */
    public void save(Object entity) {
        save(entity, true);
    }

    public void save(Object entity, boolean checkExist) {
        if (entity == null) {
            return;
        }
        checkTableExist(entity.getClass());
        TableInfo tableInfo = TableInfo.get(entity.getClass());
        Object idvalue = tableInfo.getId().getValue(entity);
        if (checkExist) {
            if (findById(idvalue, entity.getClass()) == null) {
                exeSqlInfo(SqlBuilder.buildInsertSql(entity));
            } else {
                update(entity);
            }
        } else {
            exeSqlInfo(SqlBuilder.buildInsertSql(entity));
        }
    }

    public void save(Object entity, Class<? extends UtilData> cls) {
        checkTableExist(cls);
        exeSqlInfo(SqlBuilder.buildInsertSql(entity));
    }

    public boolean save(Object entity, Class<? extends UtilData> cls, boolean isSave) {
        boolean flag = false;
        checkTableExist(cls);
        TableInfo tableInfo = TableInfo.get(entity.getClass());
        Object idvalue = tableInfo.getId().getValue(entity);
        if (isSave) {
            if (findById(idvalue, entity.getClass()) == null) {
                checkTableExist(entity.getClass());
                exeSqlInfo(SqlBuilder.buildInsertSql(entity));
                flag = true;
            } else {
                update(entity);
            }
        } else {
            exeSqlInfo(SqlBuilder.buildInsertSql(entity));
            flag = false;
        }
        return flag;
    }

    /**
     * 保存所有数据
     *
     * @param datas
     * @Description
     */
    public void saveAll(final List<?> datas) {
        if (datas != null) {
            this.saveAll(datas, true);
        }
    }

    public void saveAll(final List<?> datas, final boolean checkExist) {
        getDb().beginTransaction();
        for (int i = 0; i < datas.size(); i++) {
            save(datas.get(i), checkExist);
        }
        getDb().setTransactionSuccessful();
        getDb().endTransaction();
    }

    /**
     * 保存数据到数据库<br />
     * <b>注意：</b><br />
     * 保存成功后，entity的主键将被赋值（或更新）为数据库的主键， 只针对自增长的id有效
     *
     * @param entity 要保存的数据
     * @return ture： 保存成功 false:保存失败
     */
    public boolean saveBindId(Object entity) {
        checkTableExist(entity.getClass());
        List<KeyValue> entityKvList = SqlBuilder.getSaveKeyValueListByEntity(entity);
        if (entityKvList != null && entityKvList.size() > 0) {
            TableInfo tf = TableInfo.get(entity.getClass());
            ContentValues cv = new ContentValues();
            insertContentValues(entityKvList, cv);
            Long id = db.insert(tf.getTableName(), null, cv);
            if (id == -1) return false;
            tf.getId().setValue(entity, id);
            return true;
        }
        return false;
    }

    /**
     * 把List<KeyValue>数据存储到ContentValues
     *
     * @param list
     * @param cv
     */
    private void insertContentValues(List<KeyValue> list, ContentValues cv) {
        if (list != null && cv != null) {
            for (KeyValue kv : list) {
                cv.put(kv.getKey(), kv.getValue().toString());
            }
        } else {
            if (L.D) {
                L.w("insertContentValues: List<KeyValue> is empty or ContentValues is empty!");
            }
        }

    }

    /**
     * 更新数据 （主键ID必须不能为空）
     *
     * @param entity
     */
    public void update(Object entity) {
        checkTableExist(entity.getClass());
        exeSqlInfo(SqlBuilder.getUpdateSqlAsSqlInfo(entity));
    }

    /**
     * 根据条件更新数据
     *
     * @param entity
     * @param strWhere 条件为空的时候，将会更新所有的数据
     */
    public void update(Object entity, String strWhere) {
        checkTableExist(entity.getClass());
        exeSqlInfo(SqlBuilder.getUpdateSqlAsSqlInfo(entity, strWhere));
    }

    public void updateNoCheck(Object entity, String strWhere) {
        exeSqlInfo(SqlBuilder.getUpdateSqlAsSqlInfo(entity, strWhere));
    }

    /**
     * 删除数据
     *
     * @param entity entity的主键不能为空
     */
    public void delete(Object entity) {
        try {
            checkTableExist(entity.getClass());
            exeSqlInfo(SqlBuilder.buildDeleteSql(entity));
        } catch (SQLException e) {
            L.w("--", e);
        } catch (NullPointerException e) {
            L.w("--", e);
        }
    }

    public boolean delete(Object entity, boolean isDel) {
        boolean flag = false;
        checkTableExist(entity.getClass());
        TableInfo tableInfo = TableInfo.get(entity.getClass());
        Object idvalue = tableInfo.getId().getValue(entity);
        if (isDel) {
            if (findById(idvalue, entity.getClass()) != null) {
                exeSqlInfo(SqlBuilder.buildDeleteSql(entity));
                flag = true;
            } else {
                flag = false;
            }
        } else {
            exeSqlInfo(SqlBuilder.buildDeleteSql(entity));
            flag = false;
        }
        return flag;

    }

    /**
     * 根据主键删除数据
     *
     * @param clazz 要删除的实体类
     * @param id    主键值
     */
    public void deleteById(Class<?> clazz, Object id) {
        checkTableExist(clazz);
        exeSqlInfo(SqlBuilder.buildDeleteSql(clazz, id));
    }

    /**
     * 根据条件删除数据
     *
     * @param clazz
     * @param strWhere 条件为空的时候 将会删除所有的数据
     */
    public void deleteByWhere(Class<?> clazz, String strWhere) {
        checkTableExist(clazz);
        String sql = SqlBuilder.buildDeleteSql(clazz, strWhere);
        debugSql(sql);
        try {
            db.execSQL(sql);
        } catch (SQLException e) {
            L.w("--", e);
        } catch (NullPointerException e) {
            L.w("--", e);
        }
    }

    public void exeSqlInfo(SqlInfo sqlInfo) {
        try {
            if (sqlInfo != null) {
                debugSql(sqlInfo.getSql());
                db.execSQL(sqlInfo.getSql(), sqlInfo.getBindArgsAsArray());
            } else {
                if (L.D) {
                    L.e("sava error:sqlInfo is null");
                }
            }
        } catch (SQLException e) {
            L.w("--", e);
        } catch (NullPointerException e) {
            L.w("--", e);
        }
    }

    /**
     * 根据主键查找数据（默认不查询多对一或者一对多的关联数据）
     *
     * @param id
     * @param clazz
     */
    public <T> T findById(Object id, Class<T> clazz) {
        if (id == null) {
            return null;
        }
        checkTableExist(clazz);
        SqlInfo sqlInfo = SqlBuilder.getSelectSqlAsSqlInfo(clazz, id);
        if (sqlInfo != null) {
            debugSql(sqlInfo.getSql());
            Cursor cursor = null;
            try {
                String[] bindsStrings = sqlInfo.getBindArgsAsStringArray();
                if (bindsStrings == null) {
                    return null;
                }
                cursor = db.rawQuery(sqlInfo.getSql(), bindsStrings);
                if (cursor.moveToNext()) {
                    return CursorUtils.getEntity(cursor, clazz);
                }
            } catch (SQLException e) {
                L.w("--", e);
            } catch (NullPointerException e) {
                L.w("--", e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }

            }
        }
        return null;
    }

    public <T> void updateWhere(Class<T> clazz, String update, String where) {
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        findDbModelBySQL("UPDATE  " + tableInfo.getTableName() + " SET " + update + " WHERE "
                + where);
    }

    public <T> void updateBySql(Class<T> clazz, String sql) {
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        findDbModelBySQL("UPDATE  " + tableInfo.getTableName() + " SET " + sql);
    }

    public <T> void pushToRealById(Class<T> clazz, String id) {
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        Object idColumn = tableInfo.getId().getColumn();
        findDbModelBySQL("UPDATE  " + tableInfo.getTableName() + " SET pushData = 0 where "
                + idColumn + " = '" + id + "'");
    }

    /**
     * 查找所有的数据
     *
     * @param clazz
     */
    public <T> List<T> findAll(Class<T> clazz) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz));
    }

    /**
     * 查询多少条
     *
     * @param clazz
     * @param startId
     * @param size
     * @return
     * @Description
     */
    public <T> List<T> findAll(Class<T> clazz, Integer startId, Integer size) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQLLimit(clazz, startId, size));
    }

    public <T> List<T> findAllN(Class<T> clazz, String where, String orderBy) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz) + where + " ORDER BY " + orderBy
                + " DESC");
    }

    public <T> List<T> findAllOrderBy(Class<T> clazz, String orderBy) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz) + " ORDER BY " + orderBy);
    }

    /**
     * 查找所有数据
     *
     * @param clazz
     * @param orderBy 排序的字段
     */
    public <T> List<T> findAll(Class<T> clazz, String orderBy, Integer startId, Integer size) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz) + " ORDER BY " + orderBy
                + " DESC" + SqlBuilder.getLimit(startId, size));
    }

    public <T> List<T> findAllAsc(Class<T> clazz, String orderBy, Integer startId, Integer size) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz) + " ORDER BY " + orderBy + " ASC"
                + SqlBuilder.getLimit(startId, size));
    }

    public <T> List<T> findAllWhereAsc(Class<T> clazz, String where, String orderBy,
                                       Integer startId, Integer size) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz) + " WHERE " + where
                + " ORDER BY " + orderBy + " ASC" + SqlBuilder.getLimit(startId, size));
    }

    public <T> List<T> findAllWhereOrderBy(Class<T> clazz, String where, String orderBy,
                                           Integer startId, Integer size) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz) + " WHERE " + where
                + " ORDER BY " + orderBy + SqlBuilder.getLimit(startId, size));
    }

    public <T> T findTopWithKeyByWhere(Class<T> clazz, String key, String where) {
        T t = null;
        checkTableExist(clazz);
        List<T> list =
                findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz) + " WHERE " + where
                        + " ORDER BY " + key + " DESC" + SqlBuilder.getLimit(0, 1));
        if (list != null && list.size() > 0) {
            t = list.get(0);
        } else {
            t = null;
        }
        return t;
    }

    public <T> List<T> findAllByKeyN(Class<T> clazz, Integer startId, Integer size) {
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz) + " ORDER BY "
                + tableInfo.getId().getColumn() + "+0  DESC" + SqlBuilder.getLimit(startId, size));
    }

    public <T> List<T> findAllByKeyWhereN(Class<T> clazz, String strWhere, Integer startId,
                                          Integer size) {
        checkTableExist(clazz);
        TableInfo tableInfo = TableInfo.get(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQLByWhere(clazz, strWhere) + " ORDER BY "
                + tableInfo.getId().getColumn() + "+0 DESC" + SqlBuilder.getLimit(startId, size));
    }

    public <T> List<T> findAllByKeyWhereN(Class<T> clazz, String strWhere, String orderBy,
                                          Integer startId, Integer size) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQLByWhere(clazz, strWhere) + " ORDER BY "
                + orderBy + SqlBuilder.getLimit(startId, size));
    }

    /**
     * @param clazz
     * @param orderBy
     * @return
     * @Description
     * @author Dminter
     */
    public <T> List<T> findAllById(Class<T> clazz, String orderBy, String id) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQL(clazz) + "  ORDER BY " + orderBy
                + " DESC");
    }

    /**
     * 根据条件查找所有数据
     *
     * @param clazz
     * @param strWhere 条件为空的时候查找所有数据
     */
    public <T> List<T> findAllByWhereNoCo(Class<T> clazz, String strWhere) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQLByWhere(clazz, strWhere));
    }

    public <T> List<T> findAllByWhereN(Class<T> clazz, String strWhere, String orderBy) {
        checkTableExist(clazz);
        return findAllBySql(clazz, SqlBuilder.getSelectSQLByWhere(clazz, strWhere) + " ORDER BY "
                + orderBy + " ASC");
    }

    /**
     * 根据条件查找所有数据
     *
     * @param clazz
     * @param strSQL
     */
    public <T> List<T> findAllBySql(Class<T> clazz, String strSQL) {
        checkTableExist(clazz);
        debugSql(strSQL);
        Cursor cursor = null;
        try {
            if (db == null) {
                L.i("db null");
                return null;
            }
            if (StrUtil.isEmptyOrNull(strSQL)) {
                L.i("sql null");
                return null;
            }
            cursor = db.rawQuery(strSQL, null);
            if (cursor == null) {
                return null;
            }
            List<T> list = new ArrayList<T>();
            while (cursor.moveToNext()) {
                T t = CursorUtils.getEntity(cursor, clazz);
                list.add(t);
            }
            return list;
        } catch (SQLException e) {
            L.w("--", e);
        } catch (NullPointerException e) {
            L.w("--", e);
        } finally {
            if (cursor != null) cursor.close();
            cursor = null;
        }
        return null;
    }

    public <T> T findBySql(Class<T> clazz, String strSQL) {
        checkTableExist(clazz);
        debugSql(strSQL);
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(strSQL, null);
            cursor.moveToNext();
            T t = CursorUtils.getEntity(cursor, clazz);
            return t;
        } catch (SQLException e) {
            // CheckedExceptionHandler.handleException(e);
        } catch (NullPointerException e) {
            // CheckedExceptionHandler.handleException(e);
        } finally {
            if (cursor != null) cursor.close();
            cursor = null;
        }
        return null;
    }

    /**
     * 根据条件查找所有数据
     *
     * @param clazz
     * @param strSQL
     */
    public <T> List<T> findAllBySqlWithTable(Class<T> clazz, String strSQL) {
        debugSql(strSQL);
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(strSQL, null);
            List<T> list = new ArrayList<T>();
            while (cursor.moveToNext()) {
                T t = CursorUtils.getEntity(cursor, clazz);
                list.add(t);
            }
            return list;
        } catch (SQLException e) {
            L.w("--", e);
        } catch (NullPointerException e) {
            L.w("--", e);
        } finally {
            if (cursor != null) cursor.close();
            cursor = null;
        }
        return null;
    }

    /**
     * 根据sql语句查找数据，这个一般用于数据统计
     *
     * @param strSQL
     */
    public DbModel findDbModelBySQL(String strSQL) {
        debugSql(strSQL);
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(strSQL, null);
            if (cursor.moveToNext()) {
                return CursorUtils.getDbModel(cursor);
            }
        } catch (SQLException e) {
            L.w("--", e);
        } catch (NullPointerException e) {
            L.w("--", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public List<DbModel> findDbModelListBySQL(String strSQL) {
        debugSql(strSQL);
        Cursor cursor = null;
        List<DbModel> dbModelList = null;
        try {
            cursor = db.rawQuery(strSQL, null);
            dbModelList = new ArrayList<DbModel>();
            while (cursor.moveToNext()) {
                dbModelList.add(CursorUtils.getDbModel(cursor));
            }
        } catch (SQLException e) {
            L.w("--", e);
        } catch (NullPointerException e) {
            L.w("--", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return dbModelList;
    }

    /**
     * 清除表内容，自增系数指向1
     *
     * @Description o
     */
    public void clear(Class<?> clazz) {
        checkTableExist(clazz);
        // exeSqlInfo(SqlBuilder.buildClearSql(clazz));
        if (db != null) {
            try {
                db.execSQL(SqlBuilder.getClearSql(clazz));
            } catch (SQLException e) {
                L.w("--", e);
            } catch (NullPointerException e) {
                L.w("--", e);
            }
        }
    }

    public void checkTableExist(Class<?> clazz) {
        try {
            TableInfo tableInfo = TableInfo.get(clazz);
            if (!tableIsExist(tableInfo)) {
                String sql = SqlBuilder.getCreatTableSQL(clazz);
                debugSql(sql);
                db.execSQL(sql);
            }
        } catch (SQLException e) {
            L.w("--", e);
        } catch (NullPointerException e) {
            L.w("--", e);
        }

    }

    public void CreatTable(Class<?> clazz) {
        try {
            String sql = SqlBuilder.getCreatTableSQL(clazz);
            debugSql(sql);
            db.execSQL(sql);
        } catch (SQLException e) {
            L.w("--", e);
        } catch (NullPointerException e) {
            L.w("--", e);
        }

    }

    private boolean tableIsExist(TableInfo table) {
        if (table.isCheckDatabese()) return true;
        Cursor cursor = null;
        try {
            String sql =
                    "SELECT COUNT(*) AS c FROM sqlite_master WHERE type ='table' AND name ='"
                            + table.getTableName() + "' ";
            debugSql(sql);
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    table.setCheckDatabese(true);
                    return true;
                }
            }

        } catch (SQLException e) {
            L.w("--", e);
        } catch (NullPointerException e) {
            L.w("--", e);
        } finally {
            if (cursor != null) cursor.close();
            cursor = null;
        }
        return false;
    }

    public Integer getTableCount(Class<?> clazz) {
        TableInfo tableInfo = TableInfo.get(clazz);
        Cursor cursor = null;
        try {
            String sql = "SELECT COUNT(*) FROM " + tableInfo.getTableName();
            debugSql(sql);
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToNext()) {
                int count = cursor.getInt(0);
                return count;
            }

        } catch (SQLException e) {
            L.w("--", e);
        } catch (NullPointerException e) {
            L.w("--", e);
        } finally {
            if (cursor != null) cursor.close();
            cursor = null;
        }

        return 0;
    }

    public Integer getTableCountWhere(Class<?> clazz, String where) {
        TableInfo tableInfo = TableInfo.get(clazz);
        Cursor cursor = null;
        try {
            String sql = "SELECT COUNT(*) FROM " + tableInfo.getTableName() + " WHERE " + where;
            debugSql(sql);
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToNext()) {
                int count = cursor.getInt(0);
                return count;
            }

        } catch (SQLException e) {
            L.w("--", e);
        } catch (NullPointerException e) {
            L.w("--", e);
        } finally {
            if (cursor != null) cursor.close();
            cursor = null;
        }

        return 0;
    }

    private void debugSql(String sql) {
//        if (sql.contains("discuss_show_data")) {
//            L.e(sql);
//        }
        if (UtilsConstants.DEBUG_DB) {
            L.d("sql = [" + sql + "]");
        }
    }

    public SQLiteDatabase getDb() {
        return db;
    }

}
