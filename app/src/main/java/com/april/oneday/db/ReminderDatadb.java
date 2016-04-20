package com.april.oneday.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by coins on 2016/4/18.
 */
public class ReminderDatadb extends SQLiteOpenHelper {
    public ReminderDatadb(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /*
    * 表格项有id ,reminder名称，日期，时间，重复模式，提醒内容，铃声路径，震动模式（长度），震动次数，标记图标Rid，图标背景颜色（颜色，eg：red，white）,是否active
    * */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create="create table reminderInfo (id integer primary key autoincrement,reminder_name varchar(40),reminder_date date,reminder_time time," +
                "repeatType integer,reminder_comment varchar(150),reminder_ringpath varchar(100),reminder_vabrate_type integer,reminder_vabrate_times integer,reminder_tag_Rid integer," +
                "reminder_tag_color varchar(10),reminder_active integer);";
        sqLiteDatabase.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
