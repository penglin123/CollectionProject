package com.example.yinlian.collectionproject.http.api;


import com.example.yinlian.collectionproject.http.bean.BooksByCatsBean;
import com.example.yinlian.collectionproject.http.bean.BooksByCatsBean1;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

    @GET("/book/by-categories")
    Observable<BooksByCatsBean> getBooksByCats(@Query("gender") String gender,
                                               @Query("type") String type,
                                               @Query("major") String major,
                                               @Query("minor") String minor,
                                               @Query("start") int start,
                                               @Query("limit") int limit);


    @POST("/book/by-categories")
    Observable<BooksByCatsBean> getBooksByCats1(@Body BooksByCatsBean1 booksByCatsBean1);
}
