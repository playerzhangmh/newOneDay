package com.april.oneday.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Fragment基类
 * 1.初始化布局
 * 2.初始化数据
 */
public abstract class BaseMenuFragment extends Fragment {

    public Activity mActivity;

    /**
     * Fragment被创建的时候调用
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    //初始化Fragment布局
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView();
        return view;

    }

    /**
     * Activity创建结束,初始化数据
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 初始化布局,子类必须实现
     */
    public abstract View initView();

    /**
     * 初始化数据,子类可以不实现
     */
    public void initData() {
    }


}

