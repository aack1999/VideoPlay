package com.aution.paidd.bean;

import com.aution.paidd.model.MuiltType;

/**
 * Created by quxiang on 2017/9/4.
 */

public class GoodsListBean extends MuiltType{

	String id;

	String cid;//销售id

	String nowprice;//当前竞拍价格

	String enddate;//成交日期

	int status;//状态

	String title;

	String headimage;

	int price;

	int countdown;//倒计时

	String 	lastbid;

	String name;

	int state;//是否收藏

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getLastbid() {
		return lastbid;
	}

	public void setLastbid(String lastbid) {
		this.lastbid = lastbid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getNowprice() {
		return nowprice;
	}

	public void setNowprice(String nowprice) {
		this.nowprice = nowprice;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHeadimage() {
		return headimage;
	}

	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getCountdown() {
		return countdown;
	}

	public void setCountdown(int countdown) {
		this.countdown = countdown;
	}
}
