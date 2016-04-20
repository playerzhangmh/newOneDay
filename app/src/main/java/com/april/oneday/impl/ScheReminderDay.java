package com.april.oneday.impl;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.april.oneday.R;
import com.april.oneday.activity.MainActivity;
import com.april.oneday.base.ScheduleBasePage;
import com.april.oneday.bean.ReminderInfo;
import com.april.oneday.dao.ReminderdbDao;
import com.zcw.togglebutton.ToggleButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wancc on 2016/4/20.
 */
public class ScheReminderDay extends ScheduleBasePage {

    private List<ReminderInfo> daylyActiveReminders;
    private List<ReminderInfo> inactiveReminders;
    private int activityReminderSize;
    private ArrayList<ReminderInfo> reminderInfos;

    public ScheReminderDay(MainActivity mActivity) {
        super(mActivity);
    }

    @Override
    protected void initData() {

        reminderInfos = new ArrayList<>();

        daylyActiveReminders = mReminderdbDao.getInactiveReminders(ReminderdbDao.DAILYACTIVE_FLAG);
        inactiveReminders = mReminderdbDao.getInactiveReminders(ReminderdbDao.INACTIVE_FLAG);

        reminderInfos.addAll(daylyActiveReminders);
        reminderInfos.addAll(inactiveReminders);

        activityReminderSize = daylyActiveReminders.size();

        lv_schedule.setAdapter(new ReminderDayAdapter());


    }

    class ReminderDayAdapter extends BaseAdapter{
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
        public View getView(int position, View view, ViewGroup viewGroup) {
            View v;
            Holder holder;
            if (view!=null){
                v=view;
                holder = (Holder) v.getTag();
            }else {
                v = View.inflate(mActivity, R.layout.item_reminder, null);

                TextView tv_reminder_name = (TextView) v.findViewById(R.id.tv_reminder_name);
                TextView tv_reminder_comment = (TextView) v.findViewById(R.id.tv_reminder_comment);
                TextView tv_reminder_time = (TextView) v.findViewById(R.id.tv_reminder_time);
                ToggleButton tb_isActive = (ToggleButton) v.findViewById(R.id.tb_isActive);
                ImageButton ib_menu_more = (ImageButton) v.findViewById(R.id.ib_menu_more);

                holder = new Holder(tv_reminder_name,tv_reminder_comment,tv_reminder_time,tb_isActive,ib_menu_more);
            }
            final ReminderInfo reminderInfo = reminderInfos.get(position);
            holder.tv_reminder_name.setText(reminderInfo.getReminder_name());
            holder.tv_reminder_comment.setText(reminderInfo.getReminder_comment());
            holder.tv_reminder_time.setText(reminderInfo.getReminder_time().substring(0,5));

            holder.tb_isActive.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
                @Override
                public void onToggle(boolean on) {
                    //用户手动切换状态,不管它的repeatType是什么
                    if (reminderInfo.isActive()==0){
                        reminderInfo.setActive(1);
                    }else{
                        reminderInfo.setActive(0);
                    }

                }
            });

            if (position<activityReminderSize){
                //当为激活时,为开启状态
                holder.tb_isActive.setToggleOn();
            }else {
                //当为过期时，为关闭状态
                holder.tb_isActive.setToggleOff();
            }

            return v;
        }

        class Holder{
            TextView tv_reminder_name ;
            TextView tv_reminder_comment;
            TextView tv_reminder_time ;
            ToggleButton tb_isActive ;
            ImageButton ib_menu_more ;

            public Holder(TextView tv_reminder_name, TextView tv_reminder_comment,
                          TextView tv_reminder_time, ToggleButton tb_isActive, ImageButton ib_menu_more) {
                this.tv_reminder_name = tv_reminder_name;
                this.tv_reminder_comment = tv_reminder_comment;
                this.tv_reminder_time = tv_reminder_time;
                this.tb_isActive = tb_isActive;
                this.ib_menu_more = ib_menu_more;
            }
        }
    }


}
