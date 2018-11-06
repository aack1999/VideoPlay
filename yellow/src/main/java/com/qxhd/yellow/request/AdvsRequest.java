package com.qxhd.yellow.request;

public class AdvsRequest extends BaseRequest{

    String type;

    @Override
    public String getMethod() {
        return "";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
