package com.aution.paidd.bean;

/**
 * Created by quxiang on 2016/12/12.
 */
public class PayListItemBean {

    String id;

    String amount;//金额

    String ordertime;//充值时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }
}
