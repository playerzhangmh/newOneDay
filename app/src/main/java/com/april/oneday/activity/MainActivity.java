package com.april.oneday.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Environment;

import com.april.oneday.R;
import com.april.oneday.fragment.ScheduleFragment;
import com.april.oneday.fragment.TimeLineFragment;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import java.io.File;

public class MainActivity extends SlidingFragmentActivity {


    private TimeLineFragment timeLineFragment;
    private ScheduleFragment scheduleFragment;
    private FragmentManager fm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化目录
        initDir();

        fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        timeLineFragment = new TimeLineFragment();
        scheduleFragment = new ScheduleFragment();

        transaction.add(R.id.fl_content, timeLineFragment);
        transaction.add(R.id.fl_content, scheduleFragment,"scheduleFragment");
        transaction.hide(scheduleFragment);
        transaction.commit();
    }



    /**
     * 显示时timeLineFragment,隐藏scheduleFragment
     */
    public void showTimeline(){
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.hide(scheduleFragment);
        transaction.show(timeLineFragment);
        transaction.commit();
    }
    /**
     * 获得TimeLineFragment对象
     */
    public TimeLineFragment getTimeLineFragment() {
        FragmentManager fm = getFragmentManager();
        Fragment timeLineFragment = fm.findFragmentByTag("TimeLineFragment");
        return (TimeLineFragment) timeLineFragment;
    }


    /**
     * 隐藏scheduleFragment,隐藏timeLineFragment
     */
    public void showSchedule(){
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.hide(timeLineFragment);
        transaction.show(scheduleFragment);
        transaction.commit();
    }

    /**
     * 初始化文件目录,应该放在splashactivity中,只在用户第一次打开是运行
     */

    public void initDir(){
        File dir = new File(Environment.getExternalStorageDirectory(),"oneday");
        if (!dir.exists()){
            dir.mkdirs();
            File imgDir = new File(dir,"pic");
            File audioDir = new File(dir,"audio");
            File videoDir = new File(dir,"video");
            imgDir.mkdir();
            audioDir.mkdir();
            videoDir.mkdir();
        }
    }

    /**
     * add by jk
     * @return ScheduleFragment
     */
    public ScheduleFragment getScheduleFragment(){
        ScheduleFragment scheduleFragment = (ScheduleFragment) getFragmentManager().
                findFragmentByTag("scheduleFragment");
        return scheduleFragment;
    }

}
