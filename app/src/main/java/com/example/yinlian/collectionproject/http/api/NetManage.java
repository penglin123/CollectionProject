package com.example.yinlian.collectionproject.http.api;

/**
 * Created by xwu on 2018/4/10.
 */

public class NetManage {

    private String baseUrl = "http://gc.ditu.aliyun.com/";//阿里云根据地区名获取经纬度接口

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
        ApiHelper baseApi = new ApiHelper();
        apiService = baseApi.getRetrofit(baseUrl).create(ApiService.class);
    }
}
