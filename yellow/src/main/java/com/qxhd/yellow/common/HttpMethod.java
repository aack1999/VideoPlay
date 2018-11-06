package com.qxhd.yellow.common;

import android.text.TextUtils;


import com.framework.core.utils.LogUtils;
import com.framework.core.utils.ParseUtils;
import com.qxhd.yellow.request.AdvsRequest;
import com.qxhd.yellow.request.RegsiterRequest;

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
    public static final String BASE_URL="http://134.175.135.21/";



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
     * 获取广告
     * @param subscriber
     */
    public void getAllAdvs(AdvsRequest request,Subscriber subscriber){
        movieService.getAdvs(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 初始化登录
     * @param subscriber
     */
    public void getInitLogin(RegsiterRequest request, Subscriber subscriber){
        movieService.getAdvs(ParseUtils.toMap(request)).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }


}
