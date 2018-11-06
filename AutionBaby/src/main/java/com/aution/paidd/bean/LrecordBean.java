package com.aution.paidd.bean;

import java.io.Serializable;

/**
 * Created by quxiang on 2017/9/6.
 */

public class LrecordBean implements Serializable{


	String nickname;

	String status;

	String local;

	String nowprice;

	String img;

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getNowprice() {
		return nowprice;
	}

	public void setNowprice(String nowprice) {
		this.nowprice = nowprice;
	}

	@Override
	public String toString() {
		return "LrecordBean{" +
				"nickname='" + nickname + '\'' +
				", status='" + status + '\'' +
				", local='" + local + '\'' +
				", nowprice='" + nowprice + '\'' +
				", img='" + img + '\'' +
				'}';
	}
}
