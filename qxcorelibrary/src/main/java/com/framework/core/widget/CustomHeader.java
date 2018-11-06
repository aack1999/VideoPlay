package com.framework.core.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.framework.core.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by quxiang on 2017/9/4.
 */

public class CustomHeader extends FrameLayout implements PtrUIHandler{

	TextView tv_title;
	ImageView img;

	public CustomHeader(Context context) {
		this(context,null,0);
	}

	public CustomHeader(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs,0);
	}

	public CustomHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		LayoutInflater.from(context).inflate(R.layout.header_custom_layout,this);
		tv_title=(TextView) findViewById(R.id.tv_title);
		img=(ImageView)findViewById(R.id.img);
	}

	@Override
	public void onUIReset(PtrFrameLayout frame) {

	}

	@Override
	public void onUIRefreshPrepare(PtrFrameLayout frame) {
		tv_title.setText("下拉刷新");
		img.setImageResource(R.drawable.animation_pull);
		AnimationDrawable ad=(AnimationDrawable)img.getDrawable();
		ad.start();
	}

	@Override
	public void onUIRefreshBegin(PtrFrameLayout frame) {
		tv_title.setText("正在刷新...");
		img.setImageResource(R.drawable.animation_pull);
		AnimationDrawable ad=(AnimationDrawable)img.getDrawable();
		ad.start();
	}

	@Override
	public void onUIRefreshComplete(PtrFrameLayout frame) {
		tv_title.setText("刷新完成");
	}

	@Override
	public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
		float percent = Math.min(1f, ptrIndicator.getCurrentPercent());
		if (status == PtrFrameLayout.PTR_STATUS_PREPARE){

//			if (percent<=0.25f){
//				img.setImageResource(R.drawable.a1);
//			}else if (percent<=0.45f){
//				img.setImageResource(R.drawable.a2);
//			}else if (percent<=0.6f){
//				img.setImageResource(R.drawable.a3);
//			}else if (percent<=0.8f){
//				img.setImageResource(R.drawable.a4);
//			}else if (percent<=0.9f){
//				img.setImageResource(R.drawable.a5);
//			}else {
//				img.setImageResource(R.drawable.a6);
//			}
		}
	}
}
