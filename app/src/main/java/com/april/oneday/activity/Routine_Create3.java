package com.april.oneday.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.april.oneday.R;
import com.april.oneday.bean.RoutinesDetailInfo;
import com.april.oneday.bean.RoutinesInfo;
import com.april.oneday.dao.RoutinesDao;
import com.april.oneday.utils.ReminderUtis;

import java.io.Serializable;
import java.util.ArrayList;

import wheelview.LoopView;
import wheelview.OnItemSelectedListener;

public class Routine_Create3 extends Activity {


    public static final int REFRESH = 1000;
    private static final String TAG = "Routine_Create3";
    /**
     *  时间指示器指示的位置，起始hour,minute和终止hour,ninute，以及间隔时间
     */

    //
    private int startHourInt;
    private int startMinuteInt;
    private int endHourInt;
    private int endMinuteInt;
    private int interval;

    private String startHourString="08";
    private String startMinuteString="00";
    private String endHourString="08";
    private String endMinuteString="00";

    private String startString;
    private String endString;

    //设定默认位置的时和分
    int defStartHour =8 ;
    int defStartMinute =0;
    int defEndtHour =8 ;
    int defEndMinute =0;

    //成员变量
    private TextView tv_routine3_comment;
    private TextView tv_routine3_finaltagname;
    private TextView tv_routine3_reminder;
    private RelativeLayout rl_routines3_addtag;
    private RelativeLayout rl_routines3_comment;
    private RelativeLayout rl_routines3_reminder;
    private RoutinesDao dao;
    private String icon=R.drawable.routine3_tag_work+"";
    private int p_id;
    private int s_id;
    private int vabrate=RoutinesDao.UNVABRATE;
    private String ringbefore="0";
    private String realRingPath= ReminderUtis.getRealRingPath("巴黎浪漫");
    private RoutinesDetailInfo routinesDetailInfo;
    private int detail_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏,此处有坑
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_routine__create3);
        Toast.makeText(this,"Routine_Create3",Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();
        //Log.v()
        p_id = intent.getIntExtra("p_id",0);
        s_id = intent.getIntExtra("s_id",0);

        //初始化标题
        TextView tv_routine3_title = (TextView) findViewById(R.id.tv_routine3_title);
        if(p_id!=0){
            tv_routine3_title.setText("DAY"+p_id);
        }

        dao = new RoutinesDao(this);



        //时间选择器
        initTimerSelector();

        /**
         * 存入数据库起始时间
         */
        startString=startHourString+":"+startMinuteString;

        /**
         * 存入数据库终止时间
         */
        endString=endHourString+":"+endMinuteString;


        //增加tag
        rl_routines3_addtag = (RelativeLayout)findViewById(R.id.rl_routines3_addtag);
        tv_routine3_finaltagname = (TextView) findViewById(R.id.tv_routine3_finaltagname);;
        rl_routines3_addtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),Routine_Create4.class);
                startActivityForResult(intent,100);
            }
        });


        //增加comment
        tv_routine3_comment = (TextView)findViewById(R.id.tv_routine3_comment);
        rl_routines3_comment = (RelativeLayout)findViewById(R.id.rl_routines3_comment);
        rl_routines3_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcmment();
            }
        });


        //增加reminder
        tv_routine3_reminder = (TextView)findViewById(R.id.tv_routine3_reminder);
        rl_routines3_reminder = (RelativeLayout)findViewById(R.id.rl_routines3_reminder);
        rl_routines3_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),Routine_Create5.class);
                startActivityForResult(intent,200);
            }
        });



        //这里是事件编辑时发送过来的数据，回显在该页
        routinesDetailInfo = (RoutinesDetailInfo) intent.getSerializableExtra("RoutinesDetailInfo");
        if(routinesDetailInfo!=null){
            p_id=routinesDetailInfo.getP_id();
            s_id=routinesDetailInfo.getS_id();
            tv_routine3_title.setText("DAY"+p_id);
            String detail_start = routinesDetailInfo.getDetail_start();
            String defStartHourstring=detail_start.substring(0,detail_start.indexOf(":"));
            defStartHour=Integer.parseInt(defStartHourstring);
            String defStartminutestring=detail_start.substring(detail_start.indexOf(":")+1,detail_start.lastIndexOf(":"));
            defStartMinute=Integer.parseInt(defStartminutestring);
            String detail_end = routinesDetailInfo.getDetail_end();
            String defEndHourstring=detail_end.substring(0,detail_end.indexOf(":"));
            defStartHour=Integer.parseInt(defEndHourstring);
            String defEndminutestring=detail_end.substring(detail_end.indexOf(":")+1,detail_end.lastIndexOf(":"));
            defStartMinute=Integer.parseInt(defEndminutestring);
            String detail_comment = routinesDetailInfo.getDetail_comment();
            tv_routine3_comment.setText(detail_comment);
            String icon_name = routinesDetailInfo.getIcon_name();
            tv_routine3_finaltagname.setText(icon_name);
            icon=routinesDetailInfo.getIconRid()+"";
            ringbefore=routinesDetailInfo.getDetail_ringbefore();
            realRingPath=routinesDetailInfo.getDetail_ringpath();
            vabrate=routinesDetailInfo.getDetail_vabrate();
            detail_id = routinesDetailInfo.getDetail_id();
            tv_routine3_reminder.setText("已经添加提醒");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Routine_Create3",resultCode+"");

        if(resultCode==100){
            if(data!=null){
                Log.i("Routine_Create3","onActivityResult");
                String mRoutine4 = data.getStringExtra("Routine4");
                icon = mRoutine4.substring(mRoutine4.indexOf(",")+1);
                tv_routine3_finaltagname.setText(mRoutine4.substring(0,mRoutine4.indexOf(",")));
            }
        }else if(resultCode==200){
            if(data!=null){
                Log.i("Routine_Create5","onActivityResult");
                String finaltagname = data.getStringExtra("reminder");
                tv_routine3_reminder.setText(finaltagname);
                vabrate = data.getIntExtra("vabrate", RoutinesDao.UNVABRATE);
                ringbefore = data.getStringExtra("ringbefore");
                realRingPath = data.getStringExtra("realRingPath");
            }
        }


    }

    /**
     * 调用开源框架，仿ios的滚动数字选择器
     */
    private void initTimerSelector() {
        RelativeLayout start_routine3_hour = (RelativeLayout) findViewById(R.id.start_routine3_hour);
        RelativeLayout start_routine3_minute = (RelativeLayout) findViewById(R.id.start_routine3_minute);
        RelativeLayout end_routine3_hour = (RelativeLayout) findViewById(R.id.end_routine3_hour);
        RelativeLayout end_routine3_minute = (RelativeLayout) findViewById(R.id.end_routine3_minute);

        LoopView loopview_start_hour = new LoopView(this);
        LoopView loopview_start_minute = new LoopView(this);
        LoopView loopview_end_hour = new LoopView(this);
        LoopView loopview_end_minute = new LoopView(this);


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
        loopview_start_hour.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                startHourInt=index;
                startHourString = index<10?"0"+index:""+index;

            }
        });
        loopview_start_minute.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                startMinuteInt=index;
                startMinuteString = index<10?"0"+index:""+index;

            }
        });

        loopview_end_hour.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                endHourInt=index;
                endHourString = index<10?"0"+index:""+index;

            }
        });
        loopview_end_minute.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                endMinuteInt=index;
                endMinuteString = index<10?"0"+index:""+index;
            }
        });

        //getLoopView设置数据
        loopview_start_hour.setItems(list_hour);
        loopview_start_minute.setItems(list_minute);
        loopview_end_hour.setItems(list_hour);
        loopview_end_minute.setItems(list_minute);

        //设置默认初始位置
        loopview_start_hour.setInitPosition(defStartHour);
        loopview_start_minute.setInitPosition(defStartMinute);
        loopview_end_hour.setInitPosition(defEndtHour);
        loopview_end_minute.setInitPosition(defEndMinute);

        //设置字体大小
        loopview_start_hour.setTextSize(20);
        loopview_start_minute.setTextSize(20);
        loopview_end_hour.setTextSize(20);
        loopview_end_minute.setTextSize(20);

        start_routine3_hour.addView(loopview_start_hour,0);
        start_routine3_minute.addView(loopview_start_minute,0);
        end_routine3_hour.addView(loopview_end_hour,0);
        end_routine3_minute.addView(loopview_end_minute,0);

    }


    public void addcmment(){

        final View view = View.inflate(this, R.layout.routine3_comment, null);
        new AlertDialog.Builder(this)
                .setTitle("请添加备注信息")
                .setView(view)
                .setNegativeButton("取消",null)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        EditText et_routine3_comment=(EditText)view.findViewById(R.id.et_routine3_comment);
                        String comment = et_routine3_comment.getText().toString();
                        if(comment.isEmpty()){
                            Toast.makeText(Routine_Create3.this,"写点什么吧",Toast.LENGTH_SHORT).show();
                        }else{
                            tv_routine3_comment.setText(comment);
                            dialogInterface.dismiss();
                        }
                    }
                }).show();
    }




    public void routine3_back(View v){

        finish();
    }

    public void routine3_confirm(View v){

        //插入details数据，并带一个flag'过去告诉要刷新
        String comment = tv_routine3_comment.getText().toString();
        String finaltagname = tv_routine3_finaltagname.getText().toString();
        String reminder = tv_routine3_reminder.getText().toString();
        String start=startHourString+":"+startMinuteString+":00";
        String end=endHourString+":"+endMinuteString+":00";
        Log.v(TAG,start+"----"+end);
        int iconRid = Integer.parseInt(icon);
        if(!comment.isEmpty()&&!finaltagname.isEmpty()&&!reminder.isEmpty()){
            if(routinesDetailInfo==null){
                dao.insertDetails(p_id,s_id,iconRid,ReminderUtis.getIconName(iconRid),start,end,comment,ringbefore,realRingPath,vabrate);
            }else {
                dao.updateDetailsInfo(detail_id,iconRid,ReminderUtis.getIconName(iconRid),start,end,comment,ringbefore,realRingPath,vabrate);
            }
            Intent intent = new Intent();
            intent.putExtra("refresh",true);
            setResult(REFRESH,intent);
            finish();
        }
    }
}
