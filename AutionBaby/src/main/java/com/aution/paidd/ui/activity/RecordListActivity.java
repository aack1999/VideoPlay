package com.aution.paidd.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;


import com.framework.core.base.BaseActivity;
import com.aution.paidd.R;
import com.aution.paidd.ui.adapter.ViewPagerAdapter;
import com.aution.paidd.ui.fragment.LuckyRecordListFragment;
import com.aution.paidd.ui.fragment.ShopRecordListFragment;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 我的购买记录
 * Created by yangjie on 2016/10/22.
 */
public class RecordListActivity extends BaseActivity {

    @BindView(R.id.tablayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    ArrayList<Fragment> fragments;

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_record;
    }

    @Override
    public void initValue() {
        setTitle("我的竞拍");

        fragments=new ArrayList<>();
        addAllFragment(0,"正在拍");
        addLuckyFragment(1,0,"我拍中");
        addAllFragment(1,"差价购");
        addLuckyFragment(2,1,"待付款");
        addLuckyFragment(3,2,"待签收");
        addLuckyFragment(4,3,"待晒单");
        ViewPagerAdapter adapter = new ViewPagerAdapter(this.getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("正在拍"));
        tabLayout.addTab(tabLayout.newTab().setText("我拍中"));
        tabLayout.addTab(tabLayout.newTab().setText("差价购"));
        tabLayout.addTab(tabLayout.newTab().setText("待付款"));
        tabLayout.addTab(tabLayout.newTab().setText("待签收"));
        tabLayout.addTab(tabLayout.newTab().setText("待晒单"));
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(getIntent().getIntExtra("index",0),false);

        viewPager.setOffscreenPageLimit(fragments.size());

    }

    public void addAllFragment(int type,String title){
        Bundle bundle = new Bundle();
        bundle.putString("title",title);
        bundle.putInt("type",type);
        ShopRecordListFragment fg = new ShopRecordListFragment();
        fg.setArguments(bundle);
        fragments.add(fg);
    }

    public void addLuckyFragment(int type,int view,String title){
        Bundle bundle = new Bundle();
        bundle.putString("view", view+"");
        bundle.putString("type",type+"");
        bundle.putString("title",title);
        LuckyRecordListFragment fg = new LuckyRecordListFragment();
        fg.setArguments(bundle);
        fragments.add(fg);
    }

}
