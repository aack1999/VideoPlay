package com.aution.paidd;

import android.app.ActivityManager;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.aution.paidd.service.InitializeService;

import java.util.List;

/**
 * Created by quxiang on 2017/8/31.
 */

public class UIApplication extends MultiDexApplication {


	@Override
	public void onCreate() {
		super.onCreate();
		String processName = getProcessName(this, android.os.Process.myPid());
		if (processName != null) {
			boolean defaultProcess = processName.equals(getPackageName());
			if (defaultProcess) {
				InitializeService.start(this);
			}
		}
	}

	/**
	 * @return null may be returned if the specified process not found
	 */
	public String getProcessName(Context cxt, int pid) {
		ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
		if (runningApps == null) {
			return null;
		}
		for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
			if (procInfo.pid == pid) {
				return procInfo.processName;
			}
		}
		return null;
	}

}
