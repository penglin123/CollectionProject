package com.example.library.http.api;

import android.content.Context;

/**
 * @author penglin
 * @date 2018/4/10
 */


public class NetManage {

    private String baseUrl = "http://api.zhuishushenqi.com";

    private volatile static ApiService apiService;

    public static ApiService getApiService(Context context) {
        if (apiService == null) {
            synchronized (NetManage.class) {
                if (apiService == null) {
                    new NetManage(context);
                }
            }
        }
        return apiService;
    }

    private NetManage(Context context) {
        ApiHelper apiHelper = new ApiHelper();
        apiService = apiHelper.getRetrofit(baseUrl,context).create(ApiService.class);
    }


}
