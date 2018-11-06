package com.aution.paidd.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ListView;

import com.aution.paidd.R;


/**
 * Created by quxiang on 2017/1/6.
 */

public class InnerMoreListView extends ListView {

    boolean isEnbleLoadMore=false;

    boolean isLoading=false;

    PtrLoadListener ptrLoadListener;

    View footView;

    OnScrollListener onScrollListener;

    public InnerMoreListView(Context context) {
        super(context);
        init();
    }

    public InnerMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InnerMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (onScrollListener!=null){
                    onScrollListener.onScroll(absListView,i,i1,i2);
                }
                if (getAdapter()!=null&&isEnbleLoadMore&&!isLoading&&isLastItemVisible()){
                    Log.e("可以加载更多了哦","====================");
                    addFootView();
                }
            }
        });
    }


    public void addOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public void addFootView(){
        isLoading=true;
        if (ptrLoadListener!=null){
            ptrLoadListener.onLoadMore();
        }
        if (footView==null){
            footView= LayoutInflater.from(getContext()).inflate(R.layout.footbar_view,null);
        }
        if (isEnbleLoadMore)
        addFooterView(footView);
    }

    /**
     * 加载完成
     */
    public void onCompleRefresh(){
        isLoading=false;
        removeFooterView(footView);
    }

    public void setPtrLoadListener(PtrLoadListener ptrLoadListener) {
        this.ptrLoadListener = ptrLoadListener;
    }

    private boolean isLastItemVisible() {
        final Adapter adapter = getAdapter();
        if (null == adapter || adapter.isEmpty()) {
            return true;
        } else {
            final int lastItemPosition = getCount() - 1;
            final int lastVisiblePosition = getLastVisiblePosition();

            /**
             * This check should really just be: lastVisiblePosition == lastItemPosition, but PtRListView internally uses a
             * FooterView which messes the positions up. For me we'll just subtract one to account for it and rely on the inner
             * condition which checks getBottom().
             */
            if (lastVisiblePosition >= lastItemPosition - 1) {
                final int childIndex = lastVisiblePosition - getFirstVisiblePosition();
                final View lastVisibleChild = getChildAt(childIndex);
                if (lastVisibleChild != null) {
                    return lastVisibleChild.getBottom() <= getBottom();
                }
            }
        }
        return false;
    }

    public boolean isEnbleLoadMore() {
        return isEnbleLoadMore;
    }

    public void setEnbleLoadMore(boolean enbleLoadMore) {
        isEnbleLoadMore = enbleLoadMore;
    }

    public interface PtrLoadListener{

     public   void onLoadMore();

    }
}
