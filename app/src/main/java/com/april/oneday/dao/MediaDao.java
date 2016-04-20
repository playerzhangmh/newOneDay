package com.april.oneday.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.april.oneday.bean.MediaInfo;
import com.april.oneday.db.MediaOpenHelper;

import java.util.ArrayList;
import java.util.List;

;

/**
 * Media数据库访问接口
 */
public class MediaDao {

    public static final int TYPE_TEXT=0;
    public static final int TYPE_SINGLE_PIC=1;
    public static final int TYPE_TWO_PIC=2;
    public static final int TYPE_THREE_PIC=3;

    //在构造方法中实例化
    private MediaOpenHelper mediaOpenHelper;

    /**
     * 构造方法
     */
    public MediaDao(Context context) {
        mediaOpenHelper = new MediaOpenHelper(context);
    }
    /**
     * 增:插入一条数据的接口
     */
    public void addMediaInfo(MediaInfo mediaInfo){

        SQLiteDatabase db = mediaOpenHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        /*
            字段列表
            private int type;
            private String desc;
            private String date;
            private String pic1;
            private String pic2;
            private String pic3;
            private String audio;
            private String video;
        */
        values.put("type",mediaInfo.getType());
        values.put("desc",mediaInfo.getDesc());
        values.put("date",mediaInfo.getDate());
        values.put("pic1",mediaInfo.getPic1());
        values.put("pic2",mediaInfo.getPic2());
        values.put("pic3",mediaInfo.getPic3());
        values.put("audio",mediaInfo.getAudio());
        values.put("video",mediaInfo.getVideo());
        db.insert(MediaOpenHelper.TANLE_NAME,null,values);
        db.close();
    }

    /**
     * 删除一条数据
     */
    public void deleteMediaInfo(MediaInfo mediaInfo){
        SQLiteDatabase db = mediaOpenHelper.getReadableDatabase();
        db.delete(MediaOpenHelper.TANLE_NAME,"type=? and desc=? and date=? and pic1=? and pic2=?and pic3=? and audio=? and video=?",
                new String[]{mediaInfo.getType()+"",mediaInfo.getDesc(),mediaInfo.getDate(),mediaInfo.getPic1(),
                mediaInfo.getPic2(),mediaInfo.getPic3(),mediaInfo.getAudio(),mediaInfo.getVideo()});
        db.close();
    }

    /**
     * 查询部分数据用于listView分页加载,返回存放MediaInfo的list集合
     */
    public List<MediaInfo> queryPartMediaInfo(int MaxNum,int startindex){

        List<MediaInfo> list = new ArrayList<>();
        SQLiteDatabase db = mediaOpenHelper.getReadableDatabase();

        //查询部分数据
        Cursor cursor = db.rawQuery(
                "select type,date,desc,pic1,pic2,pic3,audio,video from media_table order by _id desc limit ? offset ?",
                new String[]{MaxNum + "", startindex + ""});

        MediaInfo mediaInfo = null;
        while(cursor.moveToNext()){
            int type = cursor.getInt(0);
            String date = cursor.getString(1);
            String desc = cursor.getString(2);
            String pic1 = cursor.getString(3);
            String pic2 = cursor.getString(4);
            String pic3 = cursor.getString(5);
            String audio = cursor.getString(6);
            String video = cursor.getString(7);

            mediaInfo = new MediaInfo(type,desc,date,pic1,pic2,pic3,audio,video);
            list.add(mediaInfo);
            mediaInfo = null;
        }
        //4.关闭数据库
        cursor.close();
        db.close();

        return list;
    }


}
