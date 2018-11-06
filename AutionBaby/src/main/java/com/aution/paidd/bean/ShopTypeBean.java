package com.aution.paidd.bean;

/**
 * Created by quxiang on 2017/9/5.
 */

public class ShopTypeBean {

	String id;

	String name;

	String img;

	boolean isCheck;

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean check) {
		isCheck = check;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
}
