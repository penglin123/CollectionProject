package com.example.yinlian.collectionproject.http.api;

/**
 * Created by xwu on 2018/4/10.
 */

public class NetManage {

    private String baseUrl = "http://api.zhuishushenqi.com";

    private volatile static ApiService apiService;

    public static ApiService getApiService() {
        if (apiService == null) {
            synchronized (NetManage.class) {
                if (apiService == null) {
                    new NetManage();
                }
            }
        }
        return apiService;
    }


    private NetManage() {
        ApiHelper apiHelper = new ApiHelper();
        apiService = apiHelper.getSimpleRetrofit(baseUrl).create(ApiService.class);
    }

}
