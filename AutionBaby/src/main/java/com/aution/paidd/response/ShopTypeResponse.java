package com.aution.paidd.response;

import com.aution.paidd.bean.ShopTypeBean;

import java.util.List;

/**
 * Created by quxiang on 2017/9/5.
 */

public class ShopTypeResponse extends BaseResponse {

	List<ShopTypeBean> obj;

	public List<ShopTypeBean> getObj() {
		return obj;
	}

	public void setObj(List<ShopTypeBean> obj) {
		this.obj = obj;
	}
}
