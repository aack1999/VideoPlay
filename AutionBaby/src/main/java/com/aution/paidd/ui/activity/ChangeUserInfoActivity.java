package com.aution.paidd.ui.activity;

import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.framework.core.base.BaseActivity;
import com.aution.paidd.R;
import com.aution.paidd.bean.UserBean;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.Constant;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.request.UpUserInfoRequest;
import com.aution.paidd.response.LoginResponse;


import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;


/**
 * 修改用户信息
 * Created by yangjie on 2016/10/21.
 */
public class ChangeUserInfoActivity extends BaseActivity {


    public static final int TYPE_NAME = 1;
    public static final int TYPE_SEX = 2;
    public static final int TYPE_WX = 3;
    public static final int TYPE_QQ = 4;
    public static final int TYPE_PHONE = 5;

    int type;
    @BindView(R.id.ed_title)
    TextView ed_title;
    @BindView(R.id.ed_value)
    EditText ed_value;
    UserBean model;

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_change_userinfo;
    }

    @OnClick({R.id.submit})
    public void onClick(View v){
        if (checkInput()) {
            doYanZhengPass();
        }
    }


    public void initValue() {
        model = CacheData.getInstance().getUserData();
        type = getIntent().getIntExtra("type", TYPE_NAME);

        switch (type) {
            case TYPE_NAME:
                setTitle("修改昵称");
                ed_value.setText(CacheData.getInstance().getUserData().getNickname());
                ed_value.setHint("请输入昵称");
                ed_value.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                ed_title.setText("昵称:");
                break;
            case TYPE_QQ:
//                setTitle("修改QQ号");
//                ed_title.setText("QQ号:");
//                ed_value.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
//                ed_value.setHint("请输入QQ号");
//                ed_value.setText(CacheData.getUserData().getQq());
                break;
            case TYPE_WX:
//                setTitle("修改微信号/支付宝号");
//                ed_title.setText("微信号/支付宝号:");
//                ed_value.setHint("请输入微信号或者支付宝号");
//                ed_value.setFilters(new InputFilter[]{new InputFilter.LengthFilter(40)});
//                ed_value.setText(CacheData.getUserData().getWeixi());
                break;
        }

    }

    public boolean checkInput() {

        switch (type) {
            case TYPE_NAME: {
                if (TextUtils.isEmpty(ed_value.getText().toString())) {
                    showToast("请输入昵称");
                    return false;
                }
                model.setNickname(ed_value.getText().toString());
                return true;
            }
            case TYPE_QQ: {
//                if (TextUtils.isEmpty(ed_value.getText().toString())) {
//                    showToast("请输入QQ号");
//                    return false;
//                }
//                if (ed_value.getText().toString().length() < 5) {
//                    showToast("请输入正确的QQ号");
//                    return false;
//                }
//                model.setQq(ed_value.getText().toString());
                return true;
            }
            case TYPE_WX: {
//                if (TextUtils.isEmpty(ed_value.getText().toString())) {
//                    showToast("请输入微信号/支付宝号");
//                    return false;
//                }
//                if (ed_value.getText().toString().length() < 5) {
//                    showToast("请输入正确的微信号或者支付宝号");
//                    return false;
//                }
//                model.setWeixi(ed_value.getText().toString());
                return true;
            }
        }

        return true;
    }


    private void doYanZhengPass() {

        UpUserInfoRequest request=new UpUserInfoRequest();
        request.setUid(model.getUid());
        request.setNickname(model.getNickname());
        Subscriber<LoginResponse> subscriber=new Subscriber<LoginResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showToast(Constant.Service_Err);
            }

            @Override
            public void onNext(LoginResponse popuBean) {
                showToast(popuBean.getMsg());
                if (popuBean != null && popuBean.isSuccess()) {
                    CacheData.getInstance().cacheUserData(popuBean.getObj());
                    setResult(200);
                    finish();
                }
            }
        };
        HttpMethod.getInstance().getUpDataInfo(subscriber,request);
    }

}
