package com.aution.paidd.utils;

import android.text.TextUtils;

/**
 * Created by quxiang on 2016/12/6.
 */
public class FormatUtils {

    public static String formatAccount(String account){
        if (!TextUtils.isEmpty(account)&&account.length()>=11){
            int cutIndex1=account.length()-8;
            int cutIndex2=account.length()-4;
            return account.substring(0,cutIndex1)+"****"+account.substring(cutIndex2);
        }
        return account;
    }

    public static String formatCountDown(long count){
        if (count>=10){
            return "00:00:"+count;
        }else {
            return "00:00:0"+count;
        }
    }

}
