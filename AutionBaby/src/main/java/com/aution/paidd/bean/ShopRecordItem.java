package com.aution.paidd.bean;

import java.util.List;

/**
 * Created by quxiang on 2017/9/8.
 */

public class ShopRecordItem {

	int maxresult;

	int currentpage;

	int totalrecord;

	List<ShopRecordBean> resultlist;

	public int getMaxresult() {
		return maxresult;
	}

	public void setMaxresult(int maxresult) {
		this.maxresult = maxresult;
	}

	public int getCurrentpage() {
		return currentpage;
	}

	public void setCurrentpage(int currentpage) {
		this.currentpage = currentpage;
	}

	public int getTotalrecord() {
		return totalrecord;
	}

	public void setTotalrecord(int totalrecord) {
		this.totalrecord = totalrecord;
	}

	public List<ShopRecordBean> getResultlist() {
		return resultlist;
	}

	public void setResultlist(List<ShopRecordBean> resultlist) {
		this.resultlist = resultlist;
	}
}
