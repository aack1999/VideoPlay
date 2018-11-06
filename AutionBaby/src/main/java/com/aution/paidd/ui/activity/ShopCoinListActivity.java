package com.aution.paidd.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.framework.core.base.BaseActivity;
import com.framework.core.widget.FlatButton;
import com.aution.paidd.R;
import com.aution.paidd.bean.UserBean;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.ui.adapter.ViewPagerAdapter;
import com.aution.paidd.ui.fragment.IncomeDetailFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.xiaopan.sketch.SketchImageView;

/**
 * 购币明细
 * Created by quxiang on 2017/9/1.
 */

public class ShopCoinListActivity extends BaseActivity {

	@BindView(R.id.img_gb)
	SketchImageView img_gb;
	@BindView(R.id.tv_shop_coin)
	TextView tv_shop_coin;
	@BindView(R.id.viewpager)
	ViewPager viewPager;

	@BindView(R.id.btn_mx)
	FlatButton btn_mx;
	@BindView(R.id.btn_zc)
	FlatButton btn_zc;

	@BindView(R.id.temp)
	TextView title;

	UserBean model;

	List<Fragment> fragments;

	String url;

	@Override
	public int getContentLayoutID() {
		return R.layout.activity_shop_coin;
	}

	public void addFragment(int type, int state) {
		fragments.add(IncomeDetailFragment.newFragment(type, state));
	}

	@Override
	public void initValue() {
		fragments = new ArrayList<>();
		model = CacheData.getInstance().getUserData();
		int type = getIntent().getIntExtra("type", 1);
		switch (type) {
			case 1:
				setTitle("我的拍币");
				title.setText("拍币合计");
				img_gb.displayResourceImage(R.drawable.ic_pbhj_bg);
				tv_shop_coin.setText(model.getPatmoney() + "");
				addFragment(1, 1);
				addFragment(1, 2);
				url= HttpMethod.BASE_URL+"bebox";
				break;
			case 2:
				setTitle("我的赠币");
				title.setText("赠币合计");
				img_gb.displayResourceImage(R.drawable.ic_zbhj_bg);
				tv_shop_coin.setText(model.getGivemoney() + "");
				addFragment(2, 1);
				addFragment(2, 2);
				url= HttpMethod.BASE_URL+"bebox";
				break;
			case 3:
				setTitle("我的购币");
				title.setText("购币合计");
				img_gb.displayResourceImage(R.drawable.ic_gbhj_bg);
				tv_shop_coin.setText(model.getBuymoney() + "");
				addFragment(3, 1);
				addFragment(3, 2);
				url= HttpMethod.BASE_URL+"bebox";
				break;
		}


		viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));

		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				if (position==0){
					btn_mx.setBackgroundColor(Color.parseColor("#55000000"));
					btn_zc.setBackgroundColor(Color.parseColor("#22000000"));
				}else {
					btn_mx.setBackgroundColor(Color.parseColor("#22000000"));
					btn_zc.setBackgroundColor(Color.parseColor("#55000000"));
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		btn_mx.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				viewPager.setCurrentItem(0);
			}
		});
		btn_zc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				viewPager.setCurrentItem(1);
			}
		});

		findViewById(R.id.btn_help).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Bundle bundle=new Bundle();
				bundle.putString("url",url);
				doActivity(H5Activity.class,bundle);
			}
		});
	}
}
