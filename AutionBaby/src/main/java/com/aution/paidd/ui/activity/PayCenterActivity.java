package com.aution.paidd.ui.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.core.base.BaseActivity;
import com.framework.core.common.AppSetting;
import com.framework.core.tools.ACache;
import com.framework.core.utils.CheckUtils;
import com.framework.core.utils.DisplayUtils;
import com.framework.core.widget.FlatButton;
import com.aution.paidd.R;
import com.aution.paidd.bean.PayCenterItem;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.CommonTools;
import com.aution.paidd.common.Constant;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.common.MD5Util;
import com.aution.paidd.common.MerchantApiUtil;
import com.aution.paidd.common.OnPayStateListener;
import com.aution.paidd.common.OtherPayTools;
import com.aution.paidd.model.MessageEvent;
import com.aution.paidd.request.PayListRequest;
import com.aution.paidd.request.RechargeRequest;
import com.aution.paidd.response.PayCenterResponse;
import com.aution.paidd.response.PaySystem;
import com.aution.paidd.utils.BitmapUtils;
import com.aution.paidd.utils.ViewFactoryUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import pay.Pay;
import rx.Subscriber;


/**
 * 充值中心
 * Created by quxiang on 2016/12/12.
 */
public class PayCenterActivity extends BaseActivity implements View.OnClickListener,OnPayStateListener {

	@BindView(R.id.pay_center_50)
	TextView pay_center_50;
	@BindView(R.id.pay_center_100)
	TextView pay_center_100;
	@BindView(R.id.pay_center_200)
	TextView pay_center_200;
	@BindView(R.id.pay_center_300)
	TextView pay_center_300;
	@BindView(R.id.pay_center_500)
	TextView pay_center_500;
	@BindView(R.id.pay_center_edit)
	EditText pay_center_edit;


	@BindView(R.id.tv_pay_money)
	TextView tv_pay_money;

	@BindView(R.id.btn_help)
	ImageView btn_help;

	List<CheckBox> payCheckList;//支付CheckBox


	String payWayCode;

	int moneyCount = 0;//选择金额,0=未选择,-1=输入的金额,其他=金额

	int minMoneyCount=5;

	//支付宝相关变量
	private String sign;

	@BindView(R.id.submit)
	FlatButton submit;

	/**
	 * 支付视图
	 */
	@BindView(R.id.pay_layout)
	LinearLayout payLayout;


	@BindView(R.id.pay_money_title)
	TextView pay_money_title;

	@Override
	public int getContentLayoutID() {
		return R.layout.activity_pay_center;
	}

	public void initValue() {
		setTitle("充值");
		payCheckList = new ArrayList<>();
		pay_center_50.setOnClickListener(this);
		pay_center_100.setOnClickListener(this);
		pay_center_200.setOnClickListener(this);
		pay_center_300.setOnClickListener(this);
		pay_center_500.setOnClickListener(this);
		pay_center_edit.setOnClickListener(this);

		findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				payToSubmit();
			}
		});
		pay_center_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
				if (i == EditorInfo.IME_ACTION_DONE) {
					//刷新价格
					if (TextUtils.isEmpty(pay_center_edit.getText().toString())) {
						moneyCount = 0;
						upMoneyUI();
						return true;
					}
					moneyCount = Integer.parseInt(pay_center_edit.getText().toString());
					upMoneyUI();
					return false;
				}
				return false;
			}
		});
		pay_center_edit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				if (TextUtils.isEmpty(pay_center_edit.getText().toString())) {
					moneyCount = 0;
					upMoneyUI();
					return;
				}
				moneyCount = Integer.parseInt(pay_center_edit.getText().toString());
				upMoneyUI();
			}
		});

		findViewById(R.id.btn_help).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doActivity(PayListActivity.class);
			}
		});

		onClick(pay_center_50);
		getPayList();

		btn_help.setImageDrawable(BitmapUtils.tintDrawable(getResources().getDrawable(R.drawable.ic_czjl), ColorStateList.valueOf(getResources().getColor(R.color.base_text_title))));

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.pay_alipay:
				selectPayType(R.id.pay_check_alipay);
				return;
			case R.id.pay_qq:
				selectPayType(R.id.pay_check_qq);
				return;
			case R.id.pay_weixin:
				selectPayType(R.id.pay_check_weixin);
				return;

		}
		pay_center_50.setSelected(false);
		pay_center_100.setSelected(false);
		pay_center_200.setSelected(false);
		pay_center_300.setSelected(false);
		pay_center_500.setSelected(false);
		pay_center_edit.setSelected(false);
		switch (view.getId()) {
			case R.id.pay_center_50:
				moneyCount = 50;
				break;
			case R.id.pay_center_100:
				moneyCount = 100;
				break;
			case R.id.pay_center_200:
				moneyCount = 200;
				break;
			case R.id.pay_center_300:
				moneyCount = 300;
				break;
			case R.id.pay_center_500:
				moneyCount = 500;
				break;
			case R.id.pay_center_edit:
				moneyCount = -1;
				String temp = pay_center_edit.getText().toString();
				if (!TextUtils.isEmpty(temp)) {
					moneyCount = Integer.parseInt(temp);
				}
				pay_center_edit.selectAll();
				break;
		}
		upMoneyUI();
		view.setSelected(true);
	}

	public void selectPayType(int id) {
		for (int i = 0; i < payCheckList.size(); i++) {
			CheckBox cb = payCheckList.get(i);
			cb.setChecked(false);
			if (cb.getId() == id) {
				cb.setChecked(true);
				payWayCode = (String) cb.getTag();
			}
		}
	}

	public void upMoneyUI() {
		if (moneyCount <= 0) {
			moneyCount = 0;
		}
		pay_money_title.setText(moneyCount+"拍币，"+(int)(moneyCount*0.2f)+"赠币");
		tv_pay_money.setText("￥"+moneyCount);
		submit.setText("立即支付(" + moneyCount + ")");
	}

	/**
	 * 获取支付列表
	 */
	public void getPayList() {
		PayListRequest request = new PayListRequest();
		request.setSystem("ANDROID");
		request.setVersion(CommonTools.getVersion(this));

		Subscriber<PaySystem> subscriber = new Subscriber<PaySystem>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				showToast(Constant.Service_Err);
			}

			@Override
			public void onNext(PaySystem popuBean) {
				if (popuBean != null) {


					if (popuBean.getObj()!=null){
						for (int i = 0; i < popuBean.getObj().length; i++) {
							View view = createPayTypeView(popuBean.getObj()[i]);
							if (view != null) {
								LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(-1, DisplayUtils.dip2px(PayCenterActivity.this, 48));
								payLayout.addView(view, ll);
								if (i!=popuBean.getObj().length-1){
									payLayout.addView(ViewFactoryUtils.getLineView(PayCenterActivity.this, 0));
								}
							}
						}
					}
					payWayCode = ACache.get(AppSetting.getContext()).getAsString("payWayCode");
					View view = new View(payLayout.getContext());
					if (!TextUtils.isEmpty(payWayCode)) {
						switch (payWayCode) {
							case "ALIPAY":
								view.setId(R.id.pay_alipay);
								break;
							case "WEIXIN":
								view.setId(R.id.pay_weixin);
								break;
							case "QQPAY":
								view.setId(R.id.pay_qq);
								break;
						}
					} else {
						//默认选择支付宝
						view.setId(R.id.pay_alipay);
					}
					onClick(view);
				}
			}
		};

		HttpMethod.getInstance().getPayList(subscriber, request);
	}

	public View createPayTypeView(String type) {
		switch (type) {
			case "ALIPAY":
				return initAliPayView("ALIPAY");
			case "ALIPAYBF":
				return initAliPayView("ALIPAYBF");
			case "WEIXIN":
				return initWXView("WEIXIN");
			case "WEIXINBF":
				return initWXView("WEIXINBF");
			case "QQPAY":
				return initQQView("QQPAY");
		}
		return null;
	}

	public View initAliPayView(String tag) {
		View view = getLayoutInflater().inflate(R.layout.activity_pay_alitype_item, null);
		CheckBox pay_check_alipay = getView(view, R.id.pay_check_alipay);
		pay_check_alipay.setTag(tag);
		payCheckList.add(pay_check_alipay);
		getView(view, R.id.pay_alipay).setOnClickListener(this);
		return view;
	}

	public View initQQView(String tag) {
		View view = getLayoutInflater().inflate(R.layout.activity_pay_qqtype_item, null);
		CheckBox pay_check_qq = getView(view, R.id.pay_check_qq);
		pay_check_qq.setTag(tag);
		payCheckList.add(pay_check_qq);
		getView(view, R.id.pay_qq).setOnClickListener(this);
		return view;
	}

	public View initWXView(String tag) {
		View view = getLayoutInflater().inflate(R.layout.activity_pay_wxtype_item, null);
		CheckBox pay_check_weixin = getView(view, R.id.pay_check_weixin);
		pay_check_weixin.setTag(tag);
		payCheckList.add(pay_check_weixin);
		getView(view, R.id.pay_weixin).setOnClickListener(this);
		return view;
	}



	public void payToSubmit() {
		boolean isPayIng = false;
		if (checkInput()) {

			if (minMoneyCount > moneyCount) {
				showToast("充值金额最少" + minMoneyCount + "元起");
				return;
			}

			for (int i = 0; i < payCheckList.size(); i++) {
				CheckBox cb = payCheckList.get(i);
				if (cb.isChecked()) {
					switch (cb.getId()) {
						case R.id.pay_check_alipay:
							if (isPayIng) {
								return;
							} else {
								isPayIng = true;
							}
							//支付宝支付
							initMap();
							doPay();
							break;
						case R.id.pay_check_qq:
							//qq支付
							initMap();
							doPay();
							break;
						case R.id.pay_check_weixin:
							//微信支付
							initMap();
							doPay();
							break;

					}
				}
			}
		}
	}

	public boolean checkInput() {
		if (moneyCount <= 0) {
			showToast("充值金额最少1块");
			return false;
		}
		if (moneyCount > 5000) {
			showToast("充值金额最多5000块");
			return false;
		}
		for (int i = 0; i < payCheckList.size(); i++) {
			CheckBox cb = payCheckList.get(i);
			if (cb.isChecked()) {
				switch (cb.getId()) {
					case R.id.pay_check_qq:
						if (!CheckUtils.isExitQQ()) {
							showToast("请先安装QQ");
							return false;
						}
						break;
					case R.id.pay_check_weixin:
						if (!CheckUtils.isExitWeChat()) {
							showToast("请先安装微信");
							return false;
						}
						break;
				}
				return true;
			}
		}
		showToast("请选择支付方式");
		return false;
	}


	//============================================================支付相关开始

	private void initMap() {
		Map<String, Object> payMap = new HashMap<String, Object>();
		payMap.put("appid", CacheData.getInstance().getAppid());
		payMap.put("system", "ANDROID");
		payMap.put("paytype", payWayCode);
		payMap.put("uid", CacheData.getInstance().getUserData().getUid() + "");
		payMap.put("amount", getPayMoneyCount() + "");
		sign = MerchantApiUtil.getSign(payMap, CacheData.getInstance().getPayKey());
	}

	public int getPayMoneyCount() {
		return moneyCount;
	}

	PayCenterItem payObjBeen;

	private void doPay() {
		showDialog(null);
		ACache.get(AppSetting.getContext()).put("payWayCode", payWayCode);
		final RechargeRequest request = new RechargeRequest();
		request.setUid(CacheData.getInstance().getUserData().getUid());
		request.setAmount(getPayMoneyCount() + "");
		request.setPaytype(payWayCode);
//		String kk="amount="+request.getAmount()+"&appid="+request.getAppid()+"&paytype="+request.getPaytype()+"&system="+request.getSystem()+"&uid="+request.getUid()+"&paySecret="+CacheData.getInstance().getPayKey();
//		String sign= MD5Util.encode(kk).toUpperCase();
		request.setSign(sign);

		Subscriber<PayCenterResponse> subscriber = new Subscriber<PayCenterResponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				closeDialog();
				showToast(Constant.Service_Err);
			}

			@Override
			public void onNext(PayCenterResponse response) {
				closeDialog();
				if (response!=null&&response.getObj()!=null){
					if (response.getCode()==10000){
						payObjBeen=response.getObj();
						switch (payWayCode) {
							case "ALIPAY":
								//支付宝
								if ("PAYBF".equals(response.getObj().getCompany())){
									String url=HttpMethod.BASE_URL+"service/pay/h5pay?orderinfo="+payObjBeen.getTradeNO()+"&system=ANDROID";
									Intent intent = new Intent();
									intent.setAction("android.intent.action.VIEW");
									Uri content_url = Uri.parse(url);
									intent.setData(content_url);
									startActivity(intent);
								}
								break;
							case "QQPAY":
								if ("PAYBF".equals(response.getObj().getCompany())){
									String url=HttpMethod.BASE_URL+"service/pay/h5pay?orderinfo="+payObjBeen.getTradeNO()+"&system=ANDROID";
									Intent intent = new Intent();
									intent.setAction("android.intent.action.VIEW");
									Uri content_url = Uri.parse(url);
									intent.setData(content_url);
									startActivity(intent);
								}
								break;
							case "WEIXIN":
								if ("PAYBF".equals(response.getObj().getCompany())){
//									String url=HttpMethod.BASE_URL+"service/pay/h5pay?orderinfo="+payObjBeen.getTradeNO()+"&system=ANDROID";
//									Intent intent = new Intent();
//									intent.setAction("android.intent.action.VIEW");
//									Uri content_url = Uri.parse(url);
//									intent.setData(content_url);
//									startActivity(intent);

									new Thread(
											new Runnable() {
												@Override
												public void run() {
													Pay.startPay(PayCenterActivity.this, payObjBeen.getPayobj());
												}
											}
									).start();
								}

								break;
						}
					}else {
						showToast(response.getMsg());
					}
				}
			}

		};
		HttpMethod.getInstance().getToRecharge(subscriber, request);
	}



	@Override
	public void onPaySuccess() {
		savePayEvent(true);
		payObjBeen = null;
		//刷新用户信息
		EventBus.getDefault().post(new MessageEvent(300));
		finish();
	}

	@Override
	public void onPayFail() {
		savePayEvent(false);
		payObjBeen = null;
	}


	public void savePayEvent(boolean isSuccess) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("充值金额", moneyCount + "");
		map.put("支付状态", isSuccess ? "成功" : "失败");
		MobclickAgent.onEvent(this, "submit_order", map);
	}


	public String getPayType() {
		switch (payWayCode) {
			case "QQPAY":
				return "QQ支付";
			case "WEIXIN":
				return "微信支付";
			case "ALIPAY":
				return "支付宝支付";
			case "BALANCE":
				return "余额支付";
			case "H5":
				return "H5支付";
			case "SCAN":
				return "微信支付(扫码)";
		}
		return "未知支付";
	}

	@Override
	public void onBackPressed() {
		setResult(300);
		super.onBackPressed();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (payObjBeen != null) {
			OtherPayTools.queryPayState(this, payObjBeen.getTradeNO(), this);
		}

	}
}
