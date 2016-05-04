package com.april.oneday.utils;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.april.oneday.R;
import com.april.oneday.activity.MainActivity;
import com.april.oneday.activity.Routine_Create2;
import com.april.oneday.bean.RoutinesInfo;
import com.april.oneday.dao.RoutinesDao;
import com.april.oneday.fragment.ScheduleFragment;
import com.zcw.togglebutton.ToggleButton;

import java.util.ArrayList;

/**
 * Created by wancc on 2016/4/21.
 */
public class RoutineListViewUtils {
    private static final String TAG = "RoutineListViewUtils";
    protected ArrayList<RoutinesInfo> routinesInfos;
    MainActivity mActivity;


    private final RoutinesDao mRoutinesDao;
    PopupWindow popupWindow;
    private  RoutineDayAdapter routineDayAdapter;
    private final ScheduleFragment scheduleFragment;

    /**
     * 构造方法：准备好上下文和数据库接口
     * @param mActivity
     */
    public RoutineListViewUtils(MainActivity mActivity) {

        this.mActivity = mActivity;
        mRoutinesDao = new RoutinesDao(mActivity);
        scheduleFragment = mActivity.getScheduleFragment();

    }


    /**
     *
     * @param routinesInfos
     * @return listview 用于把传入数据集装载到listview
     */
    public View createRoutineDayListView(final ArrayList<RoutinesInfo> routinesInfos) {

        //初始化数据集
        this.routinesInfos = routinesInfos;


        ListView listview_routine_day = (ListView) View.inflate(mActivity, R.layout.listview_routine_day, null);

        if (routineDayAdapter==null){
            routineDayAdapter = new RoutineDayAdapter();
            listview_routine_day.setAdapter(routineDayAdapter);
        }else{
            routineDayAdapter.notifyDataSetChanged();
        }


        /*//给listview添加一个header
        Date today = new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String formatData = format.format(today);

        TextView header_data = new TextView(mActivity);
        header_data.setText(formatData);
        listview_reminder_day.addHeaderView(header_data);*/


        //当listview滚动的时候，让popupwindow消失
        listview_routine_day.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (popupWindow != null) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            }
        });



        //点击进入详情页，把数据传到Routine_Create2
        listview_routine_day.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //把数据传到Reminder_Create2
                Log.v(TAG,"把数据传到Routine_Create2");
                RoutinesInfo routinesInfo = routinesInfos.get(position);
                Intent intent = new Intent(mActivity, Routine_Create2.class);
                intent.putExtra("RoutinesInfo",routinesInfo);
                mActivity.startActivity(intent);
                if (popupWindow != null) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            }
        });


        return listview_routine_day;
    }


    class RoutineDayAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return routinesInfos.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            View v;
            final Holder holder;
            if (view!=null){
                v=view;
                holder = (Holder) v.getTag();
            }else {
                v = View.inflate(mActivity, R.layout.item_routine, null);

                TextView tv_routine_title = (TextView) v.findViewById(R.id.tv_routine_title);
                TextView tv_routine_cycles = (TextView) v.findViewById(R.id.tv_routine_cycles);
                TextView tv_routine_currentday = (TextView) v.findViewById(R.id.tv_routine_currentday);
                TextView tv_routine_isactive = (TextView) v.findViewById(R.id.tv_routine_isactive);

                ToggleButton tb_isActive = (ToggleButton) v.findViewById(R.id.tb_isActive);
                ImageButton ib_menu_more = (ImageButton) v.findViewById(R.id.ib_menu_more);


                holder = new Holder(tv_routine_title,tv_routine_cycles,tv_routine_currentday,tv_routine_isactive,
                        tb_isActive,ib_menu_more) ;

                v.setTag(holder);
            }


            final RoutinesInfo routinesInfo = routinesInfos.get(position);
            final int schedule_cycles = routinesInfo.getSchedule_cycles();

            //把routinesInfo的数据显示到每个item上
            holder.tv_routine_title.setText(routinesInfo.getSchedule_title());
            holder.tv_routine_cycles.setText(schedule_cycles+"days");
            holder.tv_routine_currentday.setText("DAY "+routinesInfo.getCurrentDay());

            if (routinesInfo.isActive()){
                //当为激活时,为开启状态
                holder.tb_isActive.setToggleOn();
                holder.tv_routine_currentday.setVisibility(View.VISIBLE);
                holder.tv_routine_isactive.setVisibility(View.INVISIBLE);
            }else {
                //当为过期时，为关闭状态
                holder.tb_isActive.setToggleOff();
                holder.tv_routine_currentday.setVisibility(View.INVISIBLE);
                holder.tv_routine_isactive.setVisibility(View.VISIBLE);
            }

            //切换Reminder的状态，并修改数据库
            holder.tb_isActive.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
                @Override
                public void onToggle(boolean on) {
                    //修改数据库
                    mRoutinesDao.updateByToggleBt(routinesInfo);
                    //刷新scheduleFragment??直接修改当前item的信息
//                    scheduleFragment.onResume();
//                    scheduleFragment.onStart();
                    if (routinesInfo.isActive()){
                        routinesInfo.setActive(false);
                        holder.tv_routine_currentday.setVisibility(View.INVISIBLE);
                        holder.tv_routine_isactive.setVisibility(View.VISIBLE);
                    }else {
                        routinesInfo.setActive(true);
                        holder.tv_routine_currentday.setVisibility(View.VISIBLE);
                        holder.tv_routine_isactive.setVisibility(View.INVISIBLE);
                    }


                }
            });




            //弹出menu 选项框popupwindow
            holder.ib_menu_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View popview = View.inflate(mActivity, R.layout.popup_content_edit_routine, null);
                    TextView bt_popup_rename = (TextView) popview.findViewById(R.id.bt_popup_rename);
                    TextView bt_popup_clone = (TextView) popview.findViewById(R.id.bt_popup_clone);
                    TextView bt_popup_delete = (TextView) popview.findViewById(R.id.bt_popup_delete);
                    TextView bt_popup_change_cancel = (TextView) popview.findViewById(R.id.bt_popup_change_cancel);
                    TextView bt_popup_change_currentday = (TextView) popview.findViewById(R.id.bt_popup_change_currentday);

                    bt_popup_change_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (popupWindow!=null){
                                popupWindow.dismiss();
                                popupWindow=null;
                            }
                        }
                    });

                    //如果是非激活状态就不显示“修改当前天”
                    if (routinesInfo.isActive()){
                        bt_popup_change_currentday.setVisibility(View.VISIBLE);
                    }else {
                        bt_popup_change_currentday.setVisibility(View.GONE);
                    }

                    //第一次点击的时候popupWindow显示，第二次点击消失
                    if (popupWindow == null) {
                        popupWindow = new PopupWindow(popview, ActionBar.LayoutParams.WRAP_CONTENT,
                                ActionBar.LayoutParams.WRAP_CONTENT);

                        //根据父控件的位置,和屏幕高度，得出popup显示的Y方向的位置
                        int[] location = new int[2];
                        view.getLocationOnScreen(location);

                        int properX = ScreenUtils.getProperX(mActivity,popview);
                        int properY = ScreenUtils.getProperY(mActivity,location[1],view.getHeight(),popview);
                        popupWindow.showAtLocation(view, Gravity.TOP | Gravity.LEFT, properX, properY);

                    } else {
                        popupWindow.dismiss();
                        popupWindow = null;
                    }



                    //重命名routine
                    bt_popup_rename.setOnClickListener(new View.OnClickListener() {
                        EditText editText = new EditText(mActivity);
                        String changedName;
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                            popupWindow=null;

                            editText.setHint("输入新标题");
                            new AlertDialog.Builder(mActivity)
                                    .setView(editText)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            changedName = editText.getText().toString();
                                            Log.v(TAG,"changedName"+ changedName);

                                            if (changedName.isEmpty()){
                                                Toast.makeText(mActivity, "标题不能为空", Toast.LENGTH_SHORT).show();
                                            }else{
                                                //更新数据库上的该数据
                                                mRoutinesDao.updateSchedulename(routinesInfo.getSchedule_id(),changedName);

                                                //刷新scheduleFragment
//                                                scheduleFragment.onResume();
//                                                scheduleFragment.onStart();
                                                holder.tv_routine_title.setText(changedName);


                                                dialog.dismiss();
                                            }
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .show();

                        }
                    });

                    bt_popup_clone.setOnClickListener(new View.OnClickListener() {
                        EditText editText = new EditText(mActivity);
                        String newRoutineName;
                        @Override
                        public void onClick(View view) {
                            editText.setHint("输入副本标题");

                            popupWindow.dismiss();
                            popupWindow=null;

                            new AlertDialog.Builder(mActivity)
                                    .setView(editText)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            newRoutineName = editText.getText().toString();
                                            Log.v(TAG,"changedName"+ newRoutineName);

                                            if (newRoutineName.isEmpty()){
                                                Toast.makeText(mActivity, "标题不能为空", Toast.LENGTH_SHORT).show();
                                            }else{
                                                //更新数据库
                                                mRoutinesDao.cloneSchedule(routinesInfo,newRoutineName);

                                                //刷新scheduleFragment
//                                                scheduleFragment.onResume();
//                                                scheduleFragment.onStart();
                                                routinesInfos= (ArrayList<RoutinesInfo>) mRoutinesDao.getallSchedule();
                                                notifyDataSetChanged();

                                                dialog.dismiss();
                                            }
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .show();
                        }
                    });


                    //删除routine
                    bt_popup_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //删除数据库上的该数据
                            mRoutinesDao.deleteSchedule(routinesInfo.getSchedule_id());
                            //刷新scheduleFragment
//                            scheduleFragment.onResume();
//                            scheduleFragment.onStart();

                            if (routinesInfos.size()==1){
                                scheduleFragment.onStart();
                            }else{
                                routinesInfos.remove(position);
                                notifyDataSetChanged();
                            }
                            popupWindow.dismiss();
                            popupWindow=null;
                        }
                    });


                    //更改当天天数
                    bt_popup_change_currentday.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                            popupWindow=null;
                            //获取routine的天数
                            String[] cyclesItems = new String[schedule_cycles];
                            for (int i=0;i<schedule_cycles;i++){
                                cyclesItems[i]="Day "+(i+1);
                            }
                            new AlertDialog.Builder(mActivity)
                                    .setTitle("选择当前天数")
                                    .setSingleChoiceItems(cyclesItems, routinesInfo.getCurrentDay()-1,
                                            new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            mRoutinesDao.changeCurrentday(routinesInfo,i+1);//要不要+1？？？ TODO
                                        }
                                    })
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            scheduleFragment.onResume();
                                            scheduleFragment.onStart();

                                        }
                                    })
                                    .setNegativeButton("取消",null)
                                    .show();
                        }
                    });

                }
            });



            return v;
        }

        class Holder{
            TextView tv_routine_title ;
            TextView tv_routine_cycles;
            TextView tv_routine_currentday;
            TextView tv_routine_isactive;
            ToggleButton tb_isActive;
            ImageButton ib_menu_more;

            public Holder(TextView tv_routine_title, TextView tv_routine_cycles, TextView tv_routine_currentday,
                          TextView tv_routine_isactive, ToggleButton tb_isActive, ImageButton ib_menu_more) {
                this.tv_routine_title = tv_routine_title;
                this.tv_routine_cycles = tv_routine_cycles;
                this.tv_routine_currentday = tv_routine_currentday;
                this.tv_routine_isactive = tv_routine_isactive;
                this.tb_isActive = tb_isActive;
                this.ib_menu_more = ib_menu_more;
            }
        }

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
}
