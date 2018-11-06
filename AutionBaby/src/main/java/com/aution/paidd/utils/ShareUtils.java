package com.aution.paidd.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.cocosw.bottomsheet.BottomSheet;
import com.framework.core.common.AppSetting;
import com.framework.core.tools.ACache;
import com.framework.core.utils.LogUtils;
import com.aution.paidd.R;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.common.OnShareCallBack;


import java.io.File;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by quxiang on 2016/12/13.
 */
public class ShareUtils {

    public static final String Share_URL = HttpMethod.BASE_URL+"/link/"+ CacheData.getInstance().getAppid()+"/index.html";

    public static void showAllShare(final Context context, final String title, final String url, final String content, final String imgUrl, final int iconid) {
        new BottomSheet.Builder( context, R.style.BottomSheet_StyleDialog).grid().title("分享到").sheet(R.menu.menu_all_share).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.weixin:
                        shareToWX(context, title, url, content, imgUrl, iconid, null);
                        break;
                    case R.id.wxpyq:
                        shareToWXPYQ(context, title, url, content, imgUrl, iconid, null);
                        break;
                    case R.id.qq:
                        shareToQQ(context, title, url, content, imgUrl, iconid, null);
                        break;
                    case R.id.qqzone:
                        shareToQQZone(context, title, url, content, imgUrl, iconid, null);
                        break;
                }
            }
        }).show();

    }

    public static void showAllShare(final Context context, final String title, final String url, final String content, final String imgUrl, final int iconid, final OnShareCallBack onShareCallBack) {
        new BottomSheet.Builder( context, R.style.BottomSheet_StyleDialog).grid().title("分享到").sheet(R.menu.menu_all_share).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case R.id.weixin:
                        shareToWX(context, title, url, content, imgUrl, iconid, onShareCallBack);
                        break;
                    case R.id.wxpyq:
                        shareToWXPYQ(context, title, url, content, imgUrl, iconid, onShareCallBack);
                        break;
                    case R.id.qq:
                        shareToQQ(context, title, url, content, imgUrl, iconid, onShareCallBack);
                        break;
                    case R.id.qqzone:
                        shareToQQZone(context, title, url, content, imgUrl, iconid, onShareCallBack);
                        break;
                }
            }
        }).show();
    }

    public static void shareToWX(Context context, String title, String url, String content, String imgurl, int iconid, final OnShareCallBack onShareCallBack) {
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setText(content);
        sp.setTitle(TextUtils.isEmpty(title)?"我是一元购的幸运儿，以后请叫我幸运帝！ ":title);
        sp.setUrl(TextUtils.isEmpty(url) ? Share_URL : url);//分享点击都是下载
        sp.setShareType(Platform.SHARE_WEBPAGE);
        if (!TextUtils.isEmpty(imgurl)) {
            sp.setImageUrl(imgurl);
        } else if (iconid != 0) {
            sp.setImageData(BitmapFactory.decodeResource(context.getResources(), iconid));
        }
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                LogUtils.e("分享成功");
                if (onShareCallBack != null) {
                    onShareCallBack.onSuccess();
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                LogUtils.e("");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                LogUtils.e("");
            }
        }); // 设置分享事件回调
        wechat.share(sp);
    }

    public static void shareToWXPYQ(Context context, String title, String url, String content, String imgurl, int iconid, final OnShareCallBack onShareCallBack) {
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setText(content);
        sp.setTitle(TextUtils.isEmpty(title)?"我是一元购的幸运儿，以后请叫我幸运帝！ ":title);
        if (!TextUtils.isEmpty(imgurl)) {
            sp.setImageUrl(imgurl);
        } else if (iconid != 0) {
            sp.setImageData(BitmapFactory.decodeResource(context.getResources(), iconid));
        }
        sp.setUrl(TextUtils.isEmpty(url) ? Share_URL : url);//分享点击都是下载
        sp.setShareType(Platform.SHARE_WEBPAGE);

        Platform wechat = ShareSDK.getPlatform(WechatMoments.NAME);
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                LogUtils.e("分享成功");
                if (onShareCallBack != null) {
                    onShareCallBack.onSuccess();
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                LogUtils.e("");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                LogUtils.e("");
            }
        }); // 设置分享事件回调
        wechat.share(sp);
    }

    public static void shareToQQ(Context context, String title, String url, String content, String imgurl, int iconid, final OnShareCallBack onShareCallBack) {
        QQ.ShareParams sp = new QQ.ShareParams();
        sp.setText(content);
        sp.setTitle(TextUtils.isEmpty(title)?"我是一元购的幸运儿，以后请叫我幸运帝！ ":title);
        sp.setTitleUrl(TextUtils.isEmpty(url) ? Share_URL : url);
        sp.setShareType(Platform.SHARE_WEBPAGE);
        if (!TextUtils.isEmpty(imgurl)) {
            sp.setImageUrl(imgurl);
        } else if (iconid != 0) {
            String resImgPath = ACache.get(AppSetting.getContext()).getAsString(iconid + "");
            if (!TextUtils.isEmpty(resImgPath)) {
                File file = new File(resImgPath);
                if (file != null && file.exists()) {
                    sp.setImagePath(resImgPath);
                } else {
                    String path = context.getCacheDir().getPath() + "/resimg/";
                    String mPath = BitmapUtils.saveBitmapToFile(BitmapFactory.decodeResource(context.getResources(), iconid), path, iconid);
                    ACache.get(AppSetting.getContext()).put(iconid + "", mPath);
                    sp.setImagePath(mPath);
                }
            } else {
                String path = context.getCacheDir().getPath() + "/resimg/";
                String mPath = BitmapUtils.saveBitmapToFile(BitmapFactory.decodeResource(context.getResources(), iconid), path, iconid);
                ACache.get(AppSetting.getContext()).put(iconid + "", mPath);
                sp.imagePath = mPath;
            }
        }
        Platform wechat = ShareSDK.getPlatform(QQ.NAME);
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                LogUtils.e("分享成功");
                if (onShareCallBack != null) {
                    onShareCallBack.onSuccess();
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                LogUtils.e("");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                LogUtils.e("");
            }
        }); // 设置分享事件回调
        wechat.share(sp);
    }

    public static void shareToQQZone(Context context, String title, String url, String content, String imgurl, int iconid, final OnShareCallBack onShareCallBack) {

        QZone.ShareParams sp = new QZone.ShareParams();
        sp.setText(content);
        sp.setTitle(TextUtils.isEmpty(title)?"我是一元购的幸运儿，以后请叫我幸运帝！":title);
        sp.setTitleUrl(TextUtils.isEmpty(url) ? Share_URL : url);
        sp.setShareType(Platform.SHARE_WEBPAGE);
        if (!TextUtils.isEmpty(imgurl)) {
            sp.setImageUrl(imgurl);
        } else if (iconid != 0) {
            String resImgPath = ACache.get(AppSetting.getContext()).getAsString(iconid + "");
            if (!TextUtils.isEmpty(resImgPath)) {
                File file = new File(resImgPath);
                if (file != null && file.exists()) {
                    sp.imagePath = resImgPath;
                } else {
                    String path = context.getCacheDir().getPath() + "/resimg/";
                    String mPath = BitmapUtils.saveBitmapToFile(BitmapFactory.decodeResource(context.getResources(), iconid), path, iconid);
                    ACache.get(AppSetting.getContext()).put(iconid + "", mPath);
                    sp.imagePath = mPath;
                }
            } else {
                String path = context.getCacheDir().getPath() + "/resimg/";
                String mPath = BitmapUtils.saveBitmapToFile(BitmapFactory.decodeResource(context.getResources(), iconid), path, iconid);
                ACache.get(AppSetting.getContext()).put(iconid + "", mPath);
                sp.imagePath = mPath;
            }
        }
        Platform wechat = ShareSDK.getPlatform(QZone.NAME);
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                LogUtils.e("分享成功");
                if (onShareCallBack != null) {
                    onShareCallBack.onSuccess();
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                LogUtils.e("");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                LogUtils.e("");
            }
        }); // 设置分享事件回调
        wechat.share(sp);
    }

}
