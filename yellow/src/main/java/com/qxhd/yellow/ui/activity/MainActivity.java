package com.qxhd.yellow.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.aspsine.fragmentnavigator.FragmentNavigator;
import com.framework.core.base.BaseActivity;
import com.qxhd.yellow.R;
import com.qxhd.yellow.ui.adapter.FragmentAdapter;
import com.qxhd.yellow.ui.fragment.FindFragment;
import com.qxhd.yellow.ui.fragment.HomeFragment;
import com.qxhd.yellow.ui.fragment.MeFragment;
import com.qxhd.yellow.ui.fragment.TagFragment;
import com.qxhd.yellow.widget.BottomNavigatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.OkHttpClient;

public class MainActivity extends BaseActivity {

    @BindView(R.id.botton_view)
    BottomNavigatorView botton_view;
    FragmentNavigator mNavigator;

    HomeFragment homeFragment;
    List<Fragment> fragments;

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void initValue() {
        initData();
    }

    private void initData(){
        fragments = new ArrayList<>();
        fragments.add(homeFragment = new HomeFragment());
        fragments.add(new TagFragment());
        fragments.add(new FindFragment());
        fragments.add(new MeFragment());
        mNavigator = new FragmentNavigator(getSupportFragmentManager(), new FragmentAdapter(fragments), R.id.container);
        botton_view.setOnBottomNavigatorViewItemClickListener(new BottomNavigatorView.OnBottomNavigatorViewItemClickListener() {
            @Override
            public void onBottomNavigatorViewItemClick(int position, View view) {
                selectTab(position);
            }
        });
        selectTab(0);


    }

    public void selectTab(int position) {
        botton_view.select(position);
        mNavigator.showFragment(position, false, true);
    }
}
