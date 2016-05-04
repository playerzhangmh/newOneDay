package com.april.oneday.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.april.oneday.R;
import com.april.oneday.activity.AboutOurActivity;
import com.april.oneday.activity.GuideActivity;
import com.april.oneday.activity.UserInfoSettingActivity;
import com.april.oneday.utils.MyBitmapUtils;
import com.april.oneday.utils.PrefUtils;
import com.april.oneday.view.RoundImageView;

import java.io.File;

/**
 * Created by wangtongyu on 2016/4/20.
 */
public class TimeLineMenuFragment extends BaseMenuFragment {


    private RoundImageView riv_leftnenu_headicon;
    private Bitmap bitmap;
    private TextView tv_leftmenu_nickname;

    @Override
    public View initView() {

        View view = View.inflate(getActivity(),R.layout.slidingmenu_timeline,null);
        //头像显示设置
        riv_leftnenu_headicon = (RoundImageView) view.findViewById(R.id.riv_leftnenu_headicon);
        setHeadIcon ();


        //昵称显示设置
        tv_leftmenu_nickname = (TextView) view.findViewById(R.id.tv_leftmenu_nickname);
        setNickname ();

        //头布局点击事件
        View headerUserInfo = (View) view.findViewById(R.id.header_userinfo);
        headerUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mActivity, UserInfoSettingActivity.class);
                startActivityForResult(intent,4568);
            }
        });

        LinearLayout tv_leftmenu_userinfo = (LinearLayout) view.findViewById(R.id.tv_leftmenu_userinfo);
        LinearLayout tv_leftmenu_help = (LinearLayout) view.findViewById(R.id.tv_leftmenu_help);
        LinearLayout tv_leftmenu_aboutus = (LinearLayout) view.findViewById(R.id.tv_leftmenu_aboutus);
        LinearLayout tv_leftmenu_telltous = (LinearLayout) view.findViewById(R.id.tv_leftmenu_telltous);

        tv_leftmenu_userinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, UserInfoSettingActivity.class);
                startActivityForResult(intent,4568);
            }
        });
        tv_leftmenu_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //重新进入GuideSplash
                startActivity(new Intent(mActivity,GuideActivity.class));
            }
        });
        tv_leftmenu_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到关于我们页面
                startActivity(new Intent(mActivity, AboutOurActivity.class));
            }
        });
        tv_leftmenu_telltous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到意见反馈页面
            }
        });

        return view;
    }

    public void setHeadIcon (){
        File headIcon = new File(getActivity().getFilesDir(), "headIcon.jpg");
        Bitmap bitmap = null;
        if (headIcon.exists()){
            bitmap = MyBitmapUtils.decodeSampledBitmapFromSd(headIcon.getAbsolutePath(), 200, 200);
        }

        if (bitmap!=null){
            riv_leftnenu_headicon.setImageBitmap(bitmap);
        }
    }
    public void setNickname (){
        tv_leftmenu_nickname.setText(PrefUtils.getString("nickname","点击设置头像",getActivity()));
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setHeadIcon();
        setNickname();
    }
}
