package com.april.oneday.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import java.lang.reflect.Field;

/**
 * Created by coins on 2016/3/30.
 */
public class WindowMeasureUtils {
    public static int[] getWindowMeasure(Context context){
        int[] measure=new int[2];
        WindowManager systemService = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = systemService.getDefaultDisplay();
        DisplayMetrics metrics=new DisplayMetrics();
        defaultDisplay.getMetrics(metrics);
        measure[0]=metrics.widthPixels;
        measure[1]=metrics.heightPixels;
        return measure;
    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            Log.d("hw2", "get status bar height fail");
            e1.printStackTrace();
            return 75;
        }
    }

    public static int getTitleBarHeight(Context context,Activity activity){
        int contentTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
//statusBarHeight是上面状态栏的高度
        int titleBarHeight = contentTop - getStatusBarHeight(context);
        return titleBarHeight;
    }


    //利用基准px和当前屏幕的密度来计算出不同屏幕分辨率下的控件所占大小
    public static float px2dp(int px,Context context){
        WindowManager systemService = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = systemService.getDefaultDisplay();
        DisplayMetrics metrics=new DisplayMetrics();
        defaultDisplay.getMetrics(metrics);
        float density = metrics.density;
        return density*px;
    }
}
