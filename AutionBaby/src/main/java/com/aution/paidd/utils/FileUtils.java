package com.aution.paidd.utils;

import android.content.Context;
import android.os.Environment;


import com.framework.core.common.AppSetting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yangjie on 2016/10/12.
 */
public class FileUtils {

    public static final String ROOT_DIR = "Android/data/"
            + AppSetting.getContext().getPackageName();

    /**
     * 判断SD卡是否挂载
     */
    public static boolean isSDCardAvailable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获取应用目录，当SD卡存在时，获取SD卡上的目录，当SD卡不存在时，获取应用的cache目录
     */
    public static String getDir(String name) {
        StringBuilder sb = new StringBuilder();
        if (isSDCardAvailable()) {
            sb.append(getExternalStoragePath());
        } else {
            sb.append(getCachePath());
        }
        sb.append(name);
        sb.append(File.separator);
        String path = sb.toString();
        if (createDirs(path)) {
            return path;
        } else {
            return null;
        }
    }

    /**
     * 获取SD下的应用目录
     */
    public static String getExternalStoragePath() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append(File.separator);
        sb.append(ROOT_DIR);
        sb.append(File.separator);
        return sb.toString();
    }

    /**
     * 获取应用的cache目录
     */
    public static String getCachePath() {
        File f = AppSetting.getContext().getCacheDir();
        if (null == f) {
            return null;
        } else {
            return f.getAbsolutePath() + "/";
        }
    }


    /**
     * 创建文件夹
     */
    public static boolean createDirs(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdirs();
        }
        return true;
    }


    /**
     * 关闭流
     */
    public static boolean close(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
            }
        }
        return true;
    }


    public static String getDataFromLocal() {

        try {
            File cacheFile = getCacheFile();
            if (cacheFile.exists()) {
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(cacheFile));
                    String timeStr = reader.readLine();
                    return timeStr;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    close(reader);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


    public static File getCacheFile() {
        String dir = FileUtils.getDir("imei");// sdcard/Android/data/包名/json
        File cacheFile = new File(dir, ".txt");
        return cacheFile;
    }

    public static void deleteFile() {
        File cacheFile = getCacheFile();
        if (cacheFile.exists()) {
            cacheFile.delete();
        }

    }

    public static void putDataToSd(String imei) {
        File cacheFile = getCacheFile();
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(cacheFile));
            writer.write(imei);
        } catch (Exception e) {

        } finally {
            close(writer);
        }


    }


    public static String getFromAssets(String fileName, Context mcontext) {
        String result = "";
        try {
            InputStream is = mcontext.getResources().getAssets().open(fileName);
            int lenght = is.available();
            byte[] buffer = new byte[lenght];
            is.read(buffer);
            result = new String(buffer, "utf8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}