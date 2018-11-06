package com.aution.paidd.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.framework.core.common.AppSetting;
import com.framework.core.tools.ACache;
import com.framework.core.utils.LogUtils;
import com.framework.core.utils.imageloader.ImageCatchUtil;
import com.aution.paidd.bean.AdvsBean;
import com.aution.paidd.bean.UserBean;
import com.aution.paidd.greendao.gen.DaoSession;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by quxiang on 2017/8/31.
 */

public class CacheData {

	private static class SingletonHolder {
		static final CacheData INSTANCE = new CacheData();
	}

	public static CacheData getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private Object readResolve() {
		return getInstance();
	}

	/**
	 * 是否登录
	 */
	public boolean isLogin(){
		UserBean model = getUserData();
		if (model!=null&&!TextUtils.isEmpty(model.getUid())&&model.getAt()!=4){
			return true;
		}
		return false;
	}


	public String getAppid(){
		return APPID;
	}

	public String getImei() {
		String imei = null;
		UserBean model = getUserData();
		if (model != null && !TextUtils.isEmpty(model.getImei())) {
			return model.getImei();
		}
		try {
			TelephonyManager mTm  = (TelephonyManager) AppSetting.getContext().getSystemService(Context.TELEPHONY_SERVICE);
			imei = mTm.getDeviceId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (TextUtils.isEmpty(imei)) {
			imei = UUID.randomUUID().toString().replace("-", "");
		}
		return imei;
	}

	public UserBean getUserData() {
		List<UserBean> list= getDaoSession().getUserBeanDao().loadAll();
		if (list!=null&&list.size()>0){
			return list.get(0);
		}
		UserBean model=new UserBean();
		model.setAt(4);
		return model;
	}

	/**
	 * 缓存用户信息
	 *
	 * @param model
	 */
	public  void cacheUserData(UserBean model) {
		clearUserData();
		if (model!=null)
			getDaoSession().getUserBeanDao().insert(model);

	}

	public  void clearUserData() {
		getDaoSession().getUserBeanDao().deleteAll();
	}

	public String getPayKey(){
		return PAYKEY;
	}

	public void cacheLogoFile(String path){
		ACache.get(AppSetting.getContext()).put("logoFile",path);
	}


	public void cacheAdvsFile(String imgBaseUrl,final AdvsBean bean) {
		List<AdvsBean> list= getDaoSession().getAdvsBeanDao().loadAll();
		if (list!=null&&list.size()>0){
			if (list.get(0).getImg().equals(bean.getImg())){
				LogUtils.e("图片已经下载过了");
				return;
			}
		}
		Glide.with(AppSetting.getContext()).load(imgBaseUrl+bean.getImg()).asBitmap().into(new SimpleTarget<Bitmap>() {
			@Override
			public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
				saveBitmapFile(resource, bean);
			}
		});
	}

	private void saveBitmapFile(Bitmap bitmap, AdvsBean bean) {
		File temp = new File(AppSetting.getContext().getCacheDir() + "/advs/");
		ImageCatchUtil.getInstance().deleteFolderFile(temp.getPath(),true);
		if (!temp.exists()) {
			temp.mkdirs();
		}
		String fileName=bean.getImg().substring(bean.getImg().lastIndexOf("/")+1,bean.getImg().length());
		File file = new File(AppSetting.getContext().getCacheDir() + "/advs/"+fileName);//将要保存图片的路径
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			LogUtils.e("广告保存成功");
			bean.setAdvsFile(fileName);
			getDaoSession().getAdvsBeanDao().deleteAll();
			getDaoSession().getAdvsBeanDao().insert(bean);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadAdvsImg(ImageView img, View.OnClickListener onClickListener) {
		List<AdvsBean> list= getDaoSession().getAdvsBeanDao().loadAll();
		AdvsBean model=null;
		if (list!=null&&list.size()>0){
			model=list.get(0);
		}
		if (model!=null) {
			File file = new File(AppSetting.getContext().getCacheDir() + "/advs/"+model.getAdvsFile());
			if (file!=null&&file.exists()){
				Glide.with(img.getContext()).load(file).diskCacheStrategy(DiskCacheStrategy.NONE).into(img);
			}else {
				if (!TextUtils.isEmpty(model.getAdvsFile())){
					//重新下载图片
					getDaoSession().getAdvsBeanDao().deleteAll();
					LogUtils.e("图片被系统删除了,重新下载!!!");
				}
				onClickListener.onClick(null);
			}
			if (!TextUtils.isEmpty(model.getUrl())){
				img.setTag(model.getUrl());
				img.setOnClickListener(onClickListener);
			}
		} else {
			onClickListener.onClick(null);
		}
	}


	private DaoSession mDaoSession;

	public DaoSession getDaoSession() {
		return mDaoSession;
	}

	public void setDaoSession(DaoSession mDaoSession) {
		this.mDaoSession = mDaoSession;
	}

	private String APPID="";
	private String UMENGKEY="";
	private String PAYKEY="";


	public void setAPPID(String APPID) {
		this.APPID = APPID;
	}

	public String getUMENGKEY() {
		return UMENGKEY;
	}

	public void setUMENGKEY(String UMENGKEY) {
		this.UMENGKEY = UMENGKEY;
	}

	public void setPAYKEY(String PAYKEY) {
		this.PAYKEY = PAYKEY;
	}
}

