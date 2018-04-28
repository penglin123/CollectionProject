package com.example.library.db.helper;

/**
 * Created by penglin on 2018/4/12.
 */
public class DbUtil {
    private static UserHelper userHelper;

    public static UserHelper getUserHelper() {
        if (userHelper == null) {
            userHelper = new UserHelper(DbCore.getDaoSession().getUserDao());
        }
        return userHelper;
    }
}