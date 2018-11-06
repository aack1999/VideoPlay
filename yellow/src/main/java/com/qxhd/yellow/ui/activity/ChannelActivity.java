package com.qxhd.yellow.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.framework.core.base.BaseListActivity;
import com.framework.core.widget.pull.BaseViewHolder;
import com.framework.core.widget.pull.layoutmanager.ILayoutManager;
import com.framework.core.widget.pull.layoutmanager.MyGridLayoutManager;
import com.qxhd.yellow.R;

import butterknife.BindView;

public class ChannelActivity extends BaseListActivity<String> {

    @BindView(R.id.tab_layout)
    TabLayout tab_layout;

    String mTitles[]={"全部","无码","高清","大奶","青春","好人","美女","制服诱惑"};

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(getLayoutInflater().inflate(R.layout.activity_channel_item,parent,false));
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_channel;
    }


    @Override
    protected ILayoutManager getLayoutManager() {
        return new MyGridLayoutManager(this,2);
    }

    public void onBack(View v){
        finish();
    }

    @Override
    protected void setUpData() {
        super.setUpData();

        for (int i = 0; i < 20; i++) {
            mDataList.add(null);
        }

        for (int i = 0; i < mTitles.length; i++) {
            tab_layout.addTab(tab_layout.newTab().setText(mTitles[i]));
        }


    }

    @Override
    public void onRefresh(int action) {

    }

    class MyViewHolder extends BaseViewHolder{

        public MyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {

        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }

}
