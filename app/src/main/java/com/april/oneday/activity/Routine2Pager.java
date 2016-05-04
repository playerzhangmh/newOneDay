package com.april.oneday.activity;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.april.oneday.R;
import com.april.oneday.adapter.RoutAdapter;
import com.april.oneday.bean.RoutinesDetailInfo;

import java.util.List;

/**
 * Created by wangtongyu on 2016/4/21.
 */
public class Routine2Pager {
    public Activity mActivity;
    // 菜单详情页根布局
    public View mRootView;
    private ListView mListView;


    List<RoutinesDetailInfo> list;
    private RoutAdapter mRoutAdapter;

    public Routine2Pager(Activity activity, List<RoutinesDetailInfo> list) {
        this.list = list;
        mActivity = activity;
        mRootView = initView();

    }

    public View initView(){
        mRootView =View.inflate(mActivity, R.layout.viewpager_routine2,null);
        mListView = (ListView) mRootView.findViewById(R.id.lv_viewpager);


        initData();

        return mRootView;
    }

    public void initData() {

        //初始化listView,给listView设置Adapter
        mRoutAdapter = new RoutAdapter(mActivity, list);
        mListView.setAdapter(mRoutAdapter);

        //设置条目点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("Item","被点击了");
                if (mRoutAdapter.popupWindow!=null){
                    mRoutAdapter.popupWindow.dismiss();
                }

            }
        });

        mListView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (mRoutAdapter.popupWindow!=null){
                    mRoutAdapter.popupWindow.dismiss();
                }
            }
        });


    }

    //刷新提醒
    public void notifylistview(List<RoutinesDetailInfo> infos){
        list=infos;
        mRoutAdapter.notifyDataSetChanged();
    }


}
