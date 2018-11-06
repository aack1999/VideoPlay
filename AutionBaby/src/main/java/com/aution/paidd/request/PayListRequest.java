package com.aution.paidd.request;

import com.aution.paidd.common.CacheData;

/**
 * Created by quxiang on 2017/3/16.
 */

public class PayListRequest {

    String appid= CacheData.getInstance().getAppid();

    String system;

    String version;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
