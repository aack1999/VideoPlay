package com.framework.core.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.WindowManager;

import com.framework.core.common.AppSetting;

import java.lang.reflect.Field;

public class DisplayUtils {

	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * 
	 * @param pxValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * 
	 * @param dipValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	public static int getScreenW() {
		WindowManager wm = (WindowManager) AppSetting.getContext().getSystemService(
				Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();
	}
	
	public static int getScreenH() {
		WindowManager wm = (WindowManager) AppSetting.getContext().getSystemService(
				Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getHeight();
	}

	// 获取手机状态栏高度
		public static int getStatusBarHeight(Context context) {
			Class<?> c = null;
			Object obj = null;
			Field field = null;
			int x = 0, statusBarHeight = 0;
			try {
				c = Class.forName("com.android.internal.R$dimen");
				obj = c.newInstance();
				field = c.getField("status_bar_height");
				x = Integer.parseInt(field.get(obj).toString());
				statusBarHeight = context.getResources().getDimensionPixelSize(x);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return statusBarHeight;
		}

	/**
	 * 获取actionbar的像素高度，默认使用android官方兼容包做actionbar兼容
	 *
	 * @return
	 */
	public static float getActionBarHeight(Context context) {
		TypedArray actionbarSizeTypedArray = context.obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
		float actionBarHeight = actionbarSizeTypedArray.getDimension(0, 0);
		return actionBarHeight;
	}

}
