package com.aution.paidd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.framework.core.base.BaseListActivity;
import com.framework.core.widget.pull.BaseViewHolder;
import com.aution.paidd.R;
import com.aution.paidd.bean.AreaInfo;
import com.aution.paidd.bean.CItyInfoBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by quxiang on 2017/2/22.
 */

public class CityListActivity extends BaseListActivity<AreaInfo> {

    String title;

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_base_list;
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.enablePullToRefresh(false);
        recycler.enableLoadMore(false);
        title = getIntent().getStringExtra("title");
        setTitle(title);
        mDataList.addAll((ArrayList) getIntent().getSerializableExtra("citylist"));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(getLayoutInflater().inflate(R.layout.activity_city_list_item, parent, false));
    }

    @Override
    public void onRefresh(int action) {

    }

    class MyViewHolder extends BaseViewHolder {

        @BindView(R.id.tv_title)
        TextView tv_title;

        public MyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            AreaInfo model = mDataList.get(position);
            tv_title.setText(model.name);
        }

        @Override
        public void onItemClick(View view, int position) {
            AreaInfo model = mDataList.get(position);
            if (model.areaList != null && model.areaList.size() > 0) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("citylist", model.areaList);
                if ("选择省市".equals(title)) {
                    bundle.putString("title", "选择市区");
                    CItyInfoBean bean = new CItyInfoBean();
                    bean.setPcode(model.code);
                    bean.setPname(model.name);
                    bundle.putSerializable("citybean", bean);
                } else if ("选择市区".equals(title)) {
                    CItyInfoBean bean = (CItyInfoBean) getIntent().getSerializableExtra("citybean");
                    bean.setCcode(model.code);
                    bean.setCname(model.name);
                    bundle.putString("title", "选择区县");
                    bundle.putSerializable("citybean", bean);
                }
                doActivityForResult(CityListActivity.class, bundle, 100);
            } else {
                CItyInfoBean bean = (CItyInfoBean) getIntent().getSerializableExtra("citybean");
                if ("选择市区".equals(title)) {
                    bean.setCcode(model.code);
                    bean.setCname(model.name);
                } else {
                    bean.setAcode(model.code);
                    bean.setAname(model.name);
                }
                EventBus.getDefault().post(bean);
                finish(200);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            finish(200);
        }
    }


    public void finish(int code) {
        setResult(code);
        super.finish();
    }
}
