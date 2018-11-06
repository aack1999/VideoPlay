package com.aution.paidd.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by quxiang on 2017/9/6.
 */

public class ShopDetialItem implements Serializable{

	List<LrecordBean> lrecords;

	ShopLbidBean lbid;

	ShopCinfoBean info;

	public List<LrecordBean> getLrecords() {
		return lrecords;
	}

	public void setLrecords(List<LrecordBean> lrecords) {
		this.lrecords = lrecords;
	}

	public ShopLbidBean getLbid() {
		return lbid;
	}

	public void setLbid(ShopLbidBean lbid) {
		this.lbid = lbid;
	}

	public ShopCinfoBean getInfo() {
		return info;
	}

	public void setInfo(ShopCinfoBean info) {
		this.info = info;
	}

}
