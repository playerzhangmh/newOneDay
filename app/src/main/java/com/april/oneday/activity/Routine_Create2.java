package com.april.oneday.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.april.oneday.R;
import com.april.oneday.bean.RoutinesDetailInfo;
import com.april.oneday.bean.RoutinesInfo;
import com.april.oneday.dao.RoutinesDao;
import com.april.oneday.utils.ReminderUtis;
import com.april.oneday.utils.WindowMeasureUtils;
import com.viewpagerindicator.TabPageIndicator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Routine_Create2 extends Activity {

    private static final int CREATEDATE = 2000;
    private static final String TAG = "Routine_Create2";
    private ViewPager vp_routine2_week;
    RoutinesInfo routinesInfo;
    RoutinesDao dao;
    private int currentactiveposition;
    private Routine2Pager currentactivePage;
    private MyViewPagerAdapter myViewPagerAdapter;
    public int currentposition;
    private PopupWindow popupWindow;
    private String choice;
    private RoutinesDetailInfo routinesDetailInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //隐藏标题栏,此处有坑
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_routine__create2);

    }

    @Override
    protected void onResume() {

        super.onResume();
        //获取前个页面的数据
        initData();
        TextView tv_routine2_title= (TextView) findViewById(R.id.tv_routine2_title);
        Intent intent = getIntent();
        routinesDetailInfo = (RoutinesDetailInfo) intent.getSerializableExtra("RoutinesDetailInfo");//service跳过来
        if(routinesDetailInfo!=null){
            List<RoutinesInfo> routinesInfos = dao.getallSchedule();
            for(RoutinesInfo info: routinesInfos){
                if(info.getSchedule_id()==routinesDetailInfo.getS_id()){
                    routinesInfo=info;
                    break;
                }
            }
        }
        if(routinesInfo==null){
            routinesInfo = (RoutinesInfo) intent.getSerializableExtra("RoutinesInfo");
        }
        if(routinesInfo!=null){
            Log.v(TAG,routinesInfo.toString());
        }
        tv_routine2_title.setText(routinesInfo.getSchedule_title());

        //初始化数据
        final ViewPager vp_routine2_week=(ViewPager)findViewById(R.id.vp_routine2_week);
        myViewPagerAdapter = new MyViewPagerAdapter();
        vp_routine2_week.setAdapter(myViewPagerAdapter);

        //indicator指示器和viewpager绑定
        TabPageIndicator indicator= (TabPageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(vp_routine2_week);

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                currentposition = position;
                refreshnotify();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //进入后在set完viewpager后直接刷到active页面
        currentactiveposition = routinesInfo.getCurrentDay() - 1;
        vp_routine2_week.setCurrentItem(currentactiveposition,false);
        super.onStart();
    }

    /**
     * 初始化list
     */
    private void initData() {

        dao = new RoutinesDao(this);
    }


    class MyViewPagerAdapter extends PagerAdapter{

        @Override
        public CharSequence getPageTitle(int position) {

            return "DAY "+(position+1);
        }

        @Override
        public int getCount() {
            //返回计划的天数,有几天ViewPager初始化几页
            return routinesInfo.getSchedule_cycles();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {



            //初始化该页面的listView的数据
            List<RoutinesDetailInfo> routinesDetailInfos =
                    dao.getallDetailsByday(position+1, routinesInfo.getSchedule_id());

            Routine2Pager routine2Pager = new Routine2Pager(Routine_Create2.this,routinesDetailInfos);
            currentactivePage = routine2Pager;
            container.addView(routine2Pager.mRootView);
            return routine2Pager.mRootView;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);

        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }


    public void routine2_back(View v){

        //首页应该在restart方法中刷新一下
        finish();
    }


    public void routine2_add(View v){


        Intent intent = new Intent(this,Routine_Create3.class);
        intent.putExtra("s_id",routinesInfo.getSchedule_id());
        intent.putExtra("p_id",currentposition+1);
        hidePopWindow();
        Log.v(TAG,routinesInfo.toString());
        startActivityForResult(intent,CREATEDATE);

    }

    /**
     * popwindow实现menu菜单
     */
    public void routine2_nemu(View view){

        View menuPopwindow = View.inflate(this,R.layout.popwindow_menu_routine2,null);
        popupWindow = new PopupWindow(menuPopwindow, (int) WindowMeasureUtils.px2dp(100,this), (int) WindowMeasureUtils.px2dp(160,this));

        //获得view离父控件的高度
        int[] ints = new int[2];
        view.getLocationInWindow(ints);
        System.out.println("menu菜单的位置:"+ints[0]+","+ints[1]);
        //显示popwindow菜单
        popupWindow.showAtLocation(view, Gravity.RIGHT | Gravity.TOP,0,ints[1]);
        //popwindow菜单点击事件
        TextView tvCloneTo = (TextView) menuPopwindow.findViewById(R.id.tv_routine2popmenu_cloneto);
        TextView tvDeleteDay = (TextView) menuPopwindow.findViewById(R.id.tv_routine2popmenu_deleteday);
        TextView tvCancle = (TextView) menuPopwindow.findViewById(R.id.tv_routine2popmenu_cancle);



        //Clone Day to ...克隆今日安排至另一天,弹Dialog对画框
        tvCloneTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                final String[] clonedaylist = dao.getClonedaylist(routinesInfo.getSchedule_id(), currentposition + 1);
                choice=clonedaylist[0];
                AlertDialog.Builder builder = new AlertDialog.Builder(Routine_Create2.this);

                builder.setTitle("将整天安排克隆至..");

                builder.setSingleChoiceItems(clonedaylist , 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //执行克隆操作
                        choice = clonedaylist[i];
                        Toast.makeText(Routine_Create2.this,"你选择了"+choice,Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //数据库克隆操作
                        int targetDay = Integer.parseInt(choice.substring(choice.indexOf("day")+3));
                        System.out.println("targetDay="+targetDay);
                        dao.cloneallDetailstoOther(routinesInfo.getSchedule_id(),currentposition+1,targetDay);
                        refreshnotify();
                        hidePopWindow();
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
            }
        });
        //删除今日日程安排
        tvDeleteDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dao.deleteprocessItem(routinesInfo.getSchedule_id(),currentposition+1);
                refreshnotify();
                hidePopWindow();
            }
        });
        //取消,隐藏popwindow
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               hidePopWindow();
            }
        });


    }
    /**
     * 隐藏气泡
     */
    public boolean hidePopWindow() {
        if (popupWindow != null) {
            popupWindow.dismiss();//隐藏气泡
            popupWindow = null;
            return true;
        }
        return false;
    }


    /**
     * 重新回到这个Activity的时候刷新页面的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Routine_Create3.REFRESH){
            if(data!=null){
                boolean refresh = data.getBooleanExtra("refresh",false);
                refreshnotify();
            }
        }
    }

    private void refreshnotify() {
        List<RoutinesDetailInfo> routinesDetailInfos = dao.getallDetailsByday(currentposition + 1, routinesInfo.getSchedule_id());
        if(currentactivePage!=null){
            currentactivePage.notifylistview(routinesDetailInfos);
        }
        if(myViewPagerAdapter!=null){
            myViewPagerAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v(TAG,"here2");
        if(hidePopWindow()){
            Log.v(TAG,"here");
            return true;
        }
        return super.onTouchEvent(event);
    }


}
