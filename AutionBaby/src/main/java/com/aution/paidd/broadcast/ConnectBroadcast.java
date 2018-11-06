package com.aution.paidd.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aution.paidd.common.CacheData;
import com.aution.paidd.ui.activity.LuckyBoyOrGirlActivity;
import com.aution.paidd.ui.activity.RegisterSuccessActivity;

/**
 * Created by quxiang on 2017/9/5.
 */

public class ConnectBroadcast  extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getIntExtra("action",0)==1){
			intent.setClass(context, RegisterSuccessActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}

		if (intent!=null&&CacheData.getInstance().isLogin()&& CacheData.getInstance().getUserData().getUid().equals(intent.getStringExtra("uid"))){
			//中奖了
			intent.setClass(context, LuckyBoyOrGirlActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}


}
