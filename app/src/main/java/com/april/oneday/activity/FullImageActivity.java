package com.april.oneday.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
        /**
         *  ViewPager.OnPageChangeListener
         */
        vp_fullimage_showpics.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                pagelist.get(position).ib_pic1page_config.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(FullImageActivity.this,"已删除", Toast.LENGTH_SHORT).show();
                        int type=pics.length-1;
                        switch (position)
                        {
                            case 0:
                                mediaDao.deleteMediaInfo(new MediaInfo(type,"","",pics[position],"","","",""));
                                pagelist.remove(position);
                                setViewpageAdapter();
                                break;
                            case 1:
                                mediaDao.deleteMediaInfo(new MediaInfo(type,"","",pics[position],"","","",""));
                                pagelist.remove(position);
                                setViewpageAdapter();
                                break;
                            case 2:
                                mediaDao.deleteMediaInfo(new MediaInfo(type,"","",pics[position],"","","",""));
                                pagelist.remove(position);
                                setViewpageAdapter();
                                break;
                        }
                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
        });
    }

    /**
     * 获取Intent传来的数据
     */
    private void getIntentData() {
        Intent intent = getIntent();
        pics = intent.getStringArrayExtra("pic");

        pagelist = new ArrayList<>();

        for (int i = 0; i< pics.length-1; i++)
        {
            Pic1Page pic1Page = new Pic1Page(this);
            bitmapUtils.display(pic1Page.iv_pic1page_show,sd_path+"/oneday/pic/"+pics[i]+".jpg");
            pic1Page.tv_pic1page_date.setText(pics[pics.length-1]);
            pagelist.add(pic1Page);
        }
    }
}
