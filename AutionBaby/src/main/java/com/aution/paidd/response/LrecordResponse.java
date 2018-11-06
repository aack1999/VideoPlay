package com.aution.paidd.response;

import com.aution.paidd.bean.LrecordItem;

/**
 * Created by quxiang on 2017/9/7.
 */

public class LrecordResponse extends BaseResponse{

	LrecordItem obj;

	public LrecordItem getObj() {
		return obj;
	}

	public void setObj(LrecordItem obj) {
		this.obj = obj;
	}
}
