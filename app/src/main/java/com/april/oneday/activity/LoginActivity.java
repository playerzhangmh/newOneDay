package com.april.oneday.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.april.oneday.R;
import com.april.oneday.db.DBHelper;
import com.april.oneday.utils.PreUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import de.hdodenhof.circleimageview.CircleImageView;


public class LoginActivity extends Activity implements View.OnClickListener, Handler.Callback, PlatformActionListener {

	private static final int MSG_AUTH_CANCEL = 2;
	private static final int MSG_AUTH_ERROR= 3;
	private static final int MSG_AUTH_COMPLETE = 4;
	//定义Handler对象
	private Handler handler;

	private EditText username, password;  //用户、密码
	private TextView regis,forgetPasswd;     //立即注册、忘记密码
	private Button btn_login;   //登录

	private SQLiteDatabase db;
	private CircleImageView iv_qzone, iv_sina;    //2个图标

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		//初始化ShareSDK
		ShareSDK.initSDK(this);

		initView();

		initData();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		//实例化Handler对象并设置信息回调监听接口
		handler = new Handler(this);

		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);

		regis = (TextView) findViewById(R.id.regis);
		forgetPasswd = (TextView) findViewById(R.id.forgetPasswd);
		btn_login = (Button) findViewById(R.id.btn_login);

		iv_qzone = (CircleImageView) findViewById(R.id.iv_qzone);
		iv_sina = (CircleImageView) findViewById(R.id.iv_sina);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {

		//获取操作数据库的一个小助手
		DBHelper help = new DBHelper(this, "myapp.db", null, 1);
		//获取一个可读可写的数据库
		db = help.getWritableDatabase();

		regis.setOnClickListener(this);
		forgetPasswd.setOnClickListener(this);
		btn_login.setOnClickListener(this);

		iv_qzone.setOnClickListener(this);
		iv_sina.setOnClickListener(this);
	}

	/**
	 * 数据库中匹配用户和密码
	 */
	private boolean checkUserAndPasswd(String user, String passwd) {

		boolean value = false;
		String[] columns = {"username"};
		String[] whereargus = {user, passwd};
		Cursor cursor = db.query("user", columns, "username=? and password=?", whereargus, null, null, null);
		while (cursor.moveToNext()) {
			value = true;
		}
		return value;
	}

	/**
	 * 数据库中匹配用户
	 */
	private boolean checkUser(String user) {

		boolean value = false;
		String[] columns = {"username"};
		String[] whereargus = {user};
		Cursor cursor = db.query("user", columns, "username=?", whereargus, null, null, null);
		while (cursor.moveToNext()) {
			value = true;
		}
		return value;
	}

	@Override
	public void onClick(View v) {

		String user = username.getText().toString();
		String passwd = password.getText().toString();

		switch (v.getId()) {
			case R.id.regis:
				//跳转到注册页面
				startActivity(new Intent(LoginActivity.this, RegisActivity.class));
				//finish();
				break;
			case R.id.forgetPasswd:

				if (user.isEmpty()) {
					Toast.makeText(LoginActivity.this, "先输入用户名、判断是否存在", Toast.LENGTH_LONG).show();
				}

				if (checkUser(user)) {
					//用户存在、修改密码
					Intent intent = new Intent(LoginActivity.this,UpdatepasswdActivity.class);
					intent.putExtra("username",user);
					startActivity(intent);
					//finish();
				} else {
					Toast.makeText(LoginActivity.this, "用户名不存在", Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.btn_login:
				//用户登录

				Toast.makeText(LoginActivity.this, "点击登录", Toast.LENGTH_LONG).show();
				if (checkUserAndPasswd(user, passwd)) {
					//用户和密码匹配。。。进入主页面

					//更新sp
					PreUtils.setBoolean(LoginActivity.this,"is_login_did",true);
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					intent.putExtra("username",user);
					startActivity(intent);
					finish();
				} else if (checkUser(user)){
					//存在此用户、密码错误
					Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_LONG).show();
				} else {
					//用户不存在
					Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_LONG).show();
				}
				break;

			case R.id.iv_qzone:
				//QQ空间
				Platform qzone = ShareSDK.getPlatform(QZone.NAME);
				authorize(qzone);
				break;
			case R.id.iv_sina:
				//新浪微博
				Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
				authorize(sina);
				break;
			default:
				break;
		}
	}

	/**
	 * 执行授权,获取用户信息
	 */
	private void authorize(Platform plat) {
		if (plat == null) {
			//popupOthers();
			return;
		}
		plat.setPlatformActionListener(this);
		//关闭SSO授权
		plat.SSOSetting(true);
		plat.showUser(null);
	}

	public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
		if (action == Platform.ACTION_USER_INFOR) {
			Message msg = new Message();
			msg.what = MSG_AUTH_COMPLETE;
			msg.obj = new Object[] {platform.getName(), res};
			handler.sendMessage(msg);
		}
	}

	public void onError(Platform platform, int action, Throwable t) {
		if (action == Platform.ACTION_USER_INFOR) {
			handler.sendEmptyMessage(MSG_AUTH_ERROR);
		}
		t.printStackTrace();
	}

	public void onCancel(Platform platform, int action) {
		if (action == Platform.ACTION_USER_INFOR) {
			handler.sendEmptyMessage(MSG_AUTH_CANCEL);
		}
	}


	@SuppressWarnings("unchecked")
	public boolean handleMessage(Message msg) {
		switch(msg.what) {
			case MSG_AUTH_CANCEL: {
				//取消授权
				Toast.makeText(LoginActivity.this, "取消授权", Toast.LENGTH_SHORT).show();
			} break;
			case MSG_AUTH_ERROR: {
				//授权失败
				Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
			} break;
			case MSG_AUTH_COMPLETE: {
				//授权成功
				Toast.makeText(LoginActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
				//更新sp
				PreUtils.setBoolean(LoginActivity.this,"is_login_did",true);
				//进入主页面
				startActivity(new Intent(LoginActivity.this, MainActivity.class));
				finish();
			} break;
		}
		return false;
	}

}
