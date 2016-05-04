package com.april.oneday.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import java.util.Timer;
import java.util.TimerTask;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class EditActivity extends Activity {

    private Button bt_edit_back;
    private Button bt_edit_save;
    private ArrayList<String> mSelectPath=new ArrayList<>();
    private ArrayList<String> picNameList=new ArrayList<>();

    private static final int REQUEST_IMAGE = 2;
    boolean showCamera=true;
    int maxNum=3;
    private ImageButton ib_edit_pic1;
    private ImageButton ib_edit_pic2;
    private ImageButton ib_edit_pic3;
    private EditText et_edit_des;

    protected String des;
    private String picname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        bt_edit_back = (Button) findViewById(R.id.bt_edit_back);
        bt_edit_save = (Button) findViewById(R.id.bt_edit_save);
        et_edit_des = (EditText) findViewById(R.id.et_edit_des);
        /*图片显示*/
        ib_edit_pic1 = (ImageButton) findViewById(R.id.ib_edit_pic1);
        ib_edit_pic2 = (ImageButton) findViewById(R.id.ib_edit_pic2);
        ib_edit_pic3 = (ImageButton) findViewById(R.id.ib_edit_pic3);


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
        bt_edit_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    finish();
                }
            }
        });

        selectPic();
    }

    /**
     * 弹出键盘
     */
    @Override
    protected void onResume() {
        super.onResume();
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
                       {

                           public void run()
                           {
                               InputMethodManager inputManager =
                                       (InputMethodManager)et_edit_des.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(et_edit_des, 0);
                           }

                       },
                200);
    }

    /**
     * 跳到系统图库选择图片
     */
    public void selectPic()
    {
        ib_edit_pic1.setOnClickListener(new View.OnClickListener() {
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
                utils_0.display(ib_edit_pic1,path);
                ib_edit_pic2.setVisibility(View.VISIBLE);
                break;
            case 1:
                BitmapUtils utils_1 = new BitmapUtils(EditActivity.this);
                utils_1.display(ib_edit_pic2,path);
                ib_edit_pic3.setVisibility(View.VISIBLE);
                break;
            case 2:
                BitmapUtils utils_2 = new BitmapUtils(EditActivity.this);
                utils_2.display(ib_edit_pic3, path);
                break;
        }
    }

    /**
     * 保存选择或者拍摄的图片添加到sd卡中
     */
    public void saveSelectedPic()
    {
        if (!mSelectPath.isEmpty())
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
        else
        {
            return;
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
        MediaDao mediaDao = null;
        String currentdate ="";

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
            }
        }
        /*获取当前的时间*/
        /*yyyy.MM.dd hh:mm:ss  年月日时分秒*/
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        String pattern = "yyyy.MM.dd";
        SimpleDateFormat format=new SimpleDateFormat(pattern);
        currentdate = format.format(date);
        mediaDao =  new MediaDao(this);
        mediaDao.addMediaInfo(new MediaInfo(type,des,currentdate,picpath_1,picpath_2,picpath_3,"",""));
    }
}
