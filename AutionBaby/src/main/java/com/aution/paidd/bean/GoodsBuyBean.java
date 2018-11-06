package com.aution.paidd.bean;

import com.aution.paidd.common.CacheData;

/**
 * Created by quxiang on 2017/9/7.
 */

public class GoodsBuyBean {

	long uid= Long.parseLong(CacheData.getInstance().getUserData().getUid());

	int count;

	int aid;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}
}
