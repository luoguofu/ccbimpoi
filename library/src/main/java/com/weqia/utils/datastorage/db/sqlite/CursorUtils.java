package com.weqia.utils.datastorage.db.sqlite;

import android.database.Cursor;

import com.weqia.utils.datastorage.db.table.Property;
import com.weqia.utils.datastorage.db.table.TableInfo;

import java.util.HashMap;
import java.util.Map.Entry;

public class CursorUtils {

    public static <T> T getEntity(Cursor cursor, Class<T> clazz) {
        try {
            if (cursor != null) {
                TableInfo table = TableInfo.get(clazz);
                int columnCount = cursor.getColumnCount();
                if (columnCount > 0) {
                    T entity = (T) clazz.newInstance();
                    for (int i = 0; i < columnCount; i++) {
                        String column = cursor.getColumnName(i);
                        Property property = table.propertyMap.get(column);
                        if (property != null) {
                            if (property.getDataType() == byte[].class)
                                property.setValue(entity, cursor.getBlob(i));
                            else
                                property.setValue(entity, cursor.getString(i));
                        } else {
                            if (table.getId().getColumn().equals(column)) {
                                table.getId().setValue(entity, cursor.getString(i));
                            }
                        }
                    }
                    return entity;
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            // CheckedExceptionHandler.handleException(e);
        }

        return null;
    }

    public static DbModel getDbModel(Cursor cursor) {
        if (cursor != null && cursor.getColumnCount() > 0) {
            DbModel model = new DbModel();
            int columnCount = cursor.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                model.set(cursor.getColumnName(i), cursor.getString(i));
            }
            return model;
        }
        return null;
    }

    public static <T> T dbModel2Entity(DbModel dbModel, Class<?> clazz) {
        if (dbModel != null) {
            HashMap<String, Object> dataMap = dbModel.getDataMap();
            try {
                @SuppressWarnings("unchecked")
                T entity = (T) clazz.newInstance();
                for (Entry<String, Object> entry : dataMap.entrySet()) {
                    String column = entry.getKey();
                    TableInfo table = TableInfo.get(clazz);
                    Property property = table.propertyMap.get(column);
                    if (property != null) {
                        property.setValue(entity, entry.getValue() == null ? null : entry
                                .getValue().toString());
                    } else {
                        if (table.getId().getColumn().equals(column)) {
                            table.getId().setValue(entity,
                                    entry.getValue() == null ? null : entry.getValue().toString());
                        }
                    }

                }
                return entity;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
