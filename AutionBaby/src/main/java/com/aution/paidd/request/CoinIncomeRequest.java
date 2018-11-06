package com.aution.paidd.request;

/**
 * Created by quxiang on 2017/9/8.
 */

public class CoinIncomeRequest extends BaseListRequest{

	String uid;

	String type;

	String state;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
