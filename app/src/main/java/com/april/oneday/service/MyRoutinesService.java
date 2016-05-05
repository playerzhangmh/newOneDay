package com.april.oneday.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.april.oneday.R;
import com.april.oneday.activity.MainActivity;
import com.april.oneday.activity.Routine_Create2;
import com.april.oneday.bean.RoutinesDetailInfo;
import com.april.oneday.dao.RoutinesDao;
import com.april.oneday.utils.ServiceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/18.
 */
public class MyRoutinesService extends IntentService {

    private static final String TAG = "MyRoutinesService";
    public static String SERVICENAME="com.april.oneday.service.MyRemindersService";
    private NotificationManager nm;
    private PendingIntent pi;
    private Notification builder;

    //设定int值,使得每次的标识不一样。
    int marker=0;

    //是否振动
    private int detail_vabrate;
    private List<RoutinesDetailInfo> outlist;

    @Override
    public void onTrimMemory(int level) {
        Log.v(TAG,"onTrimMemory");
        boolean proessRunning = ServiceUtils.isProessRunning(this, SERVICENAME);
        Log.v(TAG,proessRunning+"---");
        if(!proessRunning){
            startService(new Intent(MyRoutinesService.this,MyRemindersService.class));
        }
        super.onTrimMemory(level);
    }

    public MyRoutinesService() {

        super("MyRoutinesService");
    }

    @Override
    public void onCreate() {
        Log.v(TAG,"onCreate");

        //用一个集合去装已经通知过的details
        outlist = new ArrayList<>();
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        /**
         * IntentService会单独的创建worker线程来处理onHandleIntent()方法实现的代码，无需处理多线程问题
         */

        while (true) {

            try {
                //每隔1s获取一次当前时间
                Thread.sleep(7000);
                notification();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  通知栏通知消息--》调整后
     */

    public synchronized void notification(){

        //获取当前时间
        RoutinesDao routinesDao = new RoutinesDao(getApplicationContext());
        long l = System.currentTimeMillis();
        List<RoutinesDetailInfo> ringlist = routinesDao.getRinglist();
        if(ringlist.size()==0){
            outlist.clear();
        }else {
            for(int x=0;x<ringlist.size();x++){

                RoutinesDetailInfo routinesDetailInfo = ringlist.get(x);
                if(outlist.contains(routinesDetailInfo)){
                    continue;
                }

                /**
                 * 获取数据
                 */
                //获取通知栏标题--》日程提醒
                int detail_id = routinesDetailInfo.getDetail_id();
                String routine_title = routinesDao.getTitileById(detail_id);

                //获取通知栏信息--》日程提醒标签名
                String routine_tag = routinesDetailInfo.getIcon_name();

                //是否振动提示--》是否开启振动
                detail_vabrate = routinesDetailInfo.getDetail_vabrate();
                long[] vibrateInformation = getVibrateInformation();

                //获取提示音uri--》uri
                String detail_ringpath = routinesDetailInfo.getDetail_ringpath();
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + detail_ringpath);

                //获取通知栏图标--》即应用图标
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_oneday);

                /**
                 * 获取数据
                 */
                //1.获取NotificationManager
                nm = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

                //2.定义Notification
                Intent intent = new Intent(this, Routine_Create2.class);
                intent.putExtra("RoutinesDetailInfo",routinesDetailInfo);
                pi = PendingIntent.getActivity(getApplicationContext(),marker, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder = new Notification.Builder(getApplicationContext())
                        .setContentTitle("Oneday新日程提醒： " + routine_title)
                        .setContentText(routine_tag + "时间到了。")
                        .setLargeIcon(bitmap)
                        .setSmallIcon(R.drawable.icon_oneday)
                        //通知优先级
                  /*  .setPriority(Notification.PRIORITY_MAX)*/
                        .setAutoCancel(true)
                        .setSound(uri)
                        .setVibrate(vibrateInformation)
                        .setContentIntent(pi)
                        .setTicker("收到来自Oneday发来的新提醒~")
                        //设置内容下边的一段小文字
                        .setSubText("——拒绝拖延,及时行动")
                        .build();


                //当点击通知栏通知的时候，通知栏自动销毁
                builder.flags=Notification.FLAG_AUTO_CANCEL;

                nm.notify(3, builder);
                marker+=1;
                outlist.add(routinesDetailInfo);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //设置振动，用户开启振动的long数组和用户关闭振动的long数组
    public long[] getVibrateInformation(){

        long[] l=new long[6];

        if(detail_vabrate== RoutinesDao.VABRATE){

            //振动
            for(int x=0;x<6;x++){
                if (x == 0) {

                    l[x]=0;
                }else{
                    l[x]=1000l;
                }
            }

        }else{

            //不振动
            for(int y=0;y<6;y++){
                l[y]=0;
            }
        }

        return l;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG,"onStartCommand");
        IntentFilter filter=new IntentFilter();
        filter.addAction("reminder_want_recover");
        registerReceiver(receiver,filter);
        flags=START_STICKY;
        return super.onStartCommand(intent,flags,startId);
    }

    BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            startService(new Intent(MyRoutinesService.this,MyRemindersService.class));
        }
    };

    @Override
    public void onDestroy() {
        Intent recovery=new Intent();
        recovery.setAction("routine_want_recover");
        sendBroadcast(recovery);
        super.onDestroy();
    }
}
