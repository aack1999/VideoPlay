package com.aution.paidd.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ninegrid.NineGridView;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by quxiang on 2017/9/20.
 */

public class InitializeService extends IntentService {

	private static final String ACTION_INIT_WHEN_APP_CREATE = "com.qxhd.aution.service.action.INIT";

	public InitializeService() {
		super("InitializeService");
	}

	@Override
	protected void onHandleIntent(@Nullable Intent intent) {
		if (intent != null) {
			final String action = intent.getAction();
			if (ACTION_INIT_WHEN_APP_CREATE.equals(action)) {
				performInit();
			}
		}
	}

	private void performInit(){

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
			StrictMode.setVmPolicy(builder.build());
		}

		//Share分享初始化
		ShareSDK.initSDK(getApplicationContext());

		NineGridView.setImageLoader(new GlideImageLoader());

		//极光推送初始化
//		JPushInterface.setDebugMode(false); 	// 设置开启日志,发布时请关闭日志
//		JPushInterface.init(this);
	}

	/** Picasso 加载 */
	private class GlideImageLoader implements NineGridView.ImageLoader {

		@Override
		public void onDisplayImage(Context context, ImageView imageView, String url) {
			Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
		}

		@Override
		public Bitmap getCacheImage(String url) {
			return null;
		}
	}

	public static void start(Context context) {
		Intent intent = new Intent(context, InitializeService.class);
		intent.setAction(ACTION_INIT_WHEN_APP_CREATE);
		context.startService(intent);
	}

}
