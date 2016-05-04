package com.april.oneday.bean;

import android.util.Log;

import com.april.oneday.utils.MyAsycnTaks;

/**
 * Created by coins on 2016/4/24.
 */
public class Eweqwe extends MyAsycnTaks {
    private static final String TAG = "Eweqwe";
    int i=100000;
    @Override
    public void preTask() {
        while(i>0){
            i--;
            Log.v(TAG,"pre");
        }
    }

    @Override
    public void doinBack() {
            while(i>0){
                i--;
                Log.v(TAG,"doinBack");
            }
    }

    @Override
    public void postTask() {

    }
}
