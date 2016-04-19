package com.april.oneday.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.april.oneday.R;
import com.april.oneday.activity.MainActivity;


/**
 * Created by wangtongyu on 2016/4/18.
 */
public class TimeLineFragment extends Fragment {

    MainActivity mActivity;


    public TimeLineFragment() {
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_timeline,container,false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
