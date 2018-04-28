package com.example.library.db.helper;

import android.content.Context;

import com.example.library.db.gen.DaoMaster;
import com.example.library.db.gen.DaoSession;


/**
 * Created by penglin on 2018/4/12.
 */
public class DbCore {
    private static final String DB_NAME = "collection.db";
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    private static Context mContext;
    public static void init(Context context) {
        mContext = context;
    }
    public static DaoMaster getDaoMaster() {
        if (daoMaster == null) {
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
