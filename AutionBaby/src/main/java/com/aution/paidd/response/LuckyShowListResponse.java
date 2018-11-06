package com.aution.paidd.response;

import com.aution.paidd.request.LuckyShowListItem;

/**
 * Created by quxiang on 2017/9/11.
 */

public class LuckyShowListResponse extends BaseResponse{

	LuckyShowListItem obj;

	public LuckyShowListItem getObj() {
		return obj;
	}

	public void setObj(LuckyShowListItem obj) {
		this.obj = obj;
	}
}
