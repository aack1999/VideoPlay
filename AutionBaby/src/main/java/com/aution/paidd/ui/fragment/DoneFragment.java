package com.aution.paidd.ui.fragment;

import android.os.Bundle;
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
import com.aution.paidd.bean.ShopHistoryBean;
import com.aution.paidd.common.Constant;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.request.BaseListRequest;
import com.aution.paidd.response.HeadLineResponse;
import com.aution.paidd.response.ShopHistoryResponse;
import com.aution.paidd.ui.activity.ShopDetailActivity;
import com.aution.paidd.ui.widget.MarqueeView;

import butterknife.BindView;
import me.xiaopan.sketch.SketchImageView;
import rx.Subscriber;

/**
 * Created by quxiang on 2017/8/30.
 */

public class DoneFragment extends BaseListFragment<ShopHistoryBean> {

	String imgBaseUrl;
	@BindView(R.id.view_scolltext)
	MarqueeView view_scolltext;

	@Override
	public int getContentLayoutID() {
		return R.layout.fragment_done;
	}

	@Override
	public void initValue() {
		getHeadLine();
		getData(true);
	}

	@Override
	protected void onFirstUserVisible() {

	}

	@Override
	protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
		return new MyViewHolder(getActivity().getLayoutInflater().inflate(R.layout.viewholder_done_item, parent, false));
	}

	@Override
	public void onRefresh(int action) {
		if (action== PullRecycler.ACTION_PULL_TO_REFRESH){
			currentPage=1;
		}
		getData(false);
	}

	public void getData(boolean isShow) {
		if (isShow){
			currentPage=1;
		}
		toggleShowLoading(isShow, null);
		Subscriber<ShopHistoryResponse> subscriber = new Subscriber<ShopHistoryResponse>() {
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
			public void onNext(ShopHistoryResponse response) {
				toggleShowLoading(false,null);
				recycler.onRefreshCompleted();
				if (response != null) {
					if (response.getCode() == 10000) {

						imgBaseUrl=response.getMsg();

						if (response.getObj() != null && response.getObj().getResultlist() != null) {
							if (currentPage==1){
								mDataList.clear();
							}
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
		BaseListRequest request = new BaseListRequest();
		request.setCurrentpage(currentPage + "");
		request.setMaxresult(rows + "");
		HttpMethod.getInstance().getDoneGoodList(subscriber, request);
	}


	public void getHeadLine() {
		Subscriber<HeadLineResponse> subscriber = new Subscriber<HeadLineResponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(HeadLineResponse headLineResponse) {


				if (headLineResponse != null) {
					if (headLineResponse.getCode() == 10000 && headLineResponse.getObj() != null && headLineResponse.getObj().size() > 0) {
						//刷新
						if (view_scolltext != null) {
							view_scolltext.setNotices(headLineResponse.getObj());
							view_scolltext.start();
						}
					}
				} else {
					showToast(Constant.Service_Err);
				}
			}
		};
		HttpMethod.getInstance().getHeadLine(subscriber);
	}

	class MyViewHolder extends BaseViewHolder {

		@BindView(R.id.img)
		SketchImageView img;
		@BindView(R.id.img_ok)
		SketchImageView img_ok;
		@BindView(R.id.tv_nameid)
		TextView tv_nameid;
		@BindView(R.id.tv_price)
		TextView tv_price;
		@BindView(R.id.tv_free_count)
		TextView tv_free_count;
		@BindView(R.id.tv_nowprice)
		TextView tv_nowprice;
		@BindView(R.id.tv_time)
		TextView tv_time;
		@BindView(R.id.btn_go)
		FlatButton btn_go;

		public MyViewHolder(View itemView) {
			super(itemView);
		}

		@Override
		public void onBindViewHolder(int position) {
			img.displayImage(imgBaseUrl+mDataList.get(position).getHeadimage());


			img_ok.displayResourceImage(R.drawable.ic_ycj);

			tv_nameid.setText("成交人："+mDataList.get(position).getNickname());

			SpannableString ss = new SpannableString("市场价：￥"+mDataList.get(position).getPrice());
			ss.setSpan(new StrikethroughSpan(), 4, ss.length(),
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			tv_price.setText(ss);
			tv_free_count.setText(mDataList.get(position).getSavemoney());
			String temp="成交价：￥"+mDataList.get(position).getNowprice();
			tv_nowprice.setText(TextSpanUtils.createColorText(temp,4,temp.length(),getResources().getColor(R.color.theme_color)));
			tv_time.setText("成交时间："+mDataList.get(position).getCreatetime());

			final int kk=position;
			btn_go.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Bundle bundle=new Bundle();
					bundle.putString("id",mDataList.get(kk).getCid());
					bundle.putString("view","1");
					doActivity(ShopDetailActivity.class,bundle);
				}
			});
		}

		@Override
		public void onItemClick(View view, int position) {
			Bundle bundle=new Bundle();
			bundle.putString("id",mDataList.get(position).getAid());
			bundle.putString("view","3");
			doActivity(ShopDetailActivity.class,bundle);
		}
	}

}
