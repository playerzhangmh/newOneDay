package com.april.oneday.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.april.oneday.R;
import com.april.oneday.activity.MainActivity;


/**
 * Created by wangtongyu on 2016/4/18.
 */
public class ScheduleFragment extends Fragment {

    MainActivity mActivity;
    private Button btn_timeline;


    public ScheduleFragment() {
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_schedule,container,false);
        btn_timeline = (Button) view.findViewById(R.id.btn_timeline);

        btnTimeLineOnclick();
        return view;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    private void btnTimeLineOnclick(){
        btn_timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //让mActivity切换布局
                MainActivity mMainUI = (MainActivity) getActivity();

                mMainUI.showTimeline();
            }
        });
    }
}
