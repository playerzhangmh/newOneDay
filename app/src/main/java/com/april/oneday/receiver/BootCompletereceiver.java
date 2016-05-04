package com.april.oneday.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.widget.Toast;

import com.april.oneday.service.MyRemindersService;
import com.april.oneday.service.MyRoutinesService;

/**
 * Created by coins on 2016/5/3.
 */
public class BootCompletereceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("onReceive",intent.getAction());
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            Toast.makeText(context,"为您服务哦，亲",Toast.LENGTH_SHORT).show();
            context.startService(new Intent(context,MyRemindersService.class));
            context.startService(new Intent(context,MyRoutinesService.class));

        }else if(intent.getAction().equals("android.intent.action.ACTION_SHUTDOWN")){
            //此处为关机导致通知提醒错过的bug预留
        }
    }
}
