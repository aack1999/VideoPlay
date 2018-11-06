package com.framework.core.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.text.TextUtils;

import com.framework.core.tools.ACache;

/**
 * Created by Administrator on 2016/1/1 0001.
 */
public class AppSetting {

    private static AppSetting  appSetting;

    public static AppSetting getInstance(){
        return appSetting==null?appSetting=new AppSetting():appSetting;
    }

    private static  Context context;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        AppSetting.context = context;
        String temp= ACache.get(context).getAsString("isOnPush");
        if (TextUtils.isEmpty(temp)){
            getInstance().setOnPush(true);
        }
    }

    public int TimeOut=30*1000;


    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return  version;
        } catch (Exception e) {
            return "";
        }
    }

    //获取系统默认铃声的Uri
    public Uri getSystemDefultRingtoneUri(Context context) {
        return RingtoneManager.getActualDefaultRingtoneUri(context,
                RingtoneManager.TYPE_ALARM);
    }

    public String getApplicationMetaData(String key){
        ApplicationInfo appInfo = null;
        try {
            appInfo = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA);
            String msg=appInfo.metaData.getString(key);
            return msg;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "http://app.quxiang365.com/";
    }

    public boolean isOnPush() {
        String temp= ACache.get(getContext()).getAsString("isOnPush");
        if (!TextUtils.isEmpty(temp)&&"true".equals(temp)){
            return true;
        }
        return false;
    }

    public void setOnPush(boolean onPush) {
        ACache.get(getContext()).put("isOnPush",onPush+"");
    }

}
