package com.framework.core.tools;
import java.util.List;

public class DBTools {

//    private static DbUtils dbUtils;

//    public static DbUtils getDBUtils() {
//        if (dbUtils == null) {
////            dbUtils = DbUtils.create(AppSetting.getContext());
//        }
//        return dbUtils;
//    }

    public static boolean isExitsTable(Class<?> c) {
        long count = 0;
//        try {
//            count = getDBUtils().count(c);
//        } catch (DbException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        return count > 0 ? true : false;
    }

    public static void upData(Object c) {
//        try {
//            getDBUtils().update(c);
//        } catch (DbException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }

    public static void save(Object c) {
//        try {
//            getDBUtils().save(c);
//        } catch (DbException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }

    public static void delect(Class c) {
//        try {
//            getDBUtils().deleteAll(c);
//        } catch (DbException e) {
//
//        }
    }

    public static <T> T getDBObject(Class<T> c) {
        List<T> temp = null;
//        try {
//            temp = DBTools.getDBUtils().findAll(c);
//            if (temp != null && temp.size() > 0) {
//                return temp.get(0);
//            }
//        } catch (DbException e) {
//           return null;
//        }
        return null;
    }

    public static <T> List<T> getAll(Class<T> c) {
        List<T> list = null;
//        try {
//            list = getDBUtils().findAll(c);
//        } catch (DbException e) {
//            return null;
//        }
        return list;
    }

    public static void saveAll(List<?> objects) {
//        try {
//            getDBUtils().saveAll(objects);
//        } catch (DbException e) {
//
//        }

    }

}
