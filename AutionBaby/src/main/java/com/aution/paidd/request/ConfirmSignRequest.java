package com.aution.paidd.request;

/**
 * Created by quxiang on 2017/9/11.
 */

public class ConfirmSignRequest {

	String orderno;

	String aid;

	String uid;

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
}
