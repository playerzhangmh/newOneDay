package com.april.oneday.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Media数据库OpenHelper
 */
public class MediaOpenHelper extends SQLiteOpenHelper {

    //数据库表名,方便后期我们在实现数据库操作的时候能方便去使用表名,同时也方便后期去修改表名
    public static final String TANLE_NAME="media_table";

    public MediaOpenHelper(Context context) {
        super(context, "media.db", null, 1);
    }

    //第一次创建数据库时调用,创建表结构
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表结构语句
/*       create table media_table(
        _id integer primary key autoincrement,
                type varchar(2),
                date varchar(20),
                desc varchar(100),
                pic1 varchar(20),
                pic2 varchar(20),
                pic3 varchar(20),
                audio varchar(20),
                video varchar(20)
        );
*/
        db.execSQL("create table media_table(_id integer primary key autoincrement,type varchar(2),date varchar(20),desc varchar(100),pic1 varchar(20),pic2 varchar(20),pic3 varchar(20),audio varchar(20),video varchar(20))");
    }

    //当数据库版本发生变化的时候调用
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
