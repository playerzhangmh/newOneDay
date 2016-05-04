package com.april.oneday.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.april.oneday.R;
import com.april.oneday.activity.EditActivity;
import com.april.oneday.activity.FullImageActivity;
import com.april.oneday.activity.MainActivity;
import com.april.oneday.adapter.TimeLineAdapter;
import com.april.oneday.bean.MediaInfo;
import com.april.oneday.dao.MediaDao;
import com.april.oneday.utils.CommenUtils;
import com.april.oneday.utils.MyAsycnTaks;
import com.april.oneday.utils.MyBitmapUtils;
import com.april.oneday.utils.WindowMeasureUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.makeramen.segmented.SegmentedRadioGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.bean.Image;

/**
 * Created by wangtongyu on 2016/4/18.
 */
public class TimeLineFragment extends Fragment {

    private static final int REQUEST_CODE_FULLIMAGE =100 ;
    private static final int REQUEST_CODE_EDIT =1000 ;
    MainActivity mActivity;
    private List<MediaInfo> list;
    private MediaDao dao;
    private TimeLineAdapter timeLineAdapter;
    private ListView lv_timeline;
    public List<Map<String, Bitmap>> bitmapList;

    //查询的总个数
    private final int MAXNUM=10;
    //起始位置
    private int startIndex = 0;
    private ImageButton ib_timeline;
    private String basepath;
    private int clickedItem;
    private ImageButton btnMenu;
    private SegmentedRadioGroup rg_option;
    private RadioButton rb_timeline;
    private TextView tv_app_slogan;
    private ImageView iv_appname;
    private ImageView iv_line;
    private List<MediaInfo> nextMediaInfo;
    private ImageButton btn_titalbar_menu;
    private WindowMeasureUtils utils;

    public TimeLineFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = new MediaDao(getActivity());
        mActivity = (MainActivity) getActivity();
        bitmapList = new ArrayList<>();
        utils = new WindowMeasureUtils();

        basepath=Environment.getExternalStorageDirectory().getAbsolutePath()+"/oneday/pic/";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_timeline, container,false);
        rg_option = (SegmentedRadioGroup) inflate.findViewById(R.id.rg_option);
        rb_timeline = (RadioButton) inflate.findViewById(R.id.rb_timeline);
        btnMenu = (ImageButton) inflate.findViewById(R.id.btn_titalbar_menu);
        lv_timeline = (ListView) inflate.findViewById(R.id.lv_timeline);
        /*找到背景的控件*/
        tv_app_slogan = (TextView) inflate.findViewById(R.id.tv_app_slogan);
        iv_appname = (ImageView) inflate.findViewById(R.id.iv_appname);
        iv_line = (ImageView) inflate.findViewById(R.id.iv_line);


        /*找到添加键*/
        ib_timeline = (ImageButton) inflate.findViewById(R.id.ib_timeline);
        /*找到菜单键*/
        btn_titalbar_menu = (ImageButton) inflate.findViewById(R.id.btn_titalbar_menu);

        lv_timeline.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //获取界面显示最后一个条目
                    int position = lv_timeline.getLastVisiblePosition();//获取界面显示最后一个条目,返回的时候条目的位置
                    //判断是否是查询数据的最后一个数据  20   0-19
                    if (position == list.size()-1) {
                        //加载下一波数据
                        //更新查询的其实位置   0-19    20-39
                        startIndex+=MAXNUM;
                        fillData();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        fillData();

        lv_timelineOscroll();
        rgOptionOnclick();
        initSlidingMenu();
        btnMenuOnClick();
        InitEvent();

        return inflate;
    }



    /**
     * 菜单按钮点击事件,展开关闭侧边栏
     */
    private void btnMenuOnClick() {
        this.btnMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramView) {
                mActivity.getSlidingMenu().toggle();
            }

        });
    }

    /**
     * listView滑动监听
     */

    private void lv_timelineOscroll() {
        lv_timeline.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //获取界面显示最后一个条目
                    int position = lv_timeline.getLastVisiblePosition();//获取界面显示最后一个条目,返回的时候条目的位置
                    //判断是否是查询数据的最后一个数据  20   0-19
                    if (position == list.size()-1) {
                        //加载下一波数据
                        //更新查询的其实位置   0-19    20-39
                        startIndex+=MAXNUM;
                        fillData();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }


    /**
     * radiogroup点击监听
     */
    private void rgOptionOnclick() {
        rg_option.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup paramRadioGroup, int paramInt) {
                mActivity.showSchedule();
                initSlidingMenu();
            }
        });
    }

    /**
     * 初始化侧边栏
     */
    /**
     * 初始化侧边栏
     */
    public void initSlidingMenu() {
        SlidingMenu menu = mActivity.getSlidingMenu();
        mActivity.setBehindContentView(R.layout.left_menu);
        menu.setBehindOffset(200);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        //和Fragment绑定
        FragmentManager fm = mActivity.getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_left_menu, new TimeLineMenuFragment());
        transaction.commit();
    }


    /**
     * fragment隐藏显示状态监听
     */
    public void onHiddenChanged(boolean hidden)
    {

        if (this.rb_timeline != null){
            rb_timeline.toggle();
        }

    }

    /**
     * FillData初始化数据
     */
    //初始化数据
    public void fillData() {

        new MyAsycnTaks() {

            @Override
            public void preTask() {
                //显示进度条

            }

            //后台处理的事情
            @Override
            public void doinBack() {
                nextMediaInfo = new ArrayList<MediaInfo>();
                //从数据库查询数据
                if (list == null) {
                    list = dao.queryPartMediaInfo(MAXNUM, startIndex);

                    if (list.size()>0){
                        for (int i=startIndex;i<list.size();i++) {

                            MediaInfo mediaInfo = list.get(i);
                            HashMap<String, Bitmap> map = new HashMap<String, Bitmap>();

                            if (!TextUtils.isEmpty(mediaInfo.getPic1())) {
                                map.put("pic1", MyBitmapUtils.decodeSampledBitmapFromSd(
                                        CommenUtils.getPathByName(mediaInfo.getPic1()),(int)WindowMeasureUtils.px2dp(200,mActivity), (int)WindowMeasureUtils.px2dp(200,mActivity)));
                            }
                            if (!TextUtils.isEmpty(mediaInfo.getPic2())) {
                                map.put("pic2", MyBitmapUtils.decodeSampledBitmapFromSd(
                                        CommenUtils.getPathByName(mediaInfo.getPic2()), (int)WindowMeasureUtils.px2dp(150,mActivity), (int)WindowMeasureUtils.px2dp(150,mActivity)));
                            }
                            if (!TextUtils.isEmpty(mediaInfo.getPic3())) {
                                map.put("pic3", MyBitmapUtils.decodeSampledBitmapFromSd(
                                        CommenUtils.getPathByName(mediaInfo.getPic3()), (int)WindowMeasureUtils.px2dp(100,mActivity), (int)WindowMeasureUtils.px2dp(100,mActivity)));
                            }
                            bitmapList.add(map);
                        }
                    }
                } else {

                    nextMediaInfo = dao.queryPartMediaInfo(MAXNUM, startIndex);
                    list.addAll(nextMediaInfo);
                    if (nextMediaInfo.size()>0){
                        //已经查询到下一波数据
                        for (int i=startIndex;i<list.size();i++) {

                            MediaInfo mediaInfo = list.get(i);
                            HashMap<String, Bitmap> map = new HashMap<String, Bitmap>();

                            if (!TextUtils.isEmpty(mediaInfo.getPic1())) {
                                map.put("pic1", MyBitmapUtils.decodeSampledBitmapFromSd(
                                        CommenUtils.getPathByName(mediaInfo.getPic1()), (int)WindowMeasureUtils.px2dp(200,mActivity), (int)WindowMeasureUtils.px2dp(200,mActivity)));
                            }
                            if (!TextUtils.isEmpty(mediaInfo.getPic2())) {
                                map.put("pic2", MyBitmapUtils.decodeSampledBitmapFromSd(
                                        CommenUtils.getPathByName(mediaInfo.getPic2()), (int)WindowMeasureUtils.px2dp(150,mActivity), (int)WindowMeasureUtils.px2dp(150,mActivity)));
                            }
                            if (!TextUtils.isEmpty(mediaInfo.getPic3())) {
                                map.put("pic3", MyBitmapUtils.decodeSampledBitmapFromSd(
                                        CommenUtils.getPathByName(mediaInfo.getPic3()), (int)WindowMeasureUtils.px2dp(100,mActivity), (int)WindowMeasureUtils.px2dp(100,mActivity)));
                            }
                            bitmapList.add(map);
                        }
                    }


                }
/*                int realSize=0;
                //把图片加载到内存?
                if (list.size()>0){
                    if(list.size()<MAXNUM){
                        realSize = list.size();
                        Log.i("realSize",realSize+"");
                    }else {
                        realSize = startIndex+nextMediaInfo.size();
                        Log.i("realSize",realSize+"");
                    }
                    for (int i=startIndex;i<realSize;i++) {

                        MediaInfo mediaInfo = list.get(i);
                        HashMap<String, Bitmap> map = new HashMap<String, Bitmap>();

                        if (!TextUtils.isEmpty(mediaInfo.getPic1())) {
                            map.put("pic1", MyBitmapUtils.decodeSampledBitmapFromSd(
                                    CommenUtils.getPathByName(mediaInfo.getPic1()), 300, 300));
                        }
                        if (!TextUtils.isEmpty(mediaInfo.getPic2())) {
                            map.put("pic2", MyBitmapUtils.decodeSampledBitmapFromSd(
                                    CommenUtils.getPathByName(mediaInfo.getPic1()), 200, 200));
                        }
                        if (!TextUtils.isEmpty(mediaInfo.getPic3())) {
                            map.put("pic3", MyBitmapUtils.decodeSampledBitmapFromSd(
                                    CommenUtils.getPathByName(mediaInfo.getPic1()), 100, 100));
                        }
                        bitmapList.add(map);
                    }
                }*/

            }

            @Override
            public void postTask() {

                //给listView设置适配器
                if (timeLineAdapter == null) {
                    timeLineAdapter = new TimeLineAdapter(list,bitmapList, mActivity);
                    lv_timeline.setAdapter(timeLineAdapter);
                } else {
                    timeLineAdapter.notifyDataSetChanged();
                }

                if (list!=null)
                {
                    if (list.size()==0)
                    {
                        hideline();
                    }
                    else
                    {
                        showline();
                    }
                }
                else
                {
                    hideline();
                }
            }
        }.execute();
    }


    /**
     * 设置add按钮的点击事件和listview的Item点击事件
     */
    public void InitEvent()
    {
        ib_timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mActivity,EditActivity.class),REQUEST_CODE_EDIT);
            }
        });

        lv_timeline.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickedItem =position;
                Intent intent = new Intent(mActivity, FullImageActivity.class);

                MediaInfo info = list.get(position);

                Bundle bundle = new Bundle();
                bundle.putSerializable("mediainfo",info);
                intent.putExtras(bundle);
                startActivityForResult(intent,REQUEST_CODE_FULLIMAGE);
            }
        });

        btn_titalbar_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SlidingMenu slidingMenu = mActivity.getSlidingMenu();
                slidingMenu.toggle();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode==500)
        {
            MediaInfo currentinfo = list.get(clickedItem);
            int type = -1;
            if(currentinfo!=null){
                type = currentinfo.getType();
            }



            switch(type)
            {
                case 0:
                    break;
                case 1:
                    String pic1 = currentinfo.getPic1();
                    deletepic(pic1);
                    break;
                case 2:
                    String pic11 = currentinfo.getPic1();
                    String pic2 = currentinfo.getPic2();
                    deletepic(pic11);
                    deletepic(pic2);
                    break;
                case 3:
                    String pic12 = currentinfo.getPic1();
                    String pic21 = currentinfo.getPic2();
                    String pic3 = currentinfo.getPic3();
                    deletepic(pic12);
                    deletepic(pic21);
                    deletepic(pic3);
                    break;
            }
            list.remove(clickedItem);
            bitmapList.remove(clickedItem);
            timeLineAdapter.notifyDataSetChanged();
            if (list.size()==0)
            {
                hideline();
            }
        }
        else if (requestCode==1000)
        {

            startIndex=0;
            list.clear();
            list = null;
            bitmapList.clear();
            timeLineAdapter=null;
            fillData();
        }
    }

    public void deletepic(String path)
    {
        File file = new File(basepath + path);
        file.delete();
    }

    private void hideline() {
        iv_line.setVisibility(View.INVISIBLE);
        iv_appname.setAlpha(1f);
        tv_app_slogan.setAlpha(1f);
    }

    private void showline() {
        iv_appname.setAlpha(0.1f);
        tv_app_slogan.setAlpha(0.1f);
        iv_line.setVisibility(View.VISIBLE);
    }
}
