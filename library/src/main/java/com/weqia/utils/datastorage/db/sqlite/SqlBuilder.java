package com.weqia.utils.datastorage.db.sqlite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.weqia.utils.datastorage.db.table.Id;
import com.weqia.utils.datastorage.db.table.KeyValue;
import com.weqia.utils.datastorage.db.table.ManyToOne;
import com.weqia.utils.datastorage.db.table.Property;
import com.weqia.utils.datastorage.db.table.TableInfo;

import android.text.TextUtils;

public class SqlBuilder {

    /**
     * 获取插入的sql语句
     *
     * @return
     */
    public static SqlInfo buildInsertSql(Object entity) {
        List<KeyValue> keyValueList = getSaveKeyValueListByEntity(entity);
        StringBuffer strSQL = new StringBuffer();
        SqlInfo sqlInfo = null;
        if (keyValueList != null && keyValueList.size() > 0) {

            sqlInfo = new SqlInfo();

            strSQL.append("INSERT INTO ");
            strSQL.append(TableInfo.get(entity.getClass()).getTableName());
            strSQL.append(" (");
            for (KeyValue kv : keyValueList) {
                strSQL.append(kv.getKey()).append(",");
                sqlInfo.addValue(kv.getValue());
            }
            strSQL.deleteCharAt(strSQL.length() - 1);
            strSQL.append(") VALUES ( ");

            int length = keyValueList.size();
            for (int i = 0; i < length; i++) {
                strSQL.append("?,");
            }
            strSQL.deleteCharAt(strSQL.length() - 1);
            strSQL.append(")");
            sqlInfo.setSql(strSQL.toString());
        }

        return sqlInfo;
    }

    public static List<KeyValue> getSaveKeyValueListByEntity(Object entity) {

        List<KeyValue> keyValueList = new ArrayList<KeyValue>();

        TableInfo table = TableInfo.get(entity.getClass());
        Object idvalue = table.getId().getValue(entity);

        if (!(idvalue instanceof Integer)) { // 用了非自增长,添加id , 采用自增长就不需要添加id了
            if (idvalue instanceof String && idvalue != null) {
                KeyValue kv = new KeyValue(table.getId().getColumn(), idvalue);
                keyValueList.add(kv);
            }
        }

        // 添加属性
        Collection<Property> propertys = table.propertyMap.values();
        for (Property property : propertys) {
            KeyValue kv = property2KeyValue(property, entity);
            if (kv != null)
                keyValueList.add(kv);
        }

        // 添加外键（多对一）
        Collection<ManyToOne> manyToOnes = table.manyToOneMap.values();
        for (ManyToOne many : manyToOnes) {
            KeyValue kv = manyToOne2KeyValue(many, entity);
            if (kv != null)
                keyValueList.add(kv);
        }

        return keyValueList;
    }

    private static String getUpdateReqBytableName(String tableName) {
        return "UPDATE sqlite_sequence SET seq = 0 where name = " + tableName;
    }

    public static SqlInfo buildUpdateReqSql(Class<?> clazz) {
        TableInfo table = TableInfo.get(clazz);

        StringBuffer strSQL = new StringBuffer(
                getUpdateReqBytableName(table.getTableName()));

        SqlInfo sqlInfo = new SqlInfo();
        sqlInfo.setSql(strSQL.toString());

        return sqlInfo;
    }

    private static String getDeleteSqlBytableName(String tableName) {
        return "DELETE FROM " + tableName;
    }

    public static String getClearSql(Class<?> clazz) {
        TableInfo table = TableInfo.get(clazz);
        return "DELETE FROM " + table.getTableName();
    }

    // public static SqlInfo buildClearSql(Class<?> clazz) {
    // TableInfo table = TableInfo.get(clazz);
    //
    // StringBuffer strSQL = new
    // StringBuffer(getDeleteSqlBytableName(table.getTableName()));
    //
    // SqlInfo sqlInfo = new SqlInfo();
    // sqlInfo.setSql(strSQL.toString());
    //
    // return sqlInfo;
    // }

    public static SqlInfo buildDeleteSql(Object entity) {
        TableInfo table = TableInfo.get(entity.getClass());

        Id id = table.getId();
        Object idvalue = id.getValue(entity);

        if (idvalue == null) {
            throw new IllegalArgumentException("getDeleteSQL:"
                    + entity.getClass() + " id value is null");
        }
        StringBuffer strSQL = new StringBuffer(
                getDeleteSqlBytableName(table.getTableName()));
        strSQL.append(" WHERE ").append(id.getColumn()).append("=?");

        SqlInfo sqlInfo = new SqlInfo();
        sqlInfo.setSql(strSQL.toString());
        sqlInfo.addValue(idvalue);

        return sqlInfo;
    }

    public static SqlInfo buildDeleteSql(Class<?> clazz, Object idValue) {
        TableInfo table = TableInfo.get(clazz);
        Id id = table.getId();

        if (null == idValue) {
            throw new IllegalArgumentException("getDeleteSQL:idValue is null");
        }

        StringBuffer strSQL = new StringBuffer(
                getDeleteSqlBytableName(table.getTableName()));
        strSQL.append(" WHERE ").append(id.getColumn()).append("=?");

        SqlInfo sqlInfo = new SqlInfo();
        sqlInfo.setSql(strSQL.toString());
        sqlInfo.addValue(idValue);

        return sqlInfo;
    }

    /**
     * 根据条件删除数据 ，条件为空的时候将会删除所有的数据
     *
     * @param clazz
     * @param strWhere
     * @return
     */
    public static String buildDeleteSql(Class<?> clazz, String strWhere) {
        TableInfo table = TableInfo.get(clazz);
        StringBuffer strSQL = new StringBuffer(
                getDeleteSqlBytableName(table.getTableName()));

        if (!TextUtils.isEmpty(strWhere)) {
            strSQL.append(" WHERE ");
            strSQL.append(strWhere);
        }

        return strSQL.toString();
    }

    // //////////////////////////select sql
    // start///////////////////////////////////////

    // private static String getSelectSqlByTableName(String tableName) {
    // return new StringBuffer("SELECT * FROM ").append(tableName)
    // .append(" WHERE gCoId = '").append(UtilApplication.getgMCoId())
    // .append("'").toString();
    // }

    private static String getSelectSqlByTableName(String tableName) {
        return new StringBuffer("SELECT * FROM ").append(tableName).toString();
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
    public static String getSelectSQLLimit(Class<?> clazz, Integer startId,
                                           Integer size) {
        TableInfo table = TableInfo.get(clazz);
        StringBuffer strSQL = new StringBuffer(
                getSelectSqlByTableName(table.getTableName()));
        strSQL.append(getLimit(startId, size));
        return strSQL.toString();
    }

    public static String getSelectSQL(Class<?> clazz, Object idValue) {
        TableInfo table = TableInfo.get(clazz);

        StringBuffer strSQL = new StringBuffer(
                getSelectSqlByTableName(table.getTableName()));
        strSQL.append(" WHERE ");
        strSQL.append(getPropertyStrSql(table.getId().getColumn(), idValue));

        return strSQL.toString();
    }

    public static SqlInfo getSelectSqlAsSqlInfo(Class<?> clazz, Object idValue) {
        TableInfo table = TableInfo.get(clazz);

        StringBuffer strSQL = new StringBuffer(
                getSelectSqlByTableName(table.getTableName()));
        strSQL.append(" WHERE ").append(table.getId().getColumn()).append("=?");

        SqlInfo sqlInfo = new SqlInfo();
        sqlInfo.setSql(strSQL.toString());
        sqlInfo.addValue(idValue);

        return sqlInfo;
    }

    /**
     * ID查询
     *
     * @param clazz
     * @param idValue
     * @return
     * @Description
     * @author Dminter
     */
    public static SqlInfo getSelectSqlAsSqlInfoByRealId(Class<?> clazz,
                                                        Object idValue) {
        TableInfo table = TableInfo.get(clazz);

        StringBuffer strSQL = new StringBuffer(
                getSelectSqlByTableName(table.getTableName()));
        strSQL.append(" WHERE ").append("real_id").append("=?");

        SqlInfo sqlInfo = new SqlInfo();
        sqlInfo.setSql(strSQL.toString());
        sqlInfo.addValue(idValue);

        return sqlInfo;
    }

    public static String getSelectSQL(Class<?> clazz) {
        return getSelectSqlByTableName(TableInfo.get(clazz).getTableName());
    }

    public static String getSelectSQLByWhere(Class<?> clazz, String strWhere) {
        TableInfo table = TableInfo.get(clazz);

        StringBuffer strSQL = new StringBuffer(
                getSelectSqlByTableName(table.getTableName()));

        if (!TextUtils.isEmpty(strWhere)) {
            strSQL.append(" WHERE ").append(strWhere);
        }

        return strSQL.toString();
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
    public static String getSelectSQLByWhereLimit(Class<?> clazz,
                                                  String strWhere, Integer startId, Integer size) {
        TableInfo table = TableInfo.get(clazz);
        StringBuffer strSQL = new StringBuffer(
                getSelectSqlByTableName(table.getTableName()));
        if (!TextUtils.isEmpty(strWhere)) {
            strSQL.append(" WHERE ").append(strWhere);
        }

        strSQL.append(getLimit(startId, size));
        return strSQL.toString();
    }

    public static String getLimit(Integer startId, Integer size) {
        StringBuffer strSQL = new StringBuffer();
        strSQL.append(" limit ");
        strSQL.append(startId);
        strSQL.append(",");
        strSQL.append(size);
        return strSQL.toString();
    }

    // ////////////////////////////update sql
    // start/////////////////////////////////////////////

    public static SqlInfo getUpdateSqlAsSqlInfo(Object entity) {

        TableInfo table = TableInfo.get(entity.getClass());
        Object idvalue = table.getId().getValue(entity);

        if (null == idvalue) {// 主键值不能为null，否则不能更新
            throw new IllegalArgumentException("this entity["
                    + entity.getClass() + "]'s id value is null");
        }

        List<KeyValue> keyValueList = new ArrayList<KeyValue>();
        // 添加属性
        Collection<Property> propertys = table.propertyMap.values();
        for (Property property : propertys) {
            KeyValue kv = property2KeyValue(property, entity);
            if (kv != null)
                keyValueList.add(kv);
        }

        // 添加外键（多对一）
        Collection<ManyToOne> manyToOnes = table.manyToOneMap.values();
        for (ManyToOne many : manyToOnes) {
            KeyValue kv = manyToOne2KeyValue(many, entity);
            if (kv != null)
                keyValueList.add(kv);
        }

        if (keyValueList == null || keyValueList.size() == 0)
            return null;

        SqlInfo sqlInfo = new SqlInfo();
        StringBuffer strSQL = new StringBuffer("UPDATE ");
        strSQL.append(table.getTableName());
        strSQL.append(" SET ");
        for (KeyValue kv : keyValueList) {
            strSQL.append(kv.getKey()).append("=?,");
            sqlInfo.addValue(kv.getValue());
        }
        strSQL.deleteCharAt(strSQL.length() - 1);
        strSQL.append(" WHERE ").append(table.getId().getColumn()).append("=?");
        sqlInfo.addValue(idvalue);
        sqlInfo.setSql(strSQL.toString());
        return sqlInfo;
    }

    public static SqlInfo getUpdateSqlAsSqlInfo(Object entity, String strWhere) {

        TableInfo table = TableInfo.get(entity.getClass());

        List<KeyValue> keyValueList = new ArrayList<KeyValue>();

        // 添加属性
        Collection<Property> propertys = table.propertyMap.values();
        for (Property property : propertys) {
            KeyValue kv = property2KeyValue(property, entity);
            if (kv != null)
                keyValueList.add(kv);
        }

        // 添加外键（多对一）
        Collection<ManyToOne> manyToOnes = table.manyToOneMap.values();
        for (ManyToOne many : manyToOnes) {
            KeyValue kv = manyToOne2KeyValue(many, entity);
            if (kv != null)
                keyValueList.add(kv);
        }

        if (keyValueList == null || keyValueList.size() == 0) {
            throw new IllegalArgumentException("this entity["
                    + entity.getClass() + "] has no property");
        }

        SqlInfo sqlInfo = new SqlInfo();
        StringBuffer strSQL = new StringBuffer("UPDATE ");
        strSQL.append(table.getTableName());
        strSQL.append(" SET ");
        for (KeyValue kv : keyValueList) {
            strSQL.append(kv.getKey()).append("=?,");
            sqlInfo.addValue(kv.getValue());
        }
        strSQL.deleteCharAt(strSQL.length() - 1);
        if (!TextUtils.isEmpty(strWhere)) {
            strSQL.append(" WHERE ").append(strWhere);
        }
        sqlInfo.setSql(strSQL.toString());
        return sqlInfo;
    }

    public static String getCreatTableSQL(Class<?> clazz) {
        TableInfo table = TableInfo.get(clazz);
        Id id = table.getId();
        StringBuffer strSQL = new StringBuffer();
        strSQL.append("CREATE TABLE IF NOT EXISTS ");
        strSQL.append(table.getTableName());
        strSQL.append(" ( ");

        Class<?> primaryClazz = id.getDataType();
        if (primaryClazz == int.class || primaryClazz == Integer.class)
            strSQL.append("\"").append(id.getColumn()).append("\"    ")
                    .append("INTEGER PRIMARY KEY AUTOINCREMENT,");
        else
            strSQL.append("\"").append(id.getColumn()).append("\"    ")
                    .append("TEXT PRIMARY KEY,");

        Collection<Property> propertys = table.propertyMap.values();
        for (Property property : propertys) {
            if (property.getDataType() == byte[].class){
                strSQL.append("\"").append(property.getColumn());
                strSQL.append("\" blob,");
            } else {
                strSQL.append("\"").append(property.getColumn());
                strSQL.append("\",");
            }
        }

        Collection<ManyToOne> manyToOnes = table.manyToOneMap.values();
        for (ManyToOne manyToOne : manyToOnes) {
            strSQL.append("\"").append(manyToOne.getColumn()).append("\",");
        }
        strSQL.deleteCharAt(strSQL.length() - 1);
        strSQL.append(" )");
        return strSQL.toString();
    }

    /**
     * @param key
     * @param value
     */
    private static String getPropertyStrSql(String key, Object value) {
        StringBuffer sbSQL = new StringBuffer(key).append("=");
        if (value instanceof String || value instanceof java.util.Date
                || value instanceof java.sql.Date) {
            sbSQL.append("'").append(value).append("'");
        } else {
            sbSQL.append(value);
        }
        return sbSQL.toString();
    }

    private static KeyValue property2KeyValue(Property property, Object entity) {
        KeyValue kv = null;
        String pcolumn = property.getColumn();
        Object value = property.getValue(entity);
        if (value != null) {
            kv = new KeyValue(pcolumn, value);
        } else {
            value = new Object();
//			if (property.getDefaultValue() != null
//					&& property.getDefaultValue().trim().length() != 0)
            kv = new KeyValue(pcolumn, null);
        }
        return kv;
    }

    private static KeyValue manyToOne2KeyValue(ManyToOne many, Object entity) {
        KeyValue kv = null;
        String manycolumn = many.getColumn();
        Object manyobject = many.getValue(entity);
        if (manyobject != null) {
            Object manyvalue = TableInfo.get(manyobject.getClass()).getId()
                    .getValue(manyobject);
            if (manycolumn != null && manyvalue != null) {
                kv = new KeyValue(manycolumn, manyvalue);
            }
        }

        return kv;
    }

}
