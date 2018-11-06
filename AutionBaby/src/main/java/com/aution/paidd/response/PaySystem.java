package com.aution.paidd.response;

/**
 * Created by yangjie on 2016/10/20.
 */
public class PaySystem {
    private int cmd;
    private boolean success;
    private String obj[];

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String[] getObj() {
        return obj;
    }

    public void setObj(String[] obj) {
        this.obj = obj;
    }
}
