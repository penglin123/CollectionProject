package com.example.library.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.example.library.db.gen.DaoMaster;
import com.example.library.db.gen.UserDao;

import org.greenrobot.greendao.database.Database;

/**
 * Created by penglin on 2018/4/12.
 */

public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
    MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {

            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        },UserDao.class);
    }
}
