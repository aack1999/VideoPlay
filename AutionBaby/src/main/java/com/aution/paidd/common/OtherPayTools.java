package com.aution.paidd.common;

import android.content.Context;
import android.widget.Toast;

import com.framework.core.common.WaitDialog;
import com.framework.core.utils.LogUtils;
import com.aution.paidd.request.OrderRequest;
import com.aution.paidd.response.OtherPayResponse;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


/**
 * Created by quxiang on 2017/2/23.
 */

public class OtherPayTools {

	public static void queryPayState(final Context context,final String orderno, final OnPayStateListener listener) {
		final WaitDialog dialog = new WaitDialog(context);
		dialog.show();
		dialog.setContent("确认支付结果...");
		Observable.timer(3, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
			@Override
			public void call(Long aLong) {


				Subscriber<OtherPayResponse> subscriber = new Subscriber<OtherPayResponse>() {
					@Override
					public void onCompleted() {
						dialog.dismiss();
					}

					@Override
					public void onError(Throwable e) {

					}

					@Override
					public void onNext(OtherPayResponse otherPayResponse) {
						if (otherPayResponse != null) {
							if ("1".equals(otherPayResponse.getObj())) {
								LogUtils.e("支付成功");
								listener.onPaySuccess();
							} else {
								listener.onPayFail();
								Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
							}
						}
					}
				};
				OrderRequest request = new OrderRequest();
				request.setTradeNO(orderno);
				HttpMethod.getInstance().queryPayState(subscriber, request);
			}
		});
	}

}
