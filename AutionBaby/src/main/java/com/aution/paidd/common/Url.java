package com.aution.paidd.common;

/**
 * Created by quxiang on 2017/9/12.
 */

public class Url {

	public static String getUpLogoUrl(){
		return HttpMethod.BASE_URL+"service/user/upUserImg";
	}


	public static String getAddLuckyShow(){
		return HttpMethod.BASE_URL+"service/share/add";
	}

}
