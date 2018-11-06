package com.aution.paidd.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by quxiang on 2016/12/5.
 */
@Entity
public class UserBean  {

    @Id
    private Long id;

    String uid;

    String imei;

    int sex;

    int buymoney;

    String nickname;

    int at;

    String img;

    int givemoney;

    int patmoney;

    int experience;

    String pwd;

    String account;

    String openid;//第三方登录用户id

    int flag;


    @Generated(hash = 933605058)
    public UserBean(Long id, String uid, String imei, int sex, int buymoney,
            String nickname, int at, String img, int givemoney, int patmoney,
            int experience, String pwd, String account, String openid, int flag) {
        this.id = id;
        this.uid = uid;
        this.imei = imei;
        this.sex = sex;
        this.buymoney = buymoney;
        this.nickname = nickname;
        this.at = at;
        this.img = img;
        this.givemoney = givemoney;
        this.patmoney = patmoney;
        this.experience = experience;
        this.pwd = pwd;
        this.account = account;
        this.openid = openid;
        this.flag = flag;
    }

    @Generated(hash = 1203313951)
    public UserBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getSex() {
        return this.sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getBuymoney() {
        return this.buymoney;
    }

    public void setBuymoney(int buymoney) {
        this.buymoney = buymoney;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAt() {
        return this.at;
    }

    public void setAt(int at) {
        this.at = at;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getGivemoney() {
        return this.givemoney;
    }

    public void setGivemoney(int givemoney) {
        this.givemoney = givemoney;
    }

    public int getPatmoney() {
        return this.patmoney;
    }

    public void setPatmoney(int patmoney) {
        this.patmoney = patmoney;
    }

    public int getExperience() {
        return this.experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getImei() {
        return this.imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getPwd() {
        return this.pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public int getFlag() {
        return this.flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
