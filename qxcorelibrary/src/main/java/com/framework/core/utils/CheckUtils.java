package com.framework.core.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;


public class CheckUtils {

	public static boolean checkSinaSSO(Context context) {
		return checkApkExist(context,"com.sina.weibo");
	}

	public static boolean checkQQSSO(Context context) {
		return checkApkExist(context, "com.tencent.mobileqq");
	}

	public static boolean checkWXSSO(Context context) {
		return checkApkExist(context, "com.tencent.mm");
	}
	
	public static boolean checkApkExist(Context context, String packageName) {
		if (TextUtils.isEmpty(packageName)) {
			return false;
		}
		try {
			context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

    /**
     * 验证邮箱
     * @param email
     * @return
     */
    public static boolean checkEmail(String email){
        boolean flag = false;
        try{
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }

	/**
	 * 判断网络是否连通
	 * @param context
	 * @return
	 */
	public static boolean checkNetWork(Context context){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return info != null && info.isConnected();
	}

	public static boolean isExitQQ(){
		Platform qq = ShareSDK.getPlatform (QQ.NAME);
		return  qq.isClientValid();
	}

	public static boolean isExitWeChat(){
		Platform wechat = ShareSDK.getPlatform (Wechat.NAME);
		return  wechat.isClientValid();
	}

	/**
	 * 是否存在支付宝
	 * @param context
	 * @return
	 */
	public static boolean isExitAlipay(Context context) {
		Uri uri = Uri.parse("alipays://platformapi/startApp");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		ComponentName componentName = intent.resolveActivity(context.getPackageManager());
		return componentName != null;
	}

}
