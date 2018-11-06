package com.qxhd.yellow.ui.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.framework.core.base.BaseListFragment;
import com.framework.core.widget.pull.BaseViewHolder;
import com.qxhd.yellow.R;
import com.qxhd.yellow.model.MuiltType;

public class FindFragment extends BaseListFragment<MuiltType> {

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(getActivity().getLayoutInflater().inflate(R.layout.viewholder_find_item,parent,false));
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_find;
    }

    @Override
    public void initValue() {
        for (int i = 0; i < 20; i++) {
            mDataList.add(null);
        }
    }

    @Override
    protected void onFirstUserVisible() {

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
