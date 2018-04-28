package com.example.library.base;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.blankj.utilcode.util.Utils;
import com.example.library.db.helper.DbCore;
import com.squareup.leakcanary.LeakCanary;
import com.baidu.mapapi.CoordType;

/**
 * @author penglin
 * @date 2018/4/10
 */


public class BaseApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        //数据库初始化
        DbCore.init(this);
        //内存泄露检测初始化
        LeakCanary.install(this);
        //工具类初始化
        Utils.init(this);
        //百度地图初始化
        SDKInitializer.initialize(this);

    }


    public static Context getContext() {
        return mContext;
    }
}
