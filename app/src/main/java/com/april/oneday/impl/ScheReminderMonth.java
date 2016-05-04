package com.april.oneday.impl;

import android.view.View;

import com.april.oneday.activity.MainActivity;
import com.april.oneday.base.ScheduleBasePage;
import com.april.oneday.bean.ReminderInfo;
import com.april.oneday.dao.ReminderdbDao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by wancc on 2016/4/21.
 */
public class ScheReminderMonth extends ScheduleBasePage {

    protected ArrayList<ReminderInfo> reminderInfos;

    public ScheReminderMonth(MainActivity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {

        /*if (reminderInfos==null){
            reminderInfos = new ArrayList<>();
        }*/

//        getSortedReminders(int sortflag,String date)
        Date dt = new Date();
        SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd");
        String date = matter1.format(dt);

        reminderInfos= (ArrayList<ReminderInfo>) mReminderdbDao.getSortedReminders(
                ReminderdbDao.SortFlag_MONTH,date.substring(0,7));

        /*//如果查不出info则不显示时间轴
        if (reminderInfos.size()==0){
            iv_schedule_timeLine.setVisibility(View.INVISIBLE);
            TextView textView = new TextView(mActivity);
            textView.setText("您还没有添加任何日程安排！");
            textView.setTextSize(20);
            ll_schedule.removeAllViews();
            ll_schedule.addView(textView);
            return;
        }

        ReminderListViewUtils reminderListViewUtils = new ReminderListViewUtils(mActivity);
        View reminderDayListView = reminderListViewUtils.createReminderDayListView(reminderInfos);

        ll_schedule.removeAllViews();
        ll_schedule.addView(reminderDayListView);*/


        //清空数据
        ll_schedule.removeAllViews();

       /* //给listview添加一个header
        View header_data = getReminderDateHeader(DATE_MONTH);
        ll_schedule.addView(header_data);*/

        //判断数据集是否为空，为空则提示“没有内容”，否则把数据装载到listview
        View contentView = judgeReminderInfo(reminderInfos);
        ll_schedule.addView(contentView);


    }
}
