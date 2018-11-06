package com.aution.paidd.request;

/**
 * Created by quxiang on 2017/9/4.
 */

public class BaseListRequest {


	String  currentpage;

	String  maxresult;

	String novices;//新手推荐 1.不推荐 2.推荐

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

	public String getNovices() {
		return novices;
	}

	public void setNovices(String novices) {
		this.novices = novices;
	}
}
