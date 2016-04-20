package com.april.oneday.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.april.oneday.R;
import com.april.oneday.fragment.ScheduleFragment;
import com.april.oneday.fragment.TimeLineFragment;

import java.io.File;

public class MainActivity extends Activity {


    private TimeLineFragment timeLineFragment;
    private ScheduleFragment scheduleFragment;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化目录
        initDir();

        fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        timeLineFragment = new TimeLineFragment();
        scheduleFragment = new ScheduleFragment();

        transaction.add(R.id.fl_content, timeLineFragment);
        transaction.add(R.id.fl_content, scheduleFragment);
        transaction.hide(scheduleFragment);
        transaction.commit();

    }


    /**
     * 按钮点击事件
     */
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_main_timeline://显示时间轴页面
                showTimeline();
                break;
            case R.id.btn_main_schedule://显示行程规划页面
                showSchedule();
                break;
        }
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

}
