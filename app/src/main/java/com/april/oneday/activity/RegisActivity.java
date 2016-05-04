package com.april.oneday.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.april.oneday.R;
import com.april.oneday.db.DBHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisActivity extends Activity implements View.OnClickListener{

    String APPKEY = "11d97651ac898";
    String APPSECRETE = "def037514c67c2bbce708703eaf83441";

    private EditText phoneNum;      //手机号、输入框
    private EditText et_reqcode;    //验证码、输入框
    private Button btn_register_reqcode;     //获取验证码、按钮
    private EditText password;      //登录密码、输入框
    private Button regis_next;      //下一步

    int i = 60;
    private SQLiteDatabase db;
	private String phoneNums;
	private String passwd;

	SharedPreferences sp;
	private TextView regis_xieyi, regis_qes;	//协议、注册问题

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);

        //获取操作数据库的一个小助手
        DBHelper help = new DBHelper(this,"myapp.db",null,1);
        //获取一个可读可写的数据库
        db = help.getWritableDatabase();

		sp= getSharedPreferences("config", MODE_PRIVATE);

        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {

        phoneNum = (EditText) findViewById(R.id.phoneNum);
        et_reqcode = (EditText) findViewById(R.id.et_reqcode);
        btn_register_reqcode = (Button) findViewById(R.id.btn_register_reqcode);
        password = (EditText) findViewById(R.id.password);

		regis_xieyi = (TextView) findViewById(R.id.regis_xieyi);
		regis_qes = (TextView) findViewById(R.id.regis_qes);


		regis_next = (Button) findViewById(R.id.regis_next);
        //事件监听
        btn_register_reqcode.setOnClickListener(this);
        regis_next.setOnClickListener(this);

		regis_xieyi.setOnClickListener(this);
		regis_qes.setOnClickListener(this);

        // 启动短信验证sdk
        SMSSDK.initSDK(this, APPKEY, APPSECRETE);
        EventHandler eventHandler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    public void onClick(View v) {
		phoneNums = phoneNum.getText().toString();

		SharedPreferences.Editor editor = sp.edit();
		editor.putString("username",phoneNums).commit();

		passwd = password.getText().toString();
        switch (v.getId()) {
            case R.id.btn_register_reqcode:
                // 1. 通过规则判断手机号
                if (!judgePhoneNums(phoneNums)) {
                    return;
                }
                // 2. 通过sdk发送短信验证
                SMSSDK.getVerificationCode("86", phoneNums);

                // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                btn_register_reqcode.setClickable(false);
                btn_register_reqcode.setText("重新发送(" + i + ")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-9);
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();
                break;

			case R.id.regis_xieyi:
                startActivity(new Intent(RegisActivity.this,XieyiActivity.class));
				break;
			case R.id.regis_qes:
                startActivity(new Intent(RegisActivity.this,RegisqesActivity.class));
				break;

            case R.id.regis_next:
                //将收到的验证码和手机号提交再次核对
                SMSSDK.submitVerificationCode("86", phoneNums, et_reqcode.getText().toString());
                break;
        }
    }

	private boolean regisCheck(String phoneNums, String passwd) {
        //登录密码有效性验证&&保存到数据库验证&&用户未注册过(顺序)
        if (!hasRegised(phoneNums)&&judgePasswd(passwd)&&(saveToSql(phoneNums,passwd)>0)) {
            return true;
        }
        return false;
    }

    private boolean hasRegised(String phoneNums) {
        //手机号已经注册过
        boolean value = false;
        String[] columns ={"username"};
        String[] whereargus = {phoneNums};
        Cursor cursor = db.query("user", columns, "username=?", whereargus, null, null, null);
        while(cursor.moveToNext()){
            value = true;
        }
        return value;
    }

    private int saveToSql(String phoneNums, String passwd) {

        int value = -1;

        ContentValues values = new ContentValues();
        values.put("username",phoneNums);
        values.put("password", passwd);
        value = (int) db.insert("user", null, values);
        return value;
    }

    /**
     * 判断密码中的字符
     */
    private boolean judgePasswd(String passwd) {
        //密码：6-16位（数字、字母或下划线）
        Pattern p = Pattern.compile("\\w{6,16}");
        Matcher m = p.matcher(passwd);
        if(m.matches()){
           return true;
        }
        Toast.makeText(this, "密码输入有误！",Toast.LENGTH_SHORT).show();
        return false;
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                btn_register_reqcode.setText("重新发送(" + i + ")");
            } else if (msg.what == -8) {
                btn_register_reqcode.setText("获取验证码");
                btn_register_reqcode.setClickable(true);
                i = 30;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event", "event=" + event);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        // 提交验证码成功
                        Toast.makeText(getApplicationContext(), "提交验证码成功",
                                Toast.LENGTH_SHORT).show();

						if (regisCheck(phoneNums,passwd)) {
							startActivity(new Intent(RegisActivity.this,LoginActivity.class));
                            finish();
						} else {
							Toast.makeText(RegisActivity.this, "注册信息错误！",Toast.LENGTH_SHORT).show();
						}

                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "正在获取验证码",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        ((Throwable) data).printStackTrace();
                    }
                }
            }
        }
    };

    /**
     * 判断手机号码是否合理
     */
    private boolean judgePhoneNums(String phoneNums) {
        if (isPhoneNumMatchLength(phoneNums, 11)
                && isMobileNO(phoneNums)) {
            return true;
        }
        Toast.makeText(this, "手机号码输入有误！",Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 判断一个手机号字符串的位数
     */
    public static boolean isPhoneNumMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}
