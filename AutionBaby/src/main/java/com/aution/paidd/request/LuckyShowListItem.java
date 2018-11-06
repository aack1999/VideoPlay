package com.aution.paidd.request;

import java.util.List;

/**
 * Created by quxiang on 2017/9/11.
 */

public class LuckyShowListItem {

	int maxresult;

	int currentpage;

	int totalrecord;

	List<LuckyShowListBean> resultlist;

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

	public List<LuckyShowListBean> getResultlist() {
		return resultlist;
	}

	public void setResultlist(List<LuckyShowListBean> resultlist) {
		this.resultlist = resultlist;
	}
}
