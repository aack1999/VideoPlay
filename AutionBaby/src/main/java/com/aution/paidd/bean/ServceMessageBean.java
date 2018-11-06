package com.aution.paidd.bean;

import java.io.Serializable;

/**
 * Created by quxiang on 2017/9/5.
 */

public class ServceMessageBean implements Serializable{

	String address;

	String aid;

	String cid;

	int countdown;

	String iconimage;

	String lastbid;

	String lastuid;

	String nowprice;

	int status;

	String at;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public int getCountdown() {
		return countdown;
	}

	public void setCountdown(int countdown) {
		this.countdown = countdown;
	}

	public String getIconimage() {
		return iconimage;
	}

	public void setIconimage(String iconimage) {
		this.iconimage = iconimage;
	}

	public String getLastbid() {
		return lastbid;
	}

	public void setLastbid(String lastbid) {
		this.lastbid = lastbid;
	}

	public String getLastuid() {
		return lastuid;
	}

	public void setLastuid(String lastuid) {
		this.lastuid = lastuid;
	}

	public String getNowprice() {
		return nowprice;
	}

	public void setNowprice(String nowprice) {
		this.nowprice = nowprice;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getAt() {
		return at;
	}

	public void setAt(String at) {
		this.at = at;
	}

	@Override
	public String toString() {
		return "ServceMessageBean{" +
				"address='" + address + '\'' +
				", aid='" + aid + '\'' +
				", cid='" + cid + '\'' +
				", countdown=" + countdown +
				", iconimage='" + iconimage + '\'' +
				", lastbid='" + lastbid + '\'' +
				", lastuid='" + lastuid + '\'' +
				", nowprice='" + nowprice + '\'' +
				", status=" + status +
				", at='" + at + '\'' +
				'}';
	}

	public ServceMessageBean copy(){
		ServceMessageBean bean=new ServceMessageBean();
		bean.setAddress(address);
		bean.setAid(aid);
		bean.setAt(at);
		bean.setCid(cid);
		bean.setCountdown(countdown);
		bean.setIconimage(iconimage);
		bean.setLastbid(lastbid);
		bean.setLastuid(lastuid);
		bean.setNowprice(nowprice);
		bean.setStatus(status);
		return bean;
	}

}
