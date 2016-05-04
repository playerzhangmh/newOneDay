package com.april.oneday.impl;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.april.oneday.activity.MainActivity;
import com.april.oneday.base.ScheduleBasePage;
import com.april.oneday.bean.RoutinesInfo;
import com.april.oneday.utils.RoutineListViewUtils;

import java.util.ArrayList;

/**
 * Created by wancc on 2016/4/22.
 */
public class ScheRoutineDay extends ScheduleBasePage {

    private static final String TAG = "ScheRoutineDay";

    protected ArrayList<RoutinesInfo> routinesInfos;
    private RoutineListViewUtils routineListViewUtils;

    public ScheRoutineDay(MainActivity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {

        /*for(int i=0;i<5;i++){
            mRoutinesDao.insertSchedule("title" + i,i+2);
        }*/

        //从数据库获取数据集
        routinesInfos= (ArrayList<RoutinesInfo>) mRoutinesDao.getallSchedule();
        Log.v(TAG,"routinesInfos.size()"+routinesInfos.size());


        //如果查不出info则不显示时间轴
        if (routinesInfos.size()==0){
            iv_schedule_timeLine.setVisibility(View.INVISIBLE);
            empty_home.setVisibility(View.VISIBLE);
            TextView textView = new TextView(mActivity);
            textView.setText("您还没有添加任何日程安排！");
            textView.setTextSize(12);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            ll_schedule.removeAllViews();
            ll_schedule.addView(textView);
            return;
        }else{
            iv_schedule_timeLine.setVisibility(View.VISIBLE);
            empty_home.setVisibility(View.INVISIBLE);
            routineListViewUtils = new RoutineListViewUtils(mActivity);
            View routineDayListView = routineListViewUtils.createRoutineDayListView(routinesInfos);

            ll_schedule.removeAllViews();
            ll_schedule.addView(routineDayListView);
        }
    }
    public boolean hidePopWindow(){
        if(routineListViewUtils!=null){
            return routineListViewUtils.hidePopWindow();
        }
        return false;
    }
}
