package com.april.oneday.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.april.oneday.R;
import com.april.oneday.utils.PrefUtils;

public class SetNickNameActivity extends Activity {

    private Button btn_nickname_save;
    private String nickName;
    private EditText et_nickname;
    private ImageButton btn_setnickname_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_set_nickname);

        //进入该Activity自动弹出键盘
        et_nickname = (EditText) findViewById(R.id.et_nickname);
        btn_nickname_save = (Button) findViewById(R.id.btn_nickname_save);
        btn_setnickname_back = (ImageButton) findViewById(R.id.btn_setnickname_back);


        btn_nickname_saveOnClick();
        btn_setnickname_backOnClick();

    }

    private void btn_setnickname_backOnClick() {
        btn_setnickname_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void btn_nickname_saveOnClick() {
        btn_nickname_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nickName = et_nickname.getText().toString();

                if (!TextUtils.isEmpty(nickName)){
                    Toast.makeText(SetNickNameActivity.this, "昵称设置成功", Toast.LENGTH_SHORT).show();
                    PrefUtils.putString("nickname",nickName,SetNickNameActivity.this);
                    Intent date = new Intent();
                    date.putExtra("nickname",nickName);
                    setResult(666,date);
                    finish();
                }else {
                    Toast.makeText(SetNickNameActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
