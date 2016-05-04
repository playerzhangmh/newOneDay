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
 * Created by wancc on 2016/4/22.
 */
public class ScheReminderYear extends ScheduleBasePage {

    protected ArrayList<ReminderInfo> reminderInfos;

    public ScheReminderYear(MainActivity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {

        //从数据库中获取数据集
        Date dt = new Date();
        SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-dd");
        String date = matter1.format(dt);
        reminderInfos= (ArrayList<ReminderInfo>) mReminderdbDao.getSortedReminders(
                ReminderdbDao.SortFlag_YEAR,date.substring(0,4));

        //清空数据
        ll_schedule.removeAllViews();

       /* //给listview添加一个header
        View header_data = getReminderDateHeader(DATE_YEAR);
        ll_schedule.addView(header_data);*/

        //判断数据集是否为空，为空则提示“没有内容”，否则把数据装载到listview
        View contentView = judgeReminderInfo(reminderInfos);
        ll_schedule.addView(contentView);

    }

}
