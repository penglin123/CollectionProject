package com.example.yinlian.collectionproject.http.api;


import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;

/**
 * 接口相关
 * Created by Administrator on 2017/11/9.
 */

public interface ApiService {


  //  @GET("geocoding")
  //  Observable<String> getLocation(@Query("a") String a);//获取的请求结果为String

    @GET("geocoding")
    Observable<String> getLocation(@Body String a);//获取的请求结果为String
  //  @GET("geocoding")
    //Observable<City> getCity(@Query("a") String a);//获取的请求结果为实体类型


}
