package com.example.library.utils;

import com.blankj.utilcode.util.SPUtils;

/**
 * - @Description:
 * - @Author:  penglin
 * - @Time:   2018/04/18
 */

public class SPManager {

    private static final SPUtils SP_UTILS = SPUtils.getInstance();

    private static final String TOKEN = "token";
    private static final String USERNAME = "userName";
    private static final String GESTUREPASSWORD = "gesturePassword";
    private static final String RETRYTIMES = "retryTimes";
    private static final String GESTUREISVISIBLE = "gestureIsVisible";


    /**
     * @param userName 用户名
     */
    public static void putUserName(String userName) {
        SP_UTILS.put(USERNAME, userName);
    }

    public static String getUserName() {
        return SP_UTILS.getString(USERNAME);
    }

    /**
     * @param token 令牌
     */
    public static void putToken(String token) {
        SP_UTILS.put(TOKEN, token);
    }

    public static String getToken() {
        return SP_UTILS.getString(TOKEN);
    }

    /**
     * @param gesturePassword 手势密码
     */
    public static void putGesturePassword(String gesturePassword) {
        SP_UTILS.put(GESTUREPASSWORD, gesturePassword);
    }

    public static String getGesturePassword() {
        return SP_UTILS.getString(GESTUREPASSWORD);
    }

    /**
     * @param retryTimes 手势密码重试次数
     */
    public static void putRetryTimes(int retryTimes) {
        SP_UTILS.put(RETRYTIMES, retryTimes);
    }

    public static int getRetryTimes() {
        return SP_UTILS.getInt(RETRYTIMES);
    }

    /**
     * @param gestureIsVisible 手势密码是否可见
     */
    public static void putGestureIsVisible(boolean gestureIsVisible) {
        SP_UTILS.put(GESTUREISVISIBLE, gestureIsVisible);
    }

    public static boolean getGestureIsVisible() {
        return SP_UTILS.getBoolean(GESTUREISVISIBLE);
    }
}
