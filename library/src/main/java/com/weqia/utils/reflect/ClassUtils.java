package com.weqia.utils.reflect;

import com.weqia.data.UtilsConstants;
import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;
import com.weqia.utils.datastorage.db.table.ManyToOne;
import com.weqia.utils.datastorage.db.table.OneToMany;
import com.weqia.utils.datastorage.db.table.Property;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ClassUtils {


    /**
     * 根据实体类 获得 实体类对应的表名
     * 
     * @return
     */
    public static String getTableName(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        if (table == null || table.name().trim().length() == 0) {
            // 当没有注解的时候默认用类的名称作为表名,并把点（.）替换为下划线(_)
            return clazz.getName().replace('.', '_');
        }
        return table.name();
    }

    public static Object getPrimaryKeyValue(Object entity) {
        return FieldUtils.getFieldValue(entity, ClassUtils.getPrimaryKeyField(entity.getClass()));
    }

    /**
     * 根据实体类 获得 实体类对应的表名
     * 
     * @return
     */
    @SuppressWarnings("unused")
    private static String getPrimaryKeyColumn(Class<?> clazz) {
        String primaryKey = null;
        Field[] fs = getFields(clazz);
        if (fs != null) {
            Id idAnnotation = null;
            Field idField = null;

            for (Field field : fs) { // 获取ID注解
                idAnnotation = field.getAnnotation(Id.class);
                if (idAnnotation != null) {
                    idField = field;
                    break;
                }
            }

            if (idAnnotation != null) { // 有ID注解
                primaryKey = idAnnotation.column();
                if (primaryKey == null || primaryKey.trim().length() == 0)
                    primaryKey = idField.getName();
            } else { // 没有ID注解,默认去找 _id 和 id 为主键，优先寻找 _id
                for (Field field : fs) {
                    if ("_id".equals(field.getName())) return "_id";
                }

                for (Field field : fs) {
                    if ("id".equals(field.getName())) return "id";
                }
            }
        } else {
            throw new RuntimeException("this model[" + clazz + "] has no field");
        }
        return primaryKey;
    }


    /**
     * 根据实体类 获得 实体类对应的表名
     * 
     * @return
     */
    public static Field getPrimaryKeyField(Class<?> clazz) {
        Field primaryKeyField = null;

        Field[] fs = getFields(clazz);

        for (Field field : fs) { // 获取ID注解
            if (field.getAnnotation(Id.class) != null) {
                primaryKeyField = field;
                break;
            }
        }

        if (primaryKeyField == null) { // 如果没有_id的字段
            for (Field field : fs) {
                if (UtilsConstants.DATA_PRIMARY_KEY.equals(field.getName())) {
                    primaryKeyField = field;
                    break;
                }
            }
        }

        return primaryKeyField;
        // if(primaryKeyField == null){ //没有ID注解
        // for(Field field : fields){
        // if("_id".equals(field.getName())){
        // primaryKeyField = field;
        // break;
        // }
        // }
        // }
        //
        // if(primaryKeyField == null){ // 如果没有_id的字段
        // for(Field field : fields){
        // if("id".equals(field.getName())){
        // primaryKeyField = field;
        // break;
        // }
        // }
        // }
    }


    /**
     * 根据实体类 获得 实体类对应的表名
     * 
     * @return
     */
    public static String getPrimaryKeyFieldName(Class<?> clazz) {
        Field f = getPrimaryKeyField(clazz);
        return f == null ? null : f.getName();
    }



    /**
     * 将对象转换为ContentValues
     * 
     * @return
     */
    public static List<Property> getPropertyList(Class<?> clazz) {

        List<Property> plist = new ArrayList<Property>();
        try {
            Field[] fs = getFields(clazz);

            String primaryKeyFieldName = getPrimaryKeyFieldName(clazz);
            for (Field f : fs) {
                // 必须是基本数据类型和没有标瞬时态的字段
                if (!FieldUtils.isTransient(f)) {
                    if (FieldUtils.isBaseDateType(f)) {

                        if (f.getName().equals(primaryKeyFieldName)) // 过滤主键
                            continue;

                        if (f.getName().equals("serialVersionUID")) {
                            continue;
                        }

                        Property property = new Property();
                        property.setColumn(FieldUtils.getColumnByField(f));
                        property.setFieldName(f.getName());
                        property.setDataType(f.getType());
                        property.setDefaultValue(FieldUtils.getPropertyDefaultValue(f));
                        property.setSet(FieldUtils.getFieldSetMethod(clazz, f));
                        property.setGet(FieldUtils.getFieldGetMethod(clazz, f));
                        property.setField(f);

                        plist.add(property);
                    }
                }
            }
            return plist;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static Field[] getFields(Class<?> clazz) {
        
        Field[] fs = clazz.getDeclaredFields();

        Class<?> superClass = clazz.getSuperclass();
        Field[] superFields = superClass.getDeclaredFields();

        Field[] totalField = new Field[fs.length + superFields.length];
        System.arraycopy(fs, 0, totalField, 0, fs.length);
        System.arraycopy(superFields, 0, totalField, fs.length, superFields.length);
        fs = totalField;
        return fs;
    }

//    public static Method getDeclaredMethod(Object object, String methodName,
//            Class<?>... parameterTypes) {
//        Method method = null;
//
//        for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz =
//                clazz.getSuperclass()) {
//            try {
//                method = clazz.getDeclaredMethod(methodName, parameterTypes);
//                return method;
//            } catch (Exception e) {
//            }
//        }
//
//        return null;
//    }

    /**
     * 将对象转换为ContentValues
     * 
     * @return
     */
    public static List<ManyToOne> getManyToOneList(Class<?> clazz) {

        List<ManyToOne> mList = new ArrayList<ManyToOne>();
        try {
            Field[] fs = getFields(clazz);
            for (Field f : fs) {
                if (!FieldUtils.isTransient(f) && FieldUtils.isManyToOne(f)) {

                    ManyToOne mto = new ManyToOne();
                    mto.setManyClass(f.getType());
                    mto.setColumn(FieldUtils.getColumnByField(f));
                    mto.setFieldName(f.getName());
                    mto.setDataType(f.getType());
                    mto.setSet(FieldUtils.getFieldSetMethod(clazz, f));
                    mto.setGet(FieldUtils.getFieldGetMethod(clazz, f));

                    mList.add(mto);
                }
            }
            return mList;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 将对象转换为ContentValues
     * 
     * @return
     */
    public static List<OneToMany> getOneToManyList(Class<?> clazz) {

        List<OneToMany> oList = new ArrayList<OneToMany>();
        try {
            Field[] fs = getFields(clazz);
            for (Field f : fs) {
                if (!FieldUtils.isTransient(f) && FieldUtils.isOneToMany(f)) {

                    OneToMany otm = new OneToMany();

                    otm.setColumn(FieldUtils.getColumnByField(f));
                    otm.setFieldName(f.getName());

                    Type type = f.getGenericType();

                    if (type instanceof ParameterizedType) {
                        ParameterizedType pType = (ParameterizedType) f.getGenericType();
                        Class<?> pClazz = (Class<?>) pType.getActualTypeArguments()[0];
                        if (pClazz != null) otm.setOneClass(pClazz);
                    } else {
                        throw new IllegalArgumentException("getOneToManyList Exception:"
                                + f.getName() + "'s type is null");
                    }

                    otm.setDataType(f.getClass());
                    otm.setSet(FieldUtils.getFieldSetMethod(clazz, f));
                    otm.setGet(FieldUtils.getFieldGetMethod(clazz, f));

                    oList.add(otm);
                }
            }
            return oList;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


}
