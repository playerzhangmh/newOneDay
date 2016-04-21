package com.april.oneday.Page;

import android.view.View;

import com.april.oneday.activity.FullImageActivity;





/**
 * Created by hs on 2016/4/19.
 */
public class Pic1Page extends BasePage {

    public Pic1Page(FullImageActivity fullImageActivity) {
        super(fullImageActivity);
    }

    @Override
    public void InitData() {
        super.InitData();
    }

    @Override
    public void InitEvent() {
        super.InitEvent();
        ib_pic1page_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FIA.finish();
            }
        });
    }
}
