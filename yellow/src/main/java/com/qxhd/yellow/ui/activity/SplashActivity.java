package com.qxhd.yellow.ui.activity;

import com.framework.core.base.BaseActivity;
import com.qxhd.yellow.R;
import com.qxhd.yellow.common.HttpMethod;
import com.qxhd.yellow.request.AdvsRequest;
import com.qxhd.yellow.response.AdvsResponse;

import rx.Subscriber;

public class SplashActivity extends BaseActivity {

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    public void initValue() {
//        AdvsRequest request = new AdvsRequest();
//        request.setType("1");
//        HttpMethod.getInstance().getAllAdvs(request,new Subscriber<AdvsResponse>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//
//            }
//
//            @Override
//            public void onNext(AdvsResponse o) {
//
//            }
//        });


        goToRegsiter();


    }

    public void goToRegsiter(){
        doActivity(RegisterActivity.class,true);


    }

}
