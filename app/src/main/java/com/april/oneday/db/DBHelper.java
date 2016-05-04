package com.april.oneday.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sul on 2016/4/19.
 */
public class DBHelper extends SQLiteOpenHelper {

    //打开或获取数据库
    //context 上下文
    //name 数据库的名字
    //factory null
    //version 数据库的版本（你自定义你新建的数据库的版本）

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //当用户第一次创建数据库的时候调用
    @Override
    public void onCreate(SQLiteDatabase db) {

        //一般在此处建立数据库对应的表
        String table ="create table user (username varchar(15), password varchar(18));";
        db.execSQL(table);
        //如果该表中需要有一些初始化数据插入，可以在这个函数内一并插入
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}

