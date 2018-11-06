package com.aution.paidd.response;

import com.aution.paidd.bean.HeadLineBean;

import java.util.List;

/**
 * Created by quxiang on 2017/9/5.
 */

public class HeadLineResponse extends BaseResponse {

	List<HeadLineBean> obj;

	public List<HeadLineBean> getObj() {
		return obj;
	}

	public void setObj(List<HeadLineBean> obj) {
		this.obj = obj;
	}
}
