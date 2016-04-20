package com.april.oneday.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.april.oneday.db.ReminderDatadb;


/**
 * Created by coins on 2016/4/18.
 */
public class Reminder_ContentProvider extends ContentProvider {

    private SQLiteDatabase readableDatabase;
    static UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
    static {
        matcher.addURI("com.oneday.reminder","reminderInfo",1);
        matcher.addURI("com.oneday.reminder","reminderInfo/reminder_date",2);
        matcher.addURI("com.oneday.reminder","reminderInfo/reminder_time",3);

    }

    @Override
    public boolean onCreate() {
        ReminderDatadb helper=new ReminderDatadb(getContext(),"reminder.db",null,1);
        readableDatabase = helper.getReadableDatabase();
        return false;
    }
    //在reminder页面注册一个观察者，一旦数据有改动就我们就把数据重新刷新一次

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Cursor info=null;
       if(matcher.match(uri)==1){
           Cursor reminderInfo = readableDatabase.query("reminderInfo", strings, s, strings1, s1, null, null);
           info=reminderInfo;
       }else if(matcher.match(uri)==2){
           Cursor reminderInfo = readableDatabase.query("reminderInfo", strings, s, strings1, s1, null, "reminder_date desc");
           info=reminderInfo;
       }else if(matcher.match(uri)==3){
           Cursor reminderInfo = readableDatabase.query("reminderInfo", strings, s, strings1, s1, null, "reminder_time desc");
           info=reminderInfo;
       }
        return info;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        if(matcher.match(uri)==1){

            readableDatabase.insert("reminderInfo", null, contentValues);
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int deleteraw=0;
        if(matcher.match(uri)==1){
            deleteraw = readableDatabase.delete("reminderInfo", s, strings);
        }
        return deleteraw;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int update=0;
        if(matcher.match(uri)==1){
            update=readableDatabase.update("reminderInfo",contentValues,s,strings);
        }
        return update;
    }
}
