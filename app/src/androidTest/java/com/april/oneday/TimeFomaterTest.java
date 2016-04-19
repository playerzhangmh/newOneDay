package com.april.oneday;

import android.test.AndroidTestCase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by coins on 2016/4/19.
 */
public class TimeFomaterTest extends AndroidTestCase {
    public void testLongFormaterDate(){
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        String pattern="yyyy-MM-dd hh:mm:ss";
        SimpleDateFormat format=new SimpleDateFormat(pattern);
        StringBuffer sb=new StringBuffer();
        String format1 = format.format(date);
        Log.v("TimeFomaterTest",format1);
    }
    public void testgetweek(){

        int WEEKDAYS = 7;
        String[] week=new String[]{"sunday","monday","tuesday","wendesday","thrusday","friday","sunday"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
        Log.v("TimeFomaterTest",week[dayIndex-1]);

    }
}
