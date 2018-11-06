package com.framework.core.widget.pull.layoutmanager;

import android.support.v7.widget.RecyclerView;

import com.framework.core.widget.pull.BaseListAdapter;


/**
 * Created by Stay on 5/3/16.
 * Powered by www.stay4it.com
 */
public interface ILayoutManager {
    RecyclerView.LayoutManager getLayoutManager();
    int findLastVisiblePosition();
    void setUpAdapter(BaseListAdapter adapter);
}
