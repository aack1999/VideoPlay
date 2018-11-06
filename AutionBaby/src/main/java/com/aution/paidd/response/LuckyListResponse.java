package com.aution.paidd.response;

import com.aution.paidd.bean.LuckyListItem;

/**
 * Created by quxiang on 2017/9/9.
 */

public class LuckyListResponse extends BaseResponse{

	LuckyListItem obj;

	public LuckyListItem getObj() {
		return obj;
	}

	public void setObj(LuckyListItem obj) {
		this.obj = obj;
	}
}
