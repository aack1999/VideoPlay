package com.aution.paidd.ui.activity;

import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.TextView;

import com.framework.core.base.ActivityManager;
import com.framework.core.base.BaseActivity;
import com.framework.core.widget.RowCell;
import com.framework.core.widget.RowCellGroupView;
import com.aution.paidd.R;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.CommonTools;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.model.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.xiaopan.sketch.Sketch;
import me.xiaopan.sketch.cache.DiskCache;

/**
 * Created by quxiang on 2017/9/1.
 */

public class SettingActivity extends BaseActivity {

	@BindView(R.id.rowcellgroupview)
	RowCellGroupView rowcellgroupview;

	@Override
	public int getContentLayoutID() {
		return R.layout.activity_setting;
	}

	@Override
	public void initValue() {
		setTitle("设置");

		DiskCache diskCache = Sketch.with(getBaseContext()).getConfiguration().getDiskCache();
		String usedSizeFormat = Formatter.formatFileSize(getBaseContext(), diskCache.getSize());

		List<RowCell> arr = new ArrayList<>();
		arr.add(null);
		arr.add(new RowCell().setLabel("新手指南").setViewid(0));
		arr.add(new RowCell().setLabel("常见问题").setViewid(1));
		arr.add(new RowCell().setLabel("服务协议").setViewid(2));
		arr.add(new RowCell().setLabel("关于我们").setViewid(3));
		arr.add(new RowCell().setLabel("当前版本").setDesc("v" + CommonTools.getVersion(this)).setAction(false));
		arr.add(null);
		arr.add(new RowCell().setViewid(4).setLabel("清除缓存").setDesc(usedSizeFormat));
		arr.add(null);
		rowcellgroupview.notifyDataChanged(arr);

		rowcellgroupview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				switch (view.getId()) {
					case 0: {
						Bundle bundle = new Bundle();
						bundle.putString("url", HttpMethod.BASE_URL + "newhelp");
						doActivity(H5Activity.class, bundle);
					}
					break;
					case 1: {
						Bundle bundle = new Bundle();
						bundle.putString("url", HttpMethod.BASE_URL + "question");
						doActivity(H5Activity.class, bundle);
					}
					break;
					case 2: {
						Bundle bundle = new Bundle();
						bundle.putString("url", HttpMethod.BASE_URL + "useragreement");
						doActivity(H5Activity.class, bundle);
					}

					break;
					case 3: {
						Bundle bundle = new Bundle();
						bundle.putString("url", HttpMethod.BASE_URL + "aboutme");
						doActivity(H5Activity.class, bundle);
					}
					break;
					case 4:
						Sketch.with(getBaseContext()).getConfiguration().getDiskCache().clear();
						TextView desc = (TextView) view.findViewById(R.id.row_desc);
						desc.setText("0.0M");
						break;
				}
			}
		});

		findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				CacheData.getInstance().cacheLogoFile("");
				CacheData.getInstance().clearUserData();
				ActivityManager.getInstance().finishAllActivityEx(MainActivity.class);
				EventBus.getDefault().post(new MessageEvent(102));
				//选中首页
				goToLogin();
			}
		});
	}

	public void goToLogin() {
		doActivityForResult(LoginActivity.class, null, 100);
		overridePendingTransition(R.anim.login_in_anim, R.anim.activity_nor_anim);
	}
}
