package com.aution.paidd.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framework.core.base.BaseListFragment;
import com.framework.core.utils.TextSpanUtils;
import com.framework.core.widget.FlatButton;
import com.framework.core.widget.pull.BaseViewHolder;
import com.framework.core.widget.pull.PullRecycler;
import com.aution.paidd.R;
import com.aution.paidd.bean.ShopRecordBean;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.Constant;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.request.BaseIdRequest;
import com.aution.paidd.request.ConfirmSignRequest;
import com.aution.paidd.request.OrderRequest;
import com.aution.paidd.response.BaseResponse;
import com.aution.paidd.response.LogisticsResponse;
import com.aution.paidd.response.ShopRecordResponse;
import com.aution.paidd.ui.activity.AddLuckyShowActivity;
import com.aution.paidd.ui.activity.H5Activity;
import com.aution.paidd.ui.activity.ShopDetailActivity;
import com.aution.paidd.ui.activity.SubmitOrderActivity;

import butterknife.BindView;
import me.xiaopan.sketch.SketchImageView;
import rx.Subscriber;

/**
 * 购买记录
 * Created by quxiang on 2016/12/15.
 */
public class ShopRecordListFragment extends BaseListFragment<ShopRecordBean> {

	String imgBaseUrl;

	int type;

	@Override
	public int getContentLayoutID() {
		return R.layout.fragment_record_list_layout;
	}

	@Override
	protected RecyclerView.ItemDecoration getItemDecoration() {
		return null;
	}

	@Override
	public void initValue() {
		type=getArguments().getInt("type");
	}

	@Override
	protected void onFirstUserVisible() {
		getData(true);
	}

	public void getData(boolean isShow) {
		if (isShow){
			currentPage=1;
		}
		toggleShowLoading(isShow,null);
		Subscriber<ShopRecordResponse> subscriber=new Subscriber<ShopRecordResponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				toggleShowEmpty(true, "暂无记录", "刷新", new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						getData(true);
					}
				});
				recycler.onRefreshCompleted();
			}

			@Override
			public void onNext(ShopRecordResponse response) {
				toggleShowLoading(false,null);
				recycler.onRefreshCompleted();
				if (response != null) {
					if (response.getCode() == 10000) {

						imgBaseUrl=response.getMsg();
						if (currentPage==1){
							mDataList.clear();
						}
						if (response.getObj() != null && response.getObj().getResultlist() != null) {

							mDataList.addAll(response.getObj().getResultlist());
							if (response.getObj().getResultlist().size() <= 0) {
								recycler.enableLoadMore(false);
							} else {
								recycler.enableLoadMore(true);
							}

							currentPage++;

							if (mDataList.size() <= 0) {
								toggleShowEmpty(true, "暂无记录", "刷新", new View.OnClickListener() {
									@Override
									public void onClick(View view) {

										getData(true);
									}
								});
							} else {
								adapter.notifyDataSetChanged();
							}
						}
					} else {
						toggleShowEmpty(true, response.getMsg(), "刷新", new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								getData(true);
							}
						});
					}
				} else {
					toggleShowEmpty(true, Constant.Service_Err, "刷新", new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							getData(true);
						}
					});
				}
			}
		};
		BaseIdRequest request=new BaseIdRequest();
		request.setUid(CacheData.getInstance().getUserData().getUid());
		request.setCurrentpage(currentPage+"");
		request.setMaxresult(rows+"");

		if (type==1){
			//差价购
			HttpMethod.getInstance().getRecordNowinning(subscriber,request);
		}else {
			HttpMethod.getInstance().getGoodsMyBuy(subscriber,request);
		}
	}

	@Override
	protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
		return new MyViewHolder(getActivity().getLayoutInflater().inflate(R.layout.fragment_record_list_item, parent, false));
	}

	@Override
	public void onRefresh(int action) {
		if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
			currentPage = 1;
		}
		getData(false);
	}


	class MyViewHolder extends BaseViewHolder {

		@BindView(R.id.tv_desc)
		TextView tv_desc;
		@BindView(R.id.tv_state)
		TextView tv_state;
		@BindView(R.id.tv_nowprice)
		TextView tv_nowprice;
		@BindView(R.id.tv_price)
		TextView tv_price;
		@BindView(R.id.tv_myprice)
		TextView tv_myprice;
		@BindView(R.id.img)
		SketchImageView img;
		@BindView(R.id.btn_submit)
		FlatButton btn_submit;
		@BindView(R.id.btn_left)
		FlatButton btn_left;
		ShopRecordBean model;

		public MyViewHolder(View itemViewName) {
			super(itemViewName);

		}

		@Override
		public void onBindViewHolder(int position) {
			model=mDataList.get(position);
			tv_desc.setText(type==1?mDataList.get(position).getCreatetime():"正在参与竞拍");
			final int kk=position;
			btn_submit.setEnabled(true);
			if (type==0){
				tv_state.setText(mDataList.get(position).getStatus()==1?"进行中":"已结束");
				tv_state.setTextColor(mDataList.get(position).getStatus()==1? getResources().getColor(R.color.base_text_title):getResources().getColor(R.color.theme_color));
				btn_submit.setText("继续竞拍");
				btn_submit.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Bundle bundle=new Bundle();
						bundle.putString("id",mDataList.get(kk).getId());
						bundle.putString("view","3");
						doActivity(ShopDetailActivity.class,bundle);
					}
				});
			}else {
				btn_submit.setText("差价购");
				switch (mDataList.get(position).getStatusss()){
					case 1:
						tv_state.setText("待付款");
						btn_submit.setText("差价购");
						break;
					case 2:
						tv_state.setText("待发货");
						btn_submit.setText("我要催单");
						break;
					case 3:
						tv_state.setText("");
						btn_submit.setText("已催单");
						btn_submit.setEnabled(false);
						break;
					case 4:
						tv_state.setText("待签收");
						btn_submit.setText("立即签收");
						btn_left.setVisibility(View.VISIBLE);
						btn_left.setText("物流信息");
						break;
					case 5:
						tv_state.setText(mDataList.get(position).getFlag()==1?"已晒单":"待晒单");
						if (mDataList.get(position).getFlag()==0){
							btn_submit.setText("晒单有喜");
						}
						break;
					case 6:
						tv_state.setText("交易完成");
						btn_submit.setText("继续竞拍");
						break;
				}
				final int status=mDataList.get(position).getStatusss();

				btn_submit.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						switch (status){
							case 1:
								Bundle bundle=new Bundle();
								bundle.putSerializable("model",mDataList.get(kk));
								bundle.putString("scene","1");
								bundle.putString("baseImgUrl",imgBaseUrl);
								doActivityForresult(100,SubmitOrderActivity.class,bundle);
								break;
							case 2:
								//催单
								reminder(kk);
								break;
							case 4:
								confirmSign(kk);
								break;
							case 5:
								Bundle bundle1=new Bundle();
								bundle1.putString("aid",mDataList.get(kk).getAid());
								bundle1.putString("orderno",mDataList.get(kk).getOrderno());
								doActivity(AddLuckyShowActivity.class,bundle1);
								break;
							case 6:
								Bundle bundle2=new Bundle();
								bundle2.putString("id",mDataList.get(kk).getCid());
								bundle2.putString("view","1");
								doActivity(ShopDetailActivity.class,bundle2);
								break;
						}


					}
				});
			}


			String st1="成交价：￥"+mDataList.get(position).getNowprice();
			tv_nowprice.setText(TextSpanUtils.createColorText(st1,4,st1.length(),getResources().getColor(R.color.theme_color)));
			String st2="竞拍者："+mDataList.get(position).getLastbid();
			tv_myprice.setText(st2);

			SpannableString ss = new SpannableString("市场价：￥"+mDataList.get(position).getPrice());
			ss.setSpan(new StrikethroughSpan(), 4, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			tv_price.setText(ss);

			if (type==1){
				tv_price.setText(model.getName());
				tv_nowprice.setText("市场价：￥"+mDataList.get(position).getPrice());
				tv_myprice.setText("付款成功后返购币："+model.getLockbuymoney()+"个");
				tv_myprice.setTextColor(getResources().getColor(R.color.theme_color));
			}

			img.displayImage(imgBaseUrl+mDataList.get(position).getHeadimage());
			btn_left.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					getLogistics(mDataList.get(kk).getOrderno());
				}
			});
		}



		public void confirmSign(int index){
			showDialog(null);
			Subscriber<BaseResponse> subscriber=new Subscriber<BaseResponse>() {
				@Override
				public void onCompleted() {

				}

				@Override
				public void onError(Throwable e) {
					closeDialog();
				}

				@Override
				public void onNext(BaseResponse baseResponse) {
					closeDialog();
					if (baseResponse!=null){
						showToast(baseResponse.getMsg());
						if (baseResponse.getCode()==10000){
							getData(true);
						}
					}
				}
			};
			ConfirmSignRequest request=new ConfirmSignRequest();
			request.setOrderno(mDataList.get(index).getOrderno());
			request.setAid(mDataList.get(index).getAid());
			request.setUid(CacheData.getInstance().getUserData().getUid());
			HttpMethod.getInstance().getAddressSign(subscriber,request);
		}

		public void reminder(int index){
			showDialog(null);
			Subscriber<BaseResponse> subscriber=new Subscriber<BaseResponse>() {
				@Override
				public void onCompleted() {

				}

				@Override
				public void onError(Throwable e) {
					closeDialog();
				}

				@Override
				public void onNext(BaseResponse baseResponse) {
					closeDialog();
					if (baseResponse!=null){
						showToast(baseResponse.getMsg());
						if (baseResponse.getCode()==10000){
							getData(true);
						}
					}
				}
			};
			OrderRequest request=new OrderRequest();
			request.setOrderno(mDataList.get(index).getOrderno());
			HttpMethod.getInstance().getReminder(subscriber,request);
		}



		public void getLogistics(String order){
			showDialog(null);
			Subscriber<LogisticsResponse> subscriber=new Subscriber<LogisticsResponse>() {
				@Override
				public void onCompleted() {

				}

				@Override
				public void onError(Throwable e) {
					closeDialog();
				}

				@Override
				public void onNext(LogisticsResponse logisticsResponse) {
					closeDialog();
					if (logisticsResponse!=null){
						if (logisticsResponse.getCode()==10000){
							String url="https://m.kuaidi100.com/index_all.html?type="+logisticsResponse.getObj().getType()+"&postid="+logisticsResponse.getObj().getOrdernumber();
							Bundle bundle=new Bundle();
							bundle.putString("fullurl",url);
							doActivity(H5Activity.class,bundle);
						}
					}
				}
			};
			OrderRequest request=new OrderRequest();
			request.setOrderno(order);
			HttpMethod.getInstance().getLogistics(subscriber,request);
		}

		@Override
		public void onItemClick(View view, int position) {
			Bundle bundle=new Bundle();

			if (type==1){
				//差价购
				bundle.putString("id",model.getAid());
				bundle.putString("view","3");
			}else {
				bundle.putString("id",model.getId());
				bundle.putString("view","3");
			}

			doActivity(ShopDetailActivity.class,bundle);
		}


	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		getData(true);
	}
}
