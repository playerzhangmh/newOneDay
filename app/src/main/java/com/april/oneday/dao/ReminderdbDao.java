package com.april.oneday.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;


import com.april.oneday.bean.ReminderInfo;
import com.april.oneday.db.ReminderDatadb;
import com.april.oneday.utils.ReminderUtis;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by coins on 2016/4/18.
 */
public class ReminderdbDao {

    //过期和当天未过期的标记
    public static final int INACTIVE_FLAG = 1;
    public static final int DAILYACTIVE_FLAG =2 ;
    //按年月日排序的标记
    public static final int SortFlag_YEAR =-1 ;
    public static final int SortFlag_MONTH =-2 ;
    public static final int SortFlag_DAY =-3 ;
    public static final int REPEAT_DAY = 3;
    public static final int REPEAT_MONTH = 2;
    public static final int REPEAT_YEAR = 1;
    private static final String TAG = "ReminderdbDao";
    private static final int NO_REPEAT = 0;

    public final ContentResolver contentResolver;
    public final Uri urireminder;
    public final Uri urireminder2;
    public final Uri urireminder3;
    public Context context;
    public static int VERSION=1;

    public ReminderdbDao(Context context) {
        this.context=context;
        contentResolver = context.getContentResolver();
        urireminder = Uri.parse("content://com.oneday.reminder/reminderInfo");
        urireminder2=Uri.parse("content://com.oneday.reminder/reminderInfo/reminder_date");
        urireminder3=Uri.parse("content://com.oneday.reminder/reminderInfo/reminder_time");

    }
    /*
    * 1 在创建reminder界面插入一条数据
    * 2 reminder显示界面有删除数据
    * 3 reminder显示界面有update数据
    * 4 reminder项上toggle_button根据时间是否过期以及是否属于重复提醒来确定其显示
    * 5 然后就是侧边栏根据filter来筛选数据（多种list排序）
    * 根据关键字 日期和标题
    * 根据年月日排序
    * 根据是否active排序
    * 是否时间已到该提醒用户
    *
    *
    * */

    /*
    * 表格项有id ,reminder名称，日期，时间，重复模式，提醒内容，铃声路径，震动模式（长度），震动次数，标记图标Rid，图标背景颜色（颜色，eg：red，white）,是否active
    *
    * id integer primary key autoincrement,reminder_name varchar(40),reminder_date varchar(40),reminder_time varchar(40)," +
                "repeatType integer,reminder_comment varchar(150),reminder_ringpath varchar(100),reminder_vabrate_type integer,reminder_vabrate_times integer,reminder_tag_Rid integer," +
                "reminder_tag_color varchar(10)
    * */

    //插入数据
    public void insertReminder(String reminder_name, String reminder_date, String reminder_time, int repeatType, String reminder_comment,
                               String ring_path, int vabrate_type, int vabrate_times, int iconRid, String iconBgColor){
        ContentValues values=new ContentValues();
        values.put("reminder_name",reminder_name);
        values.put("reminder_date",reminder_date);
        values.put("reminder_time",reminder_time);
        values.put("repeatType",repeatType);
        values.put("reminder_comment",reminder_comment);
        values.put("reminder_ringpath",ring_path);
        values.put("reminder_vabrate_type",vabrate_type);
        values.put("reminder_vabrate_times",vabrate_times);
        values.put("reminder_tag_Rid",iconRid);
        values.put("reminder_tag_color",iconBgColor);
        values.put("reminder_active",0);//默认插入数据时为0
        contentResolver.insert(urireminder, values);
    }

    //删除数据
    public int deleteReminder(String reminder_name){
        int delete = contentResolver.delete(urireminder, "reminder_name=?", new String[]{reminder_name});
        return delete;
    }

    //修改数据,由于数据除了id外都可能会发生改变，所以bean中最好保存一个id
    public int updateReminder(int remind_id,String reminder_name, String reminder_date, String reminder_time, int repeatType, String reminder_comment,
                               String ring_path, int vabrate_type, int vabrate_times, int iconRid, String iconBgColor,int reminder_active){
        ContentValues values=new ContentValues();
        values.put("reminder_name",reminder_name);
        values.put("reminder_date",reminder_date);
        values.put("reminder_time",reminder_time);
        values.put("repeatType",repeatType);
        values.put("reminder_comment",reminder_comment);
        values.put("reminder_ringpath",ring_path);
        values.put("reminder_vabrate_type",vabrate_type);
        values.put("reminder_vabrate_times",vabrate_times);
        values.put("reminder_tag_Rid",iconRid);
        values.put("reminder_tag_color",iconBgColor);
        values.put("reminder_active",0);
        int update = contentResolver.update(urireminder, values, "id=?", new String[]{remind_id + ""});
        return update;
    }
    //根据repeat来修改active
    public void updateActive(ReminderInfo info){
        String reminder_date = info.getReminder_date();
        String reminder_time = info.getReminder_time();
        String newreminder_date="";
        String newreminder_time="";
        int repeatType = info.getRepeatType();
        ContentValues values=new ContentValues();
        long l = ReminderUtis.stringFormaterTolong(reminder_date + " " + reminder_time);
        Log.v(TAG,l+"");
        Log.v(TAG,ReminderUtis.longFormaterTostring(l));
        switch (repeatType){
            case REPEAT_DAY:
                String newdate1 = ReminderUtis.longFormaterTostring(l + 60000l * 60l * 24l);
                String[] split1 = newdate1.split(" ");
                newreminder_date=split1[0];
                newreminder_time=split1[1];
                break;
            case REPEAT_MONTH:
                String monthdays = ReminderUtis.getMonthdays(reminder_date.substring(0, reminder_date.lastIndexOf("-")));
                long days = Integer.parseInt(monthdays);
                Log.v(TAG,days+"");
                String newdate2 = ReminderUtis.longFormaterTostring(l + (60000l * 60l * 24l*days));
                Log.v(TAG,l + 60000l * 60l * 24l*days+"");
                String[] split2 = newdate2.split(" ");
                newreminder_date=split2[0];
                newreminder_time=split2[1];
                Log.v(TAG,newreminder_date+" "+newreminder_time);
                break;
            case REPEAT_YEAR:
                String year = ReminderUtis.getMonthdays(reminder_date.substring(0, reminder_date.indexOf("-")));
                String febDays = ReminderUtis.getFebDays(year);
                String newdate3 ="";
                if(febDays.equals("28")){
                    newdate3 = ReminderUtis.longFormaterTostring(l + 60000l * 60l * 24l*365l);
                }else {
                    newdate3 = ReminderUtis.longFormaterTostring(l + 60000l * 60l * 24l*366l);
                }
                String[] split3 = newdate3.split(" ");
                newreminder_date=split3[0];
                newreminder_time=split3[1];
                break;
            case NO_REPEAT:
                newreminder_date="";
                newreminder_time="";
                break;
        }

        if(newreminder_date.isEmpty()&&newreminder_time.isEmpty()){
            values.put("reminder_active",1);
        }else {
            values.put("reminder_date",newreminder_date);
            values.put("reminder_time",newreminder_time);
            values.put("reminder_active",0);
        }
        contentResolver.update(urireminder,values,"id=?",new String[]{info.getReminder_id()+""});
    }

    //获取数据库中所有数据
    public List<ReminderInfo> getAllReminderItem(){
        List<ReminderInfo> reminderInfos=new ArrayList<>();
        List<String> list=new ArrayList<>();
        Cursor datequery = contentResolver.query(urireminder2, new String[]{"reminder_date"}, null, null,null);
        while (datequery.moveToNext()){
            String date = datequery.getString(0);
            if(!list.contains(date)){
                list.add(date);
            }
        }
        for (String i:list){
            Cursor timequery = contentResolver.query(urireminder3, null,"reminder_date=?", new String[]{i}, null);
            while (timequery.moveToNext()){
                ReminderInfo info = getReminderInfo(timequery);
                reminderInfos.add(info);
            }
        }
        return reminderInfos;
    }

    //查找，根据日期
    public List<ReminderInfo> getReminderItemByDate(String reminder_date){
        List<ReminderInfo> reminderInfos=new ArrayList<>();
        Cursor query = contentResolver.query(urireminder3, null, "reminder_date=?", new String[]{reminder_date}, null);
        while (query.moveToNext()){
            ReminderInfo info = getReminderInfo(query);
            reminderInfos.add(info);
        }
        return reminderInfos;
    }
    //查找，过期的reminder或者当天未过期的reminder
    public List<ReminderInfo> getInactiveReminders(int inactiveOrdailyactiveflag){
        String currentdate = ReminderUtis.getCurrentdate();
        Log.v(TAG,currentdate);
        String[] split = currentdate.split(" ");
        String date=split[0];
        String time=split[1];
        ReminderDatadb helper=new ReminderDatadb(context,"reminder.db",null,VERSION);
        SQLiteDatabase db = helper.getReadableDatabase();
        List<ReminderInfo> reminderInfos=new ArrayList<>();
        List<ReminderInfo> extraList=new ArrayList<>();//用来装当天没过期的
        List<String> list=new ArrayList<>();
        String querybeforedate="select reminder_date from reminderInfo where reminder_date between '1899-01-01'and '"+date+"' order by reminder_date desc;";
        Cursor datecursor = db.rawQuery(querybeforedate, null);
        while (datecursor.moveToNext()){
            String dates = datecursor.getString(0);
            if(!list.contains(dates)){
                list.add(dates);
            }
        }
        for (String i:list){
            Cursor query = contentResolver.query(urireminder3, null, "reminder_date=?", new String[]{i}, null);
            while (query.moveToNext()){
                ReminderInfo info = getReminderInfo(query);
                reminderInfos.add(info);
            }
        }
          //获取当天没过期的
        String queryextra="select * from (select * from reminderInfo where reminder_date='"+date+"') as info where reminder_time between '"+time+"' and '23:59:59';";
        Cursor extracursor = db.rawQuery(queryextra, null);
        while (extracursor.moveToNext()){
            ReminderInfo reminderInfo = getReminderInfo(extracursor);
            extraList.add(reminderInfo);
        }
        Log.v(TAG,reminderInfos.size()+"");
        Log.v(TAG,extraList.size()+"");
        if(inactiveOrdailyactiveflag==INACTIVE_FLAG){
            reminderInfos.removeAll(extraList);
            return reminderInfos;
        }
        if(inactiveOrdailyactiveflag==DAILYACTIVE_FLAG){
            return extraList;
        }
        return null;
    }



    //查找，根据名称
    public List<ReminderInfo> getReminderItemByName(String reminder_name){
        List<ReminderInfo> reminderInfos=new ArrayList<>();
        List<String> list=new ArrayList<>();
        Cursor datequery = contentResolver.query(urireminder2, new String[]{"reminder_date"}, "reminder_name=?", new String[]{reminder_name}, null);
        while (datequery.moveToNext()){
            String dates = datequery.getString(0);
            if(!list.contains(dates)){
                list.add(dates);
            }
        }

        for (String i:list){
            Cursor query = contentResolver.query(urireminder3, null, "reminder_date=?", new String[]{i}, null);
            while (query.moveToNext()){
                ReminderInfo info = getReminderInfo(query);
                reminderInfos.add(info);
            }
        }
        return reminderInfos;
    }

    /*
    * 年月日排序
    * 根据用户输入年份来获取
    * */
    public List<ReminderInfo> getSortedReminders(int sortflag,String date){
        ReminderDatadb helper=new ReminderDatadb(context,"reminder.db",null,VERSION);
        SQLiteDatabase db = helper.getReadableDatabase();
        List<ReminderInfo> reminderInfos=new ArrayList<>();
        String query="";
        switch (sortflag){
            case SortFlag_YEAR:
                query="select reminder_date,id from reminderInfo where reminder_date between '"+date+"-01-01' and '"+date+"-12-31' order by reminder_date desc;";
                Log.v(TAG,"zheli");
                break;
            case SortFlag_MONTH:
                String monthdays = ReminderUtis.getMonthdays(date);
                query="select reminder_date,id from reminderInfo where reminder_date between '"+date+"-01' and '"+date+"-"+monthdays+"' order by reminder_date desc;";
                break;
            case SortFlag_DAY:
                break;
        }
        if(query.isEmpty()){
            return getReminderItemByDate(date);
        }
        Cursor cursor = db.rawQuery(query, null);
        List<String> list=new ArrayList<>();
        while (cursor.moveToNext()){
            String dates = cursor.getString(0);
            if(!list.contains(dates)){
                list.add(dates);
            }
        }

        for (String i:list) {
            Cursor daysquery = contentResolver.query(urireminder3, null, "reminder_date=?", new String[]{i}, null);
            while (daysquery.moveToNext()){
                ReminderInfo info = getReminderInfo(daysquery);
                reminderInfos.add(info);
            }
        }
        return reminderInfos;
    }

    @NonNull
    private ReminderInfo getReminderInfo(Cursor query) {
        int remind_id = query.getInt(0);
        String reminder_names = query.getString(1);
        String reminder_dates = query.getString(2);
        String reminder_time = query.getString(3);
        int repeatType = query.getInt(4);
        String reminder_comment = query.getString(5);
        String ring_path = query.getString(6);
        int vabrate_type = query.getInt(7);
        int vabrate_times = query.getInt(8);
        int iconRid = query.getInt(9);
        String iconBgColor = query.getString(10);
        int reminder_active = query.getInt(11);
        return new ReminderInfo(remind_id,reminder_names, reminder_dates,reminder_time, repeatType, reminder_comment,
                ring_path,vabrate_type, vabrate_times, iconRid, iconBgColor,reminder_active);
    }
}
