package com.april.oneday.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.april.oneday.R;
import com.april.oneday.service.MyRemindersService;
import com.april.oneday.service.MyRoutinesService;
import com.april.oneday.utils.PreUtils;

public class SplashActivity extends Activity {

	private RelativeLayout rl_splash_bg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);


		/**
		 * 开启服务
		 */

		Intent intent1 = new Intent(getApplicationContext(), MyRoutinesService.class);
		startService(intent1);

		Intent intent2 = new Intent(getApplicationContext(), MyRemindersService.class);
		startService(intent2);

		rl_splash_bg = (RelativeLayout) findViewById(R.id.rl_splash_bg);
		startAnim();
	}

	/**
	 * 开启动画
	 */
	private void startAnim() {

		//动画集合
		AnimationSet as = new AnimationSet(true);  //共享插补器？shareInterpolator
		//旋转
        /*RotateAnimation rotate = new RotateAnimation(0,360,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        rotate.setDuration(5000);
        rotate.setFillAfter(true);  //保持动画状态
		//缩放
        ScaleAnimation scale = new ScaleAnimation(0,1,0,1,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        scale.setDuration(5000);
        scale.setFillAfter(true);*/
		//透明度----渐变
        AlphaAnimation alpha = new AlphaAnimation(0,1);
        alpha.setDuration(5000);
        alpha.setFillAfter(true);

        /*as.addAnimation(rotate);
        as.addAnimation(scale);*/
        as.addAnimation(alpha);
		//设置动画监听
		as.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				//开始
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				//结束----跳转到新手引导页
				jumpNextPage();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				//重复
			}
		});

		rl_splash_bg.startAnimation(as);
	}

	/**
	 * 跳转至新手引导页
	 */
	private void jumpNextPage() {

		//判断之前有没有显示过新手引导页
		boolean is_userguide_showed =
				PreUtils.getBoolean(this,"is_userguide_showed",false);
		//点击了开始体验后---置为true

		/*//判断之前有没有显示过新手引导页
		boolean is_login_did =
				PreUtils.getBoolean(this,"is_login_did",false);*/


		if (is_userguide_showed) {
			//显示过--就不再进入了
			//再判断是否登陆过
			/*if (is_login_did) {*/
				startActivity(new Intent(SplashActivity.this, MainActivity.class));
			/*} else {
				startActivity(new Intent(SplashActivity.this, LoginActivity.class));
			}*/
		}else {
			//没显示过--进入
			startActivity(new Intent(SplashActivity.this, GuideActivity.class));
		}

		finish();
	}

}
