package com.example.yinlian.collectionproject.http.api;


import com.example.yinlian.collectionproject.http.bean.BaseResult;
import com.example.yinlian.collectionproject.http.bean.BooksByCatsBean;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * 接口相关
 * @author penglin
 * @date 2018/4/10
 */

public interface ApiService {

    @GET("/book/by-categories")
    Observable<BooksByCatsBean> getBooksByCats(@Query("gender") String gender,
                                               @Query("type") String type,
                                               @Query("major") String major,
                                               @Query("minor") String minor,
                                               @Query("start") int start,
                                               @Query("limit") int limit);


    @POST("/book/by-categories")
    Observable<BaseResult<BooksByCatsBean>> getBooksByCats1(@Body BooksByCatsBean booksByCatsBean1);
}
