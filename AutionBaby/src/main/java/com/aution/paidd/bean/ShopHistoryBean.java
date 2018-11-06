package com.aution.paidd.bean;

import com.aution.paidd.model.MuiltType;

/**
 * Created by quxiang on 2017/9/6.
 */

public class ShopHistoryBean extends MuiltType{

	String createtime;

	String aid;

	String cid;

	String nickname;

	String img;

	String headimage;

	String nowprice;

	String price;

	String local;

	String savemoney;

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getNowprice() {
		return nowprice;
	}

	public void setNowprice(String nowprice) {
		this.nowprice = nowprice;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getSavemoney() {
		return savemoney;
	}

	public void setSavemoney(String savemoney) {
		this.savemoney = savemoney;
	}

	public String getHeadimage() {
		return headimage;
	}

	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}
}
