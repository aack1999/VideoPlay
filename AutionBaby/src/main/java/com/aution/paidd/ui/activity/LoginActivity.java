package com.aution.paidd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.framework.core.base.BaseActivity;
import com.framework.core.widget.FlatButton;
import com.aution.paidd.R;
import com.aution.paidd.bean.UserBean;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.Constant;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.request.AuthRequest;
import com.aution.paidd.request.LoginRequest;
import com.aution.paidd.response.LoginResponse;
import com.aution.paidd.utils.ThirdLoginUtils;

import java.util.Set;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import me.xiaopan.sketch.SketchImageView;
import rx.Subscriber;

/**
 * Created by quxiang on 2017/8/31.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{

	@BindView(R.id.ed_phone)
	EditText ed_phone;
	@BindView(R.id.ed_pwd)
	EditText ed_pwd;
	@BindView(R.id.btn_submit)
	FlatButton btn_submit;
	@BindView(R.id.img_ad)
	SketchImageView img_ad;

	@Override
	public int getContentLayoutID() {
		return R.layout.activity_login;
	}

	@Override
	public void initValue() {
		setTitle("拍多多");
		findViewById(R.id.btn_forget).setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		findViewById(R.id.qq_login).setOnClickListener(this);
		findViewById(R.id.wei_xin_login).setOnClickListener(this);

		img_ad.displayResourceImage(R.drawable.ic_login_ad);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.btn_forget:
				Bundle bundle=new Bundle();
				bundle.putInt("type",3);
				doActivity(ChangePassWordActivity.class,bundle);
				break;
			case R.id.btn_submit:
				if (checkInput()){
					submit();
				}
				break;
			case R.id.qq_login:
				new ThirdLoginUtils(view.getContext()).loginToQQ(new ThirdLoginUtils.OnLoginListener() {
					@Override
					public void onSuccess(UserBean userBean) {
						upUserInfo(3, userBean);
					}
				});
				break;
			case R.id.wei_xin_login:
				new ThirdLoginUtils(view.getContext()).loginToWX(new ThirdLoginUtils.OnLoginListener() {
					@Override
					public void onSuccess(UserBean userBean) {
						upUserInfo(2, userBean);
					}
				});
				break;
		}
	}


	public void upUserInfo(int type,final UserBean ub){
		final AuthRequest request=new AuthRequest();
		request.setOpenid(ub.getOpenid());
		request.setNickname(ub.getNickname());
		request.setImei(CacheData.getInstance().getImei());
		request.setImg(ub.getImg());
		request.setSex(ub.getSex()+"");
		request.setAt(type+"");
		Subscriber<LoginResponse> subscriber=new Subscriber<LoginResponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(LoginResponse response) {
				if (response!=null&&response.getObj()!=null){
					CacheData.getInstance().cacheLogoFile("");
					CacheData.getInstance().cacheUserData(response.getObj());
					JPushInterface.setAlias(LoginActivity.this, response.getObj().getUid()+"", new TagAliasCallback() {
						@Override
						public void gotResult(int i, String s, Set<String> set) {

						}
					});
					if (response.getObj().getFlag()==0){
						Intent intent = new Intent("com.qxhd.toast");
						intent.putExtra("action",1);
						sendBroadcast(intent);
					}
					finish(200);
				}else {
					showToast("服务器异常,请重试");
				}
			}
		};
		HttpMethod.getInstance().getAuthorization(subscriber,request);
	}

	public boolean checkInput(){
		if (getPhone().length()!=11){
			showToast("请检查手机格式");
			return false;
		}

		if (getPwd().length()<6){
			showToast("密码最少6位");
			return false;
		}

		return true;
	}

	public void submit(){
		LoginRequest request=new LoginRequest();
		request.setPwd(getPwd());
		request.setAccount(getPhone());
		Subscriber<LoginResponse> subscriber=new Subscriber<LoginResponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(LoginResponse loginResponse) {
				if (loginResponse!=null){
					if (loginResponse.getCode()==10000){
						CacheData.getInstance().cacheLogoFile("");
						CacheData.getInstance().cacheUserData(loginResponse.getObj());
						JPushInterface.setAlias(LoginActivity.this, CacheData.getInstance().getUserData().getUid()+"", new TagAliasCallback() {
							@Override
							public void gotResult(int i, String s, Set<String> set) {

							}
						});
						finish(200);
					}else {
						showToast(loginResponse.getMsg());
					}
				}else {
					showToast(Constant.Service_Err);
				}
			}
		};
		HttpMethod.getInstance().getLogin(subscriber,request);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_login,menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()){
			case R.id.menu_register:
				Bundle bundle=new Bundle();
				bundle.putInt("type",1);
				doActivity(ChangePassWordActivity.class,bundle);
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public String getPhone(){
		return ed_phone.getText().toString();
	}

	public String getPwd(){
		return  ed_pwd.getText().toString();
	}


	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.activity_nor_anim, R.anim.login_out_anim);
	}

	public void finish(int code) {
		setResult(code);
		finish();
	}
}
