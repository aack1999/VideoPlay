package com.aution.paidd.bean;

import java.io.Serializable;

/**
 * Created by quxiang on 2017/9/9.
 */

public class LuckyListBean implements Serializable{

	String createtime;

	String aid;

	String count;

	int status;

	int statuss;

	String headimage;

	float nowprice;

	String price;

	String savemoney;

	String orderno;//订单号,为0表示没有订单

	int flag;//0.未晒单 1.已晒单

	String cid;

	String tradeno;

	int ordertype;//1 我拍中  2. 差价购

	String lockbuymoney;

	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLockbuymoney() {
		return lockbuymoney;
	}

	public void setLockbuymoney(String lockbuymoney) {
		this.lockbuymoney = lockbuymoney;
	}

	public int getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(int ordertype) {
		this.ordertype = ordertype;
	}

	public String getTradeno() {
		return tradeno;
	}

	public void setTradeno(String tradeno) {
		this.tradeno = tradeno;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getHeadimage() {
		return headimage;
	}

	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}

	public float getNowprice() {
		return nowprice;
	}

	public void setNowprice(float nowprice) {
		this.nowprice = nowprice;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSavemoney() {
		return savemoney;
	}

	public void setSavemoney(String savemoney) {
		this.savemoney = savemoney;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getStatuss() {
		return statuss;
	}

	public void setStatuss(int statuss) {
		this.statuss = statuss;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}
}
