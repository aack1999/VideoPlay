package com.aution.paidd.response;

import com.aution.paidd.bean.MyAddressObJ;

import java.util.List;

/**
 * Created by quxiang on 2017/9/7.
 */

public class AdressUpReponse extends BaseResponse{

	List<MyAddressObJ> obj;

	public List<MyAddressObJ> getObj() {
		return obj;
	}

	public void setObj(List<MyAddressObJ> obj) {
		this.obj = obj;
	}
}
