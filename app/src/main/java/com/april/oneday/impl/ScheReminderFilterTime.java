package com.april.oneday.impl;

import android.view.View;

import com.april.oneday.activity.MainActivity;
import com.april.oneday.base.ScheduleBasePage;
import com.april.oneday.bean.ReminderInfo;

import java.util.ArrayList;

/**
 * Created by wancc on 2016/4/21.
 */
public class ScheReminderFilterTime extends ScheduleBasePage {

    private ArrayList<ReminderInfo> reminderInfos;

    public void setReminderInfos(ArrayList<ReminderInfo> reminderInfos) {
        this.reminderInfos = reminderInfos;
    }

    public ScheReminderFilterTime(MainActivity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {

        //清空数据
        ll_schedule.removeAllViews();

       /* //给listview添加一个header
        View header_data = getReminderDateHeader(DATE_MONTH);
        ll_schedule.addView(header_data);*/

        //判断数据集是否为空，为空则提示“没有内容”，否则把数据装载到listview
        if (reminderInfos!=null){
            View contentView = judgeReminderInfo(reminderInfos);
            ll_schedule.addView(contentView);
        }

        /*if (reminderInfos!=null){
            //如果查不出info则不显示时间轴
            if (reminderInfos.size()==0){
                empty_home.setVisibility(View.INVISIBLE);
                TextView textView = new TextView(mActivity);
                textView.setText("没有对应时间的日程安排！");
                textView.setTextSize(20);
                ll_schedule.removeAllViews();
                ll_schedule.addView(textView);
                return;
            }

            ReminderListViewUtils reminderListViewUtils = new ReminderListViewUtils(mActivity);
            View reminderDayListView = reminderListViewUtils.createReminderDayListView(reminderInfos);

            ll_schedule.removeAllViews();
            ll_schedule.addView(reminderDayListView);
        }*/

    }
}
