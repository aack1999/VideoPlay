package com.aution.paidd.request;

/**
 * Created by quxiang on 2017/9/13.
 */

public class OrderRequest {

	String orderno;

	String tradeNO;

	String uid;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getTradeNO() {
		return tradeNO;
	}

	public void setTradeNO(String tradeNO) {
		this.tradeNO = tradeNO;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
}
