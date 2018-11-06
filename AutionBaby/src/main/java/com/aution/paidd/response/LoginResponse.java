package com.aution.paidd.response;


import com.aution.paidd.bean.UserBean;

public class LoginResponse extends BaseResponse {

    UserBean obj;

    public UserBean getObj() {
        return obj;
    }

    public void setObj(UserBean obj) {
        this.obj = obj;
    }
}