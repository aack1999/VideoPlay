package com.aution.paidd.response;

import com.aution.paidd.bean.PayInitOrderItem;

/**
 * Created by quxiang on 2017/9/11.
 */

public class PayInitOrderResponse extends BaseResponse{

	PayInitOrderItem obj;

	public PayInitOrderItem getObj() {
		return obj;
	}

	public void setObj(PayInitOrderItem obj) {
		this.obj = obj;
	}
}
