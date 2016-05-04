package com.april.oneday.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by coins on 2016/5/4.
 */
public class ServiceUtils {

    /**
     * 判断进程是否运行
     * @return
     */
    public static boolean isProessRunning(Context context, String serviceName) {

        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(10000);
        for (ActivityManager.RunningServiceInfo info : runningServices) {
            ComponentName service = info.service;
            String serviceComp = service.toString();
            String servicename = serviceComp.substring(serviceComp.indexOf("/") + 1, serviceComp.lastIndexOf("}"));
            Log.v("Service",service+"--"+servicename+"--");
            if (servicename.equals(serviceName)) {
                return true;
            }
        }

        return false;
    }
}
