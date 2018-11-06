package com.aution.paidd.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.framework.core.base.BaseListFragment;
import com.framework.core.widget.FlatButton;
import com.framework.core.widget.pull.BaseViewHolder;
import com.framework.core.widget.pull.PullRecycler;
import com.nineoldandroids.animation.ObjectAnimator;
import com.aution.paidd.R;
import com.aution.paidd.bean.GoodsListBean;
import com.aution.paidd.bean.ServceMessageBean;
import com.aution.paidd.bean.ShopTypeBean;
import com.aution.paidd.common.Constant;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.request.BaseIdRequest;
import com.aution.paidd.response.GoodsListResponse;
import com.aution.paidd.response.ShopTypeResponse;
import com.aution.paidd.ui.activity.ShopDetailActivity;
import com.aution.paidd.ui.adapter.CategoryTabAdapter;
import com.aution.paidd.utils.FormatUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import me.xiaopan.sketch.SketchImageView;
import rx.Subscriber;

/**
 * Created by quxiang on 2017/8/30.
 */

public class AllsFragment extends BaseListFragment<GoodsListBean> {

	@BindView(R.id.hrecyclerView)
	RecyclerView hrecyclerView;

	CategoryTabAdapter cadapter;
	List<ShopTypeBean> innerPopuDate;

	String imgBaseUrl;
	String commoditytype = "";

	//全局定时器
	SparseArray<MyViewHodler> refreshShops;
	Handler mHandler;
	Runnable mRefreshTimeRunnable;
	TimerTask timerTask;

	@Override
	public int getContentLayoutID() {
		return R.layout.fragment_alls;
	}

	@Override
	public void initValue() {
		initData();
		innerPopuDate = new ArrayList<>();

		hrecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
		cadapter = new CategoryTabAdapter(getContext(), innerPopuDate);
		hrecyclerView.setAdapter(cadapter);

		cadapter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				commoditytype = (String) view.getTag();
				currentPage = 1;
				getData();
			}
		});
		adapter.notifyDataSetChanged();
		getTypeData();
	}

	public void initData() {
		refreshShops = new SparseArray<>();
		mHandler = new MyHanlder();
		timerTask = new MyTimerTask(this);
		mRefreshTimeRunnable = new MyRunnable(this);
	}

	@Override
	protected void onFirstUserVisible() {

	}

	/**
	 * 获取分类数据
	 */
	public void getTypeData() {
		Subscriber<ShopTypeResponse> subscriber = new Subscriber<ShopTypeResponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(ShopTypeResponse shopTypeResponse) {
				if (shopTypeResponse != null) {
					if (shopTypeResponse.getCode() == 10000) {
						innerPopuDate.addAll(shopTypeResponse.getObj());
						if (shopTypeResponse.getObj().size() > 0) {
							shopTypeResponse.getObj().get(0).setCheck(true);
							commoditytype = shopTypeResponse.getObj().get(0).getId();
							currentPage = 1;
							getData();
						}
						cadapter.notifyDataSetChanged();
					} else {
						showToast(shopTypeResponse.getMsg());
					}
				} else {
					showToast(Constant.Service_Err);
				}
			}
		};

		HttpMethod.getInstance().getTypeList(subscriber);
	}

	public void getData() {
		BaseIdRequest request = new BaseIdRequest();
		request.setCurrentpage(currentPage + "");
		request.setMaxresult(rows + "");
		request.setType(commoditytype);
		toggleShowLoading(true, null);
		Subscriber<GoodsListResponse> subscriber = new Subscriber<GoodsListResponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				recycler.onRefreshCompleted();
				toggleShowLoading(false, null);
			}

			@Override
			public void onNext(GoodsListResponse goodsListResponse) {
				recycler.onRefreshCompleted();
				toggleShowLoading(false, null);
				if (currentPage == 1) {
					mDataList.clear();
				}
				if (goodsListResponse != null && goodsListResponse.getObj() != null && goodsListResponse.getObj().getResultlist() != null && goodsListResponse.getObj().getResultlist().size() > 0) {
					imgBaseUrl = goodsListResponse.getMsg();
					mDataList.addAll(goodsListResponse.getObj().getResultlist());
					totalPage = goodsListResponse.getObj().getTotalrecord();
					if (mDataList.size() >= totalPage) {
						recycler.enableLoadMore(false);
					} else {
						recycler.enableLoadMore(true);
					}
					currentPage++;
				}

				if (mDataList.size() > 0) {
					startRefreshTime();
				}

				if (mDataList.size() <= 0) {
					toggleShowEmpty(true, "该分类没有商品", null, null);
				}

				adapter.notifyDataSetChanged();
			}
		};
		HttpMethod.getInstance().getGoodsList(subscriber, request);
	}

	public void startRefreshTime() {
//		if (timerTask != null) {
//			timerTask.cancel();
//			refreshShops.clear();
//		}
//		timerTask = new MyTimerTask(this);
//		new Timer().schedule(timerTask, 0, 1000);
	}

	public void upShopState(ServceMessageBean bean) {
		if (mDataList==null){
			return;
		}
		for (int i = 0; i < mDataList.size(); i++) {
			GoodsListBean model = mDataList.get(i);
			if (model.getCid().equals(bean.getCid())) {
				//找到要更新的数据了和界面了
				model.setCountdown(bean.getCountdown());
				model.setLastbid(bean.getLastbid());
				model.setNowprice(bean.getNowprice());
				model.setStatus(bean.getStatus());
				adapter.notifyItemChanged(i, 2);
				break;
			}
		}
	}

	@Override
	protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
		return new MyViewHodler(getActivity().getLayoutInflater().inflate(R.layout.viewholder_alls_item, parent, false));
	}

	@Override
	public void onRefresh(int action) {
		if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
			currentPage = 1;
		}
		getData();
	}

	class MyViewHodler extends BaseViewHolder {

		@BindView(R.id.img)
		SketchImageView img;
		@BindView(R.id.tv_nowprice)
		TextView tv_nowprice;
		@BindView(R.id.tv_name)
		TextView tv_name;
		@BindView(R.id.btn_go)
		FlatButton btn_go;
		@BindView(R.id.tv_count_down)
		TextView tv_count_down;
		@BindView(R.id.img_ok)
		SketchImageView img_ok;

		public MyViewHodler(View itemView) {
			super(itemView);
		}

		@Override
		public void onBindViewHolder(int position) {
			final GoodsListBean model = mDataList.get(position);

			img.displayImage(imgBaseUrl + model.getHeadimage());
			tv_name.setText(model.getName());
			tv_nowprice.setText("￥" + model.getNowprice());
			tv_count_down.setText(FormatUtils.formatCountDown(model.getCountdown()));

			img_ok.setVisibility(View.GONE);

			btn_go.setText("我要拍");
			btn_go.setEnabled(true);

			switch (model.getStatus()) {
				case 1:
					//倒计时
					refreshShops.put(position, this);
					break;
				case 2:
					img_ok.setVisibility(View.VISIBLE);
					img_ok.setBackgroundColor(Color.parseColor("#33000000"));
					img_ok.displayResourceImage(R.drawable.ic_ycj);
					btn_go.setText("已成交");
					btn_go.setEnabled(false);
					tv_count_down.setText("00:00:00");
					//已成交
					break;
				case 3:
					img_ok.setVisibility(View.VISIBLE);
					img_ok.setBackgroundColor(Color.parseColor("#33000000"));
					img_ok.displayResourceImage(R.drawable.ic_ycj);
					btn_go.setText("已成交");
					btn_go.setEnabled(false);
					tv_count_down.setText("00:00:00");
					//暂停
					break;
			}
			btn_go.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Bundle bundle = new Bundle();
					bundle.putString("id", model.getId());
					bundle.putString("view", "3");
					doActivity(ShopDetailActivity.class, bundle);
				}
			});
		}


		@Override
		public void onBindViewHolder(int position, List<Object> payloads) {
			for (Object payload : payloads) {
				// 刷新倒计时
				if (payload instanceof Integer && ((int) payload) == 2) {
					final GoodsListBean model = (GoodsListBean) mDataList.get(position);
					//开始动画吧tv_name
					ObjectAnimator obj = ObjectAnimator.ofFloat(tv_name, "scaleX", 1f, 1.5f);
					obj.setRepeatMode(ObjectAnimator.REVERSE);
					obj.setRepeatCount(1);
					obj.setInterpolator(new OvershootInterpolator());
					obj.start();

					ObjectAnimator obj1 = ObjectAnimator.ofFloat(tv_name, "scaleY", 1f, 1.5f);
					obj1.setRepeatMode(ObjectAnimator.REVERSE);
					obj1.setRepeatCount(1);
					obj1.setInterpolator(new OvershootInterpolator());
					obj1.start();

					ObjectAnimator obj2 = ObjectAnimator.ofFloat(tv_nowprice, "alpha", 1f, 0.3f);
					obj2.setRepeatMode(ObjectAnimator.REVERSE);
					obj2.setRepeatCount(1);
					obj2.setInterpolator(new OvershootInterpolator());
					obj2.start();

					tv_name.setText(model.getLastbid());
					tv_nowprice.setText("￥" + model.getNowprice());
					tv_count_down.setText(FormatUtils.formatCountDown(model.getCountdown()));

					img_ok.setVisibility(View.GONE);

					btn_go.setText("我要拍");
					btn_go.setEnabled(true);


					switch (model.getStatus()) {
						case 1:
							//倒计时
							break;
						case 2:
							img_ok.setVisibility(View.VISIBLE);
							img_ok.setBackgroundColor(Color.parseColor("#33000000"));
							img_ok.displayResourceImage(R.drawable.ic_ycj);
							btn_go.setText("竞拍已结束");
							btn_go.setEnabled(false);
							tv_count_down.setText("00:00:00");
							//已成交
							break;
						case 3:
							img_ok.setVisibility(View.VISIBLE);
							img_ok.setBackgroundColor(Color.parseColor("#33000000"));
							img_ok.displayResourceImage(R.drawable.ic_ycj);
							btn_go.setText("竞拍已结束");
							btn_go.setEnabled(false);
							tv_count_down.setText("00:00:00");
							//暂停
							break;
					}
				}
			}
		}

		@Override
		public void onItemClick(View view, int position) {
			Bundle bundle = new Bundle();
			bundle.putString("id", mDataList.get(position).getId());
			bundle.putString("view", "3");
			doActivity(ShopDetailActivity.class, bundle);
		}
	}


	public static class MyHanlder extends Handler {

	}

	public static class MyRunnable implements Runnable {

		WeakReference<AllsFragment> fragment;

		public MyRunnable(AllsFragment fragment) {
			this.fragment = new WeakReference<>(fragment);
		}

		@Override
		public void run() {
			if (fragment.get() == null) {
				return;
			}
			if (fragment.get().mDataList.size() == 0) {
				return;
			}
			synchronized (fragment.get().mDataList) {
				for (int i = 0; i < fragment.get().refreshShops.size(); i++) {
					int key = fragment.get().refreshShops.keyAt(i);
					MyViewHodler holder = fragment.get().refreshShops.get(key);
					int countdown =  fragment.get().mDataList.get(key).getCountdown();
					fragment.get().mDataList.get(key).setCountdown((--countdown) <= 1 ? 1 : countdown);
					holder.tv_count_down.setText(FormatUtils.formatCountDown(( fragment.get().mDataList.get(key)).getCountdown()));
				}
			}
		}
	}

	public static class MyTimerTask extends TimerTask {

		WeakReference<AllsFragment> fragment;

		public MyTimerTask(AllsFragment handlerWeakReference) {
			this.fragment = new WeakReference<>(handlerWeakReference);
		}

		@Override
		public void run() {
			if (fragment.get() != null) {
				fragment.get().mHandler.post(fragment.get().mRefreshTimeRunnable);
			}
		}
	}

}
