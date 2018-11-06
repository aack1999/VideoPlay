package com.aution.paidd.common;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import com.aution.paidd.bean.VersionBeen;
import com.aution.paidd.request.UpAppRequest;
import com.aution.paidd.service.DownAPKService;
import com.aution.paidd.utils.DialogUtil;

import rx.Subscriber;

/**
 * Created by quxiang on 2016/12/23.
 */
public class CommonTools {


	public static String getVersion(Activity activity) {
		try {
			String pkName = activity.getPackageName();
			String versionName = activity.getPackageManager().getPackageInfo(
					pkName, 0).versionName;
			return versionName;
		} catch (Exception e) {
		}
		return null;
	}

	//TODO  更新操作
	public static void upApp(final Activity activity) {

		UpAppRequest request = new UpAppRequest();
		Subscriber<VersionBeen> subscriber=new Subscriber<VersionBeen>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(VersionBeen prolistBean) {
				String appVersion = getVersion(activity);
				if (prolistBean != null && appVersion != null) {
					if (prolistBean.getCode()==10000) {
						if (appVersion.equals(prolistBean.getObj().getVnumber())) {

						} else {
							showDownAPKDialog(activity, prolistBean);
						}
					}
				}
			}
		};
		HttpMethod.getInstance().getAppVersion(subscriber,request);
	}


	public static void showDownAPKDialog(final Activity activity, final VersionBeen model) {

		DialogUtil.showMessage(activity, "更新提示", model.getObj().getRemark(), "更新", new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent updateIntent = new Intent(activity, DownAPKService.class);
				updateIntent.putExtra(DownAPKService.URLFLAG, model.getMsg() + model.getObj().getUrl());
				activity.startService(updateIntent);
			}
		}, new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialogInterface) {

			}
		});
	}

}
