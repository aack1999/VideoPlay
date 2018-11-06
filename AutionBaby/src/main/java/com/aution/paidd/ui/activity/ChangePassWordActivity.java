package com.aution.paidd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.framework.core.base.ActivityManager;
import com.framework.core.base.BaseActivity;
import com.framework.core.widget.FlatButton;
import com.aution.paidd.R;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.Constant;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.common.MerchantApiUtil;
import com.aution.paidd.model.MessageEvent;
import com.aution.paidd.request.RegisterRequest;
import com.aution.paidd.request.SendSmsRequest;
import com.aution.paidd.response.BaseResponse;
import com.aution.paidd.response.LoginResponse;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import rx.Subscriber;

/**
 * Created by quxiang on 2017/8/31.
 */

public class ChangePassWordActivity extends BaseActivity {

	int type;//1注册  2修改 3找回密码

	@BindView(R.id.ed_phone)
	EditText ed_phone;
	@BindView(R.id.ed_code)
	EditText ed_code;
	@BindView(R.id.ed_pwd)
	EditText ed_pwd;
	@BindView(R.id.ed_repwd)
	EditText ed_repwd;
	@BindView(R.id.btn_submit)
	FlatButton btn_submit;

	@BindView(R.id.btn_getCode)
	TextView btn_getCode;

	TimeCount time;

	@Override
	public int getContentLayoutID() {
		return R.layout.activity_change_password;
	}

	@Override
	public void initValue() {
		type=getIntent().getIntExtra("type",0);

		switch (type){
			case 1:
				setTitle(getString(R.string.app_name));
				break;
			case 2:
				setTitle("修改密码");
				break;
			case 3:
				setTitle("找回密码");
				break;
		}

		findViewById(R.id.temp1).setVisibility(type==1? View.VISIBLE:View.GONE);
		findViewById(R.id.temp2).setVisibility(type==1?  View.VISIBLE:View.GONE);
		findViewById(R.id.temp3).setVisibility(type==1?  View.VISIBLE:View.GONE);

		btn_submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (checkInput()){
					checkCode();
				 }
			}
		});


		btn_getCode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				getSmsCode();
			}
		});


		findViewById(R.id.temp1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Bundle bundle=new Bundle();
				bundle.putString("url",HttpMethod.BASE_URL+"useragreement");
				doActivity(H5Activity.class,bundle);
			}
		});

		time = new TimeCount(60000, 1000);
	}


	public void getSmsCode(){
		if (getPhone().length()==11){
			Map<String, Object> payMap = new HashMap<String, Object>();
			payMap.put("mobile", getPhone());
			payMap.put("state", type+"");
			SendSmsRequest request=new SendSmsRequest();
			request.setMobile(getPhone());

			if (type==2){
				request.setUid(CacheData.getInstance().getUserData().getUid());
				payMap.put("uid",request.getUid());
			}

			request.setSign(MerchantApiUtil.getSign(payMap,Constant.MessageKey));
			request.setState(type+"");//1注册  2修改 3找回密码
			Subscriber<BaseResponse> subscriber=new Subscriber<BaseResponse>() {
				@Override
				public void onCompleted() {

				}

				@Override
				public void onError(Throwable e) {

				}

				@Override
				public void onNext(BaseResponse baseResponse) {
					if (baseResponse!=null){
						showToast(baseResponse.getMsg());
						if (baseResponse.getCode()==10000){
							time.start();
						}
					}else {
						showToast(Constant.Service_Err);
					}
				}
			};
			HttpMethod.getInstance().getSendsms(subscriber,request);
		}else {
			showToast("请检查手机号");
		}
	}

	public boolean checkInput(){
		if (TextUtils.isEmpty(getPhone())){
			showToast("手机号不能为空");
			return false;
		}else if (getPhone().length()!=11){
			showToast("请检查手机格式");
			return false;
		}else if (TextUtils.isEmpty(getCode())){
			showToast("请输入验证码");
			return false;
		}else if (TextUtils.isEmpty(getPwd())){
			showToast("密码不能为空");
			return false;
		}else if (getPwd().length()<6){
			showToast("密码最少6位");
		}else if(getRePwd().length()<6||!getPwd().equals(getRePwd())){
			showToast("两次密码输入不一致");
			return false;
		}
		return true;
	}


	public void checkCode(){
		showDialog(null);
		RegisterRequest request=new RegisterRequest();
		request.setCode(getCode());
		request.setMobile(getPhone());
		Subscriber<BaseResponse> subscriber=new Subscriber<BaseResponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(BaseResponse baseResponse) {
				if (baseResponse!=null){
					if (baseResponse.getCode()==10000){
						submit();
					}else {
						showToast(baseResponse.getMsg());
						closeDialog();
					}
				}else {
					showToast(Constant.Service_Err);
					closeDialog();
				}
			}
		};
		HttpMethod.getInstance().getCheckCode(subscriber,request);
	}

	public void submit(){
		//注册或者忘记密码
		RegisterRequest request=new RegisterRequest();
		if (type==1){
			request.setMobile(getPhone());
			request.setPassword(getPwd());
		}else if (type==2){
			request.setUid(CacheData.getInstance().getUserData().getUid());
			request.setPwd(getPwd());
		}else if (type==3){
			request.setMobile(getPhone());
			request.setPwd(getPwd());
		}

		Subscriber<LoginResponse> subscriber=new Subscriber<LoginResponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				closeDialog();
			}

			@Override
			public void onNext(LoginResponse loginResponse) {
				closeDialog();
				if (loginResponse!=null){
					if (loginResponse.getCode()==10000){
						if (type==1){
							//注册
							CacheData.getInstance().cacheUserData(loginResponse.getObj());
							ActivityManager.getInstance().finishAllActivityEx(MainActivity.class);
							Intent intent = new Intent("com.qxhd.toast");
							intent.putExtra("action",1);
							sendBroadcast(intent);
						}else if (type==2){
							//修改密码 重新登录
							CacheData.getInstance().clearUserData();
							ActivityManager.getInstance().finishAllActivityEx(MainActivity.class);
							goToLogin();
							finish();
						}else if (type==3){
							//忘记密码
							CacheData.getInstance().clearUserData();
							ActivityManager.getInstance().finishAllActivityEx(MainActivity.class);
							goToLogin();
						}
					}else {
						showToast(loginResponse.getMsg());
					}
				}else {
					showToast(Constant.Service_Err);
				}
			}
		};


		if (type==1){
			HttpMethod.getInstance().getRegister(subscriber,request);
		}else {
			HttpMethod.getInstance().getUpdatePwd(subscriber,request);
		}

	}


	public String getPhone(){
		return ed_phone.getText().toString();
	}

	public String getCode(){
		return ed_code.getText().toString();
	}

	public String getPwd(){
		return  ed_pwd.getText().toString();
	}

	public String getRePwd(){
		return  ed_repwd.getText().toString();
	}

	public void goToLogin() {
		EventBus.getDefault().post(new MessageEvent(102));
		doActivityForResult(LoginActivity.class,null,100);
		overridePendingTransition(R.anim.login_in_anim, R.anim.activity_nor_anim);
	}


	// 计时器工具类
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {// 计时完毕
			btn_getCode.setText("重新发送");
			btn_getCode.setClickable(true);

		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程
			btn_getCode.setClickable(false);// 防止重复点击
			btn_getCode.setText(millisUntilFinished / 1000 + "s后重发");
		}

	}

}
