package com.aution.paidd.request;

/**
 * Created by quxiang on 2017/9/6.
 */

public class ShopHistoryRequest {

	String cid;

	String currentpage;

	String maxresult;

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCurrentpage() {
		return currentpage;
	}

	public void setCurrentpage(String currentpage) {
		this.currentpage = currentpage;
	}

	public String getMaxresult() {
		return maxresult;
	}

	public void setMaxresult(String maxresult) {
		this.maxresult = maxresult;
	}
}
