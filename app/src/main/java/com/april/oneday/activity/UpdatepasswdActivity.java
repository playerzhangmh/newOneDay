package com.april.oneday.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.april.oneday.R;
import com.april.oneday.db.DBHelper;

public class UpdatepasswdActivity extends Activity implements View.OnClickListener{

	private EditText newpasswd, renewpasswd;		//新密码、确认密码
	private Button btn_updatepwd_ok, btn_updatepwd_cancel;	//确认、取消

	private SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updatepasswd);

		initView();
		initData();
	}

	private void initView() {

		newpasswd = (EditText) findViewById(R.id.newpasswd);
		renewpasswd = (EditText) findViewById(R.id.renewpasswd);

		btn_updatepwd_ok = (Button) findViewById(R.id.btn_updatepwd_ok);
		btn_updatepwd_cancel = (Button) findViewById(R.id.btn_updatepwd_cancel);
	}

	private void initData() {

		//获取操作数据库的一个小助手
		DBHelper help = new DBHelper(this, "myapp.db", null, 1);
		//获取一个可读可写的数据库
		db = help.getWritableDatabase();

		//按钮监听事件
		btn_updatepwd_ok.setOnClickListener(this);
		btn_updatepwd_cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btn_updatepwd_ok:

				//获得输入框内容
				String newpwd = newpasswd.getText().toString();
				String repwd = renewpasswd.getText().toString();

				Intent intent = getIntent();
				String username = intent.getStringExtra("username");
				Toast.makeText(UpdatepasswdActivity.this,username,Toast.LENGTH_LONG).show();

				//存在此用户、那就修改他的密码
				if (updatePasswd(username,newpwd,repwd)) {
					//修改成功
					startActivity(new Intent(UpdatepasswdActivity.this,LoginActivity.class));
					finish();
				}
				break;
			case R.id.btn_updatepwd_cancel:
				//取消重新设置密码、回到登录页面
				startActivity(new Intent(UpdatepasswdActivity.this,LoginActivity.class));
				finish();
				break;
			default:
				break;
		}
	}

	/**
	 * 数据库中修改用户密码
	 */
	private boolean updatePasswd(String username, String newpwd, String repwd) {

		boolean value = false;
		if (newpwd.equals(repwd)) {
			//两次密码一致、修改数据库

			ContentValues values = new ContentValues();
			values.put("password", newpwd);
			String whereCluase =" username = ?"; //注意此处不用再加where
			String[] whereargus = {username};
			db.update("user", values, whereCluase, whereargus);

			value = true;
		} else {
			Toast.makeText(UpdatepasswdActivity.this,"密码不一致",Toast.LENGTH_LONG).show();
		}

		return value;
	}
}
