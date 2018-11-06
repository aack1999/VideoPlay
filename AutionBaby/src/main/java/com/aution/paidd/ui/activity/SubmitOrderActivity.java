package com.aution.paidd.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.core.base.BaseActivity;
import com.framework.core.common.AppSetting;
import com.framework.core.tools.ACache;
import com.framework.core.utils.CheckUtils;
import com.framework.core.utils.DisplayUtils;
import com.framework.core.utils.ParseUtils;
import com.framework.core.widget.FlatButton;
import com.aution.paidd.R;
import com.aution.paidd.bean.LuckyListBean;
import com.aution.paidd.bean.MyAddressObJ;
import com.aution.paidd.bean.OrderInfoBean;
import com.aution.paidd.bean.PayInitOrderItem;
import com.aution.paidd.bean.ShopRecordBean;
import com.aution.paidd.bean.UserBean;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.CommonTools;
import com.aution.paidd.common.Constant;
import com.aution.paidd.common.DecimalUtil;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.common.OnPayStateListener;
import com.aution.paidd.common.OtherPayTools;
import com.aution.paidd.model.MessageEvent;
import com.aution.paidd.request.BaseIdRequest;
import com.aution.paidd.request.GoodsBuyRequest;
import com.aution.paidd.request.PayListRequest;
import com.aution.paidd.response.DefaultAddressResponse;
import com.aution.paidd.response.PayInitOrderResponse;
import com.aution.paidd.response.PaySystem;
import com.aution.paidd.utils.ViewFactoryUtils;
import com.switfpass.pay.utils.MD5;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.codec.binary.Base64;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import me.xiaopan.sketch.SketchImageView;
import pay.Pay;
import rx.Subscriber;

/**
 * Created by quxiang on 2017/9/4.
 */

public class SubmitOrderActivity extends BaseActivity implements View.OnClickListener, OnPayStateListener {

	@BindView(R.id.btn_submit)
	FlatButton btn_submit;
	@BindView(R.id.tv_content)
	TextView tv_content;
	@BindView(R.id.img)
	SketchImageView img;
	@BindView(R.id.tv_time)
	TextView tv_time;
	@BindView(R.id.tv_price)
	TextView tv_price;
	@BindView(R.id.tv_pay_money)
	TextView tv_pay_money;
	@BindView(R.id.tv_buycount)
	TextView tv_buycount;
	@BindView(R.id.tv_pay_count)
	TextView tv_pay_count;

	@BindView(R.id.ed_content)
	EditText ed_content;

	/**
	 * 支付视图
	 */
	@BindView(R.id.pay_layout)
	LinearLayout payLayout;

	List<CheckBox> payCheckList;//支付CheckBox

	String payWayCode;

	ShopRecordBean model1;
	LuckyListBean model2;

	MyAddressObJ addressObJ;

	UserBean userBean;

	float moneyCount = 0;//选择金额,0=未选择,-1=输入的金额,其他=金额

	int buycount;//消耗的购币

	PayInitOrderItem payObjBeen;


	@Override
	public int getContentLayoutID() {
		return R.layout.activity_submit_order;
	}

	@Override
	public void initValue() {
		userBean = CacheData.getInstance().getUserData();
		payCheckList = new ArrayList<>();
		setTitle("确认订单");
		if (getIntent().getSerializableExtra("model") instanceof ShopRecordBean) {
			model1 = (ShopRecordBean) getIntent().getSerializableExtra("model");
			img.displayImage(getIntent().getStringExtra("baseImgUrl") + model1.getHeadimage());
			tv_time.setText(model1.getCreatetime());
			tv_price.setText("市场价：￥" + model1.getPrice());

			float nowprice = Float.parseFloat(model1.getPrice());
			int buymoney = CacheData.getInstance().getUserData().getBuymoney();

			float kk = (int) nowprice - buymoney;
			buycount = (kk <= 0 ? (int) nowprice : buymoney);

			tv_buycount.setText("已使用购币：" + buycount + "个(剩余" + (userBean.getBuymoney() - buycount) + "个)");
			moneyCount = Math.abs(Float.parseFloat(DecimalUtil.subtract(buycount + "", nowprice + "")));

			if (moneyCount == 0) {
				//不用给钱了
				moneyCount = 0;
			}
			tv_pay_money.setText("实付价格：￥" + moneyCount);
			tv_pay_count.setText("实付价格：￥" + moneyCount);
		} else {

			model2 = (LuckyListBean) getIntent().getSerializableExtra("model");
			img.displayImage(getIntent().getStringExtra("baseImgUrl") + model2.getHeadimage());

			tv_time.setText(model2.getCreatetime());
			tv_price.setText("市场价：￥" + model2.getPrice());

			float nowprice2 = model2.getNowprice();
			int buymoney2 = CacheData.getInstance().getUserData().getBuymoney();
			float kk = (int) nowprice2 - buymoney2;
			buycount = (kk <= 0 ? (int) nowprice2 : buymoney2);

			tv_buycount.setText("已使用购币：" + buycount + "个(剩余" + (userBean.getBuymoney() - buycount) + "个)");

			moneyCount = Math.abs(Float.parseFloat(DecimalUtil.subtract(buycount + "", nowprice2 + "")));

			if (moneyCount == 0) {
				//不用给钱了
				moneyCount = 0;
			}
			tv_pay_money.setText("实付价格：￥" + moneyCount);
			tv_pay_count.setText("实付价格：￥" + moneyCount);
		}
		getDefalutAddress();
		getPayList();

		findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (checkInput())
					submit();
			}
		});

		findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Bundle bundle = new Bundle();
				bundle.putInt("type", 2);
				doActivityForResult(AdressListActivity.class, bundle, 100);
			}
		});

		findViewById(R.id.btn_change).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Bundle bundle = new Bundle();
				bundle.putInt("type", 2);
				bundle.putLong("id", addressObJ.getId());
				doActivityForResult(AdressListActivity.class, bundle, 100);
			}
		});
		findViewById(R.id.temp1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Bundle bundle = new Bundle();
				bundle.putString("url", HttpMethod.BASE_URL + "useragreement");
				doActivity(H5Activity.class, bundle);
			}
		});
	}


	public void submit() {

		if (addressObJ == null) {
			showToast("请选择收货地址");
			return;
		}

		showDialog(null);
		Subscriber<PayInitOrderResponse> subscriber = new Subscriber<PayInitOrderResponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				closeDialog();
			}

			@Override
			public void onNext(PayInitOrderResponse response) {
				closeDialog();
				if (response != null) {
					if (response.getCode() == 10000) {
						payObjBeen = response.getObj();
						if (payObjBeen.getStart() == 2) {
							showToast(payObjBeen.getPayobj());
							setResult(200);
							finish();
							return;
						}

						switch (payWayCode) {
							case "ALIPAY":
								//支付宝
								if ("PAYBF".equals(response.getObj().getCompany())) {
									String url = HttpMethod.BASE_URL + "service/pay/h5pay?orderinfo=" + payObjBeen.getTradeNO() + "&system=ANDROID";
									Intent intent = new Intent();
									intent.setAction("android.intent.action.VIEW");
									Uri content_url = Uri.parse(url);
									intent.setData(content_url);
									startActivity(intent);
								}
								break;
							case "QQPAY":
								if ("PAYBF".equals(response.getObj().getCompany())) {
									String url = HttpMethod.BASE_URL + "service/pay/h5pay?orderinfo=" + payObjBeen.getTradeNO() + "&system=ANDROID";
									Intent intent = new Intent();
									intent.setAction("android.intent.action.VIEW");
									Uri content_url = Uri.parse(url);
									intent.setData(content_url);
									startActivity(intent);
								}
								break;
							case "WEIXIN":
								if ("PAYBF".equals(response.getObj().getCompany())) {
//									String url = HttpMethod.BASE_URL + "service/pay/h5pay?orderinfo=" + payObjBeen.getTradeNO() + "&system=ANDROID";
//									Intent intent = new Intent();
//									intent.setAction("android.intent.action.VIEW");
//									Uri content_url = Uri.parse(url);
//									intent.setData(content_url);
//									startActivity(intent);
									new Thread(
											new Runnable() {
												@Override
												public void run() {
													Pay.startPay(SubmitOrderActivity.this, payObjBeen.getPayobj());
												}
											}
									).start();
								}
								break;
						}
					} else {
						showToast(response.getMsg());
					}
				}
			}
		};
		GoodsBuyRequest request = new GoodsBuyRequest();
		OrderInfoBean gbb = new OrderInfoBean();
		gbb.setAid(getAid());
		gbb.setUid(CacheData.getInstance().getUserData().getUid());
		gbb.setScene(getIntent().getStringExtra("scene"));
		gbb.setBuymoney(buycount + "");
		gbb.setPaytype(payWayCode);
		gbb.setAmount(moneyCount + "");
		gbb.setAddid(addressObJ.getId() + "");


		String sign = "addid=" + gbb.getAddid() + "&aid=" + gbb.getAid() + "&appid=" + gbb.getAppid() + "&scene=" + gbb.getScene() + "&system=" + gbb.getSystem() + "&uid=" + gbb.getUid() + "&secret=" + Constant.GoodsBuyKey;
		request.setSign(MD5.md5s(sign).toUpperCase());
		request.setOrderinfo(new String(Base64.encodeBase64(ParseUtils.toJson(gbb).getBytes())));
		request.setComment(ed_content.getText().toString());
		HttpMethod.getInstance().getInitOrder(subscriber, request);
	}


	public String getAid() {
		return model1 != null ? model1.getAid() : model2.getAid();
	}

	public float getNowprice() {
		return model1 != null ? model1.getNowprice() : model2.getNowprice();
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
					if (popuBean.getObj() != null) {
						for (int i = 0; i < popuBean.getObj().length; i++) {
							View view = createPayTypeView(popuBean.getObj()[i]);
							if (view != null) {
								LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(-1, DisplayUtils.dip2px(SubmitOrderActivity.this, 48));
								payLayout.addView(view, ll);
								if (i != popuBean.getObj().length - 1) {
									payLayout.addView(ViewFactoryUtils.getLineView(SubmitOrderActivity.this, 0));
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

					if (moneyCount <= 0) {
						//不用给钱了

					} else {
						onClick(view);
					}
				}
			}
		};

		HttpMethod.getInstance().getPayList(subscriber, request);
	}

	public void getDefalutAddress() {
		Subscriber<DefaultAddressResponse> subscriber = new Subscriber<DefaultAddressResponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(DefaultAddressResponse baseResponse) {
				if (baseResponse != null && baseResponse.getObj() != null) {
					//有默认的
					addressObJ = baseResponse.getObj();
				} else {
					//没有默认的
					addressObJ = null;
				}

				upAddressUI();
			}
		};
		BaseIdRequest request = new BaseIdRequest();
		request.setUid(CacheData.getInstance().getUserData().getUid());
		HttpMethod.getInstance().getDefalutAddress(subscriber, request);

	}

	public void upAddressUI() {
		if (addressObJ != null) {
			findViewById(R.id.layout_address_info).setVisibility(View.VISIBLE);
			findViewById(R.id.btn_add).setVisibility(View.GONE);
			tv_content.setText(addressObJ.getContacts() + "(" + addressObJ.getPhone() + ") " + addressObJ.getProvince() + " " + addressObJ.getCity() + " " + addressObJ.getArea() + " " + addressObJ.getDetailed());
		} else {
			//没有默认的
			findViewById(R.id.layout_address_info).setVisibility(View.GONE);
			findViewById(R.id.btn_add).setVisibility(View.VISIBLE);
		}
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

		if (moneyCount <= 0) {
			showToast("购币可以全额购买，请点击支付");
			return;
		}

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

		if (moneyCount <= 0) {
			return true;
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == 200 && data != null) {
			addressObJ = (MyAddressObJ) data.getSerializableExtra("model");
			upAddressUI();
		}
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

	@Override
	public void onResume() {
		super.onResume();
		if (payObjBeen != null) {
			OtherPayTools.queryPayState(this, payObjBeen.getTradeNO(), this);
		}

	}
}
