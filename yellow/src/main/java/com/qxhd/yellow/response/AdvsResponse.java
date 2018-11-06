package com.qxhd.yellow.response;

import java.util.List;

public class AdvsResponse extends BaseResponse{

    List<AdvsBean> data;

    public List<AdvsBean> getData() {
        return data;
    }

    public void setData(List<AdvsBean> data) {
        this.data = data;
    }
}
