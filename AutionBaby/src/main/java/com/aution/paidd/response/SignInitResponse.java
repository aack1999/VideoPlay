package com.aution.paidd.response;

import com.aution.paidd.bean.SignInitItem;

/**
 * Created by quxiang on 2017/9/8.
 */

public class SignInitResponse extends BaseResponse {


	SignInitItem obj;

	public SignInitItem getObj() {
		return obj;
	}

	public void setObj(SignInitItem obj) {
		this.obj = obj;
	}
}
