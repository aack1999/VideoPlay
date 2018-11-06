package com.aution.paidd.common;

import com.aution.paidd.bean.VersionBeen;
import com.aution.paidd.response.AdressUpReponse;
import com.aution.paidd.response.AdvsResponse;
import com.aution.paidd.response.BannerResponse;
import com.aution.paidd.response.BaseResponse;
import com.aution.paidd.response.CoinIncomeResponse;
import com.aution.paidd.response.DefaultAddressResponse;
import com.aution.paidd.response.GoodsListResponse;
import com.aution.paidd.response.HeadLineResponse;
import com.aution.paidd.response.LoginResponse;
import com.aution.paidd.response.LogisticsResponse;
import com.aution.paidd.response.LrecordResponse;
import com.aution.paidd.response.LuckyListResponse;
import com.aution.paidd.response.LuckyShowListResponse;
import com.aution.paidd.response.OtherPayResponse;
import com.aution.paidd.response.PayCenterResponse;
import com.aution.paidd.response.PayInitOrderResponse;
import com.aution.paidd.response.PayListResponse;
import com.aution.paidd.response.PaySystem;
import com.aution.paidd.response.ShopDetialResponse;
import com.aution.paidd.response.ShopHistoryResponse;
import com.aution.paidd.response.ShopRecordResponse;
import com.aution.paidd.response.ShopTypeResponse;
import com.aution.paidd.response.SignInitResponse;
import com.aution.paidd.response.UserMoneyResponse;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by quxiang on 2017/5/5.
 */

public interface Engine {

	@FormUrlEncoded
	@POST("service/user/login")
	Observable<LoginResponse> getLogin(@FieldMap Map<String, String> map);

	@FormUrlEncoded
	@POST("service/user/initlogin")
	Observable<LoginResponse> getInitLogin(@FieldMap Map<String, String> map);

	@FormUrlEncoded
	@POST("service/user/register")
	Observable<LoginResponse> getRegister(@FieldMap Map<String, String> map);

	@GET("service/user/sendsms")
	Observable<BaseResponse> getSendsms(@QueryMap Map<String, String> map);

	@GET("service/user/verificationcode")
	Observable<BaseResponse> getCheckCode(@QueryMap Map<String, String> map);

	@GET("service/user/updatePwd")
	Observable<BaseResponse> getUpdatePwd(@QueryMap Map<String, String> map);

	@GET("service/activity/homeindex")
	Observable<BannerResponse> getHomeIndex();

	@GET("service/goods/list")
	Observable<GoodsListResponse> getGoodsList(@QueryMap Map<String, String> map);

	@GET("service/goods/typelist")
	Observable<ShopTypeResponse> getTypeList();

	@GET("service/message/headline")
	Observable<HeadLineResponse> getHeadLine();

	@GET("service/winning/allgoodlist")
	Observable<ShopHistoryResponse> getDoneGoodList(@QueryMap Map<String, String> map);

	@GET("service/goods/details")
	Observable<ShopDetialResponse> getGoodsDetail(@QueryMap Map<String, String> map);

	@GET("service/winning/goodlist")
	Observable<ShopHistoryResponse> getGoodHistoryList(@QueryMap Map<String, String> map);

	@GET("service/record/list")
	Observable<LrecordResponse> getJoinRecordList(@QueryMap Map<String, String> map);

	@FormUrlEncoded
	@POST("service/tobuy/goods")
	Observable<BaseResponse> getGoodsBuy(@FieldMap Map<String, String> map);

	@GET("service/goods/mybuy")
	Observable<ShopRecordResponse> getGoodsMyBuy(@QueryMap Map<String, String> map);

	@GET("service/address/list")
	Observable<AdressUpReponse> getAdressList(@QueryMap Map<String, String> map);

	@FormUrlEncoded
	@POST("service/address/add")
	Observable<BaseResponse> getAddressAdd(@FieldMap Map<String, String> map);

	@FormUrlEncoded
	@POST("service/address/update")
	Observable<BaseResponse> getAddressUp(@FieldMap Map<String, String> map);

	@GET("service/address/remove")
	Observable<BaseResponse> getRemoveAdress(@QueryMap Map<String, String> map);


	@FormUrlEncoded
	@POST("service/user/authorization")
	Observable<LoginResponse> getAuthorization(@FieldMap Map<String, String> map);


	@GET("service/check/init")
	Observable<SignInitResponse> getSignInit(@QueryMap Map<String, String> map);


	@GET("service/check/sign")
	Observable<BaseResponse> getSign(@QueryMap Map<String, String> map);


	@GET("service/currencyusage/list")
	Observable<CoinIncomeResponse> getIncomeList(@QueryMap Map<String, String> map);

	@GET("service/experience/list")
	Observable<CoinIncomeResponse> getExperienceList(@QueryMap Map<String, String> map);

	@FormUrlEncoded
	@POST("service/experience/exchange")
	Observable<BaseResponse> getExchange(@FieldMap Map<String, String> map);

	@GET("service/user/upcache")
	Observable<LoginResponse> getUserInfo(@QueryMap Map<String, String> map);


	@GET("service/pay/initpay")
	Observable<PaySystem> getPayList(@QueryMap Map<String, String> map);


	@FormUrlEncoded
	@POST("service/pay/initrecharge")
	Observable<PayCenterResponse> getToRecharge(@FieldMap Map<String, String> map);

	@GET("service/pay/record")
	Observable<PayListResponse> getPayLogList(@QueryMap Map<String, String> map);

	@GET("service/record/myrecord")
	Observable<LuckyListResponse> getLuckyRecord(@QueryMap Map<String, String> map);

	@GET("service/record/nowinning")
	Observable<ShopRecordResponse> getRecordNowinning(@QueryMap Map<String, String> map);

	@FormUrlEncoded
	@POST("service/pay/initorder")
	Observable<PayInitOrderResponse> getInitOrder(@FieldMap Map<String, String> map);

	@GET("service/address/get")
	Observable<DefaultAddressResponse> getDefalutAddress(@QueryMap Map<String, String> map);

	@GET("service/address/sign")
	Observable<BaseResponse> getAddressSign(@QueryMap Map<String, String> map);

	@GET("service/share/list")
	Observable<LuckyShowListResponse> getShareList(@QueryMap Map<String, String> map);

	@FormUrlEncoded
	@POST("service/user/updateInfo")
	Observable<LoginResponse> getUpDataInfo(@FieldMap Map<String, String> map);

	@GET("service/address/logistics")
	Observable<LogisticsResponse> getLogistics(@QueryMap Map<String, String> map);

	@GET("service/record/lottery")
	Observable<LuckyListResponse> getLottery(@QueryMap Map<String, String> map);

	@GET("service/address/reminder")
	Observable<BaseResponse> getReminder(@QueryMap Map<String, String> map);

	@GET("service/goods/usemoney")
	Observable<UserMoneyResponse> getUserMoney(@QueryMap Map<String, String> map);

	@GET("service/pay/orderstatus")
	Observable<OtherPayResponse> queryPayState(@QueryMap Map<String, String> map);

	@GET("service/activity/startAdvertising")
	Observable<AdvsResponse> getSplashAdvs(@Query("system")String system);

	@GET("service/pay/orderisbuy")
	Observable<OtherPayResponse> getOrderIsBuy(@QueryMap Map<String, String> map);

	@GET("service/version/get")
	Observable<VersionBeen> getAppVersion(@QueryMap Map<String, String> map);

	@GET("service/collect/collect")
	Observable<BaseResponse> getCollectShop(@QueryMap Map<String, String> map);

	@GET("service/collect/list")
	Observable<GoodsListResponse> getCollectList(@QueryMap Map<String, String> map);


	@GET("service/invitation/share")
	Observable<BaseResponse> getShareOk(@QueryMap Map<String, String> map);



}

