package com.aution.paidd.ui.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.framework.core.base.BaseListActivity;
import com.framework.core.utils.DialogUtils;
import com.framework.core.widget.FlatButton;
import com.framework.core.widget.pull.BaseViewHolder;
import com.aution.paidd.R;
import com.aution.paidd.bean.MyAddressObJ;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.request.BaseIdRequest;
import com.aution.paidd.request.UpAdressRequest;
import com.aution.paidd.response.AdressUpReponse;
import com.aution.paidd.response.BaseResponse;
import com.aution.paidd.ui.widget.MailLineView;
import com.aution.paidd.utils.FormatUtils;

import butterknife.BindView;
import me.xiaopan.sketch.SketchImageView;
import rx.Subscriber;

/**
 * 管理地址
 * Created by yangjie on 2016/11/6.
 */
public class AdressListActivity extends BaseListActivity<MyAddressObJ> {


	@BindView(R.id.img_add)
	SketchImageView img_add;
	int type;

	@Override
	protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
		return new MyViewHolder(getLayoutInflater().inflate(R.layout.activity_adress_item, parent, false));
	}

	@Override
	public int getContentLayoutID() {
		return R.layout.activity_adress;
	}

	@Override
	protected RecyclerView.ItemDecoration getItemDecoration() {
		return null;
	}

	@Override
	protected void setUpData() {
		super.setUpData();
		setTitle("地址管理");
		type=getIntent().getIntExtra("type",0);
		if (type==2){
			setTitle("选择地址");
		}else if (type==3){
			setTitle("选择地址");
		}
		recycler.enableLoadMore(false);
		recycler.enablePullToRefresh(false);
		getData(true);
		img_add.displayResourceImage(R.drawable.ic_xzdz);
		img_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//添加地址
				doActivityForResult(UpAdressActivity.class,null,100);
			}
		});
	}

	@Override
	public void onRefresh(int action) {

	}

	public void getData(boolean isShow) {
		if (isShow){
			currentPage=1;
		}
		toggleShowLoading(isShow, null);
		BaseIdRequest request = new BaseIdRequest();
		request.setUid(CacheData.getInstance().getUserData().getUid() + "");
		Subscriber<AdressUpReponse> subscriber = new Subscriber<AdressUpReponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				toggleShowEmpty(true, "点击右上角添加地址", null, new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						getData(true);
					}
				});
				recycler.onRefreshCompleted();
			}

			@Override
			public void onNext(AdressUpReponse myAddressListBeen) {
				recycler.onRefreshCompleted();
				toggleShowLoading(false, null);
				if (myAddressListBeen != null && myAddressListBeen.getCode() == 10000) {
					mDataList.clear();
					if (myAddressListBeen.getObj() != null && myAddressListBeen.getObj().size() > 0) {
						mDataList.addAll(myAddressListBeen.getObj());
					}
					if (mDataList.size() <= 0) {
						toggleShowEmpty(true, "点击右上角添加地址", null, new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								getData(true);
							}
						});
						return;
					}
					adapter.notifyDataSetChanged();
				} else {
					toggleShowEmpty(true, "点击右上角添加地址", null, new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							getData(true);
						}
					});
				}
			}
		};
		HttpMethod.getInstance().getAdressList(subscriber, request);
	}

	@Override
	protected int getItemType(int position) {
		return mDataList.get(position).getItemType();
	}

	class MyViewHolder extends BaseViewHolder {

		@BindView(R.id.address_name_one)
		TextView addressNameOne;
		@BindView(R.id.phone)
		TextView phone;
		@BindView(R.id.address_detail)
		TextView address_detail;
		@BindView(R.id.address_delete_one)
		FlatButton address_delete_one;
		@BindView(R.id.btn_edit)
		FlatButton btn_edit;
		@BindView(R.id.maillineview1)
		MailLineView mailLineView1;
		@BindView(R.id.maillineview2)
		MailLineView mailLineView2;
		@BindView(R.id.address_cb)
		CheckBox address_cb;
		@BindView(R.id.cb_address)
		CheckBox cb_address;
		@BindView(R.id.tv_qq)
		TextView tv_qq;
		@BindView(R.id.tv_alipay)
		TextView tv_alipay;


		public MyViewHolder(View itemView) {
			super(itemView);
		}

		@Override
		public void onBindViewHolder(final int position) {




			if (type==2){
				cb_address.setVisibility(View.VISIBLE);
				if (mDataList.get(position).getId()==getIntent().getLongExtra("id",-1)){
					cb_address.setChecked(true);
				}else {
					cb_address.setChecked(false);
				}
			}else {
				cb_address.setVisibility(View.GONE);
			}

			if (mDataList.get(position).getState() == 1) {
				mailLineView1.setVisibility(View.VISIBLE);
				mailLineView2.setVisibility(View.VISIBLE);
			} else {
				mailLineView1.setVisibility(View.GONE);
				mailLineView2.setVisibility(View.GONE);
			}


			if(TextUtils.isEmpty(mDataList.get(position).getQq())){
				itemView.findViewById(R.id.layout_qq).setVisibility(View.GONE);
			}else {
				itemView.findViewById(R.id.layout_qq).setVisibility(View.VISIBLE);
				tv_qq.setText(mDataList.get(position).getQq());
			}

			if(TextUtils.isEmpty(mDataList.get(position).getAlipay())){
				itemView.findViewById(R.id.layout_alipay).setVisibility(View.GONE);
			}else {
				itemView.findViewById(R.id.layout_alipay).setVisibility(View.VISIBLE);
				tv_alipay.setText(mDataList.get(position).getAlipay());
			}




			String pname = mDataList.get(position).getProvince();
			String cname = mDataList.get(position).getCity();
			String aname = mDataList.get(position).getArea();
			String str = "";
			if (!TextUtils.isEmpty(pname)) {
				str += pname + " ";
			}
			if (!TextUtils.isEmpty(cname)) {
				str += cname + " ";
			}
			if (!TextUtils.isEmpty(aname)) {
				str += aname + " ";
			}
			String adressstr = str + mDataList.get(position).getDetailed();
			address_detail.setText(adressstr);

			phone.setText(FormatUtils.formatAccount(mDataList.get(position).getPhone()));
			address_delete_one.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					showDleteDownAPKDialog(mDataList.get(position).getId());
				}
			});
			addressNameOne.setText(mDataList.get(position).getContacts());
			btn_edit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
                    Intent intent = new Intent(AdressListActivity.this, UpAdressActivity.class);
                    intent.putExtra("type", 1);
                    intent.putExtra("model", mDataList.get(position));
                    startActivityForResult(intent, 100);
				}
			});

			address_cb.setChecked(mDataList.get(position).getState()==1?true:false);

		}

		@Override
		public void onItemClick(View view, int position) {
			if (type==2){
				Intent intent=new Intent();
				intent.putExtra("model",mDataList.get(position));
				setResult(200,intent);
				finish();
			}
		}
	}


	public void upDataAdressData(int position) {
        UpAdressRequest request = new UpAdressRequest();
        MyAddressObJ model = mDataList.get(position);
        request.setArea(model.getArea());
        request.setAreaid(model.getAreaid());
        request.setCity(model.getCity());
        request.setCityid(model.getCityid());
        request.setContacts(model.getContacts());
        request.setDetailed(model.getDetailed());
        request.setId(model.getId() + "");
        request.setPhone(model.getPhone());
        request.setProvince(model.getProvince());
        request.setProvinceid(model.getProvinceid());
        request.setState("1");
        Subscriber<BaseResponse> subscriber=new Subscriber<BaseResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showToast(e.getMessage());
            }

            @Override
            public void onNext(BaseResponse baseResponse) {
                showToast(baseResponse.getMsg());
                if (baseResponse != null && baseResponse.getCode() == 10000) {
                    getData(true);
                }
            }
        };
        HttpMethod.getInstance().getAddressUp(subscriber,request);
	}


	public void showDleteDownAPKDialog(final long deletebuyid) {
		DialogUtils.showDialog(this, "确定要删除?", "删除", new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				initDeleteDate(deletebuyid);
			}
		});
	}


	private void initDeleteDate(long buyid) {
        BaseIdRequest request = new BaseIdRequest();
        request.setIds(buyid + "");
        Subscriber<BaseResponse> subscriber=new Subscriber<BaseResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                showToast(e.getMessage());
            }

            @Override
            public void onNext(BaseResponse baseResponse) {
                showToast(baseResponse.getMsg());
                if (baseResponse != null && baseResponse.getCode() == 10000) {
                    getData(true);
                }
            }
        };
        HttpMethod.getInstance().getRemoveAdress(subscriber,request);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 200) {
			getData(true);
		}
	}

}
