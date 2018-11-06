package com.aution.paidd.bean;

import java.io.Serializable;

/**
 * Created by quxiang on 2017/9/6.
 */

public class ShopCinfoBean implements Serializable{

	String aid;

	String cid;

	String imges;//；分开

	String nowprice;

	String price;

	String enddate;

	int countdown;

	String name;

	String singleprice;

	int status;//进程状态 1倒计时 2已成交 3暂停

	int paicount;

	int givecount;

	int recordcount;

	int count;//消耗币的总数

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getRecordcount() {
		return recordcount;
	}

	public void setRecordcount(int recordcount) {
		this.recordcount = recordcount;
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

	public String getImges() {
		return imges;
	}

	public void setImges(String imges) {
		this.imges = imges;
	}

	public String getNowprice() {
		return nowprice;
	}

	public void setNowprice(String nowprice) {
		this.nowprice = nowprice;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public int getCountdown() {
		return countdown;
	}

	public void setCountdown(int countdown) {
		this.countdown = countdown;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSingleprice() {
		return singleprice;
	}

	public void setSingleprice(String singleprice) {
		this.singleprice = singleprice;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPaicount() {
		return paicount;
	}

	public void setPaicount(int paicount) {
		this.paicount = paicount;
	}

	public int getGivecount() {
		return givecount;
	}

	public void setGivecount(int givecount) {
		this.givecount = givecount;
	}
}
