package com.aution.paidd.bean;

import com.aution.paidd.model.MuiltType;

import java.io.Serializable;

/**
 * Created by quxiang on 2017/9/8.
 */

public class ShopRecordBean extends MuiltType implements Serializable{

	String id;

	String cid;

	String period;

	float nowprice;

	String enddate;

	int status;//1.待付款4.待签收5.待晒单 6 完成

	int statuss;////1.待付款4.待签收5.待晒单 6 完成

	int statusss;//1.待付款4.待签收5.待晒单 6 完成

	String title;

	String headimage;

	String price;

	String lastbid;

	int countdown;

	String createtime;

	String aid;

	int flag;

	String orderno;//订单号,为0表示没有订单

	String name;//商品名称

	String lockbuymoney;//返回的70%的币

	public String getLockbuymoney() {
		return lockbuymoney;
	}

	public void setLockbuymoney(String lockbuymoney) {
		this.lockbuymoney = lockbuymoney;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public float getNowprice() {
		return nowprice;
	}

	public void setNowprice(float nowprice) {
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

	public int getStatuss() {
		return statuss;
	}

	public void setStatuss(int statuss) {
		this.statuss = statuss;
	}

	public int getStatusss() {
		return statusss;
	}

	public void setStatusss(int statusss) {
		this.statusss = statusss;
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getLastbid() {
		return lastbid;
	}

	public void setLastbid(String lastbid) {
		this.lastbid = lastbid;
	}

	public int getCountdown() {
		return countdown;
	}

	public void setCountdown(int countdown) {
		this.countdown = countdown;
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

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	@Override
	public int getItem_type() {
		return 4;
	}
}
