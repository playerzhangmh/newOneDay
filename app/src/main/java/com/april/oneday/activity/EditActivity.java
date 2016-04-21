package com.april.oneday.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.april.oneday.R;
import com.april.oneday.bean.MediaInfo;
import com.april.oneday.dao.MediaDao;
import com.lidroid.xutils.BitmapUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class EditActivity extends Activity {

    private ImageButton ib_edit_back;
    private ImageButton ib_edit_pic;
    private Button bt_edit_save;
    private ArrayList<String> mSelectPath=new ArrayList<>();
    private ArrayList<String> picNameList=new ArrayList<>();

    private static final int REQUEST_IMAGE = 2;
    boolean showCamera=true;
    int maxNum=3;
    private ImageView iv_edit_pic1;
    private ImageView iv_edit_pic2;
    private ImageView iv_edit_pic3;
    private EditText et_edit_des;

    /*定位使用*/
    /*private AMapLocationClient mLocationClient=null;
    private AMapLocationListener mLocationListener=null;
    private AMapLocationClientOption mLocationOption=null;*/
    private TextView tv_edit_location;
    private String des;
    private String picname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ib_edit_back = (ImageButton) findViewById(R.id.ib_edit_back);
        ib_edit_pic = (ImageButton) findViewById(R.id.ib_edit_pic);
        bt_edit_save = (Button) findViewById(R.id.bt_edit_save);
        et_edit_des = (EditText) findViewById(R.id.et_edit_des);
        tv_edit_location = (TextView) findViewById(R.id.tv_edit_location);
        /*图片显示*/
        iv_edit_pic1 = (ImageView) findViewById(R.id.iv_edit_pic1);
        iv_edit_pic2 = (ImageView) findViewById(R.id.iv_edit_pic2);
        iv_edit_pic3 = (ImageView) findViewById(R.id.iv_edit_pic3);

        /**
         * 定位初始化
         */
        /*初始化定位*//*
        //声明定位回调监听器
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
                       // amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                        amapLocation.getLatitude();//获取纬度
                        amapLocation.getLongitude();//获取经度
                        *//*amapLocation.getAccuracy();//获取精度信息
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(amapLocation.getTime());
                        df.format(date);//定位时间*//*

                        amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                        String country = amapLocation.getCountry();//国家信息
                        String province = amapLocation.getProvince();//省信息
                        String city = amapLocation.getCity();//城市信息
                        String district = amapLocation.getDistrict();//城区信息
                        String street = amapLocation.getStreet();//街道信息
                        *//*amapLocation.getStreetNum();//街道门牌号信息
                        amapLocation.getCityCode();//城市编码
                        amapLocation.getAdCode();//地区编码
                        amapLocation.getAoiName();//获取当前定位点的AOI信息*//*

                        tv_edit_location.setText(country+province+city+district+street);
                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                }
            }
        };
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(5000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();*/

        /**
         * 创建保存多媒体文件的目录
         */
        File dir = new File(Environment.getExternalStorageDirectory(),"oneday");
        if (!dir.exists()){
            dir.mkdirs();
            File imgDir = new File(dir,"pic");
            File audioDir = new File(dir,"audio");
            File videoDir = new File(dir,"video");
            imgDir.mkdir();
            audioDir.mkdir();
            videoDir.mkdir();
        }

        /**
         * back键的监听
         */
        ib_edit_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*mLocationClient.stopLocation();
                mLocationClient.onDestroy();*/
                finish();
            }
        });
        /**
         * 保存键的监听
         */
        bt_edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                des =  et_edit_des.getText().toString();
                if (mSelectPath.isEmpty()&&des.isEmpty())
                {
                    Toast.makeText(EditActivity.this, "没有输入内容", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    saveSelectedPic();
                    insertToDatabase();
                    Toast.makeText(EditActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    /*mLocationClient.stopLocation();
                    mLocationClient.onDestroy();*/
                    finish();
                }
            }
        });

        selectPic();
    }

    /**
     * 跳到系统图库选择图片
     */
    public void selectPic()
    {
        ib_edit_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*图片选择的模式：多张选择*/
                Intent intent = new Intent(EditActivity.this, MultiImageSelectorActivity.class);
                // 可以拍摄图片
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, showCamera);
                // 最多可选择3张
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
                // 选择模式
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                // 默认选择
                if (mSelectPath != null && mSelectPath.size() > 0) {
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
                }
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });
    }

    /**
     * 接受选择的图片数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                int picNum=0;
                for(String p: mSelectPath){
                    previewPic(p,picNum);
                    picNum++;
                }
            }
        }
    }

    /**
     * 选择图片后在编辑页面预览
     * @param path
     * @param picNum
     */
    private void previewPic(String path,int picNum) {
        switch (picNum)
        {
            case 0:
                BitmapUtils utils_0 = new BitmapUtils(EditActivity.this);
                utils_0.display(iv_edit_pic1,path);
                iv_edit_pic2.setVisibility(View.VISIBLE);
                break;
            case 1:
                BitmapUtils utils_1 = new BitmapUtils(EditActivity.this);
                utils_1.display(iv_edit_pic2,path);
                iv_edit_pic3.setVisibility(View.VISIBLE);
                break;
            case 2:
                BitmapUtils utils_2 = new BitmapUtils(EditActivity.this);
                utils_2.display(iv_edit_pic3, path);
                break;
        }
    }

    /**
     * 保存选择或者拍摄的图片添加到sd卡中
     */
    public void saveSelectedPic()
    {
        FileOutputStream fos = null;
        FileInputStream fis = null;
        for (int i=0;i<mSelectPath.size();i++)
        {
            String SD_path = Environment.getExternalStorageDirectory().getAbsolutePath();
            Date date = new Date();
            long time = date.getTime();
            picname = "pic" + time + ".jpg";
            picNameList.add(picname);
            try
            {
                 /*保存的目标路径*/
                File save_file = new File(SD_path + "/oneday/pic/" + "pic" + time + ".jpg");
                fos = new FileOutputStream(save_file);

                File res_file = new File(mSelectPath.get(i));
                fis = new FileInputStream(res_file);
                byte[] b = new byte[1024];
                int len=-1;
                while((len=fis.read(b))!=-1)
                {
                    fos.write(b,0,len);
                }
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try
        {
            if (fos!=null)
            {
                fos.close();
            }
            if (fis!=null)
            {
                fis.close();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 将数据添加到数据库
     */
    public void insertToDatabase()
    {
        String picpath_1="";
        String picpath_2="";
        String picpath_3="";
        int type = 0;

         /*将输入的文字取出来*/

        if (mSelectPath.size()!=0)
        {
            for (int i=0;i<mSelectPath.size();i++)
            {
                switch (i)
                {
                    case 0:
                        picpath_1=picNameList.get(i);
                        type=1;
                        break;
                    case 1:
                        picpath_2=picNameList.get(i);
                        type=2;
                        break;
                    case 2:
                        picpath_3=picNameList.get(i);
                        type=3;
                        break;
                }

                /*获取当前的时间*/
                /*yyyy.MM.dd hh:mm:ss  年月日时分秒*/
                long l = System.currentTimeMillis();
                Date date = new Date(l);
                String pattern = "yyyy.MM.dd";
                SimpleDateFormat format=new SimpleDateFormat(pattern);
                String currentdate = format.format(date);
                MediaDao mediaDao = new MediaDao(this);
                mediaDao.addMediaInfo(new MediaInfo(type,des,currentdate,picpath_1,picpath_2,picpath_3,"",""));
            }
        }
    }
}
