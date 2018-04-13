package com.example.yinlian.collectionproject.app;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.example.yinlian.collectionproject.db.helper.DbCore;
import com.squareup.leakcanary.LeakCanary;

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
        DbCore.init(this);//数据库初始化
        LeakCanary.install(this);//内存泄露检测初始化
        Utils.init(this);//工具类初始化
    }


    public static Context getContext() {
        return mContext;
    }
}
