package com.aution.paidd.model;

import java.io.Serializable;

/**
 * Created by yangjie on 2016/9/28.
 */
public class ObjPop implements Serializable{
    private static final long serialVersionUID = 1L;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    private String text;
    private String img;

    public int getMyPositoin() {
        return myPositoin;
    }

    public void setMyPositoin(int myPositoin) {
        this.myPositoin = myPositoin;
    }

    private int myPositoin;

    boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
