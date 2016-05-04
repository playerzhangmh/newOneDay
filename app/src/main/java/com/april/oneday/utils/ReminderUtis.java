package com.april.oneday.utils;


import com.april.oneday.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by coins on 2016/4/19.
 */
public class ReminderUtis {



    public static String getCurrentdate(){
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        String pattern="yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format=new SimpleDateFormat(pattern);
        String currentdate = format.format(date);
        return currentdate;
    }

    //获取某个月的天数
    public static String getMonthdays(String date){//为yy-mm这种格式时使用
        String[] split = date.split("-");
        String days="";
        String month = split[1];
        switch (month){
            case "01":
            case "03":
            case "05":
            case "07":
            case "08":
            case "10":
            case "12":
                days="31";
                break;
            case "02":
                days=getFebDays(split[0]);
                break;
            case "04":
            case "06":
            case "09":
            case "11":
                days="30";
                break;
        }

        return days;
    }

    //根据年获取二月份的天数
    public static String getFebDays(String year){
        int tagyear = Integer.parseInt(year);
        if(tagyear%100!=0&&tagyear%4==0){
            return "29";
        }
        if(tagyear%400==0){
            return "29";
        }
        if(tagyear%3200==0&&tagyear%172800==0){
            return "29";
        }
        return "28";
    }

    //将时间字符串转化成longms
    public static long stringFormaterTolong(String date){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time=0;
        try {
            Date parse = format.parse(date);
            time = parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time;
    }

    //将long转化成string
    public static String longFormaterTostring(long datems){
        Date date = new Date(datems);
        String pattern="yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format=new SimpleDateFormat(pattern);
        String currentdate = format.format(date);
        return currentdate;
    }

    //获取星期几
    public static String getWeek(Date date){
        int WEEKDAYS = 7;
        String[] week=new String[]{"周日","周一","周二","周三","周四","周五","周六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
        return week[dayIndex-1];
    }
    //获取同一天的时间数据
    public static long getLongfromTime(String time){
        String pattern="yy-MM-dd HH:mm:ss";
        SimpleDateFormat format=new SimpleDateFormat(pattern);
        long time1 =0l;
        try {
            Date parse = format.parse("2000-10-10 "+time);
            time1 = parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time1;
    }
    public static String gettimefromLong(long l){
        String pattern="yy-MM-dd HH:mm:ss";
        SimpleDateFormat format=new SimpleDateFormat(pattern);
        Date date=new Date(l);
        String format1 = format.format(date);
        return format1.substring(format1.indexOf(" ")+1);
    }

    public static String getRealRingPath(String ringname){
        String[] ring={R.raw.aa+"",R.raw.ab+"",R.raw.ac+"",R.raw.ad+"",R.raw.ae+"",R.raw.af+"",R.raw.ag+""};
        String realring="";
        switch (ringname){
            case "巴黎浪漫":
                realring=ring[0];
                break;
            case "晨曦":
                realring=ring[1];
                break;
            case "回忆":
                realring=ring[2];
                break;
            case "魔力钟声":
                realring=ring[3];
                break;
            case "清新的早晨":
                realring=ring[4];
                break;
            case "午后的约会":
                realring=ring[5];
                break;
            case "苏格拉风琴":
                realring=ring[6];
                break;
        }
        return realring;
    }
    public static int getBackground(int iconRid){
        int[] background={R.drawable.routine3_bg1,R.drawable.routine3_bg2,R.drawable.routine3_bg3,
                R.drawable.routine3_bg4,R.drawable.routine3_bg5,R.drawable.routine3_bg3,
                R.drawable.routine3_bg2,R.drawable.routine3_bg1,R.drawable.routine3_bg5,
                R.drawable.routine3_bg4,R.drawable.routine3_bg2,R.drawable.routine3_bg5,};
        int[] icon={R.drawable.routine3_tag_exercise,R.drawable.routine3_tag_outdoor,R.drawable.routine3_tag_date
                ,R.drawable.routine3_tag_bike,R.drawable.routine3_tag_clear,R.drawable.routine3_tag_ktv
                ,R.drawable.routine3_tag_tv,R.drawable.routine3_tag_internet,R.drawable.routine3_tag_work
                ,R.drawable.routine3_tag_training,R.drawable.routine3_tag_sleep,R.drawable.routine3_tag_test};
        int bg=0;
        for(int i=0;i<icon.length;i++){
            if(icon[i]==iconRid){
               bg=background[i];
                break;
            }
        }
        return bg;
    }
    public static String getIconName(int iconRid){
        int[] icon={R.drawable.routine3_tag_exercise,R.drawable.routine3_tag_outdoor,R.drawable.routine3_tag_date
                ,R.drawable.routine3_tag_bike,R.drawable.routine3_tag_clear,R.drawable.routine3_tag_ktv
                ,R.drawable.routine3_tag_tv,R.drawable.routine3_tag_internet,R.drawable.routine3_tag_work
                ,R.drawable.routine3_tag_training,R.drawable.routine3_tag_sleep,R.drawable.routine3_tag_test};
        String[] tagname={"跑步","户外","约会"
                ,"骑行","家务","K歌"
                ,"TV","上网","工作"
                ,"健身","休息","会议"};
        String iconname="";
        for(int i=0;i<icon.length;i++){
            if(icon[i]==iconRid){
                iconname=tagname[i];
                break;
            }
        }
        return iconname;
    }




}
