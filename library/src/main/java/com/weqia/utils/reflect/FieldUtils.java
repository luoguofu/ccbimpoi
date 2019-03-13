package com.weqia.utils.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

import com.weqia.utils.StrUtil;
import com.weqia.utils.TimeUtils;
import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.ManyToOne;
import com.weqia.utils.annotation.sqlite.OneToMany;
import com.weqia.utils.annotation.sqlite.Property;
import com.weqia.utils.annotation.sqlite.Transient;

@SuppressLint({"DefaultLocale", "SimpleDateFormat"})
public class FieldUtils {

    public static Method getFieldGetMethod(Class<?> clazz, Field f) {
        String fn = f.getName();
        Method m = null;
        if (f.getType() == boolean.class) {
            m = getBooleanFieldGetMethod(clazz, fn);
        }
        if (m == null) {
            m = getFieldGetMethod(clazz, fn);
        }
        return m;
    }

    private static Method getBooleanFieldGetMethod(Class<?> clazz, String fieldName) {
        String mn = parIsName(fieldName);
        try {
            return clazz.getDeclaredMethod(mn);
        } catch (NoSuchMethodException e) {
            try {
                Class<?> superClazz = clazz.getSuperclass();
                return superClazz.getDeclaredMethod(mn);
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    private static Method getBooleanFieldSetMethod(Class<?> clazz, Field f) {

        String mn = parSetName(f.getName());
        // "set" + fn.substring(0, 1).toUpperCase() + fn.substring(1);

        // if (isISStart(f.getName())) {
        // mn = "set" + fn.substring(2, 3).toUpperCase() + fn.substring(3);
        // }
        try {
            return clazz.getDeclaredMethod(mn, f.getType());
        } catch (NoSuchMethodException e) {
            try {
                Class<?> superClazz = clazz.getSuperclass();
                return superClazz.getDeclaredMethod(mn, f.getType());
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    // private static boolean isISStart(String fieldName) {
    // if (fieldName == null || fieldName.trim().length() == 0) return false;
    // // is开头，并且is之后第一个字母是大写 比如 isAdmin
    // return fieldName.startsWith("is") &&
    // !Character.isLowerCase(fieldName.charAt(2));
    // }

    private static Method getFieldGetMethod(Class<?> clazz, String fieldName) {
        String mn = parGetName(fieldName);
        // "get" + fieldName.substring(0, 1).toUpperCase() +
        // fieldName.substring(1);
        try {
            return clazz.getDeclaredMethod(mn);
        } catch (NoSuchMethodException e) {
            try {
                Class<?> superClazz = clazz.getSuperclass();
                return superClazz.getDeclaredMethod(mn);
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    public static Method getFieldSetMethod(Class<?> clazz, Field f) {
        String mn = parSetName(f.getName());
        // "set" + fn.substring(0, 1).toUpperCase() + fn.substring(1);
        try {
            return clazz.getDeclaredMethod(mn, f.getType());
        } catch (NoSuchMethodException e) {
            if (f.getType() == boolean.class) {
                return getBooleanFieldSetMethod(clazz, f);
            }
            try {
                Class<?> superClazz = clazz.getSuperclass();
                return superClazz.getDeclaredMethod(mn, f.getType());
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }

    // private static Method getFieldSetMethod(Class<?> clazz, String fieldName)
    // {
    // try {
    // String mn = parSetName(fieldName);
    // return getFieldSetMethod(clazz, clazz.getDeclaredField(mn));
    // } catch (SecurityException e) {
    // CheckedExceptionHandler.handleException(e);
    // } catch (NoSuchFieldException e) {
    // CheckedExceptionHandler.handleException(e);
    // }
    // return null;
    // }

    /**
     * 获取某个字段的值
     * 
     * @param entity
     * @return
     */
    public static Object getFieldValue(Object entity, Field field) {
        Method method = getFieldGetMethod(entity.getClass(), field);
        return invoke(entity, method);
    }

    // /**
    // * 获取某个字段的值
    // *
    // * @param entity
    // * @param fieldName
    // * @return
    // */
    // private static Object getFieldValue(Object entity, String fieldName) {
    // Method method = getFieldGetMethod(entity.getClass(), fieldName);
    // return invoke(entity, method);
    // }

    /**
     * 设置某个字段的值
     * 
     * @param entity
     * @return
     */
    public static void setFieldValue(Object entity, Field field, Object value) {
        try {
            Method set = getFieldSetMethod(entity.getClass(), field);
            if (set != null) {
                set.setAccessible(true);
                Class<?> type = field.getType();
                if (type == String.class) {
                    set.invoke(entity, value.toString());
                } else if (type == int.class || type == Integer.class) {
                    set.invoke(entity,
                            value == null ? (Integer) null : Integer.parseInt(value.toString()));
                } else if (type == float.class || type == Float.class) {
                    set.invoke(entity,
                            value == null ? (Float) null : Float.parseFloat(value.toString()));
                } else if (type == long.class || type == Long.class) {
                    set.invoke(entity,
                            value == null ? (Long) null : Long.parseLong(value.toString()));
                } else if (type == Date.class) {
                    set.invoke(entity,
                            value == null ? (Date) null : stringToDateTime(value.toString()));
                } else {
                    set.invoke(entity, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // /**
    // * 获取某个字段的值
    // *
    // * @param entity
    // * @param fieldName
    // * @return
    // */
    // private static Field getFieldByColumnName(Class<?> clazz, String
    // columnName) {
    // Field field = null;
    // if (columnName != null) {
    // Field[] fields = clazz.getDeclaredFields();
    // if (fields != null && fields.length > 0) {
    // if (columnName.equals(ClassUtils.getPrimaryKeyColumn(clazz)))
    // field = ClassUtils.getPrimaryKeyField(clazz);
    //
    // if (field == null) {
    // for (Field f : fields) {
    // Property property = f.getAnnotation(Property.class);
    // if (property != null && columnName.equals(property.column())) {
    // field = f;
    // break;
    // }
    //
    // ManyToOne manyToOne = f.getAnnotation(ManyToOne.class);
    // if (manyToOne != null && manyToOne.column().trim().length() != 0) {
    // field = f;
    // break;
    // }
    // }
    // }
    //
    // if (field == null) {
    // field = getFieldByName(clazz, columnName);
    // }
    // }
    // }
    // return field;
    // }

    // /**
    // * 获取某个字段的值
    // *
    // * @param entity
    // * @param fieldName
    // * @return
    // */
    // private static Field getFieldByName(Class<?> clazz, String fieldName) {
    // Field field = null;
    // if (fieldName != null) {
    // try {
    // field = clazz.getDeclaredField(fieldName);
    // } catch (SecurityException e) {
    // CheckedExceptionHandler.handleException(e);
    // } catch (NoSuchFieldException e) {
    // CheckedExceptionHandler.handleException(e);
    // }
    // }
    // return field;
    // }

    /**
     * 获取某个熟悉对应的 表的列
     * 
     * @return
     */
    public static String getColumnByField(Field field) {
        Property property = field.getAnnotation(Property.class);
        if (property != null && property.column().trim().length() != 0) {
            return property.column();
        }

        ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
        if (manyToOne != null && manyToOne.column().trim().length() != 0) {
            return manyToOne.column();
        }

        OneToMany oneToMany = field.getAnnotation(OneToMany.class);
        if (oneToMany != null && oneToMany.manyColumn() != null
                && oneToMany.manyColumn().trim().length() != 0) {
            return oneToMany.manyColumn();
        }

        Id id = field.getAnnotation(Id.class);
        if (id != null && id.column().trim().length() != 0) return id.column();

        return field.getName();
    }

    public static String getPropertyDefaultValue(Field field) {
        Property property = field.getAnnotation(Property.class);
        if (property != null && property.defaultValue().trim().length() != 0) {
            return property.defaultValue();
        }
        return null;
    }

    /**
     * 检测 字段是否已经被标注为 非数据库字段
     * 
     * @param f
     * @return
     */
    public static boolean isTransient(Field f) {
        return f.getAnnotation(Transient.class) != null;
    }

    /**
     * 获取某个实体执行某个方法的结果
     * 
     * @param obj
     * @param method
     * @return
     */
    private static Object invoke(Object obj, Method method) {
        if (obj == null || method == null) return null;
        try {
            return method.invoke(obj);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isManyToOne(Field field) {
        return field.getAnnotation(ManyToOne.class) != null;
    }

    public static boolean isOneToMany(Field field) {
        return field.getAnnotation(OneToMany.class) != null;
    }

    public static boolean isManyToOneOrOneToMany(Field field) {
        return isManyToOne(field) || isOneToMany(field);
    }

    public static boolean isBaseDateType(Field field) {
        Class<?> clazz = field.getType();
        return clazz.equals(String.class) || clazz.equals(Integer.class)
                || clazz.equals(Byte.class) || clazz.equals(Long.class)
                || clazz.equals(Double.class) || clazz.equals(Float.class)
                || clazz.equals(Character.class) || clazz.equals(Short.class)
                || clazz.equals(Boolean.class) || clazz.equals(Date.class)
                || clazz.equals(java.util.Date.class) || clazz.equals(java.sql.Date.class)
                || clazz.equals(byte[].class)
                || clazz.isPrimitive();
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static Date stringToDateTime(String strDate) {
        if (strDate != null) {
            try {
                return sdf.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 拼接某属性的 get方法
     * 
     * @param fieldName
     * @return String
     */
    public static String parGetName(String fieldName) {
        if (StrUtil.isEmptyOrNull(fieldName)) {
            return null;
        }
        return createMethod(fieldName, "get");
    }

    /**
     * 拼接某属性的 is方法
     * 
     * @param fieldName
     * @return String
     */
    public static String parIsName(String fieldName) {
        if (StrUtil.isEmptyOrNull(fieldName)) {
            return null;
        }
        return createMethod(fieldName, "is");
    }

    /**
     * 拼接在某属性的 set方法
     * 
     * @param fieldName
     * @return String
     */
    public static String parSetName(String fieldName) {
        if (StrUtil.isEmptyOrNull(fieldName)) {
            return null;
        }
        return createMethod(fieldName, "set");
    }

    private static String createMethod(String fieldName, String prefix) {
        if (fieldName.length() == 1) {
            return prefix + fieldName.toUpperCase(TimeUtils.getDefaultLocale());
        }

        char firstChar = fieldName.charAt(0);
        char secondChar = fieldName.charAt(1);

        if (StrUtil.isLowerChar(firstChar) && StrUtil.isUpperChar(secondChar)) {
            return prefix + fieldName;
        }

        if (StrUtil.isUpperChar(firstChar)) {
            return prefix + fieldName;
        }

        if (StrUtil.isLowerChar(firstChar) && StrUtil.isLowerChar(secondChar)) {
            return prefix + fieldName.substring(0, 1).toUpperCase(TimeUtils.getDefaultLocale())
                    + fieldName.substring(1);
        }

        return prefix + fieldName;
    }
}
