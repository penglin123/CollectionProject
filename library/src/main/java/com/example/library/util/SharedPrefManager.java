package com.example.library.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.HashMap;

/**
 * Created by kai.chen on 2017/7/3.
 * 一个SHARED_NAME一个manager
 */

public class SharedPrefManager {
    private static final int SHARED_MODE = Context.MODE_PRIVATE;

    private static final String SHARED_NAME = "wtk_shared";

    public static final String USER_ID = "userId";//id
    public static final String USER_JOB_NUMBER = "userJobNumber";//工号
    public static final String USER_NAME = "userName";//姓名

    //Util
    private Context context;

    public SharedPrefManager(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * 获取 userId
     *
     * @return userId
     */
    @Nullable
    public String getUserId() {
        return context
                .getSharedPreferences(SHARED_NAME, SHARED_MODE)
                .getString(USER_ID, null);
    }

    /**
     * 设置 userId
     *
     * @param userId userId
     * @return 是否保存成功
     */
    public boolean setUserId(String userId) {
        return context
                .getSharedPreferences(SHARED_NAME, SHARED_MODE)
                .edit()
                .putString(USER_ID, userId)
                .commit();
    }

    /**
     * 设置user信息
     *
     * @param userId
     * @param userJobNumber
     * @param userName
     * @return
     */
    public boolean setUser(String userId, String userJobNumber, String userName) {
        return context
                .getSharedPreferences(SHARED_NAME, SHARED_MODE)
                .edit()
                .putString(USER_ID, userId)
                .putString(USER_JOB_NUMBER, userJobNumber)
                .putString(USER_NAME, userName)
                .commit();
    }


    /**
     * 获取用户所有信息
     *
     * @return
     */
    public HashMap<String, String> getUser() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        SharedPreferences preferences = context.getSharedPreferences(SHARED_NAME, SHARED_MODE);
        hashMap.put(USER_ID, preferences.getString(USER_ID, null));
        hashMap.put(USER_JOB_NUMBER, preferences.getString(USER_JOB_NUMBER, null));
        hashMap.put(USER_NAME, preferences.getString(USER_NAME, null));
        return hashMap;
    }


}
