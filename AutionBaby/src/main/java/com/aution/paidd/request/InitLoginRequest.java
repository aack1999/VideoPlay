package com.aution.paidd.request;

import com.aution.paidd.common.CacheData;

/**
 * Created by quxiang on 2017/8/31.
 */

public class InitLoginRequest  {

	String uid;

	String pwd;

	String appid= CacheData.getInstance().getAppid();

	String openid;

	String imei=CacheData.getInstance().getImei();

	String system="ANDROID";

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}
}
