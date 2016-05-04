package com.april.oneday.base;

import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.april.oneday.R;
import com.april.oneday.activity.MainActivity;
import com.april.oneday.bean.ReminderInfo;
import com.april.oneday.dao.ReminderdbDao;
import com.april.oneday.dao.RoutinesDao;
import com.april.oneday.utils.ReminderListViewUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by wancc on 2016/4/20.
 */
public abstract class ScheduleBasePage {
    public static final int DATE_DAY = 1;
    public static final int DATE_MONTH = 2;
    public static final int DATE_YEAR = 3;

    protected MainActivity mActivity;

    public View mRootView;
    protected RelativeLayout empty_home;
    protected LinearLayout ll_schedule;
    protected ReminderdbDao mReminderdbDao;
    protected RoutinesDao mRoutinesDao;
    protected ImageView iv_schedule_timeLine;
    private ReminderListViewUtils reminderListViewUtils;

    public ScheduleBasePage(MainActivity mActivity) {
        this.mActivity = mActivity;
        mReminderdbDao = new ReminderdbDao(mActivity);
        mRoutinesDao = new RoutinesDao(mActivity);

        mRootView=initView();
        initData();
    }

    public abstract void initData() ;

    protected View initView(){
        View view = View.inflate(mActivity, R.layout.schedule_page, null);
        iv_schedule_timeLine = (ImageView) view.findViewById(R.id.iv_schedule_timeLine);
        empty_home = (RelativeLayout) view.findViewById(R.id.empty_home);
        ll_schedule = (LinearLayout) view.findViewById(R.id.ll_schedule);

        return view;
    }

    /**
     * 根据查询的数据（按年？月？日？），显示不同的header
     * @param dateFlag
     * @return
     */
    @NonNull
    public View getReminderDateHeader(int dateFlag) {
        Date today = new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String formatData = format.format(today);

        View header_data = View.inflate(mActivity, R.layout.header_reminder_textviw, null);
        TextView tv_reminder_header = (TextView) header_data.findViewById(R.id.tv_reminder_header);

        switch (dateFlag){
            case DATE_DAY:
                tv_reminder_header.setText(formatData);
                break;
            case DATE_MONTH:
                tv_reminder_header.setText(formatData.substring(0,7));
                break;
            case DATE_YEAR:
                tv_reminder_header.setText(formatData.substring(0,4)+"年");
                break;
        }
        return header_data;
    }


    public View judgeReminderInfo(ArrayList<ReminderInfo> reminderInfos) {

        //如果查不出info则不显示时间轴
        if (reminderInfos.size()==0){
            iv_schedule_timeLine.setVisibility(View.INVISIBLE);
            empty_home.setVisibility(View.VISIBLE);
            TextView textView = new TextView(mActivity);
            textView.setText("您还没有添加任何日程安排！");
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextSize(12);

            return textView;

        }else{
            iv_schedule_timeLine.setVisibility(View.VISIBLE);
            empty_home.setVisibility(View.INVISIBLE);
            reminderListViewUtils = new ReminderListViewUtils(mActivity);
            View reminderDayListView = reminderListViewUtils.createReminderDayListView(reminderInfos);

            return reminderDayListView;

        }



    }
    /**
     * 隐藏气泡
     */


    public boolean hidePopWindow() {
        if(reminderListViewUtils!=null){
            return reminderListViewUtils.hidePopWindow();
        }
        return false;
    }

}
