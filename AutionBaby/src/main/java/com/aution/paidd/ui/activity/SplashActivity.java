package com.aution.paidd.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.framework.core.base.BaseActivity;
import com.framework.core.common.AppSetting;
import com.framework.core.utils.DisplayUtils;
import com.framework.core.widget.FlatButton;
import com.aution.paidd.R;
import com.aution.paidd.bean.UserBean;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.greendao.gen.DaoMaster;
import com.aution.paidd.greendao.gen.DaoSession;
import com.aution.paidd.greendao.gen.MySQLiteOpenHelper;
import com.aution.paidd.request.InitLoginRequest;
import com.aution.paidd.response.AdvsResponse;
import com.aution.paidd.response.LoginResponse;
import com.umeng.analytics.MobclickAgent;

import me.xiaopan.sketch.SketchImageView;
import me.xiaopan.sketch.request.DisplayOptions;
import rx.Subscriber;

/**
 * Created by quxiang on 2017/8/31.
 */

public class SplashActivity extends BaseActivity {

	SketchImageView img;
	FlatButton btn_counttime;

	Handler handler=new Handler();

	private Runnable mLoadingRunnable = new Runnable() {
		@Override
		public void run() {
			initSqlite();
			getPermission();
			getAdvsData();
			btn_counttime.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					startActivity(new Intent(SplashActivity.this,MainActivity.class));
					finish();
				}
			});
		}
	};


	@Override
	public int getContentLayoutID() {
		return 0;
	}

	@Override
	public void initValue() {

	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		img=(SketchImageView)findViewById(R.id.img);
		btn_counttime=(FlatButton)findViewById(R.id.btn_counttime);
		getWindow().getDecorView().post(new Runnable() {
			@Override
			public void run() {
				handler.post(mLoadingRunnable);
			}
		});

		Log.e("dd",Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_DOWNLOADS).getPath());

	}

	public void initSqlite(){

		AppSetting.setContext(this);

		//数据库初始化
		MySQLiteOpenHelper mHelper= new MySQLiteOpenHelper(getApplicationContext(), "qxhd-data-db", null);
		SQLiteDatabase db= mHelper.getWritableDatabase();
		DaoMaster mDaoMaster = new DaoMaster(db);
		DaoSession mDaoSession = mDaoMaster.newSession();
		CacheData.getInstance().setDaoSession(mDaoSession);

		//配置初始化
		try {
			ApplicationInfo appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
			String APPID=appInfo.metaData.getString("APPID");
			String UMENGKEY=appInfo.metaData.getString("UMENGKEY");
			String PAYKEY=appInfo.metaData.getString("PAYKEY");
			//友盟初始化

			String appid=APPID.split("_")[1];

			MobclickAgent. startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this, UMENGKEY, appid));
			CacheData.getInstance().setAPPID(appid);
			CacheData.getInstance().setUMENGKEY(UMENGKEY);
			CacheData.getInstance().setPAYKEY(PAYKEY);

		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void initLogin() {
		UserBean bean = CacheData.getInstance().getUserData();
		InitLoginRequest request = new InitLoginRequest();
		if (!TextUtils.isEmpty(bean.getUid())) {
			request.setUid(bean.getUid());
		}
		if (!TextUtils.isEmpty(bean.getOpenid()))
		{
			request.setOpenid(bean.getOpenid());
		}
		request.setPwd(bean.getPwd());
		request.setImei(CacheData.getInstance().getImei());
		Subscriber<LoginResponse> subscriber = new Subscriber<LoginResponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(LoginResponse response) {
				if (response != null) {
					if (response.getCode() == 10000 && response.getObj() != null) {
						CacheData.getInstance().cacheUserData(response.getObj());
					}
				}
			}
		};
		HttpMethod.getInstance().getInitLogin(subscriber, request);
	}


	public void getAdvsData() {
		CacheData.getInstance().loadAdvsImg(img, new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (view == null) {
					DisplayOptions options=new DisplayOptions();
					options.setResize(DisplayUtils.getScreenW(),DisplayUtils.getScreenH());
					img.setOptions(options);
					img.displayResourceImage(R.drawable.splash_bg);
					return;
				}

				timer.cancel();
				timer=null;

				Intent intent=new Intent(SplashActivity.this,MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("url", (String) view.getTag());
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
		});
		Subscriber<AdvsResponse> subscriber = new Subscriber<AdvsResponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(AdvsResponse advsResponse) {
				if (advsResponse != null && advsResponse.getObj() != null) {
					CacheData.getInstance().cacheAdvsFile(advsResponse.getMsg(),advsResponse.getObj());
				}
			}
		};
		HttpMethod.getInstance().getSplashAdvs(subscriber);
	}

	public void getPermission() {
		requestPhoneStatePermission();
	}

	private void requestPhoneStatePermission() {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
				//已经有权限了
				doneThings();
			} else {
				//申请权限
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 100);
			}
		} else {
			//可以做事情了
			doneThings();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == 100) {
			if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				//权限通过了
				doneThings();
			} else {
				//权限被拒绝了
				doneThings();
			}
		} else {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	public void doneThings() {
		initLogin();
		timer.start();
	}

	private CountDownTimer timer = new CountDownTimer(5000, 1000) {

		@Override
		public void onTick(long millisUntilFinished) {
			btn_counttime.setText("跳过("+(millisUntilFinished / 1000) + "s)");
		}

		@Override
		public void onFinish() {
			//完成倒计时
			startActivity(new Intent(SplashActivity.this,MainActivity.class));
			finish();
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		timer.cancel();
		timer=null;
	}
}
