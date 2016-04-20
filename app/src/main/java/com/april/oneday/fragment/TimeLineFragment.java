package com.april.oneday.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;

import com.april.oneday.R;
import com.april.oneday.activity.MainActivity;
import com.april.oneday.adapter.TimeLineAdapter;
import com.april.oneday.bean.MediaInfo;
import com.april.oneday.dao.MediaDao;
import com.april.oneday.utils.MyAsycnTaks;

import java.util.List;

/**
 * Created by wangtongyu on 2016/4/18.
 */
public class TimeLineFragment extends Fragment {

    MainActivity mActivity;
    private List<MediaInfo> list;
    private MediaDao dao;
    private TimeLineAdapter timeLineAdapter;
    private ListView lv_timeline;

    //查询的总个数
    private final int MAXNUM=10;
    //起始位置
    private int startIndex = 0;
    private Button btn_schedule;

    public TimeLineFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dao = new MediaDao(getActivity());
        mActivity = (MainActivity) getActivity();



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View inflate = inflater.inflate(R.layout.fragment_timeline, container,false);
        lv_timeline = (ListView) inflate.findViewById(R.id.lv_timeline);
        fillData();
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

        btn_schedule = (Button) inflate.findViewById(R.id.btn_schedule);

        btnScheduleOnclick();

        return inflate;
    }



    //初始化数据
    public void fillData(){
        new MyAsycnTaks(){

            @Override
            public void preTask() {

            }

            @Override
            public void doinBack() {
                //从数据库查询数据
                if (list==null){
                    list = dao.queryPartMediaInfo(MAXNUM, startIndex);
                }else {
                    list.addAll(dao.queryPartMediaInfo(MAXNUM,startIndex));
                }

            }

            @Override
            public void postTask() {
                //给listView设置适配器
                if (timeLineAdapter==null){
                    timeLineAdapter = new TimeLineAdapter(list,mActivity);
                    lv_timeline.setAdapter(timeLineAdapter);
                } else {
                    timeLineAdapter.notifyDataSetChanged();
                }

            }
        }.execute();
    }


    private void btnScheduleOnclick(){
        btn_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //让mActivity切换布局
                mActivity.showSchedule();
            }
        });
    }
}
