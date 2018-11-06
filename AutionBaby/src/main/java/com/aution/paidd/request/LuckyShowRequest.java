package com.aution.paidd.request;

/**
 * Created by quxiang on 2017/9/11.
 */

public class LuckyShowRequest {

	String uid;

	String maxresult;

	String currentpage;

	String aid;

	String state;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getMaxresult() {
		return maxresult;
	}

	public void setMaxresult(String maxresult) {
		this.maxresult = maxresult;
	}

	public String getCurrentpage() {
		return currentpage;
	}

	public void setCurrentpage(String currentpage) {
		this.currentpage = currentpage;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
