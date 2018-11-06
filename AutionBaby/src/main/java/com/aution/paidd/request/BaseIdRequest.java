package com.aution.paidd.request;

/**
 * Created by quxiang on 2017/9/7.
 */

public class BaseIdRequest extends BaseListRequest{

	String aid;

	String uid;

	String cid;

	String ids;

	String type;

	String flag;// 1.收藏 2.取消收藏

	public String getFlag() {
		return flag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}
}
