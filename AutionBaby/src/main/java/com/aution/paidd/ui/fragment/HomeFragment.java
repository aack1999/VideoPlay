package com.aution.paidd.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.core.base.BaseListFragment;
import com.framework.core.imp.GlideImageLoader;
import com.framework.core.model.BannerBean;
import com.framework.core.utils.DisplayUtils;
import com.framework.core.widget.FlatButton;
import com.framework.core.widget.pull.BaseViewHolder;
import com.framework.core.widget.pull.PullRecycler;
import com.framework.core.widget.pull.layoutmanager.ILayoutManager;
import com.framework.core.widget.pull.layoutmanager.MyGridLayoutManager;
import com.nineoldandroids.animation.ObjectAnimator;
import com.aution.paidd.R;
import com.aution.paidd.bean.GoodsListBean;
import com.aution.paidd.bean.ServceMessageBean;
import com.aution.paidd.bean.ShopRecordBean;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.Constant;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.model.ChannelEntity;
import com.aution.paidd.model.MuiltType;
import com.aution.paidd.request.BaseIdRequest;
import com.aution.paidd.response.BannerResponse;
import com.aution.paidd.response.BaseResponse;
import com.aution.paidd.response.GoodsListResponse;
import com.aution.paidd.response.HeadLineResponse;
import com.aution.paidd.response.ShopRecordResponse;
import com.aution.paidd.ui.activity.H5Activity;
import com.aution.paidd.ui.activity.LoginActivity;
import com.aution.paidd.ui.activity.LuckyShowActivity;
import com.aution.paidd.ui.activity.PayCenterActivity;
import com.aution.paidd.ui.activity.ShopDetailActivity;
import com.aution.paidd.ui.activity.SignActivity;
import com.aution.paidd.ui.adapter.HeaderChannelAdapter;
import com.aution.paidd.ui.widget.FixedGridView;
import com.aution.paidd.ui.widget.MarqueeView;
import com.aution.paidd.utils.FormatUtils;
import com.toprightmenu.MenuItem;
import com.toprightmenu.TopRightMenu;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import me.xiaopan.sketch.SketchImageView;
import rx.Subscriber;

/**
 * Created by quxiang on 2017/8/29.
 */

public class HomeFragment extends BaseListFragment<MuiltType> {


	List<BannerBean> mDataItem;
	Banner banner;
	String imgBaseUrl;

	MarqueeView view_scolltext;

	//全局定时器
	SparseArray<MyViewHodler> refreshShops;
	SparseArray<MyBuysViewHolder> refreshShops_mybuy;
	Handler mHandler;
	Runnable mRefreshTimeRunnable;
	TimerTask timerTask;

	HeadLineResponse headLineResponse;

	MyGridLayoutManager linearLayoutManager;

	@BindView(R.id.suspension_bar)
	LinearLayout mSuspensionBar;

	View tab_item;

	int tabIndex = 0;

	@Override
	public int getContentLayoutID() {
		return R.layout.fragment_home;
	}

	@Override
	public void initValue() {
		initData();
		mDataItem = new ArrayList<>();
		getBannerData();
		getGoodsListData();
//		upScrollListener();

//		mSuspensionBar.findViewById(R.id.btn_zzrp).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				selectTab(0, true);
//			}
//		});
//
//		mSuspensionBar.findViewById(R.id.btn_wzp).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				selectTab(1, true);
//			}
//		});
//
//		mSuspensionBar.findViewById(R.id.btn_xstj).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				selectTab(2, true);
//			}
//		});
//
//		mSuspensionBar.findViewById(R.id.btn_wdsc).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				selectTab(3, true);
//			}
//		});


//		root.findViewById(R.id.btn_more).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				TopRightMenu mTopRightMenu = new TopRightMenu(getActivity());
//				//添加菜单项
//				List<MenuItem> menuItems = new ArrayList<>();
//				menuItems.add(new MenuItem(R.drawable.ic_rmhd, "热门活动"));
//				menuItems.add(new MenuItem(R.drawable.ic_xxgg, "消息公告"));
//				mTopRightMenu
//						.setAnimationStyle(R.style.TRM_ANIM_STYLE)
//						.addMenuList(menuItems)
//						.setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
//							@Override
//							public void onMenuItemClick(int position) {
//								Bundle bundle = new Bundle();
//								switch (position) {
//									case 0:
//										bundle.putString("url", HttpMethod.BASE_URL + "playcenter");
//										doActivity(H5Activity.class, bundle);
//										break;
//									case 1:
//										bundle.putString("url", HttpMethod.BASE_URL + "infors");
//										doActivity(H5Activity.class, bundle);
//										break;
//								}
//							}
//						})
//						.showAsDropDown(view, -DisplayUtils.dip2px(getContext(), 20), 0);
//			}
//		});


	}

	@Override
	protected void onFirstUserVisible() {

	}


	public void initData() {
		refreshShops = new SparseArray<>();
		refreshShops_mybuy=new SparseArray<>();
		mHandler = new MyHanlder();
		timerTask = new MyTimerTask(this);
		mRefreshTimeRunnable = new MyRunnable(this);
	}

	public void selectTab(int index, boolean isLoad) {

		if ((index == 1||index==3 )&& !CacheData.getInstance().isLogin()) {
			goToLogin();
			return;
		}

		mSuspensionBar.findViewById(R.id.tv_title_zzrp).setSelected(index == 0);
		mSuspensionBar.findViewById(R.id.line_zzrp).setVisibility(index == 0 ? View.VISIBLE : View.INVISIBLE);
		mSuspensionBar.findViewById(R.id.tv_title_wzp).setSelected(index == 1);
		mSuspensionBar.findViewById(R.id.line_wzp).setVisibility(index == 1 ? View.VISIBLE : View.INVISIBLE);
		mSuspensionBar.findViewById(R.id.tv_title_xstj).setSelected(index == 2);
		mSuspensionBar.findViewById(R.id.line_xstj).setVisibility(index == 2 ? View.VISIBLE : View.INVISIBLE);
		mSuspensionBar.findViewById(R.id.tv_title_wdsc).setSelected(index == 3);
		mSuspensionBar.findViewById(R.id.line_wdsc).setVisibility(index == 3 ? View.VISIBLE : View.INVISIBLE);

		tab_item.findViewById(R.id.tv_title_zzrp).setSelected(index == 0);
		tab_item.findViewById(R.id.line_zzrp).setVisibility(index == 0 ? View.VISIBLE : View.INVISIBLE);
		tab_item.findViewById(R.id.tv_title_wzp).setSelected(index == 1);
		tab_item.findViewById(R.id.line_wzp).setVisibility(index == 1 ? View.VISIBLE : View.INVISIBLE);
		tab_item.findViewById(R.id.tv_title_xstj).setSelected(index == 2);
		tab_item.findViewById(R.id.line_xstj).setVisibility(index == 2 ? View.VISIBLE : View.INVISIBLE);
		tab_item.findViewById(R.id.tv_title_wdsc).setSelected(index == 3);
		tab_item.findViewById(R.id.line_wdsc).setVisibility(index == 3 ? View.VISIBLE : View.INVISIBLE);



		switch (index){
			case 0:
				if (isLoad) {
					currentPage = 1;
					getGoodsListData();
				}
				break;
			case 1:
				if (isLoad){
					currentPage=1;
					getMyBuysData();
				}
				break;
			case 2:
				if (isLoad) {
					currentPage = 1;
					getRecommendData();
				}
				break;
			case 3:
				if (isLoad){
					currentPage=1;
					getMyCollectList();
				}
				break;
		}
		tabIndex = index;
	}

	/**
	 * 获取新手推荐
	 */
	public void getRecommendData(){
		refreshShops_mybuy.clear();
		refreshShops.clear();
		BaseIdRequest request = new BaseIdRequest();
		request.setCurrentpage(currentPage + "");
		request.setNovices("2");
		request.setUid(CacheData.getInstance().getUserData().getUid());
		request.setUid(CacheData.getInstance().getUserData().getUid());
		request.setMaxresult(rows + "");
		Subscriber<GoodsListResponse> subscriber = new Subscriber<GoodsListResponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				recycler.onRefreshCompleted();
			}

			@Override
			public void onNext(GoodsListResponse goodsListResponse) {
				recycler.onRefreshCompleted();
				if (goodsListResponse != null && goodsListResponse.getObj() != null && goodsListResponse.getObj().getResultlist() != null && goodsListResponse.getObj().getResultlist().size() > 0) {
					imgBaseUrl = goodsListResponse.getMsg();
					if (currentPage == 1) {
						mDataList.clear();
						mDataList.add(new MuiltType().setItem_type(1));
						mDataList.add(new MuiltType().setItem_type(2));
					}
					mDataList.addAll(goodsListResponse.getObj().getResultlist());
					totalPage = goodsListResponse.getObj().getTotalrecord();
					if (mDataList.size()-2 >= totalPage) {
						recycler.enableLoadMore(false);
					} else {
						recycler.enableLoadMore(true);
					}
					adapter.notifyDataSetChanged();
					currentPage++;
				}

				if (mDataList.size() > 2) {
					startRefreshTime();
				}
			}
		};
		HttpMethod.getInstance().getGoodsList(subscriber, request);
	}


	public void upScrollListener() {
		recycler.getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				//获取第一个可见view的位置
				int firstItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
				if (firstItemPosition >= 1) {
					if (mSuspensionBar.getVisibility() != View.VISIBLE)
						mSuspensionBar.setVisibility(View.VISIBLE);
				} else {
					if (mSuspensionBar.getVisibility() != View.GONE)
						mSuspensionBar.setVisibility(View.GONE);
				}

			}
		});
	}


	public void upShopState(ServceMessageBean bean) {
		if (mDataList == null) {
			return;
		}
		for (int i = 1; i < mDataList.size(); i++) {

			if (mDataList.get(i) instanceof GoodsListBean) {
				GoodsListBean model = (GoodsListBean) mDataList.get(i);
				if (model.getItem_type() == 0 && model.getCid().equals(bean.getCid())) {
					//找到要更新的数据了和界面了
					model.setCountdown(bean.getCountdown());
					model.setLastbid(bean.getLastbid());
					model.setNowprice(bean.getNowprice());
					model.setStatus(bean.getStatus());
					adapter.notifyItemChanged(i, 2);
					break;
				}
			}else  if (mDataList.get(i) instanceof ShopRecordBean){
				ShopRecordBean model = (ShopRecordBean) mDataList.get(i);
				if (model.getItem_type() == 4 && model.getCid().equals(bean.getCid())) {
					//找到要更新的数据了和界面了
					model.setCountdown(bean.getCountdown());
					model.setLastbid(bean.getLastbid());
					model.setNowprice(Float.parseFloat(bean.getNowprice()));
					model.setStatus(bean.getStatus());
					adapter.notifyItemChanged(i, 2);
					break;
				}
			}


		}
	}

	@Override
	public void onRefresh(int action) {
		if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
			currentPage = 1;
			getBannerData();
//			getHeadLine();
			recycler.onRefreshCompleted();
		}

		switch (tabIndex) {
			case 0:
				getGoodsListData();
				break;
			case 1:
				getMyBuysData();
				break;
			case 2:
				getRecommendData();
				break;
			case 3:
				getMyCollectList();
				break;
		}
	}

	@Override
	protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {

		switch (viewType) {
			case 1:
				return new BannerViewHolder(getActivity().getLayoutInflater().inflate(R.layout.viewholder_home_banner, parent, false));
//			case 2:
//				return new TabViewHolder(getActivity().getLayoutInflater().inflate(R.layout.viewholder_home_tab_item, parent, false));
			case 3:
				return new NoDataViewHolder(getActivity().getLayoutInflater().inflate(R.layout.viewholder_detail_no_data, parent, false));
			case 4:
				return new MyBuysViewHolder(getActivity().getLayoutInflater().inflate(R.layout.viewholder_home_item, parent, false));
		}
		MyViewHodler mm = new MyViewHodler(getActivity().getLayoutInflater().inflate(R.layout.viewholder_home_item, parent, false));
		return mm;
	}


	@Override
	public int getItemType(int position) {
		return mDataList.get(position).getItem_type();
	}

	@Override
	protected ILayoutManager getLayoutManager() {
		linearLayoutManager = new MyGridLayoutManager(getContext(), 2);
		return linearLayoutManager;
	}

	@Override
	protected boolean isSectionHeader(int position) {
		int kk = getItemType(position);
		return kk == 1 || kk == 2 || kk == 3 ;
	}

	int page;
	int row;

	public void getMyBuysData() {
		refreshShops_mybuy.clear();
		refreshShops.clear();
		Subscriber<ShopRecordResponse> subscriber = new Subscriber<ShopRecordResponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(ShopRecordResponse baseResponse) {
				timerTask.cancel();
				recycler.onRefreshCompleted();
				if (baseResponse!=null){
					if (baseResponse.getCode()==10000){
						if (currentPage==1){
							mDataList.clear();
							mDataList.add(new MuiltType().setItem_type(1));
							mDataList.add(new MuiltType().setItem_type(2));
						}
						mDataList.addAll(baseResponse.getObj().getResultlist());
						if (mDataList.size()-2>=baseResponse.getObj().getTotalrecord()){
							recycler.enableLoadMore(false);
						}else {
							recycler.enableLoadMore(true);
						}
						currentPage++;
					}
				}
				if (mDataList.size() <= 2) {
					mDataList.add(new MuiltType().setItem_type(3));
				}
				adapter.notifyDataSetChanged();
				startRefreshTime();
			}
		};
		BaseIdRequest request = new BaseIdRequest();
		request.setUid(CacheData.getInstance().getUserData().getUid());
		request.setCurrentpage(page + "");
		request.setMaxresult(row + "");
		HttpMethod.getInstance().getGoodsMyBuy(subscriber, request);
	}

	@Override
	protected RecyclerView.ItemDecoration getItemDecoration() {
		return null;
	}

	class BannerViewHolder extends BaseViewHolder {

		@BindView(R.id.fix_gridview)
		FixedGridView fix_gridview;

		public BannerViewHolder(View itemView) {
			super(itemView);
			banner = (Banner) itemView.findViewById(R.id.banner);
			view_scolltext = (MarqueeView) itemView.findViewById(R.id.view_scolltext);
			banner.setViewPagerIsScroll(true);
			getHeadLine();
		}

		@Override
		public void onBindViewHolder(int position) {
			List<ChannelEntity> list = new ArrayList<>();
			list.add(new ChannelEntity("充值", R.drawable.ic_cz));
			list.add(new ChannelEntity("签到", R.drawable.ic_qd));
			//list.add(new ChannelEntity("晒单", R.drawable.ic_sd));
			list.add(new ChannelEntity("活动",R.drawable.ic_hd));
			list.add(new ChannelEntity("帮助", R.drawable.ic_help));
			fix_gridview.setAdapter(new HeaderChannelAdapter(getContext(), list));
			fix_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

				}
			});

			banner.setImageLoader(new GlideImageLoader());
			banner.setDelayTime(3000);
			banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
			banner.setOnBannerListener(new OnBannerListener() {
				@Override
				public void OnBannerClick(int position) {
//					if (!TextUtils.isEmpty(mDataItem.get(position).getUrl())) {
//						Bundle bundle = new Bundle();
//						bundle.putString("url", mDataItem.get(position).getUrl());
//						doActivity(H5Activity.class, bundle);
//					} else {
//						Bundle bundle = new Bundle();
//						bundle.putString("id", mDataItem.get(position).getId());
//						bundle.putString("view", "1");
//						doActivity(ShopDetailActivity.class, bundle);
//					}
				}
			});
			banner.setImages(mDataItem);
			banner.start();

//			view_scolltext.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
//				@Override
//				public void onItemClick(int position, TextView textView) {
//					Bundle bundle = new Bundle();
//					bundle.putString("id", headLineResponse.getObj().get(position).getCid());
//					bundle.putString("view", "1");
//					doActivity(ShopDetailActivity.class, bundle);
//				}
//			});
		}

		@Override
		public void onItemClick(View view, int position) {

		}
	}

	class TabViewHolder extends BaseViewHolder {


		public TabViewHolder(View itemView) {
			super(itemView);
			tab_item = itemView;
		}

		@Override
		public void onBindViewHolder(int position) {
			selectTab(tabIndex, false);
			tab_item.findViewById(R.id.btn_zzrp).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					selectTab(0, true);
				}
			});
			tab_item.findViewById(R.id.btn_wzp).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					selectTab(1, true);
				}
			});
			tab_item.findViewById(R.id.btn_xstj).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					selectTab(2, true);
				}
			});
			tab_item.findViewById(R.id.btn_wdsc).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					selectTab(3, true);
				}
			});
		}

		@Override
		public void onItemClick(View view, int position) {

		}
	}

	class NoDataViewHolder extends BaseViewHolder {

		@BindView(R.id.img)
		SketchImageView img;
		@BindView(R.id.tv_tips)
		TextView tv_tips;

		public NoDataViewHolder(View itemView) {
			super(itemView);

		}

		@Override
		public void onBindViewHolder(int position) {
			img.displayResourceImage(R.drawable.no_date);
			tv_tips.setText("暂无记录");
		}

		@Override
		public void onItemClick(View view, int position) {

		}
	}

	public void getBannerData() {
		Subscriber<BannerResponse> subscriber = new Subscriber<BannerResponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(BannerResponse bannerResponse) {
				if (bannerResponse != null && bannerResponse.getObj() != null && bannerResponse.getObj().size() > 0) {
					mDataItem.clear();
					for (int i = 0; i < bannerResponse.getObj().size(); i++) {
						BannerBean model = bannerResponse.getObj().get(i);
						model.setImg("https://img.alicdn.com/tfs/TB1eB1StxSYBuNjSspjXXX73VXa-520-280.jpg_q90_.webp");
						mDataItem.add(model);
					}
					banner.setImages(mDataItem);
					banner.start();
				}
			}
		};
		HttpMethod.getInstance().getBanner(subscriber);
	}

	public void getGoodsListData() {
		refreshShops_mybuy.clear();
		refreshShops.clear();
		BaseIdRequest request = new BaseIdRequest();
		request.setCurrentpage(currentPage + "");
		request.setNovices("1");
		request.setUid(CacheData.getInstance().getUserData().getUid());
		request.setMaxresult(rows + "");

		Subscriber<GoodsListResponse> subscriber = new Subscriber<GoodsListResponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				recycler.onRefreshCompleted();
			}

			@Override
			public void onNext(GoodsListResponse goodsListResponse) {
				recycler.onRefreshCompleted();
				if (goodsListResponse != null && goodsListResponse.getObj() != null && goodsListResponse.getObj().getResultlist() != null && goodsListResponse.getObj().getResultlist().size() > 0) {
					imgBaseUrl = goodsListResponse.getMsg();
					if (currentPage == 1) {
						mDataList.clear();
						mDataList.add(new MuiltType().setItem_type(1));
//						mDataList.add(new MuiltType().setItem_type(2));
					}
					mDataList.addAll(goodsListResponse.getObj().getResultlist());
					totalPage = goodsListResponse.getObj().getTotalrecord();
					if (mDataList.size()-2 >= totalPage) {
						recycler.enableLoadMore(false);
					} else {
						recycler.enableLoadMore(true);
					}
					adapter.notifyDataSetChanged();
					currentPage++;
				}

				if (mDataList.size() > 2) {
					startRefreshTime();
				}
			}
		};
		HttpMethod.getInstance().getGoodsList(subscriber, request);
	}

	/**
	 * 收藏列表
	 */
	public void getMyCollectList(){
		refreshShops_mybuy.clear();
		refreshShops.clear();
		Subscriber<GoodsListResponse> subscriber=new Subscriber<GoodsListResponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(GoodsListResponse goodsListResponse) {
				recycler.onRefreshCompleted();
				if (goodsListResponse != null && goodsListResponse.getObj() != null && goodsListResponse.getObj().getResultlist() != null && goodsListResponse.getObj().getResultlist().size() > 0) {
					imgBaseUrl = goodsListResponse.getMsg();
					if (currentPage == 1) {
						mDataList.clear();
						mDataList.add(new MuiltType().setItem_type(1));
						mDataList.add(new MuiltType().setItem_type(2));
					}
					mDataList.addAll(goodsListResponse.getObj().getResultlist());
					totalPage = goodsListResponse.getObj().getTotalrecord();
					if (mDataList.size()-2 >= totalPage) {
						recycler.enableLoadMore(false);
					} else {
						recycler.enableLoadMore(true);
					}
					adapter.notifyDataSetChanged();
					currentPage++;
				}else {
					if (currentPage == 1) {
						mDataList.clear();
						mDataList.add(new MuiltType().setItem_type(1));
						mDataList.add(new MuiltType().setItem_type(2));
					}
				}

				if (mDataList.size() > 2) {
					startRefreshTime();
				}else {
					mDataList.add(new MuiltType().setItem_type(3));
					recycler.enableLoadMore(false);
				}
				adapter.notifyDataSetChanged();
			}
		};
		BaseIdRequest request=new BaseIdRequest();
		request.setMaxresult(row+"");
		request.setCurrentpage(currentPage+"");
		request.setUid(CacheData.getInstance().getUserData().getUid());
		HttpMethod.getInstance().getCollectList(subscriber,request);
	}

	public void getHeadLine() {

		if (headLineResponse != null) {
			return;
		}

		Subscriber<HeadLineResponse> subscriber = new Subscriber<HeadLineResponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(HeadLineResponse response) {

				headLineResponse = response;

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

	public void startRefreshTime() {
		if (timerTask != null) {
			timerTask.cancel();
		}
		timerTask = new MyTimerTask(this);
		new Timer().schedule(timerTask, 1000, 1000);
	}

	class MyViewHodler extends BaseViewHolder {

		@BindView(R.id.img)
		SketchImageView img;
		@BindView(R.id.tv_name)
		TextView tv_name;
		@BindView(R.id.tv_nowprice)
		TextView tv_nowprice;
		@BindView(R.id.tv_price)
		TextView tv_price;
		@BindView(R.id.tv_count_down)
		TextView tv_count_down;
		@BindView(R.id.img_ok)
		SketchImageView img_ok;
		@BindView(R.id.btn_go)
		FlatButton btn_go;
		int index;
		@BindView(R.id.img_collect)
		ImageView img_collect;

		public MyViewHodler(View itemView) {
			super(itemView);
		}

		@Override
		public void onBindViewHolder(int position) {
			index=position;

			final GoodsListBean model = (GoodsListBean) mDataList.get(position);

			img.displayImage(imgBaseUrl + model.getHeadimage());
			tv_name.setText(model.getName());
			tv_nowprice.setText("￥" + model.getNowprice());
			tv_price.setText("￥" + model.getPrice());
			tv_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			tv_count_down.setText(FormatUtils.formatCountDown(model.getCountdown()));
			img_ok.setVisibility(View.GONE);
			btn_go.setText("我要租");
			btn_go.setEnabled(true);

			//=========================添加代码
			img_collect.setImageResource(model.getState()==1?R.drawable.love_on:R.drawable.love);
			img_collect.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					int flag=model.getState()==1?2:1;
					collectShop(index,model.getCid(),flag);
				}
			});




			btn_go.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
                    Intent intent = new Intent(getContext(),H5Activity.class);
                    intent.putExtra("url","https://detail.tmall.com/item.htm?id=560516988038&ali_refid=a3_430583_1006:1102515942:N:x%E8%8B%B9%E6%9E%9C%20iphone%20x:f73adfce6a880fbfcc65a1aeef04240a&ali_trackid=1_f73adfce6a880fbfcc65a1aeef04240a&spm=a230r.1.14.1&sku_properties=10004:709990523;5919063:6536025");
                    intent.putExtra("title","iphone X");
                    intent.putExtra("money","￥233");
                    intent.putExtra("phone","15623385456");
                    startActivity(intent);
				}
			});

		}


		public void collectShop(final int index, String cid, final int flag){
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
							//改变成功
							if (flag==1){
								//收藏成功
								if (mDataList.get(index) instanceof GoodsListBean){
									((GoodsListBean) mDataList.get(index)).setState(1);
								}
							}else {
								//取消收藏成功
								if (tabIndex==3){
									//删除
									mDataList.remove(index);
									adapter.notifyDataSetChanged();
									return;
								}
								if (mDataList.get(index) instanceof GoodsListBean){
									((GoodsListBean) mDataList.get(index)).setState(0);
								}
							}
						}
						img_collect.setImageResource(flag==1?R.drawable.love_on:R.drawable.love);
					}
				}
			};
			BaseIdRequest request=new BaseIdRequest();
			request.setUid(CacheData.getInstance().getUserData().getUid());
			request.setCid(cid);
			request.setFlag(flag+"");
			HttpMethod.getInstance().getCollectShop(subscriber,request);
		}


		@Override
		public void onViewRecycled() {
			super.onViewRecycled();
			refreshShops.remove(index);
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

					btn_go.setText("我要竞拍");
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
							btn_go.setText("竞拍已结束");
							btn_go.setEnabled(false);
							tv_count_down.setText("00:00:00");

							refreshShops.remove(position);
							//已成交
							break;
						case 3:
							img_ok.setVisibility(View.VISIBLE);
							img_ok.setBackgroundColor(Color.parseColor("#33000000"));
							img_ok.displayResourceImage(R.drawable.ic_ycj);
							btn_go.setText("竞拍已结束");
							btn_go.setEnabled(false);
							tv_count_down.setText("00:00:00");
							refreshShops.remove(position);
							//暂停
							break;
					}

				}
			}
		}

		@Override
		public void onItemClick(View view, int position) {
            Intent intent = new Intent(getContext(),H5Activity.class);
            intent.putExtra("url","https://detail.tmall.com/item.htm?id=560516988038&ali_refid=a3_430583_1006:1102515942:N:x%E8%8B%B9%E6%9E%9C%20iphone%20x:f73adfce6a880fbfcc65a1aeef04240a&ali_trackid=1_f73adfce6a880fbfcc65a1aeef04240a&spm=a230r.1.14.1&sku_properties=10004:709990523;5919063:6536025");
            intent.putExtra("title","iphone X");
            intent.putExtra("money","￥233");
            intent.putExtra("phone","15623385456");
            startActivity(intent);
		}
	}

	class MyBuysViewHolder extends BaseViewHolder {

		@BindView(R.id.img)
		SketchImageView img;
		@BindView(R.id.tv_name)
		TextView tv_name;
		@BindView(R.id.tv_nowprice)
		TextView tv_nowprice;
		@BindView(R.id.tv_price)
		TextView tv_price;
		@BindView(R.id.tv_count_down)
		TextView tv_count_down;
		@BindView(R.id.img_ok)
		SketchImageView img_ok;
		@BindView(R.id.btn_go)
		FlatButton btn_go;

		ShopRecordBean model;

		int index;

		public MyBuysViewHolder(View itemViewName) {
			super(itemViewName);

		}

		@Override
		public void onBindViewHolder(int position) {
			index=position;
			model = (ShopRecordBean) mDataList.get(position);

			img.displayImage(imgBaseUrl + model.getHeadimage());
			tv_name.setText(model.getLastbid());
			tv_nowprice.setText("￥" + model.getNowprice());
			tv_price.setText("￥" + model.getPrice());
			tv_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);


			img_ok.setVisibility(View.GONE);

			btn_go.setText("我要竞拍");
			btn_go.setEnabled(true);

			switch (model.getStatus()) {
				case 1:
					//倒计时
					tv_count_down.setText(FormatUtils.formatCountDown(model.getCountdown()));
					refreshShops_mybuy.put(position, this);
					break;
				case 2:
					img_ok.setVisibility(View.VISIBLE);
					img_ok.setBackgroundColor(Color.parseColor("#33000000"));
					img_ok.displayResourceImage(R.drawable.ic_ycj);
					btn_go.setText("竞拍已结束");
					btn_go.setEnabled(false);
					tv_count_down.setText("00:00:00");

					refreshShops_mybuy.remove(position);
					//已成交
					break;
				case 3:
					img_ok.setVisibility(View.VISIBLE);
					img_ok.setBackgroundColor(Color.parseColor("#33000000"));
					img_ok.displayResourceImage(R.drawable.ic_ycj);
					btn_go.setText("竞拍已结束");
					btn_go.setEnabled(false);
					tv_count_down.setText("00:00:00");

					refreshShops_mybuy.remove(position);
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
		public void onViewRecycled() {
			super.onViewRecycled();
			refreshShops_mybuy.remove(index);
		}

		@Override
		public void onItemClick(View view, int position) {
			Bundle bundle = new Bundle();
			bundle.putString("id", model.getId());
			bundle.putString("view", "3");
			doActivity(ShopDetailActivity.class, bundle);
		}

		@Override
		public void onBindViewHolder(int position, List<Object> payloads) {
			for (Object payload : payloads) {
				// 刷新倒计时
				if (payload instanceof Integer && ((int) payload) == 2) {

					final ShopRecordBean model = (ShopRecordBean) mDataList.get(position);

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

					img_ok.setVisibility(View.GONE);

					btn_go.setText("我要竞拍");
					btn_go.setEnabled(true);


					switch (model.getStatus()) {
						case 1:
							//倒计时
							tv_count_down.setText(FormatUtils.formatCountDown(model.getCountdown()));
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
	}

	public static class MyHanlder extends Handler {

	}

	public static class MyRunnable implements Runnable {

		WeakReference<HomeFragment> fragment;

		public MyRunnable(HomeFragment fragment) {
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

			for (int i = 0; i < fragment.get().refreshShops.size(); i++) {
				int key = fragment.get().refreshShops.keyAt(i);
				MyViewHodler holder = fragment.get().refreshShops.get(key);
				GoodsListBean model=(GoodsListBean)fragment.get().mDataList.get(key);
				if (model.getStatus()==1){
					int countdown = model.getCountdown();
					model.setCountdown((--countdown) <= 1 ? 1 : countdown);
					holder.tv_count_down.setText(FormatUtils.formatCountDown(model.getCountdown()));
				}else{
					fragment.get().refreshShops.remove(key);
				}
			}

			for (int i = 0; i < fragment.get().refreshShops_mybuy.size(); i++) {
				int key = fragment.get().refreshShops_mybuy.keyAt(i);
				MyBuysViewHolder holder = fragment.get().refreshShops_mybuy.get(key);
				ShopRecordBean model=(ShopRecordBean)fragment.get().mDataList.get(key);
				if (model.getStatus()==1){
					int countdown = model.getCountdown();
					model.setCountdown((--countdown) <= 1 ? 1 : countdown);
					holder.tv_count_down.setText(FormatUtils.formatCountDown(model.getCountdown()));
				}else {
					fragment.get().refreshShops_mybuy.remove(key);
				}
			}



		}
	}

	public static class MyTimerTask extends TimerTask {

		WeakReference<HomeFragment> fragment;

		public MyTimerTask(HomeFragment handlerWeakReference) {
			this.fragment = new WeakReference<>(handlerWeakReference);
		}

		@Override
		public void run() {
			if (fragment.get() != null) {
				fragment.get().mHandler.post(fragment.get().mRefreshTimeRunnable);
			}
		}
	}


	public void goToLogin() {
		getActivity().startActivityForResult(new Intent(getContext(), LoginActivity.class), 100);
		getActivity().overridePendingTransition(R.anim.login_in_anim, R.anim.activity_nor_anim);
	}

}
