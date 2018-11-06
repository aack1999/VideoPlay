package com.aution.paidd.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.core.base.BaseActivity;
import com.framework.core.common.AppSetting;
import com.framework.core.tools.ACache;
import com.framework.core.utils.CheckUtils;
import com.framework.core.utils.DisplayUtils;
import com.framework.core.utils.TextSpanUtils;
import com.framework.core.widget.FlatButton;
import com.aution.paidd.R;
import com.aution.paidd.bean.PayCenterItem;
import com.aution.paidd.bean.UserBean;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.CommonTools;
import com.aution.paidd.common.Constant;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.common.MD5Util;
import com.aution.paidd.common.MerchantApiUtil;
import com.aution.paidd.request.PayListRequest;
import com.aution.paidd.request.RechargeRequest;
import com.aution.paidd.request.ShopDetailRequest;
import com.aution.paidd.response.PayCenterResponse;
import com.aution.paidd.response.PaySystem;
import com.aution.paidd.response.ShopDetialResponse;
import com.aution.paidd.utils.ViewFactoryUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import me.xiaopan.sketch.SketchImageView;
import rx.Subscriber;

/**
 * Created by quxiang on 2017/9/4.
 */

public class RechargeActivity extends BaseActivity implements View.OnClickListener{

	@BindView(R.id.btn_submit)
	FlatButton btn_submit;
	@BindView(R.id.img)
	SketchImageView img;
	@BindView(R.id.tv_price)
	TextView tv_price;
	@BindView(R.id.tv_pay_money)
	TextView tv_pay_money;
	@BindView(R.id.tv_buycount)
	TextView tv_buycount;
	@BindView(R.id.tv_pay_count)
	TextView tv_pay_count;

	/**
	 * 支付视图
	 */
	@BindView(R.id.pay_layout)
	LinearLayout payLayout;

	List<CheckBox> payCheckList;//支付CheckBox

	String payWayCode;

	UserBean userBean;

	PayCenterItem payObjBeen;

	float moneyCount = 0;//选择金额,0=未选择,-1=输入的金额,其他=金额

	//支付宝相关变量
	private String sign;

	@BindView(R.id.tv_tips)
	TextView tv_tips;
	@BindView(R.id.pay_center_5)
	TextView pay_center_5;
	@BindView(R.id.pay_center_8)
	TextView pay_center_8;
	@BindView(R.id.pay_center_10)
	TextView pay_center_10;

	String cid;


	@Override
	public int getContentLayoutID() {
		return R.layout.activity_recharge;
	}

	@Override
	public void initValue() {
		cid=getIntent().getStringExtra("cid");
		getData();
		userBean=CacheData.getInstance().getUserData();
		payCheckList=new ArrayList<>();
		pay_center_5.setOnClickListener(this);
		pay_center_8.setOnClickListener(this);
		pay_center_10.setOnClickListener(this);
		setTitle("不中包赔");
		getPayList();

		findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (checkInput())
					payToSubmit();
			}
		});
		onClick(pay_center_5);
		findViewById(R.id.temp1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Bundle bundle=new Bundle();
				bundle.putString("url",HttpMethod.BASE_URL+"useragreement");
				doActivity(H5Activity.class,bundle);
			}
		});
	}


	public void getData() {
		Subscriber<ShopDetialResponse> subscriber = new Subscriber<ShopDetialResponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(ShopDetialResponse baseResponse) {
				if (baseResponse != null) {
					if (baseResponse.getCode() == 10000) {
						if (baseResponse.getObj() != null) {
							String temp=baseResponse.getObj().getInfo().getImges();
							if (!TextUtils.isEmpty(temp)){
								img.displayImage(baseResponse.getMsg()+temp.split(";")[0]);
							}

							if (baseResponse.getObj().getInfo()!=null){
								String str1="市场价：￥"+baseResponse.getObj().getInfo().getPrice();
								SpannableString ss = new SpannableString(str1);
								ss.setSpan(new StrikethroughSpan(), 4, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								tv_price.setText(ss);

								String str2="当前竞拍价：￥"+baseResponse.getObj().getInfo().getNowprice();
								tv_buycount.setText(TextSpanUtils.createColorText(str2,6,str2.length(), getResources().getColor(R.color.theme_color)));

							}

							if (baseResponse.getObj().getLbid()!=null){
								String str3="竞拍人："+baseResponse.getObj().getLbid().getNickname();
								tv_pay_money.setText(str3);
							}


						}
					}
				}
			}
		};
		ShopDetailRequest request = new ShopDetailRequest();
		request.setId(cid);
		request.setView("1");
		request.setUid(CacheData.getInstance().getUserData().getUid());
		HttpMethod.getInstance().getGoodsDetail(subscriber, request);
	}

	public void payToSubmit() {
		boolean isPayIng = false;
		if (checkInput()) {

			if (5 > moneyCount) {
				showToast("充值金额最少" + 5 + "元起");
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

	private void doPay() {
		ACache.get(AppSetting.getContext()).put("payWayCode", payWayCode);
		final RechargeRequest request = new RechargeRequest();
		request.setUid(CacheData.getInstance().getUserData().getUid());
		request.setAmount(getPayMoneyCount() + "");
		request.setPaytype(payWayCode);
		String kk="amount="+request.getAmount()+"&appid="+request.getAppid()+"&paytype="+request.getPaytype()+"&system="+request.getSystem()+"&uid="+request.getUid()+"&paySecret="+CacheData.getInstance().getPayKey();
		String sign= MD5Util.encode(kk).toUpperCase();
		request.setSign(sign);

		Subscriber<PayCenterResponse> subscriber = new Subscriber<PayCenterResponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				showToast(Constant.Service_Err);
			}

			@Override
			public void onNext(PayCenterResponse response) {
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
									String url=HttpMethod.BASE_URL+"service/pay/h5pay?orderinfo="+payObjBeen.getTradeNO()+"&system=ANDROID";
									Intent intent = new Intent();
									intent.setAction("android.intent.action.VIEW");
									Uri content_url = Uri.parse(url);
									intent.setData(content_url);
									startActivity(intent);
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

	private void initMap() {
		Map<String, Object> payMap = new HashMap<String, Object>();
		payMap.put("appid", CacheData.getInstance().getAppid());
		payMap.put("system", "ANDROID");
		payMap.put("paytype", payWayCode);
		payMap.put("uid", CacheData.getInstance().getUserData().getUid() + "");
		payMap.put("amount", getPayMoneyCount() + "");

		sign = MerchantApiUtil.getSign(payMap, CacheData.getInstance().getPayKey());
	}

	public float getPayMoneyCount() {
		return moneyCount;
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
								LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(-1, DisplayUtils.dip2px(RechargeActivity.this, 48));
								payLayout.addView(view, ll);
								if (i!=popuBean.getObj().length-1){
									payLayout.addView(ViewFactoryUtils.getLineView(RechargeActivity.this, 0));
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
			case R.id.pay_center_5:
				moneyCount = 5;
				selectPayCenter();
				view.setSelected(true);
				break;
			case R.id.pay_center_8:
				moneyCount=8;
				selectPayCenter();
				view.setSelected(true);
				break;
			case R.id.pay_center_10:
				moneyCount=10;
				selectPayCenter();
				view.setSelected(true);
				break;

		}
	}

	public void selectPayCenter(){
		pay_center_5.setSelected(false);
		pay_center_8.setSelected(false);
		pay_center_10.setSelected(false);
		int temp=2;
		switch ((int)moneyCount){
			case 5:
				temp=2;
				break;
			case 8:
				temp=5;
				break;
			case 10:
				temp=8;
				break;
		}
		tv_tips.setText("当前选择了"+(int)moneyCount+"连拍，预计获得"+(int)moneyCount+"拍币，若仍然未拍中商品，将退还"+temp+"拍币");
		tv_pay_count.setText("实付价格：￥" + moneyCount);
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

	public boolean checkInput() {
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

}
