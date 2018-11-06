package com.aution.paidd.response;

import com.framework.core.model.BannerBean;

import java.util.List;

/**
 * Created by quxiang on 2017/2/28.
 */

public class BannerResponse extends BaseResponse {

    List<BannerBean> obj;

    public List<BannerBean> getObj() {
        return obj;
    }

    public void setObj(List<BannerBean> obj) {
        this.obj = obj;
    }
}
