package com.qxhd.yellow.ui.fragment;

import android.view.View;

import com.framework.core.base.BaseFragment;
import com.qxhd.yellow.R;

public class MeFragment extends BaseFragment {

    @Override
    public int getContentLayoutID() {
        return R.layout.fragment_me;
    }

    @Override
    public void initValue() {

    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }
}
