package com.aution.paidd.response;

import com.aution.paidd.bean.LogisticsItem;

/**
 * Created by quxiang on 2017/9/13.
 */

public class LogisticsResponse extends BaseResponse {

	LogisticsItem obj;

	public LogisticsItem getObj() {
		return obj;
	}

	public void setObj(LogisticsItem obj) {
		this.obj = obj;
	}
}
