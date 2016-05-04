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
import android.widget.Toast;

import com.april.oneday.R;
import com.april.oneday.dao.ReminderdbDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

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

    String defTimeHour="08" ;
    String defTimeMinute ="00";

    /*//用于拼成xx:xx:xx的日期格式
    private String defTimeHour;
    private String defTimeMinute;*/

    //用于展示存储选择的ring的index
    int chosen_ring=0;


    //以下为用于插入数据库的数据
    String reminder_name ="提醒";
    String reminder_date= new Date().toString();
    String reminder_time;
    int repeatType=0;//提醒周期，天 月  年   0,1,2,3
    String reminder_comment="写个描述吧！";
    String ring_path=R.raw.aa+"";
    int vabrate_type=0; //长震动，短震动 0,1
    int vabrate_times=0;//震动次数  2,4,6,8,10，自定义
    int iconRid= R.drawable.shape_ib_bg_blue;
    String iconBgColor="#fc8327";//根据月份进行颜色的选择
    int active=0;//是否过期，初值均为0

    //int用于修改数据
    int remind_id;
    int reminder_active;

	  //铃声名称和rid
    String[] ringnames = {"巴黎浪漫","晨曦","回忆","魔力钟声","清新的早晨","午后的约会","苏格拉风琴"};
    String[] ringids={R.raw.aa+"",R.raw.ab+"",R.raw.ac+"",R.raw.ad+"",R.raw.ae+"",R.raw.af+"",R.raw.ag+""};

    //重复类型
    String[] repeatTypeitems = {"无","每天","每月","每年"};

    //震动次数
    String[] vabrateStrings={"不震动","震动2次","震动4次","震动6次"};

    //用于判断是否是从编辑页面进入的
    boolean isEdit;

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






        String[] infoString = getIntent().getStringArrayExtra("selectedReminderInfo_String");
        int[] infoInt = getIntent().getIntArrayExtra("selectedReminderInfo_Int");


        if (infoString!=null){
            isEdit = true;

            reminder_name =infoString[0];
            reminder_date=infoString[1];

            reminder_time=infoString[2];
            Log.e(TAG,reminder_time);
            //用于初始化时间滚轴
            defTimeHour =reminder_time.substring(0,2);
            defTimeMinute=reminder_time.substring(3,5);
            Log.e(TAG,defTimeHour+":"+defTimeMinute);
            /*//用于最后插入数据库
            defTimeHour;
            defTimeMinute;*/


            reminder_comment=infoString[3];
            ring_path=infoString[4];

            iconBgColor=infoString[5];

        }



        Log.e(TAG,"infoInt"+infoInt);
        if (infoInt!=null){
            repeatType=infoInt[0];
            vabrate_type=infoInt[1];
            vabrate_times=infoInt[2];
            iconRid=infoInt[3];
            active=infoInt[4];
            remind_id = infoInt[5];//用于修改数据
            reminder_active = infoInt[6];//用于修改数据
            Log.e(TAG,"是否激活"+reminder_active);

        }


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
                defTimeHour = index<10?"0"+index:""+index;
                tv_reminder_time_hour.setText(defTimeHour);
            }
        });
        loopview_minute.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                Log.d("debug", "minute " + index);
                defTimeMinute = index<10?"0"+index:""+index;
                tv_reminder_time_minute.setText(defTimeMinute);
            }
        });

        //getLoopView设置数据
        loopview_hour.setItems(list_hour);
        loopview_minute.setItems(list_minute);

        //设置初始位置
        int defHour = parseTimeString(defTimeHour);
        int defMinute = parseTimeString(defTimeMinute);
        loopview_hour.setInitPosition(defHour);
        loopview_minute.setInitPosition(defMinute);

        //设置字体大小
        loopview_hour.setTextSize(15);
        loopview_minute.setTextSize(15);

        rl_hour.addView(loopview_hour,0);
        rl_minute.addView(loopview_minute,0);
    }


    private int parseTimeString(String time) {
        if(time.startsWith("0")){
            return Integer.parseInt(time.substring(1));
        }else return Integer.parseInt(time);
    }

    /**
     * 设置日期和默认的时间
     */
    private void initData() {
        et_reminder_title.setText(reminder_name);
        et_reminder_comment.setText(reminder_comment);

        tv_reminder_date.setText(reminder_date);


        tv_reminder_time_hour.setText(defTimeHour);
        tv_reminder_time_minute.setText(defTimeMinute);

        int index =getIndexFromArray(ringids,ring_path);

        tv_reminder_ring.setText("铃声"+ringnames[index]);
        tv_reminder_repeatType.setText(repeatTypeitems[repeatType]);
        if (vabrate_type==0){
            tv_reminder_vabratetype.setText("长震动");
        }else
            tv_reminder_vabratetype.setText("短震动");

//        tv_reminder_vabrate.setText();



    }

    private int getIndexFromArray(String[] ringids, String ring_path) {
        for (int i=0;i<ringids.length;i++){
            if (ringids[i].equals(ring_path))
                return i;
        }
        return -1;
    }




    public void changeReminderDate(View view) {
        Intent intent = new Intent(this,Reminder_Create1.class);
        startActivity(intent);
    }


    public void changeRepeatType(View view) {


        new AlertDialog.Builder(this)
                .setTitle("选择重复模式")
                .setSingleChoiceItems(repeatTypeitems,repeatType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        repeatType = which;
                        dialog.dismiss();
                        tv_reminder_repeatType.setText(repeatTypeitems[which]);
                    }
                })
                .show();
    }



    public void changeRing(View view) {




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
						tv_reminder_ring.setText(ringnames[chosen_ring]);

                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }



    public void setVabrate(View view) {
        final String[] items = {"0","2","4","6","自定义"};

        final EditText editText = new EditText(this);
        int chosenItem = 4;
        if (vabrate_times==0||vabrate_times==2||vabrate_times==4||vabrate_times==6){
            chosenItem =vabrate_times/2;
        }

        
        new AlertDialog.Builder(this)
                .setTitle("选择震动次数")
                .setSingleChoiceItems(items,chosenItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (which==items.length-1){
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
                        }else{
                            vabrate_times = 2*which;
                            tv_reminder_vabrate.setText(vabrateStrings[which]);
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
        reminder_time = defTimeHour+":"+defTimeMinute+":00";

        //根据月份设置icon的颜色
        setIconColor();


        ReminderdbDao reminderdbDao = new ReminderdbDao(this);
        if (isEdit){//把Reminder的信息修改数据库
            Log.e(TAG,"updateReminder");
            reminderdbDao.updateReminder(remind_id,reminder_name,reminder_date,reminder_time,repeatType,reminder_comment,
                    ring_path,vabrate_type,vabrate_times,iconRid,iconBgColor,reminder_active);
            Log.e(TAG,"是否激活"+reminder_active);
            Toast.makeText(Reminder_Create2.this, "修改成功！", Toast.LENGTH_SHORT).show();
        }else{//如果isEdit为false，把Reminder的信息插入数据库
            Log.e(TAG,"insertReminder");
            reminderdbDao.insertReminder(reminder_name,reminder_date,reminder_time,repeatType,reminder_comment,
                    ring_path,vabrate_type,vabrate_times,iconRid,iconBgColor);
            Toast.makeText(Reminder_Create2.this, "创建成功！", Toast.LENGTH_SHORT).show();
        }


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


    /**
     * 根据月份给iconBgColor赋值
     */
    private void setIconColor() {
        String monthString = reminder_date.substring(5, 7);
        int monthInt = parseTimeString(monthString);
        switch (monthInt){
            case 1:
                iconBgColor="#BCAC6F";
                break;
            case 2:
                iconBgColor="#9DB7C6";
                break;
            case 3:
                iconBgColor="#BEB1AB";
                break;
            case 4:
                iconBgColor="#788B57";
                break;
            case 5:
                iconBgColor="#B1D6DC";
                break;
            case 6:
                iconBgColor="#917F8B";
                break;
            case 7:
                iconBgColor="#474E5E";
                break;
            case 8:
                iconBgColor="#F5D894";
                break;
            case 9:
                iconBgColor="#C7EEB9";
                break;
            case 10:
                iconBgColor="#DEFBDA";
                break;
            case 11:
                iconBgColor="#F68F2B";
                break;
            case 12:
                iconBgColor="#F9AF69";
                break;
        }
    }


}
