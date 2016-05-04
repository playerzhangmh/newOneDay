package com.april.oneday.fragment;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.april.oneday.R;
import com.april.oneday.activity.MainActivity;
import com.april.oneday.activity.Reminder_Create1;
import com.april.oneday.activity.Routine_Create1;
import com.april.oneday.base.ScheduleBasePage;
import com.april.oneday.bean.ReminderInfo;
import com.april.oneday.dao.ReminderdbDao;
import com.april.oneday.impl.ScheReminderDay;
import com.april.oneday.impl.ScheReminderFilterName;
import com.april.oneday.impl.ScheReminderFilterTime;
import com.april.oneday.impl.ScheReminderMonth;
import com.april.oneday.impl.ScheReminderYear;
import com.april.oneday.impl.ScheRoutineDay;
import com.april.oneday.utils.ScreenUtils;
import com.april.oneday.view.NoScrollViewPager;
import com.makeramen.segmented.SegmentedRadioGroup;

import java.util.ArrayList;


/**
 * Created by wangtongyu on 2016/4/18.
 */
public class ScheduleFragment extends Fragment implements View.OnTouchListener{

    private static final String TAG = "ScheduleFragment";
    MainActivity mActivity;

    //用于公共的titlebar中间的控件的设置
    private SegmentedRadioGroup rg_option;
    private RadioButton rb_schedule;

    private RadioButton rb_timeline;
    private ImageButton btn_titalbar_viewtype;
    private ImageButton btn_titalbar_menu;
    private NoScrollViewPager vp_schedule_routine;
    private NoScrollViewPager vp_schedule_reminder;

    //    用于提示是reminder还是routine页面
    private ImageView iv_titalbar_viewtype_reminder;
    private ImageView iv_titalbar_viewtype_routine;

    boolean isReminder;//默认不显示Reminder
    private ImageButton ib_createSchedule;
    private ArrayList<ScheduleBasePage> scheReminderPages;
    private ReminderAdapter reminderAdapter;
    private ScheReminderDay scheReminderDay;
    private PopupWindow popupWindow;
    private ReminderdbDao reminderdbDao;
    private ScheReminderFilterName scheReminderFilterName;
    private ScheReminderFilterTime scheReminderFilterTime;
    private ScheReminderMonth scheReminderMonth;

    //用于filter的字段，onresume刷新时用
    private String filter_name;
    private String filter_time;
    private RoutineAdapter routineAdapter;
    private ArrayList<ScheduleBasePage> scheRoutinePages;
    private ScheRoutineDay scheRoutineDay;
    private ScheReminderYear scheReminderYear;
    private TextView tv_reminder_indicator;
    private ScheduleBasePage scheduleBasePage;


    public ScheduleFragment() {
        Log.d(TAG,"ScheduleFragment()");
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG,"onCreate");

    }



    //刷新数据
    @Override
    public void onStart() {

        Log.v(TAG,"onStart");
        refreshReminderViewPager();
        refreshRoutineViewPager();
        super.onStart();

    }


    @Override
    public void onResume() {
        Log.v(TAG,"onStart");

        super.onResume();
        /*refreshReminderViewPager();
        refreshRoutineViewPager();*/

    }


    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return 返回view给MainActivity
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mActivity = (MainActivity) getActivity();
        reminderdbDao = new ReminderdbDao(mActivity);

        View view = inflater.inflate(R.layout.fragment_schedule,container,false);

        //用于公共的titlebar中间的控件的设置
        rg_option = (SegmentedRadioGroup) view.findViewById(R.id.rg_option);
        rb_schedule = (RadioButton) view.findViewById(R.id.rb_schedule);
        rb_schedule.setChecked(true);
        rgOptionOnclick();


        rb_timeline = (RadioButton) view.findViewById(R.id.rb_timeline);
        btn_titalbar_viewtype = (ImageButton) view.findViewById(R.id.btn_titalbar_viewtype);
        btn_titalbar_menu = (ImageButton) view.findViewById(R.id.btn_titalbar_menu);
        ib_createSchedule = (ImageButton) view.findViewById(R.id.ib_createSchedule);

        vp_schedule_routine = (NoScrollViewPager) view.findViewById(R.id.vp_schedule_routine);
        vp_schedule_reminder = (NoScrollViewPager) view.findViewById(R.id.vp_schedule_reminder);

        //优化用户体验，当点击右上角切换reminder和routine时，有小图标提示
        iv_titalbar_viewtype_reminder = (ImageView) view.findViewById(R.id.iv_titalbar_viewtype_reminder);
        iv_titalbar_viewtype_routine = (ImageView) view.findViewById(R.id.iv_titalbar_viewtype_routine);
        tv_reminder_indicator = (TextView) view.findViewById(R.id.tv_reminder_indicator);

        refreshReminderViewPager();
        refreshRoutineViewPager();


        menuOnclick();
        btnChangeModeOnclick();
        imgbtnAddOnclick();
        return view;
    }




    /**用于公共的titlebar中间的控件的设置
     * radiogroup点击监听
     */
    private void rgOptionOnclick() {
        rg_option.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup paramRadioGroup, int paramInt) {
                Log.e(TAG,"rgOptionOnclick");
                mActivity.showTimeline();
//                mActivity.showSchedule();
            }
        });
    }


    /**用于公共的titlebar中间的控件的设置
     * fragment隐藏显示状态监听
     */
    public void onHiddenChanged(boolean hidden)
    {

        if (this.rb_schedule != null){
            rb_schedule.toggle();
        }

    }

    /**
     * 根据isReminder新建不同的schedule
     */
    private void imgbtnAddOnclick() {
        ib_createSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击新建跳转到新建页面
                if (isReminder){
                    //点击添加Reminder
                    Log.v(TAG,"onClick,点击添加Reminder");
                    Intent intent = new Intent(mActivity,Reminder_Create1.class);
                    startActivity(intent);
                }else{
                    //点击添加Routine
                    Log.v(TAG,"onClick,点击添加Routine");
                    Intent intent = new Intent(mActivity,Routine_Create1.class);
                    startActivity(intent);
                }
            }
        });
    }


    /**
     * 给vp_schedule_reminder设置adapter和数据
     */
    private void refreshReminderViewPager() {

        if(reminderAdapter==null){
            Log.v(TAG,"refreshReminderViewPager");
            scheReminderPages = new ArrayList<>();

            scheReminderDay = new ScheReminderDay(mActivity);
            scheReminderMonth = new ScheReminderMonth(mActivity);
            scheReminderFilterName = new ScheReminderFilterName(mActivity);
            scheReminderFilterTime = new ScheReminderFilterTime(mActivity);
            scheReminderYear = new ScheReminderYear(mActivity);


            scheReminderPages.add(scheReminderDay);
            scheReminderPages.add(scheReminderMonth);
            scheReminderPages.add(scheReminderFilterName);
            scheReminderPages.add(scheReminderFilterTime);
            scheReminderPages.add(scheReminderYear);

            reminderAdapter = new ReminderAdapter();
            vp_schedule_reminder.setAdapter(reminderAdapter);
            vp_schedule_reminder.setOnPageChangeListener(onPageChangeListener);
            scheduleBasePage=scheReminderDay;

        }else {
            Log.v(TAG,"refreshReminderViewPager,notifyDataSetChanged");
            scheReminderDay.initData();//刷新Reminder 的page0
            scheReminderMonth.initData();//刷新Reminder 的page1
            scheReminderYear.initData();//刷新Reminder 的page4

            if (filter_name!=null){
                refreshFilterName();//刷新Reminder 的page2
            }
            if (filter_time!=null){
                refreshFilterTime();//刷新page3的数据
            }
            reminderAdapter.notifyDataSetChanged();

        }

    }

    ViewPager.OnPageChangeListener onPageChangeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            scheduleBasePage = scheReminderPages.get(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    /**
     * 给vp_schedule_routine设置adapter和数据
     */
    private void refreshRoutineViewPager() {
        if(routineAdapter==null){
            Log.v(TAG,"refreshRoutineViewPager");
            //获取page
            scheRoutinePages = new ArrayList<>();
            scheRoutineDay = new ScheRoutineDay(mActivity);
            scheRoutinePages.add(scheRoutineDay);
            //设置adapter
            routineAdapter = new RoutineAdapter();
            vp_schedule_routine.setAdapter(routineAdapter);
            vp_schedule_routine.setCurrentItem(0,false);
        }else {
            Log.v(TAG,"refreshRoutineViewPager,notifyDataSetChanged");
            scheRoutineDay.initData();//刷新 Routine 的page0

            routineAdapter.notifyDataSetChanged();

        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.v("Ontouch","event"+popupWindow);
        if(scheduleBasePage!=null){
            if(scheduleBasePage.hidePopWindow()){
                return true;
            }
        }
        if(scheRoutineDay!=null){
            if(scheRoutineDay.hidePopWindow()){
                return true;
            }
        }
        if(popupWindow!=null){
            Log.v("Ontouch","eventssss");
            popupWindow.dismiss();
            popupWindow=null;
            return true;
        }
        return false;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // 拦截触摸事件，防止泄露下去
        view.setOnTouchListener(this);
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


    class RoutineAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return scheRoutinePages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = scheRoutinePages.get(position).mRootView;
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 点击在Routine和Reminder的页面切换
     */
    private void btnChangeModeOnclick() {
        //给控件设置动画
        final RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(100);


        btn_titalbar_viewtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_titalbar_viewtype.startAnimation(rotateAnimation);
                //切换Routine和Reminder的页面
                if (isReminder){
                    Log.v(TAG,"onClick,点击切换到Routine");
                    vp_schedule_routine.setVisibility(View.VISIBLE);
                    vp_schedule_reminder.setVisibility(View.GONE);
                    btn_titalbar_menu.setVisibility(View.INVISIBLE);
                    iv_titalbar_viewtype_reminder.setVisibility(View.INVISIBLE);
                    iv_titalbar_viewtype_routine.setVisibility(View.VISIBLE);
                    tv_reminder_indicator.setVisibility(View.INVISIBLE);
                    isReminder = false;

                }else{
                    Log.v(TAG,"onClick,点击切换到Reminder");
                    vp_schedule_reminder.setVisibility(View.VISIBLE);
                    vp_schedule_routine.setVisibility(View.GONE);
                    btn_titalbar_menu.setVisibility(View.VISIBLE);
                    iv_titalbar_viewtype_reminder.setVisibility(View.VISIBLE);
                    iv_titalbar_viewtype_routine.setVisibility(View.INVISIBLE);
                    tv_reminder_indicator.setVisibility(View.VISIBLE);
                    isReminder = true;

                }
            }
        });
    }

    /**
     * 点击左侧的menu键
     */
    private void menuOnclick() {
        btn_titalbar_menu.setVisibility(View.INVISIBLE);//routine没有该按钮
        btn_titalbar_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.v(TAG,"点击左侧的menu键");


                //弹出一个popupWindow
                View v = View.inflate(mActivity, R.layout.popup_content_menu, null);
                final TextView bt_popup_day = (TextView) v.findViewById(R.id.bt_popup_day);
                final TextView bt_popup_month = (TextView) v.findViewById(R.id.bt_popup_month);
                TextView bt_popup_year = (TextView) v.findViewById(R.id.bt_popup_year);
                TextView bt_popup_filter_name = (TextView) v.findViewById(R.id.bt_popup_filter_name);
                TextView bt_popup_filter_time = (TextView) v.findViewById(R.id.bt_popup_filter_time);
                TextView bt_popup_cancel = (TextView) v.findViewById(R.id.bt_popup_cancel);



                bt_popup_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (popupWindow!=null){
                            popupWindow.dismiss();
                            popupWindow=null;
                        }
                    }
                });
                //默认按天显示,禁用
                switch (vp_schedule_reminder.getCurrentItem()){
                    case 0:
//                        btn_titalbar_menu.setText();//TODO 给menu切换图片
                        bt_popup_day.setEnabled(false);
                        break;
                    case 1:
                        bt_popup_month.setEnabled(false);
                        break;
                    case 4:
                        bt_popup_year.setEnabled(false);
                        break;
                }


                //按天显示
                bt_popup_day.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.v(TAG,"bt_popup_day");
                        tv_reminder_indicator.setText("天");
                        vp_schedule_reminder.setCurrentItem(0,false);
                        //刷新数据
                        scheReminderDay.initData();
                        popupWindow.dismiss();
                        popupWindow=null;

                    }
                });
                bt_popup_month.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tv_reminder_indicator.setText("月");
                        Log.v(TAG,"bt_popup_month");
                        vp_schedule_reminder.setCurrentItem(1,false);
                        //刷新数据
                        scheReminderMonth.initData();
                        popupWindow.dismiss();
                        popupWindow=null;

                    }
                });

                bt_popup_year.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.v(TAG,"bt_popup_year");
                        vp_schedule_reminder.setCurrentItem(4,false);
                        tv_reminder_indicator.setText("年");
                        //刷新数据
                        scheReminderYear.initData();
                        popupWindow.dismiss();
                        popupWindow=null;
                    }
                });

                //按名称查找
                bt_popup_filter_name.setOnClickListener(new View.OnClickListener() {
                    EditText editText = new EditText(mActivity);
                    @Override
                    public void onClick(View view) {
                        Log.v(TAG,"bt_popup_filter_name");
                        tv_reminder_indicator.setText("查");
                        popupWindow.dismiss();
                        popupWindow=null;

                        editText.setHint("输入要查找标题");
                        new AlertDialog.Builder(mActivity)
                                .setView(editText)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        filter_name = editText.getText().toString();
                                        Log.e(TAG,"filter_name"+ filter_name);

                                        //获取filterName的数据
                                        refreshFilterName();

                                        vp_schedule_reminder.setCurrentItem(2,false);

                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                    }
                });
                bt_popup_filter_time.setOnClickListener(new View.OnClickListener() {
                    EditText editText = new EditText(mActivity);
                    @Override
                    public void onClick(View view) {
                        Log.v(TAG,"bt_popup_filter_time");
                        tv_reminder_indicator.setText("查");

                        popupWindow.dismiss();
                        popupWindow=null;

                        editText.setHint("输入要查找日期,格式");
                        new AlertDialog.Builder(mActivity)
                                .setView(editText)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {



                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        filter_time = editText.getText().toString();
                                        Log.v(TAG,"filter_time"+ filter_time);

                                        //刷新page3的数据
                                        refreshFilterTime();

                                        vp_schedule_reminder.setCurrentItem(3,false);

                                        dialog.dismiss();
                                    }


                                })
                                .setNegativeButton("取消", null)
                                .show();

                    }
                });

                //第一次点击的时候popupWindow显示，第二次点击消失
                if (popupWindow == null) {

                    popupWindow = new PopupWindow(v, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                    int[] location = new int[2];
                    view.getLocationOnScreen(location);
                    //根据父控件的位置,和屏幕高度，得出popup显示的Y方向的位置
                    int properY = ScreenUtils.getProperY(mActivity,location[1],view.getHeight(),v);
                    popupWindow.showAtLocation(view, Gravity.TOP | Gravity.LEFT, location[0], properY);
                } else {
                    popupWindow.dismiss();
                    popupWindow=null;
                }

            }
        });
    }

    private void refreshFilterName() {
        //把数据给page页面设置过去
        ArrayList<ReminderInfo> reminderItemByName =
                (ArrayList<ReminderInfo>) reminderdbDao.getReminderItemByName(filter_name);
        scheReminderFilterName.setReminderInfos(reminderItemByName);

        Log.v(TAG,"reminderItemByName"+reminderItemByName.size());

        scheReminderFilterName.initData();
    }

    private void refreshFilterTime() {
        //把数据给page页面设置过去
        ArrayList<ReminderInfo> reminderItemByDate =
                (ArrayList<ReminderInfo>) reminderdbDao.getReminderItemByDate(filter_time);
        scheReminderFilterTime.setReminderInfos(reminderItemByDate);
        scheReminderFilterTime.initData();
        Log.v(TAG,"reminderItemByDate"+reminderItemByDate.size());
    }

}
