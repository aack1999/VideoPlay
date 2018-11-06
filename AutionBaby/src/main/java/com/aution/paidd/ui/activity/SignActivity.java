package com.aution.paidd.ui.activity;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.framework.core.base.BaseActivity;
import com.framework.core.utils.TextSpanUtils;
import com.framework.core.widget.FlatButton;
import com.aution.paidd.R;
import com.aution.paidd.bean.UserBean;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.Constant;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.request.BaseIdRequest;
import com.aution.paidd.response.BaseResponse;
import com.aution.paidd.response.SignInitResponse;
import com.aution.paidd.ui.widget.SignZView;

import butterknife.BindView;
import me.xiaopan.sketch.SketchImageView;
import me.xiaopan.sketch.request.DisplayOptions;
import me.xiaopan.sketch.shaper.CircleImageShaper;
import rx.Subscriber;

/**
 * Created by quxiang on 2017/9/4.
 */

public class SignActivity extends BaseActivity implements View.OnClickListener{

	@BindView(R.id.btn_sign)
	FlatButton btn_sign;
	@BindView(R.id.signzview)
	SignZView signzview;
	@BindView(R.id.img_logo)
	SketchImageView img_logo;
	@BindView(R.id.img1)
	SketchImageView img1;
	@BindView(R.id.img2)
	SketchImageView img2;
	@BindView(R.id.img3)
	SketchImageView img3;
	@BindView(R.id.img4)
	SketchImageView img4;
	@BindView(R.id.img5)
	SketchImageView img5;
	@BindView(R.id.img6)
	SketchImageView img6;
	@BindView(R.id.img7)
	SketchImageView img7;

	@BindView(R.id.tv_1)
	TextView tv_1;
	@BindView(R.id.tv_2)
	TextView tv_2;
	@BindView(R.id.tv_3)
	TextView tv_3;
	@BindView(R.id.tv_4)
	TextView tv_4;
	@BindView(R.id.tv_5)
	TextView tv_5;
	@BindView(R.id.tv_6)
	TextView tv_6;
	@BindView(R.id.tv_7)
	TextView tv_7;

	TextView tv[]=new TextView[7];

	@BindView(R.id.tv_nameid)
	TextView tv_nameid;
	@BindView(R.id.tv_score)
	TextView tv_score;
	@BindView(R.id.tv_tips)
	TextView tv_tips;
	@BindView(R.id.tv_tip_content)
	TextView tv_tip_content;

	SketchImageView img[]=new SketchImageView[7];

	SignInitResponse signInitResponse;


	@Override
	public int getContentLayoutID() {
		return R.layout.activity_sign;
	}

	@Override
	public void initValue() {

		setTitle("签到");


		SpannableString ss=new SpannableString(getString(R.string.sign_help));
		ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.base_gray)),0,37, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.base_gray)),ss.length()-43,ss.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		tv_tip_content.setText(ss);

		btn_sign.setOnClickListener(this);

		img[0]=img1;
		img[1]=img2;
		img[2]=img3;
		img[3]=img4;
		img[4]=img5;
		img[5]=img6;
		img[6]=img7;

		tv[0]=tv_1;
		tv[1]=tv_2;
		tv[2]=tv_3;
		tv[3]=tv_4;
		tv[4]=tv_5;
		tv[5]=tv_6;
		tv[6]=tv_7;




		for (int i = 0; i < img.length; i++) {
			img[i].displayResourceImage(R.drawable.ic_sing_jbx);
		}

		UserBean model=CacheData.getInstance().getUserData();
		tv_nameid.setText(model.getNickname());
		DisplayOptions options = new DisplayOptions();
		options.setImageShaper(new CircleImageShaper());
		img_logo.setOptions(options);
		img_logo.displayImage(model.getImg());

		String str="共有"+model.getExperience()+"积分";
		tv_score.setText(TextSpanUtils.createColorText(str,2,str.length()-2,getResources().getColor(R.color.theme_color)));
		getData();

		btn_sign.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				submit();
			}
		});
	}

	public void getData(){
		Subscriber<SignInitResponse> subscriber=new Subscriber<SignInitResponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(SignInitResponse response) {
				signInitResponse=response;
				if (signInitResponse!=null){

					if (signInitResponse.getCode()==10000){
						if (signInitResponse.getObj()!=null){

							for (int i = 0; i < img.length; i++) {
								if (i==img.length-1){
									img[i].displayResourceImage(i<signInitResponse.getObj().getCount()?R.drawable.ic_sign_jbd1:R.drawable.ic_sign_jbd);
								}else {
									img[i].displayResourceImage(i<signInitResponse.getObj().getCount()?R.drawable.ic_sign_jbx1:R.drawable.ic_sing_jbx);
								}
								tv[i].setSelected(i<signInitResponse.getObj().getCount());
							}

							tv_tips.setVisibility(View.VISIBLE);
							tv_tips.setText("累计签到"+signInitResponse.getObj().getTotal()+"天,连续签到可以获得赠币哦!");

							if (signInitResponse.getObj().getWhether()==1){
								//没签到

							}else {
								//签到了
								btn_sign.setText("已签到");
								btn_sign.setEnabled(false);
							}
						}
					}else {
						showToast(signInitResponse.getMsg());
					}
				}else {
					showToast(Constant.Service_Err);
				}
			}
		};
		BaseIdRequest request=new BaseIdRequest();
		request.setUid(CacheData.getInstance().getUserData().getUid());
		HttpMethod.getInstance().getSignInit(subscriber,request);
	}


	public void submit(){
		showDialog(null);
		Subscriber<BaseResponse> subscriber=new Subscriber<BaseResponse>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				closeDialog();
				showToast(Constant.Service_Err);
			}

			@Override
			public void onNext(BaseResponse baseResponse) {
				closeDialog();
				if (baseResponse!=null){
					showToast(baseResponse.getMsg());
					if (baseResponse.getCode()==10000){
						btn_sign.setEnabled(false);
						btn_sign.setText("已签到");

						img[signInitResponse.getObj().getCount()].displayResourceImage(R.drawable.ic_sign_jbx1);
						tv[signInitResponse.getObj().getCount()].setSelected(true);
						tv_tips.setText("累计签到"+(signInitResponse.getObj().getTotal()+1)+"天,连续签到可以获得赠币哦!");

						UserBean userBean=CacheData.getInstance().getUserData();

						userBean.setExperience(userBean.getExperience()+(signInitResponse.getObj().getCount()+1)*10);

						CacheData.getInstance().cacheUserData(userBean);

						String str="共有"+userBean.getExperience()+"积分";
						tv_score.setText(TextSpanUtils.createColorText(str,2,str.length()-2,getResources().getColor(R.color.theme_color)));
					}
				}
			}
		};
		BaseIdRequest request=new BaseIdRequest();
		request.setUid(CacheData.getInstance().getUserData().getUid());
		HttpMethod.getInstance().getSign(subscriber,request);
	}

	@Override
	public void onClick(View view) {
		signzview.moveTo(1);
	}
}
