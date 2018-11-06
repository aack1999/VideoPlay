package com.aution.paidd.common;

import android.text.TextUtils;

import com.framework.core.utils.LogUtils;
import com.framework.core.utils.ParseUtils;
import com.aution.paidd.request.AuthRequest;
import com.aution.paidd.request.BaseIdRequest;
import com.aution.paidd.request.BaseListRequest;
import com.aution.paidd.request.CoinIncomeRequest;
import com.aution.paidd.request.ConfirmSignRequest;
import com.aution.paidd.request.ExcahngeRequest;
import com.aution.paidd.request.GoodsBuyRequest;
import com.aution.paidd.request.InitLoginRequest;
import com.aution.paidd.request.LoginRequest;
import com.aution.paidd.request.LuckyRequest;
import com.aution.paidd.request.LuckyShowRequest;
import com.aution.paidd.request.OrderRequest;
import com.aution.paidd.request.PayListRequest;
import com.aution.paidd.request.RechargeRequest;
import com.aution.paidd.request.RegisterRequest;
import com.aution.paidd.request.SendSmsRequest;
import com.aution.paidd.request.ShopDetailRequest;
import com.aution.paidd.request.ShopHistoryRequest;
import com.aution.paidd.request.UpAdressRequest;
import com.aution.paidd.request.UpAppRequest;
import com.aution.paidd.request.UpUserInfoRequest;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by quxiang on 2017/5/5.
 */

public class HttpMethod {

    private Retrofit retrofit;
    private Engine movieService;
    private static final int DEFAULT_TIMEOUT = 30;
    public static final String BASE_URL="http://app.paiduoduo.net.cn/";
//    public static final String BASE_URL="http://192.168.1.77:88/app/";
//    public static final String BASE_URL="http://192.168.1.77:70/app/";//测试服
//    public static final String BASE_URL="http://192.168.1.70:80/app/";
//    public static final String BASE_URL="http://192.168.1.7:80/app/";//王淦

    public static final String Socket_URL="ws://f.paiduoduo.net.cn:9999?uid=";
//    public static final String Socket_URL="ws://192.168.1.77:9999?uid=";



    //构造方法私有
    private HttpMethod() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        //如果不是在正式包，添加拦截 打印响应json
        if (LogUtils.isDebug) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(
                    new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
                            if (TextUtils.isEmpty(message)) return;
                            String s = message.substring(0, 1);
                            //如果收到想响应是json才打印
                            if ("{".equals(s) || "[".equals(s)) {
                                LogUtils.e("收到响应: " + message);
                            }
                        }
                    });
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClientBuilder.addInterceptor(logging);
    }

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        movieService = retrofit.create(Engine.class);
    }

    //在访问HttpMethods时创建单例


    private static class SingletonHolder {
        private static final HttpMethod INSTANCE = new HttpMethod();
    }

    //获取单例
    public static HttpMethod getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * 登录
     * @param subscriber
     */
    public void getLogin(Subscriber subscriber, LoginRequest request){
        movieService.getLogin(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 初始化登录
     * @param subscriber
     */
    public void getInitLogin(Subscriber subscriber, InitLoginRequest request){
        movieService.getInitLogin(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 注册
     * @param subscriber
     */
    public void getRegister(Subscriber subscriber, RegisterRequest request){
        movieService.getRegister(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 发送验证码
     * @param subscriber
     */
    public void getSendsms(Subscriber subscriber, SendSmsRequest request){
        movieService.getSendsms(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 效验验证码
     * @param subscriber
     */
    public void getCheckCode(Subscriber subscriber, RegisterRequest request){
        movieService.getCheckCode(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 修改密码和忘记密码
     * @param subscriber
     */
    public void getUpdatePwd(Subscriber subscriber, RegisterRequest request){
        movieService.getUpdatePwd(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 获取轮播图
     * @param subscriber
     */
    public void getBanner(Subscriber subscriber){
        movieService.getHomeIndex().subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 获取正在热拍商品
     * @param subscriber
     */
    public void getGoodsList(Subscriber subscriber, BaseIdRequest request){
        movieService.getGoodsList(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 获取商品分类
     * @param subscriber
     */
    public void getTypeList(Subscriber subscriber){
        movieService.getTypeList().subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 获取 头条
     * @param subscriber
     */
    public void getHeadLine(Subscriber subscriber){
        movieService.getHeadLine().subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 获取 商品详情
     * @param subscriber
     */
    public void getGoodsDetail(Subscriber subscriber, ShopDetailRequest request){
        movieService.getGoodsDetail(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 获取 往期揭晓
     * @param subscriber
     */
    public void getGoodHistoryList(Subscriber subscriber, ShopHistoryRequest request){
        movieService.getGoodHistoryList(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 获取 最新成交
     * @param subscriber
     */
    public void getDoneGoodList(Subscriber subscriber, BaseListRequest request){
        movieService.getDoneGoodList(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 获取 出价记录
     * @param subscriber
     */
    public void getJoinRecordList(Subscriber subscriber, BaseIdRequest request){
        movieService.getJoinRecordList(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 获取 购买
     * @param subscriber
     */
    public void getGoodsBuy(Subscriber subscriber, GoodsBuyRequest request){
        movieService.getGoodsBuy(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
    /**
     * 获取 正在拍
     * @param subscriber
     */
    public void getGoodsMyBuy(Subscriber subscriber, BaseIdRequest request){
        movieService.getGoodsMyBuy(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 获取 地址列表
     * @param subscriber
     */
    public void getAdressList(Subscriber subscriber, BaseIdRequest request){
        movieService.getAdressList(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 获取 添加地址
     * @param subscriber
     */
    public void getAddressAdd(Subscriber subscriber, UpAdressRequest request){
        movieService.getAddressAdd(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 获取 修改地址
     * @param subscriber
     */
    public void getAddressUp(Subscriber subscriber, UpAdressRequest request){
        movieService.getAddressUp(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 获取 删除地址
     * @param subscriber
     */
    public void getRemoveAdress(Subscriber subscriber, BaseIdRequest request){
        movieService.getRemoveAdress(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 第三方登录
     * @param subscriber
     */
    public void getAuthorization(Subscriber subscriber, AuthRequest request){
        movieService.getAuthorization(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 签到数据
     * @param subscriber
     */
    public void getSignInit(Subscriber subscriber ,BaseIdRequest request){
        movieService.getSignInit(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 签到
     * @param subscriber
     */
    public void getSign(Subscriber subscriber ,BaseIdRequest request){
        movieService.getSign(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

	/**
	 * 币明细
	 * @param subscriber
	 */
	public void getIncomeList(Subscriber subscriber ,CoinIncomeRequest request){
		movieService.getIncomeList(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
	}

    /**
     * 积分明细
     * @param subscriber
     */
    public void getExperienceList(Subscriber subscriber ,CoinIncomeRequest request){
        movieService.getExperienceList(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 积分兑换
     * @param subscriber
     */
    public void getExchange(Subscriber subscriber ,ExcahngeRequest request){
        movieService.getExchange(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
    /**
     * 获取用户信息
     * @param subscriber
     */
    public void getUserInfo(Subscriber subscriber ,BaseIdRequest request){
        movieService.getUserInfo(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 获取支付列表
     * @param subscriber
     */
    public void getPayList(Subscriber subscriber ,PayListRequest request){
        movieService.getPayList(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 获取充值
     * @param subscriber
     */
    public void getToRecharge(Subscriber subscriber ,RechargeRequest request){
        movieService.getToRecharge(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     *支付记录
     * @param subscriber
     */
    public void getPayLogList(Subscriber subscriber, BaseIdRequest request){
        movieService.getPayLogList(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }


    /**
     * 我拍中记录
     * @param subscriber
     */
    public void getLuckyRecord(Subscriber subscriber, LuckyRequest request){
        movieService.getLuckyRecord(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 差价购列表
     * @param subscriber
     */
    public void getRecordNowinning(Subscriber subscriber, BaseIdRequest request){
        movieService.getRecordNowinning(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 支付订单
     * @param subscriber
     */
    public void getInitOrder(Subscriber subscriber, GoodsBuyRequest request){
        movieService.getInitOrder(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 获取 默认地址
     * @param subscriber
     */
    public void getDefalutAddress(Subscriber subscriber, BaseIdRequest request){
        movieService.getDefalutAddress(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 立即签收
     * @param subscriber
     */
    public void getAddressSign(Subscriber subscriber, ConfirmSignRequest request){
        movieService.getAddressSign(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }


    /**
     * 晒单
     * @param subscriber
     */
    public void getShareList(Subscriber subscriber, LuckyShowRequest request){
        movieService.getShareList(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 修改用户信息
     * @param subscriber
     */
    public void getUpDataInfo(Subscriber subscriber, UpUserInfoRequest request){
        movieService.getUpDataInfo(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 物流
     * @param subscriber
     */
    public void getLogistics(Subscriber subscriber, OrderRequest request){
        movieService.getLogistics(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 我拍中
     * @param subscriber
     */
    public void getLottery(Subscriber subscriber, LuckyRequest request){
        movieService.getLottery(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 催单
     * @param subscriber
     */
    public void getReminder(Subscriber subscriber, OrderRequest request){
        movieService.getReminder(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }


    /**
     * 用户消耗
     * @param subscriber
     */
    public void getUserMoney(Subscriber subscriber, ShopDetailRequest request){
        movieService.getUserMoney(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     *查询订单状态
     * @param subscriber
     */
    public void queryPayState(Subscriber subscriber, OrderRequest request){
        movieService.queryPayState(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     *启动广告
     * @param subscriber
     */
    public void getSplashAdvs(Subscriber subscriber){
        movieService.getSplashAdvs("ANDROID").subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }


    /**
     * 查询是否可以购买
     * @param subscriber
     */
    public void getOrderIsBuy(Subscriber subscriber, OrderRequest request){
        movieService.getOrderIsBuy(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     *app更新
     * @param subscriber
     */
    public void getAppVersion(Subscriber subscriber,UpAppRequest request){
        movieService.getAppVersion(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }


    /**
     *收藏
     * @param subscriber
     */
    public void getCollectShop(Subscriber subscriber,BaseIdRequest request){
        movieService.getCollectShop(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     *收藏列表
     * @param subscriber
     */
    public void getCollectList(Subscriber subscriber,BaseIdRequest request){
        movieService.getCollectList(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }


    /**
     *分享奖励
     * @param subscriber
     */
    public void getShareOk(Subscriber subscriber,BaseIdRequest request){
        movieService.getShareOk(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }




}
