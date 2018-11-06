package com.aution.paidd.bean;

import com.aution.paidd.common.CacheData;

/**
 * Created by quxiang on 2017/9/11.
 */

public class OrderInfoBean {

	String appid= CacheData.getInstance().getAppid();

	String system="ANDROID";

	String paytype;

	String uid;

	String amount;//需支付的金额

	String aid;//商品id

	String addid;//地址id

	String buymoney;//购币

	String scene;//场景

	String orderno;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getAddid() {
		return addid;
	}

	public void setAddid(String addid) {
		this.addid = addid;
	}

	public String getBuymoney() {
		return buymoney;
	}

	public void setBuymoney(String buymoney) {
		this.buymoney = buymoney;
	}

	public String getScene() {
		return scene;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
}
