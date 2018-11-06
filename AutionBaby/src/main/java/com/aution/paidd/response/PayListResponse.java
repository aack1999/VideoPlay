package com.aution.paidd.response;


import com.aution.paidd.bean.PayListBean;

/**
 * Created by quxiang on 2016/12/12.
 */
public class PayListResponse extends BaseResponse {

    PayListBean obj;

    public PayListBean getObj() {
        return obj;
    }

    public void setObj(PayListBean obj) {
        this.obj = obj;
    }
}
