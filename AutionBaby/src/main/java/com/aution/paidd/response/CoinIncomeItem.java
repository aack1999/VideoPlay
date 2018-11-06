package com.aution.paidd.response;

import com.aution.paidd.bean.CoinIncomeBean;

import java.util.List;

/**
 * Created by quxiang on 2017/9/8.
 */

public class CoinIncomeItem {

	int maxresult;

	int currentpage;

	int totalrecord;

	List<CoinIncomeBean> resultlist;

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

	public List<CoinIncomeBean> getResultlist() {
		return resultlist;
	}

	public void setResultlist(List<CoinIncomeBean> resultlist) {
		this.resultlist = resultlist;
	}
}
