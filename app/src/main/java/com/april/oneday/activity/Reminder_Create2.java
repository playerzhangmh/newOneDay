package com.april.oneday.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.april.oneday.R;
import com.april.oneday.dao.ReminderdbDao;

import java.io.IOException;
import java.util.ArrayList;

import wheelview.LoopView;
import wheelview.OnItemSelectedListener;

public class Reminder_Create2 extends Activity {

    private static final String TAG = "Reminder_Create2";
    private EditText et_reminder_title;
    private EditText et_reminder_comment;
    private TextView tv_reminder_date;

    private TextView tv_reminder_time_hour;
    private TextView tv_reminder_time_minute;

    private TextView tv_reminder_repeatType;
    private TextView tv_reminder_ring;
    private TextView tv_reminder_vabrate;
    private TextView tv_reminder_vabratetype;

    private LinearLayout rl_hour;
    private LinearLayout rl_minute;

    int defTimeHour =8 ;
    int defTimeMinute =0;

    //用于拼成xx:xx:xx的日期格式
    private String defTimeHourString;
    private String defTimeMinuteString;

    //用于展示存储选择的ring的index
    int chosen_ring=0;


    //以下为用于插入数据库的数据
    String reminder_name ="提醒";
    String reminder_date;
    String reminder_time="08:00:00";
    int repeatType=0;//提醒周期，天 月  年   0,1,2,3
    String reminder_comment="";
    String ring_path=R.raw.aa+"";
    int vabrate_type=0; //长震动，短震动 0,1
    int vabrate_times=0;//震动次数  2,4,6,8,10，自定义
    int iconRid= R.drawable.clock_reminder;
    String iconBgColor="#fc8327";//TODO,是否用int更好？
    int active=0;//是否过期，初值均为0



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_create2);

        reminder_date = getIntent().getStringExtra("reminder_date");

        et_reminder_title = (EditText) findViewById(R.id.et_reminder_title);
        et_reminder_comment = (EditText) findViewById(R.id.et_reminder_comment);
        tv_reminder_date  = (TextView) findViewById(R.id.tv_reminder_date);

        tv_reminder_time_hour    = (TextView) findViewById(R.id.tv_reminder_time_hour);
        tv_reminder_time_minute  = (TextView) findViewById(R.id.tv_reminder_time_minute);

        tv_reminder_repeatType = (TextView) findViewById(R.id.tv_reminder_repeatType);
        tv_reminder_ring       = (TextView) findViewById(R.id.tv_reminder_ring);
        tv_reminder_vabrate    = (TextView) findViewById(R.id.tv_reminder_vabrate);
        tv_reminder_vabratetype = (TextView) findViewById(R.id.tv_reminder_vabratetype);


        initData();//设置日期和默认的时间

        initTimerSelector();//初始化时间选择滚轴
    }

    /**
     * 调用开源框架，仿ios的滚动数字选择器
     */
    private void initTimerSelector() {
        rl_hour = (LinearLayout) findViewById(R.id.rl_hour);
        rl_minute = (LinearLayout) findViewById(R.id.rl_minute);

        LoopView loopview_hour = new LoopView(this);
        LoopView loopview_minute = new LoopView(this);


        ArrayList<String> list_hour = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            list_hour.add(i+"");
        }

        ArrayList<String> list_minute = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            list_minute.add(i+"");
        }
        //设置是否循环播放
        //loopView.setNotLoop();
        //滚动监听
        loopview_hour.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                Log.d("debug", "hour " + index);
                defTimeHourString = index<10?"0"+index:""+index;
                tv_reminder_time_hour.setText(defTimeHourString);
            }
        });
        loopview_minute.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                Log.d("debug", "minute " + index);
                defTimeMinuteString = index<10?"0"+index:""+index;
                tv_reminder_time_minute.setText(defTimeMinuteString);
            }
        });

        //getLoopView设置数据
        loopview_hour.setItems(list_hour);
        loopview_minute.setItems(list_minute);

        //设置初始位置
        loopview_hour.setInitPosition(defTimeHour);
        loopview_minute.setInitPosition(defTimeMinute);

        //设置字体大小
        loopview_hour.setTextSize(15);
        loopview_minute.setTextSize(15);

        rl_hour.addView(loopview_hour,0);
        rl_minute.addView(loopview_minute,0);
    }

    /**
     * 设置日期和默认的时间
     */
    private void initData() {
        tv_reminder_date.setText(reminder_date);
        defTimeHourString = defTimeHour<10?"0"+defTimeHour:""+defTimeHour;
        defTimeMinuteString = defTimeMinute<10?"0"+defTimeMinute:""+defTimeMinute;

        tv_reminder_time_hour.setText(defTimeHourString);
        tv_reminder_time_minute.setText(defTimeMinuteString);

    }


    public void changeReminderDate(View view) {
        Intent intent = new Intent(this,Reminder_Create1.class);
        startActivity(intent);
    }


    public void changeRepeatType(View view) {
        final String[] items = {"无","每天","每月","每年"};

        new AlertDialog.Builder(this)
                .setTitle("选择重复模式")
                .setSingleChoiceItems(items,repeatType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        repeatType = which;
                        dialog.dismiss();
                        tv_reminder_repeatType.setText(items[which]);
                    }
                })
                .show();
    }


    //TODO
    public void changeRing(View view) {

        final String[] ringnames = {"aa","ab","ac","ad","ae","ad","ag"};
        final int[] ringids={R.raw.aa,R.raw.ab,R.raw.ac,R.raw.ad,R.raw.ae,R.raw.af,R.raw.ag};




        new AlertDialog.Builder(this)
                .setTitle("选择提示音")
                .setSingleChoiceItems(ringnames,chosen_ring, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chosen_ring=which;
                        // 播放提示音
                        playRawRing(which);
                    }

                    private void playRawRing(int index) {
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        try {
                            mediaPlayer.setDataSource(Reminder_Create2.this, Uri.parse("android.resource://" +
                                    Reminder_Create2.this.getPackageName() + "/" + ringids[index]));
                            mediaPlayer.prepare();
                            mediaPlayer.start();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //把选择的提示音的名称回显到页面
                        ring_path=ringids[chosen_ring]+"";
                        tv_reminder_ring.setText("铃声"+ringnames[chosen_ring]);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }



    public void setVabrate(View view) {
        final String[] items = {"0","2","4","6","自定义"};
        final EditText editText = new EditText(this);
        
        new AlertDialog.Builder(this)
                .setTitle("选择震动次数")
                .setSingleChoiceItems(items,0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0){
                            vabrate_times=0;
                            dialog.dismiss();
                            tv_reminder_vabrate.setText("不震动");
                        }else if (which==items.length-1){
                            dialog.dismiss();
                            new AlertDialog.Builder(Reminder_Create2.this)
                                    .setView(editText)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String custom = editText.getText().toString();
                                            int customInt = Integer.parseInt(custom);
                                            vabrate_times = customInt;
                                            dialog.dismiss();
                                            tv_reminder_vabrate.setText("震动"+customInt+"次");

                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .show();
                        }else {
                            vabrate_times = 2*which;
                            dialog.dismiss();
                            tv_reminder_vabrate.setText("震动"+vabrate_times+"次");
                        }


                    }
                })
                .show();

    }


    public void setVabrateType(View view) {
        final String[] items = {"长震动","短震动"};

        new AlertDialog.Builder(this)
                .setTitle("选择震动模式")
                .setSingleChoiceItems(items,vabrate_type, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vabrate_type = which;
                        dialog.dismiss();
                        tv_reminder_vabratetype.setText(items[which]);
                    }
                })
                .show();
    }

    public void cancelReminder(View view) {
        onBackPressed();
    }

    //把Reminder的信息插入数据库
    public void createReminder(View view) {
        String title = et_reminder_title.getText().toString();
        reminder_comment = et_reminder_comment.getText().toString();
        if (!title.isEmpty()){
            Log.e(TAG,title);
            reminder_name = title;
        }
        reminder_time = defTimeHourString+":"+defTimeMinuteString+":00";


        //把Reminder的信息插入数据库
        ReminderdbDao reminderdbDao = new ReminderdbDao(this);
        reminderdbDao.insertReminder(reminder_name,reminder_date,reminder_time,repeatType,reminder_comment,
                ring_path,vabrate_type,vabrate_times,iconRid,iconBgColor);

        Log.e(TAG, reminder_name+","+
         reminder_date+","+
         reminder_time+","+
         repeatType+","+
         reminder_comment+","+
         ring_path+","+
         vabrate_type+","+
         vabrate_times+","+
         iconRid+","+
         iconBgColor+","+
         active+",");

        finish();
    }


}
