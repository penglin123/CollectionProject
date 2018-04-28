package com.example.library.http.api;


import com.example.library.http.bean.BaseResponse;
import com.example.library.http.bean.BooksByCatsResponse;
import com.example.library.http.bean.LoginRequest;
import com.example.library.http.bean.LoginResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * 接口相关
 *
 * @author penglin
 * @date 2018/4/10
 */

public interface ApiService {

    @GET("/book/by-categories")
    Observable<BooksByCatsResponse> getBooksByCats(@Query("gender") String gender,
                                                   @Query("type") String type,
                                                   @Query("major") String major,
                                                   @Query("minor") String minor,
                                                   @Query("start") int start,
                                                   @Query("limit") int limit);


    @POST("/book/by-categories")
    Observable<BaseResponse<BooksByCatsResponse>> post(@Body BooksByCatsResponse booksByCatsBean1);

    @POST("/book/by-categories")
    Observable<LoginResponse> login(@Body LoginRequest loginRequest);

}
