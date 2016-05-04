package com.april.oneday.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by wancc on 2016/3/26.
 */
public class ScreenUtils {

    /**
     * 获取屏幕宽高
     * @param context
     * @return displayMetrics对象 包含屏幕宽高、密度、文字缩放的信息
     */
    public static DisplayMetrics getScreenSize(Context context){
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = wm.getDefaultDisplay();
        defaultDisplay.getMetrics(metrics);
        return metrics;
    }

    /**
     *
     * @param context
     * @return 屏幕密度
     */
    public static float getScreenDensity(Context context){
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.density;
    }


    /**
     * 把像素为单位的转为以dp为单位的
     * @param context
     * @param px
     * @return
     */
    public static int px2dp(Context context,int px){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px/scale+0.5f);
    }


    public static int dp2px(Context context,int dp){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp*scale+0.5f);
    }


    public static int px2sp(Context context,int px){
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px/scale+0.5f);
    }


    public static int sp2px(Context context,int sp){
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp*scale+0.5f);
    }


    public static int dp2pxByTV(Context context,int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,
                context.getResources().getDisplayMetrics());
    }

    public static int sp2pxByTV(Context context,int sp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,
                context.getResources().getDisplayMetrics());
    }


    /**
     *
     * @param context
     * @param y  父控件的location[1]，
     * @param parentHeight 父控件的高度
     * @param popview 要显示的控件
     * @return 给popview找到显示的合适位置（y方向的坐标），在父控件的上方或者下方显示
     */
    public static int getProperY(Context context, int y,int parentHeight, View popview) {
        DisplayMetrics screenSize = getScreenSize(context);
        int heightPixels = screenSize.heightPixels;

        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        popview.measure(w,h);

        int height =popview.getMeasuredHeight();

        if (y>heightPixels/2){
            //让popview在父控件的上面显示
            return y-height;
        }else{
            //让popview在父控件的下面显示
            return y+parentHeight;
        }
    }


    /**
     *
     * @param context
     * @param popview 要显示的控件
     * @return 让popview贴着屏幕的右侧（y方向的坐标）
     */
    public static int getProperX(Context context,  View popview) {
        DisplayMetrics screenSize = getScreenSize(context);
        int widthPixels = screenSize.widthPixels;

        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        popview.measure(w,h);
        int width =popview.getMeasuredWidth();

        //让popview显示在屏幕的右侧
        return widthPixels-width;
    }
}
