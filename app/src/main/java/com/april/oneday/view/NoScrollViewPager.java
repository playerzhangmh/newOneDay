package com.april.oneday.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by wancc on 2016/4/11.
 */
public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;//super.onTouchEvent(ev);
    }


    /*
         * This method JUST determines whether we want to intercept the motion.
         * If we return true, onMotionEvent will be called and we do the actual
         * scrolling there.
         */
    //viewpager实际上如果要拦截的话，会直接在这里做拦截后的处理。所以如果禁用掉viewpager的移动 应该在这里和onTouchEvent 都返回false。
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;//super.onInterceptHoverEvent(event);
    }
}
