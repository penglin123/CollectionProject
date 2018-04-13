package com.example.yinlian.collectionproject.db.helper;

import android.content.Context;

import com.example.yinlian.collectionproject.db.gen.DaoMaster;
import com.example.yinlian.collectionproject.db.gen.DaoSession;


/**
 * Created by penglin on 2018/4/12.
 */
public class DbCore {
    private static final String DEFAULT_DB_NAME = "collection.db";
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    private static Context mContext;
    private static String DB_NAME;

    public static void init(Context context) {
        init(context, DEFAULT_DB_NAME);
    }

    public static void init(Context context, String dbName) {
        if (context == null) {
            throw new IllegalArgumentException("context can't be null");
        }
        mContext = context;
        DB_NAME = dbName;
    }

    public static DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            //此处不可用 DaoMaster.DevOpenHelper, 那是开发辅助类，我们要自定义一个，方便升级
            MySQLiteOpenHelper helper = new MySQLiteOpenHelper(mContext, DB_NAME,null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession() {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }


}
