package com.april.oneday.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.april.oneday.bean.RoutinesDetailInfo;
import com.april.oneday.bean.RoutinesInfo;
import com.april.oneday.bean.RoutinesProcessInfo;
import com.april.oneday.db.RoutinesDatadb;
import com.april.oneday.utils.ReminderUtis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coins on 2016/4/20.
 */
public class RoutinesDao {
    private static final int VERSION = 1;
    public static final int SCHEDULE_INACTIVE =0 ;
    public static final int SCHEDULE_ACTIVE =1 ;
    private static final String TAG = "RoutinesDao";
    public Context context;


    public RoutinesDao(Context context) {
        this.context=context;
    }

    private SQLiteDatabase getRoutinedatebases(Context context) {
        SQLiteDatabase routinesdb;RoutinesDatadb helper=new RoutinesDatadb(context,"routines.db",null,VERSION);
        routinesdb = helper.getReadableDatabase();
        return routinesdb;
    }

    //1，插入计划数据计划
    /*String createScheduleProcess="create table process (process_id integer,s_id integer primary key," +
                "constraint s_id_FK foreign key(s_id) references schedule(schedule_id)," +
                "process_active integer,process_start datetime,process_end datetime,process_time varchar(30));";*/
    public void insertSchedule(String title,int cycles){
        SQLiteDatabase routinesdb = getRoutinedatebases(context);
        ContentValues values=new ContentValues();
        values.put("schedule_title",title);
        values.put("schedule_cycles",cycles);
        values.put("schedule_active",SCHEDULE_INACTIVE);
        values.put("schedule_process",1);
        routinesdb.insert("schedule",null,values);
        //然后根据天数，往进程表中插入等同条数数据
        //获取其id
        Cursor cursor = routinesdb.rawQuery("select schedule_id from schedule where schedule_title='" + title + "';", null);
        cursor.moveToNext();
        int _id = cursor.getInt(0);
        Log.v(TAG,_id+"");
        int j=1;
        for(int i=0;i<cycles;i++){

            String insertprocess="insert into process values("+j+","+_id+","+SCHEDULE_INACTIVE+",'1900-01-01 00:00:00','1900-01-01 00:00:00','0');";
            j++;
            routinesdb.execSQL(insertprocess);
        }
        if(routinesdb!=null){
            routinesdb.close();
        }
    }
    //2，插入事件详情表

    /*"create table details (detail_id integer primary key autoincrement,p_id integer promary key," +
                "constraint p_id_FK foreign key(p_id) references process(process_id)," +
                "iconRid integer,icon_name varchar(50),detail_start time,detail_end time,detail_comment varchar(100)," +
                "detail_ringbefore varchar(30),detail_ringpath varchar(30),detail_vabrate integer);";*/
    /*
    * 插入事件要根据是在哪一天，若不是最后一天，则可以跨天插入数据，若是某个数据事件1跨了两天，则，另一天的事件插入必须从1的终止时间开始
    * 同理，后一件事的起始时间必须在前一件事的终止时间之后
    * */
    public void insertDetails(int p_id,int s_id,int iconRid,String icon_name,String detail_start,String detail_end,String detail_comment,
                              String detail_ringbefore,String detail_ringpath,int detail_vabrate){
        SQLiteDatabase routinesdb = getRoutinedatebases(context);
        String insertdetail="insert into details (p_id,s_id,iconRid,icon_name,detail_start,detail_end,detail_comment" +
                ",detail_ringbefore,detail_ringpath,detail_vabrate) values("+p_id+","+s_id+","+iconRid+",'"+icon_name+"'," +
                "'"+detail_start+"','"+detail_end+"','"+detail_comment+"','"+detail_ringbefore+"','"+detail_ringpath+"',"+detail_vabrate+")";
        routinesdb.execSQL(insertdetail);
        if(routinesdb!=null){
            routinesdb.close();
        }
    }

    //3 ，删除计划项
    public void deleteSchedule(int s_id){
        SQLiteDatabase routinesdb = getRoutinedatebases(context);

        Cursor process = routinesdb.query("process", new String[]{"process_id"}, "s_id=?", new String[]{s_id + ""}, null, null, null);
        while (process.moveToNext()){
            int anInt = process.getInt(0);
            routinesdb.delete("details","p_id=? and s_id=?",new String[]{anInt+"",s_id+""});
        }
        routinesdb.delete("process","s_id=?",new String[]{s_id+""});
        routinesdb.delete("schedule","schedule_id=?",new String[]{s_id+""});
        if(routinesdb!=null){
            routinesdb.close();
        }
    }
    //4 删除一天的事件,固定某个计划的某一天
    public void deleteprocessItem(int s_id,int p_id){
        SQLiteDatabase routinesdb = getRoutinedatebases(context);

        routinesdb.execSQL("delete from details where p_id="+p_id+" and s_id="+s_id+";");
        if(routinesdb!=null){
            routinesdb.close();
        }
    }
    //5 删除事件，由于事件主键设置，所以不依赖其他id
    public void deleteDetails(int detail_id){
        SQLiteDatabase routinesdb = getRoutinedatebases(context);
        routinesdb.execSQL("delete from details where detail_id="+detail_id+";");
        if(routinesdb!=null){
            routinesdb.close();
        }
    }
    //6 修改计划名
    public void updateSchedulename(int s_id,String title){
        SQLiteDatabase routinesdb = getRoutinedatebases(context);
        ContentValues values=new ContentValues();
        values.put("schedule_title",title);
        routinesdb.update("schedule",values,"schedule_id=?",new String[]{s_id+""});
        if(routinesdb!=null){
            routinesdb.close();
        }
    }
    //7 修改事件信息
    public void updateDetailsInfo(int datail_id,int iconRid,String icon_name,String detail_start,String detail_end,String detail_comment,
                                  String detail_ringbefore,String detail_ringpath,int detail_vabrate){
        SQLiteDatabase routinesdb = getRoutinedatebases(context);
        ContentValues values=new ContentValues();
        values.put("iconRid",iconRid);
        values.put("icon_name",icon_name);
        values.put("detail_start",detail_start);
        values.put("detail_end",detail_end);
        values.put("detail_comment",detail_comment);
        values.put("detail_ringbefore",detail_ringbefore);
        values.put("detail_ringpath",detail_ringpath);
        values.put("detail_vabrate",detail_vabrate);
        routinesdb.update("details",values,"detail_id=?",new String[]{datail_id+""});
        if(routinesdb!=null){
            routinesdb.close();
        }

    }
    //8 对单一计划切换active的页面（某天），修改schedule数据库的process，修改process数据库中对应天数的内容
    public void changeCurrentday(RoutinesInfo info,int schedule_process){
        SQLiteDatabase routinesdb = getRoutinedatebases(context);
        int schedule_id = info.getSchedule_id();
        int currentDay = info.getCurrentDay();
        boolean active = info.isActive();
        ContentValues values_s=new ContentValues();
        values_s.put("schedule_process",schedule_process);
        routinesdb.update("schedule",values_s,"schedule_id=?",new String[]{schedule_id+""});
        if(active){
            //修改process中的现在进行中的数据
            ContentValues values_pb=new ContentValues();
            Cursor process = routinesdb.query("process", new String[]{"process_time"}, "s_id=? and process_id=?", new String[]{schedule_id + "", schedule_process + ""}, null, null, null);
            if(process.moveToNext()){
                String processtime = process.getString(0);
                if((Long.parseLong(processtime)+10*60*1000l)>=24*60*60*1000l){
                    processtime="0";
                }
                values_pb.put("process_start", ReminderUtis.getCurrentdate());
                values_pb.put("process_active",SCHEDULE_ACTIVE);
                values_pb.put("process_time",processtime);
                routinesdb.update("process",values_pb,"s_id=? and process_id=?",new String[]{schedule_id+"",schedule_process+""});
            }
            process.close();

            //修改process之前进行中的数据，先取出之前的起始数据和进行时常，然后更换终止date，开启未进行状态
            Cursor cursor = routinesdb.rawQuery("select process_start,process_time from process where s_id=" + schedule_id + " and process_id=" + currentDay + ";", null);
            cursor.moveToNext();
            String start = cursor.getString(0);
            String time = cursor.getString(1);
            if((Long.parseLong(time)+10*60*1000l)>=24*60*60*1000l){
                time="0";
            }else {
                long l = System.currentTimeMillis() - ReminderUtis.stringFormaterTolong(start);
                if((Long.parseLong(time)+l)>=24*60*60*1000l){
                    time="0";
                }else {
                    time=(Long.parseLong(time)+l)+"";
                }
            }
            ContentValues values_pn=new ContentValues();
            values_pn.put("process_end",ReminderUtis.getCurrentdate());
            values_pn.put("process_time",time);
            values_pn.put("process_active",SCHEDULE_INACTIVE);
            routinesdb.update("process",values_pn,"s_id=? and process_id=?",new String[]{schedule_id+"",currentDay+""});
            cursor.close();
            if(routinesdb!=null){
                routinesdb.close();
            }
        }
        //如果不是active的就process中流不做任何修改
    }

    //9toggle-button滑动事件，当active的时候，就改变schedule中的active值，然后去当前正在进行的process，去修改终止时间和进行时间（最多只能24小时，超过重置）
    //以及inactive，当inactive的时候，改变当前process的起始时间和使其active
    public void updateByToggleBt(RoutinesInfo info){
        SQLiteDatabase routinesdb = getRoutinedatebases(context);
        int schedule_id = info.getSchedule_id();
        int currentDay = info.getCurrentDay();
        int schedule_cycles = info.getSchedule_cycles();
        boolean active = info.isActive();
        if(active){
            ContentValues values_s=new ContentValues();
            values_s.put("schedule_active",SCHEDULE_INACTIVE);
            routinesdb.update("schedule",values_s,"schedule_id=?",new String[]{schedule_id+""});
            Cursor cursor = routinesdb.rawQuery("select process_start,process_time from process where s_id=" + schedule_id + " and process_id=" + currentDay + ";", null);
            cursor.moveToNext();
            String start = cursor.getString(0);
            String time = cursor.getString(1);
            long l = System.currentTimeMillis() - ReminderUtis.stringFormaterTolong(start);
            if((Long.parseLong(time)+l)>=24*60*60*1000l){
                time="0";
            }else {
                time=(Long.parseLong(time)+l)+"";
            }
            ContentValues values_pn=new ContentValues();
            values_pn.put("process_end",ReminderUtis.getCurrentdate());
            values_pn.put("process_time",time);
            values_pn.put("process_active",SCHEDULE_INACTIVE);
            routinesdb.update("process",values_pn,"s_id=? and process_id=?",new String[]{schedule_id+"",currentDay+""});
            cursor.close();
        }else {
            ContentValues values_s=new ContentValues();
            values_s.put("schedule_active",SCHEDULE_ACTIVE);
            Cursor cursor = routinesdb.rawQuery("select process_time from process where s_id=" + schedule_id + " and process_id=" + currentDay + ";", null);
            cursor.moveToNext();
            String time = cursor.getString(0);
            long l = Long.parseLong(time);
            long daytime=24l*60l*60l*1000l;
            if(l>=daytime-60000){//当时间接近24小时时，打开的话，就要切换到下个页面，如果当前面是最后一个页面，就只能跳到第一个页面
                int newprocessday=0;
                if(currentDay==schedule_cycles){
                    newprocessday=1;
                }else {
                    newprocessday=currentDay+1;
                }
                values_s.put("schedule_process",newprocessday);
                ContentValues values_pn=new ContentValues();
                values_pn.put("process_start", ReminderUtis.getCurrentdate());
                values_pn.put("process_active",SCHEDULE_ACTIVE);
                values_pn.put("process_time","0");
                routinesdb.update("process",values_pn,"s_id=? and process_id=?",new String[]{schedule_id+"",newprocessday+""});
            }else {
                ContentValues values_pb=new ContentValues();
                values_pb.put("process_start", ReminderUtis.getCurrentdate());
                values_pb.put("process_active",SCHEDULE_ACTIVE);
                routinesdb.update("process",values_pb,"s_id=? and process_id=?",new String[]{schedule_id+"",currentDay+""});
            }
            routinesdb.update("schedule",values_s,"schedule_id=?",new String[]{schedule_id+""});
        }
        if(routinesdb!=null){
            routinesdb.close();
        }
    }
    //10查询，所有的计划
    public List<RoutinesInfo> getallSchedule(){
        SQLiteDatabase routinesdb = getRoutinedatebases(context);
        List<RoutinesInfo> routinesInfos=new ArrayList<>();
        Cursor schedule = routinesdb.query("schedule", null, null, null, null, null, null);
        while (schedule.moveToNext()){
            int id = schedule.getInt(0);
            String title = schedule.getString(1);
            int cycles = schedule.getInt(2);
            int active = schedule.getInt(3);
            int process = schedule.getInt(4);
            boolean active_s=false;
            if(active==SCHEDULE_ACTIVE){
                active_s=true;
            }
            RoutinesInfo info=new RoutinesInfo(id,title,cycles,process,active_s);
            routinesInfos.add(info);
        }
        schedule.close();
        if(routinesdb!=null){
            routinesdb.close();
        }
        return routinesInfos;
    }
    //10-2根据title查询一个routinesInfo
    public RoutinesInfo getScheduleByTitle(String title){
        SQLiteDatabase routinesdb = getRoutinedatebases(context);
        Cursor cursor = routinesdb.rawQuery("select * from schedule where schedule_title='" + title + "';", null);
        cursor.moveToNext();
        int s_id = cursor.getInt(0);
        int cycles = cursor.getInt(2);
        int schedule_active = cursor.getInt(3);
        int process = cursor.getInt(4);
        cursor.close();
        if(routinesdb!=null){
            routinesdb.close();
        }
        if(schedule_active==SCHEDULE_ACTIVE){
            return new RoutinesInfo(s_id,title,cycles,process,true);
        }
        return new RoutinesInfo(s_id,title,cycles,process,false);
    }

    //11查询某个计划的process
    /*
    * start和end为datetime格式 yy-mm-dd hh-MM-ss
    * time为ms
    * */
    public List<RoutinesProcessInfo> getallprocessByschedule(int schedule_id){
        SQLiteDatabase routinesdb = getRoutinedatebases(context);
        List<RoutinesProcessInfo> routinesProcessInfos=new ArrayList<>();
        Cursor process = routinesdb.query("process", null, "s_id=?", new String[]{schedule_id+""}, null, null, null);
        while (process.moveToNext()){
            int process_id = process.getInt(0);
            int s_id = process.getInt(1);
            int active = process.getInt(2);
            String start = process.getString(3);
            String end = process.getString(4);
            String time = process.getString(5);
            boolean active_s=false;
            if(active==SCHEDULE_ACTIVE){
                active_s=true;
            }
            RoutinesProcessInfo info=new RoutinesProcessInfo(process_id,s_id,active_s,start,end,time);
            routinesProcessInfos.add(info);
        }
        process.close();
        if(routinesdb!=null){
            routinesdb.close();
        }
        return routinesProcessInfos;
    }
    //12 查询某天所有的计划事件
    /*
    * end,start 为时间 格式为hh-mm-ss
    * ring_before 为提醒前ms
    * vabrate为震动或不震动
    * */
    public static final int VABRATE=0;
    public static final int UNVABRATE=1;

    public List<RoutinesDetailInfo> getallDetailsByday(int process_day,int schedule_id){
        SQLiteDatabase routinesdb = getRoutinedatebases(context);
        List<RoutinesDetailInfo> routinesDetailInfos=new ArrayList<>();
        Cursor details = routinesdb.query("details", null, "s_id=? and p_id=?", new String[]{schedule_id+"",process_day+""}, null, null, "detail_start desc");
        while(details.moveToNext()){
            int detail_id = details.getInt(0);
            int p_id = details.getInt(1);
            int s_id = details.getInt(2);
            int iconRid = details.getInt(3);
            String icon_name = details.getString(4);
            String start = details.getString(5);
            String end = details.getString(6);
            String detail_comment = details.getString(7);
            String detail_ringbefore = details.getString(8);
            String ring_path = details.getString(9);
            int vabrate = details.getInt(10);
            RoutinesDetailInfo routinesDetailInfo=new RoutinesDetailInfo(detail_id,p_id,s_id,iconRid,icon_name,start,end,detail_comment,
                    detail_ringbefore,ring_path,vabrate);
            routinesDetailInfos.add(routinesDetailInfo);
        }
        details.close();
        if(routinesdb!=null){
            routinesdb.close();
        }
        return routinesDetailInfos;
    }
    //13查询所有计划名称，未防止有重复title提供接口
    public List<String> getAllTitle(){
        SQLiteDatabase routinesdb = getRoutinedatebases(context);
        List<String> list=new ArrayList<>();
        Cursor title = routinesdb.query("schedule", new String[]{"schedule_title"}, null, null, null, null, null);
        while (title.moveToNext()){
            String titlestring = title.getString(0);
            list.add(titlestring);
        }
        title.close();
        if(routinesdb!=null){
            routinesdb.close();
        }
        return list;
    }
    //13-2 根据s_id 获取计划名
    public String getTitileById(int s_id){
        SQLiteDatabase routinesdb = getRoutinedatebases(context);
        Cursor cursor = routinesdb.rawQuery("select schedule_title from schedule where schedule_id=" + s_id + ";", null);
        cursor.moveToNext();
        return cursor.getString(0);
    }
    //13-3根据s_id查询cycle
    public int getCycles(int s_id){
        SQLiteDatabase routinesdb = getRoutinedatebases(context);
        Cursor cursor = routinesdb.rawQuery("select schedule_cycles from schedule where schedule_id=" + s_id + ";", null);
        cursor.moveToNext();
        return cursor.getInt(0);
    }
    //13-4根据s_id和当前p_id，来确定clone数组
    public String[] getClonedaylist(int s_id,int currentp_id){
        int cycles = getCycles(s_id);
        String[] clonelist=new String[cycles-1];
        int flag=1;
        for(int i=0;i<clonelist.length;i++){
            if(currentp_id==(i+1)){
                flag++;
                clonelist[i]="day"+flag;
                flag++;
                continue;
            }
           clonelist[i]="day"+flag;
            flag++;
        }
        return clonelist;
    }
    //14clone 计划,相当于新创建一个名字不同但天数和每天事件相同的计划，天数里面的时间重置为0
    public void cloneSchedule(RoutinesInfo routinesInfo,String new_title){
        SQLiteDatabase routinesdb = getRoutinedatebases(context);
        int schedule_id = routinesInfo.getSchedule_id();
        int schedule_cycles = routinesInfo.getSchedule_cycles();
        insertSchedule(new_title,schedule_cycles);
        Cursor schedule = routinesdb.query("schedule", new String[]{"schedule_id"}, "schedule_title=?", new String[]{new_title}, null, null, null);
        schedule.moveToNext();
        int new_id_s = schedule.getInt(0);
        List<RoutinesProcessInfo> routinesProcessInfos = getallprocessByschedule(schedule_id);
        for (RoutinesProcessInfo info_p:routinesProcessInfos){
            int process_id = info_p.getProcess_id();
            List<RoutinesDetailInfo> routinesDetailInfos = getallDetailsByday(process_id, schedule_id);
            for (RoutinesDetailInfo info_d:routinesDetailInfos){
                insertDetails(info_d.getP_id(),new_id_s,info_d.getIconRid(),info_d.getIcon_name(),info_d.getDetail_start()
                ,info_d.getDetail_end(),info_d.getDetail_comment(),info_d.getDetail_ringbefore(),info_d.getDetail_ringpath(),info_d.getDetail_vabrate());
            }
        }
        schedule.close();
        if(routinesdb!=null){
            routinesdb.close();
        }
    }
    //15 clone 一天事件to另一天（单一计划）

    public void cloneallDetailstoOther(int s_id,int process_id,int tagprocess){
        SQLiteDatabase routinesdb = getRoutinedatebases(context);

        //删除目标的所有detail事件
        routinesdb.delete("details","s_id=? and p_id=?",new String[]{s_id+"",tagprocess+""});
        List<RoutinesDetailInfo> routinesDetailInfos = getallDetailsByday(process_id, s_id);
        for(RoutinesDetailInfo info_d:routinesDetailInfos){
            insertDetails(tagprocess,s_id,info_d.getIconRid(),info_d.getIcon_name(),info_d.getDetail_start()
                    ,info_d.getDetail_end(),info_d.getDetail_comment(),info_d.getDetail_ringbefore(),info_d.getDetail_ringpath(),info_d.getDetail_vabrate());
        }
        if(routinesdb!=null){
            routinesdb.close();
        }
    }
    //16 clone某件事情to另一天（单一计划）
    public void cloneDetailtoOther(RoutinesDetailInfo info_d,int tagprocess){
        insertDetails(tagprocess,info_d.getS_id(),info_d.getIconRid(),info_d.getIcon_name(),info_d.getDetail_start()
                ,info_d.getDetail_end(),info_d.getDetail_comment(),info_d.getDetail_ringbefore(),info_d.getDetail_ringpath(),info_d.getDetail_vabrate());
    }

    //17添加查询计划名称，为用户输入名称时不可重复提供
    public boolean queryTitleExist(String title){
        SQLiteDatabase routinesdb = getRoutinedatebases(context);
        Cursor cursor = routinesdb.rawQuery("select * from schedule where schedule_title='" + title + "';", null);
        if(cursor.moveToNext()){
            return true;
        }
        cursor.close();
        if(routinesdb!=null){
            routinesdb.close();
        }
        return false;
    }
    //18 查询当前需要震动的details,顺便更新active时间接近24小时de
    public List<RoutinesDetailInfo> getRinglist(){
        SQLiteDatabase routinesdb = getRoutinedatebases(context);
        List<RoutinesDetailInfo> remindelist=new ArrayList<>();
        Cursor process = routinesdb.query("process", null, null, null, null, null, null);
        while (process.moveToNext()){
            int p_id = process.getInt(0);
            int s_id = process.getInt(1);
            int active = process.getInt(2);
            String start = process.getString(3);
            long l_start = ReminderUtis.stringFormaterTolong(start);
            long l = System.currentTimeMillis();
            if(active==SCHEDULE_ACTIVE){
                if(((l-l_start)>=(24*60*60*1000l-2*60000l))&&((l-l_start)<=(24*60*60*1000l))){//随时间更新数据库数据
                    Cursor cursor = routinesdb.rawQuery("select schedule_cycles from schedule where schedule_id=" + p_id + ";", null);
                    cursor.moveToNext();
                    int cycles = cursor.getInt(0);
                    int tagprocess=p_id+1;
                    if(p_id==cycles){
                        tagprocess=1;
                    }
                    cursor.close();
                    routinesdb.execSQL("update schedule set schedule_process="+tagprocess+";");//schedule表更新
                    ContentValues values=new ContentValues();
                    values.put("process_active",SCHEDULE_INACTIVE);
                    values.put("process_end",ReminderUtis.longFormaterTostring(l));
                    routinesdb.update("process",values,"s_id=? and process_id=?",new String[]{s_id+"",p_id+""});//旧process更新
                    ContentValues values2=new ContentValues();//新process更新
                    values2.put("process_active",SCHEDULE_ACTIVE);
                    values2.put("process_start",ReminderUtis.longFormaterTostring(l));
                    values2.put("process_time","0");
                    routinesdb.update("process",values2,"s_id=? and process_id=?",new String[]{s_id+"",tagprocess+""});
                }

                //统计需提醒项
                List<RoutinesDetailInfo> routinesDetailInfos = getallDetailsByday(p_id, s_id);
                for (RoutinesDetailInfo info_d:routinesDetailInfos){
                    String detail_start = info_d.getDetail_start();
                    String detail_ringbefore = info_d.getDetail_ringbefore();
                    long before = Long.parseLong(detail_ringbefore);
                    String ringstart=start.substring(0,start.indexOf(" "));
                    long ringstart_l = ReminderUtis.stringFormaterTolong(ringstart+" "+detail_start);
                    if(((ringstart_l+10000l-before)>=System.currentTimeMillis())&&((ringstart_l-before)<=System.currentTimeMillis())){
                        remindelist.add(info_d);
                    }
                }
            }
        }
        if(routinesdb!=null){
            routinesdb.close();
        }
        return remindelist;
    }
}
