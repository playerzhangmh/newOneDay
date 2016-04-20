package com.april.oneday;

import android.test.AndroidTestCase;
import android.util.Log;


import com.april.oneday.bean.ReminderInfo;
import com.april.oneday.dao.ReminderdbDao;

import java.util.List;

/**
 * Created by coins on 2016/4/18.
 */
public class ReminderdbTest extends AndroidTestCase {
    public void testInsert(){
        ReminderdbDao dao=new ReminderdbDao(getContext());
        for(int i=0;i<5;i++){
            dao.insertReminder("ssadeatingsad","2016-06-19","21:00:19",2,"i love eating","ringpath",1,3,3424234,"red");
        }
       // dao.insertReminder("eating","2016-3-25","19:00:09",1,"i love eating","ringpath",1,3,3424234,"red");
    }
    public void testDelete(){
        ReminderdbDao dao=new ReminderdbDao(getContext());

        dao.deleteReminder("eating");
    }
    public void testUpdate(){
        ReminderdbDao dao=new ReminderdbDao(getContext());

        dao.updateReminder(6,"read","2015-06-28","18:18:18",2,"i hate eating","realpath",1,2,34324,"dark",1);
    }
    public void  testQuerybyDate(){
        ReminderdbDao dao=new ReminderdbDao(getContext());

        List<ReminderInfo> reminderItemByDate = dao.getReminderItemByDate("2017-03-19");
        Log.v("ReminderdbTest",reminderItemByDate.toString());
        assertTrue(10==reminderItemByDate.size());
    }
    public void testInactive(){
        ReminderdbDao dao=new ReminderdbDao(getContext());
      //  List<ReminderInfo> inactiveReminders = dao.getInactiveReminders(ReminderdbDao.INACTIVE_FLAG);
        List<ReminderInfo> inactiveReminders = dao.getInactiveReminders(ReminderdbDao.DAILYACTIVE_FLAG);

        Log.v("ReminderdbTest",inactiveReminders.toString());
        Log.v("ReminderdbTest",inactiveReminders.size()+"");
        assertTrue(20==inactiveReminders.size());
    }
    public void testQueryByname(){
        ReminderdbDao dao=new ReminderdbDao(getContext());
        List<ReminderInfo> eating = dao.getReminderItemByName("eating");
        Log.v("ReminderdbTest",eating.toString());
        assertTrue(10==eating.size());

    }
    public void testSortreminder(){
        ReminderdbDao dao=new ReminderdbDao(getContext());
       // List<ReminderInfo> sortedReminders = dao.getSortedReminders(ReminderdbDao.SortFlag_YEAR, "2016");
        List<ReminderInfo> sortedReminders = dao.getSortedReminders(ReminderdbDao.SortFlag_DAY, "2016-04-19");

        Log.v("ReminderdbTest",sortedReminders.size()+sortedReminders.toString());
        assertTrue(sortedReminders.size()==20);
    }

    public void testgetall(){
        ReminderdbDao dao=new ReminderdbDao(getContext());
        List<ReminderInfo> allReminderItem = dao.getAllReminderItem();
        for (ReminderInfo info: allReminderItem){
            Log.v("ReminderdbTest",info.toString());
        }
    }

    public void testUpdateafterNotify(){
        ReminderdbDao dao=new ReminderdbDao(getContext());
        dao.updateActive(new ReminderInfo(185,"eating","2016-06-19","21:00:19",2,"i love eating","ringpath",1,3,3424234,"red",0));
    }

}
