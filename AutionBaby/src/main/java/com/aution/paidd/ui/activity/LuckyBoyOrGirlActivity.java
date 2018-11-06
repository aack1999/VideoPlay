package com.aution.paidd.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.nineoldandroids.animation.ObjectAnimator;
import com.aution.paidd.R;

import me.xiaopan.sketch.SketchImageView;

/**
 * Created by quxiang on 2017/9/12.
 */

public class LuckyBoyOrGirlActivity extends Activity {


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_lucky_winner);
		init();
	}


	public void init(){
		SketchImageView img_bg=(SketchImageView)findViewById(R.id.img_bg1);
		SketchImageView temp=(SketchImageView)findViewById(R.id.temp);
		SketchImageView tempp=(SketchImageView)findViewById(R.id.tempp);
		SketchImageView btn_xx=(SketchImageView)findViewById(R.id.btn_xx);
		img_bg.displayResourceImage(R.drawable.ic_gxpzjb);
		temp.displayResourceImage(R.drawable.ic_gxpzbj);
		tempp.displayResourceImage(R.drawable.ic_msfk);
		btn_xx.displayResourceImage(R.drawable.ic_gb);

		btn_xx.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});


		tempp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent=new Intent(LuckyBoyOrGirlActivity.this,RecordListActivity.class);
				intent.putExtra("index",1);
				startActivity(intent);
				finish();
			}
		});



		ObjectAnimator oaAnimator=ObjectAnimator.ofFloat(img_bg, "rotation", 0,360);
		oaAnimator.setRepeatCount(-1);
		oaAnimator.setRepeatMode(ObjectAnimator.INFINITE);
		oaAnimator.setDuration(3000);
		oaAnimator.setInterpolator(new LinearInterpolator());
		oaAnimator.start();

	}


}
