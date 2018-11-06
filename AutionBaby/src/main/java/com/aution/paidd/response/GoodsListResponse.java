package com.aution.paidd.response;

import com.aution.paidd.bean.GoodsListItem;

/**
 * Created by quxiang on 2017/9/4.
 */

public class GoodsListResponse extends BaseResponse {

	GoodsListItem obj;

	public GoodsListItem getObj() {
		return obj;
	}

	public void setObj(GoodsListItem obj) {
		this.obj = obj;
	}
}
