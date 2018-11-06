package com.aution.paidd.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.framework.core.common.AppSetting;


/**
 * Created by quxiang on 2017/3/9.
 */

public class SharePreUtils {

    public static void putString(String key,String value){
        getSP().edit().putString(key,value).commit();
    }

    public static String getString(String key){
        return getSP().getString(key,"");
    }

    public static String getString(String key,String defaul){
        return getSP().getString(key,defaul);
    }

    private static SharedPreferences getSP(){
        return AppSetting.getContext().getSharedPreferences("qxhd_aution", Context.MODE_PRIVATE);
    }

}
