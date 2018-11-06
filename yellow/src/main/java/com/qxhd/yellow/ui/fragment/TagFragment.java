package com.qxhd.yellow.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.framework.core.base.BaseFragment;
import com.qxhd.yellow.R;
import com.qxhd.yellow.ui.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by quxiang on 2017/8/29.
 */

public class TagFragment extends BaseFragment {


    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    List<Fragment> fragments;

    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_tag;
    }


    @Override
    protected View getLoadingTargetView() {
        return null;
    }



    @Override
    public void initValue() {
        initData();
    }

    @Override
    protected void onFirstUserVisible() {

    }

    public void initData() {
        fragments=new ArrayList<>();
        fragments.add(ZytjFragment.newFragmet("专栏推荐"));
        fragments.add(BqsxFragment.newFragmet("标签筛选"));

        tabLayout.addTab(tabLayout.newTab().setText("专栏推荐"));
        tabLayout.addTab(tabLayout.newTab().setText("标签筛选"));
        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(),fragments));
        tabLayout.setupWithViewPager(viewPager);
    }



}
