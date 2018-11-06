package com.aution.paidd.response;

import com.aution.paidd.bean.PayCenterItem;

/**
 * Created by quxiang on 2017/9/11.
 */

public class PayCenterResponse extends BaseResponse{

	PayCenterItem obj;

	public PayCenterItem getObj() {
		return obj;
	}

	public void setObj(PayCenterItem obj) {
		this.obj = obj;
	}
}
