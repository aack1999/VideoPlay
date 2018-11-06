package com.aution.paidd.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framework.core.base.BaseListFragment;
import com.framework.core.widget.pull.BaseViewHolder;
import com.framework.core.widget.pull.PullRecycler;
import com.aution.paidd.R;
import com.aution.paidd.bean.CoinIncomeBean;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.Constant;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.request.CoinIncomeRequest;
import com.aution.paidd.response.CoinIncomeResponse;

import butterknife.BindView;
import rx.Subscriber;

/**
 * Created by quxiang on 2017/9/8.
 */

public class IncomeDetailFragment extends BaseListFragment<CoinIncomeBean> {

	int state;

	@Override
	public int getContentLayoutID() {
		return R.layout.fragment_income_list;
	}

	@Override
	protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
		return new MyViewHolder(getActivity().getLayoutInflater().inflate(R.layout.fragment_income_item,parent,false));
	}

	@Override
	public void initValue() {
		state=getArguments().getInt("state");
		getData(true);
	}

	@Override
	protected void onFirstUserVisible() {

	}


	@Override
	public void onRefresh(int action) {
		if (action== PullRecycler.ACTION_PULL_TO_REFRESH){
			currentPage=1;
		}
		getData(false);
	}

	public void getData(boolean isShow){
		if (isShow){
			currentPage=1;
		}
		toggleShowLoading(isShow,null);
		Subscriber<CoinIncomeResponse> subscriber=new Subscriber<CoinIncomeResponse>() {
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
			public void onNext(CoinIncomeResponse response) {
				toggleShowLoading(false,null);
				recycler.onRefreshCompleted();
				if (response != null) {
					if (response.getCode() == 10000) {

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
		CoinIncomeRequest request=new CoinIncomeRequest();
		request.setUid(CacheData.getInstance().getUserData().getUid());
		request.setCurrentpage(currentPage+"");
		request.setMaxresult(rows+"");
		request.setState(state+"");
		request.setType(getArguments().getInt("type")+"");
		HttpMethod.getInstance().getIncomeList(subscriber,request);
	}

	class MyViewHolder extends BaseViewHolder{

		@BindView(R.id.tv_time)
		TextView tv_time;
		@BindView(R.id.tv_content)
		TextView tv_content;
		@BindView(R.id.tv_count)
		TextView tv_count;

		public MyViewHolder(View itemView) {
			super(itemView);
		}

		@Override
		public void onBindViewHolder(int position) {
			tv_time.setText(mDataList.get(position).getCreatetime().replaceAll(" ","\n"));
			tv_content.setText(mDataList.get(position).getRemark());
			tv_count.setText((state==1?"+":"-")+mDataList.get(position).getNumber());
		}

		@Override
		public void onItemClick(View view, int position) {

		}
	}


	public static IncomeDetailFragment newFragment(int type,int state){
		IncomeDetailFragment idf=new IncomeDetailFragment();
		Bundle bundle=new Bundle();
		bundle.putInt("type",type);
		bundle.putInt("state",state);
		idf.setArguments(bundle);
		return idf;
	}

}
