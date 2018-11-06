package com.aution.paidd.request;

/**
 * Created by quxiang on 2017/9/7.
 */

public class GoodsBuyRequest {

	String sign;

	String tobuyinfo;

	String orderinfo;

	String comment;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getOrderinfo() {
		return orderinfo;
	}

	public void setOrderinfo(String orderinfo) {
		this.orderinfo = orderinfo;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTobuyinfo() {
		return tobuyinfo;
	}

	public void setTobuyinfo(String tobuyinfo) {
		this.tobuyinfo = tobuyinfo;
	}
}
