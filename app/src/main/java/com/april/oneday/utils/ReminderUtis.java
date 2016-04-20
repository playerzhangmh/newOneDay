package com.april.oneday.utils;


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
}
