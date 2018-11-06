package com.aution.paidd.bean;

import java.io.Serializable;

/**
 * Created by yangjie on 2016/10/11.
 */
public class ShaiDan implements Serializable {
    private static final long serialVersionUID = 1L;
    private String content;
    private String friendlytime;
    private long id;
    private String images;
    private String nickname;
    private int replycount;
    private String title;
    private String img;
    private int upcount;
    private String ctitle;
    private int period;
    private long buyid;
    private String headimage;

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    private long cid;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getCtitle() {
        return ctitle;
    }

    public void setCtitle(String ctitle) {
        this.ctitle = ctitle;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public long getBuyid() {
        return buyid;
    }

    public void setBuyid(long buyid) {
        this.buyid = buyid;
    }

    public int getStatus() {
        return status;
    }

    public String getHeadimage() {
        return headimage;
    }

    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private long uid;
    private int status;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFriendlytime() {
        return friendlytime;
    }

    public void setFriendlytime(String friendlytime) {
        this.friendlytime = friendlytime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getReplycount() {
        return replycount;
    }

    public void setReplycount(int replycount) {
        this.replycount = replycount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getUpcount() {
        return upcount;
    }

    public void setUpcount(int upcount) {
        this.upcount = upcount;
    }
}
