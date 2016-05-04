package com.april.oneday.utils;

import android.app.ActionBar;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.april.oneday.R;
import com.april.oneday.activity.MainActivity;
import com.april.oneday.activity.Reminder_Create2;
import com.april.oneday.bean.ReminderInfo;
import com.april.oneday.dao.ReminderdbDao;
import com.april.oneday.fragment.ScheduleFragment;
import com.zcw.togglebutton.ToggleButton;

import java.util.ArrayList;

/**
 * Created by wancc on 2016/4/21.
 */
public class ReminderListViewUtils {
    private static final String TAG = "ReminderListViewUtils";
    protected ArrayList<ReminderInfo> reminderInfos;
    MainActivity mActivity;


    private final ReminderdbDao mReminderdbDao;
    PopupWindow popupWindow;
    private  ReminderDayAdapter reminderDayAdapter;
    private final ScheduleFragment scheduleFragment;
    private ListView listview_reminder_day;

    public ReminderListViewUtils(MainActivity mActivity) {

        this.mActivity = mActivity;
        mReminderdbDao = new ReminderdbDao(mActivity);
        Log.v(TAG,"ReminderListViewUtils 构造方法");
        scheduleFragment = mActivity.getScheduleFragment();//用于当发生修改的时候刷新界面
    }


    public View createReminderDayListView(ArrayList<ReminderInfo> reminderInfos) {

        //初始化数据集
        this.reminderInfos = reminderInfos;


        listview_reminder_day = (ListView) View.inflate(mActivity, R.layout.listview_reminder_day, null);

        if (reminderDayAdapter==null){
            reminderDayAdapter = new ReminderDayAdapter();
            listview_reminder_day.setAdapter(reminderDayAdapter);
        }else{
            reminderDayAdapter.notifyDataSetChanged();
        }

        //当listview滚动的时候，让popupwindow消失
        listview_reminder_day.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.v(TAG, "onScrollStateChanged");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.v(TAG, "onScroll");

                if (popupWindow != null) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            }
        });

        listview_reminder_day.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v(TAG,"onItemClick");
                if (popupWindow != null) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            }
        });

        return listview_reminder_day;
    }


    class ReminderDayAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return reminderInfos.size();
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
            Holder holder;
            if (view!=null){
                v=view;
                holder = (Holder) v.getTag();
            }else {
                v = View.inflate(mActivity, R.layout.item_reminder, null);


//                ImageView iv_schedule_icon = (ImageView) v.findViewById(R.id.iv_schedule_icon);
//                TextView tv_schedule_iconMonth = (TextView) v.findViewById(R.id.tv_schedule_iconMonth);
                TextView tv_reminder_date = (TextView) v.findViewById(R.id.tv_reminder_date);
                TextView tv_reminder_name = (TextView) v.findViewById(R.id.tv_reminder_name);
                TextView tv_reminder_comment = (TextView) v.findViewById(R.id.tv_reminder_comment);
                TextView tv_reminder_time = (TextView) v.findViewById(R.id.tv_reminder_time);
                ToggleButton tb_isActive = (ToggleButton) v.findViewById(R.id.tb_isActive);
                ImageButton ib_menu_more = (ImageButton) v.findViewById(R.id.ib_menu_more);

                holder = new Holder(tv_reminder_date,tv_reminder_name,tv_reminder_comment,tv_reminder_time,tb_isActive,ib_menu_more);
                v.setTag(holder);
            }



            final ReminderInfo reminderInfo = reminderInfos.get(position);

           /* GradientDrawable myGrad = (GradientDrawable)holder.iv_schedule_icon.getBackground();
            int fillColor = Color.parseColor(reminderInfo.getIconBgColor());//内部填充颜色
            myGrad.setColor(fillColor);*/

//            holder.tv_schedule_iconMonth.setText(reminderInfo.getReminder_date().substring(5,7));
            holder.tv_reminder_date.setText(reminderInfo.getReminder_date());
            holder.tv_reminder_name.setText(reminderInfo.getReminder_name());
            holder.tv_reminder_comment.setText(reminderInfo.getReminder_comment());
            holder.tv_reminder_time.setText(reminderInfo.getReminder_time().substring(0,5));

            //切换Reminder的状态，并修改数据库
            holder.tb_isActive.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
                @Override
                public void onToggle(boolean on) {
                    //用户手动切换状态,不管它的repeatType是什么
                    if (reminderInfo.isActive()==0){
                        Log.v(TAG,"手动关闭");
                        //切换后，修改数据库
                        mReminderdbDao.toInactive(reminderInfo.getReminder_id(),0);
                        reminderInfo.setActive(1);

                    }else{
                        Log.v(TAG,"手动开启");
                        mReminderdbDao.toInactive(reminderInfo.getReminder_id(),1);
                        reminderInfo.setActive(0);
                    }

                }
            });

            if (reminderInfo.isActive()==0){
                //当为激活时,为开启状态
                holder.tb_isActive.setToggleOn();
            }else {
                //当为过期时，为关闭状态
                holder.tb_isActive.setToggleOff();
            }


            //弹出delete 和 edit 选项框popupwindow
            holder.ib_menu_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    View popview = View.inflate(mActivity, R.layout.popup_content_edit_reminder, null);
                    TextView bt_popup_delete = (TextView) popview.findViewById(R.id.bt_popup_delete);
                    TextView bt_popup_edit = (TextView) popview.findViewById(R.id.bt_popup_edit);
                    TextView bt_popup_cancel = (TextView) popview.findViewById(R.id.bt_popup_cancel);

                    bt_popup_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (popupWindow!=null){
                                popupWindow.dismiss();
                                popupWindow=null;
                            }
                        }
                    });

                    //第一次点击的时候popupWindow显示，第二次点击消失
                    if (popupWindow == null) {
                        popupWindow = new PopupWindow(popview, ActionBar.LayoutParams.WRAP_CONTENT,
                                ActionBar.LayoutParams.WRAP_CONTENT);

                        //view是ImageButton，popview是用布局文件填充的view
                        int[] location = new int[2];
                        view.getLocationOnScreen(location);

                        //根据父控件的位置,和屏幕高度，得出popup显示的Y方向的位置
                        int properX = ScreenUtils.getProperX(mActivity,popview);
                        int properY = ScreenUtils.getProperY(mActivity,location[1],view.getHeight(),popview);
                        popupWindow.showAtLocation(view, Gravity.TOP | Gravity.LEFT, properX, properY);

                    } else {
                        popupWindow.dismiss();
                        popupWindow = null;
                    }

                    bt_popup_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ReminderInfo selectedInfo = reminderInfos.get(position);
                            //删除数据库上的该数据
                            mReminderdbDao.deleteReminder(selectedInfo.getReminder_id());
                            //刷新scheduleFragment
//                            scheduleFragment.onStart();
                            reminderInfos.remove(position);
                            notifyDataSetChanged();
                            if (reminderInfos.size()==0){
                                scheduleFragment.onStart();
                            }
                            popupWindow.dismiss();
                            popupWindow=null;
                        }
                    });


                    bt_popup_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ReminderInfo editInfo = reminderInfos.get(position);
                            Intent intent = new Intent(mActivity,Reminder_Create2.class);

                            String[] infoString={editInfo.getReminder_name(),editInfo.getReminder_date(),
                                    editInfo.getReminder_time(),editInfo.getReminder_comment(),
                                    editInfo.getRing_path(),editInfo.getIconBgColor()};
                            intent.putExtra("selectedReminderInfo_String",infoString);

                            int[] infoInt={editInfo.getRepeatType(),editInfo.getVabrate_type(),
                                    editInfo.getVabrate_times(),editInfo.getIconRid(),
                                    editInfo.isActive(),editInfo.getReminder_id(),editInfo.isActive()};
                            intent.putExtra("selectedReminderInfo_Int",infoInt);


//                            Log.v(TAG,"是否激活"+editInfo.isActive());
                            //进入编辑页面
                            mActivity.startActivity(intent);
                            popupWindow.dismiss();
                            popupWindow=null;

                        }
                    });

                }
            });
            return v;
        }

        class Holder{
//            ImageView iv_schedule_icon;
//            TextView tv_schedule_iconMonth;
            TextView tv_reminder_date;
            TextView tv_reminder_name ;
            TextView tv_reminder_comment;
            TextView tv_reminder_time ;
            ToggleButton tb_isActive ;
            ImageButton ib_menu_more ;

            public Holder(TextView tv_reminder_date, TextView tv_reminder_name,
                          TextView tv_reminder_comment, TextView tv_reminder_time, ToggleButton tb_isActive,
                          ImageButton ib_menu_more) {

                this.tv_reminder_date = tv_reminder_date;
                this.tv_reminder_name = tv_reminder_name;
                this.tv_reminder_comment = tv_reminder_comment;
                this.tv_reminder_time = tv_reminder_time;
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
