package com.april.oneday.base;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.april.oneday.R;
import com.april.oneday.activity.MainActivity;

/**
 * Created by wancc on 2016/4/20.
 */
public abstract class ScheduleBasePage {
    protected MainActivity mActivity;

    public View mRootView;
    private ImageView viewById;
    private ListView lv_schedule;

    public ScheduleBasePage(MainActivity mActivity) {
        this.mActivity = mActivity;

        mRootView=initView();
        initData();
    }

    protected abstract void initData() ;

    protected View initView(){
        View view = View.inflate(mActivity, R.layout.schedule_page, null);
        viewById = (ImageView) view.findViewById(R.id.iv_schedule_timeLine);
        lv_schedule = (ListView) view.findViewById(R.id.lv_schedule);

        return view;
    }




}
