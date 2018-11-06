package com.aution.paidd.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by quxiang on 2017/1/20.
 */
@Entity
public class AdvsBean {

    @Id
    Long id;

    String img;

    String url;

    String advsFile;

    @Generated(hash = 1641872469)
    public AdvsBean(Long id, String img, String url, String advsFile) {
        this.id = id;
        this.img = img;
        this.url = url;
        this.advsFile = advsFile;
    }

    @Generated(hash = 1967590729)
    public AdvsBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAdvsFile() {
        return this.advsFile;
    }

    public void setAdvsFile(String advsFile) {
        this.advsFile = advsFile;
    }




}
