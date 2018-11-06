package com.aution.paidd.request;


import com.aution.paidd.common.CacheData;

/**
 * Created by quxiang on 2017/3/17.
 */

public class UpAppRequest {

    String appid= CacheData.getInstance().getAppid();

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}
