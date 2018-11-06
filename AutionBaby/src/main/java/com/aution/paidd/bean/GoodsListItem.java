package com.aution.paidd.bean;

import java.util.List;

/**
 * Created by quxiang on 2017/9/4.
 */

public class GoodsListItem {

	int maxresult;

	int currentpage;

	int totalrecord;

	List<GoodsListBean> resultlist;

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

	public List<GoodsListBean> getResultlist() {
		return resultlist;
	}

	public void setResultlist(List<GoodsListBean> resultlist) {
		this.resultlist = resultlist;
	}
}
