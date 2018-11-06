package com.aution.paidd.bean;

import java.util.List;

/**
 * Created by quxiang on 2016/12/12.
 */
public class PayListBean  {

    int currentpage;

    int maxresult;

    int totalrecord;

    List<PayListItemBean> resultlist;

    public int getCurrentpage() {
        return currentpage;
    }

    public void setCurrentpage(int currentpage) {
        this.currentpage = currentpage;
    }

    public int getMaxresult() {
        return maxresult;
    }

    public void setMaxresult(int maxresult) {
        this.maxresult = maxresult;
    }

    public List<PayListItemBean> getResultlist() {
        return resultlist;
    }

    public void setResultlist(List<PayListItemBean> resultlist) {
        this.resultlist = resultlist;
    }

    public int getTotalrecord() {
        return totalrecord;
    }

    public void setTotalrecord(int totalrecord) {
        this.totalrecord = totalrecord;
    }
}
