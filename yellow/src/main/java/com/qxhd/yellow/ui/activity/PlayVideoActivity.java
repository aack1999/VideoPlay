package com.qxhd.yellow.ui.activity;

import android.view.View;

import com.framework.core.base.BaseActivity;
import com.qxhd.yellow.R;

public class PlayVideoActivity extends BaseActivity {

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_play_video;
    }

    @Override
    public void initValue() {

    }

    public void onBack(View v){
        finish();
    }

    public void goToPlay(View v){
        doActivity(PlayVideoActivity.class);
    }

}
