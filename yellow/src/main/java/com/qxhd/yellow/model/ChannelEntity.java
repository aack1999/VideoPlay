package com.qxhd.yellow.model;

import java.io.Serializable;

/**
 * Created by sunfusheng on 16/4/20.
 */
public class ChannelEntity implements Serializable {

    private String title;
    private int resid;
    String desc;
    String img;
    String id;



    public ChannelEntity() {

    }

    public ChannelEntity(String title) {
        this.title = title;
    }

    public ChannelEntity(String title, int resid) {
        this.title = title;
        this.resid = resid;
    }

    public ChannelEntity(String title, int resid, String desc) {
        this.title = title;
        this.resid = resid;
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getResid() {
        return resid;
    }

    public void setResid(int resid) {
        this.resid = resid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
