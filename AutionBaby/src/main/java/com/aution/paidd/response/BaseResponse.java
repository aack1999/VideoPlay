package com.aution.paidd.response;

import java.io.Serializable;

/**
 * Created by quxiang on 2017/2/28.
 */

public class BaseResponse implements Serializable{

    int code;//返回状态码

    boolean success;//是否成功

    String msg;//错误信息

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
