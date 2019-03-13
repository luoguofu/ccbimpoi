package com.weqia.utils;


public class MResource {

    // 利用反射机制访问资源
    public static int getIdByName(String packageName, String className, String name) {
        // String packageName = context.getPackageName();//
        Class<?> r = null;
        int id = 0;
        try {
            r = Class.forName(packageName + ".R");// 得到测试类的R类
            @SuppressWarnings("rawtypes")
            Class[] classes = r.getClasses();
            Class<?> desireClass = null;

            for (int i = 0; i < classes.length; ++i) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    // 得到资源 className=tip_no_network定义的 “网络连接不可用，请稍后重试”
                    desireClass = classes[i];
                    break;
                }
            }

            if (desireClass != null) id = desireClass.getField(name).getInt(desireClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return id;
    }

    public static int[] getIdsByName(String packageName, String className, String name) {
        // String packageName = context.getPackageName();
        Class<?> r = null;
        int[] ids = null;
        try {
            r = Class.forName(packageName + ".R");

            @SuppressWarnings("rawtypes")
            Class[] classes = r.getClasses();
            Class<?> desireClass = null;

            for (int i = 0; i < classes.length; ++i) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];
                    break;
                }
            }

            if ((desireClass != null) && (desireClass.getField(name).get(desireClass) != null)
                    && (desireClass.getField(name).get(desireClass).getClass().isArray()))
                ids = (int[]) desireClass.getField(name).get(desireClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return ids;
    }
}
