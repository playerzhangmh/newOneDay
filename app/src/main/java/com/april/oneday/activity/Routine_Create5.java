package com.april.oneday.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.april.oneday.R;
import com.april.oneday.dao.RoutinesDao;
import com.april.oneday.utils.ReminderUtis;

import java.io.IOException;

public class Routine_Create5 extends Activity {


    //记录上一次的提醒当默认值
    public int defaultMusic=0;
    public int defaultReminderTime=0;
    public int defaultVibtate=0;


    //成员变量
    private RelativeLayout rl_routines5_addbell;
    private RelativeLayout rl_routines5_addremindertime;
    private RelativeLayout rl_routines5_addvibrate;

    private TextView tv_routine5_music;
    private TextView tv_routine5_remindertime;
    private TextView tv_routine5_vibrate;

    //初始化bell,remindername,vibrate数组
    String[] bell={"巴黎浪漫","晨曦","回忆","魔力钟声","清新的早晨","午后的约会","苏格拉风琴"};
    String[] ringids={R.raw.aa+"",R.raw.ab+"",R.raw.ac+"",R.raw.ad+"",R.raw.ae+"",R.raw.af+"",R.raw.ag+""};
    String[] remindername={"10分钟","20分钟","30分钟","40分钟","50分钟","60分钟"};
    String[] vibrate={"是","否"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏,此处有坑
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.routine5_reminder);



        //选择通知提示音声音
        tv_routine5_music = (TextView)findViewById(R.id.tv_routine5_music);
        rl_routines5_addbell = (RelativeLayout)findViewById(R.id.rl_routines5_addbell);
        rl_routines5_addbell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addbell();
            }
        });


        //选择提前提醒时间
        tv_routine5_remindertime = (TextView)findViewById(R.id.tv_routine5_remindertime);
        rl_routines5_addremindertime = (RelativeLayout)findViewById(R.id.rl_routines5_addremindertime);
        rl_routines5_addremindertime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addremindertime();
            }
        });


        //选择是否振动
        tv_routine5_vibrate = (TextView)findViewById(R.id.tv_routine5_vibrate);
        rl_routines5_addvibrate = (RelativeLayout)findViewById(R.id.rl_routines5_addvibrate);
        rl_routines5_addvibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addvibrate();
            }
        });
    }


    //增加提示音
    public void addbell(){

        new AlertDialog.Builder(this)
                .setTitle("选择提示音")
                .setSingleChoiceItems(bell, defaultMusic, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //将选择的值当成下一次进来的默认值
                        defaultMusic=i;

                        tv_routine5_music.setText(bell[i]);
                        /**
                         * 存入数据库中的realRingPath(uri)
                         */
                        // 播放提示音
                        playRawRing(i);

                    }

                    public void playRawRing(int index) {
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        try {
                            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse("android.resource://" +
                                    Routine_Create5.this.getPackageName() + "/" +ringids[index]));
                            mediaPlayer.prepare();
                            mediaPlayer.start();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })

                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();






    }


    //增加提醒时间
    public void addremindertime(){

        new AlertDialog.Builder(this)
                .setTitle("选择提前提醒时间")
                .setSingleChoiceItems(remindername, defaultReminderTime, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //将选择的值当成下一次进来的默认值
                        defaultReminderTime=i;

                        tv_routine5_remindertime.setText(remindername[i]);
                        int reminderValue = getReminderValue(remindername[i]);
                        Log.i("Routine_Create5",reminderValue+"");
                        /**
                         * 提前提醒时间reminderValue
                         */
                        dialogInterface.dismiss();
                    }
                }).show();
        
    }


    //是否开启振动
    public void addvibrate(){

        new AlertDialog.Builder(this)
                .setTitle("是否开启振动")
                .setSingleChoiceItems(vibrate, defaultVibtate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //将选择的值当成下一次进来的默认值
                        defaultVibtate=i;

                        tv_routine5_vibrate.setText(vibrate[i]);
                        int vibrateValue = getVibrate(Routine_Create5.this.vibrate[i]);
                        Log.i("Routine_Create5",vibrateValue+"");
                        /**
                         * 是否振动vibrateValue
                         */
                        dialogInterface.dismiss();
                    }
                }).show();
    }


    public int getReminderValue(String ringname){
        int[] timearray={10,20,30,40,50,60};
        int remindervalue = 0;
        switch (ringname){
            case "10分钟":
                remindervalue=timearray[0];
                break;
            case "20分钟":
                remindervalue=timearray[1];
                break;
            case "30分钟":
                remindervalue=timearray[2];
                break;
            case "40分钟":
                remindervalue=timearray[3];
                break;
            case "50分钟":
                remindervalue=timearray[4];
                break;
            case "60分钟":
                remindervalue=timearray[5];
                break;

        }
        return remindervalue;
    }


    public int getVibrate(String vibrate){
        int[] vibratearray={0,1};
        int vibratevalue=0;

        switch (vibrate){

            case "否":
                vibratevalue=vibratearray[0];
                break;
            case "是":
                vibratevalue=vibratearray[1];
                break;
        }
        return vibratevalue;

    }


    //返回按钮
    public void routine5_back(View v){
        finish();
    }


    //确认按钮
    public void routine5_confirm(View v){
        Intent intent = new Intent();
        String vabrate = tv_routine5_vibrate.getText().toString();
        if(vabrate.equals("是")){
            intent.putExtra("vabrate", RoutinesDao.VABRATE);
        }else {//1
            intent.putExtra("vabrate", RoutinesDao.UNVABRATE);
        }

        String time = tv_routine5_remindertime.getText().toString();
        long ringbefore=Integer.parseInt(time.substring(0,time.indexOf("分")))*60*1000;//2
        intent.putExtra("ringbefore",ringbefore+"");

        String ringpath = tv_routine5_music.getText().toString();
        String realRingPath = ReminderUtis.getRealRingPath(ringpath);
        intent.putExtra("realRingPath",realRingPath);//3
        intent.putExtra("reminder","提醒设置成功");//4
        setResult(200,intent);
        finish();
    }

}
