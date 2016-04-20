package com.april.oneday.base;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.april.oneday.R;
import com.april.oneday.activity.MainActivity;
import com.april.oneday.dao.ReminderdbDao;

/**
 * Created by wancc on 2016/4/20.
 */
public abstract class ScheduleBasePage {
    protected MainActivity mActivity;

    public View mRootView;
    protected ImageView iv_schedule_timeLine;
    protected ListView lv_schedule;
    protected ReminderdbDao mReminderdbDao;

    public ScheduleBasePage(MainActivity mActivity) {
        this.mActivity = mActivity;
        mReminderdbDao = new ReminderdbDao(mActivity);

        mRootView=initView();
        initData();
    }

    protected abstract void initData() ;

    protected View initView(){
        View view = View.inflate(mActivity, R.layout.schedule_page, null);
        iv_schedule_timeLine = (ImageView) view.findViewById(R.id.iv_schedule_timeLine);
        lv_schedule = (ListView) view.findViewById(R.id.lv_schedule);

        return view;
    }




}
