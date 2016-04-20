package com.april.oneday.Page;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.april.oneday.R;
import com.april.oneday.activity.FullImageActivity;


/**
 * Created by hs on 2016/4/19.
 */
public class BasePage
{
    public View pageview;
    public FullImageActivity FIA;
    public ImageButton ib_pic1page_back;
    public ImageButton ib_pic1page_share;
    public ImageButton ib_pic1page_config;
    public ImageView iv_pic1page_show;
    public TextView tv_pic1page_date;

    public BasePage(FullImageActivity fullImageActivity)
    {
        this.FIA=fullImageActivity;
        pageview =InitView();
        InitData();
        InitEvent();
    }

    public  View InitView()
    {
        View pageview = View.inflate(FIA, R.layout.picpage, null);

        ib_pic1page_back = (ImageButton) pageview.findViewById(R.id.ib_pic1page_back);
        ib_pic1page_share = (ImageButton) pageview.findViewById(R.id.ib_pic1page_share);
        ib_pic1page_config = (ImageButton) pageview.findViewById(R.id.ib_pic1page_config);
        iv_pic1page_show = (ImageView) pageview.findViewById(R.id.iv_pic1page_show);
        tv_pic1page_date = (TextView) pageview.findViewById(R.id.tv_pic1page_date);

        return  pageview;
    }

    public void InitData(){}
    public void InitEvent()
    {

    }

    public View getPageview()
    {
        return pageview;
    }
}
