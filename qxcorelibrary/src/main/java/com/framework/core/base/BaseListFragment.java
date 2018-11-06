package com.framework.core.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.framework.core.R;
import com.framework.core.common.WaitDialog;
import com.framework.core.viewcontroller.VaryViewHelperController;
import com.framework.core.widget.pull.BaseListAdapter;
import com.framework.core.widget.pull.BaseViewHolder;
import com.framework.core.widget.pull.DividerItemDecoration;
import com.framework.core.widget.pull.PullRecycler;
import com.framework.core.widget.pull.layoutmanager.ILayoutManager;
import com.framework.core.widget.pull.layoutmanager.MyLinearLayoutManager;

import java.util.ArrayList;

import butterknife.ButterKnife;


/**
 * Created by Stay on 8/3/16.
 * Powered by www.stay4it.com
 */
public abstract class BaseListFragment<T> extends BaseFragment implements PullRecycler.OnRecyclerRefreshListener {
    protected BaseListAdapter adapter;
    protected ArrayList<T> mDataList;
    protected PullRecycler recycler;
    protected int currentPage=1;
    protected int totalPage=1;
    protected int rows=20;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        recycler = (PullRecycler) view.findViewById(R.id.pullRecycler);
        recycler.setOnRefreshListener(this);
        recycler.setLayoutManager(getLayoutManager());
        recycler.addItemDecoration(getItemDecoration());
        adapter = new ListAdapter();
        recycler.setAdapter(adapter);
        setUpData();
        super.onViewCreated(view, savedInstanceState);
    }

    public <T extends View>T getView(int resid){
        return (T)root.findViewById(resid);
    }

    protected void setUpData() {
        mDataList=new ArrayList<>();
    }

    protected ILayoutManager getLayoutManager() {
        return new MyLinearLayoutManager(getContext());
    }


    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST, R.drawable.list_divider);
    }


    @Override
    protected View getLoadingTargetView() {
        return recycler;
    }

    public void doActivity(Class c) {
        Intent intent = new Intent(getContext(), c);
        startActivity(intent);
    }

    public void doActivity(Class c, Bundle bundle) {
        Intent intent = new Intent(getContext(), c);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    WaitDialog dialog;

    public void showDialog(String msg){
        dialog=new WaitDialog(getContext());
        if (!TextUtils.isEmpty(msg)){
            dialog.setContent(msg);
        }else {
            dialog.setContent("正在请求数据");
        }
        dialog.show();
    }

    public void closeDialog(){
        if (dialog!=null){
            dialog.dismiss();
            dialog=null;
        }
    }

    /**
     * toggle show loading
     *
     * @param toggle
     */
    protected void toggleShowLoading(boolean toggle, String msg) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showLoading(msg);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * toggle show empty
     *
     * @param toggle
     */
    protected void toggleShowEmpty(boolean toggle, String msg,String btnText, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showEmpty(msg,btnText, onClickListener);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    public void showToast(String msg){
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }

    public class ListAdapter extends BaseListAdapter {

        @Override
        protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
            return getViewHolder(parent, viewType);
        }

        @Override
        protected int getDataCount() {
            return mDataList != null ? mDataList.size() : 0;
        }

        @Override
        protected int getDataViewType(int position) {
            return getItemType(position);
        }

        @Override
        public boolean isSectionHeader(int position) {
            return BaseListFragment.this.isSectionHeader(position);
        }

        @Override
        public void onViewRecycled(BaseViewHolder holder) {
            super.onViewRecycled(holder);
            holder.onViewRecycled();
        }
    }


    protected boolean isSectionHeader(int position) {
        return false;
    }

    public int getItemType(int position) {
        return 0;
    }

    protected abstract BaseViewHolder getViewHolder(ViewGroup parent, int viewType);


}
