package com.aution.paidd.ui.activity;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framework.core.base.BaseListActivity;
import com.framework.core.utils.DisplayUtils;
import com.framework.core.widget.pull.BaseViewHolder;
import com.aution.paidd.R;
import com.aution.paidd.bean.LrecordBean;
import com.aution.paidd.common.Constant;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.request.BaseIdRequest;
import com.aution.paidd.response.LrecordResponse;

import java.text.DecimalFormat;

import butterknife.BindView;
import me.xiaopan.sketch.SketchImageView;
import me.xiaopan.sketch.request.DisplayOptions;
import me.xiaopan.sketch.shaper.CircleImageShaper;
import rx.Subscriber;

/**
 * 参与记录
 * Created by quxiang on 2017/9/2.
 */

public class JoinListActivity extends BaseListActivity<LrecordBean> {

	@Override
	protected void setUpData() {
		super.setUpData();
		setTitle("出价记录");
		getData(true);
		recycler.enablePullToRefresh(false);
	}

	@Override
	protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {

		if (viewType==mDataList.size()-1){
			TextView tv=new TextView(this);
			int dp10=DisplayUtils.dip2px(this,10);
			tv.setTextColor(getResources().getColor(R.color.base_text_title));
			tv.setTextSize(14f);
			tv.setWidth(DisplayUtils.getScreenW());
			tv.setGravity(Gravity.CENTER);
			tv.setPadding(dp10,dp10,dp10,dp10);
			return new InfoViewHolder(tv);
		}

		return new MyViewHolder(getLayoutInflater().inflate(R.layout.item_join_list,parent,false));
	}

	@Override
	protected int getItemType(int position) {
		return position;
	}

	@Override
	public int getContentLayoutID() {
		return R.layout.activity_base_list;
	}

	@Override
	public void onRefresh(int action) {

	}

	public void getData(boolean isShow){
		if (isShow){
			currentPage=1;
		}
		toggleShowLoading(isShow,null);
		Subscriber<LrecordResponse> subscriber=new Subscriber<LrecordResponse>() {
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
			public void onNext(LrecordResponse response) {
				toggleShowLoading(false,null);
				recycler.onRefreshCompleted();
				if (response != null) {
					if (response.getCode() == 10000) {
						if (response.getObj() != null && response.getObj().getResultlist() != null) {
							if (currentPage==1){
								mDataList.clear();
							}
							mDataList.addAll(response.getObj().getResultlist());
							mDataList.add(null);
							recycler.enableLoadMore(false);
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
		request.setCurrentpage("1");
		request.setMaxresult("20");
		request.setAid(getIntent().getStringExtra("aid"));
		HttpMethod.getInstance().getJoinRecordList(subscriber,request);
	}

	class MyViewHolder extends BaseViewHolder{

		@BindView(R.id.img_type)
		SketchImageView img_type;
		@BindView(R.id.tv_nameid)
		TextView tv_nameid;
		@BindView(R.id.tv_nowprice)
		TextView tv_nowprice;
		@BindView(R.id.tv_local)
		TextView tv_local;
		@BindView(R.id.tv_state)
		TextView tv_state;

		public MyViewHolder(View itemView) {
			super(itemView);
		}

		@Override
		public void onBindViewHolder(int position) {
			tv_nameid.setText(mDataList.get(position).getNickname());
			tv_local.setText(mDataList.get(position).getLocal());

			float kk = Float.parseFloat(getIntent().getStringExtra("nowprice"));

			DecimalFormat decimalFormat = new DecimalFormat("0.0");
			String temp = "￥" + decimalFormat.format(kk - 0.1 * position);

			tv_nowprice.setText(temp);
			tv_state.setText("2".equals(mDataList.get(position).getStatus())?"领先":"出局");

			DisplayOptions options = new DisplayOptions();
			options.setImageShaper(new CircleImageShaper());
			options.setErrorImage(R.drawable.userpic);
			options.setLoadingImage(R.drawable.userpic);
			img_type.setOptions(options);
			img_type.displayImage(mDataList.get(position).getImg());


			if (position==0){
				tv_nameid.setTextColor(getResources().getColor(R.color.theme_color));
				tv_local.setTextColor(getResources().getColor(R.color.theme_color));
				tv_nowprice.setTextColor(getResources().getColor(R.color.theme_color));
				tv_state.setTextColor(getResources().getColor(R.color.theme_color));
			}else {
				tv_nameid.setTextColor(getResources().getColor(R.color.base_text_title));
				tv_local.setTextColor(getResources().getColor(R.color.base_text_title));
				tv_nowprice.setTextColor(getResources().getColor(R.color.base_text_title));
				tv_state.setTextColor(getResources().getColor(R.color.base_text_title));
			}

		}

		@Override
		public void onItemClick(View view, int position) {

		}
	}

	class InfoViewHolder extends BaseViewHolder{

		public InfoViewHolder(View itemView) {
			super(itemView);
		}

		@Override
		public void onBindViewHolder(int position) {
			TextView tv=(TextView) itemView;
			tv.setText("~只显示最近20条数据~");
		}

		@Override
		public void onItemClick(View view, int position) {

		}
	}
}
