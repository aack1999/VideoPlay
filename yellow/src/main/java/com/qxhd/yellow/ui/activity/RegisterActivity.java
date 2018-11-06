package com.qxhd.yellow.ui.activity;

import com.framework.core.base.BaseActivity;
import com.qxhd.yellow.R;
import com.qxhd.yellow.common.CKTools;
import com.qxhd.yellow.common.HttpMethod;
import com.qxhd.yellow.request.RegsiterRequest;
import com.qxhd.yellow.response.LoginResponse;

import rx.Subscriber;

public class RegisterActivity extends BaseActivity {

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_rigster ;
    }

    @Override
    public void initValue() {

    }


    public void submit(){
        RegsiterRequest request=new RegsiterRequest();
        request.setImei(CKTools.getImei());
        HttpMethod.getInstance().getInitLogin(request, new Subscriber<LoginResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(LoginResponse o) {

            }
        });
    }


}
