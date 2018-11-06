package com.aution.paidd.bean;

/**
 * Created by quxiang on 2017/9/11.
 */

public class PayInitOrderItem {

	int start;//start说明：1.在线支付 2.购币混合支付

	/**
	 * {
	 "code": 10000,
	 "msg": "处理成功!",
	 "obj": [
	 {"start" : 1,"company":"BPAY","tradeNO":"dsffsd,fdsf" }
	 ]
	 }


	 {
	 "code": 10000,
	 "msg": "处理成功!",
	 "obj": [
	 {"start" : 2,"payobj":"支付成功或失败" }
	 ]
	 }
	 */

	String company;

	String tradeNO;

	String payobj;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTradeNO() {
		return tradeNO;
	}

	public void setTradeNO(String tradeNO) {
		this.tradeNO = tradeNO;
	}

	public String getPayobj() {
		return payobj;
	}

	public void setPayobj(String payobj) {
		this.payobj = payobj;
	}
}
