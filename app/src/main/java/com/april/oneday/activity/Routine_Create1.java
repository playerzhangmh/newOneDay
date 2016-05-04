package com.april.oneday.activity;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.april.oneday.R;
import com.april.oneday.bean.RoutinesInfo;
import com.april.oneday.dao.RoutinesDao;
import com.april.oneday.service.MyRoutinesService;
import com.april.oneday.utils.ReminderUtis;

public class Routine_Create1 extends Activity {

    //成员变量
    private EditText et_routine1_name;
    private SeekBar sb_routine1_seek;
    private TextView tv_routines_days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏,此处有坑
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_routine__create1);

        //临时开启service 待删代码
        startService(new Intent(this, MyRoutinesService.class));


        //初始化
        et_routine1_name = (EditText)findViewById(R.id.et_routine1_name);
        sb_routine1_seek = (SeekBar)findViewById(R.id.sb_routine1_seek);
        tv_routines_days = (TextView)findViewById(R.id.tv_routines_days);

        //配置seekBar,天数1--15
        sb_routine1_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                //设置seekBar最大值
                sb_routine1_seek.setMax(14);


                /**
                 * tv_routines_days为用户设定天数
                 */
                tv_routines_days.setText(i+1+"");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }


    //返回--》Routine展示页面
    public void routine1_back(View v){
        finish();
    }

    //确认--》Routine_Create2
    public void routine1_confirm(View v){
        String title = et_routine1_name.getText().toString();
        String days = tv_routines_days.getText().toString();
        if(title.isEmpty()){
        }else{
            RoutinesDao dao=new RoutinesDao(this);
            if(dao.queryTitleExist(title)){
                Toast.makeText(this,"标题已存在，请重新输入",Toast.LENGTH_SHORT).show();
            }else {
                dao.insertSchedule(title,Integer.parseInt(days));
                RoutinesInfo scheduleByTitle = dao.getScheduleByTitle(title);
                Intent intent = new Intent(this,Routine_Create2.class);
                intent.putExtra("RoutinesInfo",scheduleByTitle);
                startActivity(intent);
                finish();
            }

        }



    }
}
