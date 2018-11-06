package com.qxhd.yellow.common;

import com.qxhd.yellow.response.AdvsResponse;
import com.qxhd.yellow.response.LoginResponse;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface Engine {



    @GET("user/api/index")
    Observable<AdvsResponse> getAdvs(@QueryMap Map<String, String> map);



    @GET("user/api/index")
    Observable<LoginResponse> getInitLogin(@QueryMap Map<String, String> map);



}
