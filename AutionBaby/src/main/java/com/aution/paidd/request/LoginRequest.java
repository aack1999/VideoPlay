package com.aution.paidd.request;


import com.aution.paidd.common.CacheData;

/**
 * Created by quxiang on 2017/3/9.
 */

public class LoginRequest {

    String account;

    String pwd;

    String appid= CacheData.getInstance().getAppid();

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}
