package com.april.oneday.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.april.oneday.R;
import com.april.oneday.utils.CommenUtils;
import com.april.oneday.utils.MyBitmapUtils;
import com.april.oneday.utils.PrefUtils;
import com.april.oneday.view.RoundImageView;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UserInfoSettingActivity extends Activity {

    private static final int REQUEST_FOR_NICKNAME = 333;
    private static final int REQGEST_PHOTO_GALLERY = 100;
    private ImageButton btnBack;
    private RelativeLayout setHeadIcon;
    private RelativeLayout setName;
    private RelativeLayout setBirthday;
    private RelativeLayout setGender;
    private RoundImageView riv_useringfo_headicon;
    private TextView tv_useringfo_name;
    private TextView tv_useringfo_birthday;
    private TextView tv_useringfo_phonenumber;
    private TextView tv_useringfo_gender;
    private OptionsPickerView pvOptions;
    ArrayList<String> optionsItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_info_setting);



        btnBack = (ImageButton) findViewById(R.id.btn_userinfo_back);
        setHeadIcon = (RelativeLayout) findViewById(R.id.rl_userinfo_headicon);
        setName = (RelativeLayout) findViewById(R.id.rl_userinfo_name);
        setBirthday = (RelativeLayout) findViewById(R.id.rl_userinfo_birthday);
        setGender = (RelativeLayout) findViewById(R.id.rl_userinfo_gender);


        riv_useringfo_headicon = (RoundImageView) findViewById(R.id.riv_useringfo_headicon);
        tv_useringfo_name = (TextView) findViewById(R.id.tv_useringfo_name);
        tv_useringfo_phonenumber = (TextView) findViewById(R.id.tv_useringfo_phonenumber);
        tv_useringfo_birthday = (TextView) findViewById(R.id.tv_useringfo_birthday);
        tv_useringfo_gender = (TextView) findViewById(R.id.tv_useringfo_gender);


        //上次设置的值进行回显
        Bitmap bitmap = null;
        File headIcon = new File(getFilesDir(), "headIcon.jpg");
        if (headIcon.exists()){
            bitmap = MyBitmapUtils.decodeSampledBitmapFromSd(headIcon.getAbsolutePath(), 200, 200);
        }

        if (bitmap!=null){
            riv_useringfo_headicon.setImageBitmap(bitmap);
        }


        tv_useringfo_name.setText(PrefUtils.getString("nickname","未设置",this));
        tv_useringfo_birthday.setText(PrefUtils.getString("birthday","未设置",this));
        tv_useringfo_gender.setText(PrefUtils.getString("gender","未设置",this));
        tv_useringfo_phonenumber.setText(PrefUtils.getString("phonenumber","未注册",this));



        btnBackOnclick();
        setHeadIconOnclick();
        setBirthdayOnclick();
        setGenderOnclick();
        setNameOnclick();


    }

    /**
     * 设置昵称
     */
    private void setNameOnclick() {

        setName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInfoSettingActivity.this,SetNickNameActivity.class);
                startActivityForResult(intent,REQUEST_FOR_NICKNAME);
            }
        });
    }

    //设置性别
    private void setGenderOnclick() {


        optionsItems.add("男");
        optionsItems.add("女");
        pvOptions = new OptionsPickerView(UserInfoSettingActivity.this);

        pvOptions.setPicker(optionsItems);
        pvOptions.setSelectOptions(0);
        pvOptions.setCyclic(false);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String gender = optionsItems.get(options1);
                tv_useringfo_gender.setText(gender);
                PrefUtils.putString("gender",gender,UserInfoSettingActivity.this);

            }
        });

        setGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvOptions.show();
                //弹出选项选择器
            }
        });

    }

    //设置生日
    private void setBirthdayOnclick() {

        //时间选择联动菜单
        final TimePickerView pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();//获取当前年份
        pvTime.setRange(calendar.get(Calendar.YEAR) - 50, calendar.get(Calendar.YEAR));

        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);

        //弹出时间选择器
        setBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置性别条目点击事件
                setBirthday.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pvTime.show();
                    }
                });

            }
        });
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                tv_useringfo_birthday.setText(getTime(date));
                PrefUtils.putString("birthday",getTime(date),UserInfoSettingActivity.this);
            }
        });
    }


    //设置头像
    private void setHeadIconOnclick() {
        setHeadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeHeadPhoto();
            }
        });
    }

    //返回按钮点击事件
    private void btnBackOnclick() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    /**
     * 时间格式化
     */
    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 更换头像
     */
    private void changeHeadPhoto() {
        //查找手机中的图片
        Intent intent = new Intent(Intent.ACTION_PICK, null);//从列表中选择某项并返回所有数据
        //得到系统所有的图片
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");//图片的类型,image/*为所有类型图片
        startActivityForResult(intent, REQGEST_PHOTO_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data!=null){
            switch (requestCode) {
                //请求为获取本地图片时
                case REQGEST_PHOTO_GALLERY:
                    //图片信息需包含在返回数据中
                    String[] proj ={MediaStore.Images.Media.DATA};
                    //获取包含所需数据的Cursor对象
                    @SuppressWarnings("deprecation")
                    Cursor cursor = managedQuery(data.getData(), proj, null, null, null);

                    if (cursor.moveToNext()) {
                        //cursor.moveToFirst();
                        //获取索引
                        int photocolumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        //根据索引值获取图片路径
                        String path = cursor.getString(photocolumn);

                        final Bitmap headIcon = BitmapFactory.decodeFile(path);


                        riv_useringfo_headicon.setImageBitmap(headIcon);

                        CommenUtils.saveHeadIcon(headIcon,UserInfoSettingActivity.this);



                    }
                    cursor.close();
                    break;
                case REQUEST_FOR_NICKNAME:
                    String nickname = data.getStringExtra("nickname");
                    Log.i("REQUEST_FOR_NICKNAME",nickname);
                    tv_useringfo_name.setText(nickname);
                default:
                    break;
            }
        }

    }



}
