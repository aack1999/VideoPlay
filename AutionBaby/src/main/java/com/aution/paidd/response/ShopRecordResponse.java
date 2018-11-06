package com.aution.paidd.response;

import com.aution.paidd.bean.ShopRecordItem;

/**
 * Created by quxiang on 2017/9/8.
 */

public class ShopRecordResponse extends BaseResponse {

	ShopRecordItem obj;

	public ShopRecordItem getObj() {
		return obj;
	}

	public void setObj(ShopRecordItem obj) {
		this.obj = obj;
	}
}
