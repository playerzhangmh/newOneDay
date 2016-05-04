package com.april.oneday.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.april.oneday.R;
import com.april.oneday.utils.PreUtils;

import java.util.ArrayList;

public class GuideActivity extends Activity {

	private static final int[] mImageId =
			new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};

	private ArrayList<ImageView> mImageViewList;

	private ViewPager vp_guide;
	private LinearLayout ll_point_group;       //引导圆点的父控件
	private int mPointWidth;      //圆点距离
	private View view_red_point;
	private Button btnStart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //去掉引导页的标题---并且一定是在这个位置
		setContentView(R.layout.activity_guide);

		vp_guide = (ViewPager) findViewById(R.id.vp_guide);
		ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);
		view_red_point = findViewById(R.id.view_red_point);

		btnStart = (Button) findViewById(R.id.start);
		btnStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//更新sp
				PreUtils.setBoolean(GuideActivity.this,"is_userguide_showed",true);

				//跳转到登录、finish掉现在这个页面
				startActivity(new Intent(GuideActivity.this, LoginActivity.class));
				finish();
			}
		});

		initViews();

		vp_guide.setAdapter(new GuideAdapter());  //PagerAdapter

		//监听页面、红色圆点跟着变化
		vp_guide.setOnPageChangeListener(new MyGuideChangeListener());     //OnPageChangeListener
	}

	/**
	 * 数据适配器
	 * 初始化界面
	 */
	private void initViews() {

		mImageViewList = new ArrayList<ImageView>();

		//初始化引导页3个页面
		for(int i=0;i<mImageId.length;i++) {
			ImageView iv = new ImageView(this);
			//设置引导页背景
			iv.setImageResource(mImageId[i]);
			mImageViewList.add(iv);
		}

		//初始化页的小圆点
		for(int i=0;i<mImageId.length;i++) {
			View point = new View(this);
			point.setBackgroundResource(R.drawable.shape_point_light);
			//圆点的尺寸大小设置-------像素单位px
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
			if (i>0) {
				//圆点之间 做一段距离
				params.leftMargin = 10;
			}
			point.setLayoutParams(params);
			ll_point_group.addView(point);  //圆点添加给线性布局
		}
        /*
        //圆点之间的距离---0---oncreate里、还没绘制出来
        int width = ll_point_group.getChildAt(1).getLeft()-
                ll_point_group.getChildAt(0).getLeft();
        */
		//获取视图树---全局layout监听
		ll_point_group.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				//当layout执行结束后回调此方法--到了这个方法就说明layout执行完成、为了防止他一直回调、remove掉
				//这里的this是OnGlobalLayoutListener
				ll_point_group.getViewTreeObserver().removeOnGlobalLayoutListener(this);

				//圆点之间的距离---layout已绘制
				mPointWidth = ll_point_group.getChildAt(1).getLeft()-
						ll_point_group.getChildAt(0).getLeft();
			}
		});
	}

	class GuideAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mImageId.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			//初始化页面
			container.addView(mImageViewList.get(position));

			return mImageViewList.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			//删除
			container.removeView((View) object);
			//super.destroyItem(container, position, object);
		}
	}

	/**
	 * ViewPager的滑动监听
	 */
	private class MyGuideChangeListener implements ViewPager.OnPageChangeListener {
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			//滑动变化--positionOffset偏移量---positionOffsetPixels滑动像素---页面滑动时数据变化
			int len = (int) (mPointWidth * positionOffset) + position * mPointWidth;     //圆点移动距离
			//ViewGroup.LayoutParams layoutParams = view_red_point.getLayoutParams();
			//获取当前的布局参数
			RelativeLayout.LayoutParams layoutParams =
					(RelativeLayout.LayoutParams) view_red_point.getLayoutParams();
			layoutParams.leftMargin = len;
			view_red_point.setLayoutParams(layoutParams);   //重新给小红点设置布局参数
		}

		@Override
		public void onPageSelected(int position) {
			//页面被选中
			if (position==mImageId.length-1) {
				//最后一个页面
				btnStart.setVisibility(View.VISIBLE);   //显示开始体验按钮
			}else {
				btnStart.setVisibility(View.INVISIBLE);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			//状态变化
		}
	}
}
