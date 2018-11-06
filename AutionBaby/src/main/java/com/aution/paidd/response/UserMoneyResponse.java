package com.aution.paidd.response;

import com.aution.paidd.bean.UserMoneyItem;

/**
 * Created by quxiang on 2017/9/15.
 */

public class UserMoneyResponse extends BaseResponse {

	UserMoneyItem obj;

	public UserMoneyItem getObj() {
		return obj;
	}

	public void setObj(UserMoneyItem obj) {
		this.obj = obj;
	}
}
