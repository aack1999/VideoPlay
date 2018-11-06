package com.aution.paidd.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by quxiang on 2017/1/19.
 */
@Entity
public class CustomMessageBean {

    @Id
    long id;

    String url;

    String action;

    String msgid;

    @Generated(hash = 1585304170)
    public CustomMessageBean(long id, String url, String action, String msgid) {
        this.id = id;
        this.url = url;
        this.action = action;
        this.msgid = msgid;
    }

    @Generated(hash = 271627364)
    public CustomMessageBean() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
