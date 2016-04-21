package com.april.oneday.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.april.oneday.Page.BasePage;
import com.april.oneday.Page.Pic1Page;
import com.april.oneday.R;
import com.april.oneday.bean.MediaInfo;
import com.april.oneday.dao.MediaDao;
import com.lidroid.xutils.BitmapUtils;

import java.io.File;
import java.util.ArrayList;


public class FullImageActivity extends Activity {

    private ArrayList<BasePage> pagelist;
    private BitmapUtils bitmapUtils;
    private String[] pics;
    private ViewPager vp_fullimage_showpics;
    private MediaDao mediaDao;
    private File sd_path;
    private int type;
    private MediaInfo mediainfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullimage);

        bitmapUtils = new BitmapUtils(this);
        mediaDao = new MediaDao(this);
        sd_path = Environment.getExternalStorageDirectory().getAbsoluteFile();
        /*获取数据*/
        getIntentData();
        vp_fullimage_showpics = (ViewPager) findViewById(R.id.vp_fullimage_showpics);
        setViewpageAdapter();
    }

    private void setdeleteItemListener() {
        int currentItem = vp_fullimage_showpics.getCurrentItem();
        pagelist.get(currentItem).ib_pic1page_config
                .setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(FullImageActivity.this)
                                .setTitle("warning")
                                .setMessage("确认删除这条记录吗？")
                                .setCancelable(false)
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mediaDao.deleteMediaInfo(mediainfo);
                                        Toast.makeText(FullImageActivity.this, "已删除", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }).show();
                    }
                });
    }

    /**
     * 给viewpage添加适配器
     */
    private void setViewpageAdapter() {
        vp_fullimage_showpics.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return pagelist.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return object==view;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                BasePage page = pagelist.get(position);
                View pageview = page.getPageview();
                container.addView(pageview);
                return pageview;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public void finishUpdate(ViewGroup container) {
                super.finishUpdate(container);
                setdeleteItemListener();
            }
        });
    }

    /**
     * 获取Intent传来的数据
     */
    private void getIntentData() {
        //Intent intent = getIntent();
        //pics = intent.getStringArrayExtra("pic");

        mediainfo = (MediaInfo) getIntent().getSerializableExtra("mediainfo");
        type = mediainfo.getType();
        if (type >0&& type <4)
        {
            switch (type)
            {
                case 1:
                    pics=new String[]{mediainfo.getPic1()};
                    break;
                case 2:
                    pics=new String[]{mediainfo.getPic1(), mediainfo.getPic2()};
                    break;
                case 3:
                    pics=new String[]{mediainfo.getPic1(), mediainfo.getPic2(), mediainfo.getPic3()};
                    break;
            }
            pagelist = new ArrayList<>();

            for (int i = 0; i< pics.length; i++)
            {
                Pic1Page pic1Page = new Pic1Page(this);
                bitmapUtils.display(pic1Page.iv_pic1page_show,sd_path+"/oneday/pic/"+pics[i]+".jpg");
                pic1Page.tv_pic1page_date.setText(mediainfo.getDate());
                pic1Page.tv_fullimage_showtext.setText(mediainfo.getDesc());
                pagelist.add(pic1Page);
            }
        }
        else
        {
            pagelist = new ArrayList<>();
            Pic1Page pic1Page = new Pic1Page(this);
            pic1Page.iv_pic1page_show.setImageResource(R.drawable.default_fullimage);
            pic1Page.tv_pic1page_date.setText(mediainfo.getDate());
            pic1Page.tv_fullimage_showtext.setText(mediainfo.getDesc());
            pagelist.add(pic1Page);
        }
    }
}