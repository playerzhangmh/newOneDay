package com.april.oneday.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by coins on 2016/4/20.
 */
public class RoutinesDatadb extends SQLiteOpenHelper {
    public RoutinesDatadb(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /*
    * 创建计划表，包含计划id，title，计划周期,是否active，处于计划哪一天
    * 创建进程表，包含进程id，计划id,是否进行中active，开始时间starttime(用datetime存进去，方便计算出long值)，processtime（long，用varcharc存），终止endtime
    * 创建某天事件详情表，包含事情id，进程id，iconRid（图标）,iconname（图标对应事情，可以根据事情时间大小的覆盖，如果超过当前事情的时间范围就会覆盖掉，如果在其范围中就继续添加别的事情）
    * ,starttime（time存入，不让单个事件项目跨天）,endtime,commnet（事情说明）,ringbefore（long，用varchar存入，计算通知时间时根据process提供的起始日期和该事情的starttime来决定是否要通知）,ringpath,vebrate
    * */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createSchedule="create table schedule (schedule_id integer primary key autoincrement,schedule_title varchar(50),schedule_cycles integer" +
                ",schedule_active integer,schedule_process integer);";
        String createScheduleProcess="create table process (process_id integer,s_id integer," +
                "process_active integer,process_start datetime,process_end datetime,process_time varchar(30));";
        String createScheduleProcessDetails="create table details (detail_id integer primary key autoincrement,p_id integer,s_id integer" +
                ",iconRid integer,icon_name varchar(50),detail_start time,detail_end time,detail_comment varchar(100)," +
                "detail_ringbefore varchar(30),detail_ringpath varchar(30),detail_vabrate integer);";

        sqLiteDatabase.execSQL(createSchedule);
        sqLiteDatabase.execSQL(createScheduleProcess);
        sqLiteDatabase.execSQL(createScheduleProcessDetails);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
