package com.aution.paidd.model;

import java.io.Serializable;

/**
 * Created by yangjie on 2016/10/14.
 */
public class PayObjBeen implements Serializable{

    private String tradeNO;
    private String partnerid;
    private String seller;
    private String privatekey;
    private String productName;
    private String company;//支付四方平台
    private String notifyURL;
    private String prepayid;
    private String h5content;
    private String payobj;

    public String getApppackage() {
        return apppackage;
    }

    public void setApppackage(String apppackage) {
        this.apppackage = apppackage;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    private String apppackage;
    private String timeStart;



    public String getPartnerappid() {
        return partnerappid;
    }

    public void setPartnerappid(String partnerappid) {
        this.partnerappid = partnerappid;
    }

    private String partnerappid;

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    private String apikey;

    @Override
    public String toString() {
        return "PayObjBeen{" +
                "tradeNO='" + tradeNO + '\'' +
                ", partnerid='" + partnerid + '\'' +
                ", seller='" + seller + '\'' +
                ", privatekey='" + privatekey + '\'' +
                ", productName='" + productName + '\'' +
                ", company='" + company + '\'' +
                ", notifyURL='" + notifyURL + '\'' +
                ", prepayid='" + prepayid + '\'' +
                ", autograph='" + autograph + '\'' +
                ", nonecstr='" + nonecstr + '\'' +
                ", amount=" + amount +
                '}';
    }


    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public String getTradeNO() {
        return tradeNO;
    }

    public void setTradeNO(String tradeNO) {
        this.tradeNO = tradeNO;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getNotifyURL() {
        return notifyURL;
    }

    public void setNotifyURL(String notifyURL) {
        this.notifyURL = notifyURL;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPrivatekey() {
        return privatekey;
    }

    public void setPrivatekey(String privatekey) {
        this.privatekey = privatekey;
    }

    public String getNonecstr() {
        return nonecstr;
    }

    public void setNonecstr(String nonecstr) {
        this.nonecstr = nonecstr;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    private String autograph;
    private String nonecstr;
    private long amount;


    public String getH5content() {
        return h5content;
    }

    public void setH5content(String h5content) {
        this.h5content = h5content;
    }

    public String getPayobj() {
        return payobj;
    }

    public void setPayobj(String payobj) {
        this.payobj = payobj;
    }
}
