package com.aution.paidd.greendao.gen;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, UserBeanDao.class);
        MigrationHelper.migrate(db, AdvsBeanDao.class);
    }
}