package com.aution.paidd.bean;

/**
 * Created by quxiang on 2017/9/11.
 */

public class PayCenterItem {

	String tradeNO;

	String company;

	int start;

	String payobj;

	public String getPayobj() {
		return payobj;
	}

	public void setPayobj(String payobj) {
		this.payobj = payobj;
	}

	public String getTradeNO() {
		return tradeNO;
	}

	public void setTradeNO(String tradeNO) {
		this.tradeNO = tradeNO;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}
}
