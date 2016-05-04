package com.april.oneday.impl;

import android.util.Log;
import android.view.View;

import com.april.oneday.activity.MainActivity;
import com.april.oneday.base.ScheduleBasePage;
import com.april.oneday.bean.ReminderInfo;
import com.april.oneday.dao.ReminderdbDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wancc on 2016/4/20.
 */
public class ScheReminderDay extends ScheduleBasePage {

    private static final String TAG = "ScheReminderDay";
    private List<ReminderInfo> daylyActiveReminders;
    private List<ReminderInfo> inactiveReminders;

    protected ArrayList<ReminderInfo> reminderInfos;
//    protected ReminderDayAdapter reminderDayAdapter;
//    protected PopupWindow popupWindow;

    public ScheReminderDay(MainActivity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {

        if (reminderInfos==null){
            reminderInfos = new ArrayList<>();
        }
        else{//如果是用于刷新页面，则要把数据集清空，再重新获取
            Log.v(TAG,"reminderInfo!=null");
            reminderInfos.clear();
            daylyActiveReminders.clear();
            inactiveReminders.clear();
        }

        daylyActiveReminders = mReminderdbDao.getInactiveReminders(ReminderdbDao.DAILYACTIVE_FLAG);
        inactiveReminders = mReminderdbDao.getInactiveReminders(ReminderdbDao.INACTIVE_FLAG);

        reminderInfos.addAll(daylyActiveReminders);
        reminderInfos.addAll(inactiveReminders);

/*        Log.v(TAG,"daylyActiveReminders"+daylyActiveReminders.toString());
        Log.v(TAG,"inactiveReminders"+inactiveReminders.toString());
        Log.v(TAG,"reminderInfos"+reminderInfos.toString());*/


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

        /*//给listview添加一个header
        View header_data = getReminderDateHeader(DATE_DAY);
        ll_schedule.addView(header_data);*/

        //判断数据集是否为空，为空则提示“没有内容”，否则把数据装载到listview
        View contentView = judgeReminderInfo(reminderInfos);
        ll_schedule.addView(contentView);


    }

}
