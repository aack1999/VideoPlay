package com.qxhd.yellow.request;

public class RegsiterRequest extends BaseRequest {

    String system = "1";

    String imei;

    String token;

    public String getToken(){
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    @Override
    public String getMethod() {
        return "tourists";
    }


}
