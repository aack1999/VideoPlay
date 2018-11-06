package com.aution.paidd.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.framework.core.common.WaitDialog;
import com.aution.paidd.bean.UserBean;
import com.aution.paidd.common.CacheData;


import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by quxiang on 2016/12/7.
 */
public class ThirdLoginUtils {


    WaitDialog dialog;

    Context context;

    public ThirdLoginUtils(Context context) {
        this.context = context;
        dialog = new WaitDialog(context);
        dialog.setContent("正在登录...");
    }

    public void loginToWX(final OnLoginListener listener) {
        dialog.show();
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                dialog.dismiss();
                if (msg.what == 1)
                    listener.onSuccess((UserBean) msg.obj);
            }
        };
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        if (wechat.isAuthValid()) {
            wechat.removeAccount(true);
        }
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                //用户资源都保存到res
//通过打印res数据看看有哪些数据是你想要的
                if (i == Platform.ACTION_USER_INFOR) {
                    PlatformDb platDB = platform.getDb();//获取数平台数据DB
                    //通过DB获取各种数据
                    platDB.getToken();
                    platDB.getUserGender();
                    platDB.getUserIcon();
                    platDB.getUserId();
                    platDB.getUserName();
                    UserBean user = CacheData.getInstance().getUserData();
                    if (user == null) {
                        user = new UserBean();
                        user.setImei(CacheData.getInstance().getImei());
                    }
                    user.setOpenid(platDB.getUserId());
                    user.setAt(2);
                    user.setImg(platDB.getUserIcon());
                    user.setNickname(platDB.getUserName());
                    String sexx = platDB.getUserGender();
                    if (!TextUtils.isEmpty(sexx)) {
                        user.setSex(platDB.getUserGender().equals("m") ? 1 : 2);
                    } else {
                        user.setSex(1);
                    }
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = user;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                handler.sendEmptyMessage(2);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                handler.sendEmptyMessage(3);
            }
        });
        wechat.showUser(null);
    }


    public void loginToQQ(final OnLoginListener listener) {
        dialog.show();
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                dialog.dismiss();
                if (msg.what == 1)
                    listener.onSuccess((UserBean) msg.obj);
            }
        };
        Platform wechat = ShareSDK.getPlatform(QQ.NAME);
        if (wechat.isAuthValid()) {
            wechat.removeAccount(true);
        }
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                //用户资源都保存到res
//通过打印res数据看看有哪些数据是你想要的
                if (i == Platform.ACTION_USER_INFOR) {
                    PlatformDb platDB = platform.getDb();//获取数平台数据DB
                    //通过DB获取各种数据
                    platDB.getToken();
                    platDB.getUserGender();
                    platDB.getUserIcon();
                    platDB.getUserId();
                    platDB.getUserName();
                    UserBean user = CacheData.getInstance().getUserData();
                    if (user == null) {
                        user = new UserBean();
                        user.setImei(CacheData.getInstance().getImei());
                    }
                    user.setOpenid(platDB.getUserId());
                    user.setAt(3);
                    user.setImg(platDB.getUserIcon());
                    user.setNickname(platDB.getUserName());
                    String sexx = platDB.getUserGender();
                    if (!TextUtils.isEmpty(sexx)) {
                        user.setSex(platDB.getUserGender().equals("m") ? 1 : 2);
                    } else {
                        user.setSex(1);
                    }
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = user;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                handler.sendEmptyMessage(2);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                handler.sendEmptyMessage(3);
            }
        });
        wechat.showUser(null);
    }

    public interface OnLoginListener {
        void onSuccess(UserBean userBean);
    }

}
