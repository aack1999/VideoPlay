package com.aution.paidd.response;

import com.aution.paidd.bean.ShopHistoryItem;

/**
 * Created by quxiang on 2017/9/6.
 */

public class ShopHistoryResponse extends BaseResponse {

	ShopHistoryItem obj;

	public ShopHistoryItem getObj() {
		return obj;
	}

	public void setObj(ShopHistoryItem obj) {
		this.obj = obj;
	}
}
