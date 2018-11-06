package com.aution.paidd.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.framework.core.utils.DisplayUtils;
import com.aution.paidd.R;

import java.util.concurrent.TimeUnit;

import me.xiaopan.sketch.SketchImageView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by quxiang on 2017/9/15.
 */

public class RegisterSuccessActivity extends AppCompatActivity {



	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showDialog(this);
	}


	public void showDialog(Context context){
		final Dialog dialog=new Dialog(context, R.style.mydialog);
		dialog.setContentView(R.layout.layout_first_sign);
		SketchImageView btn_xx=(SketchImageView)dialog.findViewById(R.id.btn_xx);
		SketchImageView img=(SketchImageView)dialog.findViewById(R.id.img);
		btn_xx.displayResourceImage(R.drawable.ic_gb);
		img.displayResourceImage(R.drawable.ic_first);

		dialog.getWindow().getAttributes().width= DisplayUtils.getScreenW();

		dialog.show();

		btn_xx.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
				finish();
			}
		});

		Observable.timer(5, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
			@Override
			public void call(Long aLong) {
				if (dialog!=null&&dialog.isShowing()){
					dialog.dismiss();
					finish();
				}
			}
		});
	}
}
