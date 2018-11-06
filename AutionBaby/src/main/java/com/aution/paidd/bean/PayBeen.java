package com.aution.paidd.bean;


import com.aution.paidd.model.PayObjBeen;

/**
 * Created by yangjie on 2016/10/14.
 */
public class PayBeen {
    private int cmd;
    private String msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public PayObjBeen getObj() {
        return obj;
    }

    public void setObj(PayObjBeen obj) {
        this.obj = obj;
    }

    private boolean success;
    private PayObjBeen obj;

    @Override
    public String toString() {
        return "PayBeen{" +
                "cmd=" + cmd +
                ", msg='" + msg + '\'' +
                ", success=" + success +
                ", obj=" + obj +
                '}';
    }
}
