package com.aution.paidd.request;

/**
 * Created by quxiang on 2017/3/13.
 */

public class UpUserInfoRequest {

    String uid;

    String nickname;

    String sex;

    String remark;

    String qq;

    String weixi;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeixi() {
        return weixi;
    }

    public void setWeixi(String weixi) {
        this.weixi = weixi;
    }
}
