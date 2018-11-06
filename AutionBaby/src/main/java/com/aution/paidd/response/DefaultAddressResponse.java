package com.aution.paidd.response;

import com.aution.paidd.bean.MyAddressObJ;

/**
 * Created by quxiang on 2017/9/11.
 */

public class DefaultAddressResponse extends BaseResponse {

	MyAddressObJ obj;

	public MyAddressObJ getObj() {
		return obj;
	}

	public void setObj(MyAddressObJ obj) {
		this.obj = obj;
	}
}
