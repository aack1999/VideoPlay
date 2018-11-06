package com.aution.paidd.ui.activity;

import com.framework.core.base.BaseActivity;
import com.aution.paidd.R;


/**
 * Created by quxiang on 2017/1/18.
 */

public class LuckyShowActivity extends BaseActivity {

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_lucky_show;
    }

    @Override
    public void initValue() {
        setTitle("晒单");
    }
}
