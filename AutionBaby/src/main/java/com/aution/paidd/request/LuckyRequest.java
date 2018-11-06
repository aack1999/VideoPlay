package com.aution.paidd.request;

import com.aution.paidd.common.CacheData;

/**
 * Created by quxiang on 2017/9/9.
 */

public class LuckyRequest {

	String uid;

	String maxresult;

	String currentpage;

	String appid= CacheData.getInstance().getAppid();

	String view;

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

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}
}
