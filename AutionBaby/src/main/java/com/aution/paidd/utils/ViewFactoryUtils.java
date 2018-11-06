package com.aution.paidd.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;

import com.framework.core.utils.DisplayUtils;
import com.aution.paidd.R;

/**
 * Created by quxiang on 2017/1/16.
 */

public class ViewFactoryUtils {


	@SuppressLint("ResourceAsColor")
	public static View getLineView(Context context, int marginLeft) {
		View v = new View(context);
		v.setBackgroundColor(ContextCompat.getColor(context, R.color.base_line));
		LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(-1, DisplayUtils.dip2px(context, 0.5f));
		l.leftMargin = marginLeft;
		v.setLayoutParams(l);
		return v;
	}

}
