package com.framework.core.widget.pull;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.framework.core.R;
import com.framework.core.utils.DisplayUtils;
import com.framework.core.widget.CustomHeader;
import com.framework.core.widget.pull.layoutmanager.ILayoutManager;
import com.shizhefei.view.coolrefreshview.CoolRefreshView;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;


/**
 * Created by Stay on 5/3/16.
 * Powered by www.stay4it.com
 */
public class PullRecycler extends FrameLayout {
    private PtrFrameLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    public static final int ACTION_PULL_TO_REFRESH = 1;
    public static final int ACTION_LOAD_MORE_REFRESH = 2;
    public static final int ACTION_IDLE = 0;
    private OnRecyclerRefreshListener listener;
    private int mCurrentState = ACTION_IDLE;
    private boolean isLoadMoreEnabled = false;
    private boolean isPullToRefreshEnabled = true;
    private ILayoutManager mLayoutManager;
    private BaseListAdapter adapter;

    public PullRecycler(Context context) {
        super(context);
        setUpView();
    }

    public PullRecycler(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpView();
    }

    public PullRecycler(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUpView();
    }

    private void setUpView() {
        LayoutInflater.from(getContext()).inflate(R.layout.framework_pull_to_refresh, this, true);
        mSwipeRefreshLayout = (PtrFrameLayout) findViewById(R.id.swipeRefreshLayout);


        // header
//        final MaterialHeader header = new MaterialHeader(getContext());
//        int[] colors = getResources().getIntArray(R.array.theme_colors);
//        header.setColorSchemeColors(colors);
//        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
//        header.setPadding(0, DisplayUtils.dip2px(getContext(),15), 0, DisplayUtils.dip2px(getContext(),10));
//        header.setPtrFrameLayout(mSwipeRefreshLayout);

        CustomHeader header=new CustomHeader(getContext());


        mSwipeRefreshLayout.setLoadingMinTime(1000);
        mSwipeRefreshLayout.setDurationToCloseHeader(1500);
        mSwipeRefreshLayout.setHeaderView(header);
        mSwipeRefreshLayout.addPtrUIHandler(header);

        mSwipeRefreshLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                mCurrentState = ACTION_PULL_TO_REFRESH;
                listener.onRefresh(ACTION_PULL_TO_REFRESH);

            }
        });


//        mSwipeRefreshLayout.setJRefreshListener(new JRefreshLayout.JRefreshListener() {
//            @Override
//            public void onRefresh(JRefreshLayout refreshLayout) {
//                mCurrentState = ACTION_PULL_TO_REFRESH;
//                listener.onRefresh(ACTION_PULL_TO_REFRESH);
//            }
//        });



//        mSwipeRefreshLayout.setColorSchemeResources(R.color.theme_color);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mCurrentState = ACTION_PULL_TO_REFRESH;
//                listener.onRefresh(ACTION_PULL_TO_REFRESH);
//            }
//        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mCurrentState == ACTION_IDLE && isLoadMoreEnabled && checkIfNeedLoadMore()) {
                    mCurrentState = ACTION_LOAD_MORE_REFRESH;
                    adapter.onLoadMoreStateChanged(true);
                    mSwipeRefreshLayout.setEnabled(false);
                    listener.onRefresh(ACTION_LOAD_MORE_REFRESH);
                }
            }
        });
    }


    public PtrFrameLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    private boolean checkIfNeedLoadMore() {
        int lastVisibleItemPosition = mLayoutManager.findLastVisiblePosition();
        int totalCount = mLayoutManager.getLayoutManager().getItemCount();
        return totalCount - lastVisibleItemPosition < 5;
    }

    public void enableLoadMore(boolean enable) {
        isLoadMoreEnabled = enable;
    }

    public void enablePullToRefresh(boolean enable) {
        isPullToRefreshEnabled = enable;
        mSwipeRefreshLayout.setEnabled(enable);
    }

    public void setLayoutManager(ILayoutManager manager) {
        this.mLayoutManager = manager;
        mRecyclerView.setLayoutManager(manager.getLayoutManager());
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decoration) {
        if (decoration != null) {
            mRecyclerView.addItemDecoration(decoration);
        }
    }

    public void setAdapter(BaseListAdapter adapter) {
        this.adapter = adapter;
        mRecyclerView.setAdapter(adapter);
        mLayoutManager.setUpAdapter(adapter);
    }

    public void setOnRefreshListener(OnRecyclerRefreshListener listener) {
        this.listener = listener;
    }


    public void onRefreshing(CoolRefreshView refreshView) {
        mCurrentState = ACTION_PULL_TO_REFRESH;
        listener.onRefresh(ACTION_PULL_TO_REFRESH);
    }

    public void onRefreshCompleted() {
        switch (mCurrentState) {
            case ACTION_PULL_TO_REFRESH:
                mSwipeRefreshLayout.refreshComplete();
                break;
            case ACTION_LOAD_MORE_REFRESH:
                adapter.onLoadMoreStateChanged(false);
                if (isPullToRefreshEnabled) {
                    mSwipeRefreshLayout.setEnabled(true);
                }
                break;
        }
        mCurrentState = ACTION_IDLE;
    }


    public void setSelection(int position) {
        mRecyclerView.scrollToPosition(position);
    }

    public RecyclerView getRecyclerView(){
        return mRecyclerView;
    }

    public interface OnRecyclerRefreshListener {
        void onRefresh(int action);
    }
}
