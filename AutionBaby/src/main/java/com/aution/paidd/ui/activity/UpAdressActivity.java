package com.aution.paidd.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.framework.core.base.BaseActivity;
import com.aution.paidd.R;
import com.aution.paidd.bean.AreaInfo;
import com.aution.paidd.bean.CItyInfoBean;
import com.aution.paidd.bean.MyAddressObJ;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.request.UpAdressRequest;
import com.aution.paidd.response.BaseResponse;
import com.aution.paidd.utils.FileUtils;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;


public class UpAdressActivity extends BaseActivity {

	@BindView(R.id.sp_city)
	TextView spCity;
	@BindView(R.id.address_name)
	EditText addressName;
	@BindView(R.id.address_detail)
	EditText addressDetail;
	@BindView(R.id.address_phone)
	EditText addressPhone;
	@BindView(R.id.address_cb)
	CheckBox addressCk;
	@BindView(R.id.tv_qq)
	EditText tv_qq;
	@BindView(R.id.tv_alipay)
	EditText tv_alipay;

	int type = 0;//0=添加; 1=更新
	private int myState = 2;
	private MyAddressObJ model;

	private String myProvinceName;
	private String myProvinceId;
	private String myCityName;
	private String myCityCode;
	private String myAreaName;
	private String myAreaId;

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			initSpinnerCity(areaMap);
		}
	};


	@Override
	public int getContentLayoutID() {
		return R.layout.activity_upadress;
	}

	@Override
	public void initValue() {
		EventBus.getDefault().register(this);
		type = getIntent().getIntExtra("type", 0);
		setTitle(type == 0 ? "收货地址" : "更新地址");
		model = (MyAddressObJ) getIntent().getSerializableExtra("model");
		if (model != null) {
			addressName.setText(model.getContacts());
			addressDetail.setText(model.getDetailed());
			addressPhone.setText(model.getPhone());
			tv_qq.setText(model.getQq());
			tv_alipay.setText(model.getAlipay());
			if (model.getState() == 1) {
				//默认
				addressCk.setChecked(true);
			} else {
				addressCk.setChecked(false);
			}
		}

		new Thread() {
			public void run() {
				areaMap = getAreaMap();
				handler.sendEmptyMessage(0);
			}
		}.start();
	}

	ArrayList<AreaInfo> areaMap;

	@OnClick({R.id.address_cb_layout, R.id.btn_submit})
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.address_cb_layout:
				addressCk.setChecked(!addressCk.isChecked());
				break;
			case R.id.btn_submit:
				saveOrUp();
				break;
		}
	}

	private void initSpinnerCity(final ArrayList<AreaInfo> cityList) {
		if (model != null) {
			myProvinceId = model.getProvinceid();
			myProvinceName = model.getProvince();
			myCityName = model.getCity();
			myCityCode = model.getCityid();
			myAreaName = model.getArea();
			myAreaId = model.getAreaid();
			setSpCity();
		}
		spCity.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("citylist", areaMap);
				bundle.putString("title", "选择省市");
				doActivity(CityListActivity.class, bundle);
			}
		});
	}

	public ArrayList<AreaInfo> getAreaMap() {
		if (areaMap == null || areaMap.isEmpty()) {
			String jsontxt = FileUtils.getFromAssets("area.txt", this);
			JSONArray jarray = null;
			try {
				jarray = new JSONArray(jsontxt);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			areaMap = getArea(jarray);
		}
		return areaMap;
	}

	private ArrayList<AreaInfo> getArea(JSONArray jarray) {
		ArrayList<AreaInfo> areaList = new ArrayList<AreaInfo>();

		int count = jarray.length();
		AreaInfo temp = null;
		JSONObject jobj = null;
		for (int i = 0; i < count; i++) {
			try {
				jobj = jarray.getJSONObject(i);
				temp = new AreaInfo();
				temp.code = jobj.getString("id");
				temp.name = jobj.getString("name");
				ArrayList<AreaInfo> tempList = new ArrayList<AreaInfo>();
				if (jobj.has("nodes") && jobj.getJSONArray("nodes") != null && jobj.getJSONArray("nodes").length() > 0) {
					tempList = getArea(jobj.getJSONArray("nodes"));
				}
				temp.areaList = tempList;
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				areaList.add(temp);
			}
		}

		return areaList;
	}

	/**
	 * 提交数据
	 */
	public void saveOrUp() {
		if (checkIsEmpty()) {
			if (addressCk.isChecked()) {
				myState = 1;
			}
			submit();
		}
	}

	public boolean checkIsEmpty() {
		if (TextUtils.isEmpty(addressName.getText().toString())) {
			showToast("联系人不能为空");
			return false;
		} else if (TextUtils.isEmpty(addressPhone.getText().toString())) {
			showToast("联系电话不能为空");
			return false;
		} else if (spCity.getText().toString().equals("请选择")) {
			showToast("所在地区不能为空");
			return false;
		} else if (TextUtils.isEmpty(addressDetail.getText().toString())) {
			showToast("详细地址不能为空");
			return false;
		}
		return true;
	}

	private void submit() {
		UpAdressRequest request = new UpAdressRequest();
		if (type == 0) {
			request.setUid(CacheData.getInstance().getUserData().getUid());
		} else {
			request.setUid(CacheData.getInstance().getUserData().getUid());
			request.setId(model.getId() + "");
		}
		request.setArea(myAreaName);
		request.setAreaid(myAreaId);
		request.setCity(myCityName);
		request.setCityid(myCityCode);
		request.setContacts(addressName.getText().toString());
		request.setDetailed(addressDetail.getText().toString());
		request.setPhone(addressPhone.getText().toString());
		request.setProvince(myProvinceName);
		request.setProvinceid(myProvinceId);
		request.setState(myState + "");
		request.setQq(tv_qq.getText().toString());
		request.setAlipay(tv_alipay.getText().toString());


		Subscriber<BaseResponse> subscriber = new Subscriber<BaseResponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(BaseResponse baseResponse) {
				showToast(baseResponse.getMsg());
				if (baseResponse != null && baseResponse.getCode() == 10000) {
					finish(200);
				}
			}
		};
		if (type == 0) {
			HttpMethod.getInstance().getAddressAdd(subscriber, request);
		} else {
			HttpMethod.getInstance().getAddressUp(subscriber, request);
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onMessageEvent(CItyInfoBean event) {
		myProvinceId = event.getPcode();
		myProvinceName = event.getPname();
		myCityName = event.getCname();
		myCityCode = event.getCcode();
		myAreaName = event.getAname();
		myAreaId = event.getAcode();
		setSpCity();
	}

	public void setSpCity() {
		String str = "";
		if (!TextUtils.isEmpty(myProvinceName)) {
			str += myProvinceName + " ";
		}
		if (!TextUtils.isEmpty(myCityName)) {
			str += myCityName + " ";
		}
		if (!TextUtils.isEmpty(myAreaName)) {
			str += myAreaName + " ";
		}
		spCity.setText(str);
	}

	public void finish(int code) {
		setResult(code);
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}
