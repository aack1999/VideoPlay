package com.aution.paidd.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.framework.core.base.ActivityManager;
import com.framework.core.base.BaseFragment;
import com.framework.core.common.AppSetting;
import com.aution.paidd.R;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.model.MessageEvent;
import com.aution.paidd.ui.activity.H5Activity;
import com.aution.paidd.ui.activity.LoginActivity;
import com.aution.paidd.ui.activity.LuckyShowActivity;
import com.aution.paidd.ui.activity.MainActivity;
import com.aution.paidd.ui.activity.PayCenterActivity;
import com.aution.paidd.ui.activity.ShopDetailActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created by quxiang on 2017/8/30.
 */

public class FindFragment extends BaseFragment {

	@BindView(R.id.webView)
	WebView webView;

	String url;

	@Override
	public int getContentLayoutID() {
		return R.layout.fragment_find;
	}

	@Override
	public void initValue() {
		url= HttpMethod.BASE_URL+"finds?system=ANDROID&uid="+ CacheData.getInstance().getUserData().getUid();
		webView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				return true;
			}
		});
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

		// 开启DOM缓存。
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setDatabaseEnabled(true);
		webView.getSettings().setDatabasePath(getActivity().getCacheDir().getAbsolutePath());

		webView.setWebViewClient(webViewClient);
		webView.addJavascriptInterface(new AndroidH5Object(), "android");
		webView.loadUrl(url);
	}


	WebViewClient webViewClient = new WebViewClient() {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Bundle bundle=new Bundle();
			bundle.putString("fullurl",url);
			doActivity(H5Activity.class,bundle);
			return true;
		}

	};


	public class AndroidH5Object {

		public AndroidH5Object() {

		}

		@JavascriptInterface
		public void goodsdetailed(String id,String view) {
			//商品详情
			Intent intent = new Intent();
			intent.setClass(AppSetting.getContext(), ShopDetailActivity.class);
			intent.putExtra("id", id);
			intent.putExtra("view", view);
			startActivity(intent);
		}

		@JavascriptInterface
		public void h5register() {
			//注册
			doActivity(LoginActivity.class);
		}

		@JavascriptInterface
		public void recharge() {
			//充值
			if (CacheData.getInstance().isLogin()){
				doActivity(PayCenterActivity.class);
			}else {
				doActivity(LoginActivity.class);
			}
		}

		@JavascriptInterface
		public void index() {
			//到首页
			ActivityManager.getInstance().finishAllActivityEx(MainActivity.class);
			EventBus.getDefault().post(new MessageEvent(145));
		}

		@JavascriptInterface
		public void share(){
			doActivity(LuckyShowActivity.class);
		}


	}

	@Override
	protected void onFirstUserVisible() {

	}

	@Override
	protected View getLoadingTargetView() {
		return null;
	}
}
