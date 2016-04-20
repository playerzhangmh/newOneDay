package com.april.oneday.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.april.oneday.R;
import com.april.oneday.activity.MainActivity;
import com.april.oneday.activity.Reminder_Create1;
import com.april.oneday.base.ScheduleBasePage;
import com.april.oneday.impl.ScheReminderDay;

import java.util.ArrayList;


/**
 * Created by wangtongyu on 2016/4/18.
 */
public class ScheduleFragment extends Fragment {

    private static final String TAG = "ScheduleFragment";
    MainActivity mActivity;
    private Button btn_timeline;
    private Button btn_change_schedule_mode;
    private Button menu;
    private ViewPager vp_schedule_routine;
    private ViewPager vp_schedule_reminder;

    boolean isReminder;//默认不显示Reminder
    private ImageButton ib_createSchedule;
    private ArrayList<ScheduleBasePage> scheReminderPages;

    public ScheduleFragment() {
        Log.d(TAG,"ScheduleFragment()");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.d(TAG,"onCreateView");
        mActivity = (MainActivity) getActivity();

        View view = inflater.inflate(R.layout.fragment_schedule,container,false);

        btn_timeline = (Button) view.findViewById(R.id.btn_timeline);
        btn_change_schedule_mode = (Button) view.findViewById(R.id.btn_change_schedule_mode);
        menu = (Button) view.findViewById(R.id.menu);
        ib_createSchedule = (ImageButton) view.findViewById(R.id.ib_createSchedule);


        vp_schedule_routine = (ViewPager) view.findViewById(R.id.vp_schedule_routine);
        vp_schedule_reminder = (ViewPager) view.findViewById(R.id.vp_schedule_reminder);
        initViewPager();




        btnTimeLineOnclick();
        menuOnclick();
        btnChangeModeOnclick();
        imgbtnAddOnclick();
        return view;
    }

    private void imgbtnAddOnclick() {
        ib_createSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击新建跳转到新建页面
                if (isReminder){
                    //点击添加Reminder
                    Log.d(TAG,"onClick,点击添加Reminder");
                    Intent intent = new Intent(mActivity,Reminder_Create1.class);
                    startActivity(intent);
                }else{
                    //点击添加Routine
                    Log.d(TAG,"onClick,点击添加Routine");
//                    Intent intent = new Intent(mActivity,Routine_Create1.class);
//                    startActivity(intent);
                }
            }
        });
    }

    private void initViewPager() {
        scheReminderPages = new ArrayList<>();

        scheReminderPages.add(new ScheReminderDay(mActivity));
//        scheReminderPages.add(new ScheReminderMonth(mActivity));

        vp_schedule_reminder.setAdapter(new ReminderAdapter());

    }


    class ReminderAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return scheReminderPages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = scheReminderPages.get(position).mRootView;
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void btnChangeModeOnclick() {
        btn_change_schedule_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //切换Routine和Reminder的页面
                if (isReminder){
                    Log.d(TAG,"onClick,点击切换到Routine");
                    vp_schedule_routine.setVisibility(View.VISIBLE);
                    vp_schedule_reminder.setVisibility(View.GONE);
                    isReminder = false;

                }else{

                    Log.d(TAG,"onClick,点击切换到Reminder");
                    vp_schedule_reminder.setVisibility(View.VISIBLE);
                    vp_schedule_routine.setVisibility(View.GONE);
                    isReminder = true;

                }
            }
        });
    }

    private void menuOnclick() {
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示侧边栏
            }
        });
    }

    private void btnTimeLineOnclick(){
        btn_timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //让mActivity切换布局
                mActivity.showTimeline();
            }
        });
    }
}
